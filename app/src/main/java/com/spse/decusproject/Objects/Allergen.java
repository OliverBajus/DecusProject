package com.spse.decusproject.Objects;


public class Allergen {
    private String ingredientName;
    private String id;

    public Allergen(String ingredientName, String id) {
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
