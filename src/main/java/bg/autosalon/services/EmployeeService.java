package bg.autosalon.services;

import bg.autosalon.dao.impl.EmployeeDao;
import bg.autosalon.entities.Employee;

import java.util.*;

public class EmployeeService {

    private final EmployeeDao employeeDao = new EmployeeDao();

    public void addEmployee(Employee employee) {
        employeeDao.save(employee);
    }

    public void updateEmployee(Employee employee) {
        employeeDao.update(employee);
    }

    public Employee getEmployee(Long id) {
        return employeeDao.findById(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeDao.findAll();
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeDao.findById(id);
        if (employee != null) {
            employeeDao.delete(employee);
        }
    }
}
