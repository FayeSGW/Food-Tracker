package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

class AddExerciseGUI {
    TrackerControl control;
    LocalDate date;
    String type, oldName;

    JFrame window;
    JPanel whole, inputs, name, time, cals, buttons;
    JLabel instructions, nameLabel, timeLabel, minutesLabel, secondsLabel, calsLabel;
    JTextField nameInput, minsInput, secsInput, calsInput;
    JButton submit, delete;

    AddExerciseGUI(TrackerControl control, LocalDate date, String type) {
        this.control = control;
        this.date = date;
        this.type = type;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Add/edit Workout");
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
        minsInput = new JTextField(); minsInput.setPreferredSize(new Dimension(40, 26));
        time.add(minsInput);
        minutesLabel = new JLabel("minutes"); time.add(minutesLabel);

        secsInput = new JTextField(); secsInput.setPreferredSize(new Dimension(40, 26));
        time.add(secsInput);
        secondsLabel = new JLabel("seconds"); time.add(secondsLabel);

        cals = new JPanel(); inputs.add(cals, BorderLayout.SOUTH);
        calsLabel = new JLabel("Calories burned: "); cals.add(calsLabel);
        calsInput = new JTextField(); calsInput.setPreferredSize(new Dimension(40, 26));
        cals.add(calsInput);

        buttons = new JPanel(); whole.add(buttons, BorderLayout.SOUTH);
        submit = new JButton("Add workout"); submit.addActionListener(new addExercise(date));
        buttons.add(submit);

        if (type.equals("edit")) {
            delete = new JButton("Delete workout"); delete.addActionListener(new removeExercise(date));
            buttons.add(delete);
        }

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    void existingData(String name, String time, int cals) {
        nameInput.setText(name);
        oldName = name;
        String[] timeSplit = time.split(":");
        minsInput.setText(timeSplit[0]);
        secsInput.setText(timeSplit[1]);
        calsInput.setText(Integer.toString(cals));
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
                int mins = Integer.valueOf(minsInput.getText());
                int secs = Integer.valueOf(secsInput.getText());

                if (secs > 59) {
                    int addMins = secs/60;
                    int newSecs = secs%60;
                    mins += addMins;
                    secs = newSecs;
                }

                int calories = Integer.valueOf(calsInput.getText());
                if (type.equals("add")) {
                    control.addExercise(date, name, mins, secs, calories);
                } else {
                    control.editExercise(date, oldName, name, mins, secs, calories);
                    
                }
                control.updateNutrition();
                window.dispose();
                
                
            } catch (NumberFormatException n) {
                
            }
        }
    }

    class removeExercise implements ActionListener {
        LocalDate date;
        removeExercise(LocalDate date) {
            this.date = date;
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            String name = nameInput.getText();
            control.removeExercise(date, name);
            window.dispose();
        }
    }
}