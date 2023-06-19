import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

class DateButtons extends JPanel {
    TrackerControl control;

    JPanel dateButtons;
    static JButton prevDay, chooseDay, nextDay;

    DateButtons(TrackerControl control) {
        this.control = control;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        prevDay = new JButton("<"); prevDay.addActionListener(new goToPrev());
        chooseDay = new JButton(".."); chooseDay.addActionListener(new chooseDate());
        nextDay = new JButton(">"); nextDay.addActionListener(new goToNext());
        this.add(prevDay); this.add(chooseDay); this.add(nextDay);
    }

    static void changeDate(String day, int date, String month) {
        String string = String.format("%s %d %s", day, date, month);
        chooseDay.setText(string);
    }

    class chooseDate implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            LocalDate current = control.showCurrentDate();
            CalendarGUI cGUI = new CalendarGUI(control, current);
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