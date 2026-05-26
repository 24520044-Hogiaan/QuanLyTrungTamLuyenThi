package com.trungtam.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ignored) {
        }
        URL = props.getProperty("db.url", "jdbc:oracle:thin:@localhost:1521:xe");
        USER = props.getProperty("db.user", "system");
        PASSWORD = props.getProperty("db.password", "12345678");
    }

    private static Connection connection;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
