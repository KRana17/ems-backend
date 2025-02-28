package com.example.ems.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger; // Import Logger
import org.slf4j.LoggerFactory; // Import LoggerFactory
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.service.EmployeeService;

import java.util.List;

@CrossOrigin("*")  // Be more specific in production!
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class); // Create a logger instance

    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        logger.info("Received request to create employee: {}", employeeDto); // Log the received data

        try {
            EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
            logger.info("Employee created successfully: {}", savedEmployee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) { // Catch potential exceptions
            logger.error("Error creating employee: ", e); // Log the exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Or return a more specific error response
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId) {
        logger.info("Received request to get employee by ID: {}", employeeId);

        try {
            EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
            logger.info("Employee found: {}", employeeDto);
            return ResponseEntity.ok(employeeDto);
        } catch (Exception e) {
            logger.error("Error getting employee by ID: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        logger.info("Received request to get all employees");

        try {
            List<EmployeeDto> employees = employeeService.getAllEmployees();
            logger.info("Returning {} employees", employees.size());
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            logger.error("Error getting all employees: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId,
                                                      @RequestBody EmployeeDto updatedEmployee) {
        logger.info("Received request to update employee with ID {}: {}", employeeId, updatedEmployee);

        try {
            EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updatedEmployee);
            logger.info("Employee updated successfully: {}", employeeDto);
            return ResponseEntity.ok(employeeDto);
        } catch (Exception e) {
            logger.error("Error updating employee: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
        logger.info("Received request to delete employee with ID: {}", employeeId);

        try {
            employeeService.deleteEmployee(employeeId);
            logger.info("Employee deleted successfully: {}", employeeId);
            return ResponseEntity.ok("Employee deleted successfully!.");
        } catch (Exception e) {
            logger.error("Error deleting employee: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}