package app.graphics;

import java.awt.event.*;
import javax.swing.*;

import exceptions.*;

class UserGUI extends JPanel {
    UserControl uControl;
    TrackerControl tControl;

    JPanel whole, headerPanel, namePanel, genderPanel, dobPanel, heightPanel, weightPanel,
        goalPanel, ratePanel, waterPanel, footerPanel;
    JLabel headerLabel, nameLabel, genderLabel, dobLabel, dobLabel2, heightLabel, 
        heightLabel2, weightLabel, weightLabel2, goalLabel, goalLabel2, rateLabel, 
        waterLabel, waterLabel2, messageLabel;
    JButton saveButton, dobButton;
    JTextField nameField, heightField, weightField, rateField, waterField;
    JComboBox<String> genderChooser, goalChooser;

    String[] genderList = {"Male", "Female"};
    String[] goalList = {"Lose", "Maintain", "Gain"};

    UserGUI(UserControl uControl, TrackerControl tControl) {
        this.uControl = uControl;
        this.tControl = tControl;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        whole = new JPanel(); whole.setLayout(new BoxLayout(whole, BoxLayout.Y_AXIS)); this.add(whole);
        headerPanel = new JPanel(); whole.add(headerPanel);
        headerLabel = new JLabel("Hi " + uControl.userName() + "!"); headerPanel.add(headerLabel);
        
        namePanel = new JPanel(); whole.add(namePanel);
        nameLabel = new JLabel("Name: "); 
        nameField = new JTextField(uControl.userName());
        namePanel.add(nameLabel); namePanel.add(nameField);

        genderPanel = new JPanel(); whole.add(genderPanel);
        genderLabel = new JLabel("Gender: ");
        genderChooser = new JComboBox<>(genderList); genderChooser.setSelectedItem(uControl.userGender());
        genderPanel.add(genderLabel); genderPanel.add(genderChooser);

        dobPanel = new JPanel(); whole.add(dobPanel);
        dobLabel = new JLabel("Date of Birth: "); dobLabel2 = new JLabel(uControl.userDOB());
        dobButton = new JButton("Change"); dobButton.addActionListener(new calendarDOB());
        dobPanel.add(dobLabel); dobPanel.add(dobLabel2); dobPanel.add(dobButton);

        heightPanel = new JPanel(); whole.add(heightPanel);
        heightLabel = new JLabel("Height: ");
        heightField = new JTextField(Integer.toString(uControl.userHeight()));
        heightLabel2 = new JLabel(" cm");
        heightPanel.add(heightLabel); heightPanel.add(heightField); heightPanel.add(heightLabel2);

        weightPanel = new JPanel(); whole.add(weightPanel);
        weightLabel = new JLabel("Weight: ");
        weightField = new JTextField(Double.toString(uControl.userWeight()));
        weightLabel2 = new JLabel(" kg");
        weightPanel.add(weightLabel); weightPanel.add(weightField); weightPanel.add(weightLabel2);

        goalPanel = new JPanel(); whole.add(goalPanel);
        goalLabel = new JLabel("Goal: ");
        goalChooser = new JComboBox<>(goalList); goalChooser.setSelectedItem(uControl.userGoal());
        goalPanel.add(goalLabel); goalPanel.add(goalChooser);
        
        if (uControl.userGoal().equals("Maintain")) {
            goalLabel2 = new JLabel("weight.");
        } else {
            rateField = new JTextField(Double.toString(uControl.userRate()));
            goalPanel.add(rateField);
            goalLabel2 = new JLabel("kg per week.");
        }
        goalPanel.add(goalLabel2);

        waterPanel = new JPanel(); whole.add(waterPanel);   
        waterLabel = new JLabel("Water Goal: ");
        waterField = new JTextField(Integer.toString(uControl.userWater()));
        waterLabel2 = new JLabel(" glasses per day");
        waterPanel.add(waterLabel); waterPanel.add(waterField); waterPanel.add(waterLabel2);

        footerPanel = new JPanel(); whole.add(footerPanel);
        messageLabel = new JLabel();
        saveButton = new JButton("Save changes"); saveButton.addActionListener(new saveChanges(this));
        footerPanel.add(messageLabel); footerPanel.add(saveButton);
    }

    public void changeDOBLabel(String string) {
        dobLabel2.setText(string);
    }

    public void updateWeight() {
        weightField.setText(Double.toString(uControl.userWeight()));
    }

    class calendarDOB implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            uControl.openCalendar();
        }
    }

    class saveChanges implements ActionListener {
        UserGUI uGUI = null;
        saveChanges(UserGUI uGUI) {
            this.uGUI = uGUI;
        }
        @Override
        public void actionPerformed (ActionEvent e) {
            try {
                String oldName = uControl.userName();
                String newName = nameField.getText().trim();
                if (ifNullDontUpdate(newName)) {
                    newName = uControl.userName();
                }

                String newGender = (String)genderChooser.getSelectedItem();

                String newDOB = dobLabel2.getText();

                String newHeightText = heightField.getText();
                int newHeight = 0;
                if (ifNullDontUpdate(newHeightText)) {
                    newHeight = uControl.userHeight();
                } else {
                    newHeight = ExHandling.checkInts("Height", newHeightText);
                }

                String newWeightText = weightField.getText();
                double newWeight = 0;
                if (ifNullDontUpdate(newWeightText)) {
                    newWeight = uControl.userWeight();
                } else {
                    newWeight = ExHandling.checkDoubles("Weight", newWeightText);
                }

                String newGoal = (String)goalChooser.getSelectedItem();

                String newRateText = rateField.getText();
                double newRate = 0;
                if (ifNullDontUpdate(newRateText)) {
                    newRate = uControl.userRate();
                } else {
                    newRate = ExHandling.checkDoubles("Rate of weight change", newRateText);
                }

                String newWaterText = waterField.getText();
                int newWater = 0;
                if (ifNullDontUpdate(newWaterText)) {
                    newWater = uControl.userWater();
                } else {
                    newWater = ExHandling.checkInts("Weight", newWaterText);
                }

                uControl.updateUserParameters(oldName, newName, newGender, newWeight, newHeight, newDOB, newGoal, newRate, newWater);
                messageLabel.setText("Details updated!");
                headerLabel.setText("Hi " + uControl.userName() + "!");

            } catch (NumberFormatException | NoNegativeException n) {
                messageLabel.setText(n.getMessage());
            }
        }

        private boolean ifNullDontUpdate(String string) {
            // If text box is empty, don't update the value
            if (string == null || string.equals("")) {
                return true;
            }
            return false;
        }
    }
    
}