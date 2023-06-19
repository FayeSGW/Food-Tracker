package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

/*class CalTest {
    public static void main (String [] args) {
        TrackerControl control = new TrackerControl();
        CalendarGUI gui = new CalendarGUI(control);
    }
}*/

class CalendarGUI {
    TrackerControl control;

    LocalDate date = LocalDate.now();
    int month = date.getMonthValue(), year = date.getYear(), day = 17;

    JFrame calendar;
    JPanel whole, monthYear, days, todayText;

    JButton prevYear, nextYear, prevMonth, nextMonth;
    JButton[][] daysGrid = new JButton[6][7];
    JButton[] grid = new JButton[42];
    JLabel todayLabel;

    JComboBox<String> months;
    JComboBox<Integer> years;

    String[] dayLabelList = {"M", "T", "W", "T", "F", "S", "S"};
    String[] chooseMonthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    Integer[] chooseYearList = {2020, 2021, 2022, 2023, 2024, 2025, 2026};

    CalendarGUI(TrackerControl control, LocalDate date) {
        this.control = control;
        this.date = date;
        month = date.getMonthValue(); 
        year = date.getYear(); 
        day = date.getDayOfMonth();


        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        calendar = new JFrame("Choose Date");
        calendar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        whole = new JPanel(new BorderLayout());
        calendar.add(whole);

        monthYear = new JPanel(new FlowLayout());
        days = new JPanel(); days.setLayout(new GridLayout(7,7));
        todayText = new JPanel();
        whole.add(monthYear, BorderLayout.NORTH); 

        prevYear = new JButton("<<"); prevYear.addActionListener(new goToPrevYear());
        prevMonth = new JButton("<"); prevMonth.addActionListener(new goToPrevMonth());
        months = new JComboBox<>(chooseMonthList); months.setSelectedIndex(month-1);
        months.addActionListener(new chooseMonth());
        years = new JComboBox<>(chooseYearList); years.setSelectedIndex(setComboBox());
        years.addActionListener(new chooseYear());
        nextMonth = new JButton(">"); nextMonth.addActionListener(new goToNextMonth());
        nextYear = new JButton(">>"); nextYear.addActionListener(new goToNextYear());
        monthYear.add(prevYear); monthYear.add(prevMonth); 
        monthYear.add(months); monthYear.add(years);
        monthYear.add(nextMonth); monthYear.add(nextYear);

        for (int c = 0; c < 7; c++) {
            JLabel day = new JLabel(dayLabelList[c]);
            days.add(day);
        }

        int a = 0;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                JButton d = new JButton("0");
                //daysGrid[r][c] = d;
                grid[a] = d;
                a++;
                d.addActionListener(new chooseDay());
                days.add(d);
            }
        }
        
        whole.add(days, BorderLayout.CENTER);

        todayLabel = new JLabel("test");
        todayText.add(todayLabel);
        whole.add(todayText, BorderLayout.SOUTH);

        update();

        calendar.pack();
        //calendar.setSize(300,200);
        calendar.setLocationRelativeTo(null);
        calendar.setVisible(true);
    }

    private int setComboBox() {
        int index = 0;
        for (int yr: chooseYearList) {
            if (yr == year) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private void update() {
        LocalDate firstOfMonth = LocalDate.of(year, month, 1); 
        int weekDay = firstOfMonth.getDayOfWeek().getValue();
        int monthLength = firstOfMonth.lengthOfMonth();
        int d = 1;

        for (int i = weekDay-1; i < (weekDay + monthLength-1); i++) {
            JButton button = grid[i];
            button.setText(Integer.toString(d));
            d++;
        }

        int prevMonthLength = updatePrev(firstOfMonth);
        for (int j = (weekDay-2); j >= 0 ; j--) {
            JButton button = grid[j];
            button.setText(Integer.toString(prevMonthLength));
            prevMonthLength--;
        }
      
        d = 1;
        for (int k = weekDay+monthLength-1; k < 42; k++) {
            JButton button = grid[k];
            button.setText(Integer.toString(d));
            d++;
        }

        d = 1;
    }

    private int updatePrev(LocalDate current) {
        LocalDate prev = null;
        if (current.getMonthValue() == 1) {
            prev = LocalDate.of(year, 12, 20);
        } else {
            prev = LocalDate.of(year, month-1, 20);
        }

        int prevMonthLength = prev.lengthOfMonth();
        return prevMonthLength;
    }

    class goToPrevMonth implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            if (month == 1) {
                month = 12;
                year = year-1;
                years.setSelectedIndex(setComboBox());
            } else {
                month = month-1;
            }
            update();
            months.setSelectedIndex(month-1);

        }
    }

    class goToNextMonth implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            if (month == 12) {
                month = 1;
                year = year+1;
                years.setSelectedIndex(setComboBox());
            } else {
                month = month + 1;
            }
            update();
            months.setSelectedIndex(month-1);
        }
    }

    class goToPrevYear implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            year = year-1;
            years.setSelectedIndex(setComboBox());
            update();
        }
    }

    class goToNextYear implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            year = year+1;
            years.setSelectedIndex(setComboBox());
            update();
        }
    }


    class chooseMonth implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            month = months.getSelectedIndex() + 1;
            update();
            //Update day grid and send month to control
        }
    }

    class chooseYear implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            year = (int)years.getSelectedItem();
            update();
            //Update day grid and send year to control
        }
    }

    class chooseDay implements ActionListener {
        
        @Override 
        public void actionPerformed (ActionEvent e) {
            JButton button = (JButton) e.getSource();
            day = Integer.parseInt(button.getText());
            LocalDate newDate = LocalDate.of(year, month, day);
            control.chooseDate(newDate);
            //Send day to control

            calendar.dispose();
        }
    }

    class prevMonthAL implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent e) {

        }
    }
}