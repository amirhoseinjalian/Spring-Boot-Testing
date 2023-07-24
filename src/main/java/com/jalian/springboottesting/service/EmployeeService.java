package com.jalian.springboottesting.service;

import com.jalian.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee save(Employee employee);

    List<Employee> findAll();

    Employee update(Employee employee);

    Optional<Employee> findById(Long id);

    void delete(Long id);
}
