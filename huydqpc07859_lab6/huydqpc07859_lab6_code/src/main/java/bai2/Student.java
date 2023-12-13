/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai2;

import java.sql.Date;

/**
 *
 * @author huyyd
 */
public class Student {

    private Integer id;
    String name, address, parentName, contactNo, standard;
    private Long fees;
    private Date regDate;

    public Student() {
    }

    public Student(String name, String address, String parentName, String contactNo, String standard, Long fees, Date regdate) {
        this.name = name;
        this.address = address;
        this.parentName = parentName;
        this.contactNo = contactNo;
        this.standard = standard;
        this.fees = fees;
        this.regDate = regdate;
    }


    public Student(Integer id, String name, String address, String parentName, String contactNo, String standard, Long fees, Date regDate) {
        this.name = name;
        this.address = address;
        this.parentName = parentName;
        this.contactNo = contactNo;
        this.standard = standard;
        this.fees = fees;
        this.regDate = regDate;
    }

    public void setRegDate(Date regDate) {
        
        this.regDate = regDate;
    }

    public Date getRegDate() {
        return this.regDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Long getFees() {
        return fees;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }


}
