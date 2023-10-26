package src.diary;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;

import src.db.*;
import src.SQL.java.connect.sql.code.*;

public class Day {
    private LocalDate date;
    private Meal breakfast, lunch, dinner, snacks;
    private double[] nutrition, remainingNutrition;
    private HashMap<String, Exercise> exercise;
    private ArrayList<Exercise> workouts;
    private int caloriesBurned = 0, waterDrunk = 0, remainingWater;
    private double calorieGoal, carbGoal, fatGoal, proteinGoal;
    private double todaysWeight = 0;
    private User user;
    private Database database;

    public Day(LocalDate date, User user) {
        this.date = date;
        this.user = user;
        this.database = user.accessDatabase();
        breakfast = new Meal("Breakfast", date, database);
        lunch = new Meal("Lunch", date, database);
        dinner = new Meal("Dinner", date, database);
        snacks = new Meal("Snacks", date, database);

        nutrition = new double[8];
        remainingNutrition = new double[8];
        for (int i = 0; i < nutrition.length; i++) {
            remainingNutrition[i] = user.showNutrition()[i];
        }

        calorieGoal = user.showNutrition()[0];
        carbGoal = user.showNutrition()[3];
        fatGoal = user.showNutrition()[1];
        proteinGoal = user.showNutrition()[6];

        this.exercise = new HashMap<>();
        this.workouts = new ArrayList<>();
        remainingWater = user.showWater();
        
    }

    public LocalDate showDate() {
        return date;
    }

    public void addFood(String meal, String item, double amount) {
        String name = meal.toLowerCase().trim();
        double[] foodNutrition = new double[8];
        if (name.equals("breakfast")) {
            foodNutrition = breakfast.add(item, amount);
        } else if (name.equals("lunch")) {
            foodNutrition = lunch.add(item, amount);
        } else if (name.equals("dinner")) {
            foodNutrition = dinner.add(item, amount);
        } else if (name.equals("snacks")) {
            foodNutrition = snacks.add(item, amount);
        }
        for (int i = 0; i < nutrition.length; i++) {
            nutrition[i] = nutrition[i] + foodNutrition[i];
            remainingNutrition[i] = remainingNutrition[i] - foodNutrition[i];

        }
    }

    public void addFoodFromGUI(String meal, String item, double amount) {
        SupFood food = database.findItem(item);
        String fullName = food.showName();
        String date = showDate().toString();
        addFood(meal, fullName, amount);
        AddToDiary.addFood(user, date, meal, fullName, amount);
    }

    public double[] showGoals() {
        double[] goals = {calorieGoal, fatGoal, carbGoal, proteinGoal};
        return goals;
    }

    public void remove(String meal, String item) {
        String name = meal.toLowerCase().trim();
        String date = showDate().toString();
        double[] nutr = new double[8];
        if (name.equals("breakfast")) {
            nutr = breakfast.remove(item);
        } else if (name.equals("lunch")) {
            nutr = lunch.remove(item);
        } else if (name.equals("dinner")) {
            nutr = dinner.remove(item);
        } else if (name.equals("snacks")) {
            nutr = snacks.remove(item);
        }
        for (int i = 0; i < nutrition.length; i++) {
            nutrition[i] = nutrition[i] - nutr[i];
            remainingNutrition[i] = remainingNutrition[i] + nutr[i];
        }
        SupFood food = database.findItem(item);

        if (food instanceof Food) {
            AddToDiary.removeFood(date, meal, item, "food");
        } else {
            AddToDiary.removeFood(date, meal, item, "reciped");
        }
        
    }

    public void edit(String meal, String item, double weight) {
        String name = meal.toLowerCase().trim();
        double[] nutr = new double[8];

        /*if (name.equals("breakfast")) {
            nutr = breakfast.edit(item, weight);
        } else if (name.equals("lunch")) {
            nutr = lunch.edit(item, weight);
        } else if (name.equals("dinner")) {
            nutr = dinner.edit(item, weight);
        } else if (name.equals("snacks")) {
            nutr = snacks.edit(item, weight);
        }*/

        remove(meal, item);
        addFoodFromGUI(meal, item, weight);

    }

    public void copyMeal(Meal fromMeal, String toMeal, Day toDay) {
        /*String name = fromMeal.trim().toLowerCase();
        double[] nutr = new double[8];
        if (name.equals("breakfast")) {
            nutr = breakfast.copy(toMeal);
        } else if (name.equals("lunch")) {
            nutr = lunch.copy(toMeal);
        } else if (name.equals("dinner")) {
            nutr = dinner.copy(toMeal);
        } else if (name.equals("snacks")) {
            nutr = snacks.copy(toMeal);
        }*/

        HashMap<String, ArrayList<Object>> foodList = fromMeal.showFoods();
        for (String item: foodList.keySet()) {
            ArrayList<Object> details = foodList.get(item);
            double amount = (double) details.get(1);
            toDay.addFoodFromGUI(toMeal, item, amount);
        }

    }

    public void clearMeal(String meal) {
        String name = meal.trim().toLowerCase();
        double[] nutr = new double[8];
        if (name.equals("breakfast")) {
            nutr = breakfast.showNutrition();
            breakfast.removeAll();
        } else if (name.equals("lunch")) {
            nutr = lunch.showNutrition();
            lunch.removeAll();
        } else if (name.equals("dinner")) {
            nutr = dinner.showNutrition();
            dinner.removeAll();
        } else if (name.equals("snacks")) {
            nutr = snacks.showNutrition();
            snacks.removeAll();
        }
        for (int i = 0; i < nutrition.length; i++) {
            nutrition[i] = nutrition[i] - nutr[i];
            remainingNutrition[i] = remainingNutrition[i] + nutr[i];
        }
    }

    public void clearAll() {
        breakfast.removeAll();
        lunch.removeAll();
        dinner.removeAll();
        snacks.removeAll();
        nutrition = new double[8];
        remainingNutrition = user.showNutrition();
        exercise.clear();
        caloriesBurned = 0;
    }

    public Exercise showWorkout(String name) {
        return exercise.get(name);
    }

    public void addExercise(String name, int minutes, int seconds, int calories) {
        Exercise workout = new Exercise(name, minutes, seconds, calories);
        remainingNutrition[0] = remainingNutrition[0] + calories;
        remainingNutrition[1] = remainingNutrition[1] + ((calories * 0.25)/9); //update fat requirement based on new calories
        remainingNutrition[3] = remainingNutrition[3] + ((calories * 0.5)/4); //update carbs based on new calories
        remainingNutrition[6] = remainingNutrition[6] + ((calories * 0.25)/4); //update protein requirement based on new calories
        
        calorieGoal += calories;
        fatGoal += ((calories * 0.25)/9);
        carbGoal += ((calories * 0.5)/4);
        proteinGoal += ((calories * 0.25)/4);

        caloriesBurned += calories;
        exercise.put(name, workout);
        workouts.add(workout);
    }

    public void addExercisefromGUI(String name, int minutes, int seconds, int calories) {
        addExercise(name, minutes, seconds, calories);
        String date = showDate().toString();
        AddToDiary.addExercise(date, name, minutes, seconds, calories);
    }

    public void editExercise(String oldName, String newName, int minutes, int seconds, int calories) {
        removeExercise(oldName);
        addExercisefromGUI(newName, minutes, seconds, calories);
        
    }

    public void removeExercise(String name) {
        if (exercise.containsKey(name)) {
            Exercise workout = exercise.get(name);
            int calories = workout.showCalories();
            remainingNutrition[0] = remainingNutrition[0] - calories;
            remainingNutrition[1] = remainingNutrition[1] - ((calories * 0.25)/9);
            remainingNutrition[3] = remainingNutrition[3] - ((calories * 0.5)/4);
            remainingNutrition[6] = remainingNutrition[6] - ((calories * 0.25)/4);
            
            calorieGoal -= calories;
            fatGoal -= ((calories * 0.25)/9);
            carbGoal -= ((calories * 0.5)/4);
            proteinGoal -= ((calories * 0.25)/4);
            
            caloriesBurned -= calories;
            exercise.remove(name);
            workouts.remove(workout);

            String date = showDate().toString();
            AddToDiary.removeWorkout(date, name);
        }
    }

    public ArrayList<Exercise> showWorkouts() {
        return workouts;
    }

    public void addWaterFromGUI() {
        addWater(1);
        AddToDiary.addWater(this);
    }

    public void addWater(int amount) {
        waterDrunk += amount;
        remainingWater -= amount;
    }

    public int showWaterDrunk() {
        return waterDrunk;
    }

    public int showRemainingWater() {
        return remainingWater;
    }

    public double showWeight() {
        return todaysWeight;
    }

    public void addWeight(double weight) {
        todaysWeight = weight;
        if (weight > 0) {
            user.updateWeight(date, weight);
        }
    }

    public void addWeightFromGUI(double weight) {
        addWeight(weight);
        AddToDiary.updateWeight(this, user);
    }

    public String exerciseToString() {
        ArrayList<String> list = new ArrayList<>();
        for (String workout: exercise.keySet()) {
            list.add(workout.toString());
        }
        return String.join("\n", list);
    }

    public double[] showNutrition() {
        return nutrition;
    }

    public double[] showRemainingNutrition() {
        return remainingNutrition;
    }

    public double showRemainingCalories() {
        return remainingNutrition[0];
    }

    public Meal showMeal(String meal) {
        if (meal.equals("Breakfast")) {
            return breakfast;
        } else if (meal.equals("Lunch")) {
            return lunch;
        } else if (meal.equals("Dinner")) {
            return dinner;
        } else {
            return snacks;
        }
    }


    @Override
    public String toString() {
        String meals = String.format("%s \nBreakfast: %s \n\nLunch: %s \n\nDinner: %s \n\nSnacks: %s", date, breakfast.toString(), lunch.toString(), dinner.toString(), snacks.toString());
        String exercise = "\n\nToday's workouts: \n" + this.exerciseToString();
        String total = String.format("\n\nTotal Eaten: \nCalories: %.0f \nFat: %.1f \nSaturated Fat: %.1f \nCarbohydrates: %.1f \nSugar: %.1f \nFibre: %.1f \nProtein: %.1f \nSalt: %.1f", nutrition[0], nutrition[1], nutrition[2], nutrition[3], nutrition[4], nutrition[5], nutrition[6], nutrition[7]);
        String burned = "\n\nTotal calories burned: " + caloriesBurned;
        String remaining = String.format("\n\nRemaining nutrition: \nCalories: %.0f \nFat: %.1f \nSaturated Fat: %.1f \nCarbohydrates: %.1f \nSugar: %.1f \nFibre: %.1f \nProtein: %.1f \nSalt: %.1f", remainingNutrition[0], remainingNutrition[1], remainingNutrition[2], remainingNutrition[3], remainingNutrition[4], remainingNutrition[5], remainingNutrition[6], remainingNutrition[7]);

        return meals + exercise + total + burned + remaining;
    }
}