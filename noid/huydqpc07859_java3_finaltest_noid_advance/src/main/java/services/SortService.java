/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.Entity;

/**
 *
 * @author dinhh
 */
public class SortService {

    public static boolean sort = false;

    public static List<Entity> sortIncrement(List<Entity> list) {
        // Using Collections.sort() with a lambda expression as the comparator
//        Collections.sort(list, (Entity a, Entity b) -> {
//            // Ternary operator to determine the sorting order based on the 'sort' variable
//            return sort ? a.getId().compareTo(b.getId()) : b.getId().compareTo(a.getId());
//        });
        return list;

    }

    public static List<Entity> sortInteger(List<Entity> list) {
        // Using Collections.sort() with a lambda expression as the comparator
        Collections.sort(list, (Entity a, Entity b) -> {
            // Ternary operator to determine the sorting order based on the 'sort' variable
            return sort ? Integer.valueOf(a.getSoGioLamViec()).compareTo(Integer.valueOf(b.getSoGioLamViec()))
                    : Integer.valueOf(b.getSoGioLamViec()).compareTo(Integer.valueOf(a.getSoGioLamViec()));
        });
        return list;

    }

}
