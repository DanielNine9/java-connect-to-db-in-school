/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai3;

import bai3.User;
import bai3.MyFile;
import java.util.ArrayList;

/**
 *
 * @author dinhh
 */
public class UserRepository {

    private ArrayList<User> list;
    private static UserRepository instance;

    private UserRepository() {
        this.list = MyFile.readFile() == null ? new ArrayList<>() : (ArrayList<User>) MyFile.readFile();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public void add(User newUser) {
        this.list.add(newUser);
        MyFile.saveFile(this.list);
    }

    public boolean checkUser(User user) {
        if (!this.checkConflig(user.getUsername())) {
            return false;
        } else {
            for (User x : this.list) {
                if (x.getUsername().equalsIgnoreCase(user.getUsername()) && x.getPassword().equalsIgnoreCase(user.getPassword())) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean checkConflig(String username) {
        return this.list.stream().anyMatch(user -> user.getUsername().equals(username));
    }
}
