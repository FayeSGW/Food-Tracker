package app.diary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

import app.db.*;
import app.sql.java.connect.*;

public class Day {
    private LocalDate date;
    private Meal breakfast, lunch, dinner, snacks;
    private double[] nutrition, remainingNutrition, totalNutrition;
    private HashMap<Integer, Exercise> exercise;
    private ArrayList<Exercise> workouts;
    private int caloriesBurned = 0, waterDrunk = 0, remainingWater;
    private double calorieGoal, carbGoal, fatGoal, proteinGoal;
    private double todaysWeight = 0, todaysBodyFat = 0;
    private HashMap<String, Double> measurements = new HashMap<>();
    private User user;
    private Database database;
    private boolean realWeight = false;

    public Day(LocalDate date, User user) {
        this.date = date;
        this.user = user;
        database = user.accessDatabase();
        breakfast = new Meal("Breakfast", date, database);
        lunch = new Meal("Lunch", date, database);
        dinner = new Meal("Dinner", date, database);
        snacks = new Meal("Snacks", date, database);

        nutrition = new double[8];
        //remainingNutrition = user.showNutrition();

        Map.Entry<LocalDate, Day> prevEntry = user.accessDiary().showDiary().floorEntry(date);
        
        if (prevEntry != null) {
            //System.out.println(date + " " + prevEntry.getValue().showDate() + " " + prevEntry.getValue().showWeight());
            todaysWeight = prevEntry.getValue().showWeight();
        }
        
        /*for (int i = 0; i < nutrition.length; i++) {
            remainingNutrition[i] = user.showNutrition()[i];
        }*/
        totalNutrition = new double[8];
        if (todaysWeight > 0) {
            //totalNutrition = user.calculateNutrition(date, todaysWeight);
            for (int i = 0; i < nutrition.length; i++) {
                totalNutrition[i] = user.calculateNutrition(date, todaysWeight)[i];
            }
        } else {
            
            for (int i = 0; i < nutrition.length; i++) {
                totalNutrition[i] = user.showNutrition()[i];
            }
            //totalNutrition = user.showNutrition();
        }
       
        remainingNutrition = totalNutrition;

        calorieGoal = totalNutrition[0];
        carbGoal = totalNutrition[3];
        fatGoal = totalNutrition[1];
        proteinGoal = totalNutrition[6];

        this.exercise = new HashMap<>();
        this.workouts = new ArrayList<>();
        remainingWater = user.showWater();
        
        measurements.put("Waist", 0.0);
        measurements.put("Hips", 0.0);
        measurements.put("Calf", 0.0);
        measurements.put("Thigh", 0.0);
        measurements.put("Upper Arm", 0.0);
        measurements.put("Chest", 0.0);
        measurements.put("Body Fat", 0.0);
        if (user.showGender().equals("Female")) {
            measurements.put("Underwire", 0.0);
        }   
    }

    public double[] showTotal() {
        return totalNutrition;
    }

    public LocalDate showDate() {
        return date;
    }

    public void addByIndex(String meal, int index, double amount) {
        SupFood item = database.getItemFromIndex(index);
        addFood(meal, item, amount);
    }

    public void addFood(String meal, SupFood item, double amount) {
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

    public void addFoodFromGUI(String meal, String name, double amount, String type) {
        SupFood food = database.findItem(name);
        saveToDB(meal, food, amount, type);
    }

    public void addFoodByIndex(String meal, int index, double amount, String type) {
        SupFood food = database.getItemFromIndex(index);
        saveToDB(meal, food, amount, type);
    }

    public void saveToDB(String meal, SupFood food, double amount, String type) {
        String fullName = food.showName();
        String date = showDate().toString();
        addFood(meal, food, amount);

        AddToDiary.addFood(database, type, date, meal, fullName, food.showIndex(), amount);
    }

    public double[] showGoals() {
        double[] goals = {calorieGoal, fatGoal, carbGoal, proteinGoal};
        return goals;
    }

    public void removeFromDB(String meal, int item) {
        String date = showDate().toString();
        removeFromMemory(meal, item);
        AddToDiary.removeFood(database, date, meal, item);
    }

    public void removeFromMemory(String meal, int item) {
        String name = meal.toLowerCase().trim();
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
    }

    public void edit(String meal, int item, double weight) {
        String name = meal.toLowerCase().trim();
        double[] nutr = new double[8];
        String foodName = database.getItemFromIndex(item).showName();

        removeFromMemory(meal, item);
        addFoodByIndex(meal, item, weight, "edit");
    }

    public void copyMeal(Meal fromMeal, String toMeal, Day toDay) {
        HashMap<Integer, ArrayList<Object>> foodList = fromMeal.showFoods();
        for (Integer item: foodList.keySet()) {
            ArrayList<Object> details = foodList.get(item);
            double amount = (double) details.get(1);
            toDay.addFoodByIndex(toMeal, item, amount, "new");
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
        remainingNutrition = totalNutrition;
        exercise.clear();
        caloriesBurned = 0;
    }

    public Exercise showWorkout(Integer index) {
        return exercise.get(index);
    }

    public Integer addExercise(Integer index, String name, int minutes, int seconds, int calories) {
        Exercise workout = new Exercise(index, name, minutes, seconds, calories);
        recalculateNutritionFromExercise(calories);

        // Why do I do this?? Figure out later
        Integer ind = workout.showIndex();
        exercise.put(ind, workout);
        workouts.add(workout);

        return ind;
    }

    public void addExercisefromGUI(Integer index, String name, int minutes, int seconds, int calories) {
        Integer ind = addExercise(index, name, minutes, seconds, calories);
        String date = showDate().toString();
        AddToDiary.addExercise(ind, date, name, minutes, seconds, calories, null);
    }

    public void editExercise(Integer index, String oldName, String newName, int minutes, int seconds, int newCalories) {
        Exercise workout = exercise.get(index);
        int originalCalories = workout.showCalories();
        workout.edit(newName, minutes, seconds, newCalories);

        int caloriesDifference = newCalories - originalCalories;
        recalculateNutritionFromExercise(caloriesDifference);
        
        AddToDiary.addExercise(index, showDate().toString(), newName, minutes, seconds, newCalories, oldName);
    }

    public void removeExercise(Integer index) {
        if (exercise.containsKey(index)) {
            Exercise workout = exercise.get(index);
            int calories = workout.showCalories();
            recalculateNutritionFromExercise((calories * -1));

            exercise.remove(index);
            workouts.remove(workout);

            String date = showDate().toString();
            AddToDiary.removeWorkout(date, index);
        }
    }

    void recalculateNutritionFromExercise(int calories) {
        remainingNutrition[0] = remainingNutrition[0] + calories;
        remainingNutrition[1] = remainingNutrition[1] + ((calories * 0.25)/9); //update fat requirement based on new calories
        remainingNutrition[3] = remainingNutrition[3] + ((calories * 0.5)/4); //update carbs based on new calories
        remainingNutrition[6] = remainingNutrition[6] + ((calories * 0.25)/4); //update protein requirement based on new calories
        
        calorieGoal += calories;
        fatGoal += ((calories * 0.25)/9);
        carbGoal += ((calories * 0.5)/4);
        proteinGoal += ((calories * 0.25)/4);

        caloriesBurned += calories;
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

    public boolean hasRealWeight() {
        return realWeight;
    }

    public void addWeight(double weight) {
        todaysWeight = weight;
        realWeight = true;
        if (weight > 0) {
            user.updateWeight(date, weight);

            totalNutrition = user.calculateNutrition(date, weight);
            calorieGoal = totalNutrition[0];
            carbGoal = totalNutrition[3];
            fatGoal = totalNutrition[1];
            proteinGoal = totalNutrition[6];

            for (int i = 0; i < nutrition.length; i++) {
                remainingNutrition[i] = totalNutrition[i] - nutrition[i];
            }
        }
    }

    public void addWeightFromGUI(double weight) {
        addWeight(weight);
        AddToDiary.updateWeight(this, user);
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

    public Double getSingleMeasurement(String type) {
        return measurements.get(type);
    }

    public HashMap<String, Double> getMeasurements() {
        return measurements;
    }

    public void setMeasurement(String type, double value) {
        if (measurements.containsKey(type)) {
            measurements.put(type, value);
            if (value > 0) {
                user.setMeasurement(date, type, value);
            }
        }
    }

    public void setMeasurementFromGUI(String type, double value, boolean save) {
        if (type != null) {
            setMeasurement(type, value);
        }
        if (save) {
            AddToDiary.updateMeasurements(this, user);
        }   
    }
}