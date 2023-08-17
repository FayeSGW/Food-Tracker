package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

class AddExerciseGUI {
    TrackerControl control;
    LocalDate date;

    JFrame window;
    JPanel whole, inputs, name, time, cals, buttons;
    JLabel instructions, nameLabel, timeLabel, minutesLabel, calsLabel;
    JTextField nameInput, timeInput, calsInput;
    JButton submit;

    AddExerciseGUI(TrackerControl control, LocalDate date) {
        this.control = control;
        this.date = date;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Add Workout");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        whole = new JPanel(new BorderLayout());
        whole.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(whole);

        instructions = new JLabel("Give the name, duration and calories burned of your workout:");
        whole.add(instructions, BorderLayout.NORTH);

        inputs = new JPanel(new BorderLayout()); whole.add(inputs, BorderLayout.CENTER);
        
        name = new JPanel(); inputs.add(name, BorderLayout.NORTH);
        nameLabel = new JLabel("Workout name: "); name.add(nameLabel);
        nameInput = new JTextField(); nameInput.setPreferredSize(new Dimension(150, 26));
        name.add(nameInput);

        time = new JPanel(); inputs.add(time, BorderLayout.CENTER);
        timeLabel = new JLabel("Workout duration: "); time.add(timeLabel);
        timeInput = new JTextField(); timeInput.setPreferredSize(new Dimension(40, 26));
        time.add(timeInput);
        minutesLabel = new JLabel("minutes"); time.add(minutesLabel);

        cals = new JPanel(); inputs.add(cals, BorderLayout.SOUTH);
        calsLabel = new JLabel("Calories burned: "); cals.add(calsLabel);
        calsInput = new JTextField(); calsInput.setPreferredSize(new Dimension(40, 26));
        cals.add(calsInput);

        buttons = new JPanel(); whole.add(buttons, BorderLayout.SOUTH);
        submit = new JButton("Add workout"); submit.addActionListener(new addExercise(date));
        buttons.add(submit);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    class addExercise implements ActionListener {
        LocalDate date;
        addExercise(LocalDate date) {
            this.date = date;
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            try {
                String name = nameInput.getText();
                int time = Integer.valueOf(timeInput.getText());
                int calories = Integer.valueOf(calsInput.getText());
                control.addExercise(date, name, time, calories);
                
            } catch (NumberFormatException n) {
            }
        }
    }
}