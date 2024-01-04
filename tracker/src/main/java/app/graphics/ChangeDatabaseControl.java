package app.graphics;

import java.util.HashMap;
import java.util.Arrays;

import app.sql.java.connect.*;
import app.db.*;
import app.diary.*;

class ChangeDatabaseControl {
    TrackerControl tControl;
    Database data;
    ChangeDatabaseGUI dbGUI;

    String recipeName, messageString;
    Recipe recipe;
    NewRecipeGUI rGUI;
    NewFoodGUI fGUI;
    AddFoodGUI aGUI;

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
        fGUI = new NewFoodGUI(this);
    }

    void newRecipeGUI() {
        rGUI = new NewRecipeGUI(this, tControl);
    }

    void edit() {
        tControl.addFoodDialogue(0, "edit");
    }

    void editFoodorRecipeGUI(String foodName, AddFoodGUI gui) {
        aGUI = gui;
        SupFood supFood = data.findItem(foodName);
        if (supFood instanceof Food) {
            Food food = (Food)supFood;
            NewFoodGUI nGUI = new NewFoodGUI(this);
            double[] nutrition = food.showNutrition();
            nGUI.existingFoodData(food.showName(), food.showDisplayName(), food.showAmount(), food.showUnit(), nutrition[0], nutrition[1], nutrition[2], nutrition[3], nutrition[4], nutrition[5], nutrition[6], nutrition[7], food.showBarcode());
        } else if (supFood instanceof Recipe) {
            recipe = (Recipe)supFood;
            recipeName = recipe.showName();
            rGUI = new NewRecipeGUI(this, tControl);
            rGUI.setRecipeName(recipeName);
            HashMap<String, Double> ingredients = showIngredientsInRecipe();
            rGUI.existingRecipeData(recipeName, recipe.weight(), ingredients);
        }
    }

    boolean checkRecipeName(String string) {
        if (string.equals(recipe.showName())) {
            return true;
        }
        return false;
    }

    boolean checkRecipeServings(double num) {
        if (num == recipe.weight()) {
            return true;
        }
        return false;
    }

    boolean editRecipe(String oldName, String newName, double servings) {
        String name = null;
        if (!oldName.equals(newName)) {
            name = newName;
        }
        if (!nameCheck(name, name)) {
            return false;
        } else {      
            /* If the recipe is not contained in the diary, we can just edit it
               Otherwise we need to account for the diary entries
               I have chosen to keep a reference to the old recipe (in the same way as when deleting
               items) so that already-existing diary entries are not changed,
               therefore essentially "delete" the old recipe and create a new one */                  
            if (!EditFoodRecipeDatabase.checkIfContains(null, oldName, "recipe")) {
                data.editRecipe(oldName, newName, servings);
                EditFoodRecipeDatabase.addRecipe(oldName, newName, servings);
            } else {
                Recipe rec = (Recipe) data.findItem(oldName);
                HashMap<String, Double> ingredients = rec.showIngredientList();
                //If only the name has been changed, or no ingredients have been added/removed/
                // changed, we can just edit the recipe as normal
                // If the number of servings has changed, the unit nutrition of the recipe has changed,
                // So we need to keep a reference to the existing recipe, and create a new one with the new
                // values.
                if (servings == rec.weight() && rec.showTempIngredients().size() == 0) {
                    data.editRecipe(oldName, newName, servings);
                    EditFoodRecipeDatabase.addRecipe(oldName, newName, servings);
                } else {
                    delete(oldName);
                    Recipe newRec = data.addRecipe(newName, servings);
                    for (String ingredient: ingredients.keySet()) {
                        double weight = ingredients.get(ingredient);
                        newRec.addIngredient(ingredient, weight);
                    }
                    EditFoodRecipeDatabase.addRecipe(null, newName, servings);
                }
                
            }
            aGUI.updateResults();
            return true;
        }
    }

    boolean saveEditedFood(String oldName, String newName, String oldDisplayName, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        String name = null, disp = null;
        if (!oldName.equals(newName)) {
            name = newName;
        }
        if (!oldDisplayName.equals(displayName)) {
            disp = displayName;
        }
        if (!nameCheck(name, disp)) {
            return false;
        } else {      
            /* If the food is not contained in a recipe or in the diary, we can just edit it.
               Otherwise we need to account for the recipe/diary entries.
               If only the name/display name has changed, we can edit as normal and just update the name
               in the corresponding diary/recipe entries.
               For cases when the nutrition/unit nutrition has changed, I have chosen to keep a reference 
               to the old food (in the same way as when deleting items) so that already-existing recipe 
               ingredient/diary entries are not changed, therefore essentially "delete" the old food and 
               create a new one */                  
            if (!EditFoodRecipeDatabase.checkIfContains(null, oldName, "food")) {
                data.editFood(oldName, newName, oldDisplayName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                EditFoodRecipeDatabase.addFood(oldName, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            } else {
                Food food = (Food) data.findItem(oldName);
                double[] newNutr = {calories, fat, satfat, carbs, sugar, fibre, protein, salt};
                //If only the name or display name has been edited, then we can edit as normal
                // But if the nutrition or unit nutrition of the food has changed, then we need to keep a reference
                // to the current food.
                if (Arrays.equals(newNutr, food.showNutrition()) && amount == food.showAmount() && unit.equals(food.showUnit())) {
                    data.editFood(oldName, newName, oldDisplayName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                    EditFoodRecipeDatabase.addFood(oldName, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                } else {
                    delete(oldName);
                    data.addFood(newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                    EditFoodRecipeDatabase.addFood(null, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                }   
            }
            aGUI.updateResults();
            return true;
        }
    }

    boolean nameCheck(String name, String displayName) {
        SupFood item = data.findItem(name);
        messageString = "There is already a food/recipe with that name!";
        if (item != null) {
            if (item.isDeleted()) {
                messageString = "This name is in use by a deleted item!";
            }
            if (fGUI != null) {
                fGUI.changeAddedLabel(messageString); 
            } else if (rGUI != null) {
                rGUI.setInfoLabel(messageString);
            }
            return false;
        }
        return true;
    }

    boolean saveNewFood(String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        if (!nameCheck(name, displayName)) {
            return false;
        } else {
            data.addFood(name, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            EditFoodRecipeDatabase.addFood(null, name, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            return true;
        }
    }

    boolean saveNewRecipe(String name, double amount) {
        if (!nameCheck(name, name)) {
            return false;
        }
        data.addRecipe(name, amount);
        EditFoodRecipeDatabase.addRecipe(null, name, amount);
        return true;
    }

    void addIngredientToRecipe(String foodName, double amount) {
        //Recipe recipe = (Recipe)data.findItem(recipeName);
        SupFood ingredient = data.findItem(foodName);
        recipe.addIngredientFromGUI(ingredient.showName(), amount);
        rGUI.populateIngredients();
    }

    void editRecipeIngredient(String foodName, double amount) {
        Recipe recipe = (Recipe) data.findItem(recipeName);
        Food food = (Food) data.findItem(foodName);
        if (amount == 0) {
            recipe.removeIngredient(food.showName());
        } else {
            recipe.editIngredient(food.showName(), amount);
        }
        rGUI.populateIngredients();
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
        
        boolean fullDeletion = true;
        if (item instanceof Food) {
            fullDeletion = EditFoodRecipeDatabase.deleteFood(fullName, "food");
        } else {
            fullDeletion = EditFoodRecipeDatabase.deleteFood(fullName, "recipe");
        }
        data.delete(fullName, displayName, fullDeletion);
    }

    int ingredientsInRecipe(String name) {
        Recipe item = (Recipe) data.findItem(name);
        return item.numberIngredients();
    }
}