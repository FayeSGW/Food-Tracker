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
    private ArrayList<String> foodNamesList;
    private double[] nutrition;

    public Meal(String name, LocalDate date, Database data) {
        this.name = name;
        this.date = date;
        this.data = data;
        foodlst = new HashMap<>();
        foodNamesList = new ArrayList<>();
        nutrition = new double[8];
    }
    
    public double[] add(String name, double weight) {
        double [] weighted = new double[8];
        ArrayList<Object> arr;
        try {
            SupFood food = data.findItem(name);
            String unit = food.showUnit();
            double[] unitNutrition = food.unitNutrition();
            if (foodNamesList.contains(name)) {
                arr = foodlst.get(name);
                double oldWeight = (double) arr.get(1);
                double newWeight = oldWeight + weight;
                arr.set(1, newWeight);
            } else {
                arr = new ArrayList<>();
                arr.add(food); arr.add(weight); arr.add(unit);
                foodlst.put(name, arr);
                foodNamesList.add(name);
            }
            for (int i = 0; i < nutrition.length; i++) {
                weighted[i] = unitNutrition[i] * weight;
                nutrition[i] = nutrition[i] + weighted[i];
            }
        } catch (NullPointerException e) {
            System.out.println("Not in database! ");
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

        for (String key: foodlst.keySet()) {
            ArrayList<Object> details = foodlst.get(key);
            double weight = (double) details.get(1);
            meal.add(key, weight);
        }

        return nutrition;

        //foodlst = meal.showFoods();
        //nutrition = meal.showNutrition();
    }

    public double[] edit(String name, double weight) {
        double[] weighted = new double[8];
        ArrayList<Object> lst = foodlst.get(name);
        lst.set(1, weight);

        SupFood food = (SupFood) lst.get(0);
        double[] removed = remove(name);
        foodlst.put(name, lst);
        foodNamesList.add(name);

        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = food.unitNutrition()[i] * weight;
            nutrition[i] = nutrition[i] + weighted[i];
        }

        return weighted;
    }

    public double[] remove(String name) {
        double[] weighted = new double[8];
        ArrayList<Object> lst = foodlst.get(name);
        double weight = (double) lst.get(1);
        SupFood food = (SupFood) lst.get(0);
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = food.unitNutrition()[i] * weight;
            nutrition[i] = nutrition[i] - weighted[i];
        }
        foodlst.remove(name);
        foodNamesList.remove(name);
        return weighted;
    }
   
    public void removeAll() {
        foodlst.clear();
        nutrition = new double[8];
    }

    public double[] showNutrition() {
        return nutrition;
    }

    public HashMap<String, ArrayList<Object>> showFoods() {
        return foodlst;
    }

    public ArrayList<String> showFoodNames() {
        return foodNamesList;
    }

    public ArrayList<Object> showFood(String name) {
        return foodlst.get(name);
    }

    public String showFoodItemAmount(String name) {
        ArrayList<Object> details = foodlst.get(name);
        double weight = (double)details.get(1);
        String unit = (String)details.get(2);
        if (((weight * 10)%10) == 0) {
            return String.format("%.0f %s", weight, unit);
        }
        return String.format("%.1f %s", weight, unit);
    }

    public double[] showFoodItemNutrition(String name) {
        ArrayList<Object> details = foodlst.get(name);
        SupFood food = (SupFood)details.get(0);
        double weight = (double)details.get(1);
        double[] unitNutrition = food.unitNutrition();
        double[] weightedNutrition = new double[8];
        for (int i = 0; i < 8; i++) {
            weightedNutrition[i] = unitNutrition[i] * weight;
        }

        return weightedNutrition;
    }

    public String showName() {
        return name;
    }

    public String toString() {
        ArrayList<String> stringList = new ArrayList<>();
        ArrayList<Object> lst;
        for (String food: foodlst.keySet()) {
            lst = foodlst.get(food);
            double weight = (double) lst.get(1);
            String unit = (String) lst.get(2);
            String strng = String.valueOf(weight) + " " + unit + " " + food;
            stringList.add(strng);
        }
        String meal = name + " on " + date + "\n";
        String foods = String.join(", ", stringList);
        String nutrition = String.format("\nCalories: %.0f \nFat: %.1f \nSaturated Fat: %.1f \nCarbohydrates: %.1f \nSugar: %.1f \nFibre: %.1f \nProtein: %.1f \nSalt: %.1f", this.nutrition[0], this.nutrition[1], this.nutrition[2], this.nutrition[3], this.nutrition[4], this.nutrition[5], this.nutrition[6], this.nutrition[7]);
        return meal + foods + nutrition;
    }
}