package app.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

import app.sql.java.connect.*;

public class Recipe extends SupFood {
    private HashMap<String, Double> ingredientsList = new HashMap<>();
    private HashMap<Integer, ArrayList<Object>> ingList = new HashMap<>();
    private HashMap<Integer, ArrayList<Object>> tempList = new HashMap<>();
    private HashMap<String, Double> tempIngredients = new HashMap<>();
    private HashMap<String, Integer> foodTypes = new HashMap<>();
    private HashSet<String> recipeType = new HashSet<>();
    private HashSet<String> mealType = new HashSet<>();
    private String instructions;

    public Recipe(Database data, Integer index, String name, double servings) {
        super(data, index, name, servings);
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

    public HashMap<String, Double> showTempIngredients() {
        return tempIngredients;
    }
    //returns list of all saved ingredients and temp ingredients, to show them in the edit recipe window
    public HashMap<Integer, ArrayList<Object>> showAllCurrentIngredients() {
        HashMap<Integer, ArrayList<Object>> list = new HashMap<>();
        for (Integer index: ingList.keySet()) {
            list.put(index, ingList.get(index));
        }
        for (Integer index: tempList.keySet()) {
            if ((double)tempList.get(index).get(1) == 0.0) {
                list.remove(index);
            } else {
                list.put(index, tempList.get(index));
            }
        }
        return list;
        /*HashMap<String, Double> all = new HashMap<>();
        for (String name: ingredientsList.keySet()) {
            all.put(name, ingredientsList.get(name));
        }
        for (String name: tempIngredients.keySet()) {
            if (tempIngredients.get(name) == 0.0) {
                all.remove(name);
            } else {
                all.put(name, tempIngredients.get(name));
            }
        }
        return all;*/
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
        double weight = 0;
        if (tempIngredients.keySet().contains(name)) {
            weight = tempIngredients.get(name);
        } else if (ingredientsList.keySet().contains(name)) {
            weight = ingredientsList.get(name);
        }
        return weight;
    }

    public void addTempIngredient(String name, double weight) {
        SupFood ingredient;
        ArrayList<Object> details = new ArrayList<>();
        try {
            ingredient = data.findItem(name);
            int index = ingredient.showIndex();
            if (ingredient instanceof Food) {
                if (ingList.containsKey(index)) {
                    double oldWeight = (double)ingList.get(index).get(1);
                    double newWeight = oldWeight + weight;
                    tempIngredients.put(name, newWeight);
                    details.add(name); details.add(newWeight);
                    tempList.put(index, details);
                }
                if (!tempList.containsKey(index)) {
                    details.add(name); details.add(weight);
                    tempList.put(index, details);
                } else {
                    double oldWeight = (double)tempList.get(index).get(1);
                    double newWeight = oldWeight + weight;
                    details.add(name); details.add(newWeight);
                    tempList.put(index, details);
                }
            } else {
                Recipe rec = (Recipe) ingredient;
                addFromRecipeTemp(rec, weight);
            }
        } catch (NullPointerException e) {} 
    }

    public void addFromRecipeTemp(Recipe rec, double servings) {
        for (String name: rec.showIngredientList().keySet()) {
            double weight = rec.showIngredientList().get(name);
            double totalServings = rec.weight();
            double newWeight = weight * ((double) servings / totalServings);
            weight = (double) newWeight;
            addTempIngredient(name, weight);
        }
    }

    public void editIngredient(String name, double amount) {
        tempIngredients.put(name, amount);
    }

    public void removeTempIngredient(String name) {
        tempIngredients.put(name, 0.0);
    }

    public void save() {
        for (int index: tempList.keySet()) {
            Food food = (Food) data.getItemFromIndex(index);
            ArrayList<Object> details = tempList.get(index);
            if ((double)details.get(1) == 0.0) {
                removeIngredientPermanently(food, index);
                tempList.remove(index);
                EditFoodRecipeDatabase.removeIngredient(name, (String)details.get(0));
            } else {
                double amount = (double)details.get(1);
                double[] nutr = food.showUnitNutrition();
                food.addRecipe(this);
                for (String type: food.showFoodTypes()) {
                    addFoodType(type);
                }
                double[] weighted = new double[8];
                for (int i = 0; i < nutrition.length; i++) {
                    weighted[i] = nutr[i] * weight;
                    nutrition[i] = nutrition[i] + weighted[i];
                } 
                details.set(1, amount);
                ingList.put(index, details);
                EditFoodRecipeDatabase.addRecipeIngredient(name, showIndex(), (String)details.get(0), index, amount);
            }
        }

        /*for (String foodName: tempIngredients.keySet()) {
            Food food = (Food) data.findItem(foodName);
            if (tempIngredients.get(foodName) == 0.0) {
                removeIngredientPermanently(food, foodName);
                tempIngredients.remove(foodName);
                EditFoodRecipeDatabase.removeIngredient(name, foodName);
            } else {
                double amount = tempIngredients.get(foodName);
                double[] nutr = food.showUnitNutrition();
                food.addRecipe(this);
                for (String type: food.showFoodTypes()) {
                    addFoodType(type);
                }
                double[] weighted = new double[8];
                for (int i = 0; i < nutrition.length; i++) {
                    weighted[i] = nutr[i] * weight;
                    nutrition[i] = nutrition[i] + weighted[i];
                } 
                ingredientsList.put(foodName, amount);
                EditFoodRecipeDatabase.addRecipeIngredient(name, foodName, amount);
            }
        }*/
    }

    public void clearTemp() {
        tempIngredients.clear();
    }

    public void addIngredientByName(String name, double weight) {
        Food ingredient = (Food) data.findItem(name);
        addIngredientFromDB(ingredient, weight);
    }

    public void addIngredientByIndex(int index, double weight) {
        Food ingredient = (Food) data.getItemFromIndex(index);
        addIngredientFromDB(ingredient, weight);
    }

    private void addIngredientFromDB(Food ingredient, double weight) {
        double[] nutr = ingredient.showUnitNutrition();
        ingredientsList.put(ingredient.showName(), weight);
        ingredient.addRecipe(this);
        for (String type: ingredient.showFoodTypes()) {
            addFoodType(type);
        }
        double[] weighted = new double[8];
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = nutr[i] * weight;
            nutrition[i] = nutrition[i] + weighted[i];
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

    public void removeIngredientPermanently(Food food, int index) {
        if (ingList.keySet().contains(index)) {
            double[] weighted = new double[8];
            double weight = (double)ingList.get(index).get(1);
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
            ingList.remove(index);
        }

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