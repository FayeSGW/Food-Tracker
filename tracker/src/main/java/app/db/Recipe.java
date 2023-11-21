package app.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import app.sql.java.connect.*;

public class Recipe extends SupFood {
    private HashMap<String, Double> ingredientsList;
    private HashMap<String, Integer> foodTypes;
    private HashSet<String> recipeType;
    private HashSet<String> mealType;
    private String instructions;

    public Recipe(Database data, String name, double servings) {
        super(data, name, servings);
        this.ingredientsList = new HashMap<>();
        foodTypes = new HashMap<>();
        recipeType = new HashSet<>();
        mealType = new HashSet<>();
    }
    
    @Override
    public String showName() {
        return name;
    }

    @Override
    public String showUnit() {
        return "servings";
    }

    public void addInstructions(String instruct) {
        instructions = instruct;
    }

    public String sbowInstructions() {
        return instructions;
    }

    public void addFoodType(String type) {
        int number = 1;
        if (foodTypes.keySet().contains(type)) {
            number += foodTypes.get(type);
        }
        foodTypes.put(type, number);
    }

    public void removeFoodType(String type) {
        if (foodTypes.get(type) > 1) {
            int number = foodTypes.get(type) -1;
            foodTypes.put(type, number);
        } else {
            foodTypes.remove(type);
        }
    }

    public Set<String> showFoodTypes() {
        return foodTypes.keySet();
    }

    public void addRecipeType(String type) {
        recipeType.add(type);
    }

    public void removeRecipeType(String type) {
        recipeType.remove(type);
    }

    public HashSet<String> showRecipeType() {
        return recipeType;
    }

    public void addMealType (String type) {
        mealType.add(type);
    }

    public void removeMealType(String type) {
        mealType.remove(type);
    }

    public HashSet<String> showMealType() {
        return mealType;
    }

    public double showIngredientWeight(String name) {
        double weight = ingredientsList.get(name);
        return weight;
    }

    public void addFoodIngredient(Food food, String name, double weight) {
        double[] nutr = food.showUnitNutrition();
        ingredientsList.put(name, weight);
        food.addRecipe(this);
        for (String type: food.showFoodTypes()) {
            addFoodType(type);
        }
        double[] weighted = new double[8];
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = nutr[i] * weight;
            nutrition[i] = nutrition[i] + weighted[i];
        } 
    }


    public void addIngredient(String name, double weight) {
        SupFood ingredient;
        try {
            ingredient = data.addFromDatabase(name);
            if (ingredient instanceof Food) {
                Food ingr = (Food) ingredient;
                addFoodIngredient(ingr, name, weight);
                
            } else {
                Recipe rec = (Recipe) ingredient;
                addFromRecipe(rec, weight);
            }
        } catch (NullPointerException e) {
            //System.out.println("Not in database! ");
        } 
    }

    public void addIngredientFromGUI(String foodName, double amount) {
        addIngredient(foodName, amount);
        EditFoodRecipeDatabase.addRecipeIngredient(name, foodName, ingredientsList.get(foodName)); 
    }

    public void addFromRecipe(Recipe rec, double servings) {
        for (String name: rec.showIngredientList().keySet()) {
            double weight = rec.showIngredientList().get(name);
            double totalServings = rec.weight();
            double newWeight = weight * ((double) servings / totalServings);
            weight = (double) newWeight;
            addIngredient(name, weight);
        }
    }

    public boolean checkIngredientFoodTypes(String ingr, String type) {
        /*for (Food ingredient: showIngredients().values()) {
            if (!ingredient.showName().equals(ingr) && ingredient.showFoodTypes().contains(type)) {
                return true;
            } 
        }*/
        for (String name: showIngredientList().keySet()) {
            Food food = (Food) data.findItem(name);
            if (!name.equals(ingr) && food.showFoodTypes().contains(type)) {
                return true;
            }
        }
        return false;
    }

    public void removeIngredient1(Food food, String name) {
        double[] weighted = new double[8];
        double weight = ingredientsList.get(name);
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = food.showUnitNutrition()[i] * weight; 
            nutrition[i] = nutrition[i] - weighted[i];
        }

        for (String type: food.showFoodTypes()) {
            /*if (!checkIngredientFoodTypes(name, type)) {
                removeFoodType(type);
            }*/
            removeFoodType(type);
        }
        
        ingredientsList.remove(name);
    }

    public void removeIngredient(String name) {
        Food food = (Food) data.findItem(name);
        removeIngredient1(food, name);        

        EditFoodRecipeDatabase.removeIngredient(this.name, name);
    }

    public void editIngredient(String name, double amount) {
        removeIngredient(name);
        addIngredientFromGUI(name, amount);
    }

    public String perServing() {
        double[] perServ = showUnitNutrition();
        String text = String.format("%s \nCalories: %.0f \nFat: %.1f g \nSaturated Fat: %.1f g \nCarbs: %.1f g \nSugar: %.1f g \nFibre: %.1f g \nProtein: %.1f g \nSalt: %.1f g", 
        this.name, perServ[0], perServ[1], perServ[2], perServ[3], perServ[4], perServ[5], perServ[6], perServ[7]);
        return text;
    }

    public HashMap<String, Double> showIngredientList() {
        return ingredientsList;
    }

    public int numberIngredients() {
        return ingredientsList.size();
    }

}