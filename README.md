# TCC

## Event Sourcing to bank transfer system

[@Gabriel Passos](https://github.com/gabrielSpassos)
[@Thomas Soares](https://github.com/thomas-soares)

### Usage

* Install [docker](https://docs.docker.com/install/) and [compose](https://docs.docker.com/compose/install/)
* Run the docker-compose from this repository
```
docker-compose up
```
* Check the local [RabbitMQ](http://localhost:15672/), username and password are "admin"
* Connect to MySQL database with this url: jdbc:mysql://localhost:3306/BANK, username: "root" and password: "password"
* Run this [sql scripts](https://github.com/gabrielSpassos/tcc/blob/dev/database.sql) at BANK schema
* Check how to start [transfer-producer](https://github.com/gabrielSpassos/tcc/blob/dev/transfer-producer/README.md)
* Check how to start [transfer-consumer](https://github.com/gabrielSpassos/tcc/blob/dev/transfer-consumer/README.md)
