/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai2;

import bai1.Book;
import bai1.BookRepository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ConnectionDatabase;

/**
 *
 * @author huyyd
 */
public class StudentRepository {

    public List<Student> getStudents() {
        List<Student> list = new ArrayList<>();

        Connection con = ConnectionDatabase.getConnection();
        PreparedStatement pst = null;
        if (con != null) {
            String sql = "SELECT * FROM STUDENT";
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Integer id = rs.getInt("Id");
                    String name = rs.getString("nameStudent");
                    String address = rs.getString("addressName");
                    String parentName = rs.getString("parentName");
                    String contactNo = rs.getString("contactNo");
                    String standard = rs.getString("StandardName");
                    Long fees = rs.getLong("fees");
                    Date regdate = rs.getDate("regdate");
                    list.add(new Student(id, name,address, parentName, contactNo, standard, fees, regdate));
                }
            } catch (SQLException ex) {
                Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        closeAllDatabaseUtil(con, pst);
        return list;

    }

    public boolean addStudent(Student newStudent) {
        Connection con = ConnectionDatabase.getConnection();
        PreparedStatement pst = null;

        if (con != null) {
            String insertSQL = "INSERT INTO STUDENT VALUES(?, ?, ?, ?, ?, ?, ?)";
            try {

                pst = con.prepareStatement(insertSQL);

                pst.setString(1, newStudent.getName());
                pst.setString(2, newStudent.getAddress());
                pst.setString(3, newStudent.getParentName());
                pst.setString(4, newStudent.getContactNo());
                pst.setString(5, newStudent.getStandard());
                pst.setDouble(6, newStudent.getFees());
                pst.setDate(7, newStudent.getRegDate());
                
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

    public boolean deleteStudent(Integer id) {
        Connection con = ConnectionDatabase.getConnection();
        PreparedStatement pst = null;
        if (con != null) {
            String sql = "DELETE FROM STUDENT WHERE ID = ?";
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, id);
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
        return false;
    }

    public boolean updateStudent(Student newStudent) {
        Connection con = ConnectionDatabase.getConnection();
        PreparedStatement pst = null;
        if (con != null) {
            String sql = "UPDATE STUDENT SET nameStudent = ?, addressName = ?, parentName = ?, contactNo = ?, standardName = ?, fees = ? WHERE id = ?";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, newStudent.getName());
                pst.setString(2, newStudent.getAddress());
                pst.setString(3, newStudent.getParentName());
                pst.setString(4, newStudent.getContactNo());
                pst.setString(5, newStudent.getStandard());
                pst.setDouble(6, newStudent.getFees());
               pst.setInt(7, newStudent.getId());

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

    public Student findByName(String name) {
        String sql = "SELECT * FROM STUDENT WHERE NAMESTUDENT = ?";
        PreparedStatement pst;
        Connection con = ConnectionDatabase.getConnection();
        ResultSet rs = null;
        Student std = null;

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, name);
                rs = pst.executeQuery();
                if(rs.next()){
                    std = new Student();
                    std.setAddress(rs.getString("addressName"));
                    std.setContactNo(rs.getString("contactNo"));
                    std.setFees(rs.getLong("fees"));
                    std.setName(rs.getString("nameStudent"));
                    std.setParentName(rs.getString("parentName"));
                    std.setStandard(rs.getString("standardName"));
                    std.setId(rs.getInt("id"));
                    std.setRegDate(rs.getDate("regdate"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(StudentRepository.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return std;
    }
}
