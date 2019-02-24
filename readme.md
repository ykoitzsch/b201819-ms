# b201819-ms-event-sourcing

This is an e-commerce application with a microservice-architecture and the event-sourcing pattern implemented.
There are a total of 5 microservices which are: 

  - Customer Service
  - Rating Service
  - Inventory Service
  - Order Service
  - Invoice Service

### Requirements
 - Java 1.8+
 - NPM (version used in development is 6.4.1) (optional)
 - docker-compose (tested with v1.2.3)
 - docker (tested with v18.09)
 
### Installation

First clone the JHipster Registry from their repository https://github.com/jhipster/jhipster-registry
```sh
$ git clone https://github.com/jhipster/jhipster-registry.git
```

Start the registry
```sh
$ cd jhipster-registry
$ mvnw (or ./mvnw with linux)
```
This application is using Kafka as an event store. Therefore kafka and docker has to be started. After docker is running you can run kafka with:
```sh
$ docker-compose -f kafka.yml up
```
(it is rare but it can happen that starting kafka fails, then just try again until there is no error message )

After the JHipster Registry is running you can start the API Gateway and the microservices. It is recommended to start the API Gateway first.
```sh
$ cd shop
$ mvnw (or ./mvnw with linux)
$ npm start (optional)
```

Start the microservices
```sh
$ cd customers/orders/ratings/invoices/inventory
$ mvnw (or ./mvnw with linux)
```

### Setup
(Admin)
 - Once the application is running you can access it at http://localhost:8080 or http://localhost:9000. The latter if you used npm start.
 - Login as admin/admin
 - Create product categories
 - Create products and link them to categories
 
(Customer)
- Register
- Login (only possible after admin has activated the account)
- Shop!


