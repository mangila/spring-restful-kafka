#!/bin/sh
./mvnw clean package
docker build -t mangila/spring-restful-kafka-consumer ./consumer
docker build -t mangila/spring-restful-kafka-producer ./producer
docker push mangila/spring-restful-kafka-consumer
docker push mangila/spring-restful-kafka-producer