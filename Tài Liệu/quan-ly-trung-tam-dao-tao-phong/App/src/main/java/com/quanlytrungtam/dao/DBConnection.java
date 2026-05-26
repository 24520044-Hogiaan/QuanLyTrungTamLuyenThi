package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getConnection() {
        String url = "jdbc:oracle:thin:@//localhost:1521/ORCLDBPDB1";
        try {
            Connection con = DriverManager.getConnection(url, "SINHVIEN04", "abc");
            System.out.println("Database connected");
            return con;
        } catch (SQLException ex) {
            System.out.println("Warning: Could not connect to database. UI will run in offline mode.");
            return null;
        }
    }

    public void closeConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database closed");
            }
        } catch (SQLException ex) {
            System.out.println("Can't close connection");
        }
    }
}
