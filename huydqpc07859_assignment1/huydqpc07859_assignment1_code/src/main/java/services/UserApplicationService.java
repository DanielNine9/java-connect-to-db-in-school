/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import models.Role;
import models.UserApplication;
import repositories.UserApplicationRepository;

/**
 *
 * @author dinhh
 */
public class UserApplicationService {

    static private UserApplicationService instance;

    private UserApplicationService() {
    }

    static synchronized public UserApplicationService getInstance() {
        if (instance == null) {
            instance = new UserApplicationService();
        }
        return instance;
    }

    public boolean register(String username, String password, Role role) {
        UserApplication userExists = UserApplicationRepository.getUser(username);

        if (userExists != null) {
            return false;
        }

        UserApplicationRepository.addUser(new UserApplication(username, password, role));
        return true;
    }

    public UserApplication login(String username, String password) {
        UserApplication userExists = UserApplicationRepository.getUser(username);

        if (userExists == null) {
            return null;
        }

        if (userExists.getPassword().equals(password)) {
            return userExists;
        }

        return null;
    }

}
