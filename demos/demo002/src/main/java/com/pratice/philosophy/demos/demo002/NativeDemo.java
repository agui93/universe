package com.pratice.philosophy.demos.demo002;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.util.Enumeration;

public class NativeDemo {

    private static String databaseName = "jdbc";
    private static String user = "root";
    private static String passwd = "123456";


    private static void printDrivers() throws ClassNotFoundException {
        System.out.println("Before loading SQLServerDriver:");
        Enumeration driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println("   " + driverClass.getClass().getName());
        }

        Class.forName("com.mysql.jdbc.Driver");

        System.out.println("After loading SQLServerDriver:");
        driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println("   " + driverClass.getClass().getName());
        }
    }

    private static void connectionByDriverManager() throws ClassNotFoundException, SQLException {

        // Loading a JDBC driver
        Class.forName("com.mysql.jdbc.Driver");


        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName + "?" + "user=" + user + "&password=" + passwd);
        System.out.println("Connected with host:port/database.");
        con.close();

        con = DriverManager.getConnection("jdbc:mysql://:3306/" + databaseName + "?" + "user=" + user + "&password=" + passwd);
        System.out.println("Connected with default host.");
        con.close();

        con = DriverManager.getConnection("jdbc:mysql://localhost/" + databaseName + "?" + "user=" + user + "&password=" + passwd);
        System.out.println("Connected with default port.");
        con.close();

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/?" + "user=" + user + "&password=" + passwd);
        System.out.println("Connected with no database.");
        con.close();

        con = DriverManager.getConnection("jdbc:mysql:///?" + "user=" + user + "&password=" + passwd);
        System.out.println("Connected with properties only.");
        con.close();
    }

    private static void connectionByDataSource() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName(databaseName);
        dataSource.setUser(user);
        dataSource.setPassword(passwd);


        Connection connection = dataSource.getConnection();

        DatabaseMetaData meta = connection.getMetaData();
        System.out.println("Server name: " + meta.getDatabaseProductName());
        System.out.println("Server version: " + meta.getDatabaseProductVersion());

        connection.close();
    }

    private static void checkDriverServerInfo() {
        Connection con;
        try {
            // Setting up the DataSource object
            com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
            ds.setServerName("localhost");
            ds.setPortNumber(3306);
            ds.setDatabaseName(databaseName);
            ds.setUser(user);
            ds.setPassword(passwd);

            // Getting a connection object
            con = ds.getConnection();

            // Getting driver and database info
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("Server name: " + meta.getDatabaseProductName());
            System.out.println("Server version: " + meta.getDatabaseProductVersion());
            System.out.println("Driver name: " + meta.getDriverName());
            System.out.println("Driver version: " + meta.getDriverVersion());
            System.out.println("JDBC major version: " + meta.getJDBCMajorVersion());
            System.out.println("JDBC minor version: " + meta.getJDBCMinorVersion());

            // Closing the connection
            con.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    private static void createTableWithAutoIncrement() {
        Connection con = null;
        try {

            // Setting up the DataSource object
            com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds
                    = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
            ds.setServerName("localhost");
            ds.setPortNumber(3306);
            ds.setDatabaseName(databaseName);
            ds.setUser(user);
            ds.setPassword(passwd);

            // Getting a connection object
            con = ds.getConnection();

            // Creating a database table
            Statement sta = con.createStatement();
            int count = sta.executeUpdate(
                    "CREATE TABLE Profile ("
                            + " ID INTEGER PRIMARY KEY AUTO_INCREMENT,"
                            + " FirstName VARCHAR(20) NOT NULL,"
                            + " LastName VARCHAR(20),"
                            + " Point REAL DEFAULT 0.0,"
                            + " BirthDate DATE DEFAULT '1998-12-31',"
                            + " ModTime TIMESTAMP DEFAULT '2016-12-31 23:59:59.999')");
            System.out.println("Table created.");
            sta.close();

            con.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
//        printDrivers();
//        connectionByDriverManager();
//        connectionByDataSource();
//        checkDriverServerInfo();

        createTableWithAutoIncrement();
    }


}
