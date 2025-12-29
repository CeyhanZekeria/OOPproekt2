package bg.autosalon.services;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.dao.impl.ServiceRecordDao;
import bg.autosalon.entities.ServiceRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ServiceRecordService {

    private final ServiceRecordDao serviceDao = new ServiceRecordDao();

    public void addRecord(ServiceRecord record) {
        serviceDao.save(record);
    }

    public void updateRecord(ServiceRecord record) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(record);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteRecord(Long id) throws Exception {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ServiceRecord record = em.find(ServiceRecord.class, id);

            if (record != null) {
                em.remove(record);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<ServiceRecord> getAllRecords() {
        return serviceDao.findAll();
    }
}