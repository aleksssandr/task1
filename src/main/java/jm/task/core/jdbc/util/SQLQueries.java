package jm.task.core.jdbc.util;

public class SQLQueries {
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS users(" +
                    "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                    "name VARCHAR(255), " +
                    "lastName VARCHAR(255), " +
                    "age SMALLINT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS users";
    public static final String INSERT_USER = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    public static final String DELETE_BY_ID = "DELETE FROM users WHERE id = ?";
    public static final String SELECT_ALL = "SELECT * FROM users";
    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE users";
}
