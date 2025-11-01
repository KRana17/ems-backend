package com.example.ems.impl;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.entity.Employee;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.repository.EmployeeRepository;
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
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");

        employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@example.com");
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.createEmployee(employeeDto);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDto result = employeeService.getEmployeeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee is not exists with given id : 1");
    }

    @Test
    void testGetAllEmployees() {
        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmail("jane.smith@example.com");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee, employee2));

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(1).getFirstName()).isEqualTo("Jane");
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testUpdateEmployee() {
        EmployeeDto updatedDto = new EmployeeDto();
        updatedDto.setId(1L);
        updatedDto.setFirstName("John");
        updatedDto.setLastName("Updated");
        updatedDto.setEmail("john.updated@example.com");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setFirstName("John");
        updatedEmployee.setLastName("Updated");
        updatedEmployee.setEmail("john.updated@example.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        EmployeeDto result = employeeService.updateEmployee(1L, updatedDto);

        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("Updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployeeNotFound() {
        EmployeeDto updatedDto = new EmployeeDto();
        updatedDto.setId(1L);
        updatedDto.setFirstName("John");
        updatedDto.setLastName("Updated");
        updatedDto.setEmail("john.updated@example.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(1L, updatedDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee is not exists with given id: 1");
    }

    @Test
    void testDeleteEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee is not exists with given id: 1");
    }
}