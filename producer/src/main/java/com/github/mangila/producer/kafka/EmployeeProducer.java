package com.github.mangila.producer.kafka;

import com.github.mangila.common.Employee;
import com.github.mangila.common.KafkaHeaders;
import com.github.mangila.common.KafkaTopics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeProducer {

    private final KafkaTemplate<String, Employee> kafka;

    public void delete(String id) {
        var employee = Employee.builder().id(id).build();
        var record = new ProducerRecord<>(KafkaTopics.EMPLOYEE_TOPIC, employee.getId(), employee);
        record.headers().add(KafkaHeaders.CONSUMER_OPERATION, "delete".getBytes());
        var f = kafka.send(record);
        f.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                ex.printStackTrace();
                log.warn("EmployeeService.delete - message failed!");
            }

            @Override
            public void onSuccess(SendResult<String, Employee> result) {
                log.info("EmployeeService.delete - message success!");
            }
        });
    }

    public void insert(Employee employee) {
        var record = new ProducerRecord<>(KafkaTopics.EMPLOYEE_TOPIC, employee.getId(), employee);
        record.headers().add(KafkaHeaders.CONSUMER_OPERATION, "insert".getBytes());
        var f = kafka.send(record);
        f.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                ex.printStackTrace();
                log.warn("EmployeeService.insert - message failed!");
            }

            @Override
            public void onSuccess(SendResult<String, Employee> result) {
                log.info("EmployeeService.insert - message success!");
            }
        });
    }

    public void update(Employee employee) {
        var record = new ProducerRecord<>(KafkaTopics.EMPLOYEE_TOPIC, employee.getId(), employee);
        record.headers().add(KafkaHeaders.CONSUMER_OPERATION, "update".getBytes());
        var f = kafka.send(record);
        f.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                ex.printStackTrace();
                log.warn("EmployeeService.update - message failed!");
            }

            @Override
            public void onSuccess(SendResult<String, Employee> result) {
                log.info("EmployeeService.update - message success!");
            }
        });
    }
}
