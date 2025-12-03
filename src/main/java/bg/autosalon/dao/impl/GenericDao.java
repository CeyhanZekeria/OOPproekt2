package bg.autosalon.dao.impl;

import bg.autosalon.config.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.*;

public class GenericDao<T> {

    private final Class<T> entityClass;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
    }

    public void update(T entity) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
        em.close();
    }

    public T findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        T entity = em.find(entityClass, id);
        em.close();
        return entity;
    }

    public List<T> findAll() {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        List<T> result = em.createQuery(
                        "SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
        em.close();
        return result;
    }


    public void delete(T entity) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        T managed = em.contains(entity) ? entity : em.merge(entity);
        em.remove(managed);

        em.getTransaction().commit();
        em.close();
    }
}
