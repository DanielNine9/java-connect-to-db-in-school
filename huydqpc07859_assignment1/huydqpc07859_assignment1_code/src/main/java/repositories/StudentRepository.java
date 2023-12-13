/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import models.Role;
import models.Student;
import utils.DatabaseUtil;

/**
 *
 * @author dinhh
 */
public class StudentRepository {

    public static boolean addStudent(Student student) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseUtil.getConnection();
            String insertQuery = "INSERT INTO students (id, fullName, email, phoneNumber, address, sex, image, english, it, physical) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);
            String id = generateNewUserId(connection);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, student.getFullName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getPhoneNumber());
            preparedStatement.setString(5, student.getAddress());
            preparedStatement.setInt(6, student.getSex());
            preparedStatement.setString(7, student.getImage());
            preparedStatement.setFloat(8, 0);
            preparedStatement.setFloat(9, 0);
            preparedStatement.setFloat(10, 0);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }

        return false;
    }

    private static String generateNewUserId(Connection connection) throws SQLException {
        String newUserId = null;
        String query = "SELECT MAX(id) FROM students";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String currentMaxId = resultSet.getString(1);
            if (currentMaxId == null) {
                newUserId = "PC00001";
            } else {
                int maxId = Integer.parseInt(currentMaxId.substring(2));
                maxId++; // Tăng giá trị lên 1
                Formatter formatter = new Formatter();
                newUserId = formatter.format("PC%05d", maxId).toString();
            }
        } else {
            newUserId = "PC00001";
        }

        return newUserId;
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseUtil.getConnection();
            String selectQuery = "SELECT * FROM students";
            preparedStatement = connection.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getString("id"));
                student.setFullName(resultSet.getString("fullName"));
                student.setEmail(resultSet.getString("email"));
                student.setPhoneNumber(resultSet.getString("phoneNumber"));
                student.setAddress(resultSet.getString("address"));
                student.setSex(resultSet.getInt("sex"));
                student.setEnglish(resultSet.getDouble("english"));
                student.setTech(resultSet.getDouble("it"));
                student.setPhysicalEducation(resultSet.getDouble("physical"));
                student.setImage(resultSet.getString("image"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResultSet(resultSet);
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }

        return students;
    }

    public static boolean deleteStudent(String studentId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseUtil.getConnection();
            String deleteQuery = "DELETE FROM students WHERE id = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, studentId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }

        return false;
    }

    public static boolean updateStudent(Role role, Student student) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseUtil.getConnection();

            if (role.equals(Role.Manager)) {
                String updateQueryInfo = "UPDATE students SET "
                        + "fullName = ?, email = ?, phoneNumber = ?, address = ?, sex = ?, image = ? "
                        + "WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateQueryInfo);
                preparedStatement.setString(1, student.getFullName());
                preparedStatement.setString(2, student.getEmail());
                preparedStatement.setString(3, student.getPhoneNumber());
                preparedStatement.setString(4, student.getAddress());
                preparedStatement.setInt(5, student.getSex());
                preparedStatement.setString(6, student.getImage());
                preparedStatement.setString(7, student.getId());
            } else {
                String updateQueryScore = "UPDATE students SET "
                        + "english = ?, it = ?, physical = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateQueryScore);
                preparedStatement.setDouble(1, student.getEnglish());
                preparedStatement.setDouble(2, student.getTech());
                preparedStatement.setDouble(3, student.getPhysicalEducation());
                preparedStatement.setString(4, student.getId());
            }

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("row affected " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }

        return false;
    }

    public static Student getStudent(String idStudent) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        Student std = null;
        String query = "SELECT * FROM students WHERE id = ?";

        try {
            con = DatabaseUtil.getConnection();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, idStudent);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                std = new Student();
                std.setId(resultSet.getString("id"));
                std.setFullName(resultSet.getString("fullName"));
                std.setEmail(resultSet.getString("email"));
                std.setPhoneNumber(resultSet.getString("phoneNumber"));
                std.setAddress(resultSet.getString("address"));
                std.setSex(resultSet.getInt("sex"));
                std.setEnglish(resultSet.getDouble("english"));
                std.setTech(resultSet.getDouble("it"));
                std.setPhysicalEducation(resultSet.getDouble("physical"));
                std.setImage(resultSet.getString("image"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(con);
        }

        return std;
    }

    public static List<Student> getTopTenStudents() {
        List<Student> students = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseUtil.getConnection();
            String selectQuery = "SELECT TOP 10 *, (english + it + physical) / 3 AS avg FROM students "
                    + "WHERE english IS NOT NULL AND it IS NOT NULL AND physical IS NOT NULL "
                    + "ORDER BY avg DESC";
            preparedStatement = connection.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getString("id"));
                student.setFullName(resultSet.getString("fullName"));
                student.setEmail(resultSet.getString("email"));
                student.setPhoneNumber(resultSet.getString("phoneNumber"));
                student.setAddress(resultSet.getString("address"));
                student.setSex(resultSet.getInt("sex"));
                student.setEnglish(resultSet.getDouble("english"));
                student.setTech(resultSet.getDouble("it"));
                student.setPhysicalEducation(resultSet.getDouble("physical"));
                student.setImage(resultSet.getString("image"));

                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResultSet(resultSet);
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }

        return students;
    }

}
