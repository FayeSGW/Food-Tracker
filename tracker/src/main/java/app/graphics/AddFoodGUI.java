package app.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import exceptions.ExHandling;
import exceptions.NoNegativeException;
import exceptions.NoNullException;

import java.util.ArrayList;

class AddFoodGUI {
    AddFoodControl control;
    int index;
    String type, editedName; //editedName used to store the new name of items when edited, in order to update the list model 

    JFrame window;
    JPanel whole, searchPanel, listPanel, foodButtons, amountPanel, spare, space;
    JScrollPane listScroll;
    JButton searchButton, showDetailsButton, addButton, finished, editButton, deleteButton;
    JComboBox<String> mealChooser;
    DefaultListModel<String> model;
    JList<String> foodsList;
    JTextField searchBar, amountInput;
    JLabel mealLabel, amountLabel, unitLabel, addedLabel;
    JCheckBox checkFoods, checkRecipes;

    String[] mealsList = {"Breakfast", "Lunch", "Dinner", "Snacks"};
    ArrayList<String> searchResult;

    AddFoodGUI (AddFoodControl control, int index, String type) {
        this.control = control;
        searchResult = new ArrayList<>();
        this.index = index;
        this.type = type;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Add to Diary");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        whole = new JPanel(new BorderLayout());
        window.add(whole);

        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); whole.add(searchPanel, BorderLayout.NORTH);
        searchBar = new JTextField("Search for food or recipe"); searchBar.setPreferredSize(new Dimension(219, 26));
        searchBar.addFocusListener(new focusSearchBar());
        /*searchBar.addMouseListener(new MouseAdapter() {
            @Override //This makes it do that the "Search for..." text disappears when the user clicks on the search bar so they don't have to manually delete it
            public void mouseClicked(MouseEvent e) {
                if (searchBar.getText().equals("Search for food or recipe")) {
                    searchBar.setText("");
                }
            }
        });*/
        searchBar.addActionListener(new search());
        
        searchButton = new JButton("Search"); searchButton.setPreferredSize(new Dimension(75, 26));
        searchButton.addActionListener(new search());
        searchPanel.add(searchBar); /*searchPanel.add(Box.createRigidArea(new Dimension(4,0)));*/ searchPanel.add(searchButton);
        checkFoods = new JCheckBox("Show food items", true); checkRecipes = new JCheckBox("Show Recipes", true);
        searchPanel.add(checkFoods); searchPanel.add(checkRecipes);
        
        //Creates the scrollable panel for the list of items returned by the search
        listPanel = new JPanel(); whole.add(listPanel, BorderLayout.WEST);
        model = new DefaultListModel<>();
        foodsList = new JList<>(model); foodsList.setSize(300, 300); foodsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        foodsList.addListSelectionListener(new selectItem());
        listScroll = new JScrollPane(foodsList); listScroll.setPreferredSize(new Dimension(300, 225));
        listPanel.add(listScroll);

        foodButtons = new JPanel(); foodButtons.setLayout(new BoxLayout(foodButtons, BoxLayout.PAGE_AXIS)); 
        foodButtons.setBorder(new EmptyBorder(5, 0, 5, 5)); whole.add(foodButtons, BorderLayout.EAST);

        showDetailsButton = new JButton("Details"); showDetailsButton.setMaximumSize(new Dimension(75, 26));
        showDetailsButton.setAlignmentX(Component.LEFT_ALIGNMENT); foodButtons.add(showDetailsButton);

        editButton = new JButton("Edit"); editButton.setMaximumSize(new Dimension(75, 26));
        editButton.setAlignmentX(Component.LEFT_ALIGNMENT); editButton.addActionListener(new editItem()); 
        foodButtons.add(editButton);
        foodButtons.add(Box.createRigidArea(new Dimension(0,10)));

        //If window opened for the purpose of adding a new food to the diary, we need to be able to select the meal to add the food to
        if (type.equals("diary")) {
            mealLabel = new JLabel("Choose meal:"); mealLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            foodButtons.add(mealLabel);

            mealChooser = new JComboBox<>(mealsList); mealChooser.setMaximumSize(new Dimension(100, 30));
            mealChooser.setSelectedIndex(index);
            mealChooser.setAlignmentX(Component.LEFT_ALIGNMENT); foodButtons.add(mealChooser);
            foodButtons.add(Box.createRigidArea(new Dimension(0,10))); 
        }

        //If the window NOT opened with the purpose of editing an existing food (i.e. is for adding food to the diary, or adding an ingredient to a recipe)
        // We need to able to choose the amount of food to add and show the unit of the selected item
        if (!type.equals("edit")) {
            amountLabel = new JLabel("Amount to add:"); amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            foodButtons.add(amountLabel);
            amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); amountPanel.setPreferredSize(new Dimension(150, 30));
            amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT); foodButtons.add(amountPanel);
            amountInput = new JTextField(); amountInput.setPreferredSize(new Dimension(50,26)); amountInput.addActionListener(new AddFood());
            amountPanel.add(amountInput); 
            unitLabel = new JLabel(); amountPanel.add(unitLabel);
            
            addButton = new JButton("Add"); 
            addButton.setAlignmentX(Component.LEFT_ALIGNMENT); addButton.addActionListener(new AddFood());
            foodButtons.add(addButton); foodButtons.add(Box.createGlue()); 
        } else { //If the window opened for the purpose of editing an item, we want to have the option to delete it from the database
            deleteButton = new JButton("Delete"); deleteButton.setMaximumSize(new Dimension(75, 26));
            deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT); deleteButton.addActionListener(new deleteItem());
            foodButtons.add(deleteButton);
        }
        finished = new JButton("Done"); 
        finished.setAlignmentX(Component.LEFT_ALIGNMENT); finished.setMaximumSize(new Dimension(75, 26));
        finished.addActionListener(new Exit());
        foodButtons.add(finished);
        
        window.pack();
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    class focusSearchBar implements FocusListener {
        @Override
        public void focusGained(FocusEvent g) {
            if (searchBar.getText().equals("Search for food or recipe")) {
                searchBar.setText("");
            }
            foodsList.clearSelection();
        }

        @Override
        public void focusLost(FocusEvent l) {
            if (searchBar.getText().equals("")) {
                searchBar.setText("Search for food or recipe");
            }
        }
    }

    ArrayList<String> getSearchResults() {
        String text = searchBar.getText();
        if (text.equals("Search for food or recipe")) {
            text = "";
        }

        ArrayList<String> results = null;
        if (checkFoods.isSelected() && checkRecipes.isSelected()) {
            results  = control.searchDatabase(text, "all");
        } else if (checkFoods.isSelected()) {
            results = control.searchDatabase(text, "food");
        } else if (checkRecipes.isSelected()) {
            results = control.searchDatabase(text, "recipe");
        } else {
            results = new ArrayList<>();
        }
        return results;
    }

    void updateResults() {
        model.clear();
        ArrayList<String> results = getSearchResults();
        model.addAll(results);
    }

    class search implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            try {
                model.clear();
                ArrayList<String> results = getSearchResults();
                if (results.size() == 0) {
                    model.addElement("Nothing found!");
                } else {
                    model.addAll(results);
                }
            } catch (NullPointerException n) {
                System.out.println("Oops" + n.getMessage());
            }
        }
    }

    class selectItem implements ListSelectionListener {
        @Override
        public void valueChanged (ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                String name = foodsList.getSelectedValue();
                //System.out.println(name);
                if (!type.equals("edit") && !(name == null)) {
                    String unit = control.showUnit(name);
                    unitLabel.setText(unit);
                    amountInput.requestFocusInWindow();
                }
            }
        }
    }

    class GetDetails implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            int choice = foodsList.getSelectedIndex();
        }
    }

    class editItem implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            String name = foodsList.getSelectedValue();
            control.editItem(name);
        }
    }

    class deleteItem implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            String name = foodsList.getSelectedValue();
            int index = foodsList.getSelectedIndex();
            control.deleteFromDatabase(name);
            model.remove(index);
        }
    }

    class AddFood implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            try {
                double amount = ExHandling.checkDoublesIncNullCheck("Amount", amountInput.getText());
                String itemName = foodsList.getSelectedValue();
                if (type.equals("diary")) {
                    String meal = (String)mealChooser.getSelectedItem();
                    control.addFoodToDiary(meal, itemName, amount);
                } else {
                    control.addFoodToRecipe(itemName, amount);
                }
                amountInput.setText("");
                searchBar.requestFocusInWindow();
            } catch (NumberFormatException | NoNegativeException | NoNullException n) {}
        }
    }

    class Exit implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            window.dispose();
        }
    }
}