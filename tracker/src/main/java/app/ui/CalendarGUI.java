package app.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;
import java.util.stream.*;

class CalendarGUI {
    TrackerControl control;

    LocalDate date, today = LocalDate.now();
    int month, year, day;
    String type;

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
    Integer[] chooseYearList;  
    

    CalendarGUI(TrackerControl control, LocalDate date, String type) {
        this.control = control;
        this.date = date;
        month = date.getMonthValue(); 
        year = date.getYear(); 
        day = date.getDayOfMonth();
        this.type = type;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        calendar = new JFrame("Choose Date");
        calendar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        whole = new JPanel(new BorderLayout()); calendar.add(whole);

        monthYear = new JPanel(new FlowLayout());
        days = new JPanel(); days.setLayout(new GridLayout(7,7));
        todayText = new JPanel();
        whole.add(monthYear, BorderLayout.NORTH); 

        //Sets different year ranges for choosing DOB (via User profie) and choosing a day for the diary
        if (type == null || type.equals("copy")) {
            chooseYearList = IntStream.range(2020, (today.getYear()+5)).boxed().toArray(Integer[]::new);
        } else if (type.equals("DOB")) {
            chooseYearList = IntStream.range(1950, (today.getYear()+1)).boxed().toArray(Integer[]::new);
            today = date;
        }

        prevYear = new JButton("<<"); prevYear.addActionListener(new goToPrevYear());
        prevMonth = new JButton("<"); prevMonth.addActionListener(new goToPrevMonth());
        months = new JComboBox<>(chooseMonthList); months.setSelectedIndex(month-1);
        months.addActionListener(new chooseMonth());
        years = new JComboBox<>(chooseYearList); years.setSelectedItem(year);
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
                grid[a] = d;
                a++;
                d.addActionListener(new chooseDay());
                days.add(d);
            }
        }
        whole.add(days, BorderLayout.CENTER);

        //Change label depending on if calendar being used for the diary or for updating DOB
        if (type == null || type.equals("copy")) {
            todayLabel = new JLabel("Today's Date: " + today.toString());
            todayLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    control.chooseDate(LocalDate.now());
                    calendar.dispose();
                }
            });
        } else if (type.equals("DOB")) {
            todayLabel = new JLabel("Current DOB: " + today.toString());
        }
        todayText.add(todayLabel);
        whole.add(todayText, BorderLayout.SOUTH);

        update();

        calendar.pack();
        //calendar.setSize(300,200);
        calendar.setLocationRelativeTo(null);
        calendar.setVisible(true);
    }

    private void update() {
        for (JButton button: grid) {
            button.setBackground(null);
        }
        LocalDate firstOfMonth = LocalDate.of(year, month, 1); 
        int weekDay = firstOfMonth.getDayOfWeek().getValue();
        int monthLength = firstOfMonth.lengthOfMonth();
        int dayNum = 1;

        LocalDate current = control.showCurrentDate();
        int currentDate = current.getDayOfMonth();
        int currentMonth = current.getMonthValue();
        int currentYear = current.getYear();

        for (int i = weekDay-1; i < (weekDay + monthLength-1); i++) {
            JButton button = grid[i];
            button.setText(Integer.toString(dayNum));
            if (dayNum == currentDate && currentMonth == month && currentYear == year) {
                button.setBackground(Color.GREEN);
            }
            dayNum++;
        }

        //Sets day labels for previous month's overlapping days correctly
        int prevMonthLength = updatePrev(firstOfMonth);
        for (int j = (weekDay-2); j >= 0 ; j--) {
            JButton button = grid[j];
            button.setText(Integer.toString(prevMonthLength));
            button.setBackground(Color.GRAY);
            prevMonthLength--;
        }
      
        //Sets day labels for next month's overlapping days correctly
        dayNum = 1;
        for (int k = weekDay+monthLength-1; k < 42; k++) {
            JButton button = grid[k];
            button.setText(Integer.toString(dayNum));
            button.setBackground(Color.GRAY);
            dayNum++;
        }
        dayNum = 1;
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

    private void goToPrevMonth() {
        if (month == 1) {
                month = 12;
                year = year-1;
                years.setSelectedItem(year);
            } else {
                month = month - 1;
            }
            months.setSelectedIndex(month-1);
    }

    private void goToNextMonth() {
        if (month == 12) {
                month = 1;
                year = year+1;
                years.setSelectedItem(year);
            } else {
                month = month + 1;
            }
            months.setSelectedIndex(month-1);
    }

    class goToPrevMonth implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            goToPrevMonth();
            update();
        }
    }

    class goToNextMonth implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            goToNextMonth();
            update();
        }
    }

    class goToPrevYear implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            year = year-1;
            years.setSelectedItem(year);
            update();
        }
    }

    class goToNextYear implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            year = year+1;
            years.setSelectedItem(year);
            update();
        }
    }

    class chooseMonth implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            month = months.getSelectedIndex() + 1;
            update();
            //Update day grid and dropdowns
        }
    }

    class chooseYear implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            year = (int)years.getSelectedItem();
            update();
            //Update day grid and dropdowns
        }
    }

    class chooseDay implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            JButton button = (JButton) e.getSource();
            day = Integer.parseInt(button.getText());

            //If user clicks on a day from the previous month, update the month/year so that they're
            // taken to the correct day
            for (int i = 0; i < 7; i++) {
                JButton gridButton = grid[i];
                if (gridButton == button) {
                    if (day <= i+1) {
                        break;
                    }
                    goToPrevMonth();
                }
            }

            //If user clicks on a day from the next month, update the month/year so that they're
            // taken to the correct day
            for (int i = 41; i > 34; i--) {
                JButton gridButton = grid[i];
                if (gridButton == button) {
                    if (day > 15) {
                        break;
                    } 
                    goToNextMonth();
                }
            }

            LocalDate newDate = LocalDate.of(year, month, day);
            if (type == null) { //Calendar used for basic diary entries
                control.chooseDate(newDate);
            }
            else if (type.equals("copy")) { //Calendar used for copying meals
                control.setTempDate(newDate);
            } else if (type.equals("DOB")) { //Calendar used to choose DOB
                control.setUserTempDOB(newDate);
            }
            calendar.dispose();
        }
    }
}