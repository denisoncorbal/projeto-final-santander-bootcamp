FROM node:lts-alpine AS build

WORKDIR /dist/src/app

RUN npm cache clean --force

RUN npm install -g @angular/cli@latest

COPY package.json package-lock.json ./

RUN npm install

COPY . .

RUN npm run build:production

FROM nginx:latest

COPY --from=build /dist/src/app/dist/expensecontrol /usr/share/nginx/html
COPY /nginx.conf  /etc/nginx/conf.d/default.conf

EXPOSE 80