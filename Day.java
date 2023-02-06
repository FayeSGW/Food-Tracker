import java.util.ArrayList;
import java.util.HashMap;

class Day {
    private String date;
    private Meal breakfast, lunch, dinner, snacks;
    private double[] nutrition, remainingNutrition;
    private HashMap<String, Exercise> exercise;
    private int caloriesBurned;

    public Day(String date, User user) {
        this.date = date;
        this.breakfast = new Meal("Breakfast", this.date);
        this.lunch = new Meal("Lunch", this.date);
        this.dinner = new Meal("Dinner", this.date);
        this.snacks = new Meal("Snacks", this.date);
        this.nutrition = new double[8];
        this.remainingNutrition = user.showNutrition();
        this.exercise = new HashMap<>();
        this.caloriesBurned = 0;
    }

    public String showDate() {
        return this.date;
    }

    public void addFood(String meal, String item, int amount, FoodDatabase database) {
        String name = meal.toLowerCase().trim();
        double[] foodNutrition = new double[8];
        if (name.equals("breakfast")) {
            foodNutrition = this.breakfast.addFood(item, amount, database);
        } else if (name.equals("lunch")) {
            foodNutrition = this.lunch.addFood(item, amount, database);
        } else if (name.equals("dinner")) {
            foodNutrition = this.dinner.addFood(item, amount, database);
        } else if (name.equals("snacks")) {
            foodNutrition = this.snacks.addFood(item, amount, database);
        }
        for (int i = 0; i < this.nutrition.length; i++) {
            this.nutrition[i] = this.nutrition[i] + foodNutrition[i];
            this.remainingNutrition[i] = this.remainingNutrition[i] - foodNutrition[i];

        }
    }

    public void addRecipe(String meal, String item, int amount, RecipeDatabase database) {
        String name = meal.toLowerCase().trim();
        double[] recipeNutrition = new double[8];
        if (name.equals("breakfast")) {
            recipeNutrition = breakfast.addRecipe(item, amount, database);
        } else if (name.equals("lunch")) {
            recipeNutrition = lunch.addRecipe(item, amount, database);
        } else if (name.equals("dinner")) {
            recipeNutrition = dinner.addRecipe(item, amount, database);
        } else if (name.equals("snacks")) {
            recipeNutrition = snacks.addRecipe(item, amount, database);
        }
        for (int i = 0; i < nutrition.length; i++) {
            nutrition[i] = nutrition[i] + recipeNutrition[i];
            remainingNutrition[i] = remainingNutrition[i] - recipeNutrition[i];

        }
    }



    public void removeFood(String meal, String item) {
        String name = meal.toLowerCase().trim();
        double[] nutr = new double[8];
        if (name.equals("breakfast")) {
            nutr = breakfast.remove(item);
        } else if (name.equals("lunch")) {
            nutr = lunch.remove(item);
        } else if (name.equals("dinner")) {
            nutr = dinner.remove(item);
        } else if (name.equals("sncakc")) {
            nutr = snacks.remove(item);
        }
        for (int i = 0; i < nutrition.length; i++) {
            nutrition[i] = nutrition[i] - nutr[i];
            remainingNutrition[i] = remainingNutrition[i] + nutr[i];
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

    public void addExercise(String name, int time, int calories) {
        Exercise workout = new Exercise(name, time, calories);
        remainingNutrition[0] = remainingNutrition[0] + calories;
        remainingNutrition[1] = remainingNutrition[1] + ((calories * 0.25)/9); //update fat requirement based on new calories
        remainingNutrition[3] = remainingNutrition[3] + ((calories * 0.5)/4); //update carbs based on new calories
        remainingNutrition[6] = remainingNutrition[6] + ((calories * 0.25)/4); //update protein requirement based on new calories
        caloriesBurned += calories;
        exercise.put(name, workout);
    }

    public void removeExercise(String name) {
        if (exercise.containsKey(name)) {
            Exercise workout = exercise.get(name);
            int calories = workout.showCalories();
            remainingNutrition[0] = remainingNutrition[0] - calories;
            remainingNutrition[1] = remainingNutrition[1] - ((calories * 0.25)/9);
            remainingNutrition[3] = remainingNutrition[3] - ((calories * 0.5)/4);
            remainingNutrition[6] = remainingNutrition[6] - ((calories * 0.25)/4);
            caloriesBurned -= calories;
            exercise.remove(name);
        }
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

    @Override
    public String toString() {
        String meals = String.format("%s \nBreakfast: %s \n\nLunch: %s \n\nDinner: %s \n\nSnacks: %s", date, breakfast.toString(), lunch.toString(), dinner.toString(), snacks.toString());
        String exercise = "\nToday's workouts: \n" + this.exerciseToString();
        String total = String.format("\nTotal Eaten: \nCalories: %.0f \nFat: %.1f \nSaturated Fat: %.1f \nCarbohydrates: %.1f \nSugar: %.1f \nFibre: %.1f \nProtein: %.1f \nSalt: %.1f", nutrition[0], nutrition[1], nutrition[2], nutrition[3], nutrition[4], nutrition[5], nutrition[6], nutrition[7]);
        String burned = "\n\nTotal calories burned: " + caloriesBurned;
        String remaining = String.format("\n\nRemaining nutrition: \nCalories: %.0f \nFat: %.1f \nSaturated Fat: %.1f \nCarbohydrates: %.1f \nSugar: %.1f \nFibre: %.1f \nProtein: %.1f \nSalt: %.1f", remainingNutrition[0], remainingNutrition[1], remainingNutrition[2], remainingNutrition[3], remainingNutrition[4], remainingNutrition[5], remainingNutrition[6], remainingNutrition[7]);

        return meals + exercise + total + burned + remaining;
    }
}