package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

class DiaryGUI extends JPanel {
    TrackerControl control;

    JPanel dateButtonsPanel;
    JButton prevDay, chooseDay, nextDay;
    

    DiaryGUI(TrackerControl control) {
        this.control = control;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        dateButtonsPanel = new JPanel(); this.add(dateButtonsPanel);

        prevDay = new JButton("<"); prevDay.addActionListener(new goToPrev());
        chooseDay = new JButton(".."); chooseDay.addActionListener(new chooseDate());
        nextDay = new JButton(">"); nextDay.addActionListener(new goToNext());
        dateButtonsPanel.add(prevDay); dateButtonsPanel.add(chooseDay); dateButtonsPanel.add(nextDay);
    }

    void changeDate(String day, int date, String month) {
        String string = String.format("%s %d %s", day, date, month);
        chooseDay.setText(string);
    }

    class chooseDate implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            //LocalDate current = control.showCurrentDate();
            //CalendarGUI cGUI = new CalendarGUI(control, current);
            control.openCalendar();
        }
    }

    class goToPrev implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            control.goToPrevDay();
        }
    }

    class goToNext implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            control.goToNextDay();
        }
    }
}