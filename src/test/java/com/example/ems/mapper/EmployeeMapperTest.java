package com.example.ems.mapper;

import com.example.ems.dto.DepartmentDto;
import com.example.ems.dto.EmployeeDto;
import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeMapperTest {

    @Test
    void testMapToEmployeeDto() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");

        EmployeeDto result = EmployeeMapper.mapToEmployeeDto(employee);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testMapToEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@example.com");

        Employee result = EmployeeMapper.mapToEmployee(employeeDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
    }
}

class DepartmentMapperTest {

    @Test
    void testMapToDepartmentDto() {
        Department department = new Department();
        department.setId(1L);
        department.setDepartmentName("IT");
        department.setDepartmentDescription("Information Technology");

        DepartmentDto result = DepartmentMapper.mapToDepartmentDto(department);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepartmentName()).isEqualTo("IT");
        assertThat(result.getDepartmentDescription()).isEqualTo("Information Technology");
    }

    @Test
    void testMapToDepartment() {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(1L);
        departmentDto.setDepartmentName("IT");
        departmentDto.setDepartmentDescription("Information Technology");

        Department result = DepartmentMapper.mapToDepartment(departmentDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepartmentName()).isEqualTo("IT");
        assertThat(result.getDepartmentDescription()).isEqualTo("Information Technology");
    }
}