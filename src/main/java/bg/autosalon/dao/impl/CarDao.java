package bg.autosalon.dao.impl;

import bg.autosalon.entities.Car;

public class CarDao extends GenericDao<Car> {
    public CarDao() {
        super(Car.class);
    }
}
