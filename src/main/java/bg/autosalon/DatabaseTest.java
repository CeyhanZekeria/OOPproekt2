package bg.autosalon;

import bg.autosalon.config.HibernateUtil;
import jakarta.persistence.EntityManagerFactory;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
            emf.createEntityManager();
            System.out.println("Connected successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
