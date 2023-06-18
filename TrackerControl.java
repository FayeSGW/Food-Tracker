import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.DayOfWeek;

class TrackerControl {
    TrackerGUI tGUI;
    SummaryGUI sGUI;
    CalendarGUI cGUI;
    User user;
    Diary diary;

    TrackerControl(User user, Diary diary) {
        sGUI = new SummaryGUI(this);
        tGUI = new TrackerGUI(this, sGUI);
        this.user = user;
        this.diary = diary;
        
        
        //cGUI = new CalendarGUI(this, LocalDate.now());
        
    }

    void start() {
        chooseDate(LocalDate.now());
        showGoals();
    }

    void updateDate(LocalDate date) {
        String day = DayOfWeek.from(date).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        int dayDate = date.getDayOfMonth();
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        sGUI.changeDate(day, dayDate, month);
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

    void updateNutrition() {
        Day day = diary.getDay(showCurrentDate());
        double[] remaining = day.showRemainingNutrition();
        double[] nutrition = day.showNutrition();
        int drunk = day.showWaterDrunk();

        String cals = String.format("%.0f calories remaining", remaining[0]);
        sGUI.updateCalories(cals);

        String carbs = String.format("%.1f g remaining  ", remaining[3]);
        sGUI.updateCarbs(carbs, nutrition[3]);

        String protein = String.format("%.1f g remaining", remaining[6]);
        sGUI.updateProtein(protein, nutrition[6]);

        String fat = String.format("%.1f g remaining      ", remaining[1]);
        sGUI.updateFat(fat, nutrition[1]);

        sGUI.updateWater(drunk);
    }

    void showGoals() {
        int water = user.showWater();
        sGUI.setWaterProgessBounds(water);

        double[] nutrition = user.showNutrition();
        double carbs = nutrition[3], protein = nutrition[6], fat = nutrition[1];
        sGUI.setCarbsGoal(carbs);
        sGUI.setProteinGoal(protein);
        sGUI.setFatGoal(fat);
    }

    void updateWaterDrunk() {
        Day day = diary.getDay(showCurrentDate());
        int drunk = day.showWaterDrunk();
        sGUI.updateWater(drunk);
    }
}