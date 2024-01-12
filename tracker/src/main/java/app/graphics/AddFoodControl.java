package app.graphics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

import app.sql.java.connect.*;
import app.diary.*;
import app.db.*;

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

    AddFoodGUI showGUI() {
        return gui;
    }

    ArrayList<String> searchDatabase(String string, String constraint) {
        HashSet<SupFood> results = data.searchDatabase(string, constraint);
        
        ArrayList<String> asString = new ArrayList<>();
        for (SupFood food: results) {
            asString.add(food.showDisplayName());
        }

        Collections.sort(asString);
        return asString;
    }

    String showUnit(String name) {
        SupFood item = data.findItem(name);
        return item.showUnit();
    }

    void addFoodToDiary(String meal, String name, double amount) {
        tControl.addFoodToDiary(meal, name, amount);
    }

    void addFoodToRecipe(String name, double amount) {
        cControl.addIngredientToRecipe(name, amount);
    }

    void editItem(String name) {
        cControl.editFoodorRecipeGUI(name, gui);
    }

    void updateSearchResults() {
        gui.updateResults();
    }

    void deleteFromDatabase(String name) {
        cControl.delete(name);
    }

}
