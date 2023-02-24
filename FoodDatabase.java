import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class FoodDatabase extends Database {
    Scanner input = new Scanner(System.in);
    //private String name;
    //private ArrayList<Food> database = new ArrayList<>();
    //private HashMap<String, Food> database = new HashMap<String, Food>();

    public FoodDatabase(String name) {
        super(name);
        //this.database = database;
    }

    public void addFood(String name, int weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt) {
        if (addCheck(name)) {
            Food food = new Food(name, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt);
            this.database.put(name, food);
        }
    }

    /*public Boolean addFoodCheck(String name) {
        Boolean True = true;
        Boolean False = false;
        if (this.database.containsKey(name)) {
            System.out.println("This food exists already! ");
            System.out.println(this.database.get(name));
            System.out.println("Would you to overwrite this food? ");
            String replace = input.nextLine();
            if (replace.toUpperCase().equals("N")) {
                return False;
            }
        }
        return True;
    }*/

    /*public HashMap<String, SupFood> access() {
        return this.database;
    }*/

    public void deleteFood(String item) {
        database.remove(item);
    }

    public String searchDatabase(String item) {
        ArrayList<String> result = new ArrayList<>();
        for (String food: database.keySet()) {
            if (food.contains(item)) {
                result.add(food);
            }
        }
        String search = String.join(", ", result);
        return search; 
    }


    public SupFood addFromDatabase(String name) {
        if (!database.containsKey(name)) {
            System.out.println("Not in database!");
        }
        //if (this.database.contains(name)) {
            SupFood item = database.get(name);
            return item;
        //} else {
            //System.out.println("Not in database!");
            //return null;
        //}

    }

    public SupFood databaseCheck(String name) {
        SupFood item = this.database.get(name);
        return item;        
    }

    @Override
    public String toString() {
        ArrayList<String> db = new ArrayList<>();
        for (String item: this.database.keySet()) {
            db.add(item);
        }
        String list = String.join(", ", db);
        return list;

    }
}