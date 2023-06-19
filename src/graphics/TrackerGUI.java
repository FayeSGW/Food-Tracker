package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import src.db.*;
import src.diary.*;

class Test {
    public static void main (String [] args) {
        User user = new User("Faye", "F", 81, 165, "24.07.1989", "loss", 1);
        Diary diary = new Diary("", user);
        TrackerControl control = new TrackerControl(user, diary);

        Database data = user.accessDatabase();
        data.addFood("Apple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0, null);
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



    TrackerGUI (TrackerControl control, SummaryGUI sGUI, DiaryGUI dGUI) {
        this.control = control;
        this.sGUI = sGUI;
        this.dGUI = dGUI;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Food and Exercise Tracker");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        window.add(tabbedPane);

        summary = sGUI; summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        tabbedPane.add("Summary", summary);

        diary = dGUI;
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
    }

}