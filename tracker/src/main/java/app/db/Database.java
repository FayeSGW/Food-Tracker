package app.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Scanner;

import app.sql.java.connect.*;

public class Database {
    transient Scanner input = new Scanner(System.in);
    protected String name;
    protected HashMap<String, SupFood> database = new HashMap<String, SupFood>();
    protected HashMap<String, SupFood> displayDatabase = new HashMap<>(); 
    public HashMap<Integer, SupFood> indexedDatabase = new HashMap<>();
    private HashSet<SupFood> searchResults;

    public Database(String name) {
        this.name = name;
    }

    public Food addFood(Integer index, int deleted, String name, String nickname, double weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        Food food = null;
        if (deleted == 1) {
            food = new Food(this, index, name, nickname, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            food.setDeleted();
        } else if (nameCheck(name) && nicknameCheck(nickname)) {
            food = new Food(this, index, name, nickname, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            database.put(name, food);
            displayDatabase.put(nickname, food);
        }
        indexedDatabase.put(index, food);
        return food;
    }

    public Recipe addRecipe(Integer index, int deleted, String name, double servings) {
        Recipe recipe = new Recipe(this, index, name, servings);
        if (deleted == 1) {
            recipe.setDeleted();
            indexedDatabase.put(index, recipe);
        }
        if (nameCheck(name)) {
            database.put(name, recipe);
            indexedDatabase.put(index, recipe);
        }
        return recipe;
    }

    public boolean nameCheck(String name) {
        if (database.containsKey(name)) {
            return false;
        }
        return true;
    }

    public boolean nicknameCheck(String name) {
        if (displayDatabase.containsKey(name)) {
            return false;
        }
        return true;
    }

    public HashMap<String, SupFood> access() {
        return database;
    }

    public int editFood(String oldName, String newName, String oldDisplayName, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        Food food = (Food) findItem(oldName);
        food.edit(newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
        database.remove(oldName); displayDatabase.remove(oldDisplayName);
        database.put(newName, food); displayDatabase.put(displayName, food);
        indexedDatabase.put(food.showIndex(), food);
        return food.showIndex();
    }

    public int editRecipe(String oldName, String newName, double amount) {
        Recipe recipe = (Recipe) findItem(oldName);
        recipe.edit(newName, amount);
        database.remove(oldName); displayDatabase.remove(oldName);
        database.put(newName, recipe); displayDatabase.put(newName, recipe);
        indexedDatabase.put(recipe.showIndex(), recipe);
        return recipe.showIndex();
    }

    //If "full" is true, it means that the item is not referenced by any recipes in the database or any entries in the diary, and
    // therefore can be fully deleted from the external database
    public void delete(String fullName, String displayName, boolean full) {
        SupFood food = database.get(fullName);
        food.setDeleted();
        database.remove(fullName);
        displayDatabase.remove(displayName);
        /*if (!full) {
            indexedDatabase.put(food.showIndex(), food); 
        }*/
    }

    public HashSet<SupFood> searchDatabase(String item, String constraint) {
        searchResults = new HashSet<>();
        for (String food: database.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                SupFood result = database.get(food);
                //If food marked as "deleted", don't add to search result
                if (result.isDeleted()) {
                    continue;
                }
                if (constraint.equals("all")) {
                    searchResults.add(result);
                } else if (constraint.equals("food") && result instanceof Food) {
                    searchResults.add(result);
                } else if (constraint.equals("recipe") && result instanceof Recipe) {
                    searchResults.add(result);
                }
            }
        }
        //In case the display name contains the search string but the full name doesn't (unlikely!)
        for (String food: displayDatabase.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                SupFood result = displayDatabase.get(food);
                if (result.isDeleted()) {
                    continue;
                }
                if (constraint.equals("all")) {
                    searchResults.add(result);
                } else if (constraint.equals("food") && result instanceof Food) {
                    searchResults.add(result);
                } else if (constraint.equals("recipe") && result instanceof Recipe) {
                    searchResults.add(result);
                }
            }
        }
        return searchResults; 
    }

    /*public SupFood addFromDatabase(String name) {
        if (!database.containsKey(name) && !displayDatabase.containsKey(name)) {
            System.out.println(name + " Not in database!");
            return null;
        }
        return findItem(name);
    }*/

    public SupFood findItem(String name) {
        SupFood item = null;
        if (database.keySet().contains(name)) {
            item = database.get(name);
        } else if (displayDatabase.keySet().contains(name)) {
            item = displayDatabase.get(name);
        }
        return item;        
    }

    public SupFood getItemFromIndex(int index) {
        return indexedDatabase.get(index);
    }

    public boolean isRecipe(int index) {
        SupFood item = getItemFromIndex(index);
        if (item instanceof Recipe) {
            return true;
        }
        return false;
    }
}