package com.neueda.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpService {
    @Autowired
    private EmpRepository empRepository;
    //getEmployees method to return the list of employees
    public List<Employee> getEmployees() {
        return empRepository.getEmployees();
    }
    //saveEmployee method to add a new employee to the list
    public int saveEmployee(Employee employee) {
        return empRepository.saveEmployee(employee)!= null? 1 : 0;
    }
//    //getEmployeeById method to return an employee by id
    public Employee getEmployeeById(int id) {
        return empRepository.getEmployeeById(id);
    }
    //updateEmployee method to update an existing employee in the list
    public Employee updateEmployee(int id, Employee employee) {
        return empRepository.updateEmployee(id, employee);
    }
    //deleteEmployee method to delete an existing employee in the list
    public int deleteEmployee(int id) {
        return empRepository.deleteEmployee(id);
    }

}
