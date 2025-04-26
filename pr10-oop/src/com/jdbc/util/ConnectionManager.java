package com.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";


    static {
        loadDriver();
    }

    private ConnectionManager(){}

    public static Connection open() {
        Connection connection = null;
        try {
            // Підключення до бази даних
            String url = PropertiesUtil.get("db.url");
            String username = PropertiesUtil.get("db.username");
            String password = PropertiesUtil.get("db.password");

            if (url == null || username == null || password == null) {
                throw new SQLException("Необхідні параметри підключення не знайдені в файлі конфігурації.");
            }

            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Помилка при підключенні до бази даних: " + e.getMessage());
        }
        return connection;
    }

    public static void loadDriver(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            new RuntimeException(e);
        }
    }
}
