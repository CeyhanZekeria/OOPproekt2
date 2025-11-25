package bg.autosalon.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client extends User {

    private int loyaltyPoints;

    public Client() {}
}
