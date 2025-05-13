package ro.mpp2024.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ro.mpp2024.models.*;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Load config from hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();

            // Explicitly register annotated classes (optional if in XML, good for clarity)
            configuration.addAnnotatedClass(Participant.class);
            configuration.addAnnotatedClass(Result.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Arbiter.class);

            ServiceRegistry serviceRegistry =
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties())
                            .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
