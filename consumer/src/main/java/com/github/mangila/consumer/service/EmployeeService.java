package com.github.mangila.consumer.service;

import com.github.mangila.common.Employee;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeService {

    private final ConcurrentHashMap<String, Employee> map;

    public Collection<Employee> findAll() {
        return map.values();
    }

    public Employee findById(String id) {
        var employee = map.get(id);
        if (Objects.nonNull(employee)) {
            return employee;
        }
        log.warn("Employee with id: '" + id + "' does not exist!");
        throw new DataAccessResourceFailureException("Employee with id: '" + id + "' does not exist!");
    }

    public void update(Employee employee) {
        if (map.containsKey(employee.getId())) {
            map.replace(employee.getId(), employee);
            log.info("EmployeeService.update " + employee + " was updated!");
        } else {
            insert(employee);
        }
    }

    public void delete(Employee employee) {
        map.remove(employee.getId());
        log.info("EmployeeService.delete " + employee + " was deleted!");
    }

    public void insert(Employee employee) {
        map.put(employee.getId(), employee);
        log.info("EmployeeService.insert " + employee + " was inserted!");
    }
}
