package app.db;

import java.util.HashSet;

public class Food extends SupFood{
    private String unit, displayName;
    private String barcode;
    private HashSet<String> foodType;
    private HashSet<Recipe> inRecipes;

    public Food(Database data, Integer index, String name, String displayName, double weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        super(data, index, name, weight);
        if (displayName == null) {
            this.displayName = name;
        } else {
            this.displayName = displayName;
        }
        this.barcode = barcode;
        this.unit = unit;
        foodType = new HashSet<>();
        inRecipes = new HashSet<>();
        this.nutrition[0] = calories; this.nutrition[1] = fat; this.nutrition[2] = satfat; this.nutrition[3] = carbs; this.nutrition[4] = sugar; this.nutrition[5] = fibre; this.nutrition[6] = protein; this.nutrition[7] = salt;
        }

    @Override
    public String showName() {
        return name;
    }

    @Override
    public String showDisplayName() {
        return displayName;
    }

    //When a food item is added to a recipe as an ingredient, that recipe is added to the inRecipes list
    public void addRecipe(Recipe recipe) {
        inRecipes.add(recipe);
    }

    public void removeRecipe(Recipe recipe) {
        inRecipes.remove(recipe);
    }

    public HashSet<Recipe> showRecipes() {
        return inRecipes;
    }

    //Add food type, and then add that type to all recipes which contain this food as an ingredient
    public void addFoodType(String type) {
        foodType.add(type);
        for (Recipe recipe: inRecipes) {
            recipe.addFoodType(type);
        }
    }

    public void removeFoodType(String type) {
        boolean removed = foodType.remove(type);
        if (removed) { 
        //Only remove food type from recipes if it has actually been removed from the ingredient
            for (Recipe recipe: inRecipes) {
                recipe.removeFoodType(type);
            }
        }
    }

    public HashSet<String> showFoodTypes() {
        return foodType;
    }

    public String showBarcode() {
        return barcode;
    }

    public String showWeight() {
        String show = weight +" " + unit;
        return show;
    }

    public double showAmount() {
        return weight;
    }
    
    @Override
    public String showUnit() {
        return unit;
    }

    public void edit(String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        this.name = name;
        this.displayName = displayName;
        this.weight = amount;
        this.unit = unit;
        this.barcode = barcode;
        nutrition[0] = calories; nutrition[1] = fat; nutrition[2] = satfat; nutrition[3] = carbs; nutrition[4] = sugar; nutrition[5] = fibre; nutrition[6] = protein; nutrition[7] = salt;
    }
}

