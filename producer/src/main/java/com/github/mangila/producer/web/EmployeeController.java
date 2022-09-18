package com.github.mangila.producer.web;

import com.github.mangila.common.Employee;
import com.github.mangila.producer.kafka.EmployeeProducer;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeProducer service;

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<String> insert(@RequestBody Employee employee) {
        service.insert(employee);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Employee employee) {
        service.update(employee);
        return ResponseEntity.ok().build();
    }

}
