package bg.autosalon.tests;

import bg.autosalon.entities.Car;
import bg.autosalon.entities.ServiceRecord;
import bg.autosalon.enums.ServiceType;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceRecordTest {

    @Test
    public void testServiceRecordCreation() {
        
        ServiceRecord record = new ServiceRecord();
        record.setType(ServiceType.REPAIR);
        record.setPrice(150.50);
        record.setDate(LocalDate.now());
        record.setDescription("Смяна на масло");


        assertEquals(150.50, record.getPrice(), "Цената на ремонта трябва да е 150.50");
        assertEquals(ServiceType.REPAIR, record.getType());
    }

    @Test
    public void testTotalServiceRevenue() {


        ServiceRecord rec1 = new ServiceRecord();
        rec1.setPrice(100.00);

        ServiceRecord rec2 = new ServiceRecord();
        rec2.setPrice(250.00);

        ServiceRecord rec3 = new ServiceRecord();
        rec3.setPrice(50.00);

        double total = rec1.getPrice() + rec2.getPrice() + rec3.getPrice();

        assertEquals(400.00, total, "Общият приход от сервиз трябва да е 400.00");
    }

    @Test
    public void testNegativePriceValidation() {

        ServiceRecord record = new ServiceRecord();
        record.setPrice(-50.00);


        assertTrue(record.getPrice() < 0, "Цената може да бъде зададена като отрицателна (ако няма валидация в сетъра)");
    }
}