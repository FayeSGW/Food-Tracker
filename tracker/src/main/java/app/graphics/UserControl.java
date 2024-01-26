package app.graphics;

import java.time.LocalDate;

import app.diary.*;

class UserControl {
    TrackerControl tControl;
    UserGUI gui;
    User user;
    LocalDate tempDOB;

    UserControl(TrackerControl tControl, User user) {
        this.tControl = tControl;
        this.user = user;
        gui = new UserGUI(this, tControl);
    }

    public UserGUI showUserGUI() {
        return gui;
    }

    public String userName() {
        return user.showName();
    }

    public String userGender() {
        return user.showGender();
    }

    public String userDOB() {
        return user.showDOB();
    }

    public int userHeight() {
        return user.showHeight();
    }

    public double userWeight() {
        return user.showWeight();
    }

    public String userGoal() {
        return user.showGoal();
    }

    public double userRate() {
        return user.showRate();
    }

    public int userWater() {
        return user.showWater();
    }

    public void openCalendar() {
        LocalDate dob = LocalDate.parse(user.showDOB());
        CalendarGUI cGUI = new CalendarGUI(tControl, dob, "DOB");
    }

    public void updateUserParameters(String oldName, String newName, String gender, double weight, int height, String dob,
        String goal, double rate, int water) {
            user.edit(oldName, newName, gender, weight, height, dob, goal, rate, water);
        }
}