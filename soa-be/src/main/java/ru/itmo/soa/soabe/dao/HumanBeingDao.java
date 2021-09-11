package ru.itmo.soa.soabe.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.itmo.soa.soabe.datasource.HibernateDatasource;
import ru.itmo.soa.soabe.entity.HumanBeing;

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

    public List<HumanBeing> getAllHumans() {
        Transaction transaction = null;
        List<HumanBeing> humans = List.of();
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            humans = session.createQuery("from HumanBeing").getResultList();
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
