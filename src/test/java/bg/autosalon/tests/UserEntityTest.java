package bg.autosalon.tests;

import bg.autosalon.entities.Employee;
import bg.autosalon.entities.User;
import bg.autosalon.enums.UserRole;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    @Test
    public void testEmployeeInheritance() {


        Employee emp = new Employee();
        emp.setFirstName("Dimitar");
        emp.setLastName("Berbatov");
        emp.setSalary(5000.00);
        emp.setRole(UserRole.SELLER);


        assertNotNull(emp);
        assertEquals("Dimitar", emp.getFirstName());
        assertEquals(5000.00, emp.getSalary());
    }

    @Test
    public void testPolymorphism() {

        Employee emp = new Employee();


        assertTrue(emp instanceof User, "Класът Employee трябва да наследява User!");
    }

    @Test
    public void testAdminRole() {

        User admin = new User();
        admin.setUsername("admin");
        admin.setRole(UserRole.ADMIN);

        assertEquals(UserRole.ADMIN, admin.getRole());
        assertNotEquals(UserRole.CLIENT, admin.getRole());
    }
}