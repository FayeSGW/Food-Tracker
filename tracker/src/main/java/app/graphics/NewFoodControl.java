package app.graphics;

import java.util.ArrayList;

import app.sql.java.connect.*;
import app.diary.*;
import app.db.*;


class NewFoodControl {
    TrackerControl tControl;
    NewFoodGUI gui;
    Database data;

    NewFoodControl (TrackerControl tControl) {
        this.tControl = tControl;
        //gui = new NewFoodGUI(this);
        data = tControl.showUser().accessDatabase();
    }


}