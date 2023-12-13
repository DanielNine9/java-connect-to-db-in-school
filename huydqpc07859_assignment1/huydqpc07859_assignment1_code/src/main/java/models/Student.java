/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author dinhh
 */
public class Student {

    private String id, fullName, email, phoneNumber, address, image;
    private int sex;
    private double english, tech, physicalEducation;

    public Student() {
    }

    public Student(String id, String fullName, String email, String phoneNumber, String address, int sex) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.sex = sex;
    }

    public Student(String fullName, String email, String phoneNumber, String address, int sex, String image) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.sex = sex;
        this.image = image;
    }

    public Student(String id, String fullName, String email, String phoneNumber, String address, int sex, double english, double tech, double physicalEducation) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.sex = sex;
        this.english = english;
        this.tech = tech;
        this.physicalEducation = physicalEducation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getEnglish() {
        return english;
    }

    public void setEnglish(double english) {
        this.english = english;
    }

    public double getTech() {
        return tech;
    }

    public void setTech(double tech) {
        this.tech = tech;
    }

    public double getPhysicalEducation() {
        return physicalEducation;
    }

    public void setPhysicalEducation(double physicalEducation) {
        this.physicalEducation = physicalEducation;
    }

}
