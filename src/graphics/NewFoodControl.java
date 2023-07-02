package src.graphics;

import java.util.ArrayList;

import src.SQL.java.connect.sql.code.*;
import src.diary.*;
import src.db.*;


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