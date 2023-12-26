package app.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class UserGUI extends JPanel {
    UserControl uControl;
    TrackerControl tControl;

    JPanel whole, headerPanel, namePanel, genderPanel, dobPanel, heightPanel, weightPanel,
        goalPanel, ratePanel, waterPanel;
    JLabel headerLabel, nameLabel, genderLabel, dobLabel, dobLabel2, heightLabel, 
        heightLabel2, weightLabel, weightLabel2, goalLabel, rateLabel, waterLabel;
    JButton editButton;

    //For when profile being edited
    JButton saveButton, dobButton;
    JTextField nameField, heightField, weightField, rateField, waterField;
    JComboBox<String> genderChooser, goalChooser;

    String[] genderList = {"Male", "Female"};

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

        ratePanel = new JPanel(); whole.add(ratePanel);

        waterPanel = new JPanel(); whole.add(waterPanel);   
    }

    public void changeDOBLabel(String string) {
        dobLabel2.setText(string);
    }

    class calendarDOB implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            uControl.openCalendar();
        }
    }
    
}