package bg.autosalon.services;

import bg.autosalon.dao.impl.CarDao;
import bg.autosalon.entities.Car;
import bg.autosalon.enums.CarStatus;

import java.util.*;

public class CarService {

    private final CarDao carDao = new CarDao();

    public void addCar(Car car) {
        car.setStatus(CarStatus.AVAILABLE);
        carDao.save(car);
    }

    public void updateCar(Car car) {
        carDao.update(car);
    }

    public Car getCar(Long id) {
        return carDao.findById(id);
    }

    public List<Car> getAllCars() {
        return carDao.findAll();
    }

    public void deleteCar(Long id) {
        Car car = carDao.findById(id);
        if (car != null) {
            carDao.delete(car);
        }
    }
}
