# spring-restful-kafka
producer and consumer spring boot kafka C.R.U.D service in a maven multi module project.
Sample app with Kafka, Zookeeper and Testcontainers

## Swagger
* http://localhost:8081/swagger-ui/index.html - consumer
* http://localhost:8082/swagger-ui/index.html - producer

## Docker
* ``docker-compose up -d`` - spin up kafka,zookeeper,producer and consumer
* ``docker-compose -f docker-compose-dev.yml up -d`` - spin up kafka and zookeeper