package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.time.LocalDate;

class CopyMealGUI {
    TrackerControl control;
    String fromMeal;
    LocalDate fromDate;

    int index;

    JFrame window;
    JPanel whole, choices;
    JLabel label;
    JButton chooseDate, copyButton;
    JComboBox<String> mealChooser;

    String[] mealsList = {"Breakfast", "Lunch", "Dinner", "Snacks"};

    CopyMealGUI(TrackerControl control, String fromMeal, LocalDate fromDate) {
        this.control = control;
        this.fromMeal = fromMeal;
        this.fromDate = fromDate;
        
        for (int i = 0; i < 4; i++) {
            String meal = mealsList[i];
            if (fromMeal.equals(meal)) {
                index = i;
            }
        }

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Choose Meal");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        whole = new JPanel(new BorderLayout());
        window.add(whole);

        label = new JLabel("Choose meal to copy to: "); whole.add(label, BorderLayout.NORTH);

        choices = new JPanel(); whole.add(choices, BorderLayout.CENTER);
        chooseDate = new JButton("Date"); chooseDate.addActionListener(new date());
        choices.add(chooseDate);
        mealChooser = new JComboBox<>(mealsList); mealChooser.setSelectedIndex(index);
        choices.add(mealChooser);

        copyButton = new JButton("Copy"); copyButton.addActionListener(new Copy());
        whole.add(copyButton, BorderLayout.SOUTH);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    class date implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            control.openCalendar("copy");
        }
    }

    class Copy implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            String toMeal = (String)mealChooser.getSelectedItem();
            control.copyMeal(fromMeal, toMeal);
        }
    }

}