package app.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import exceptions.*;

class UserGUI extends JPanel {
    UserControl uControl;
    TrackerControl tControl;

    JPanel whole, headerPanel, leftPanel, centerPanel, rightPanel, namePanel, genderPanel, dobPanel, heightPanel, weightPanel,
        goalPanel, ratePanel, waterPanel, waistPanel, hipsPanel, calfPanel, thighPanel,
        upperArmPanel, chestPanel, underwirePanel, bodyFatPanel, footerPanel;
    JLabel headerLabel, nameLabel, genderLabel, dobLabel, dobLabel2, heightLabel, 
        heightLabel2, weightLabel, weightLabel2, goalLabel, goalLabel2, rateLabel, 
        waterLabel, waterLabel2, waistLabel1, waistLabel2, hipsLabel1, hipsLabel2, calfLabel1,
        calfLabel2, thighLabel1, thighLabel2, upperArmLabel1, upperArmLabel2, chestLabel1,
        chestLabel2, underwireLabel1, underwireLabel2, bodyFatLabel1, bodyFatLabel2, messageLabel, space;
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

        whole = new JPanel(new BorderLayout()); this.add(whole);
        headerPanel = new JPanel(); whole.add(headerPanel, BorderLayout.NORTH);
        headerLabel = new JLabel("Hi " + uControl.getUserName() + "!"); headerLabel.setFont(new Font(headerLabel.getFont().toString(), Font.BOLD, 15));
        headerPanel.add(headerLabel);
        
        leftPanel = new JPanel(); leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS)); 
        rightPanel = new JPanel(); rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS)); 
        whole.add(leftPanel, BorderLayout.WEST); whole.add(rightPanel, BorderLayout.EAST);

        centerPanel = new JPanel(); whole.add(centerPanel, BorderLayout.CENTER);
        space = new JLabel(); space.setPreferredSize(new Dimension(2,0)); centerPanel.add(space);

        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); leftPanel.add(namePanel);
        nameLabel = new JLabel("Name: "); nameLabel.setPreferredSize(new Dimension(80, 23)); 
        nameField = new JTextField(uControl.getUserName()); nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setPreferredSize(new Dimension(100, 23));
        namePanel.add(nameLabel); namePanel.add(nameField);

        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //genderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(genderPanel);
        genderLabel = new JLabel("Gender: "); genderLabel.setPreferredSize(new Dimension(80, 23)); 
        genderChooser = new JComboBox<>(genderList); genderChooser.setSelectedItem(uControl.getUserGender());
        genderPanel.add(genderLabel); genderPanel.add(genderChooser);

        dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //dobPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(dobPanel);
        dobLabel = new JLabel("Date of Birth: "); dobLabel.setPreferredSize(new Dimension(80, 23)); 
        dobLabel2 = new JLabel(uControl.getUserDOB());
        dobButton = new JButton("Change"); dobButton.addActionListener(new calendarDOB());
        dobPanel.add(dobLabel); dobPanel.add(dobLabel2); dobPanel.add(dobButton);

        heightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //heightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(heightPanel);
        heightLabel = new JLabel("Height: "); heightLabel.setPreferredSize(new Dimension(80, 23)); 
        heightField = new JTextField(Integer.toString(uControl.getUserHeight())); heightField.setHorizontalAlignment(JTextField.CENTER);
        heightField.setPreferredSize(new Dimension(35, 23));
        heightLabel2 = new JLabel(" cm");
        heightPanel.add(heightLabel); heightPanel.add(heightField); heightPanel.add(heightLabel2);

        weightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(weightPanel);
        weightLabel = new JLabel("Weight: "); weightLabel.setPreferredSize(new Dimension(75, 23));
        weightField = new JTextField(Double.toString(uControl.getUserWeight())); weightField.setHorizontalAlignment(JTextField.CENTER);
        weightField.setPreferredSize(new Dimension(35, 23));
        weightLabel2 = new JLabel(" kg");
        weightPanel.add(weightLabel); weightPanel.add(weightField); weightPanel.add(weightLabel2);

        goalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); goalPanel.setPreferredSize(new Dimension(290, 30)); 
        leftPanel.add(goalPanel);
        goalLabel = new JLabel("Goal: "); goalLabel.setPreferredSize(new Dimension(80, 23)); 
        goalChooser = new JComboBox<>(goalList); goalChooser.setSelectedItem(uControl.getUserGoal()); goalChooser.addItemListener(new updateGoal());
        goalPanel.add(goalLabel); goalPanel.add(goalChooser);
        changeGoal(uControl.getUserGoal());

        waterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); leftPanel.add(waterPanel);   
        waterLabel = new JLabel("Water Goal: "); waterLabel.setPreferredSize(new Dimension(80, 23)); 
        waterField = new JTextField(Integer.toString(uControl.getUserWaterGoal())); waterField.setHorizontalAlignment(JTextField.CENTER);
        waterField.setPreferredSize(new Dimension(35, 23));
        waterLabel2 = new JLabel(" glasses per day");
        waterPanel.add(waterLabel); waterPanel.add(waterField); waterPanel.add(waterLabel2);

        waistPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(waistPanel);
        waistLabel1 = new JLabel("Waist: "); waistLabel1.setPreferredSize(new Dimension(75, 23));
        waistField = new JTextField(Double.toString(uControl.userMeasurement("Waist")));
        waistField.setPreferredSize(new Dimension(35, 23)); waistField.setHorizontalAlignment(JTextField.CENTER);
        waistLabel2 = new JLabel("cm");
        waistPanel.add(waistLabel1); waistPanel.add(waistField); waistPanel.add(waistLabel2);

        hipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(hipsPanel);
        hipsLabel1 = new JLabel("Hips: "); hipsLabel1.setPreferredSize(new Dimension(75, 23));
        hipsField = new JTextField(Double.toString(uControl.userMeasurement("Hips")));
        hipsField.setPreferredSize(new Dimension(35, 23)); hipsField.setHorizontalAlignment(JTextField.CENTER);
        hipsLabel2 = new JLabel("cm");
        hipsPanel.add(hipsLabel1); hipsPanel.add(hipsField); hipsPanel.add(hipsLabel2);

        calfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(calfPanel);
        calfLabel1 = new JLabel("Calf: "); calfLabel1.setPreferredSize(new Dimension(75, 23));
        calfField = new JTextField(Double.toString(uControl.userMeasurement("Calf")));
        calfField.setPreferredSize(new Dimension(35, 23)); calfField.setHorizontalAlignment(JTextField.CENTER);
        calfLabel2 = new JLabel("cm");
        calfPanel.add(calfLabel1); calfPanel.add(calfField); calfPanel.add(calfLabel2);

        thighPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(thighPanel);
        thighLabel1 = new JLabel("Thigh: "); thighLabel1.setPreferredSize(new Dimension(75, 23));
        thighField = new JTextField(Double.toString(uControl.userMeasurement("Thigh")));
        thighField.setPreferredSize(new Dimension(35, 23)); thighField.setHorizontalAlignment(JTextField.CENTER);
        thighLabel2 = new JLabel("cm");
        thighPanel.add(thighLabel1); thighPanel.add(thighField); thighPanel.add(thighLabel2);

        upperArmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(upperArmPanel);
        upperArmLabel1 = new JLabel("Upper Arm: "); upperArmLabel1.setPreferredSize(new Dimension(75, 23));
        upperArmField = new JTextField(Double.toString(uControl.userMeasurement("Upper Arm")));
        upperArmField.setPreferredSize(new Dimension(35, 23)); upperArmField.setHorizontalAlignment(JTextField.CENTER);
        upperArmLabel2 = new JLabel("cm");
        upperArmPanel.add(upperArmLabel1); upperArmPanel.add(upperArmField); upperArmPanel.add(upperArmLabel2);

        chestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(chestPanel);
        chestLabel1 = new JLabel("Chest: "); chestLabel1.setPreferredSize(new Dimension(75, 23));
        chestField = new JTextField(Double.toString(uControl.userMeasurement("Chest")));
        chestField.setPreferredSize(new Dimension(35, 23)); chestField.setHorizontalAlignment(JTextField.CENTER);
        chestLabel2 = new JLabel("cm");
        chestPanel.add(chestLabel1); chestPanel.add(chestField); chestPanel.add(chestLabel2);

        if (uControl.getUserGender().equals("Female")) {
            underwirePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(underwirePanel);
            underwireLabel1 = new JLabel("Underwire: "); underwireLabel1.setPreferredSize(new Dimension(75, 23));
            underwireField = new JTextField(Double.toString(uControl.userMeasurement("Underwire")));
            underwireField.setPreferredSize(new Dimension(35, 23)); underwireField.setHorizontalAlignment(JTextField.CENTER);
            underwireLabel2 = new JLabel("cm");
            underwirePanel.add(underwireLabel1); underwirePanel.add(underwireField); underwirePanel.add(underwireLabel2);
        }
        
        bodyFatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); rightPanel.add(bodyFatPanel);
        bodyFatLabel1 = new JLabel("Body Fat: "); bodyFatLabel1.setPreferredSize(new Dimension(75, 23));
        bodyFatField = new JTextField(Double.toString(uControl.userMeasurement("Body Fat")));
        bodyFatField.setPreferredSize(new Dimension(35, 23)); bodyFatField.setHorizontalAlignment(JTextField.CENTER);
        bodyFatLabel2 = new JLabel("%");
        bodyFatPanel.add(bodyFatLabel1); bodyFatPanel.add(bodyFatField); bodyFatPanel.add(bodyFatLabel2);

        footerPanel = new JPanel(); footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.PAGE_AXIS));
        whole.add(footerPanel, BorderLayout.SOUTH);
        messageLabel = new JLabel(" "); messageLabel.setAlignmentX(CENTER_ALIGNMENT);
        saveButton = new JButton("Save changes"); saveButton.setAlignmentX(CENTER_ALIGNMENT);
        saveButton.addActionListener(new saveChanges(this));
        footerPanel.add(messageLabel); footerPanel.add(saveButton);
    }

    public void changeDOBLabel(String string) {
        dobLabel2.setText(string);
    }

    public void updateWeight() {
        weightField.setText(Double.toString(uControl.getUserWeight()));
    }

    // Adjust the goalPanel depending on what the Goal is
    public void changeGoal(String goal) {
        if (goal.equals("Maintain")) {
            goalLabel2 = new JLabel("weight.");
        } else {
            rateField = new JTextField(Double.toString(uControl.getUserRate())); rateField.setHorizontalAlignment(JTextField.CENTER);
            rateField.setPreferredSize(new Dimension(35, 23));
            goalPanel.add(rateField);
            goalLabel2 = new JLabel("kg per week.");
        }
        goalPanel.add(goalLabel2);
    }

    class calendarDOB implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            uControl.openCalendar();
        }
    }

    // To update the goal panel if the goal is changed
    class updateGoal implements ItemListener {
        @Override
        public void itemStateChanged (ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (rateField != null) {
                    goalPanel.remove(rateField);
                }
                goalPanel.remove(goalLabel2);
                changeGoal(e.getItem().toString());
                goalPanel.revalidate();
                goalPanel.repaint();
            }
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

                String oldName = uControl.getUserName();
                String newName = nameField.getText().trim();
                if (ifNullDontUpdate(newName)) {
                    newName = oldName;
                }

                String newGender = (String)genderChooser.getSelectedItem();
                String newDOB = dobLabel2.getText();
                String newGoal = (String)goalChooser.getSelectedItem();

                String newHeightText = heightField.getText();
                int newHeight = uControl.getUserHeight();
                if (!ifNullDontUpdate(newHeightText)) {
                    newHeight = ExHandling.checkInts("Height", newHeightText);
                } 

                String newWeightText = weightField.getText();
                double newWeight = uControl.getUserWeight();
                if (!ifNullDontUpdate(newWeightText)) {
                    newWeight = ExHandling.checkDoubles("Weight", newWeightText);
                } 

                // If Goal = Maintain when the app is opened, and not changed, then rateField is never initialised
                // This therefore gives an error for getText()
                String newRateText;
                if (rateField == null) {
                    newRateText = "0";
                } else {
                    newRateText = rateField.getText(); 
                }
                double newRate = uControl.getUserRate();
                if (!ifNullDontUpdate(newRateText)) {
                    newRate = ExHandling.checkDoubles("Rate of weight change", newRateText);
                } 

                String newWaterText = waterField.getText();
                int newWater = uControl.getUserWaterGoal();
                if (!ifNullDontUpdate(newWaterText)) {
                    newWater = ExHandling.checkInts("Weight", newWaterText);
                } 

                //Body measurements (inc body fat %) handled in a loop
                for (int i = 0; i < measurements.length; i++) {
                    String newText = texts[i].getText();
                    newMeasurements[i] = uControl.userMeasurement(measurements[i]);
                    if (!ifNullDontUpdate(newText)) {
                        newMeasurements[i] = ExHandling.checkDoubles(measurements[i] + " Measurement", newText);
                    }
                }
                
                boolean somethingUpdated = uControl.updateUserParameters(oldName, newName, newGender, newWeight, 
                    newHeight, newDOB, newGoal, newRate, newWater, newMeasurements);

                if (somethingUpdated) { 
                    messageLabel.setText("Details updated!");
                } else {
                    messageLabel.setText("Nothing to update!");
                }

                headerLabel.setText("Hi " + uControl.getUserName() + "!");

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