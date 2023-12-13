/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import connection.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Student;

/**
 *
 * @author huyyd
 */
public class UserRepository {

    private UserRepository userRepository = null;

    private static UserRepository instance;

    private UserRepository() {

    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public void add(Student newStudent) throws SQLException {
        Connection connection = MyConnection.getConnection();

        PreparedStatement statement = null;
        if (connection != null) {
            try {
                String sql = "INSERT INTO STUDENTS(Name, Email, SoDT, GioiTinh, Diachi) VALUES (?, ? , ? , ? ,?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newStudent.getName());
                statement.setString(2, newStudent.getEmail());
                statement.setString(3, newStudent.getPhone());
                statement.setBoolean(4, newStudent.isSex());
                statement.setString(5, newStudent.getAddress());
                int result = statement.executeUpdate();
                if (result == 0) {
                    throw new SQLException("Add student failed");
                }
            } finally {
                statement.close();
                connection.close();
            }
        } else {
            throw new SQLException("Có lỗi xảy ra ");
        }
    }

    public Student findById(int id) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement statement = null;
        if (connection != null) {
            Student st = null;
            try {
                String sql = "SELECT * FROM STUDENTS WHERE STUDENTS.MaSV = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results.next()) {
                    st = new Student();
                    st.setId(results.getInt("MaSV"));
                    st.setName(results.getString("Name"));
                    st.setEmail(results.getString("Email"));
                    st.setPhone(results.getString("SoDT"));
                    st.setAddress(results.getString("DiaChi"));
                    st.setSex(results.getBoolean("GioiTinh"));
                }
                return st;
            } finally {
                connection.close();
                statement.close();
            }
        }
        return null;
    }

    public ArrayList<Student> findAll() throws SQLException {
        Connection connection = MyConnection.getConnection();
        ArrayList<Student> list = new ArrayList<>();
        if (connection != null) {
            String sql = "SELECT * FROM STUDENTS";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Student st = new Student();
                st.setId(results.getInt("MaSV"));
                st.setName(results.getString("Name"));
                st.setEmail(results.getString("Email"));
                st.setPhone(results.getString("SoDT"));
                st.setAddress(results.getString("DiaChi"));
                st.setSex(results.getBoolean("GioiTinh"));
                list.add(st);
            }
        }
        return list;
    }

    public Student update(Student updateStudent) throws SQLException {
        Connection connection = MyConnection.getConnection();
        if (connection != null) {
            String sql = "UPDATE STUDENTS SET STUDENTS.Email = ?, STUDENTS.SoDT = ?, STUDENTS.GioiTinh = ?, STUDENTS.DiaChi = ?, STUDENTS.Name = ? WHERE STUDENTS.MaSV = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, updateStudent.getEmail());
            statement.setString(2, updateStudent.getPhone());
            statement.setBoolean(3, updateStudent.isSex());
            statement.setString(4, updateStudent.getAddress());
            statement.setString(5, updateStudent.getName());
            statement.setInt(6, updateStudent.getId());
            int results = statement.executeUpdate();
            if (results != 0) {
                return this.findById(updateStudent.getId());
            }
        }
        return null;

    }

    public boolean delete(int id) throws SQLException {
        Connection connection = MyConnection.getConnection();
        if (connection != null) {
            String sql = "DELETE STUDENTS WHERE STUDENTS.MaSV = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if (result == 0) {
                return false;
            }
        }
        return true;
    }
}
