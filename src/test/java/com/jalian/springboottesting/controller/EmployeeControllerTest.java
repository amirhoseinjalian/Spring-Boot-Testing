package com.jalian.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalian.springboottesting.model.Employee;
import com.jalian.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEmployeeTest() throws Exception {
        Employee employee = Employee.builder()
                .firstName("amirhosein")
                .lastName("jalian")
                .email("aj@gmail.com")
                .build();

        given(employeeService.save(any(Employee.class))).willAnswer(
                (invocationOnMock -> invocationOnMock.getArgument(0)));
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
        given(employeeService.findAll()).willReturn(employees);
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
        given(employeeService.findById(1L)).willReturn(Optional.of(employee));
        ResultActions resultActions = mockMvc.perform(get("/api/employees/{id}", 1L));
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
        given(employeeService.findById(1L)).willReturn(Optional.empty());
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
        given(employeeService.findById(1L)).willReturn(Optional.of(savedEmployee));
        given(employeeService.update(any(Employee.class))).will(invocationOnMock -> invocationOnMock.getArgument(0));
        ResultActions resultActions = mockMvc.perform(put("/api/employees/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
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
        given(employeeService.findById(1L)).willReturn(Optional.empty());
        given(employeeService.update(any(Employee.class))).will(invocationOnMock -> invocationOnMock.getArgument(0));
        ResultActions resultActions = mockMvc.perform(put("/api/employees/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployeeTest() throws Exception {
        willDoNothing().given(employeeService).delete(1L);
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", 1L));
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
