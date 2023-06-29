package src.SQL.java.connect.sql.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import src.db.Food;
import src.db.SupFood;
import src.db.Recipe;
//mport sql.code.*;

public class Database implements java.io.Serializable {
    transient Scanner input = new Scanner(System.in);
    protected String name;
    //private ArrayList<Food> database = new ArrayList<>();
    protected HashMap<String, SupFood> database = new HashMap<String, SupFood>();
    //protected HashMap<String, Recipe> rdata = new HashMap<String, Recipe>();
    private ArrayList<SupFood> searchResults;

    public Database(String name) {
        this.name = name;
    }

    public void addFood(String name, String nickname, double weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        if (addCheck(name)) {
            Food food = new Food(this, name, nickname, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            database.put(name, food);
        }
    }

    public Recipe addRecipe(String name, double servings) {
        if (!addCheck(name)) {
            System.out.println("OK!");
        }
        Recipe recipe = new Recipe(this, name, servings);
        database.put(name, recipe);
        return recipe;
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


    public void delete(String item) {
        database.remove(item);
    }

    public ArrayList<SupFood> searchDatabase(String item) {
        searchResults = new ArrayList<>();
        for (String food: database.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                SupFood result = database.get(food);
                searchResults.add(result);
                System.out.println(result.showName());
            }
        }
        //SupFood[] results = new SupFood[searchResults.size()];
        //String search = String.join(", ", result);
        return searchResults; 
    }


    public SupFood addFromDatabase(String name) {
        if (!database.containsKey(name)) {
            System.out.println("Not in database!");
            return null;
        }
        return findItem(name);
    }

    public SupFood findItem(String name) {
        SupFood item = null;
        for (String fullName: database.keySet()) {
            if (fullName.contains(name)) {
                item = database.get(fullName);
            }
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