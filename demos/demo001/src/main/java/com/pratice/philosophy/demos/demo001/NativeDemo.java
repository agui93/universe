package com.pratice.philosophy.demos.demo001;

import java.sql.*;
import java.util.*;

public class NativeDemo {

    public static void main(String[] args) {
        Enumeration driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println("   " + driverClass.getClass().getName());
        }
    }


}
