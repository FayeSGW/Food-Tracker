import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Test {
    public static void main (String [] args) {
        User user = new User("Faye", "F", 81, 165, "24.07.1989", "loss", 1);
        Diary diary = new Diary("", user);
        TrackerControl control = new TrackerControl(user, diary);
        control.start();
        

        //TrackerGUI gui = new TrackerGUI(control);
    }
}

class TrackerGUI {
    TrackerControl control;
    SummaryGUI sGUI;
    JFrame window;
    JTabbedPane tabbedPane;

    JPanel summary, diary, history, profile; //tab panels

    //Components for summary tab
    

    //Components for diary tab
    JPanel diaryDateButtons, remaining, breakfastTitle, breakfastContents, lunchTitle, lunchContents, dinnerTitle, dinnerContents, snacksTitle, snacksContents, diaryWater, diaryExercise;



    TrackerGUI (TrackerControl control, SummaryGUI sGUI) {
        this.control = control;
        this.sGUI = sGUI;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Food and Exercise Tracker");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        window.add(tabbedPane);

        summary = sGUI; summary.setLayout(new BoxLayout(summary, BoxLayout.PAGE_AXIS));
        tabbedPane.add("Summary", summary);

        diary = new JPanel();
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
        window.setSize(500, 500);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

}