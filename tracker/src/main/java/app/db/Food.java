package app.db;

import java.util.ArrayList;
import java.util.Scanner;
import app.sql.java.connect.*;

public class Food extends SupFood{
    Scanner scanner = new Scanner(System.in);
    private String unit, displayName;
    private String barcode;
    private ArrayList<String> foodType;

    public Food(Database data, String name, String displayName, double weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        super(data, name, weight);
        if (displayName == null) {
            this.displayName = name;
        } else {
            this.displayName = displayName;
        }
        this.barcode = barcode;
        this.unit = unit;
        foodType = new ArrayList<>();
        this.nutrition[0] = calories; this.nutrition[1] = fat; this.nutrition[2] = satfat; this.nutrition[3] = carbs; this.nutrition[4] = sugar; this.nutrition[5] = fibre; this.nutrition[6] = protein; this.nutrition[7] = salt;
        }

    @Override
    public String toString() {
        String text = String.format("%s: \n%s \n%.0f calories \n%.1f g fat \n%.1f g saturated fat \n%.1f g carbohydrates\n%.1f g sugar \n%.1f g fibre \n%.1f g protein \n%.1f g salt", name, showWeight(), this.nutrition[0], this.nutrition[1], this.nutrition[2], this.nutrition[3], this.nutrition[4], this.nutrition[5], this.nutrition[6], this.nutrition[7]);
        return text;
    }

    @Override
    public String showName() {
        return name;
    }

    @Override
    public String showDisplayName() {
        return displayName;
    }

    public void addFoodType(String type) {
        foodType.add(type);
        for (SupFood food: data.access().values()) {
            if (food instanceof Recipe) {
                Recipe recipe = (Recipe) food;
                if (recipe.showIngredients().keySet().contains(name)) {
                    recipe.addFoodType(type);
                }
            }
        }
    }

    public void removeFoodType(String type) {
        foodType.remove(type);
        for (SupFood food: data.access().values()) {
            if (food instanceof Recipe) {
                Recipe recipe = (Recipe) food;
                if (!recipe.checkIngredientFoodTypes(name, type)) {
                    recipe.removeFoodType(type);
                }
            }
        }
    }

    public ArrayList<String> showFoodTypes() {
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

