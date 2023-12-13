/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai1;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ConnectionDatabase;

/**
 *
 * @author huyyd
 */
public class BookRepository {

    public List<Book> getBooks() {
        List<Book> list = new ArrayList<>();

        Connection con = ConnectionDatabase.getConnection();
        PreparedStatement pst = null;
        if (con != null) {
            String sql = "SELECT * FROM BOOK";

            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("Id");
                    String title = rs.getString("title");
                    Double price = rs.getDouble("price");
                    list.add(new Book(id, title, price));
                }
            } catch (SQLException ex) {
                Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        closeAllDatabaseUtil(con, pst);
        return list;

    }

    public boolean addBook(Book newBook) {
        Connection con = ConnectionDatabase.getConnection();
        PreparedStatement pst = null;

        if (con != null) {
            String insertSQL = "INSERT INTO BOOK VALUES(?, ?)";
            try {

                pst = con.prepareStatement(insertSQL);
                pst.setString(1, newBook.getTitle());
                pst.setDouble(2, newBook.getPrice());
                int rowEffect = pst.executeUpdate();
                if (rowEffect > 0) {
                    closeAllDatabaseUtil(con, pst);
                    return true;
                }

            } catch (SQLException ex) {
                Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        closeAllDatabaseUtil(con, pst);

        return true;
    }

    public boolean deleteBook(String id) {
        Connection con = ConnectionDatabase.getConnection();
        PreparedStatement pst = null;
        if (con != null) {
            String sql = "DELETE FROM BOOK WHERE ID = ?";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, id);
                int rowEffect = pst.executeUpdate();
                if(rowEffect > 0){
                    closeAllDatabaseUtil(con, pst);
                    return true;
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        closeAllDatabaseUtil(con, pst);
        return false;
    }
    
    public boolean updateBook(Book newBook) {
        Connection con = ConnectionDatabase.getConnection();
        PreparedStatement pst = null;
        if(con != null){
            String sql = "UPDATE FROM BOOK SET title = ?, price = ? WHERE id = ?";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, newBook.getTitle());
                pst.setDouble(2, newBook.getPrice());
                pst.setString(3, newBook.getId());
                int rowEffect = pst.executeUpdate();
                if(rowEffect > 0){
                    closeAllDatabaseUtil(con, pst);
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        closeAllDatabaseUtil(con, pst);
        return false;
    }

    private void closeAllDatabaseUtil(Connection con, PreparedStatement pst) {
        if (con != null) {
            ConnectionDatabase.closeConnection(con);
        }
        if (pst != null) {
            ConnectionDatabase.closePrepareStatement(pst);
        }

    }
}
