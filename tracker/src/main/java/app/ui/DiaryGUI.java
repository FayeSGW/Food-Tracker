package app.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;

class DiaryGUI extends JPanel {
    TrackerControl control;
    String[] mealsList = {"Breakfast", "Lunch", "Dinner", "Snacks"};
    JPanel[] mealPanels = new JPanel[4];

    JPanel dateButtonsPanel, addButtonsPanel, overviewPanel, overviewLabels, eatenPanel, remainingPanel, 
        mealsPanel, waterPanel;
    JPanel overviewTitlePanel, overviewCaloriesPanel, overviewCarbsPanel, overviewSugarPanel, overviewFatPanel, 
        overviewSatFatPanel, overviewProteinPanel, overviewFibrePanel, overviewSaltPanel;
    JScrollPane scroll;
    JPanel breakfastPanel, breakfastTitlePanel, breakfastLabelsPanel, breakfastFoodsPanel;
    JPanel lunchPanel, lunchTitlePanel, lunchLabelsPanel, lunchFoodsPanel;
    JPanel dinnerPanel, dinnerTitlePanel, dinnerLabelsPanel, dinnerFoodsPanel;
    JPanel snacksPanel, snacksTitlePanel, snacksLabelsPanel, snacksFoodsPanel;
    JPanel exercisePanel, exerciseTitlePanel, exerciseLabelsPanel, exerciseWorkoutsPanel;

    JPanel ePanel;

    JButton prevDay, chooseDay, nextDay;
    JButton addFoodButton, addWaterButton, addExerciseButton;
    JButton addToBreakfast, addToLunch, addToDinner, addToSnacks;
    JButton copyBreakfast, copyLunch, copyDinner, copySnacks;

    JLabel overviewTitle, overviewCalories, overviewCarbs, overviewSugar, overviewFat, overviewSatFat, overviewProtein, overviewFibre, overviewSalt;
    JLabel eatenTitle, eatenCalories, eatenCarbs, eatenSugar, eatenFat, eatenSatFat, eatenProtein, eatenFibre, eatenSalt;
    JLabel remainingTitle, remainingCalories, remainingCarbs, remainingSugar, remainingFat, remainingSatFat, remainingProtein, remainingFibre, remainingSalt;
    JLabel breakfastTitle, breakfastBlank, breakfastCalories, breakfastCarbs, breakfastFat, breakfastProtein, breakfastFibre;
    JLabel lunchTitle, lunchBlank, lunchCalories, lunchCarbs, lunchFat, lunchProtein, lunchFibre;
    JLabel dinnerTitle, dinnerBlank, dinnerCalories, dinnerCarbs, dinnerFat, dinnerProtein, dinnerFibre;
    JLabel snacksTitle, snacksBlank, snacksCalories, snacksCarbs, snacksFat, snacksProtein, snacksFibre;
    JLabel exerciseTitle, exerciseName, exerciseTime, exerciseCals;


    DiaryGUI(TrackerControl control) {
        this.control = control;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        dateButtonsPanel = new JPanel(); this.add(dateButtonsPanel);
        prevDay = new JButton("<"); prevDay.addActionListener(new goToPrev());
        chooseDay = new JButton(".."); chooseDay.addActionListener(new chooseDate());
        nextDay = new JButton(">"); nextDay.addActionListener(new goToNext());
        dateButtonsPanel.add(prevDay); dateButtonsPanel.add(chooseDay); dateButtonsPanel.add(nextDay);

        addButtonsPanel = new JPanel(); this.add(addButtonsPanel);
        addFoodButton = new JButton("Add Food"); addFoodButton.addActionListener(new addFood(0));
        addWaterButton = new JButton("Add Water"); addWaterButton.addActionListener(new addWaterC());
        addExerciseButton = new JButton("Add Workout"); addExerciseButton.addActionListener(new addExercise());
        addButtonsPanel.add(addFoodButton); addButtonsPanel.add(addWaterButton); addButtonsPanel.add(addExerciseButton);

        overviewPanel = new JPanel(); this.add(overviewPanel);
        
        overviewTitlePanel = new JPanel(); overviewTitlePanel.setLayout(new BoxLayout(overviewTitlePanel, BoxLayout.Y_AXIS));
        overviewCaloriesPanel = new JPanel(); overviewCaloriesPanel.setLayout(new BoxLayout(overviewCaloriesPanel, BoxLayout.Y_AXIS));
        overviewCarbsPanel = new JPanel(); overviewCarbsPanel.setLayout(new BoxLayout(overviewCarbsPanel, BoxLayout.Y_AXIS));
        overviewSugarPanel = new JPanel(); overviewSugarPanel.setLayout(new BoxLayout(overviewSugarPanel, BoxLayout.Y_AXIS));
        overviewFatPanel = new JPanel(); overviewFatPanel.setLayout(new BoxLayout(overviewFatPanel, BoxLayout.Y_AXIS));
        overviewSatFatPanel = new JPanel(); overviewSatFatPanel.setLayout(new BoxLayout(overviewSatFatPanel, BoxLayout.Y_AXIS));
        overviewProteinPanel = new JPanel(); overviewProteinPanel.setLayout(new BoxLayout(overviewProteinPanel, BoxLayout.Y_AXIS));
        overviewFibrePanel = new JPanel(); overviewFibrePanel.setLayout(new BoxLayout(overviewFibrePanel, BoxLayout.Y_AXIS));
        overviewSaltPanel = new JPanel(); overviewSaltPanel.setLayout(new BoxLayout(overviewSaltPanel, BoxLayout.Y_AXIS));
        overviewPanel.add(overviewTitlePanel); overviewPanel.add(overviewCaloriesPanel); overviewPanel.add(overviewCarbsPanel);
        overviewPanel.add(overviewSugarPanel); overviewPanel.add(overviewFatPanel); overviewPanel.add(overviewSatFatPanel); overviewPanel.add(overviewProteinPanel);
        overviewPanel.add(overviewFibrePanel); overviewPanel.add(overviewSaltPanel);

        overviewTitle = new JLabel(" "); overviewTitlePanel.add(overviewTitle);
        eatenTitle = new JLabel("Eaten: "); eatenTitle.setAlignmentX(Component.RIGHT_ALIGNMENT); overviewTitlePanel.add(eatenTitle);
        remainingTitle = new JLabel("Remaining: "); remainingTitle.setAlignmentX(Component.RIGHT_ALIGNMENT); overviewTitlePanel.add(remainingTitle);

        overviewCalories = new JLabel("Calories"); overviewCalories.setAlignmentX(Component.CENTER_ALIGNMENT); overviewCaloriesPanel.add(overviewCalories);
        eatenCalories = new JLabel(""); eatenCalories.setAlignmentX(Component.CENTER_ALIGNMENT); overviewCaloriesPanel.add(eatenCalories);
        remainingCalories = new JLabel(""); remainingCalories.setAlignmentX(Component.CENTER_ALIGNMENT); overviewCaloriesPanel.add(remainingCalories);

        overviewCarbs = new JLabel("Carbs"); overviewCarbs.setAlignmentX(Component.CENTER_ALIGNMENT); overviewCarbsPanel.add(overviewCarbs);
        eatenCarbs = new JLabel(""); eatenCarbs.setAlignmentX(Component.CENTER_ALIGNMENT); overviewCarbsPanel.add(eatenCarbs);
        remainingCarbs = new JLabel(""); remainingCarbs.setAlignmentX(Component.CENTER_ALIGNMENT); overviewCarbsPanel.add(remainingCarbs);

        overviewSugar = new JLabel("Sugar"); overviewSugar.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSugarPanel.add(overviewSugar);
        eatenSugar = new JLabel(""); eatenSugar.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSugarPanel.add(eatenSugar);
        remainingSugar = new JLabel(""); remainingSugar.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSugarPanel.add(remainingSugar);

        overviewFat = new JLabel("Fat"); overviewFat.setAlignmentX(Component.CENTER_ALIGNMENT); overviewFatPanel.add(overviewFat);
        eatenFat = new JLabel(""); eatenFat.setAlignmentX(Component.CENTER_ALIGNMENT); overviewFatPanel.add(eatenFat);
        remainingFat = new JLabel(""); remainingFat.setAlignmentX(Component.CENTER_ALIGNMENT); overviewFatPanel.add(remainingFat);

        overviewSatFat = new JLabel("Sat. Fat"); overviewSatFat.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSatFatPanel.add(overviewSatFat);
        eatenSatFat = new JLabel(""); eatenSatFat.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSatFatPanel.add(eatenSatFat);
        remainingSatFat = new JLabel(""); remainingSatFat.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSatFatPanel.add(remainingSatFat);

        overviewProtein = new JLabel("Protein"); overviewProtein.setAlignmentX(Component.CENTER_ALIGNMENT); overviewProteinPanel.add(overviewProtein);
        eatenProtein = new JLabel(""); eatenProtein.setAlignmentX(Component.CENTER_ALIGNMENT); overviewProteinPanel.add(eatenProtein);
        remainingProtein = new JLabel(""); remainingProtein.setAlignmentX(Component.CENTER_ALIGNMENT); overviewProteinPanel.add(remainingProtein);

        overviewFibre = new JLabel("Fibre"); overviewFibre.setAlignmentX(Component.CENTER_ALIGNMENT); overviewFibrePanel.add(overviewFibre);
        eatenFibre = new JLabel(""); eatenFibre.setAlignmentX(Component.CENTER_ALIGNMENT); overviewFibrePanel.add(eatenFibre);
        remainingFibre = new JLabel(""); remainingFibre.setAlignmentX(Component.CENTER_ALIGNMENT); overviewFibrePanel.add(remainingFibre);

        overviewSalt = new JLabel("Salt"); overviewSalt.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSaltPanel.add(overviewSalt);
        eatenSalt = new JLabel(""); eatenSalt.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSaltPanel.add(eatenSalt);
        remainingSalt = new JLabel(""); remainingSalt.setAlignmentX(Component.CENTER_ALIGNMENT); overviewSaltPanel.add(remainingSalt);

        mealsPanel = new JPanel(); mealsPanel.setLayout(new BoxLayout(mealsPanel, BoxLayout.Y_AXIS));

        scroll = new JScrollPane(mealsPanel); scroll.getVerticalScrollBar().setUnitIncrement(10);
        this.add(scroll);

        breakfastPanel = new JPanel(); breakfastPanel.setLayout(new BoxLayout(breakfastPanel, BoxLayout.Y_AXIS));
        lunchPanel = new JPanel(); lunchPanel.setLayout(new BoxLayout(lunchPanel, BoxLayout.Y_AXIS));
        dinnerPanel = new JPanel(); dinnerPanel.setLayout(new BoxLayout(dinnerPanel, BoxLayout.Y_AXIS));
        snacksPanel = new JPanel(); snacksPanel.setLayout(new BoxLayout(snacksPanel, BoxLayout.Y_AXIS));
        exercisePanel = new JPanel(); exercisePanel.setLayout(new BoxLayout(exercisePanel, BoxLayout.Y_AXIS));
        waterPanel = new JPanel(); waterPanel.setLayout(new BoxLayout(waterPanel, BoxLayout.Y_AXIS));
        mealsPanel.add(breakfastPanel); mealsPanel.add(lunchPanel); mealsPanel.add(dinnerPanel); mealsPanel.add(snacksPanel); 
        mealsPanel.add(exercisePanel); mealsPanel.add(waterPanel);

        breakfastTitlePanel = new JPanel(); breakfastTitlePanel.setAlignmentX(CENTER_ALIGNMENT); breakfastPanel.add(breakfastTitlePanel);
        breakfastTitle = new JLabel("Breakfast"); breakfastTitle.setFont(new Font(breakfastTitle.getFont().toString(), Font.BOLD, 15));
        breakfastTitle.setPreferredSize(new Dimension(100, 30)); breakfastTitlePanel.add(breakfastTitle);
        addToBreakfast = new JButton("Add food"); breakfastTitlePanel.add(addToBreakfast);
        addToBreakfast.addActionListener(new addFood(0));
        copyBreakfast = new JButton("Copy"); breakfastTitlePanel.add(copyBreakfast);
        copyBreakfast.addActionListener(new copyMeal("Breakfast", 0));
        breakfastLabelsPanel = new JPanel(); breakfastPanel.add(breakfastLabelsPanel);
        breakfastBlank = new JLabel(""); breakfastBlank.setPreferredSize(new Dimension(200, 26));
        breakfastLabelsPanel.add(breakfastBlank);
        breakfastCalories = new JLabel("Calories"); breakfastLabelsPanel.add(breakfastCalories);
        breakfastCarbs = new JLabel("Carbs"); breakfastLabelsPanel.add(breakfastCarbs);
        breakfastFat = new JLabel("Fat"); breakfastLabelsPanel.add(breakfastFat);
        breakfastProtein = new JLabel("Protein"); breakfastLabelsPanel.add(breakfastProtein);
        breakfastFibre = new JLabel("Fibre"); breakfastLabelsPanel.add(breakfastFibre);
        breakfastFoodsPanel = new JPanel(); breakfastFoodsPanel.setLayout(new BoxLayout(breakfastFoodsPanel, BoxLayout.Y_AXIS));
        breakfastPanel.add(breakfastFoodsPanel);

        lunchTitlePanel = new JPanel(); lunchTitlePanel.setAlignmentX(CENTER_ALIGNMENT); lunchPanel.add(lunchTitlePanel);
        lunchTitle = new JLabel("Lunch"); lunchTitle.setFont(new Font(breakfastTitle.getFont().toString(), Font.BOLD, 15));
        lunchTitle.setPreferredSize(new Dimension(100, 30)); lunchTitlePanel.add(lunchTitle);
        addToLunch = new JButton("Add Food"); lunchTitlePanel.add(addToLunch);
        addToLunch.addActionListener(new addFood(1));
        copyLunch = new JButton("Copy"); lunchTitlePanel.add(copyLunch);
        copyLunch.addActionListener(new copyMeal("Lunch", 1));
        lunchLabelsPanel = new JPanel(); lunchPanel.add(lunchLabelsPanel);
        lunchBlank = new JLabel(" "); lunchBlank.setPreferredSize(new Dimension(200, 26));
        lunchLabelsPanel.add(lunchBlank);
        lunchCalories = new JLabel("Calories"); lunchLabelsPanel.add(lunchCalories);
        lunchCarbs = new JLabel("Carbs"); lunchLabelsPanel.add(lunchCarbs);
        lunchFat = new JLabel("Fat"); lunchLabelsPanel.add(lunchFat);
        lunchProtein = new JLabel("Protein"); lunchLabelsPanel.add(lunchProtein);
        lunchFibre = new JLabel("Fibre"); lunchLabelsPanel.add(lunchFibre);
        lunchFoodsPanel = new JPanel(); lunchFoodsPanel.setLayout(new BoxLayout(lunchFoodsPanel, BoxLayout.Y_AXIS));
        lunchPanel.add(lunchFoodsPanel);

        dinnerTitlePanel = new JPanel(); dinnerTitlePanel.setAlignmentX(CENTER_ALIGNMENT); dinnerPanel.add(dinnerTitlePanel);
        dinnerTitle = new JLabel("Dinner"); dinnerTitle.setFont(new Font(breakfastTitle.getFont().toString(), Font.BOLD, 15));
        dinnerTitle.setPreferredSize(new Dimension(100, 30)); dinnerTitlePanel.add(dinnerTitle);
        addToDinner = new JButton("Add Food"); dinnerTitlePanel.add(addToDinner);
        addToDinner.addActionListener(new addFood(2));
        copyDinner = new JButton("Copy"); dinnerTitlePanel.add(copyDinner);
        copyDinner.addActionListener(new copyMeal("Dinner", 2));
        dinnerLabelsPanel = new JPanel(); dinnerPanel.add(dinnerLabelsPanel);
        dinnerBlank = new JLabel(" "); dinnerBlank.setPreferredSize(new Dimension(200, 26));
        dinnerLabelsPanel.add(dinnerBlank);
        dinnerCalories = new JLabel("Calories"); dinnerLabelsPanel.add(dinnerCalories);
        dinnerCarbs = new JLabel("Carbs"); dinnerLabelsPanel.add(dinnerCarbs);
        dinnerFat = new JLabel("Fat"); dinnerLabelsPanel.add(dinnerFat);
        dinnerProtein = new JLabel("Protein"); dinnerLabelsPanel.add(dinnerProtein);
        dinnerFibre = new JLabel("Fibre"); dinnerLabelsPanel.add(dinnerFibre);
        dinnerFoodsPanel = new JPanel(); dinnerFoodsPanel.setLayout(new BoxLayout(dinnerFoodsPanel, BoxLayout.Y_AXIS));
        dinnerPanel.add(dinnerFoodsPanel);

        snacksTitlePanel = new JPanel(); snacksTitlePanel.setAlignmentX(CENTER_ALIGNMENT); snacksPanel.add(snacksTitlePanel);
        snacksTitle = new JLabel("Snacks"); snacksTitle.setFont(new Font(breakfastTitle.getFont().toString(), Font.BOLD, 15));
        snacksTitle.setPreferredSize(new Dimension(100, 30)); snacksTitlePanel.add(snacksTitle);
        addToSnacks = new JButton("Add Food"); snacksTitlePanel.add(addToSnacks);
        addToSnacks.addActionListener(new addFood(3));
        copySnacks = new JButton("Copy"); snacksTitlePanel.add(copySnacks);
        copySnacks.addActionListener(new copyMeal("Snacks", 3));
        snacksLabelsPanel = new JPanel(); snacksPanel.add(snacksLabelsPanel);
        snacksBlank = new JLabel(" "); snacksBlank.setPreferredSize(new Dimension(200, 26));
        snacksLabelsPanel.add(snacksBlank);
        snacksCalories = new JLabel("Calories"); snacksLabelsPanel.add(snacksCalories);
        snacksCarbs = new JLabel("Carbs"); snacksLabelsPanel.add(snacksCarbs);
        snacksFat = new JLabel("Fat"); snacksLabelsPanel.add(snacksFat);
        snacksProtein = new JLabel("Protein"); snacksLabelsPanel.add(snacksProtein);
        snacksFibre = new JLabel("Fibre"); snacksLabelsPanel.add(snacksFibre);
        snacksFoodsPanel = new JPanel(); snacksFoodsPanel.setLayout(new BoxLayout(snacksFoodsPanel, BoxLayout.Y_AXIS));
        snacksPanel.add(snacksFoodsPanel);

        exerciseTitlePanel = new JPanel(); exerciseTitlePanel.setAlignmentX(RIGHT_ALIGNMENT); exercisePanel.add(exerciseTitlePanel);
        exerciseTitle = new JLabel("Exercise"); exerciseTitle.setFont(new Font(breakfastTitle.getFont().toString(), Font.BOLD, 15)); exerciseTitlePanel.add(exerciseTitle);
        exerciseLabelsPanel = new JPanel(); exercisePanel.add(exerciseLabelsPanel);
        exerciseName = new JLabel("Workout"); exerciseName.setPreferredSize(new Dimension(100, 30));
        exerciseLabelsPanel.add(exerciseName);
        exerciseTime = new JLabel("Duration"); exerciseLabelsPanel.add(exerciseTime);
        exerciseCals = new JLabel("Calories Burned"); exerciseLabelsPanel.add(exerciseCals);
        exerciseWorkoutsPanel = new JPanel(); exerciseWorkoutsPanel.setLayout(new BoxLayout(exerciseWorkoutsPanel, BoxLayout.Y_AXIS));
        exercisePanel.add(exerciseWorkoutsPanel);
        
        mealPanels[0] = breakfastFoodsPanel; mealPanels[1] = lunchFoodsPanel; mealPanels[2] = dinnerFoodsPanel; mealPanels[3] = snacksFoodsPanel;

        populateMealPanels();
    }

    void changeDate(String day, int date, String month) {
        String string = String.format("%s %d %s", day, date, month);
        chooseDay.setText(string);
    }

    void updateSummary(double[] nutrition, double[] remaining) {
        eatenCalories.setText(Integer.toString((int)nutrition[0]));
        eatenCarbs.setText(Integer.toString((int)Math.round(nutrition[3])) + " g");
        eatenSugar.setText(Integer.toString((int)Math.round(nutrition[4])) + " g");
        eatenFat.setText(Integer.toString((int)Math.round(nutrition[1])) + " g");
        eatenSatFat.setText(Integer.toString((int)Math.round(nutrition[2])) + " g");
        eatenProtein.setText(Integer.toString((int)Math.round(nutrition[6])) + " g");
        eatenFibre.setText(Integer.toString((int)Math.round(nutrition[5])) + " g");
        eatenSalt.setText(Integer.toString((int)Math.round(nutrition[7])) + " g");

        remainingCalories.setText(Integer.toString((int)remaining[0]));
        remainingCarbs.setText(Integer.toString((int)Math.round(remaining[3])) + " g");
        remainingSugar.setText(Integer.toString((int)Math.round(remaining[4])) + " g");
        remainingFat.setText(Integer.toString((int)Math.round(remaining[1])) + " g");
        remainingSatFat.setText(Integer.toString((int)Math.round(remaining[2])) + " g");
        remainingProtein.setText(Integer.toString((int)Math.round(remaining[6])) + " g");
        remainingFibre.setText(Integer.toString((int)Math.round(remaining[5])) + " g");
        remainingSalt.setText(Integer.toString((int)Math.round(remaining[7])) + " g");
    }

    void clearMealPanels() {
        for (JPanel panel: mealPanels) {
            Component[] foodPanels = panel.getComponents();
            for (Component foodPanel: foodPanels) {
                panel.remove(foodPanel);
            }
            panel.repaint();
        }
        
        Component[] exPanel = exerciseWorkoutsPanel.getComponents();
        for (Component panel: exPanel) {
            exerciseWorkoutsPanel.remove(panel);
        }
        exerciseWorkoutsPanel.revalidate();
        exerciseWorkoutsPanel.repaint();
    }

    void populateMealPanels() {
        clearMealPanels();
        for (String meal:mealsList) {
            updateMealPanels(meal);
        }
        updateExercisePanel();
        this.repaint();
    }

    void updateMealPanels(String mealName) {
        HashMap <Integer, ArrayList<Object>> foodsList = control.showFoodsinMeal(mealName);

        for (Integer index: foodsList.keySet()) {
            ArrayList<Object> details = foodsList.get(index);
            String foodName = (String)details.get(0);
            String displayName = (String)details.get(3);
            String foodAmount = control.showFoodItemAmount(mealName, index);
            String[] foodNutrition = control.showFoodItemNutrition(mealName, index);

            JPanel foodItem = new JPanel();
            JPanel mp;
            
            if (mealName.equals("Breakfast")) {
                mp = breakfastFoodsPanel;
            } else if (mealName.equals("Lunch")) {
                mp = lunchFoodsPanel;
            } else if (mealName.equals("Dinner")) {
                mp = dinnerFoodsPanel;
            } else {
                mp = snacksFoodsPanel;
            }

            mp.add(foodItem);
            
            JLabel name = new JLabel(String.format("%s, %s", displayName, foodAmount)); name.setPreferredSize(new Dimension(200, 15));
            JLabel calories = new JLabel(foodNutrition[0], SwingConstants.CENTER); calories.setPreferredSize(new Dimension(47, 15));
            JLabel carbs = new JLabel(foodNutrition[3] + " g", SwingConstants.CENTER); carbs.setPreferredSize(new Dimension(34, 15));
            JLabel fat = new JLabel(foodNutrition[1] + " g", SwingConstants.CENTER); fat.setPreferredSize(new Dimension(25, 15));
            JLabel protein = new JLabel(foodNutrition[6] + " g", SwingConstants.CENTER); protein.setPreferredSize(new Dimension(42, 15));
            JLabel fibre = new JLabel(foodNutrition[5] + " g", SwingConstants.CENTER); fibre.setPreferredSize(new Dimension(30, 15));
            foodItem.add(name); foodItem.add(calories); foodItem.add(carbs); foodItem.add(fat); foodItem.add(protein); foodItem.add(fibre);
;
            name.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    control.editMealDialogue(mealName, index);
                }
            });
        }
    }

    void updateExercisePanel() {
        HashMap<Integer, ArrayList<String>> workouts = control.showWorkouts();
        for (Integer workout: workouts.keySet()) {
            JPanel exercise = new JPanel();
            exerciseWorkoutsPanel.add(exercise);

            String time = workouts.get(workout).get(1);
            String cals = workouts.get(workout).get(2);
            String workoutName = workouts.get(workout).get(0);

            JLabel name = new JLabel(workoutName);
            JLabel timeLabel = new JLabel(time);
            JLabel calories = new JLabel(cals);
            exercise.add(name); exercise.add(timeLabel); exercise.add(calories);
            
            name.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    control.editExerciseDialogue("edit", workout);
                }
            });
        }
    }

    class chooseDate implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            control.openCalendar(null);
        }
    }

    class goToPrev implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            control.goToPrevDay();
        }
    }

    class goToNext implements ActionListener {
        @Override 
        public void actionPerformed (ActionEvent e) {
            control.goToNextDay();
        }
    }

    class addFood implements ActionListener {
        int mealIndex;
        addFood(int index) {
            this.mealIndex = index;
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            control.addFoodDialogue(mealIndex, "diary");
        }
    }

    class addWaterC implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            control.addWater();
        }
    }

    class addExercise implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            control.addExerciseDialogue("add");
        }
    }

    class copyMeal implements ActionListener {
        String meal;
        int mealIndex;
        copyMeal(String meal, int mealIndex) {
            this.meal = meal;
            this.mealIndex = mealIndex;
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            control.copyMealDialogue(meal, mealIndex);
        }
    }
}