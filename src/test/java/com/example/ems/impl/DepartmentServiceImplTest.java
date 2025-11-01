package com.example.ems.impl;

import com.example.ems.dto.DepartmentDto;
import com.example.ems.entity.Department;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentDto departmentDto;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setDepartmentName("IT");
        department.setDepartmentDescription("Information Technology");

        departmentDto = new DepartmentDto();
        departmentDto.setId(1L);
        departmentDto.setDepartmentName("IT");
        departmentDto.setDepartmentDescription("Information Technology");
    }

    @Test
    void testCreateDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentDto result = departmentService.createDepartment(departmentDto);

        assertThat(result).isNotNull();
        assertThat(result.getDepartmentName()).isEqualTo("IT");
        assertThat(result.getDepartmentDescription()).isEqualTo("Information Technology");
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testGetDepartmentById() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        DepartmentDto result = departmentService.getDepartmentById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepartmentName()).isEqualTo("IT");
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDepartmentByIdNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getDepartmentById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department is not exists with a given id: 1");
    }

    @Test
    void testGetAllDepartments() {
        Department department2 = new Department();
        department2.setId(2L);
        department2.setDepartmentName("HR");
        department2.setDepartmentDescription("Human Resources");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department, department2));

        List<DepartmentDto> result = departmentService.getAllDepartments();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDepartmentName()).isEqualTo("IT");
        assertThat(result.get(1).getDepartmentName()).isEqualTo("HR");
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testUpdateDepartment() {
        DepartmentDto updatedDto = new DepartmentDto();
        updatedDto.setId(1L);
        updatedDto.setDepartmentName("IT");
        updatedDto.setDepartmentDescription("Updated Description");

        Department updatedDepartment = new Department();
        updatedDepartment.setId(1L);
        updatedDepartment.setDepartmentName("IT");
        updatedDepartment.setDepartmentDescription("Updated Description");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(updatedDepartment);

        DepartmentDto result = departmentService.updateDepartment(1L, updatedDto);

        assertThat(result).isNotNull();
        assertThat(result.getDepartmentDescription()).isEqualTo("Updated Description");
        verify(departmentRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testUpdateDepartmentNotFound() {
        DepartmentDto updatedDto = new DepartmentDto();
        updatedDto.setId(1L);
        updatedDto.setDepartmentName("IT");
        updatedDto.setDepartmentDescription("Updated Description");

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.updateDepartment(1L, updatedDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department is not exists with a given id:1");
    }

    @Test
    void testDeleteDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDepartmentNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.deleteDepartment(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department is not exists with a given id: 1");
    }
}