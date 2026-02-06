package bg.autosalon.services;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.dao.impl.NotificationDao;
import bg.autosalon.entities.Notification;
import jakarta.persistence.EntityManager;

import java.util.List;

public class NotificationService {

    private final NotificationDao notificationDao = new NotificationDao();

    public void addNotification(Notification notification) {
        notificationDao.save(notification);
    }


    public List<Notification> getClientNotifications(Long clientId) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notification n WHERE n.client.id = :cid ORDER BY n.date DESC",
                            Notification.class)
                    .setParameter("cid", clientId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

}