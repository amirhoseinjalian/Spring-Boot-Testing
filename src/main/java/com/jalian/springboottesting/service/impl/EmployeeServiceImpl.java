package com.jalian.springboottesting.service.impl;

import com.jalian.springboottesting.exception.ResourceNotFoundException;
import com.jalian.springboottesting.model.Employee;
import com.jalian.springboottesting.repository.EmployeeRepository;
import com.jalian.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        Employee savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(savedEmployee != null) {
            throw new ResourceNotFoundException("the employee has already exists!!!");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee update(Employee employee) {
        if(employee.getId() == null || !employeeRepository.findById(employee.getId()).isPresent()) {
            throw new RuntimeException("the employee not found");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
