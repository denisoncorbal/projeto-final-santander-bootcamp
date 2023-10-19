############## FRONTEND ######################################
FROM node:lts-alpine AS buildfrontend

WORKDIR /dist/src/app

COPY ./frontend/package.json ./frontend/package-lock.json ./

RUN npm cache clean --force

COPY ./frontend .

RUN npm install -g @angular/cli@latest

RUN npm install

RUN npm run build:back4app

#################### BACKEND #########################################################
FROM maven:3.9.4-eclipse-temurin-17-alpine as build

WORKDIR /workspace/app

COPY backend/pom.xml .
COPY backend/src src
COPY --from=buildfrontend /dist/src/app/dist/expensecontrol /workspace/app/src/main/resources/static
RUN mvn install -DskipTests

RUN java -Djarmode=layertools -jar target/expensecontrol.jar extract --destination target/extracted

FROM eclipse-temurin:17-jre-alpine as buildbackend
RUN apk --no-cache add curl
RUN addgroup -S demo && adduser -S demo -G demo
VOLUME /tmp
USER demo
ARG EXTRACTED=/workspace/app/target/extracted

WORKDIR /application
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./

###################### DEPLOY ##########################################################

FROM alpine:latest

ARG DEBIAN_FRONTEND=noninteractive

ARG PSQL_PWD

ARG PSQL_USR

ARG PSQL_DB

ENV PSQL_DB=$PSQL_DB

ENV PSQL_PWD=$PSQL_PWD

ENV PSQL_USR=$PSQL_USR

RUN apk cache clean

RUN apk update

RUN apk upgrade

RUN apk add wget

RUN mkdir -p /etc/apt/keyrings

RUN wget -O /etc/apk/keys/adoptium.rsa.pub https://packages.adoptium.net/artifactory/api/security/keypair/public/repositories/apk

RUN echo 'https://packages.adoptium.net/artifactory/apk/alpine/main' >> /etc/apk/repositories

RUN apk update

RUN apk upgrade

RUN apk add postgresql postgresql-contrib postgresql-client temurin-17-jdk bash runuser

RUN mkdir -p /var/lib/postgresql/data

RUN mkdir -p /run/postgresql/

RUN chown postgres -R /var/lib/postgresql/
RUN chown postgres -R /run/postgresql/

USER postgres

RUN initdb /var/lib/postgresql/data && \
    echo "host all  all    0.0.0.0/0  md5" >> /var/lib/postgresql/data/pg_hba.conf && \
    echo "listen_addresses='*'" >> /var/lib/postgresql/data/postgresql.conf && \
    pg_ctl start -D /var/lib/postgresql/data &&\
    psql -U postgres --command "CREATE USER ${PSQL_USR} WITH SUPERUSER PASSWORD '${PSQL_PWD}';" && \
    createdb -O ${PSQL_USR} ${PSQL_DB}

EXPOSE 5432

USER root

WORKDIR /application

COPY --from=buildbackend /application /application

EXPOSE 8080

CMD runuser -l postgres -c "pg_ctl -U $PSQL_USR -D /var/lib/postgresql/data restart";runuser -l root -c "cd /application && java -XX:TieredStopAtLevel=1 -Dspring.main.lazy-initialization=true -Dspring.datasource.url=jdbc:postgresql://localhost:5432/${PSQL_DB} -Dspring.datasource.username=${PSQL_USR} -Dspring.datasource.password=${PSQL_PWD} -Dspring.datasource.driver-class-name=org.postgresql.Driver org.springframework.boot.loader.JarLauncher"