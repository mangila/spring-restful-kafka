package com.github.mangila.consumer.web;

import com.github.mangila.common.Employee;
import com.github.mangila.consumer.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping
    public ResponseEntity<Collection<Employee>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
