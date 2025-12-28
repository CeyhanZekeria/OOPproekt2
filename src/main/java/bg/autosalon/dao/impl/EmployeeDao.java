package bg.autosalon.dao.impl;

import bg.autosalon.entities.Employee;

public class EmployeeDao extends GenericDao<Employee> {
    public EmployeeDao() {
        super(Employee.class);
    }
}
