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
    ChangeDatabaseControl dbControl;
    ChangeDatabaseGUI dbGUI;
    //DiaryControl dCont;
    //CalendarGUI cGUI;
    User user;
    Diary diary;
    Database data;

    LocalDate tempDateForCopy;


    TrackerControl(User user, Diary diary, Database data) {
        this.user = user;
        this.diary = diary;  
        this.data = data;
        sGUI = new SummaryGUI(this);
        //dCont = new DiaryControl(this);
        dGUI = new DiaryGUI(this);
        dbControl = new ChangeDatabaseControl(this);
        dbGUI = dbControl.showDbGUI();
        tGUI = new TrackerGUI(this, user.showName(), sGUI, dGUI, dbGUI);
             
    }

    public User showUser() {
        return user;
    }


    void start() {
        chooseDate(LocalDate.now());
        System.out.println(LocalDate.now());
        showGoals();
    }

    //------------------CALENDAR-----------------------------------------------
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
    void openCalendar(String type) {
        LocalDate current = showCurrentDate();
        CalendarGUI cGUI = new CalendarGUI(this, current, type);
    }

    //---------------------SUMMARY--------------------------------------------
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

    //-----------------DIARY------------------------------------------------
    //Functionality for Diary tab
    void addFoodToDiary(String meal, String name, double amount) {
        Day day = diary.getDay(showCurrentDate());
        day.addFoodFromGUI(meal, name, amount);
        updateNutrition();
    }

    void addFoodDialogue(int index, String type) {
        AddFoodControl aControl = new AddFoodControl(this, index, type, dbControl);
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

    void addExerciseDialogue(String type) {
        AddExerciseGUI eGUI = new AddExerciseGUI(this, showCurrentDate(), type);
    }

    void addExercise(LocalDate date, Integer index, String workoutName, int workoutMinutes, int workoutSeconds, int caloriesBurned) {
        Day day = diary.getDay(date);
        day.addExercisefromGUI(index, workoutName, workoutMinutes, workoutSeconds, caloriesBurned);
        updateNutrition();
    }

    void editExercise(LocalDate date, Integer index, String oldName, String workoutName, int workoutMinutes, int workoutSeconds, int caloriesBurned) {
        Day day = diary.getDay(date);
        day.editExercise(index, oldName, workoutName, workoutMinutes, workoutSeconds, caloriesBurned);
        
    }

    void editExerciseDialogue(String type, Integer index) {
        Day day = diary.getDay(showCurrentDate());
        Exercise workout= day.showWorkout(index);
        String workoutName = workout.showName();
        String time = workout.showTime();
        int cals = workout.showCalories();
        AddExerciseGUI eGUI = new AddExerciseGUI(this, showCurrentDate(), type);
        eGUI.existingData(workoutName, time, cals, index);
    }

    void removeExercise(LocalDate date, Integer index) {
        Day day = diary.getDay(date);
        day.removeExercise(index);
        updateNutrition();
    }

    HashMap<Integer, ArrayList<String>> showWorkouts() {
        Day day = diary.getDay(showCurrentDate());
        ArrayList<Exercise> workouts = day.showWorkouts();
        HashMap<Integer, ArrayList<String>> exerciseList = new HashMap<>();

        for (Exercise workout: workouts) {
            String name = workout.showName();
            String time  = workout.showTime();
            String cals = Integer.toString(workout.showCalories());
            Integer index = workout.showIndex();

            //System.out.println(name);
            ArrayList<String> details = new ArrayList<>();
            details.add(name); details.add(time); details.add(cals); 
            exerciseList.put(index, details);
        }

        return exerciseList;
    }

    void newFood() {
        //ChangeDatabaseControl cControl = new ChangeDatabaseControl(this);
        dbControl.newFoodGUI();
    }

    void newRecipe() {
        //ChangeDatabaseControl cControl = new ChangeDatabaseControl(this);
        dbControl.newRecipeGUI();
    }

    void removeFromMeal(String mealName, String foodName) {
        SupFood food = data.findItem(foodName);
        Day day = diary.getDay(showCurrentDate());
        //Meal meal = day.showMeal(mealName);
        //meal.remove(food.showName());
        day.remove(mealName, food.showName());
    }

    void clearMeal(String mealName) {
        Day day = diary.getDay(showCurrentDate());
        //Meal meal = day.showMeal(mealName);
        //meal.removeAll();
        day.clearMeal(mealName);
        updateNutrition();
    }

    void editRecipeIngredientDialogue(String recipeName, String foodName) {
        EditFoodEntryGUI fGUI = new EditFoodEntryGUI(this, "recipe", recipeName);
        SupFood food = data.findItem(foodName);
        String unit = food.showUnit();
        Recipe recipe = (Recipe) data.findItem(recipeName);

        double ingredientWeight = recipe.showIngredientWeight(food.showName());
        fGUI.existingData(food.showDisplayName(), ingredientWeight, unit);
    }

    void editRecipeIngredient(String recipeName, String foodName, double newAmount) {
        /*Recipe recipe = (Recipe) data.findItem(recipeName);
        Food food = (Food) data.findItem(foodName);
        recipe.editIngredient(food.showName(), newAmount);*/
        dbControl.editRecipeIngredient(foodName, newAmount);
    }

    void editMealDialogue(String mealName, String foodName) {
        EditFoodEntryGUI fGUI = new EditFoodEntryGUI(this, "diary", mealName);
        SupFood food = data.findItem(foodName);
        Day day = diary.getDay(showCurrentDate());
        Meal meal = day.showMeal(mealName);

        ArrayList<Object> data = meal.showFood(foodName);
        fGUI.existingData(food.showDisplayName(), (double)data.get(1), (String)data.get(2));
    }

    void editMeal(String mealName, String foodName, double newAmount) {
        SupFood food = data.findItem(foodName);
        Day day = diary.getDay(showCurrentDate());
        //Meal meal = day.showMeal(mealName);
        //meal.edit(food.showName(), newAmount);
        day.edit(mealName, food.showName(), newAmount);
    }


    void setTempDate(LocalDate date) {
        tempDateForCopy = date;
    }

    LocalDate showTempDate() {
        return tempDateForCopy;
    }

    void copyMealDialogue(String mealName) {
        CopyMealGUI cmGUI = new CopyMealGUI(this, mealName, showCurrentDate());
    }

    void copyMeal(String fromMeal, String toMeal) {
        Day fromDay = diary.getDay(showCurrentDate());

        if (tempDateForCopy == null) {
            return;
        }

        Day toDay = diary.getDay(tempDateForCopy);
        Meal from = fromDay.showMeal(fromMeal);

        fromDay.copyMeal(from, toMeal, toDay);
        

        chooseDate(tempDateForCopy);
        tempDateForCopy = null;
        updateNutrition();
    }
}