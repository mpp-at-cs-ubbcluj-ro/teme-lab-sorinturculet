package ro.mpp2024.repository;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ro.mpp2024.models.User;

import java.util.List;
import java.util.Optional;

public class UserHibernateRepository implements IUserRepository {

    @Override
    public User create(User entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return entity;
        }
    }

    @Override
    public Optional<User> read(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        }
    }

    @Override
    public User update(User entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return entity;
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<User> query = session.createQuery("""
                FROM User u
                WHERE u.name = :username AND u.password = :password
            """, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getResultStream().findFirst();
        }
    }
}
