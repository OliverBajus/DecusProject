package com.spse.decusproject;

import java.util.Date;

public class DailyRoutine {

    String id,productID,dayPart,date;

    public DailyRoutine() {
    }

    public DailyRoutine(String id, String productID, String dayPart, String date) {
        this.id = id;
        this.productID = productID;
        this.dayPart = dayPart;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getProductID() {
        return productID;
    }

    public String getDayPart() {
        return dayPart;
    }

    public String getDate() {
        return date;
    }
}
