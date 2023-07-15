package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class NewFoodGUI {
    ChangeDatabaseControl control;
    String foodName;

    JFrame window;
    JPanel whole, titlePanel, namePanel, displayNamePanel, amountPanel, caloriesPanel, fatPanel, satfatPanel, carbsPanel, sugarPanel, fibrePanel, proteinPanel, saltPanel, buttonPanel;
    JTextField nameField, displayNameField, amountField, unitField, caloriesField, fatField, satfatField, carbsField, sugarField, fibreField, proteinField, saltField;
    JLabel titleLabel, nameLabel, displayNameLabel, amountLabel, caloriesLabel, fatLabel, satfatLabel, carbsLabel, sugarLabel, fibreLabel, proteinLabel, saltLabel;
    JButton button;

    NewFoodGUI (ChangeDatabaseControl control) {
        this.control = control;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Add/Edit Food");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        whole = new JPanel(); whole.setLayout(new BoxLayout(whole, BoxLayout.Y_AXIS));
        window.add(whole);

        titlePanel = new JPanel(); whole.add(titlePanel);
        titleLabel = new JLabel("Add new food/edit food in database"); titlePanel.add(titleLabel);

        namePanel = new JPanel(); whole.add(namePanel);
        nameLabel = new JLabel("Name of Food: "); namePanel.add(nameLabel);
        nameField = new JTextField(); nameField.setPreferredSize(new Dimension(150,26)); namePanel.add(nameField);

        displayNamePanel = new JPanel(); whole.add(displayNamePanel);
        displayNameLabel = new JLabel("Display Name of Food: "); displayNamePanel.add(displayNameLabel);
        displayNameField = new JTextField(); displayNameField.setPreferredSize(new Dimension(150,26)); displayNamePanel.add(displayNameField);

        amountPanel = new JPanel(); whole.add(amountPanel);
        amountLabel = new JLabel("Amount: "); amountPanel.add(amountLabel);
        amountField = new JTextField(); amountField.setPreferredSize(new Dimension(50,26)); amountPanel.add(amountField);
        unitField = new JTextField(); unitField.setPreferredSize(new Dimension(50,26)); amountPanel.add(unitField);

        caloriesPanel = new JPanel(); whole.add(caloriesPanel);
        caloriesLabel = new JLabel("Calories: "); caloriesPanel.add(caloriesLabel);
        caloriesField = new JTextField(); caloriesField.setPreferredSize(new Dimension(50,26)); caloriesPanel.add(caloriesField);

        fatPanel = new JPanel(); whole.add(fatPanel);
        fatLabel = new JLabel("Fat (g): "); fatPanel.add(fatLabel);
        fatField = new JTextField(); fatField.setPreferredSize(new Dimension(50,26)); fatPanel.add(fatField);

        satfatPanel = new JPanel(); whole.add(satfatPanel);
        satfatLabel = new JLabel("Saturated Fat (g): "); satfatPanel.add(satfatLabel);
        satfatField = new JTextField(); satfatField.setPreferredSize(new Dimension(50,26)); satfatPanel.add(satfatField);

        carbsPanel = new JPanel(); whole.add(carbsPanel);
        carbsLabel = new JLabel("Carbohydrates (g): "); carbsPanel.add(carbsLabel);
        carbsField = new JTextField(); carbsField.setPreferredSize(new Dimension(50,26)); carbsPanel.add(carbsField);

        sugarPanel = new JPanel(); whole.add(sugarPanel);
        sugarLabel = new JLabel("Sugars (g): "); sugarPanel.add(sugarLabel);
        sugarField = new JTextField(); sugarField.setPreferredSize(new Dimension(50,26)); sugarPanel.add(sugarField);

        fibrePanel = new JPanel(); whole.add(fibrePanel);
        fibreLabel = new JLabel("Fibre (g): "); fibrePanel.add(fibreLabel);
        fibreField = new JTextField(); fibreField.setPreferredSize(new Dimension(50,26)); fibrePanel.add(fibreField);

        proteinPanel = new JPanel(); whole.add(proteinPanel);
        proteinLabel = new JLabel("Protein (g): "); proteinPanel.add(proteinLabel);
        proteinField = new JTextField(); proteinField.setPreferredSize(new Dimension(50,26)); proteinPanel.add(proteinField);

        saltPanel = new JPanel(); whole.add(saltPanel);
        saltLabel = new JLabel("Salt (g): "); saltPanel.add(saltLabel);
        saltField = new JTextField(); saltField.setPreferredSize(new Dimension(50,26)); saltPanel.add(saltField);

        buttonPanel = new JPanel(); whole.add(buttonPanel);
        button = new JButton("Add"); button.addActionListener(new save());
        buttonPanel.add(button);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    void existingFoodData(String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        nameField.setText(name);
        this.foodName = name;
        displayNameField.setText(displayName);
        amountField.setText(Double.toString(amount));
        unitField.setText(unit);
        caloriesField.setText(Double.toString(calories));
        fatField.setText(Double.toString(fat));
        satfatField.setText(Double.toString(satfat));
        carbsField.setText(Double.toString(carbs));
        sugarField.setText(Double.toString(sugar));
        fibreField.setText(Double.toString(fibre));
        proteinField.setText(Double.toString(protein));
        saltField.setText(Double.toString(salt));
    }

    class save implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            
            try {
                String newName = nameField.getText();
                String displayName = displayNameField.getText();
                if (displayName == null || displayName.equals("")) {
                    displayName = newName;
                }
                double amount =  Double.parseDouble(amountField.getText());
                String unit = unitField.getText();
                double calories = Double.parseDouble(caloriesField.getText());
                double fat = Double.parseDouble(fatField.getText());
                double satfat = Double.parseDouble(satfatField.getText());
                double carbs = Double.parseDouble(carbsField.getText());
                double sugar = Double.parseDouble(sugarField.getText());
                double fibre = Double.parseDouble(fibreField.getText());
                double protein = Double.parseDouble(proteinField.getText());
                double salt = Double.parseDouble(saltField.getText());
                if (foodName != null) {
                    String oldName = foodName;
                    control.saveEdited(oldName, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, oldName);
                } else {
                    control.saveNewFood(newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, unit);
                }
            } catch (NumberFormatException n) {}          }
        }
    


}