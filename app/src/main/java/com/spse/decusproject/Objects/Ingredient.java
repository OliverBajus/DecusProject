package com.spse.decusproject.Objects;

public class Ingredient {

    private String name,description;

    public Ingredient() {
    }

    public Ingredient(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public String getIngredientName() {
        return name;
    }

    public String getIngredientDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
