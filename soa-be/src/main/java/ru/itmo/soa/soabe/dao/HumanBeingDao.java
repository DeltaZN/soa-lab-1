package ru.itmo.soa.soabe.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.itmo.soa.soabe.datasource.HibernateDatasource;
import ru.itmo.soa.soabe.entity.Car;
import ru.itmo.soa.soabe.entity.Coordinates;
import ru.itmo.soa.soabe.entity.HumanBeing;
import ru.itmo.soa.soabe.servlet.HumanBeingFilterParams;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

public class HumanBeingDao {

    public HumanBeingDao() {}

    public Long countHumansSoundtrackNameLess(String soundtrack) {
        long count = 0;
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<Long> query = session.createQuery("select count(*) from HumanBeing H where H.soundtrackName < :soundtrack");
            query.setParameter("soundtrack", soundtrack);
            count = query.getSingleResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return count;
    }

    public List<HumanBeing> findHumansMinutesOfWaitingLess(long minutesOfWaiting) {
        List<HumanBeing> list = List.of();
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<HumanBeing> query = session.createQuery("from HumanBeing H where H.minutesOfWaiting < :minutesOfWaiting");
            query.setParameter("minutesOfWaiting", minutesOfWaiting);
            list = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return list;
    }

    public long deleteAnyHumanMinutesOfWaitingEqual(long minutesOfWaiting) {
        long deletedId = -1;
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<HumanBeing> query = session.createQuery("from HumanBeing H where H.minutesOfWaiting = :minutesOfWaiting");
            query.setParameter("minutesOfWaiting", minutesOfWaiting);
            List<HumanBeing> humans = query.getResultList();
            if (humans.size() > 0) {
                session.delete(humans.get(0));
                session.flush();
                deletedId = humans.get(0).getId();
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return deletedId;
    }

    public List<HumanBeing> getAllHumans(HumanBeingFilterParams params) {
        Transaction transaction = null;
        List<HumanBeing> humans = List.of();
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<HumanBeing> cr =
                    cb.createQuery(HumanBeing.class);
            Root<HumanBeing> root = cr.from(HumanBeing.class);
            Join<HumanBeing, Car> join = root.join("car");
            Join<HumanBeing, Coordinates> joinCoordinates = root.join("coordinates");

            List<Predicate> predicates = params.getPredicates(cb, root, join, joinCoordinates);
            CriteriaQuery<HumanBeing> query = cr.select(root).where(predicates.toArray(new Predicate[0]));
            humans = session.createQuery(query).getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return humans;
    }

    public Optional<HumanBeing> getHuman(int id) {
        Transaction transaction = null;
        HumanBeing humanBeing = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            humanBeing = session.find(HumanBeing.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.ofNullable(humanBeing);
    }

    public boolean deleteHuman(int id) {
        Transaction transaction = null;
        boolean successful = false;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            HumanBeing humanBeing = session.find(HumanBeing.class, id);
            if (humanBeing != null) {
                session.delete(humanBeing);
                session.flush();
                successful = true;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return successful;
    }

    public void createHuman(HumanBeing human) {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(human);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateHuman(HumanBeing human) {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(human);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
