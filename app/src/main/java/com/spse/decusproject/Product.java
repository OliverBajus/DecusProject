package com.spse.decusproject;

import android.content.Intent;

import java.util.Date;

public class Product {
    private String name,brand,category,date,userID;

    public Product() {}

    public Product(String name, String brand, String category, String date, String userID) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.date = date;
        this.userID = userID;
    }

    public  String getName() {
        return name;
    }

    public  String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getUserID() {
        return userID;
    }
}
