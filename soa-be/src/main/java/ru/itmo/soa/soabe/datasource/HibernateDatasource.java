package ru.itmo.soa.soabe.datasource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import ru.itmo.soa.soabe.entity.Car;
import ru.itmo.soa.soabe.entity.Coordinates;
import ru.itmo.soa.soabe.entity.HumanBeing;

import java.util.Properties;


// TODO независимость от окружения
// TODO фильтрация по полям
// TODO пагинация

public class HibernateDatasource {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, "jdbc:postgresql://localhost:5432/test");
                settings.put(Environment.USER, "postgres");
                settings.put(Environment.PASS, "1234");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.SHOW_SQL, "true");

                settings.put(Environment.HBM2DDL_AUTO, "create");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(HumanBeing.class);
                configuration.addAnnotatedClass(Car.class);
                configuration.addAnnotatedClass(Coordinates.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                System.out.println("Hibernate Java Config serviceRegistry created");
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                return sessionFactory;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
