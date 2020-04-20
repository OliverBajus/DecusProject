package com.spse.decusproject;

public class Allergen {
    String ingredientName;
    String description;
    String id;

    public Allergen() {
    }

    public Allergen(String ingredientName, String description,String id) {
        this.ingredientName = ingredientName;
        this.description = description;
        this.id=id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}
