package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.SQLQueries;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    @Override
    public void createUsersTable() {
        Util.jdbcTransaction(conn ->
        {
            try {
                conn.createStatement().executeUpdate(SQLQueries.CREATE_TABLE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void dropUsersTable() {
        Util.jdbcTransaction(conn ->
        {
            try {
                conn.createStatement().executeUpdate(SQLQueries.DROP_TABLE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Util.jdbcTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_USER)) {
                ps.setString(1, name);
                ps.setString(2, lastName);
                ps.setByte(3, age);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void removeUserById(long id) {
        Util.jdbcTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(SQLQueries.DELETE_BY_ID)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        Util.jdbcTransaction(conn -> {
            try (ResultSet rs = conn.createStatement().executeQuery(SQLQueries.SELECT_ALL)) {
                while (rs.next()) {
                    User user = new User(
                            rs.getString("name"),
                            rs.getString("lastName"),
                            rs.getByte("age")
                    );
                    user.setId(rs.getLong("id"));
                    users.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        return users;
    }

    @Override
    public void cleanUsersTable() {
        Util.jdbcTransaction(conn ->
        {
            try {
                conn.createStatement().executeUpdate(SQLQueries.TRUNCATE_TABLE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
