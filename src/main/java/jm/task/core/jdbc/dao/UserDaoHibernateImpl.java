package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.SQLQueries;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    @Override
    public void createUsersTable() {
        Util.hibernateTransaction(session ->
                session.createSQLQuery(SQLQueries.CREATE_TABLE).executeUpdate());
    }

    @Override
    public void dropUsersTable() {
        Util.hibernateTransaction(session ->
                session.createSQLQuery(SQLQueries.DROP_TABLE).executeUpdate());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Util.hibernateTransaction(session ->
                session.save(new User(name, lastName, age)));
    }

    @Override
    public void removeUserById(long id) {
        Util.hibernateTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        return Util.hibernateQuery(session ->
                session.createQuery("from User", User.class).list());
    }

    @Override
    public void cleanUsersTable() {
        Util.hibernateTransaction(session ->
                session.createQuery("delete from User").executeUpdate());
    }
}
