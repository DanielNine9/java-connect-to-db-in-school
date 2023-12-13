/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huyyd
 */
public class ConnectionDatabase {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=LAB6_PC07859;encrypt=true;trustServerCertificate=true";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "1234";
    
    public static Connection getConnection() {
        Connection con = null;
        
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
    public static void closeConnection(Connection con) {
        if(con != null){
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void closeResultSet(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void closePrepareStatement(PreparedStatement ps){
        if(ps != null){
            try {
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
