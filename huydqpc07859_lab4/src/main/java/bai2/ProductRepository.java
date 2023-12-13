/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.MyFile;

/**
 *
 * @author huyyd
 */
public class ProductRepository {

    private String fileName = "data.dat";
    ArrayList<Product> list;

    public ProductRepository() {
        try {
            ArrayList<Product> result = (ArrayList<Product>) MyFile.readObject(fileName);
            list = result;
        } catch (Exception e) {
            list = new ArrayList();
        }
    }

    public ArrayList<Product> getList() {

        return list;
    }

    public boolean add(Product product) {
        for (Product prod : list) {
            if (prod.getId().equals(product.getId())) {
                return false;
            }
        }
        if (list.add(product)) {
            saveFile();
            return true;
        }
        return false;
    }

    private void saveFile() {
        try {
            MyFile.writeObject(fileName, this.list);
        } catch (IOException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean update(Product product) {
        for (Product prod : list) {
            if (prod.getId().equals(product.getId())) {
                prod.setName(product.getName());
                prod.setDonVi(product.getDonVi());
                prod.setPrice(product.getPrice());
                prod.setSource(product.getSource());
                saveFile();
                return true;
            }
        }
        return false;
    }

    public boolean delete(Product product) {
        for (Product prod : list) {
            if (prod.getId().equals(product.getId())) {
                list.remove(prod);
                saveFile();
                return true;
            }
        }
        return false;
    }

    Optional<Product> find(String id) {
        return this.list.stream().filter(prd -> prd.getId().equals(id)).findFirst();
    }

}
