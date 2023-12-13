/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author dinhh
 */
public class MyFile {

    public static void saveFile(Object object) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.dat"))) {
            oos.writeObject(object);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Object readFile() {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream("data.dat"))) {
            return oos.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
