package app.ui;

import app.diary.*;

public class HistoryControl {
    TrackerControl tControl;
    HistoryGUI gui;
    User user;
    Diary diary;

    HistoryControl(TrackerControl tControl, User user) {
        this.tControl = tControl;
        this.user = user;
        gui = new HistoryGUI(this, tControl);
        diary = user.accessDiary();
    }

    public HistoryGUI showHistoryGUI() {
        return gui;
    }
}