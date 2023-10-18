FROM node:lts-alpine AS buildfrontend

WORKDIR /dist/src/app

COPY ./frontend/package.json ./frontend/package-lock.json ./

RUN npm cache clean --force

COPY ./frontend .

RUN npm install -g @angular/cli@latest

RUN npm install

RUN npm run build:production

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

COPY ./backend /application

COPY --from=buildfrontend /dist/src/app/dist/expensecontrol /application/src/main/resources/static

COPY ./entrypoint_dockerfile.sh /application/entrypoint_dockerfile.sh

EXPOSE 8080

CMD /application/entrypoint_dockerfile.sh