package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.ArrayList;

import src.diary.*;
import src.SQL.java.connect.sql.code.*;

class AddFoodTest {
    /*public static void main (String [] args) {
        //User user = new User("Faye", "F", 81, 165, "24.07.1989", "loss", 1);
        Diary diary = new Diary("", user);
        TrackerControl control = new TrackerControl(user, diary);
        Database data = user.accessDatabase();
        GetFoodsDB.getFoods(data);
        //AddFoodGUI gui = new AddFoodGUI();
        AddFoodControl cont = new AddFoodControl(control);

    }*/
}

class AddFoodGUI {
    AddFoodControl control;
    int index;

    JFrame window;
    JPanel whole, searchPanel, listPanel, foodButtons, amountPanel, spare, space;
    JScrollPane listScroll;
    JButton searchButton, showDetailsButton, addButton, finished;
    JComboBox<String> mealChooser;
    DefaultListModel<String> model;
    JList<String> foodsList;
    JTextField searchBar, amountInput;
    JLabel mealLabel, amountLabel, unitLabel, addedLabel;

    String[] mealsList = {"Breakfast", "Lunch", "Dinner", "Snacks"};
    ArrayList<String> searchResult;

    AddFoodGUI (AddFoodControl control, int index) {
        this.control = control;
        searchResult = new ArrayList<>();
        this.index = index;

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
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchBar.getText().equals("Search for food or recipe")) {
                    searchBar.setText("");
                }
            }
        });

        searchBar.addActionListener(new search());
        
        searchButton = new JButton("Search"); searchButton.setPreferredSize(new Dimension(75, 26));
        searchButton.addActionListener(new search());
        searchPanel.add(searchBar); /*searchPanel.add(Box.createRigidArea(new Dimension(4,0)));*/ searchPanel.add(searchButton);
        

        listPanel = new JPanel(); whole.add(listPanel, BorderLayout.WEST);
        model = new DefaultListModel<>();
        foodsList = new JList<>(model); foodsList.setSize(300, 300); foodsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        foodsList.addListSelectionListener(new selectItem());
        listScroll = new JScrollPane(foodsList); listScroll.setPreferredSize(new Dimension(300, 225));
        listPanel.add(listScroll);

        //space = new JPanel(); whole.add(space, BorderLayout.CENTER);

        foodButtons = new JPanel(); foodButtons.setLayout(new BoxLayout(foodButtons, BoxLayout.PAGE_AXIS)); 
        //foodButtons.setAlignmentX(Component.LEFT_ALIGNMENT); //foodButtons.setMaximumSize(new Dimension(150, 200));
        foodButtons.setBorder(new EmptyBorder(5, 0, 5, 5));
        whole.add(foodButtons, BorderLayout.EAST);

        //foodButtons.add(Box.createGlue()); 
        //foodButtons.add(Box.createRigidArea(new Dimension(0, 50)));
        showDetailsButton = new JButton("Details"); showDetailsButton.setMaximumSize(new Dimension(75, 26));
        showDetailsButton.setAlignmentX(Component.LEFT_ALIGNMENT); foodButtons.add(showDetailsButton);
        mealLabel = new JLabel("Choose meal:"); mealLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        foodButtons.add(Box.createRigidArea(new Dimension(0,10))); foodButtons.add(mealLabel);

        mealChooser = new JComboBox<>(mealsList); mealChooser.setMaximumSize(new Dimension(100, 30));
        mealChooser.setSelectedIndex(index);
        mealChooser.setAlignmentX(Component.LEFT_ALIGNMENT); foodButtons.add(mealChooser);
        foodButtons.add(Box.createRigidArea(new Dimension(0,10))); 

        amountLabel = new JLabel("Amount to add:"); amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        foodButtons.add(amountLabel);
        amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); amountPanel.setPreferredSize(new Dimension(150, 30));
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT); foodButtons.add(amountPanel);
        amountInput = new JTextField(); amountInput.setPreferredSize(new Dimension(50,26));
        amountInput.addActionListener(new AddFood());
        amountPanel.add(amountInput); 
        unitLabel = new JLabel(); amountPanel.add(unitLabel);
        //foodButtons.add(Box.createRigidArea(new Dimension(0,10))); 
        
        addButton = new JButton("Add"); //addButton.setMaximumSize(new Dimension(75, 26));
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT); addButton.addActionListener(new AddFood());
        foodButtons.add(addButton);
        foodButtons.add(Box.createGlue()); 
        finished = new JButton("Done"); 
        finished.setAlignmentX(Component.LEFT_ALIGNMENT); finished.setMaximumSize(new Dimension(75, 26));
        finished.addActionListener(new Exit());
        foodButtons.add(finished);
        
        //spare = new JPanel(); foodButtons.add(spare);
        

        window.pack();
        window.setResizable(false);
        //window.setPreferredSize(new Dimension(350,));
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        System.out.println(window.getHeight());
    }

    class focusSearchBar implements FocusListener {
        @Override
        public void focusGained(FocusEvent g) {
            if (searchBar.getText().equals("Search for food or recipe")) {
                searchBar.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent l) {
            if (searchBar.getText().equals("")) {
                searchBar.setText("Search for food or recipe");
            }
        }
    }

    class search implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            try {
                model.clear();
                String text = searchBar.getText();
                if (text.equals("Search for food or recipe")) {
                    text = "";
                }
                //String[] results = control.searchDatabase(text);
                ArrayList<String> results = control.searchDatabase(text);
                if (results.size() == 0) {
                    model.addElement("Nothing found!");
                } else {
                    model.addAll(results);
                }
                
            } catch (NullPointerException n) {
                System.out.println("Oops" + n.getMessage());
                actionPerformed(e);
            }
        }
    }

    class selectItem implements ListSelectionListener {
        @Override
        public void valueChanged (ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                String name = foodsList.getSelectedValue();
                //System.out.println(name);
                String unit = control.showUnit(name);
                unitLabel.setText(unit);
            }
            amountInput.requestFocusInWindow();
        }
    }

    class GetDetails implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            int choice = foodsList.getSelectedIndex();
        }
    }

    
    class AddFood implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            try {
                String meal = (String)mealChooser.getSelectedItem();
                String itemName = foodsList.getSelectedValue();
                double amount = Double.valueOf(amountInput.getText());
                control.addFoodToDiary(meal, itemName, amount);
                amountInput.setText("");
                searchBar.requestFocusInWindow();
            } catch (NumberFormatException n) {}
        }
    }

    class Exit implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            window.dispose();
        }
    }
}