package com.spse.decusproject;

public class Allergen {
    String ingredientName;
    String description;

    public Allergen() {
    }

    public Allergen(String ingredientName, String description) {
        this.ingredientName = ingredientName;
        this.description = description;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getDescription() {
        return description;
    }
}
