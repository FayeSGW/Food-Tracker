package app.ui;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

import app.db.*;
import app.diary.*;

class TrackerControl {
    TrackerGUI tGUI;
    SummaryGUI sGUI;
    DiaryGUI dGUI;
    ChangeDatabaseControl dbControl;
    ChangeDatabaseGUI dbGUI;
    UserControl uControl;
    UserGUI uGUI;
    HistoryControl hControl;
    HistoryGUI hGUI;
    User user;
    Diary diary;
    Database data;

    LocalDate tempDateForCopy;

    TrackerControl(User user, Diary diary, Database data) {
        this.user = user;
        this.diary = diary;  
        this.data = data;
        sGUI = new SummaryGUI(this);
        dGUI = new DiaryGUI(this);
        dbControl = new ChangeDatabaseControl(this);
        dbGUI = dbControl.showDbGUI();
        uControl = new UserControl(this, user);
        uGUI = uControl.showUserGUI();
        hControl = new HistoryControl(this, user);
        hGUI = hControl.showHistoryGUI();
        
        

        tGUI = new TrackerGUI(this, user.showName(), sGUI, dGUI, dbGUI, uGUI, hGUI);  
    }

    public User showUser() {
        return user;
    }

    void start() {
        chooseDate(LocalDate.now());
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
        diary.goToDay(date);
        updateDate(date);
        updateNutrition();
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
        Day day = diary.goToDay(showCurrentDate());
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

        Day day = diary.goToDay(showCurrentDate());

        double[] goals = day.showGoals();
        sGUI.setCalsGoal((int)goals[0]);
        sGUI.setCarbsGoal(goals[2]);
        sGUI.setProteinGoal(goals[3]);
        sGUI.setFatGoal(goals[1]);
    }

    //-----------------DIARY------------------------------------------------
    //Functionality for Diary tab
    void addFoodToDiary(String meal, String name, double amount) {
        Day day = diary.goToDay(showCurrentDate());
        day.addFoodFromGUI(meal, name, amount, "new");
        updateNutrition();
    }

    AddFoodGUI addFoodDialogue(int index, String type) {
        AddFoodControl aControl = new AddFoodControl(this, index, type, dbControl);
        return aControl.showGUI();
    }

    String showFoodDisplayName(String fullName) {
        SupFood food = user.accessDatabase().findItem(fullName);
        return food.showDisplayName();
    }

    void addWater() {
        Day day = diary.goToDay(showCurrentDate());
        day.addWaterFromGUI();
        updateNutrition();
    }

    HashMap<Integer, ArrayList<Object>> showFoodsinMeal(String mealName) {
        Day day = diary.goToDay(showCurrentDate());
        Meal meal = day.showMeal(mealName);
        return meal.showFoods();
    }

    String[] showFoodItemNutrition(String mealName, int index) {
        Day day = diary.goToDay(showCurrentDate());
        Meal meal = day.showMeal(mealName);
        double[] nutrition = meal.showFoodItemNutrition(index);
        String[] nutritionString = new String[8];
        for (int i = 0; i < 8; i++) {
            nutritionString[i] = Integer.toString((int)nutrition[i]);
        }
        return nutritionString;
    }

    String showFoodItemAmount(String mealName, int index) {
        Day day = diary.goToDay(showCurrentDate());
        Meal meal = day.showMeal(mealName);
        return meal.showFoodItemAmount(index);
    }

    //Functionality for user to update their weight
    void updateWeightDialogue(LocalDate date) {
        UpdateWeightGUI wGUI = new UpdateWeightGUI(this, date);
    }

    void updateWeight(LocalDate date, double weight) {
        Day day = diary.goToDay(date);
        day.addWeightFromGUI(weight);
        uGUI.updateWeight();
    }

    //Functionality for user to add/edit workouts
    void addExerciseDialogue(String type) {
        AddExerciseGUI eGUI = new AddExerciseGUI(this, showCurrentDate(), type);
    }

    void addExercise(LocalDate date, Integer index, String workoutName, int workoutMinutes, int workoutSeconds, int caloriesBurned) {
        Day day = diary.goToDay(date);
        day.addExercisefromGUI(index, workoutName, workoutMinutes, workoutSeconds, caloriesBurned);
        updateNutrition();
    }

    void editExercise(LocalDate date, Integer index, String oldName, String workoutName, int workoutMinutes, int workoutSeconds, int caloriesBurned) {
        Day day = diary.goToDay(date);
        day.editExercise(index, oldName, workoutName, workoutMinutes, workoutSeconds, caloriesBurned);
    }

    void editExerciseDialogue(String type, Integer index) {
        Day day = diary.goToDay(showCurrentDate());
        Exercise workout= day.showWorkout(index);
        String workoutName = workout.showName();
        String time = workout.showTime();
        int cals = workout.showCalories();
        AddExerciseGUI eGUI = new AddExerciseGUI(this, showCurrentDate(), type);
        eGUI.existingData(workoutName, time, cals, index);
    }

    void removeExercise(LocalDate date, Integer index) {
        Day day = diary.goToDay(date);
        day.removeExercise(index);
        updateNutrition();
    }

    HashMap<Integer, ArrayList<String>> showWorkouts() {
        Day day = diary.goToDay(showCurrentDate());
        ArrayList<Exercise> workouts = day.showWorkouts();
        HashMap<Integer, ArrayList<String>> exerciseList = new HashMap<>();
        for (Exercise workout: workouts) {
            String name = workout.showName();
            String time  = workout.showTime();
            String cals = Integer.toString(workout.showCalories());
            Integer index = workout.showIndex();

            ArrayList<String> details = new ArrayList<>();
            details.add(name); details.add(time); details.add(cals); 
            exerciseList.put(index, details);
        }
        return exerciseList;
    }

    void newFood() {
        dbControl.newFoodGUI();
    }

    void newRecipe() {
        dbControl.newRecipeGUI();
    }

    void removeFromMeal(String mealName, int index) {
        Day day = diary.goToDay(showCurrentDate());
        day.removeFromDB(mealName, index);
    }

    void clearMeal(String mealName) {
        Day day = diary.goToDay(showCurrentDate());
        day.clearMeal(mealName);
        updateNutrition();
    }

    void editRecipeIngredientDialogue(String recipeName, int index) {
        EditFoodEntryGUI fGUI = new EditFoodEntryGUI(this, "recipe", recipeName, index);
        SupFood food = data.getItemFromIndex(index);
        String unit = food.showUnit();
        Recipe recipe = (Recipe) data.findItem(recipeName);

        double ingredientWeight = recipe.showIngredientWeight(index);
        fGUI.existingData(food.showDisplayName(), ingredientWeight, unit);
    }

    void editRecipeIngredient(String recipeName, int index, double newAmount) {
        dbControl.editRecipeIngredient(index, newAmount);
    }

    void editMealDialogue(String mealName, int index) {
        EditFoodEntryGUI fGUI = new EditFoodEntryGUI(this, "diary", mealName, index);
        SupFood food = data.getItemFromIndex(index);
        Day day = diary.goToDay(showCurrentDate());
        Meal meal = day.showMeal(mealName);

        ArrayList<Object> data = meal.showFood(index);
        fGUI.existingData(food.showDisplayName(), (double)data.get(1), (String)data.get(2));
    }

    void editMeal(String mealName, int index, double newAmount) {
        Day day = diary.goToDay(showCurrentDate());
        day.edit(mealName, index, newAmount);
    }

    void setTempDate(LocalDate date) {
        tempDateForCopy = date;
    }

    LocalDate showTempDate() {
        return tempDateForCopy;
    }

    void copyMealDialogue(String mealName, int mealIndex) {
        CopyMealGUI cmGUI = new CopyMealGUI(this, mealName, mealIndex, showCurrentDate());
    }

    void copyMeal(String fromMeal, String toMeal) {
        Day fromDay = diary.goToDay(showCurrentDate());

        if (tempDateForCopy == null) {
            return;
        }
        Day toDay = diary.goToDay(tempDateForCopy);
        Meal from = fromDay.showMeal(fromMeal);

        fromDay.copyMeal(from, toMeal, toDay);
        chooseDate(tempDateForCopy);
        tempDateForCopy = null;
        updateNutrition();
    }

    void setUserTempDOB(LocalDate date) {
        uGUI.changeDOBLabel(date.toString());
    }
}