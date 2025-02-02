package app.ui;

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

    public String getUserName() {
        return user.showName();
    }

    public String getUserGender() {
        return user.showGender();
    }

    public String getUserDOB() {
        return user.showDOB();
    }

    public int getUserHeight() {
        return user.showHeight();
    }

    public double getUserWeight() {
        return user.showWeight();
    }

    public String getUserGoal() { // Loss, gain, maintain
        return user.showGoal();
    }

    public double getUserRate() { // Rate of weight loss/gain per week
        return user.showRate();
    }

    public int getUserWaterGoal() {
        return user.showWater();
    }

    public double userMeasurement(String type) {
        return user.getSingleMeasurement(type);
    }

    public void openCalendar() {
        LocalDate dob = LocalDate.parse(user.showDOB());
        CalendarGUI cGUI = new CalendarGUI(tControl, dob, "DOB");
    }

    public boolean updateUserParameters(String oldName, String newName, String gender, double weight, int height, String dob,
        String goal, double rate, int water, double[] measurements) {
            return user.edit(oldName, newName, gender, weight, height, dob, goal, rate, water, measurements);
        }
}