package bg.autosalon.tests;

import bg.autosalon.entities.Car;
import bg.autosalon.entities.Client;
import bg.autosalon.enums.CarStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaleLogicTest {

    @Test
    public void testCarStatusChange() {

        Car car = new Car();
        car.setBrand("Opel");
        car.setStatus(CarStatus.AVAILABLE);


        assertEquals(CarStatus.AVAILABLE, car.getStatus());


        car.setStatus(CarStatus.SOLD);


        assertEquals(CarStatus.SOLD, car.getStatus(), "Статусът на колата трябва да бъде SOLD.");
    }

    @Test
    public void testLoyaltyPointsCalculation() {


        Client client = new Client();
        client.setLoyaltyPoints(10);

        double carPrice = 25000.00;


        int newPoints = (int) (carPrice / 1000);
        client.setLoyaltyPoints(client.getLoyaltyPoints() + newPoints);


        assertEquals(35, client.getLoyaltyPoints(), "Точките за лоялност не са сметнати вярно!");
    }
}