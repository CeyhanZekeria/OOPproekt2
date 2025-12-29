package bg.autosalon.services;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.dao.impl.SaleDao;
import bg.autosalon.entities.Car;
import bg.autosalon.entities.Sale;
import bg.autosalon.enums.CarStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class SaleService {

    private final SaleDao saleDao = new SaleDao();

    public void addSale(Sale sale) {
        saleDao.save(sale);
    }

    public void updateSale(Sale sale) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(sale);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteSale(Long id) throws Exception {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Sale sale = em.find(Sale.class, id);

            if (sale != null) {
                Car car = sale.getCar();
                if (car != null) {
                    car.setStatus(CarStatus.AVAILABLE);
                    em.merge(car);
                }
                em.remove(sale);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Sale> getAllSales() {
        return saleDao.findAll();
    }
}