package bg.autosalon.tests;

import bg.autosalon.entities.Client;
import bg.autosalon.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    public void testClientCreation() {

        Client client = new Client();
        client.setFirstName("Ivan");
        client.setLastName("Petrov");
        client.setEmail("ivan@test.com");
        client.setLoyaltyPoints(100);
        client.setRole(UserRole.CLIENT);


        assertEquals("Ivan", client.getFirstName());
        assertEquals("Petrov", client.getLastName());
        assertEquals(100, client.getLoyaltyPoints());
        assertEquals(UserRole.CLIENT, client.getRole());
    }

    @Test
    public void testLoyaltyPointsLogic() {
        Client client = new Client();
        client.setLoyaltyPoints(50);


        int newPoints = 20;
        client.setLoyaltyPoints(client.getLoyaltyPoints() + newPoints);


        assertEquals(70, client.getLoyaltyPoints(), "Loyalty points should sum up correctly");
    }
}