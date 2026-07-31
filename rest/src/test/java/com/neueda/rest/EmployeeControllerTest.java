package com.neueda.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmpController.class)
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmpService service;

    @Test
    void shouldReturnEmployeesFromService() throws Exception {
        List<Employee> list = List.of(new Employee("John", "IT", 50000));

        when(service.getEmployees()).thenReturn(list);

        mockMvc.perform(get("/api/v1/employees/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employees retrieved successfully"))
                .andExpect(jsonPath("$.data[0].name").value("John"));
    }

    @Test
    void shouldReturnEmptyEmployeeListWhenNoEmployeesExist() throws Exception {
        when(service.getEmployees()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/employees/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employees retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldCreateEmployeeWhenServiceReportsSuccess() throws Exception {
        when(service.saveEmployee(any(Employee.class))).thenReturn(1);

        mockMvc.perform(post("/api/v1/employees/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"role\":\"IT\",\"salary\":50000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Employee added successfully"))
                .andExpect(jsonPath("$.data.name").value("John"));
    }

    @Test
    void shouldReturnServerErrorWhenCreateFails() throws Exception {
        when(service.saveEmployee(any(Employee.class))).thenReturn(0);

        mockMvc.perform(post("/api/v1/employees/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"role\":\"IT\",\"salary\":50000}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Failed to add employee"));
    }

    @Test
    void shouldReturnEmployeeByIdWhenFound() throws Exception {
        Employee employee = new Employee("John", "IT", 50000);
        employee.setId(1);
        when(service.getEmployeeById(1)).thenReturn(employee);

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee retrieved successfully"))
                .andExpect(jsonPath("$.data.name").value("John"));
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeMissing() throws Exception {
        when(service.getEmployeeById(999)).thenReturn(null);

        mockMvc.perform(get("/api/v1/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    void shouldUpdateEmployeeWhenServiceReturnsUpdatedEmployee() throws Exception {
        Employee updated = new Employee("John", "Lead", 70000);
        when(service.updateEmployee(eq(1), any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"role\":\"Lead\",\"salary\":70000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee updated successfully"))
                .andExpect(jsonPath("$.data.role").value("Lead"));
    }

    @Test
    void shouldReturnServerErrorWhenUpdateFails() throws Exception {
        when(service.updateEmployee(eq(1), any(Employee.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"role\":\"Lead\",\"salary\":70000}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Failed to update employee"));
    }

    @Test
    void shouldDeleteEmployeeWhenServiceReportsSuccess() throws Exception {
        when(service.deleteEmployee(1)).thenReturn(1);

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
    }

    @Test
    void shouldReturnServerErrorWhenDeleteFails() throws Exception {
        when(service.deleteEmployee(1)).thenReturn(0);

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Failed to delete employee"));
    }
}
