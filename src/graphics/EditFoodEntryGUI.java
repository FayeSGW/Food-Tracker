package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class EditFoodEntryGUI {
    TrackerControl control;
    String type;
    String mealName, recipeName;

    JFrame window;
    JPanel whole, foodPanel, amountPanel, buttonsPanel;
    JButton saveButton, deleteButton;
    JLabel foodNameLabel, currentAmount, unitLabel;
    JTextField amountField;

    EditFoodEntryGUI(TrackerControl control, String type, String mealName) {
        this.control = control;
        this.type = type;
        this.mealName = mealName;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Edit Entry");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        whole = new JPanel(new BorderLayout()); window.add(whole);

        foodPanel = new JPanel(); whole.add(foodPanel, BorderLayout.NORTH);
        foodNameLabel = new JLabel(); foodPanel.add(foodNameLabel);

        amountPanel = new JPanel(); whole.add(amountPanel, BorderLayout.CENTER);
        currentAmount = new JLabel("Change amount:  "); amountPanel.add(currentAmount);
        amountField = new JTextField(); amountField.setPreferredSize(new Dimension(40, 26));
        amountPanel.add(amountField);
        unitLabel = new JLabel(); amountPanel.add(unitLabel);

        buttonsPanel = new JPanel(); whole.add(buttonsPanel, BorderLayout.SOUTH);
        deleteButton = new JButton("Delete"); deleteButton.addActionListener(new delete());
        buttonsPanel.add(deleteButton);
        saveButton = new JButton("Save"); saveButton.addActionListener(new saveEdits());
        buttonsPanel.add(saveButton);

        window.pack();
        window.setSize(250, 140);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

    void existingData(String name, double amount, String unit) {
        foodNameLabel.setText(name);
        amountField.setText(Double.toString(amount));
        unitLabel.setText(" " + unit);
    }

    class delete implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            control.removeFromMeal(mealName, foodNameLabel.getText());
            control.updateNutrition();
            window.dispose();
        }
    }

    class saveEdits implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            String foodName = foodNameLabel.getText();
            double amount = Double.parseDouble(amountField.getText());
            control.editMeal(mealName, foodName, amount);
            control.updateNutrition();
            window.dispose();
        }
    }
}