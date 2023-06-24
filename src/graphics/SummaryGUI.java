package src.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

class SummaryGUI extends JPanel {
    TrackerControl control;

    JPanel summaryDateButtons, summaryAddButtons, caloriesPanel, caloriesText, caloriesGraphic;
    JPanel macros, macrosPanel, macrosCarbs, macrosProtein, macrosFat, macrosText, macrosGraphic, carbsText, carbsGraphic, proteinText, proteinGraphic, fatText, fatGraphic, waterPanel, waterAll, waterGraphic;
    JLabel caloriesTitle, caloriesLeft, calsZero, calsGoal, macrosTitle, carbs, carbsZero, carbsGoal, protein, proteinZero, proteinGoal, fat, fatZero, fatGoal, waterTitle, waterZero, waterGoal;
    JButton prevDay, chooseDay, nextDay, addFood, addWater, addExercise, updateWeight;
    JProgressBar caloriesProgress, carbProgress, proteinProgress, fatProgress, waterProgress;

    SummaryGUI(TrackerControl control) {
        this.control = control;

                
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }


        summaryDateButtons = new JPanel(new FlowLayout());
        this.add(summaryDateButtons);

        prevDay = new JButton("<"); prevDay.addActionListener(new goToPrev());
        chooseDay = new JButton(".."); chooseDay.addActionListener(new chooseDate());
        nextDay = new JButton(">"); nextDay.addActionListener(new goToNext());
        summaryDateButtons.add(prevDay); summaryDateButtons.add(chooseDay); summaryDateButtons.add(nextDay);

        summaryAddButtons = new JPanel(new FlowLayout());
        this.add(summaryAddButtons);
        addFood = new JButton("Add Food"); addFood.addActionListener(new addFood(0));
        addWater = new JButton("Add Water"); addWater.addActionListener(new addWaterC());
        addExercise = new JButton("Add Exercise");
        updateWeight = new JButton("Update Weight");
        summaryAddButtons.add(addFood); summaryAddButtons.add(addWater); summaryAddButtons.add(addExercise); summaryAddButtons.add(updateWeight);

        caloriesPanel = new JPanel(new BorderLayout()); this.add(caloriesPanel);
        caloriesTitle = new JLabel("Calories", SwingConstants.CENTER); 
        caloriesPanel.add(caloriesTitle, BorderLayout.NORTH); 

        caloriesText = new JPanel(); caloriesText.setPreferredSize(new Dimension(200, 25));
        caloriesPanel.add(caloriesText, BorderLayout.WEST); 
        caloriesLeft = new JLabel("Left: "); caloriesText.add(caloriesLeft);
        caloriesGraphic = new JPanel(); 
        caloriesPanel.add(caloriesGraphic, BorderLayout.EAST);

        calsZero = new JLabel("0"); caloriesGraphic.add(calsZero);
        caloriesProgress = new JProgressBar(); caloriesGraphic.add(caloriesProgress);
        calsGoal = new JLabel(); calsGoal.setPreferredSize(new Dimension(50, 26)); caloriesGraphic.add(calsGoal);

        macrosPanel = new JPanel(new BorderLayout()); this.add(macrosPanel);
        macrosTitle = new JLabel("Macros", SwingConstants.CENTER); macrosPanel.add(macrosTitle, BorderLayout.NORTH);

        //macros = new JPanel(); macros.setLayout(new BoxLayout(macros, BoxLayout.Y_AXIS));
        //this.add(macros);

        macrosText = new JPanel(); macrosText.setLayout(new BoxLayout(macrosText, BoxLayout.Y_AXIS));
        macrosGraphic = new JPanel(); macrosGraphic.setLayout(new BoxLayout(macrosGraphic, BoxLayout.Y_AXIS));
        macrosPanel.add(macrosText, BorderLayout.WEST); macrosPanel.add(macrosGraphic, BorderLayout.EAST);

        //macrosCarbs = new JPanel(); macros.add(macrosCarbs);
        carbsText = new JPanel(); carbsText.setPreferredSize(new Dimension(150, 25)); macrosText.add(carbsText);
        carbs = new JLabel("Carbs: ");  //macrosCarbs.add(carbs);

        //macrosProtein = new JPanel(); macros.add(macrosProtein);
        proteinText = new JPanel(); proteinText.setPreferredSize(new Dimension(150, 25)); macrosText.add(proteinText);
        protein = new JLabel("Protein: "); //macrosProtein.add(protein);

        //macrosFat = new JPanel(); macros.add(macrosFat);
        fatText = new JPanel(); fatText.setPreferredSize(new Dimension(200, 25)); macrosText.add(fatText);
        fat = new JLabel("Fat: "); //macrosFat.add(fat);
        carbsText.add(carbs); proteinText.add(protein); fatText.add(fat);

        carbsGraphic = new JPanel(); macrosGraphic.add(carbsGraphic);
        proteinGraphic = new JPanel(); macrosGraphic.add(proteinGraphic);
        fatGraphic = new JPanel(); macrosGraphic.add(fatGraphic);

        carbsZero = new JLabel("0"); carbsGraphic.add(carbsZero);
        carbProgress = new JProgressBar(); carbsGraphic.add(carbProgress);
        carbsGoal = new JLabel(); carbsGoal.setPreferredSize(new Dimension(50, 26)); carbsGraphic.add(carbsGoal);

        proteinZero = new JLabel("0"); proteinGraphic.add(proteinZero);
        proteinProgress = new JProgressBar(); proteinGraphic.add(proteinProgress);
        proteinGoal = new JLabel(); proteinGoal.setPreferredSize(new Dimension(50, 26)); proteinGraphic.add(proteinGoal);

        fatZero = new JLabel("0"); fatGraphic.add(fatZero);
        fatProgress = new JProgressBar(); fatGraphic.add(fatProgress);
        fatGoal = new JLabel(); fatGoal.setPreferredSize(new Dimension(50, 26)); fatGraphic.add(fatGoal);

        waterPanel = new JPanel(new BorderLayout()); this.add(waterPanel);

        waterTitle = new JLabel("Water", SwingConstants.CENTER); waterPanel.add(waterTitle, BorderLayout.NORTH);

        waterAll = new JPanel();
        waterPanel.add(waterAll, BorderLayout.SOUTH);

        waterZero = new JLabel("0"); waterAll.add(waterZero);
        waterProgress = new JProgressBar(); waterAll.add(waterProgress);
        waterGoal = new JLabel(); waterAll.add(waterGoal);

    }

    void changeDate(String day, int date, String month) {
        String string = String.format("%s %d %s", day, date, month);
        chooseDay.setText(string);
    }

    void updateCalories(String string, int cals) {
        caloriesLeft.setText("Left: " + string);
        caloriesProgress.setValue(cals);
        caloriesProgress.setString(Integer.toString(cals));
        caloriesProgress.setStringPainted(true);
    }

    void updateCarbs(String string, double carbs) {
        this.carbs.setText("Carbs: " + string);
        carbProgress.setValue((int)carbs);
        carbProgress.setString(String.format("%.1f g", carbs));
        carbProgress.setStringPainted(true);
    }

    void updateProtein(String string, double protein) {
        this.protein.setText("Protein: " + string);
        proteinProgress.setValue((int)protein);
        proteinProgress.setString(String.format("%.1f g", protein));
        proteinProgress.setStringPainted(true);
    }

    void updateFat(String string, double fat) {
        this.fat.setText("Fat: " + string);
        fatProgress.setValue((int)fat);
        fatProgress.setString(String.format("%.1f g", fat));
        fatProgress.setStringPainted(true);
    }

    void setCalsGoal(int calories) {
        caloriesProgress.setMaximum(calories);
        calsGoal.setText(Integer.toString(calories) + "    ");
    }

    void setWaterProgessBounds(int water) {
        //System.out.println(water);
        waterProgress.setMaximum(water);
        waterGoal.setText(Integer.toString(water));
    }

    void setCarbsGoal(double carbs) {
        carbProgress.setMaximum((int)carbs);
        carbsGoal.setText(String.format("%.1f g", carbs));
    }

    void setProteinGoal(double protein) {
        proteinProgress.setMaximum((int)protein);
        proteinGoal.setText(String.format("%.1f g", protein));
    }

    void setFatGoal(double fat) {
        fatProgress.setMaximum((int)fat);
        fatGoal.setText(String.format("%.1f g ", fat));
    }

    void updateWater(int water) {
        waterProgress.setValue(water);
        waterProgress.setString(Integer.toString(water) + " glasses");
        waterProgress.setStringPainted(true);
    }

    class addFood implements ActionListener {
        int index;
        addFood(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            //AddFoodControl aControl = new AddFoodControl(control);
            control.addFoodDialogue(index);
            //control.addFoodTest();
        }
    }

    class addWaterC implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            control.addWater();
        }
    }

    class chooseDate implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            //LocalDate current = control.showCurrentDate();
            //CalendarGUI cGUI = new CalendarGUI(control, current);
            control.openCalendar();
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


}