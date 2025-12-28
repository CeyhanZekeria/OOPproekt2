package bg.autosalon.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee extends User {

    private double salary;

    public Employee() {}

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
