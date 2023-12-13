/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai2;

import java.io.Serializable;

/**
 *
 * @author huyyd
 */
public class Product implements Serializable{
    private String id, name, source, donVi;
    private double price;

    public Product() {
    }

    public Product(String id, String name, String source, String donVi, double price) {
        System.out.println("source " + source);
        this.id = id;
        this.name = name;
        this.source = source;
        this.donVi = donVi;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
    
}
