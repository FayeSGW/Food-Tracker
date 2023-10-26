package src.graphics;

import java.util.ArrayList;

import src.SQL.java.connect.sql.code.*;
import src.diary.*;
import src.db.*;

class AddFoodControl {
    TrackerControl tControl;
    ChangeDatabaseControl cControl;
    AddFoodGUI gui;
    Database data;


    AddFoodControl(TrackerControl tControl, int index, String type, ChangeDatabaseControl cControl) {
        this.tControl = tControl;
        this.cControl = cControl;
        gui = new AddFoodGUI(this, index, type);
        data = tControl.showUser().accessDatabase();

    }

    ArrayList<String> searchDatabase(String string, String constraint) {
        ArrayList<SupFood> results = data.searchDatabase(string, constraint);
        
        ArrayList<String> asString = new ArrayList<>();
        for (SupFood food: results) {
            asString.add(food.showDisplayName());
            //System.out.println(food.showName());
            //System.out.println(food.showDisplayName());
        }
        
        return asString;
    }

    String showUnit(String name) {
        SupFood item = data.findItem(name);
        //System.out.println(data.isRecipe(name));
        //System.out.println(item);
        return item.showUnit();
    }

    void addFoodToDiary(String meal, String name, double amount) {
        tControl.addFoodToDiary(meal, name, amount);
    }

    void addFoodToRecipe(String name, double amount) {
        cControl.addIngredientToRecipe(name, amount);
    }

    void editItem(String name) {
        cControl.editFoodorRecipeGUI(name);
    }

    void deleteFromDatabase(String name) {
        cControl.delete(name);
    }

}
