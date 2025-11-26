package bg.autosalon.dao.impl;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.entities.User;
import jakarta.persistence.NoResultException;

public class UserDao extends GenericDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User findByEmail(String email) {
        var em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
