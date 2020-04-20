package com.spse.decusproject;


public class Allergen {
    String ingredientName;
    String id;

    public Allergen() {
    }

    public Allergen(String ingredientName,String id) {
        this.ingredientName = ingredientName;
        this.id = id;
    }

    public Allergen(String object) {
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getId() {
        return id;
    }
}
