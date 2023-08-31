package src.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import src.SQL.java.connect.sql.code.*;

public class Recipe extends SupFood {
    private HashMap<String, Food> ingredients;
    private HashMap<String, Double> ingredientsList;
    private HashSet<String> foodTypes;
    private HashSet<String> recipeType;
    private HashSet<String> mealType;
    private HashMap<String, Double> newIngredients;
    //private double nutrition[];

    public Recipe(Database data, String name, double servings) {
        super(data, name, servings);
        this.ingredients = new HashMap<>();
        this.ingredientsList = new HashMap<>();
        foodTypes = new HashSet<>();
        recipeType = new HashSet<>();
        mealType = new HashSet<>();

        //this.nutrition = new double[8];

    }
    
    @Override
    public String showName() {
        return name;
    }

    @Override
    public String showUnit() {
        return "servings";
    }

    public void addFoodType(String type) {
        foodTypes.add(type);
    }

    public void removeFoodType(String type) {
        foodTypes.remove(type);
    }

    public HashSet<String> showFoodTypes() {
        return foodTypes;
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


    public void addIngredient(String name, double weight) {
        SupFood ingredient;
        try {
            ingredient = data.addFromDatabase(name);
            if (ingredient instanceof Food) {
                Food ingr = (Food) ingredient;
                double[] nutr = ingredient.unitNutrition();
                ingredients.put(name, ingr);
                ingredientsList.put(name, weight);
                foodTypes.addAll(ingr.showFoodTypes());
                double[] weighted = new double[8];
                for (int i = 0; i < nutrition.length; i++) {
                    weighted[i] = nutr[i] * weight;
                    nutrition[i] = nutrition[i] + weighted[i];
                } 

                try {
                    newIngredients.put(ingredient.showName(), weight);
                } catch (NullPointerException e) {};
                
            } else {
                Recipe rec = (Recipe) ingredient;
                addFromRecipe(rec, weight);
            }
        } catch (NullPointerException e) {
            //System.out.println("Not in database! ");
        } 
    }

    public void addIngredientFromGUI(String foodName, double amount) {
        //newIngredients = new HashMap<>();
        addIngredient(foodName, amount);
        EditFoodRecipeDatabase.addRecipeIngredient(name, foodName, ingredientsList.get(foodName)); 
        /*for (String itemName: newIngredients.keySet()) {
            
        }*/
         
    }

    public void addFromRecipe(Recipe rec, double servings) {
        for (String name: rec.showIngredients().keySet()) {
            System.out.println(name);
            double weight = rec.showIngredientList().get(name);
            double totalServings = rec.weight();
            double newWeight = weight * ((double) servings / totalServings);
            weight = (double) newWeight;
            addIngredient(name, weight);
        }
    }

    public boolean checkIngredientFoodTypes(String ingr, String type) {
        for (Food ingredient: showIngredients().values()) {
            if (!ingredient.showName().equals(ingr) && ingredient.showFoodTypes().contains(type)) {
                return true;
            } 
        }
        return false;
    }

    public void removeIngredient(String name) {
        Food food = ingredients.get(name);
        double[] weighted = new double[8];
        double weight = ingredientsList.get(name);
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = food.unitNutrition()[i] * weight; 
            nutrition[i] = nutrition[i] - weighted[i];
        }

        for (String type: food.showFoodTypes()) {
            if (!checkIngredientFoodTypes(name, type)) {
                removeFoodType(type);
            }
        }
        
        ingredients.remove(name);
        ingredientsList.remove(name);

        AddToDiary.removeIngredient(this.name, name);
    }

    public void editIngredient(String name, double amount) {
        removeIngredient(name);
        addIngredientFromGUI(name, amount);
    }

    public String perServing() {
        double[] perServ = unitNutrition();
        String text = String.format("%s \nCalories: %.0f \nFat: %.1f g \nSaturated Fat: %.1f g \nCarbs: %.1f g \nSugar: %.1f g \nFibre: %.1f g \nProtein: %.1f g \nSalt: %.1f g", 
        this.name, perServ[0], perServ[1], perServ[2], perServ[3], perServ[4], perServ[5], perServ[6], perServ[7]);
        return text;
    }


    public HashMap<String, Food> showIngredients() {
        return ingredients;
    }

    public HashMap<String, Double> showIngredientList() {
        return ingredientsList;
    }

    public String toString() {
        ArrayList<String> ingrdnts = new ArrayList<>();
        for (String name : ingredients.keySet()) {
            Food ingredient = ingredients.get(name);
            String strng = ingredientsList.get(name) + " " + ingredient.showUnit() + " " + name;
            ingrdnts.add(strng);
        }
        String list = name + ": " + String.join(", " , ingrdnts);
        return list;
    }


    
}