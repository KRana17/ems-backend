package com.example.ems.controller;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@example.com");
    }

    @Test
    void testCreateEmployee() throws Exception {
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(employeeDto);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(employeeDto);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setId(2L);
        employeeDto2.setFirstName("Jane");
        employeeDto2.setLastName("Smith");
        employeeDto2.setEmail("jane.smith@example.com");

        List<EmployeeDto> employees = Arrays.asList(employeeDto, employeeDto2);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setId(1L);
        updatedEmployee.setFirstName("John");
        updatedEmployee.setLastName("Updated");
        updatedEmployee.setEmail("john.updated@example.com");

        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully!."));
    }
}