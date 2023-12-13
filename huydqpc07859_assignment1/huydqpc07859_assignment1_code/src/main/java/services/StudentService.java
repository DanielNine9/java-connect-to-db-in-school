/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.List;
import models.Role;
import models.Student;
import models.UserApplication;
import repositories.StudentRepository;

/**
 *
 * @author dinhh
 */
public class StudentService {

    static private StudentService instance;

    private StudentService() {
    }

    static synchronized public StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public boolean addStudent(UserApplication user, Student std) {
        if(user == null){
            return false;
        }
        if (!user.getRole().equals(Role.Manager)) {
            return false;
        }

        boolean isSuccess = StudentRepository.addStudent(std);
        if (isSuccess) {
            return true;
        }

        return false;
    }

    public boolean updateMarks(UserApplication user, Student std) {
        if (!user.getRole().equals(Role.Teacher)) {
            return false;
        }

        boolean isSuccess = StudentRepository.updateStudent(user.getRole(), std);

        if (isSuccess) {
            return true;
        }
        return false;
    }

    public boolean deleteStudent(UserApplication user, String idStudent) {
        if (!user.getRole().equals(Role.Manager)) {
            return false;
        }

        boolean isSuccess = StudentRepository.deleteStudent(idStudent);
        if (isSuccess) {
            return true;
        }

        return false;
    }

    public List<Student> getStudents() {
        return StudentRepository.getAllStudents();
    }

    public Student getStudent(String id) {
        return StudentRepository.getStudent(id);
    }

    public Student updateInfoStudent(UserApplication user, Student std) {
        if (!user.getRole().equals(Role.Manager)) {
            return null;
        }

        boolean isSuccess = StudentRepository.updateStudent(user.getRole(), std);
        if (isSuccess) {
            return getStudent(std.getId());
        }

        return null;
    }
    
    public List<Student> top10Student() {
        return StudentRepository.getTopTenStudents();
    }
}
