/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author huyyd
 */
public class Student {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private boolean sex; // false : female true : male

    public Student() {
    }

    public Student(int id, String name, String email, boolean sex, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.sex = sex;
    }

    public Student(String name, String email, boolean sex, String phone,String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "{"
                + "\nID: " + this.id
                + "\nName: " + this.name
                + "\nEmail: " + this.email
                + "\nPhone: " + this.phone
                + "\nAddress: " + this.address
                + "\nSex: " + (this.sex ? "nam" : "nu")
                + "\n}";
    }

}
