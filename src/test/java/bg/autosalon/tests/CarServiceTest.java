package bg.autosalon.tests;

import bg.autosalon.dao.impl.CarDao;
import bg.autosalon.entities.Car;
import bg.autosalon.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarServiceTest {


    static class CarDaoStub extends CarDao {
        boolean saveWasCalled = false;
        boolean deleteWasCalled = false;
        Car carToReturn = null;

        @Override
        public void save(Car car) {

            this.saveWasCalled = true;
        }

        @Override
        public Car findById(Long id) {

            return carToReturn;
        }

        @Override
        public void delete(Car car) {

            this.deleteWasCalled = true;
        }
    }


    private CarService carService;
    private CarDaoStub carDaoStub;

    @BeforeEach
    public void setUp() {

        carDaoStub = new CarDaoStub();


        carService = new CarService(carDaoStub);
    }

    @Test
    public void testAddCar() {

        Car car = new Car();
        car.setBrand("Audi");


        carService.addCar(car);


        assertTrue(carDaoStub.saveWasCalled, "Грешка: Методът за запис не беше извикан!");
    }

    @Test
    public void testDeleteCar() {

        Car car = new Car();
        car.setId(1L);


        carDaoStub.carToReturn = car;


        carService.deleteCar(1L);


        assertTrue(carDaoStub.deleteWasCalled, "Грешка: Методът за триене не беше извикан!");
    }
}