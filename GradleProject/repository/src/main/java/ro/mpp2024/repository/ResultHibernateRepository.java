package ro.mpp2024.repository;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ro.mpp2024.models.Event;
import ro.mpp2024.models.Result;

import java.util.*;

public class ResultHibernateRepository implements IResultRepository {

    @Override
    public Result create(Result result) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(result);
            tx.commit();
            return result;
        }
    }

    @Override
    public Optional<Result> read(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(Result.class, id));
        }
    }

    @Override
    public Result update(Result result) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(result);
            tx.commit();
            return result;
        }
    }

    @Override
    public List<Result> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Result", Result.class).list();
        }
    }

    @Override
    public List<Object[]> getTotalPointsPerParticipant() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT r.participant.id, r.participant.name, SUM(r.points)
                FROM Result r
                GROUP BY r.participant.id, r.participant.name
                ORDER BY r.participant.name
            """, Object[].class).list();
        }
    }

    @Override
    public List<Result> getResultsByEvent(String event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<Result> query = session.createQuery("""
                FROM Result r
                WHERE r.event = :event
                ORDER BY r.points DESC
            """, Result.class);
            query.setParameter("event", Event.valueOf(event));
            return query.getResultList();
        }
    }

    @Override
    public Map<Integer, Integer> getTotalPointsByParticipantForEvent(String event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createQuery("""
                SELECT r.participant.id, SUM(r.points)
                FROM Result r
                WHERE r.event = :event
                GROUP BY r.participant.id
            """, Object[].class)
                    .setParameter("event", Event.valueOf(event))
                    .list();

            Map<Integer, Integer> map = new HashMap<>();
            for (Object[] row : results) {
                map.put((Integer) row[0], ((Long) row[1]).intValue());
            }
            return map;
        }
    }
}
