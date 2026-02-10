package bg.autosalon.services;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.dao.impl.CarDao;
import bg.autosalon.entities.Car;
import bg.autosalon.enums.CarStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class CarService {


    private final CarDao carDao;


    public CarService() {
        this.carDao = new CarDao();
    }


    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }


    public void addCar(Car car) {
        car.setStatus(CarStatus.AVAILABLE);
        carDao.save(car);
    }

    public void updateCar(Car car) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
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