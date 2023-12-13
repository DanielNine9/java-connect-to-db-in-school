/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author huyyd
 */
public class MyConnection {
    private static String url = "jdbc:sqlserver://localhost:1433;databaseName=QLSV_PC07859;encrypt=true;trustServerCertificate=true";
    private static String username = "sa";
    private static String password = "1234";

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        MyConnection.url = url;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        MyConnection.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        MyConnection.password = password;
    }

    public static Connection getConnection() {
        try {
            return  DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
