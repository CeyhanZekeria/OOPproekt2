package bg.autosalon.services;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.dao.impl.ClientDao;
import bg.autosalon.entities.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ClientService {

    private final ClientDao clientDao = new ClientDao();

    public void addClient(Client client) {
        clientDao.save(client);
    }

    public void updateClient(Client client) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(client);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteClient(Long id) throws Exception {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Client client = em.find(Client.class, id);

            em.remove(client);


            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Client> getAllClients() {
        return clientDao.findAll();
    }
}