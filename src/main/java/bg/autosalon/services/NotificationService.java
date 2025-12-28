package bg.autosalon.services;

import bg.autosalon.dao.impl.NotificationDao;
import bg.autosalon.entities.Notification;

import java.util.*;

public class NotificationService {

    private final NotificationDao notificationDao = new NotificationDao();

    public void addNotification(Notification notification) {
        notificationDao.save(notification);
    }

    public void updateNotification(Notification notification) {
        notificationDao.update(notification);
    }

    public Notification getNotification(Long id) {
        return notificationDao.findById(id);
    }

    public List<Notification> getAllNotifications() {
        return notificationDao.findAll();
    }

    public void deleteNotification(Long id) {
        Notification notification = notificationDao.findById(id);
        if (notification != null) {
            notificationDao.delete(notification);
        }
    }
}
