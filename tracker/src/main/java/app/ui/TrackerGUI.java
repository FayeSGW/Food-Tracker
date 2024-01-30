package app.ui;

import javax.swing.*;
import io.github.cdimascio.dotenv.*;

import app.db.*;
import app.diary.*;
import app.sql.java.connect.*;

class Test {
    public static void main (String [] args) {
        Dotenv dt = Config.dotenv;
        Config.setDBUrl(dt.get("DATABASE"));
        Config.setTesting(false);
        
        User user = GetFoodsDB.getUser();
        Diary diary = user.accessDiary();
    
        Database data = user.accessDatabase();
        TrackerControl control = new TrackerControl(user, diary, data);
        control.start();
    }
}

class TrackerGUI {
    TrackerControl control;
    SummaryGUI sGUI;
    DiaryGUI dGUI;
    ChangeDatabaseGUI dbGUI;
    UserGUI uGUI;
    HistoryGUI hGUI;
    JFrame window;
    JTabbedPane tabbedPane;

    JPanel summary, diary, history, database, profile; //tab panels

    TrackerGUI (TrackerControl control, String name, SummaryGUI sGUI, DiaryGUI dGUI, ChangeDatabaseGUI dbGUI,
                UserGUI uGUI, HistoryGUI hGUI) {
        this.control = control;
        this.sGUI = sGUI;
        this.dGUI = dGUI;
        this.dbGUI = dbGUI;
        this.uGUI = uGUI;
        this.hGUI = hGUI;

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

        history = hGUI; hGUI.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS));
        tabbedPane.add("History", history);

        database = dbGUI;
        tabbedPane.add("Database", database);

        profile = uGUI; 
        tabbedPane.add("Profile", profile);

        window.pack();
        window.setSize(500, 500);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //window.setResizable(false);
    }

}