package src.graphics;

import java.awt.*;
import java.awt.event.*;

import javax.sound.midi.Track;
import javax.swing.*;

import java.util.HashMap;

class NewRecipeGUI {
    ChangeDatabaseControl control;
    TrackerControl tControl;
    String recipeName;

    JFrame window;
    JPanel whole, titlePanel, namePanel, amountPanel, ingredientsPanel, buttonsPanel, donePanel;
    JPanel totalNutritionPanel, perServingNutritionPanel;
    JTextField nameField, amountField;
    JLabel titleLabel, nameLabel, amountLabel, servingsLabel, ingredientsLabel;
    JButton createRecipeButton, addIngredientButton, deleteButton, doneButton;

    NewRecipeGUI (ChangeDatabaseControl control, TrackerControl tControl) {
        this.control = control;
        this.tControl = tControl;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Add/Edit Recipe");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        whole = new JPanel(); whole.setLayout(new BoxLayout(whole, BoxLayout.Y_AXIS));
        window.add(whole);

        titlePanel = new JPanel(); whole.add(titlePanel);
        titleLabel = new JLabel("Add new recipe/edit recipe in database"); titlePanel.add(titleLabel);

        namePanel = new JPanel(); whole.add(namePanel);
        nameLabel = new JLabel("Name of Recipe: "); namePanel.add(nameLabel);
        nameField = new JTextField(); nameField.setPreferredSize(new Dimension(150,26)); namePanel.add(nameField);

        amountPanel = new JPanel(); whole.add(amountPanel);
        amountLabel = new JLabel("No. servings: "); amountPanel.add(amountLabel);
        amountField = new JTextField(); amountField.setPreferredSize(new Dimension(50,26)); amountPanel.add(amountField);
        servingsLabel = new JLabel("servings"); amountPanel.add(servingsLabel);
        createRecipeButton = new JButton("Create Recipe"); createRecipeButton.addActionListener(new create());
        amountPanel.add(createRecipeButton);

        ingredientsPanel = new JPanel(); ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS)); whole.add(ingredientsPanel);
        ingredientsLabel = new JLabel("Ingredients in recipe:"); ingredientsPanel.add(ingredientsLabel);

        buttonsPanel = new JPanel(); whole.add(buttonsPanel);
        addIngredientButton = new JButton("Add Ingredient"); addIngredientButton.setEnabled(false); 
        addIngredientButton.addActionListener(new addIngredient()); buttonsPanel.add(addIngredientButton);
        deleteButton = new JButton("Delete"); deleteButton.setEnabled(false);
        deleteButton.addActionListener(new deleteRecipe()); buttonsPanel.add(deleteButton);
        donePanel = new JPanel(); whole.add(donePanel);
        doneButton = new JButton("Done"); doneButton.setEnabled(false); 
        doneButton.addActionListener(new finished()); donePanel.add(doneButton);


        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    void setRecipeName(String name) {
        recipeName = name;
        createRecipeButton.setEnabled(false);
        addIngredientButton.setEnabled(true);
        deleteButton.setEnabled(true);
        doneButton.setEnabled(true);
    }

    void addIngredient() {
        tControl.addFoodDialogue(0, "recipe");
        
    }

    void updateIngredientsPanel() {
        clearIngredientsPanel();
        HashMap<String, Double> list = control.showIngredientsInRecipe();
        for (String ingredient: list.keySet()) {
            //JPanel ingPanel = new JPanel(); ingredientsPanel.add(ingPanel);

            JLabel name = new JLabel(String.format("%s %.0f", ingredient, list.get(ingredient)));
            ingredientsPanel.add(name);
        }
        ingredientsPanel.repaint();
    }

    void clearIngredientsPanel() {
        Component[] panels = ingredientsPanel.getComponents();
        for (Component panel: panels) {
            ingredientsPanel.remove(panel);
        }
        ingredientsPanel.repaint();
    }

    void existingRecipeData(String name, double servings, HashMap<String, Double> ingredients) {
        nameField.setText(name);
        amountField.setText(Double.toString(servings));
        updateIngredientsPanel();
    }

    class create implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            String name = nameField.getText();
            int servings;
            if (amountField.getText() == null || amountField.getText().equals("")) {
                servings = 1;
            } else {
                servings = Integer.parseInt(amountField.getText());
            }

            if (name != null) {
                System.out.println(name);
                control.saveNewRecipe(name, servings);
                control.recipe(name);
                setRecipeName(name);
            }

            //addIngredientButton.setEnabled(true);
            //saveButton.setEnabled(true);
        }
    }

    class addIngredient implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            tControl.addFoodDialogue(0, "recipe");
        }
    }

    class deleteRecipe implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            control.delete(recipeName);
            window.dispose();
        }
    }

    class finished implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            window.dispose();
        }
    }
}