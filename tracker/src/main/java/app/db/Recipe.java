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
    
    // ---------------------------------BASE RECIPE-------------------------
    @Override
    public String showName() {
        return name;
    }

    @Override
    public String showUnit() {
        return "servings";
    }

    public HashMap<String, Double> showIngredientList() {
        return ingredientsList;
    }

    public int numberIngredients() {
        return ingredientsList.size();
    }

    public void edit(String name, double servings) {
        this.name = name;
        this.weight = servings;
    }


    // ----------------------------INSTRUCTIONS----------------------------
    public void addInstructions(String instruct) {
        instructions = instruct;
    }

    public String showInstructions() {
        return instructions;
    }


    // ----------------------------INGREDIENTS--------------------------
    public double showIngredientWeight(String name) {
        double weight = ingredientsList.get(name);
        return weight;
    }

    //This is separated from the main addIngredient() method to allow for unit testing
    public boolean addFoodIngredient(Food food, String name, double weight) {
        double[] nutr = food.showUnitNutrition();
        boolean ingredientExists = false;
        if (!ingredientsList.containsKey(name)) {
            ingredientsList.put(name, weight);
        } else {
            ingredientExists = true;
            double oldWeight = ingredientsList.get(name);
            double newWeight = oldWeight + weight;
            ingredientsList.put(name, newWeight);
        }
        
        food.addRecipe(this);
        for (String type: food.showFoodTypes()) {
            addFoodType(type);
        }
        double[] weighted = new double[8];
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = nutr[i] * weight;
            nutrition[i] = nutrition[i] + weighted[i];
        } 

        return ingredientExists;
    }


    public boolean addIngredient(String name, double weight) {
        SupFood ingredient;
        boolean ingredientExists = false;
        try {
            ingredient = data.findItem(name);
            if (ingredient instanceof Food) {
                Food ingr = (Food) ingredient;
                ingredientExists = addFoodIngredient(ingr, name, weight);
                
            } else {
                Recipe rec = (Recipe) ingredient;
                addFromRecipe(rec, weight);
            }
        } catch (NullPointerException e) {
            //System.out.println("Not in database! ");
        } 

        return ingredientExists;
    }

    public void addIngredientFromGUI(String foodName, double amount) {
        boolean ingredientsExists = addIngredient(foodName, amount);
        // If ingredient has previously been added to the recipe, remove it from the database and then
        // re-add with the updated amount
        if (ingredientsExists) {
            EditFoodRecipeDatabase.removeIngredient(name, foodName);
        }
        EditFoodRecipeDatabase.addRecipeIngredient(name, foodName, ingredientsList.get(foodName)); 
    }

    // This allows the user to add an existing recipe to this one, resulting in all the ingredients from
    // that recipe being added to this.
    public void addFromRecipe(Recipe rec, double servings) {
        for (String name: rec.showIngredientList().keySet()) {
            double weight = rec.showIngredientList().get(name);
            double totalServings = rec.weight();
            double newWeight = weight * ((double) servings / totalServings);
            weight = (double) newWeight;
            addIngredient(name, weight);
        }
    }

    /*public boolean checkIngredientFoodTypes(String ingr, String type) {
        //for (Food ingredient: showIngredients().values()) {
        //    if (!ingredient.showName().equals(ingr) && ingredient.showFoodTypes().contains(type)) {
        //        return true;
        //    } 
        //}
        for (String name: showIngredientList().keySet()) {
            Food food = (Food) data.findItem(name);
            if (!name.equals(ingr) && food.showFoodTypes().contains(type)) {
                return true;
            }
        }
        return false;
    }*/

    public void removeIngredient1(Food food, String name) {
        if (ingredientsList.keySet().contains(name)) {
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
                if (type != null) {
                    removeFoodType(type);
                }
                
            }
            food.removeRecipe(this);
            ingredientsList.remove(name);
        }
        
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


    // ----------------------------TYPES------------------------

    //Food types are stored in a HashMap in order to keep track of how many ingredients have each type.
    //This means that if two ingredients have the same food type, and we remove one, we know not to remove 
    //the type from the recipe.
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

}