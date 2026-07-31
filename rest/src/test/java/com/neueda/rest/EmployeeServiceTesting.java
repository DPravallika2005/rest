package com.neueda.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTesting {
    //for service layer testing we will use mock data and not use the actual database

    @InjectMocks //instead of @Autowired we will use @InjectMocks to inject the mock repository into the service
    EmpService service;
    @Mock //mock data for repository layer
    EmpRepository repo;
    @Test
    void shouldReturnEmployee(){
        //mock data
        List<Employee> list = List.of(new Employee("John", "Doe",80000),new Employee("Jane", "Doe",70000));
        when(repo.getEmployees()).thenReturn(list);
        List<Employee> result = service.getEmployees();
        assertEquals(2,result.size());
        verify(repo).getEmployees();

    }
    @Test
void shouldReturnEmptyListWhenNoEmployeesExist() {
    when(repo.getEmployees()).thenReturn(List.of());

    List<Employee> result = service.getEmployees();

    assertEquals(0, result.size());
    verify(repo).getEmployees();
}

@Test
void shouldReturnOneWhenSaveEmployeeSucceeds() {
    Employee employee = new Employee("John", "IT", 50000);
    when(repo.saveEmployee(employee)).thenReturn(employee);

    int result = service.saveEmployee(employee);

    assertEquals(1, result);
    verify(repo).saveEmployee(employee);
}

@Test
void shouldReturnZeroWhenSaveEmployeeFails() {
    Employee employee = new Employee("John", "IT", 50000);
    when(repo.saveEmployee(employee)).thenReturn(null);

    int result = service.saveEmployee(employee);

    assertEquals(0, result);
    verify(repo).saveEmployee(employee);
}

@Test
void shouldReturnEmployeeWhenGetByIdSucceeds() {
    Employee employee = new Employee("John", "IT", 50000);
    employee.setId(1);
    when(repo.getEmployeeById(1)).thenReturn(employee);

    Employee result = service.getEmployeeById(1);

    assertEquals("John", result.getName());
    assertEquals(1, result.getId());
    verify(repo).getEmployeeById(1);
}

@Test
void shouldReturnNullWhenGetByIdFails() {
    when(repo.getEmployeeById(999)).thenReturn(null);

    Employee result = service.getEmployeeById(999);

    assertEquals(null, result);
    verify(repo).getEmployeeById(999);
}

@Test
void shouldThrowEmployeeNotFoundExceptionWhenGetByIdThrows() {
    when(repo.getEmployeeById(99)).thenThrow(new EmployeeNotFoundException("Employee not found"));

    assertThrows(EmployeeNotFoundException.class, () -> service.getEmployeeById(99));
    verify(repo).getEmployeeById(99);
}

@Test
void shouldReturnUpdatedEmployeeWhenUpdateSucceeds() {
    Employee update = new Employee("John", "Lead", 70000);
    when(repo.updateEmployee(1, update)).thenReturn(update);

    Employee result = service.updateEmployee(1, update);

    assertEquals("Lead", result.getRole());
    verify(repo).updateEmployee(1, update);
}

@Test
void shouldReturnNullWhenUpdateFails() {
    Employee update = new Employee("John", "Lead", 70000);
    when(repo.updateEmployee(1, update)).thenReturn(null);

    Employee result = service.updateEmployee(1, update);

    assertEquals(null, result);
    verify(repo).updateEmployee(1, update);
}

@Test
void shouldReturnOneWhenDeleteSucceeds() {
    when(repo.deleteEmployee(1)).thenReturn(1);

    int result = service.deleteEmployee(1);

    assertEquals(1, result);
    verify(repo).deleteEmployee(1);
}

@Test
void shouldReturnZeroWhenDeleteFails() {
    when(repo.deleteEmployee(1)).thenReturn(0);

    int result = service.deleteEmployee(1);

    assertEquals(0, result);
    verify(repo).deleteEmployee(1);
}

}
