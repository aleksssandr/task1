package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class Util {
    private static final Logger logger = Logger.getLogger(Util.class.getName());
    private static final Properties properties = new Properties();
    private static SessionFactory sessionFactory;

    static {
        try {
            properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
            Class.forName(properties.getProperty("db.driver"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Initialization error", e);
        }
    }

    public static void hibernateTransaction(Consumer<Session> action) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                action.accept(session);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public static <T> T hibernateQuery(Function<Session, T> action) {
        try (Session session = getSessionFactory().openSession()) {
            return action.apply(session);
        }
    }

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .setProperty("hibernate.connection.driver_class", properties.getProperty("db.driver"))
                    .setProperty("hibernate.connection.url", properties.getProperty("db.url"))
                    .setProperty("hibernate.connection.username", properties.getProperty("db.user"))
                    .setProperty("hibernate.connection.password", properties.getProperty("db.password"))
                    .setProperty("hibernate.dialect", properties.getProperty("db.dialect"))
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void jdbcTransaction(Consumer<Connection> action) {
        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password"))) {

            connection.setAutoCommit(false);
            try {
                action.accept(connection);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "JDBC transaction error", e);
        }
    }

    public static String getDaoImplementation() {
        return properties.getProperty("dao.implementation", "hibernate");
    }
}