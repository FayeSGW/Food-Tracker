package src.graphics;


import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

import src.SQL.java.connect.sql.code.*;
import src.db.*;
import src.diary.*;

class TrackerControl {
    TrackerGUI tGUI;
    SummaryGUI sGUI;
    DiaryGUI dGUI;
    NewRecipeGUI rGUI;
    //DiaryControl dCont;
    //CalendarGUI cGUI;
    User user;
    Diary diary;
    Database data;
    String recipeName;

    TrackerControl(User user, Diary diary, Database data) {
        this.user = user;
        this.diary = diary;  
        this.data = data;
        sGUI = new SummaryGUI(this);
        //dCont = new DiaryControl(this);
        dGUI = new DiaryGUI(this);
        tGUI = new TrackerGUI(this, user.showName(), sGUI, dGUI);
             
    }

    public User showUser() {
        return user;
    }

    public void recipe(String name) {
        recipeName = name;
    }


    void start() {
        chooseDate(LocalDate.now());
        System.out.println(LocalDate.now());
        showGoals();
    }

    //Functionality for the date chooser on the SummaryGUI and DiaryGUI
    void updateDate(LocalDate date) {
        String day = DayOfWeek.from(date).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        int dayDate = date.getDayOfMonth();
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        sGUI.changeDate(day, dayDate, month);
        dGUI.changeDate(day, dayDate, month);
    }

    void chooseDate(LocalDate date) {
        diary.getDay(date);
        updateDate(date);
        updateNutrition();
        //System.out.println(diary.showDays().size());
    }

    LocalDate showCurrentDate() {
        return diary.showCurrentDate();
    }

    void goToPrevDay() {
        LocalDate current = showCurrentDate();
        LocalDate prev = current.minusDays(1);
        chooseDate(prev);
    }

    void goToNextDay() {
        LocalDate current = showCurrentDate();
        LocalDate next = current.plusDays(1);
        chooseDate(next);

    }

    //Open calendar window
    void openCalendar() {
        LocalDate current = showCurrentDate();
        CalendarGUI cGUI = new CalendarGUI(this, current);
    }

    //Updating nutrition display on the SummaryGUI
    void updateNutrition() {
        Day day = diary.getDay(showCurrentDate());
        //System.out.println(showCurrentDate());
        double[] remaining = day.showRemainingNutrition();
        double[] nutrition = day.showNutrition();
        int drunk = day.showWaterDrunk();

        String cals = String.format("%.0f calories remaining", remaining[0]);
        sGUI.updateCalories(cals, (int)nutrition[0]);

        String carbs = String.format("%.1f g remaining", remaining[3]);
        sGUI.updateCarbs(carbs, nutrition[3]);

        String protein = String.format("%.1f g remaining", remaining[6]);
        sGUI.updateProtein(protein, nutrition[6]);

        String fat = String.format("%.1f g remaining", remaining[1]);
        sGUI.updateFat(fat, nutrition[1]);

        sGUI.updateWater(drunk);

        showGoals();

        dGUI.updateSummary(nutrition, remaining);
        dGUI.populateMealPanels();

    }

    void showGoals() {
        int water = user.showWater();
        sGUI.setWaterProgessBounds(water);

        Day day = diary.getDay(showCurrentDate());

        double[] goals = day.showGoals();
        sGUI.setCalsGoal((int)goals[0]);
        sGUI.setCarbsGoal(goals[2]);
        sGUI.setProteinGoal(goals[3]);
        sGUI.setFatGoal(goals[1]);
    }

    //Functionality for Diary tab
    void addFoodToDiary(String meal, String name, double amount) {
        Day day = diary.getDay(showCurrentDate());
        day.addFoodFromGUI(meal, name, amount);
        updateNutrition();
    }

    void addFoodDialogue(int index, String type) {
        AddFoodControl aControl = new AddFoodControl(this, index, type);
    }

    String showFoodDisplayName(String fullName) {
        SupFood food = user.accessDatabase().findItem(fullName);
        return food.showDisplayName();
    }

    void addWater() {
        Day day = diary.getDay(showCurrentDate());
        day.addWaterFromGUI();
        updateNutrition();
    }

    ArrayList<String> showFoodsinMeal(String mealName) {
        Day day = diary.getDay(showCurrentDate());
        Meal meal = day.showMeal(mealName);
        return meal.showFoodNames();
    }

    String[] showFoodItemNutrition(String mealName, String foodName) {
        Day day = diary.getDay(showCurrentDate());
        Meal meal = day.showMeal(mealName);
        double[] nutrition = meal.showFoodItemNutrition(foodName);
        String[] nutritionString = new String[8];
        for (int i = 0; i < 8; i++) {
            nutritionString[i] = Integer.toString((int)nutrition[i]);
        }

        return nutritionString;

    }

    String showFoodItemAmount(String mealName, String foodName) {
        Day day = diary.getDay(showCurrentDate());
        Meal meal = day.showMeal(mealName);
        return meal.showFoodItemAmount(foodName);
    }

    //Functionality for user to update their weight
    void updateWeightDialogue(LocalDate date) {
        UpdateWeightGUI wGUI = new UpdateWeightGUI(this, date);
    }

    void updateWeight(LocalDate date, double weight) {
        Day day = diary.getDay(date);
        day.addWeightFromGUI(weight);
    }

    //Functionality for adding new foods to database/editing foods
    void newFoodGUI() {
        NewFoodGUI nGUI = new NewFoodGUI(this);
    }

    void newRecipeGUI() {
        rGUI = new NewRecipeGUI(this);
    }

    void editFoodorRecipeGUI(String foodName) {
        SupFood supFood = data.findItem(foodName);
        if (supFood instanceof Food) {
            Food food = (Food)supFood;
            NewFoodGUI nGUI = new NewFoodGUI(this);
            double[] nutrition = food.showNutrition();
            nGUI.existingFoodData(food.showName(), food.showDisplayName(), food.showAmount(), food.showUnit(), nutrition[0], nutrition[1], nutrition[2], nutrition[3], nutrition[4], nutrition[5], nutrition[6], nutrition[7], food.showBarcode());
        } else {
            Recipe recipe = (Recipe)supFood;
            //
        }
    }

    void saveEdited(String oldName, String newName, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        SupFood supFood = data.findItem(oldName);
        if (supFood instanceof Food) {
            Food food = (Food)supFood;
            food.edit(newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            EditFoodRecipeDatabase.addFood(oldName, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
        } else {
            Recipe recipe = (Recipe)supFood;
            //edit recipe
            //save to DB
        }

    }

    void saveNewFood(String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        data.addFood(name, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
        EditFoodRecipeDatabase.addFood(null, name, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
    }

    void saveNewRecipe(String name, double amount) {
        data.addRecipe(name, amount);
        EditFoodRecipeDatabase.addRecipe(name, amount);
    }

    void addIngredientToRecipe(String foodName, double amount) {
        System.out.println(recipeName);
        Recipe recipe = (Recipe)data.findItem(recipeName);
        SupFood ingredient = data.findItem(foodName);

        recipe.addIngredientFromGUI(ingredient.showName(), amount);
        rGUI.updateIngredientsPanel();
    }

    HashMap<String, Double> showIngredientsInRecipe() {
        Recipe recipe = (Recipe)data.findItem(recipeName);
        HashMap<String, Double> list = recipe.showIngredientList();
        return list;
    }

    void updateIngredientsList() {}
}