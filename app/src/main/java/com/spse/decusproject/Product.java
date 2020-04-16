package com.spse.decusproject;


public class Product {
    private String name,brand,category,date, productID;


    public Product() {}

    public Product(String name, String brand, String category, String date, String productID) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.date = date;
        this.productID = productID;
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

    public String getProductID() {
        return productID;
    }

    @Override
    public String toString() {

        return name+ " ("+brand+")" ;
    }
}
