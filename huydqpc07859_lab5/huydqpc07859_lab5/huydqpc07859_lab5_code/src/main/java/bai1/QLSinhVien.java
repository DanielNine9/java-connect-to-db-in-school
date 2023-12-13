/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai1;

import java.sql.Connection;
import connection.MyConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Student;

/**
 *
 * @author huyyd
 */
public class QLSinhVien {

    public static void main(String[] args) {
        Connection connection = MyConnection.getConnection();
        PreparedStatement statement = null;
        if (connection != null) {
            String sql = "SELECT * FROM STUDENTS";
            try {
                statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery();
                ;
                ArrayList<Student> list = new ArrayList<>();
                while (results.next()) {
                    Student st = new Student(results.getInt("masv"), results.getString("name"), results.getString("email"), results.getBoolean("gioitinh"), results.getString("sodt"), results.getString("diachi"));
                    list.add(st);
                }

                if (list.isEmpty()) {
                    System.out.println("Khong tim thay sinh vien nao");
                } else {
                    for (Student st : list) {
                        System.out.println(st.toString());
                        System.out.println("");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                try {
                    connection.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
