package ru.itmo.soa.soabe.dao;

import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.datasource.HibernateDatasource;
import ru.itmo.soa.soabe.entity.HumanBeing;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HumanBeingDao {
    private final Converter converter;

    public HumanBeingDao(Converter converter) {
        this.converter = converter;
    }

    public List<HumanBeing> getAllHumans() {
        Transaction transaction = null;
        List<HumanBeing> cities = List.of();
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            cities = session.createQuery("from HumanBeing").getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return cities;
    }

    public Optional<HumanBeing> getHuman(int id) {
        return Optional.empty();
    }

    public Optional<HumanBeing> deleteHuman(int id) {
        return Optional.empty();
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
    }
}
