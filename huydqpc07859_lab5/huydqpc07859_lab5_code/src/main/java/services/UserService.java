/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import models.Student;
import repository.UserRepository;

/**
 *
 * @author huyyd
 */
public class UserService {

    private final UserRepository userRepository;

    private static UserService instance;

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public ArrayList<Student> findAll() {
        try {
            return this.userRepository.findAll();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    public void add(Student st)
            throws SQLException {
        this.userRepository.add(st);
    }

    public Student update(Student st) throws SQLException {
        return this.userRepository.update(st);
    }

    public boolean delete(int id) throws SQLException {
        return this.userRepository.delete(id);
    }

    public Student findById(int id) {
        try {
            return this.userRepository.findById(id);
        } catch (SQLException ex) {
            return null;
        }
    }
}
