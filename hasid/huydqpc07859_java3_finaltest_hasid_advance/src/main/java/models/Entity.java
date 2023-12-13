/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import utils.DatabaseUtil;

/**
 *
 * @author huyyd
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Entity {

    private int id;
    private String name;
    private String url;

//    public Entity(ResultSet rs) throws SQLException {
//        this.id = rs.getInt("id");
//        this.name = rs.getString("name");
//        this.url = rs.getString("url");
//    }
    
     
   public Entity(ResultSet rs) throws SQLException {
        Field[] fields = getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);

                String fieldName = field.getName();
                Class<?> fieldType = field.getType();

                if (fieldType == String.class) {
                    field.set(this, rs.getString(fieldName));
                } else if (fieldType == int.class) {
                    field.set(this, rs.getInt(fieldName));
                } else if (fieldType == double.class || fieldType == Double.class) {
                    field.set(this, rs.getDouble(fieldName));
                } else if (fieldType == float.class) {
                    field.set(this, rs.getFloat(fieldName));
                } else if (fieldType == boolean.class) {
                    field.set(this, rs.getBoolean(fieldName));
                } else if (fieldType == Date.class) {
                    field.set(this, rs.getDate(fieldName));
                } else if (fieldType == long.class) {
                    field.set(this, rs.getLong(fieldName));
                } else if (fieldType == short.class) {
                    field.set(this, rs.getShort(fieldName));
                } else if (fieldType == byte.class) {
                    field.set(this, rs.getByte(fieldName));
                } else if (fieldType == java.sql.Timestamp.class) {
                    field.set(this, rs.getTimestamp(fieldName));
                } else if (fieldType == java.sql.Time.class) {
                    field.set(this, rs.getTime(fieldName));
                }
                // Add more conditions for other data types as needed
            }
        } catch (IllegalAccessException e) {
            throw new SQLException("Error mapping ResultSet to Entity", e);
        }
    }
    public static String generateCreateTableSQL(String tableName) {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE " + tableName + " (");

        Field[] fields = Entity.class.getDeclaredFields();
        List<String> columnDefinitions = new ArrayList<>();

        for (Field field : fields) {
            String columnName = field.getName();
            String columnType = mapFieldTypeToSQL(field.getType());

            // Customize column definition as needed (e.g., add constraints)
            String columnDefinition = columnName + " " + columnType;
            if(field.getName().contains("id")){
                columnDefinition += " IDENTITY(1,1) PRIMARY KEY";
            }
            columnDefinitions.add(columnDefinition);
        }

        sqlBuilder.append(String.join(", ", columnDefinitions));
        sqlBuilder.append(")");
        System.out.println(sqlBuilder);
        try (Connection connection = DatabaseUtil.getConnection(); Statement statement = connection.createStatement()) {

            // Execute the SQL statement
            statement.executeUpdate(sqlBuilder.toString());

            System.out.println("Table created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sqlBuilder.toString());
        return sqlBuilder.toString();
    }

    private static String mapFieldTypeToSQL(Class<?> fieldType) {
        if (fieldType == String.class) {
            return "NVARCHAR(255)";
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return "INT";
        } else if (fieldType == double.class || fieldType == Double.class) {
            return "FLOAT";
        } else if (fieldType == float.class || fieldType == Float.class) {
            return "FLOAT";
        } else if (fieldType == long.class || fieldType == Long.class) {
            return "BIGINT";
        } else if (fieldType == short.class || fieldType == Short.class) {
            return "SMALLINT";
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            return "TINYINT";
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return "BOOLEAN";
        } else if (fieldType == java.util.Date.class || fieldType == java.sql.Date.class) {
            return "DATE";
        } else if (fieldType == java.sql.Timestamp.class) {
            return "TIMESTAMP";
        } else if (fieldType == java.sql.Time.class) {
            return "TIME";
        }
        // Add more mappings for other data types as needed

        // Default to VARCHAR(255) for unknown types
        return "NVARCHAR(255)";
    }


}
