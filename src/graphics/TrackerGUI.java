package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import src.db.*;
import src.diary.*;
import src.SQL.java.connect.sql.code.*;

class Test {
    public static void main (String [] args) {
        User user = GetFoodsDB.getUser();
        //User user = new User("Faye", "F", 81, 165, "24.07.1989", "L", 1);
        Diary diary = user.accessDiary();
        TrackerControl control = new TrackerControl(user, diary);

        Database data = user.accessDatabase();
        //GetFoodsDB.getFoods(data);
        control.start();
        

        //TrackerGUI gui = new TrackerGUI(control);
    }
}

class TrackerGUI {
    TrackerControl control;
    SummaryGUI sGUI;
    DiaryGUI dGUI;
    JFrame window;
    JTabbedPane tabbedPane;

    JPanel summary, diary, history, profile; //tab panels

    //Components for summary tab
    

    //Components for diary tab
    JPanel diaryDateButtons, remaining, breakfastTitle, breakfastContents, lunchTitle, lunchContents, dinnerTitle, dinnerContents, snacksTitle, snacksContents, diaryWater, diaryExercise;



    TrackerGUI (TrackerControl control, String name, SummaryGUI sGUI, DiaryGUI dGUI) {
        this.control = control;
        this.sGUI = sGUI;
        this.dGUI = dGUI;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame(name + "'s Food and Exercise Tracker");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        window.add(tabbedPane);

        summary = sGUI; summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        tabbedPane.add("Summary", summary);

        diary = dGUI; dGUI.setLayout(new BoxLayout(diary, BoxLayout.Y_AXIS));
        tabbedPane.add("Diary", diary);

        history = new JPanel();
        tabbedPane.add("History", history);

        profile = new JPanel();
        tabbedPane.add("Profile", profile);

        //SUMMARY TAB
        


        //DIARY TAB


        //HISTORY TAB


        //PROFILE TAB




        window.pack();
        window.setSize(500, 400);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //window.setResizable(false);

        System.out.println(String.format("%s, %s, %s, %s, %s, %s, %s, %s", dGUI.overviewCalories.getWidth(), dGUI.overviewCarbs.getWidth(), dGUI.overviewSugar.getWidth(), dGUI.overviewFat.getWidth(), dGUI.overviewSatFat.getWidth(), dGUI.overviewProtein.getWidth(), dGUI.overviewFibre.getWidth(), dGUI.overviewSalt.getWidth()));
    }

}