package src.diary;

import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;

import src.SQL.java.connect.sql.code.*;
import src.db.*;

public class Meal {
    private String name;
    private LocalDate date;
    private HashMap<String, ArrayList<Object>> foodlst;
    private double[] nutrition;

    public Meal(String name, LocalDate date) {
        this.name = name;
        this.date = date;
        this.foodlst = new HashMap<>();
        this.nutrition = new double[8];
    }
    
    public double[] add(String name, int weight, Database data) {
        double [] weighted = new double[8];
        ArrayList<Object> arr;
        try {
            SupFood food = data.addFromDatabase(name);
            String unit = food.showUnit();
            double[] unitNutrition = food.unitNutrition();
            if (foodlst.containsKey(name)) {
                arr = foodlst.get(name);
                int oldWeight = (int) arr.get(1);
                int newWeight = oldWeight + weight;
                arr.set(1, newWeight);
            } else {
                arr = new ArrayList<>();
                arr.add(food); arr.add(weight); arr.add(unit);
                foodlst.put(name, arr);
            }
            for (int i = 0; i < this.nutrition.length; i++) {
                weighted[i] = unitNutrition[i] * weight;
                this.nutrition[i] = this.nutrition[i] + weighted[i];
            }
        } catch (NullPointerException e) {
            System.out.println("Not in database! ");
        }
        return weighted;
    }

    public void copy(Meal meal) {
        removeAll();
        foodlst = meal.showFoods();
        nutrition = meal.showNutrition();
    }

    public void edit(String name, int weight) {
        double[] weighted = new double[8];
        ArrayList<Object> lst = foodlst.get(name);
        lst.set(1, weight);

        SupFood food = (SupFood) lst.get(0);
        double[] removed = remove(name);
        foodlst.put(name, lst);

        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = food.unitNutrition()[i] * weight;
            nutrition[i] = nutrition[i] + weighted[i];
        }
    }

    public double[] remove(String name) {
        double[] weighted = new double[8];
        ArrayList<Object> lst = foodlst.get(name);
        int weight = (int) lst.get(1);
        SupFood food = (SupFood) lst.get(0);
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = food.unitNutrition()[i] * weight;
            nutrition[i] = nutrition[i] - weighted[i];
        }
        foodlst.remove(name);
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

    public String showName() {
        return name;
    }

    public String toString() {
        ArrayList<String> stringList = new ArrayList<>();
        ArrayList<Object> lst;
        for (String food: foodlst.keySet()) {
            lst = foodlst.get(food);
            int weight = (int) lst.get(1);
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