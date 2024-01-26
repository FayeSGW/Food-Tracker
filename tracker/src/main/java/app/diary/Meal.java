package app.diary;

import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;

import app.sql.java.connect.*;
import app.db.*;

public class Meal {
    private String name;
    private LocalDate date;
    private Database data;
    private HashMap<String, ArrayList<Object>> foodlst;
    private HashMap<Integer, ArrayList<Object>> foodLst = new HashMap<>();
    private ArrayList<String> foodNamesList;
    private ArrayList<SupFood> foodObjects;
    private double[] nutrition;

    public Meal(String name, LocalDate date, Database data) {
        this.name = name;
        this.date = date;
        this.data = data;
        foodlst = new HashMap<>();
        foodNamesList = new ArrayList<>();
        foodObjects = new ArrayList<>();
        nutrition = new double[8];
    }

    public void addByName(String name, double weight) {
        SupFood food = data.findItem(name);
        add(food, weight);
    }

    public void addByIndex(int index, double weight) {
        SupFood food = data.getItemFromIndex(index);
        add(food, weight);
    }
    
    public double[] add(SupFood food, double weight) {
        double [] weighted = new double[8];
        ArrayList<Object> arr;
        try {
            String name = food.showName();
            String displayName = food.showDisplayName();
            String unit = food.showUnit();
            int index = food.showIndex();
            double[] unitNutrition = food.showUnitNutrition();
            if (foodLst.keySet().contains(index)) {
                arr = foodLst.get(index);
                double oldWeight = (double) arr.get(1);
                double newWeight = oldWeight + weight;
                arr.set(1, newWeight);
            } else {
                arr = new ArrayList<>();
                arr.add(name); arr.add(weight); arr.add(unit); arr.add(displayName);
                foodLst.put(index, arr);
                foodNamesList.add(food.showName());
            }
            for (int i = 0; i < nutrition.length; i++) {
                weighted[i] = unitNutrition[i] * weight;
                nutrition[i] = nutrition[i] + weighted[i];
            }
        } catch (NullPointerException e) {
            System.out.println("Not in database! " + food.showName());
        }
        return weighted;
    }

    public double[] copy(Meal meal) {
        //removeAll();

        /*for (String key: meal.showFoods().keySet()) {
            ArrayList<Object> details = meal.showFoods().get(key);
            double weight = (double) details.get(1);
            add(key, weight);
        }*/

        for (Integer key: foodLst.keySet()) {
            ArrayList<Object> details = foodLst.get(key);
            double weight = (double) details.get(1);
            meal.addByIndex(key, weight);
        }

        return nutrition;

        //foodlst = meal.showFoods();
        //nutrition = meal.showNutrition();
    }

    public double[] edit(int index, double weight) {
        double[] weighted = new double[8];
        ArrayList<Object> lst = foodlst.get(index);
        lst.set(1, weight);

        SupFood food = data.getItemFromIndex(index);
        double[] removed = remove(index);
        foodLst.put(index, lst);
        //foodNamesList.add(index);

        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = food.showUnitNutrition()[i] * weight;
            nutrition[i] = nutrition[i] + weighted[i];
        }

        return weighted;
    }

    public double[] remove(int index) {
        double[] weighted = new double[8];
        ArrayList<Object> lst = foodLst.get(index);
        double weight = (double) lst.get(1);
        SupFood food = data.getItemFromIndex(index);
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = food.showUnitNutrition()[i] * weight;
            nutrition[i] = nutrition[i] - weighted[i];
        }
        foodLst.remove(index);
        foodNamesList.remove(lst.get(0));
        return weighted;
    }
   
    public void removeAll() {
        foodlst.clear();
        nutrition = new double[8];
    }

    public double[] showNutrition() {
        return nutrition;
    }

    public HashMap<Integer, ArrayList<Object>> showFoods() {
        return foodLst;
    }

    public ArrayList<String> showFoodNames() {
        return foodNamesList;
    }

    public ArrayList<Object> showFood(int index) {
        return foodLst.get(index);
    }

    public String showFoodItemAmount(int index) {
        ArrayList<Object> details = foodLst.get(index);
        double weight = (double)details.get(1);
        String unit = (String)details.get(2);
        if (((weight * 10)%10) == 0) {
            return String.format("%.0f %s", weight, unit);
        }
        return String.format("%.1f %s", weight, unit);
    }

    public double[] showFoodItemNutrition(int index) {
        ArrayList<Object> details = foodLst.get(index);
        SupFood food = data.getItemFromIndex(index);
        double weight = (double)details.get(1);
        double[] unitNutrition = food.showUnitNutrition();
        double[] weightedNutrition = new double[8];
        for (int i = 0; i < 8; i++) {
            weightedNutrition[i] = unitNutrition[i] * weight;
        }

        return weightedNutrition;
    }

    public String showName() {
        return name;
    }
}