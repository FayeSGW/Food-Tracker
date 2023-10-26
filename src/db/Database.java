//package src.SQL.java.connect.sql.code;
package src.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
//import src.db.Food;
//import src.db.SupFood;
//import src.db.Recipe;
import src.SQL.java.connect.sql.code.*;

public class Database {
    transient Scanner input = new Scanner(System.in);
    protected String name;
    //private ArrayList<Food> database = new ArrayList<>();
    protected HashMap<String, SupFood> database = new HashMap<String, SupFood>();
    protected HashMap<String, SupFood> displayDatabase = new HashMap<>();
    //protected HashMap<String, Recipe> rdata = new HashMap<String, Recipe>();
    private ArrayList<SupFood> searchResults;

    public Database(String name) {
        this.name = name;
    }

    public void addFood(String name, String nickname, double weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        if (addCheck(name)) {
            Food food = new Food(this, name, nickname, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            database.put(name, food);
            displayDatabase.put(nickname, food);
        }
    }

    public Recipe addRecipe(String name, double servings) {
        if (!addCheck(name)) {
            System.out.println("OK!");
        }
        Recipe recipe = new Recipe(this, name, servings);
        database.put(name, recipe);
        displayDatabase.put(name, recipe);
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

    public boolean addCheck(String name) {
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
    }

    public HashMap<String, SupFood> access() {
        return database;
    }


    public void delete(String fullName, String displayName) {
        database.remove(fullName);
        displayDatabase.remove(displayName);
    }

    public ArrayList<SupFood> searchDatabase(String item, String constraint) {
        searchResults = new ArrayList<>();
        for (String food: database.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                SupFood result = database.get(food);
                if (constraint.equals("all")) {
                    searchResults.add(result);
                } else if (constraint.equals("food") && result instanceof Food) {
                    searchResults.add(result);
                } else if (constraint.equals("recipe") && result instanceof Recipe) {
                    searchResults.add(result);
                }
                
                //System.out.println(result.showName());
            }
        }
        //SupFood[] results = new SupFood[searchResults.size()];
        //String search = String.join(", ", result);
        return searchResults; 
    }

    public ArrayList<SupFood> searchForRecipes(String item) {
        searchResults = new ArrayList<>();
        for (String food: database.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                SupFood result = database.get(food);
                if (result instanceof Recipe) {
                    searchResults.add(result);
                }
            }
        }
        return searchResults; 
    }

    public ArrayList<SupFood> searchForFoods(String item) {
        searchResults = new ArrayList<>();
        for (String food: database.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                SupFood result = database.get(food);
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
        //System.out.println(name);
        
        //item = displayDatabase.get(name);

        if (database.keySet().contains(name)) {
            item = database.get(name);
        } else if (displayDatabase.keySet().contains(name)) {
            item = displayDatabase.get(name);
        }


        /*for (String fullName: database.keySet()) {
            if (fullName.contains(name)) {
                item = database.get(fullName);
                if (item.showDisplayName().equals(name)) {
                    return item;
                }
            }
        }*/
        return item;        
    }

    

    public boolean isRecipe(String name) {
        SupFood item = findItem(name);
        if (item instanceof Recipe) {
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        ArrayList<String> db = new ArrayList<>();
        for (String item: database.keySet()) {
            db.add(item);
        }
        String list = String.join(", ", db);
        return list;
    }
}