package bg.autosalon.services;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.entities.TestDriveRequest;
import bg.autosalon.enums.RequestStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RequestService {

    
    public void createRequest(TestDriveRequest request) {
        request.setStatus(RequestStatus.PENDING);

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(request);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


    public List<TestDriveRequest> getAllRequests() {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<TestDriveRequest> query = em.createQuery("FROM TestDriveRequest", TestDriveRequest.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void updateStatus(Long requestId, RequestStatus newStatus) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            TestDriveRequest request = em.find(TestDriveRequest.class, requestId);
            if (request != null) {
                request.setStatus(newStatus);
                em.merge(request);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}