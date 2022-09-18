package com.github.mangila.consumer.kafka;

import com.github.mangila.common.Employee;
import com.github.mangila.common.KafkaHeaders;
import com.github.mangila.common.KafkaTopics;
import com.github.mangila.consumer.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmployeeListener {

    private final EmployeeService service;

    @KafkaListener(topics = KafkaTopics.EMPLOYEE_TOPIC)
    public void listen(
            @Payload Employee employee,
            @Header(KafkaHeaders.CONSUMER_OPERATION) String operation) {
        switch (operation) {
            case "update" -> service.update(employee);
            case "delete" -> service.delete(employee);
            case "insert" -> service.insert(employee);
            default -> log.warn("EmployeeListener.listen has a invalid CONSUMER_OPERATION header " + operation);
        }
        ;
    }
}
