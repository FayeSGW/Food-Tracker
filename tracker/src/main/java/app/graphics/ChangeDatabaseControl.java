package app.graphics;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import app.sql.java.connect.*;
import app.db.*;

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

    //Sets the recipe currently being edited in the window so the programme can remember it for later operations
    public void recipe(String name) {
        recipeName = name;
        recipe = (Recipe)data.findItem(name);
    }

    //----------------------Opening GUIs for creating new or editing foods/recipes---------------------------------
    void newFoodGUI() {
        fGUI = new NewFoodGUI(this);
    }

    void newRecipeGUI() {
        rGUI = new NewRecipeGUI(this, tControl);
    }

    //Opens a window for editing a food or recipe, and populates the window's field with the relevant data
    void editFoodorRecipeGUI(String foodName, AddFoodGUI gui) {
        aGUI = gui;
        SupFood supFood = data.findItem(foodName);
        if (supFood instanceof Food) {
            Food food = (Food)supFood;
            newFoodGUI();
            double[] nutrition = food.showNutrition();
            fGUI.existingFoodData(food.showName(), food.showDisplayName(), food.showAmount(), food.showUnit(), nutrition[0], nutrition[1], nutrition[2], nutrition[3], nutrition[4], nutrition[5], nutrition[6], nutrition[7], food.showBarcode());
        } else if (supFood instanceof Recipe) {
            recipe = (Recipe)supFood;
            recipeName = recipe.showName();
            newRecipeGUI();
            rGUI.setRecipeName(recipeName);
            HashMap<String, Double> ingredients = showIngredientsInRecipe();
            rGUI.existingRecipeData(recipeName, recipe.weight(), ingredients);
        }
    }

    //--------------------------------------EDITING OBJECTS---------------------------------
    void edit() {
        tControl.addFoodDialogue(0, "edit");
    }

    //Check of recipe name for use in the GUI
    boolean checkRecipeName(String string) {
        if (string.equals(recipe.showName())) {
            return true;
        }
        return false;
    }
    //Check of recipe servings for use in the GUI
    boolean checkRecipeServings(double num) {
        if (num == recipe.weight()) {
            return true;
        }
        return false;
    }

    //Editing recipe objects, taking into account issues with the recipe having been prevously added to the diary
    boolean saveEditedRecipe(String oldName, String newName, double servings) {
        String name = null;
        if (!oldName.equals(newName)) {
            name = newName;
        }
        if (!databaseCheck(name, name)) {
            return false;
        } else {      
            /* If the recipe is not contained in the diary, or if only its name has been changed, we can just edit it, otherwise we 
             * need to account for the diary entries.
             * I have chosen to keep a reference to the old recipe (in the same way as when deleting items) so that already-existing 
             * diary entries are not changed; this essentially "deletes" the old recipe and creates a new one */                  
            if (!EditFoodRecipeDatabase.checkIfContains(null, oldName, "recipe")) {
                int index = data.editRecipe(oldName, newName, servings);
                EditFoodRecipeDatabase.addRecipe(index, oldName, newName, servings);
            } else {
                Recipe rec = (Recipe) data.findItem(oldName);
                HashMap<String, Double> ingredients = rec.showIngredientList();
                /* If the number of servings has changed, or ingredients have been added/removed/edited, the unit nutrition of 
                 * the recipe has changed. We need to keep a reference to the existing recipe for existing diary entries and 
                 * create a new one with the new values. */
                if (servings == rec.weight() && rec.showTempIngredients().size() == 0) {
                    int index = data.editRecipe(oldName, newName, servings);
                    EditFoodRecipeDatabase.addRecipe(index, oldName, newName, servings);
                } else {
                    delete(oldName);
                    Recipe newRec = data.addRecipe(null, 0, newName, servings);
                    for (String ingredient: ingredients.keySet()) {
                        double weight = ingredients.get(ingredient);
                        newRec.addIngredientByName(ingredient, weight);
                    }
                    EditFoodRecipeDatabase.addRecipe(newRec.showIndex(), null, newName, servings);
                }
            }
            aGUI.updateResults();
            return true;
        }
    }
    //Editing food objects, taking into account issues with the food having been prevously added to the diary or to recipes
    boolean saveEditedFood(String oldName, String newName, String oldDisplayName, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        String name = oldName, disp = oldDisplayName;
        if (!oldName.equals(newName)) {
            name = newName;
        }
        if (!oldDisplayName.equals(displayName)) {
            disp = displayName;
        }
        System.out.println(databaseCheck(name, disp));
        if (!databaseCheck(name, disp)) {
            return false;
        } else {      
            /* If the food is not contained in a recipe or in the diary, we can just edit it, otherwise we need to account for the 
             * recipe/diary entries.
             * If only the name/display name has changed, we can edit as normal and just update the name in the corresponding 
             * diary/recipe entries.
             * For cases when the nutrition/unit nutrition has changed, I have chosen to keep a reference to the old food (in the 
             * same way as when deleting items) so that already-existing recipe ingredient/diary entries are not changed-
             * This essentially "deletes" the old food and creates a new one */                  
            if (!EditFoodRecipeDatabase.checkIfContains(null, oldName, "food")) {
                int index = data.editFood(oldName, newName, oldDisplayName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                EditFoodRecipeDatabase.addFood(index, oldName, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            } else {
                Food food = (Food) data.findItem(oldName);
                double[] newNutr = {calories, fat, satfat, carbs, sugar, fibre, protein, salt};
                /* If only the name or display name has been edited, then we can edit as normal.
                 * But if the nutrition or unit nutrition of the food has changed, then we need to keep a reference
                 * to the current food.*/
                if (Arrays.equals(newNutr, food.showNutrition()) && amount == food.showAmount() && unit.equals(food.showUnit())) {
                    int index = data.editFood(oldName, newName, oldDisplayName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                    EditFoodRecipeDatabase.addFood(index, oldName, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                } else {
                    delete(oldName);
                    Food newFood = data.addFood(null, 0, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                    EditFoodRecipeDatabase.addFood(newFood.showIndex(), null, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                }   
            }
            aGUI.updateResults();
            return true;
        }
    }
    //Check if the submitted name for a food/recipe is already in use
    boolean nameCheck(String name, String string) {
        SupFood item = data.findItem(name);
        messageString = "There is already a food/recipe with that " + string + "!";
        if (item != null) {
            if (item.isDeleted()) {
                messageString = "This " + string + " is in use by a deleted item!";
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

    boolean databaseCheck(String name, String displayName) {  
        if (!nameCheck(name, "name") || !nameCheck(displayName, "display name")) {
            return false;
        }
        return true;
    }

    //Save a newly-created food object
    boolean saveNewFood(String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        if (!databaseCheck(name, displayName)) {
            return false;
        } else {
            Food food = data.addFood(null, 0, name, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            EditFoodRecipeDatabase.addFood(food.showIndex(), null, name, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            return true;
        }
    }

    //Save a newly-created recipe object
    boolean createRecipe(String name, double amount) {
        if (!databaseCheck(name, name)) {
            return false;
        }
        Recipe rec = data.addRecipe(null, 0, name, amount);
        EditFoodRecipeDatabase.addRecipe(rec.showIndex(), null, name, amount);
        return true;
    }

    //Add an ingredient to a recipe
    void addIngredientToRecipe(String foodName, double amount) {
        SupFood ingredient = data.findItem(foodName);
        recipe.addTempIngredient(ingredient.showName(), amount);
        rGUI.populateIngredients();
    }

    void saveRecipeWithIngredients() {
        recipe.save();
    }

    void cancelRecipeEdit() {
        recipe.clearTemp();
    }

    //Edit a recipe ingredient
    void editRecipeIngredient(String foodName, double amount) {
        Recipe recipe = (Recipe) data.findItem(recipeName);
        Food food = (Food) data.findItem(foodName);
        if (amount == 0) {
            recipe.removeTempIngredient(food.showName());
        } else {
            recipe.editIngredient(food.showName(), amount);
        }
        rGUI.populateIngredients();
    }

    HashMap<String, Double> showIngredientsInRecipe() {
        HashMap<String, Double> list = recipe.showIngredientList();
        return list;
    }

    HashMap<Integer, ArrayList<Object>> showAllCurrentIngredients() {
        return recipe.showAllCurrentIngredients();
    }

    //Delete an item, either fully (if it is not reference by the diary or a recipe), or partially
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
        aGUI.updateResults();
    }

    int ingredientsInRecipe(String name) {
        Recipe item = (Recipe) data.findItem(name);
        return item.showIngredientList().size();
    }

    int tempIngredientsInRecipe(String name) {
        Recipe item = (Recipe) data.findItem(name);
        return item.showTempIngredients().size();
    }
}