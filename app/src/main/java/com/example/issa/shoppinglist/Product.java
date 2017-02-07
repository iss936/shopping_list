package com.example.issa.shoppinglist;

/**
 * Created by philippe on 04/02/2017.
 */

public class Product {

    private String name;
    private int id;
    private int quantity;
    private double price;


    public Product(String name, int quantity, double price, int id) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(int id) {this.id = id;}

    public int getId(){ return this.id;}
}
