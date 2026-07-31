package com.neueda.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController//localhost
@RequestMapping("api/v1/employees")//localhost/api/v1/employees
public class EmpController {
    @Autowired
    EmpService empService;
    //get all employees from the service layer
    @GetMapping("/")//localhost:8081/api/v1/employees/
    public ResponseEntity<Map<String,Object>> getEmployees() {
        Map<String, Object> response = new HashMap<>();
        List<Employee> employees = empService.getEmployees();
        response.put("message", "Employees retrieved successfully");
        response.put("data", employees);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/") //localhost/api/v1/employees/
    //save a new employee to the service layer
    public ResponseEntity<Map<String,Object>> addEmployee(@RequestBody Employee employee) {
        int result = empService.saveEmployee(employee);
        Map<String, Object> response = new HashMap<>();
        if (result > 0) {
            response.put("message", "Employee added successfully");
            response.put("data", employee);
            return ResponseEntity.status(201).body(response);
        } else {
            response.put("message", "Failed to add employee");
            return ResponseEntity.status(500).body(response);
        }
        //return ResponseEntity.status(201).body(response);
    }
    //get an employee by id from the service layer
    @GetMapping("/{id}")//localhost/api/v1/employees/{id}
    public ResponseEntity<Map<String,Object>> getEmployeeById(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        Employee employee = empService.getEmployeeById(id);
        if (employee != null) {
            response.put("message", "Employee retrieved successfully");
            response.put("data", employee);
            return ResponseEntity.status(200).body(response);
        } else {
            response.put("message", "Employee not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    @PutMapping("/{id}")
    //update an existing employee in the service layer
    public ResponseEntity<Map<String,Object>> updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        Map<String, Object> response = new HashMap<>();
        Employee updatedEmployee = empService.updateEmployee(id, employee);
        if (updatedEmployee != null) {
            response.put("message", "Employee updated successfully");
            response.put("data", updatedEmployee);
            return ResponseEntity.status(200).body(response);
        } else {
            response.put("message", "Failed to update employee");
            return ResponseEntity.status(500).body(response);
        }
    }
    @DeleteMapping("/{id}")
    //delete an existing employee in the service layer
    public ResponseEntity<Map<String,Object>> deleteEmployee(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        int deletedEmployee = empService.deleteEmployee(id);
        if (deletedEmployee != 0) {
            response.put("message", "Employee deleted successfully");
            return ResponseEntity.status(200).body(response);
        } else {
            response.put("message", "Failed to delete employee");
            return ResponseEntity.status(500).body(response);
        }
    }

}
