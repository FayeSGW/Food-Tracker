package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.DayOfWeek;

import src.db.*;
import src.diary.*;

class TrackerControl {
    TrackerGUI tGUI;
    SummaryGUI sGUI;
    DiaryGUI dGUI;
    //CalendarGUI cGUI;
    User user;
    Diary diary;

    TrackerControl(User user, Diary diary) {
        this.user = user;
        this.diary = diary;  
        sGUI = new SummaryGUI(this);
        dGUI = new DiaryGUI(this);
        tGUI = new TrackerGUI(this, user.showName(), sGUI, dGUI);
             
    }

    public User showUser() {
        return user;
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
        System.out.println(diary.showDays().size());
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

    void addFoodTest() {
        Day day = diary.getDay(showCurrentDate());
        day.addFood("breakfast", "Banana", 100);
        updateNutrition();
    }

    void addFoodToDiary(String meal, String name, int amount) {
        Day day = diary.getDay(showCurrentDate());
        day.addFood(meal, name, amount);
        updateNutrition();
    }

    void addFoodDialogue() {
        AddFoodControl aControl = new AddFoodControl(this);
    }
}