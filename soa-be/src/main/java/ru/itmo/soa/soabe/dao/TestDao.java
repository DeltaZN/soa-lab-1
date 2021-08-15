package ru.itmo.soa.soabe.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.itmo.soa.soabe.datasource.HibernateDatasource;
import ru.itmo.soa.soabe.entity.TestEntity;

import java.util.Optional;

public class TestDao {
    public Optional<Long> test() {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TestEntity testEntity = new TestEntity();
            session.save(testEntity);
            transaction.commit();
            return Optional.of(testEntity.id);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
