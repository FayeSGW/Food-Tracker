package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import SQL.java.connect.sql.code.*;

public class Database implements java.io.Serializable {
    transient Scanner input = new Scanner(System.in);
    protected String name;
    //private ArrayList<Food> database = new ArrayList<>();
    protected HashMap<String, SupFood> database = new HashMap<String, SupFood>();
    //protected HashMap<String, Recipe> rdata = new HashMap<String, Recipe>();

    public Database(String name) {
        this.name = name;
    }

    public void addFood(String name, int weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        if (addCheck(name)) {
            Food food = new Food(this, name, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            database.put(name, food);
        }
    }

    /*public Recipe addRecipe(String name, int servings) {
        if (!addCheck(name)) {
            System.out.println("OK!");
        }
        Recipe recipe = new Recipe(this, name, servings);
        database.put(name, recipe);
        return recipe;
    }*/

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

    public String searchDatabase(String item) {
        ArrayList<String> result = new ArrayList<>();
        for (String food: database.keySet()) {
            if (food.toLowerCase().contains(item.toLowerCase())) {
                result.add(food);
            }
        }
        String search = String.join(", ", result);
        return search; 
    }


    public SupFood addFromDatabase(String name) {
        if (!database.containsKey(name)) {
            System.out.println("Not in database!");
            return null;
        }
        return findItem(name);
    }

    public SupFood findItem(String name) {
        SupFood item = database.get(name);
        return item;        
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