package com.jalian.springboottesting.repository;

import com.jalian.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();
    }

    @Test
    void saveEmployeeTest() {
        //BDD, given when then
        Employee savedEmployee = employeeRepository.save(employee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isEqualTo(employee);
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    void findAllEmployeesTest() {
        Employee employee2 = Employee.builder()
                .firstName("rihanna")
                .lastName("fenty")
                .email("robynFenty@gmail.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee2);
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    void findByIdEmployeeTest() {
        employeeRepository.save(employee);
        Employee founded = employeeRepository.findById(employee.getId()).get();
        assertThat(founded).isNotNull();
    }

    @Test
    void findByEmailEmployeeTest() {
        employeeRepository.save(employee);
        Employee foundedByEmail = employeeRepository.findByEmail(employee.getEmail());
        assertThat(foundedByEmail).isNotNull();
        assertThat(foundedByEmail.getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void updateEmployeeTest() {
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("amirhosein2");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
    }

    @Test
    void deleteEmployeeTest() {
        employeeRepository.save(employee);
        employeeRepository.delete(employee);
        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());
        assertThat(deletedEmployee).isEmpty();
    }

    @Test
    void findByFirstNameAndLastNameEmployeeTest() {
        employeeRepository.save(employee);
        Employee foundedByFirstNameAndLastName = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
        assertThat(foundedByFirstNameAndLastName).isNotNull();
        assertThat(foundedByFirstNameAndLastName.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(foundedByFirstNameAndLastName.getLastName()).isEqualTo(employee.getLastName());
    }
}
