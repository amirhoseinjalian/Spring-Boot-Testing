package com.jalian.springboottesting.service;

import com.jalian.springboottesting.exception.ResourceNotFoundException;
import com.jalian.springboottesting.model.Employee;
import com.jalian.springboottesting.repository.EmployeeRepository;
import com.jalian.springboottesting.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();
    }

    @Test
    void saveEmployeeTest() {
        given(employeeRepository.save(employee)).willReturn(employee);
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(null);
        Employee savedEmployee = employeeService.save(employee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isEqualTo(employee);
    }

    @Test
    void saveEmployeeTestWhichThrowsException() {
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(employee);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.save(employee);
        });
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void findAllEmployeeTest() {
        List<Employee> employees = new ArrayList<>();
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("rihanna")
                .lastName("fenty")
                .email("robyn@gmail.com")
                .build();

        Employee employee3 = Employee.builder()
                .id(3L)
                .firstName("taylor")
                .lastName("swift")
                .email("ts@gmail.com")
                .build();
        employees.add(employee);
        employees.add(employee2);
        employees.add(employee3);
        given(employeeRepository.findAll()).willReturn(employees);

        List<Employee> savedEmployees = employeeService.findAll();
        assertThat(savedEmployees).isNotNull();
        assertThat(savedEmployees).isEqualTo(employees);
        assertThat(savedEmployees.size()).isEqualTo(3);
    }

    @Test
    void findByIdEmployeeTest() {
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        Optional<Employee> foundedEmployee = employeeService.findById(employee.getId());
        assertThat(foundedEmployee).isPresent();
        assertThat(foundedEmployee.get()).isEqualTo(employee);
    }

    @Test
    void updateEmployeeTest() {
        employee.setFirstName("amirhosein2");
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(employee);
        Employee updatedEmployee = employeeService.update(employee);
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }

    @Test
    void updateEmployeeTestWhichThrowsException() {
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> {
            employeeService.update(employee);
        });
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployeeTest() {
        willDoNothing().given(employeeRepository).deleteById(employee.getId());
        employeeService.delete(employee.getId());
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }
}
