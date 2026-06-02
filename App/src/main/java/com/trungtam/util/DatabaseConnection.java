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
        URL = "jdbc:oracle:thin:@//localhost:1521/orcl";
        USER = "Hogiaan";
        PASSWORD = "12345678";
    }

    private static Connection realConnection;
    private static Connection proxyConnection;

    private DatabaseConnection() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (realConnection == null || realConnection.isClosed()) {
            realConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            
            proxyConnection = (Connection) java.lang.reflect.Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        return null; // Do nothing, keep connection alive
                    }
                    if ("isClosed".equals(method.getName())) {
                        return false;
                    }
                    return method.invoke(realConnection, args);
                }
            );
        }
        return proxyConnection;
    }

    public static void closeConnection() {
        if (realConnection != null) {
            try {
                realConnection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
