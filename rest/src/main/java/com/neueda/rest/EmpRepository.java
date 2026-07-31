package com.neueda.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class EmpRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //getEmployees method to return the list of employees
    public List<Employee> getEmployees(){
        String sql = "SELECT * FROM employee";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Employee.class));

    }
//    private List<Employee> employees = new ArrayList<>();
//    public EmpRepository(){
//        employees.add(new Employee("John", "SoftwareEngineer", 30000));
//        employees.add(new Employee("Jane", "Project Manager", 25000));
//        employees.add(new Employee("Jim", "QA", 35000));
//    }
    //getEmployees method to return the list of employees
//    public List<Employee> getEmployees() {
//        return employees;
//    }

    //saveEmployee method to add a new employee to the list
    public Employee saveEmployee(Employee employee) {
        String sql = "INSERT INTO employee (name, role, salary) VALUES (?, ?, ?)";
        int result = jdbcTemplate.update(sql, employee.getName(), employee.getRole(), employee.getSalary());
        if(result > 0) {
            return employee;
        }else {
            return null;
        }

    }
    //getemployeeById method to return an employee by id
    public Employee getEmployeeById(int id) {

        String sql = "SELECT * FROM employee WHERE id = ?";
        //RISKY CODE
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class), id);
        } catch (EmptyResultDataAccessException e) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
    }
//    //updateEmployee method to update an existing employee in the list
    public Employee updateEmployee(int id, Employee employee) {
        String sql = "UPDATE employee SET name = ?, role = ?, salary = ? WHERE id = ?";
        int result = jdbcTemplate.update(sql, employee.getName(), employee.getRole(), employee.getSalary(), id);
        if(result > 0) {
            return employee;
        }else {
            return null;
        }
    }
//    //deleteEmployee method to delete an existing employee in the list
    public int deleteEmployee(int id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        int result = jdbcTemplate.update(sql, id);
        if(result > 0) {
            return 1;
        }else {
            return 0;
        }
    }
}
