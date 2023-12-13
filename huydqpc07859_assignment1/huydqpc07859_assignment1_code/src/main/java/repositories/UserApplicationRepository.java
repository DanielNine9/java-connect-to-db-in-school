/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import models.Role;
import models.UserApplication;
import utils.DatabaseUtil;

/**
 *
 * @author dinhh
 */
public class UserApplicationRepository {

    public static boolean addUser(UserApplication user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseUtil.getConnection();
            String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().equals(Role.Manager) ? "Manager" : "Teacher");

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResultSet(resultSet);
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }
        return false;
    }

//     public static void addUser(UserApplication user) {
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//
//        try {
//            connection = DatabaseUtil.getConnection();
//            String insertQuery = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
//            preparedStatement = connection.prepareStatement(insertQuery);
//            preparedStatement.setString(1, user.getUsername());
//            preparedStatement.setString(2, user.getPassword());
//            preparedStatement.setString(3, user.getId());
//
//            int rowsAffected = preparedStatement.executeUpdate();
//
//            if (rowsAffected > 0) {
//                System.out.println("Thêm người dùng thành công.");
//            } else {
//                System.out.println("Không có người dùng được thêm.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DatabaseUtil.closePreparedStatement(preparedStatement);
//            DatabaseUtil.closeConnection(connection);
//        }
//    }
    public static boolean updateUser(UserApplication user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseUtil.getConnection();
            String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getUsername());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }
        return false;
    }

    public static void deleteUser(int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseUtil.getConnection();
            String deleteQuery = "DELETE FROM users WHERE id = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, userId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Xóa người dùng thành công.");
            } else {
                System.out.println("Không có người dùng nào bị xóa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }
    }

    public static UserApplication getUser(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UserApplication user = null;

        try {
            connection = DatabaseUtil.getConnection();
            String selectQuery = "SELECT * FROM users WHERE username = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new UserApplication();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role").equalsIgnoreCase("Manager") ? Role.Manager : Role.Teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResultSet(resultSet);
            DatabaseUtil.closePreparedStatement(preparedStatement);
            DatabaseUtil.closeConnection(connection);
        }

        return user;
    }

}
