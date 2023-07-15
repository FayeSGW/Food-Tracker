package src.graphics;

import java.util.HashMap;

import src.SQL.java.connect.sql.code.*;
import src.db.*;
import src.diary.*;

class ChangeDatabaseControl {
    TrackerControl tControl;
    Database data;
    ChangeDatabaseGUI dbGUI;

    String recipeName;
    Recipe recipe;
    NewRecipeGUI rGUI;

    ChangeDatabaseControl(TrackerControl tControl) {
        this.tControl = tControl;
        data = tControl.showUser().accessDatabase();
        dbGUI = new ChangeDatabaseGUI(this);
    }

    public ChangeDatabaseGUI showDbGUI() {
        return dbGUI;
    }

    public void recipe(String name) {
        recipeName = name;
        recipe = (Recipe)data.findItem(name);
    }

    //Functionality for adding new foods to database/editing foods
    void newFoodGUI() {
        NewFoodGUI nGUI = new NewFoodGUI(this);
    }

    void newRecipeGUI() {
        rGUI = new NewRecipeGUI(this, tControl);
    }

    void edit() {
        tControl.addFoodDialogue(0, "edit");
    }

    void editFoodorRecipeGUI(String foodName) {
        SupFood supFood = data.findItem(foodName);
        if (supFood instanceof Food) {
            Food food = (Food)supFood;
            NewFoodGUI nGUI = new NewFoodGUI(this);
            double[] nutrition = food.showNutrition();
            nGUI.existingFoodData(food.showName(), food.showDisplayName(), food.showAmount(), food.showUnit(), nutrition[0], nutrition[1], nutrition[2], nutrition[3], nutrition[4], nutrition[5], nutrition[6], nutrition[7], food.showBarcode());
        } else {
            recipe = (Recipe)supFood;
            recipeName = recipe.showName();
            rGUI = new NewRecipeGUI(this, tControl);
            rGUI.setRecipeName(recipeName);
            HashMap<String, Double> ingredients = showIngredientsInRecipe();
            rGUI.existingRecipeData(recipeName, recipe.weight(), ingredients);
            //
        }
    }

    void saveEdited(String oldName, String newName, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        SupFood supFood = data.findItem(oldName);
        if (supFood instanceof Food) {
            Food food = (Food)supFood;
            food.edit(newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            EditFoodRecipeDatabase.addFood(oldName, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
        } else {
            //Recipe is saved upon creation and every time an ingredient is added so no need to save here
            
            //Recipe recipe = (Recipe)supFood;
            //edit recipe
            //save to DB
            //EditFoodRecipeDatabase.addRecipe(recipe.showName(), recipe.weight());
            
            /*HashMap<String, Double> ingredients = recipe.showIngredientList();

            for (String ing: ingredients.keySet()) {
                EditFoodRecipeDatabase.addRecipeIngredient(recipe.showName(), ing, ingredients.get(ing));
            }*/
        }

    }

    void saveNewFood(String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        data.addFood(name, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
        EditFoodRecipeDatabase.addFood(null, name, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
    }

    void saveNewRecipe(String name, double amount) {
        data.addRecipe(name, amount);
        EditFoodRecipeDatabase.addRecipe(name, amount);
        //recipeName = "";
    }

    void addIngredientToRecipe(String foodName, double amount) {
        //System.out.println(recipeName);
        //Recipe recipe = (Recipe)data.findItem(recipeName);
        SupFood ingredient = data.findItem(foodName);

        recipe.addIngredientFromGUI(ingredient.showName(), amount);
        rGUI.updateIngredientsPanel();
    }

    HashMap<String, Double> showIngredientsInRecipe() {
        //Recipe recipe = (Recipe)data.findItem(recipeName);
        HashMap<String, Double> list = recipe.showIngredientList();
        return list;
    }

    void delete(String name) {
        SupFood item = data.findItem(name);
        String fullName = item.showName();
        String displayName = item.showDisplayName();
        data.delete(fullName, displayName);
        if (item instanceof Food) {
            EditFoodRecipeDatabase.deleteFood(fullName, "food");
        } else {
            EditFoodRecipeDatabase.deleteFood(fullName, "recipe");
        }
        
    }

    void updateIngredientsList() {}
}