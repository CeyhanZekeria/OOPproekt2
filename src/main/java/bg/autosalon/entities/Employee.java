package bg.autosalon.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@PrimaryKeyJoinColumn(name = "id")
public class Employee extends User {

    @Column(name = "salary")
    private Double salary;


    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    private List<Sale> sales = new ArrayList<>();


    public Employee() {

    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }


    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}