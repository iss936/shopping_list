package com.example.issa.shoppinglist;

/**
 * Created by Philippe on 04/02/2017.
 */

public class Product {
    private int quantity;
    private String name;
    private String text;


    public Product(int quantity, String name, String text) {
        this.quantity = quantity;
        this.name = name;
        this.text = text;
    }

    public int getquantity() {
        return quantity;
    }

    public void setquantity(int quantity) {
        this.quantity = quantity;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
