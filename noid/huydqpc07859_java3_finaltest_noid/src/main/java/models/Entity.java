/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author huyyd
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Entity {

    private String id;
    private String name;

    private String url;

    public Entity(ResultSet rs) throws SQLException {
        this.id = rs.getString("id");
        this.name = rs.getString("name");
  
        this.url = rs.getString("url");
    }


}
