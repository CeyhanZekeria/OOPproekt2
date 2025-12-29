package bg.autosalon.services;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.dao.impl.EmployeeDao;
import bg.autosalon.entities.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class EmployeeService {

    private final EmployeeDao employeeDao = new EmployeeDao();

    public void addEmployee(Employee employee) {
        employeeDao.save(employee);
    }

    public void updateEmployee(Employee employee) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteEmployee(Long id) throws Exception {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Employee employee = em.find(Employee.class, id);

            if (employee != null) {

                if (employee.getSales() != null && !employee.getSales().isEmpty()) {
                    throw new Exception("Cannot delete employee with sales history!");
                }
                em.remove(employee);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeDao.findAll();
    }
}