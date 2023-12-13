///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package repositories;
//
//import java.lang.reflect.Field;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import models.Entity;
//
///**
// *
// * @author huyyd
// */
//public class Test {
//
//    public static void main(String[] args) {
//        Field[] fields = Entity.class.getDeclaredFields();
//        Entity entity = Entity.builder().name("Name").id(1).price(213).quantity(1000).test("testhaha")
//                
//                .build();
//
//        for (Field field : fields) {
//            field.setAccessible(true);
//            try {
//                System.out.println(field.getName() + " " + field.get(entity));
//            } catch (IllegalArgumentException ex) {
//                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IllegalAccessException ex) {
//                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//
//    }
//
//}
