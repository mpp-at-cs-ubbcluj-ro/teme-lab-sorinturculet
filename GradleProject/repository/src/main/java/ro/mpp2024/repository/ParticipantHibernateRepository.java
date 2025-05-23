package ro.mpp2024.repository;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ro.mpp2024.models.Participant;

import java.util.List;
import java.util.Optional;

public class ParticipantHibernateRepository implements IParticipantRepository {

    @Override
    public Participant create(Participant participant) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(participant);
            tx.commit();
            return participant;
        }
    }

    @Override
    public Optional<Participant> read(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Participant p = session.find(Participant.class, id);
            return Optional.ofNullable(p);
        }
    }

    @Override
    public Participant update(Participant participant) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(participant);
            tx.commit();
            return participant;
        }
    }
    @Override
    public void delete(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Participant participant = session.find(Participant.class, id);
            if (participant != null) {
                session.remove(participant);
            }
            tx.commit();
        }
    }

    @Override
    public List<Participant> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Participant p ORDER BY p.name", Participant.class).list();
        }
    }

    @Override
    public List<Participant> findAllParticipantsSorted() {
        return findAll();
    }

    @Override
    public List<Participant> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<Participant> query = session.createQuery(
                    "FROM Participant p WHERE p.name LIKE :name", Participant.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        }
    }
}
