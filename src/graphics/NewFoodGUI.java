package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class NewFoodGUI {
    ChangeDatabaseControl control;
    String foodName;

    JFrame window;
    JPanel whole, titlePanel, namePanel, displayNamePanel, amountPanel, caloriesPanel, fatPanel, satfatPanel, carbsPanel, sugarPanel, fibrePanel, proteinPanel, saltPanel, barcodePanel, buttonPanel, addedPanel;
    JTextField nameField, displayNameField, amountField, unitField, caloriesField, fatField, satfatField, carbsField, sugarField, fibreField, proteinField, saltField, barcodeField;
    JLabel titleLabel, nameLabel, displayNameLabel, amountLabel, caloriesLabel, fatLabel, satfatLabel, carbsLabel, sugarLabel, fibreLabel, proteinLabel, saltLabel, barcodeLabel, addedLabel;
    JButton button, doneButton;

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
        displayNameLabel = new JLabel("Display Name of Food (optional): "); displayNamePanel.add(displayNameLabel);
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

        barcodePanel = new JPanel(); whole.add(barcodePanel);
        barcodeLabel = new JLabel("Barcode (optional): "); barcodePanel.add(barcodeLabel);
        barcodeField = new JTextField(); barcodeField.setPreferredSize(new Dimension(100,26)); barcodePanel.add(barcodeField);

        buttonPanel = new JPanel(); whole.add(buttonPanel);
        button = new JButton("Add"); button.addActionListener(new save());
        doneButton = new JButton("Done"); doneButton.addActionListener(new finished());
        buttonPanel.add(button); buttonPanel.add(doneButton);

        addedPanel = new JPanel(); whole.add(addedPanel);
        addedLabel = new JLabel(); addedPanel.add(addedLabel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    void existingFoodData(String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        button.setText("Save");
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
        barcodeField.setText(barcode);
    }

    class save implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            try {

                String newName = nameField.getText().trim();
                if (newName == null || newName.equals("")) {
                    throw new NumberFormatException();
                }

                String displayName = displayNameField.getText().trim();
                if (displayName == null || displayName.equals("")) {
                    displayName = newName;
                }
                double amount =  Double.parseDouble(amountField.getText().trim());
                String unit = unitField.getText().trim();
                if (unit == null || unit.equals("")) {
                    throw new NumberFormatException();
                }
                double calories = Double.parseDouble(caloriesField.getText().trim());
                double fat = Double.parseDouble(fatField.getText().trim());
                double satfat = Double.parseDouble(satfatField.getText().trim());
                double carbs = Double.parseDouble(carbsField.getText().trim());
                double sugar = Double.parseDouble(sugarField.getText().trim());
                double fibre = Double.parseDouble(fibreField.getText().trim());
                double protein = Double.parseDouble(proteinField.getText().trim());
                double salt = Double.parseDouble(saltField.getText().trim());
                String barcode = barcodeField.getText().trim();

                
                if (barcode.equals("") || barcode == null) {
                    barcode = null;
                } 
                
                if (foodName != null) {
                    String oldName = foodName;
                    control.saveEdited(oldName, newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                } else {
                    control.saveNewFood(newName, displayName, amount, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                }

                String confirmation = displayName + " added successfully!";
                addedLabel.setText(confirmation);

                nameField.setText("");
                displayNameField.setText("");
                amountField.setText("");
                unitField.setText("");
                caloriesField.setText("");
                fatField.setText("");
                satfatField.setText("");
                carbsField.setText("");
                sugarField.setText("");
                fibreField.setText("");
                proteinField.setText("");
                saltField.setText("");
                barcodeField.setText("");

            } catch (NumberFormatException n) {
                addedLabel.setText("Make sure no non-optional fields are empty, and decimal points are '.' not ',");
            }
        }
    }

    class finished implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            window.dispose();
        }
    }
    


}