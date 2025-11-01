package com.example.ems.controller;

import com.example.ems.dto.DepartmentDto;
import com.example.ems.service.DepartmentService;
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

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private DepartmentDto departmentDto;

    @BeforeEach
    void setUp() {
        departmentDto = new DepartmentDto(1L, "IT", "Information Technology");
    }

    @Test
    void testCreateDepartment() throws Exception {
        when(departmentService.createDepartment(any(DepartmentDto.class))).thenReturn(departmentDto);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.departmentName").value("IT"))
                .andExpect(jsonPath("$.departmentDescription").value("Information Technology"));
    }

    @Test
    void testGetDepartmentById() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(departmentDto);

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.departmentName").value("IT"))
                .andExpect(jsonPath("$.departmentDescription").value("Information Technology"));
    }

    @Test
    void testGetAllDepartments() throws Exception {
        DepartmentDto departmentDto2 = new DepartmentDto(2L, "HR", "Human Resources");
        List<DepartmentDto> departments = Arrays.asList(departmentDto, departmentDto2);

        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].departmentName").value("IT"))
                .andExpect(jsonPath("$[1].departmentName").value("HR"));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        DepartmentDto updatedDepartment = new DepartmentDto(1L, "IT", "Updated Technology");

        when(departmentService.updateDepartment(eq(1L), any(DepartmentDto.class))).thenReturn(updatedDepartment);

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDepartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentDescription").value("Updated Technology"));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Department deleted successfully!"));
    }
}