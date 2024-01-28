package app.graphics;

import java.awt.event.*;
import javax.swing.*;

import exceptions.*;

class UserGUI extends JPanel {
    UserControl uControl;
    TrackerControl tControl;

    JPanel whole, headerPanel, namePanel, genderPanel, dobPanel, heightPanel, weightPanel,
        goalPanel, ratePanel, waterPanel, waistPanel, hipsPanel, calfPanel, thighPanel,
        upperArmPanel, chestPanel, underwirePanel, bodyFatPanel, footerPanel;
    JLabel headerLabel, nameLabel, genderLabel, dobLabel, dobLabel2, heightLabel, 
        heightLabel2, weightLabel, weightLabel2, goalLabel, goalLabel2, rateLabel, 
        waterLabel, waterLabel2, waistLabel1, waistLabel2, hipsLabel1, hipsLabel2, calfLabel1,
        calfLabel2, thighLabel1, thighLabel2, upperArmLabel1, upperArmLabel2, chestLabel1,
        chestLabel2, underwireLabel1, underwireLabel2, bodyFatLabel1, bodyFatLabel2, messageLabel;
    JButton saveButton, dobButton;
    JTextField nameField, heightField, weightField, rateField, waterField, waistField, hipsField,
        calfField, thighField, upperArmField, chestField, underwireField, bodyFatField;
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

        waistPanel = new JPanel(); whole.add(waistPanel);
        waistLabel1 = new JLabel("Waist: ");
        waistField = new JTextField(Double.toString(uControl.userMeasurement("Waist")));
        waistLabel2 = new JLabel("cm.");
        waistPanel.add(waistLabel1); waistPanel.add(waistField); waistPanel.add(waistLabel2);

        hipsPanel = new JPanel(); whole.add(hipsPanel);
        hipsLabel1 = new JLabel("Hips: ");
        hipsField = new JTextField(Double.toString(uControl.userMeasurement("Hips")));
        hipsLabel2 = new JLabel("cm.");
        hipsPanel.add(hipsLabel1); hipsPanel.add(hipsField); hipsPanel.add(hipsLabel2);

        calfPanel = new JPanel(); whole.add(calfPanel);
        calfLabel1 = new JLabel("Calf: ");
        calfField = new JTextField(Double.toString(uControl.userMeasurement("Calf")));
        calfLabel2 = new JLabel("cm.");
        calfPanel.add(calfLabel1); calfPanel.add(calfField); calfPanel.add(calfLabel2);

        thighPanel = new JPanel(); whole.add(thighPanel);
        thighLabel1 = new JLabel("Thigh: ");
        thighField = new JTextField(Double.toString(uControl.userMeasurement("Thigh")));
        thighLabel2 = new JLabel("cm.");
        thighPanel.add(thighLabel1); thighPanel.add(thighField); thighPanel.add(thighLabel2);

        upperArmPanel = new JPanel(); whole.add(upperArmPanel);
        upperArmLabel1 = new JLabel("Upper Arm: ");
        upperArmField = new JTextField(Double.toString(uControl.userMeasurement("Upper Arm")));
        upperArmLabel2 = new JLabel("cm.");
        upperArmPanel.add(upperArmLabel1); upperArmPanel.add(upperArmField); upperArmPanel.add(upperArmLabel2);

        chestPanel = new JPanel(); whole.add(chestPanel);
        chestLabel1 = new JLabel("Chest: ");
        chestField = new JTextField(Double.toString(uControl.userMeasurement("Chest")));
        chestLabel2 = new JLabel("cm.");
        chestPanel.add(chestLabel1); chestPanel.add(chestField); chestPanel.add(chestLabel2);

        if (uControl.userGender().equals("Female")) {
            underwirePanel = new JPanel(); whole.add(underwirePanel);
            underwireLabel1 = new JLabel("Underwire: ");
            underwireField = new JTextField(Double.toString(uControl.userMeasurement("Underwire")));
            underwireLabel2 = new JLabel("cm.");
            underwirePanel.add(underwireLabel1); underwirePanel.add(underwireField); underwirePanel.add(underwireLabel2);
        }
        
        bodyFatPanel = new JPanel(); whole.add(bodyFatPanel);
        bodyFatLabel1 = new JLabel("Body Fat: ");
        bodyFatField = new JTextField(Double.toString(uControl.userMeasurement("Body Fat")));
        bodyFatLabel2 = new JLabel("%");
        bodyFatPanel.add(bodyFatLabel1); bodyFatPanel.add(bodyFatField); bodyFatPanel.add(bodyFatLabel2);

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
                String[] measurements = {"Waist", "Hips", "Calf", "Thigh", "Upper Arm", "Chest", "Underwire", "Body Fat"};
                JTextField[] texts = {waistField, hipsField, calfField, thighField, upperArmField, chestField, underwireField, bodyFatField};
                double[] newMeasurements = {0, 0, 0, 0, 0, 0, 0, 0};

                String oldName = uControl.userName();
                String newName = nameField.getText().trim();
                if (ifNullDontUpdate(newName)) {
                    newName = uControl.userName();
                }

                String newGender = (String)genderChooser.getSelectedItem();
                String newDOB = dobLabel2.getText();

                String newHeightText = heightField.getText();
                int newHeight = uControl.userHeight();
                if (!ifNullDontUpdate(newHeightText)) {
                    newHeight = ExHandling.checkInts("Height", newHeightText);
                } 

                String newWeightText = weightField.getText();
                double newWeight = uControl.userWeight();
                if (!ifNullDontUpdate(newWeightText)) {
                    newWeight = ExHandling.checkDoubles("Weight", newWeightText);
                } 

                String newGoal = (String)goalChooser.getSelectedItem();

                String newRateText = rateField.getText();
                double newRate = uControl.userRate();
                if (!ifNullDontUpdate(newRateText)) {
                    newRate = ExHandling.checkDoubles("Rate of weight change", newRateText);
                } 

                String newWaterText = waterField.getText();
                int newWater = uControl.userWater();
                if (!ifNullDontUpdate(newWaterText)) {
                    newWater = ExHandling.checkInts("Weight", newWaterText);
                } 

                for (int i = 0; i < measurements.length; i++) {
                    String newText = texts[i].getText();
                    newMeasurements[i] = uControl.userMeasurement(measurements[i]);
                    if (!ifNullDontUpdate(newText)) {
                        newMeasurements[i] = ExHandling.checkDoubles(measurements[i] + " Measurement", newText);
                    }
                }

                /*String newBodyFatText = bodyFatField.getText();
                double newBodyFat = uControl.getBodyFat();
                if (!ifNullDontUpdate(newBodyFatText)) {
                    newBodyFat = ExHandling.checkDoubles("Body Fat Percentage", newBodyFatText);
                } */
                
                uControl.updateUserParameters(oldName, newName, newGender, newWeight, newHeight, newDOB, newGoal, newRate, newWater, 
                    newMeasurements);
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