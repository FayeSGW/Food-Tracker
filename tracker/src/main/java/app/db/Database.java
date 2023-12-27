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
    protected HashMap<String, SupFood> displayDatabase = new HashMap<>(); //For foods where the display name and full name are very different
    //protected HashSet<SupFood> deleted = new HashSet<>();
    private HashSet<SupFood> searchResults;

    public Database(String name) {
        this.name = name;
    }

    public void addFood(String name, String nickname, double weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        if (nameCheck(name) && nicknameCheck(nickname)) {
            Food food = new Food(this, name, nickname, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            database.put(name, food);
            displayDatabase.put(nickname, food);
        }
    }

    public Recipe addRecipe(String name, double servings) {
        Recipe recipe = null;
        if (nameCheck(name)) {
            recipe = new Recipe(this, name, servings);
            database.put(name, recipe);
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

    /*public boolean addCheck(String name) {
        if (database.containsKey(name)) {
            System.out.println("This exists already! ");
            System.out.println(database.get(name));
            System.out.println("Would you to overwrite? ");
            String replace = input.nextLine();
            if (replace.toUpperCase().equals("N")) {
                return false;
            }
        }
        return true;
    }*/

    public HashMap<String, SupFood> access() {
        return database;
    }

    public void delete(String fullName, String displayName, boolean full) {
        SupFood food = database.get(fullName);
        //deleted.add(food);
        food.setDeleted();
        if (full) {
            database.remove(fullName);
            displayDatabase.remove(displayName);
        }
       
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

    public HashSet<SupFood> searchForRecipes(String item) {
        searchResults = new HashSet<>();
        for (String food: database.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                SupFood result = database.get(food);
                if (result.isDeleted()) {
                    continue;
                }
                if (result instanceof Recipe) {
                    searchResults.add(result);
                }
            }
        }
        return searchResults; 
    }

    public HashSet<SupFood> searchForFoods(String item) {
        searchResults = new HashSet<>();
        for (String food: database.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                SupFood result = database.get(food);
                if (result.isDeleted()) {
                    continue;
                }
                if (result instanceof Food) {
                    searchResults.add(result);
                }
            }
        }
        return searchResults; 
    }

    public SupFood addFromDatabase(String name) {
        if (!database.containsKey(name) && !displayDatabase.containsKey(name)) {
            System.out.println(name + " Not in database!");
            return null;
        }
        return findItem(name);
    }

    public SupFood findItem(String name) {
        SupFood item = null;
        if (database.keySet().contains(name)) {
            item = database.get(name);
        } else if (displayDatabase.keySet().contains(name)) {
            item = displayDatabase.get(name);
        }
        return item;        
    }

    public boolean isRecipe(String name) {
        SupFood item = findItem(name);
        if (item instanceof Recipe) {
            return true;
        }
        return false;
    }
}