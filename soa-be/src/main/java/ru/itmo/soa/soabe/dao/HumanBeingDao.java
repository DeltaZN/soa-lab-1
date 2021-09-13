package ru.itmo.soa.soabe.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.itmo.soa.soabe.datasource.HibernateDatasource;
import ru.itmo.soa.soabe.entity.Car;
import ru.itmo.soa.soabe.entity.Coordinates;
import ru.itmo.soa.soabe.entity.HumanBeing;
import ru.itmo.soa.soabe.servlet.HumanBeingRequestParams;

import javax.persistence.criteria.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
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

    @Data
    @AllArgsConstructor
    @XmlRootElement(name = "pagination_result")
    public static class PaginationResult {
        @XmlElement
        private final int totalPages;
        @XmlElement
        private final int currentPage;
        @XmlElement
        private final long totalItems;
        @XmlElementWrapper(name="humans")
        @XmlElement(name="human")
        private final List<HumanBeing> list;
        public PaginationResult() {
            totalPages = 0;
            currentPage = 0;
            totalItems = 0;
            list = List.of();
        }
    }

    private void applyPagination(Query<HumanBeing> typedQuery, HumanBeingRequestParams params) {
        int pageIndex = params.pageIdx;
        int numberOfRecordsPerPage = params.pageSize;
        int startIndex = (pageIndex * numberOfRecordsPerPage) - numberOfRecordsPerPage;
        typedQuery.setFirstResult(startIndex);
        typedQuery.setMaxResults(numberOfRecordsPerPage);
    }

    public PaginationResult getAllHumans(HumanBeingRequestParams params) {
        Transaction transaction = null;
        PaginationResult res = new PaginationResult();
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<HumanBeing> cr =
                    cb.createQuery(HumanBeing.class);
            Root<HumanBeing> root = cr.from(HumanBeing.class);
            Join<HumanBeing, Car> join = root.join("car");
            Join<HumanBeing, Coordinates> joinCoordinates = root.join("coordinates");

            List<Predicate> predicates = params.getPredicates(cb, root, join, joinCoordinates);
            if (params.sorting != null) {
                if (params.sorting.startsWith("coordinates")) {
                    cr.orderBy(cb.asc(joinCoordinates.get(params.sorting.replaceAll("coordinates", "").toLowerCase())));
                } else if (params.sorting.startsWith("car")) {
                    cr.orderBy(cb.asc(join.get(params.sorting.replaceAll("car", "").toLowerCase())));
                } else {
                    cr.orderBy(cb.asc(root.get(params.sorting)));
                }
            }

            CriteriaQuery<HumanBeing> query = cr.select(root).where(predicates.toArray(new Predicate[0]));
            Query<HumanBeing> typedQuery = session.createQuery(query);
            applyPagination(typedQuery, params);

            CriteriaQuery<Long> countQuery = cb
                    .createQuery(Long.class);
            countQuery.select(cb
                    .count(countQuery.from(HumanBeing.class)));
            Long count = session.createQuery(countQuery)
                    .getSingleResult();

            List<HumanBeing> list = typedQuery.getResultList();

            res = new PaginationResult((int) (count / params.pageSize) + 1, params.pageIdx, count, list);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return res;
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

    public int createHuman(HumanBeing human) {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(human);
            transaction.commit();
            return human.getId();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
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
            throw e;
        }
    }
}
