/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Entity;
import utils.DatabaseUtil;

/**
 *
 * @author huyyd
 */
public class EntityRepository {

    private String entity = "orders_auto";

    public List<Entity> getAllEntity() {
        Connection con = DatabaseUtil.getConnection();
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "SELECT * FROM " + entity;
        List<Entity> list = new ArrayList<>();
        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(new Entity(rs));
                }
            } catch (SQLException ex) {
                Logger.getLogger(EntityRepository.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeAllConnection(con, pst, rs);
            }
        }
        return list;
    }

    public Entity findById(String entityId) {
        Connection con = DatabaseUtil.getConnection();
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "SELECT * FROM " + entity + " WHERE id = ?";
        Entity entity = null;

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, entityId);
                rs = pst.executeQuery();

                if (rs.next()) {
                    entity = (Entity) new Entity(rs);
                }

            } catch (SQLException ex) {
                Logger.getLogger(EntityRepository.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeAllConnection(con, pst, rs);
            }
        }
        return entity;
    }

    public boolean deleteEntity(String entityId) {
        Connection con = DatabaseUtil.getConnection();
        PreparedStatement pst = null;
        String sql = "DELETE FROM " + entity + " WHERE id = ?";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, entityId);
                int rowsAffected = pst.executeUpdate();
                return rowsAffected > 0;

            } catch (SQLException ex) {
                Logger.getLogger(EntityRepository.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeAllConnection(con, pst, null);
            }
        }
        return false;
    }

    public boolean updateEntity(Entity updatedEntity) {
        Connection con = DatabaseUtil.getConnection();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                // Get the class of the entity
                Class<?> entityClass = updatedEntity.getClass();

                // Get all fields of the entity class (including private ones)
                Field[] fields = entityClass.getDeclaredFields();

                // Build the SQL update statement
                StringBuilder sql = new StringBuilder("UPDATE " + entity + " SET ");

                for (Field field : fields) {
                    if (field.getName().equalsIgnoreCase("id")) {
                        continue;
                    }
                    // Exclude static fields
                    if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                        // Set the field accessible to read private fields
                        field.setAccessible(true);

                        // Append the field name to the SQL statement
                        sql.append(field.getName()).append(" = ?, ");
                    }
                }

                // Remove the trailing comma and space
                sql.delete(sql.length() - 2, sql.length());

                // Append the WHERE clause
                sql.append(" WHERE id = ?");

                // Prepare the statement
                pst = con.prepareStatement(sql.toString());

                // Set parameter values
                int parameterIndex = 1;
                for (Field field : fields) {
                    if (field.getName().equalsIgnoreCase("id")) {
                        continue;
                    }
                    if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                        // Set the field accessible to read private fields
                        field.setAccessible(true);

                        // Get the field value from the updatedEntity
                        Object value = field.get(updatedEntity);

                        // Set the parameter value in the prepared statement
                        pst.setObject(parameterIndex++, value);
                    }
                }

                // Set the WHERE clause parameter
                pst.setString(parameterIndex, updatedEntity.getId());

                // Execute the update
                int rowsAffected = pst.executeUpdate();
                return rowsAffected > 0;

            } catch (SQLException | IllegalAccessException ex) {
                Logger.getLogger(EntityRepository.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeAllConnection(con, pst, null);
            }
        }
        return false;
    }

    public boolean createEntity(Entity newEntity) {
        Connection con = DatabaseUtil.getConnection();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                // Build the SQL INSERT statement
                StringBuilder sql = new StringBuilder("INSERT INTO " + entity + " (");

                int isId = 0;
                // Append column names
                for (Field field : newEntity.getClass().getDeclaredFields()) {
//                    if (field.getName().equalsIgnoreCase("id")) {
//                        isId = -1;
//                        continue;
//                    }
                    field.setAccessible(true);
                    sql.append(field.getName()).append(", ");
                }
                // Remove the trailing comma and space
                sql.delete(sql.length() - 2, sql.length());

                // Continue building the SQL statement
                sql.append(") VALUES (");

                // Append placeholders for values
                for (int i = 0; i < newEntity.getClass().getDeclaredFields().length + isId; i++) {
                    sql.append("?, ");
                }
                // Remove the trailing comma and space
                sql.delete(sql.length() - 2, sql.length());

                // Complete the SQL statement
                sql.append(")");

                // Prepare the statement
                pst = con.prepareStatement(sql.toString());

                // Set parameters
                int parameterIndex = 1;
                for (java.lang.reflect.Field field : newEntity.getClass().getDeclaredFields()) {
//                    if (field.getName().equalsIgnoreCase("id")) {
//                        continue;
//                    }
                    field.setAccessible(true);
                    pst.setObject(parameterIndex++, field.get(newEntity));
                }
                System.out.println("SQL Statement: " + sql.toString());

                // Execute the insert
                int rowsAffected = pst.executeUpdate();
                closeAllConnection(con, pst, null);
                return rowsAffected > 0;

            } catch (SQLException | IllegalAccessException ex) {
                Logger.getLogger(EntityRepository.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeAllConnection(con, pst, null);
            }
        }
        return false;
    }

    private void closeAllConnection(Connection con, PreparedStatement pst, ResultSet rs) {
        DatabaseUtil.closeConnection(con);
        DatabaseUtil.closePreparedStatement(pst);
        DatabaseUtil.closeResultSet(rs);

    }

}
