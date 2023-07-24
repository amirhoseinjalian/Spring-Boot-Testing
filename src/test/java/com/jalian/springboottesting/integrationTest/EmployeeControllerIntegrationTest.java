package com.jalian.springboottesting.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalian.springboottesting.model.Employee;
import com.jalian.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void createEmployeeTest() throws Exception {
        Employee employee = Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();
        ResultActions response = mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    void findAllEmployeeTest() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build());
        employees.add(Employee.builder()
                .firstName("robyn")
                .lastName("fenty")
                .email("rihanna@gmail.com")
                .build());
        employeeRepository.saveAll(employees);
        ResultActions resultActions = mockMvc.perform(get("/api/employees"));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employees.size())))
                .andDo(print());
    }

    @Test
    void findByIdEmployeeTest() throws Exception {
        Employee employee = Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();
        employeeRepository.save(employee);
        ResultActions resultActions = mockMvc.perform(get("/api/employees/{id}", employee.getId()));
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void findByIdEmployeeTestThrowsException() throws Exception {
        Employee employee = Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();
        employeeRepository.save(employee);
        ResultActions resultActions = mockMvc.perform(get("/api/employees/{id}", 1L));
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateEmployeeTest() throws Exception {
        Employee savedEmployee = Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("amirhosein2")
                .lastName("jalian")
                .email("aj2@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        ResultActions resultActions = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    void updateEmployeeTestThrowsException() throws Exception {
        Employee savedEmployee = Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("amirhosein2")
                .lastName("jalian")
                .email("aj2@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        ResultActions resultActions = mockMvc.perform(put("/api/employees/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployeeTest() throws Exception {
        Employee savedEmployee = Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
