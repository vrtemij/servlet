package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/servlet";
    public static final String USER = "postgres";
    public static final String PASSWORD = "root";
    public static final String DB_DRIVER = "org.postgresql.Driver";

    public static Connection initializeDatabase() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        return connection;
    }
}
