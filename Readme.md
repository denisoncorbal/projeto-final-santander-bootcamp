# About
An basic expense control app presented as final project of santander bootcamp by Digital Innovation One.
## Deploy
Deploy from the fullstack application can be tested on this [site](https://expensecontrol-omptr9n0.b4a.run).\
The deploy was made on the [Back4App](https://back4app.com) platform.
## Stack
- Frontend: Angular
- Backend: Spring Boot
## Features
- [x] Add transactions divided by income and outcome
- [x] History from transactions with balance and separated by income and outcome
- [x] Login and create and display data by user
- [x] Add class to categorize transactions
## Enhacements
- [ ] Improve precision on money representation
- [ ] Add history by class
- [ ] Improve security (sanitize inputs, implements jwt token)
- [ ] Add error treatment on frontend and backend
- [ ] Add tests
- [ ] Improve performance (sql queries, page loading)
- [ ] Add backend api documentation with swagger
- [ ] Add frontend documentation (jsdoc maybe)
- [ ] Improve design
- [ ] Improve responsiveness
## Changelog
This software uses [Conventional Commits](https://www.conventionalcommits.org/) and [Semantic Versioning](https://semver.org/).\
The changelog is auto generated with [Commit-And-Tag-Version](https://www.npmjs.com/package/commit-and-tag-version) lib.\
You can check the changelog [here](https://github.com/denisoncorbal/projeto-final-santander-bootcamp/blob/main/CHANGELOG.md).
## UML
Some diagrams used to design the software.
### Class diagram (backend)
``` mermaid
---
title: ExpenseControl
---
classDiagram
direction LR
    RegisterUser "1" *-- "0..n" Register
    RegisterUser "1" *-- "0..n" RegisterClass
    Register "0..n" o-- "1" RegisterClass
    class Register{
      -Long id
      -LocalDate date
      -BigDecimal registerValue
    }
    class RegisterClass{
      -Long id
      -String name
    }
    class RegisterUser{
      -Long id
      -String email
      -String password
    }
```
### Entity relation diagram (backend)
``` mermaid
---
title: ExpenseControl
---
erDiagram
    REGISTER_USER ||--o{ REGISTER : has
    REGISTER_USER ||--o{ REGISTER_CLASS : has
    REGISTER }o..|| REGISTER_CLASS : has
    REGISTER{
      Long id PK
      LocalDate date
      BigDecimal registerValue
    }
    REGISTER_CLASS{
      Long id PK
      String name
      Long registerUserId fk
    }
    REGISTER_USER{
      Long id PK
      String email
      String password
      Long registerUserId fk
      Long registerClassId fk
    }
```
## Screens
Here you can see the look from the software pages.\
![add-class](https://github.com/denisoncorbal/projeto-final-santander-bootcamp/blob/main/frontend/.screens/add-class.png)
![add-transaction](https://github.com/denisoncorbal/projeto-final-santander-bootcamp/blob/main/frontend/.screens/add-transaction.png)
![income-history](https://github.com/denisoncorbal/projeto-final-santander-bootcamp/blob/main/frontend/.screens/income-history.png)
![login](https://github.com/denisoncorbal/projeto-final-santander-bootcamp/blob/main/frontend/.screens/login.png)
![outcome-history](https://github.com/denisoncorbal/projeto-final-santander-bootcamp/blob/main/frontend/.screens/outcome-history.png)
![transaction-history](https://github.com/denisoncorbal/projeto-final-santander-bootcamp/blob/main/frontend/.screens/transaction-history.png)
