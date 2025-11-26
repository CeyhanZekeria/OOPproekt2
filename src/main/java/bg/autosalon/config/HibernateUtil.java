package bg.autosalon.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {

    private static final EntityManagerFactory emf;

    static {
        try {
            Map<String, String> settings = new HashMap<>();

            settings.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
            settings.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
            settings.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

            emf = Persistence.createEntityManagerFactory("autosalonPU", settings);

        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to initialize Hibernate: " + e);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
