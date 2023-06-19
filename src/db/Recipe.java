package src.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Recipe extends SupFood {
    private HashMap<String, Food> ingredients;
    private HashMap<String, Integer> ingredientsList;
    private HashSet<String> foodTypes;
    private HashSet<String> recipeType;
    private HashSet<String> mealType;
    //private double nutrition[];

    public Recipe(Database data, String name, int servings) {
        super(data, name, servings);
        this.ingredients = new HashMap<>();
        this.ingredientsList = new HashMap<>();
        foodTypes = new HashSet<>();
        recipeType = new HashSet<>();
        mealType = new HashSet<>();
        //this.nutrition = new double[8];

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


    public void addIngredient(String name, int weight) {
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
            } else {
                Recipe rec = (Recipe) ingredient;
                addFromRecipe(rec, weight);
            }
        } catch (NullPointerException e) {
            //System.out.println("Not in database! ");
        } 
    }

    public void addFromRecipe(Recipe rec, int servings) {
        for (String name: rec.showIngredients().keySet()) {
            System.out.println(name);
            int weight = rec.showIngredientList().get(name);
            int totalServings = rec.weight();
            double newWeight = weight * ((double) servings / totalServings);
            weight = (int) newWeight;
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
        int weight = ingredientsList.get(name);
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

    public HashMap<String, Integer> showIngredientList() {
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