package ru.itmo.soa.soabe.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.datasource.HibernateDatasource;
import ru.itmo.soa.soabe.entity.HumanBeing;
import ru.itmo.soa.soabe.entity.TestEntity;

import java.util.List;
import java.util.Optional;

public class HumanBeingDao {
    private final Converter converter;

    public HumanBeingDao(Converter converter) {
        this.converter = converter;
    }

    public List<HumanBeing> getAllHumans() {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TestEntity testEntity = new TestEntity();
            session.save(testEntity);
            transaction.commit();
            return Optional.of(converter.toStr(testEntity));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<HumanBeing> getHuman(int id) {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TestEntity testEntity = new TestEntity();
            session.save(testEntity);
            transaction.commit();
            return Optional.of(converter.toStr(testEntity));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<HumanBeing> deleteHuman(int id) {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TestEntity testEntity = new TestEntity();
            session.save(testEntity);
            transaction.commit();
            return Optional.of(converter.toStr(testEntity));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void createHuman(HumanBeing human) {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TestEntity testEntity = new TestEntity();
            session.save(testEntity);
            transaction.commit();
            return Optional.of(converter.toStr(testEntity));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void updateHuman(HumanBeing human) {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TestEntity testEntity = new TestEntity();
            session.save(testEntity);
            transaction.commit();
            return Optional.of(converter.toStr(testEntity));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
