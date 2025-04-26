package com.jdbc.util;

import java.sql.Connection;
import java.sql.Driver;

public class Main {

    public static void main(String[] args) {
        Class<Driver> driverClass = Driver.class;
        try(Connection connection = ConnectionManager.open()){
            System.out.println(connection.getTransactionIsolation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
