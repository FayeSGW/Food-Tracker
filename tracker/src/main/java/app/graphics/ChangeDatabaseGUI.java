package app.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ChangeDatabaseGUI extends JPanel {
    //TrackerControl tControl;
    ChangeDatabaseControl dbControl;
    
    JPanel whole, addButtons, editButtons;
    JButton addFood, addRecipe, edit;

    ChangeDatabaseGUI (ChangeDatabaseControl dbControl) {
        //this.tControl = tControl;
        //dbControl = new ChangeDatabaseControl(tControl);
        this.dbControl = dbControl;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        whole = new JPanel(); whole.setLayout(new BoxLayout(whole, BoxLayout.Y_AXIS)); this.add(whole);
        addButtons = new JPanel(); whole.add(addButtons);
        editButtons = new JPanel(); whole.add(editButtons);

        addFood = new JButton("Add new food item"); addFood.addActionListener(new addFood());
        addRecipe = new JButton("Add new recipe"); addRecipe.addActionListener(new addRecipe());
        addButtons.add(addFood); addButtons.add(addRecipe);

        edit = new JButton("Edit existing entry"); edit.addActionListener(new editItem());
        editButtons.add(edit); 
    }


    class addFood implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            dbControl.newFoodGUI();
        }
    }

    class addRecipe implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            dbControl.newRecipeGUI();
        }
    }

    class editItem implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            dbControl.edit();
        }
    }
}