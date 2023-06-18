import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

class SummaryGUI extends JPanel {
    TrackerControl control;
    User user;

    JPanel summaryDateButtons, summaryAddButtons, caloriesPanel, caloriesGraphic, macros, macrosCarbs, macrosProtein, macrosFat, macrosText, macrosGraphic, carbsGraphic, proteinGraphic, fatGraphic, waterAll, waterGraphic;
    JLabel caloriesTitle, caloriesLeft, macrosTitle, carbs, carbsZero, carbsGoal, protein, proteinZero, proteinGoal, fat, fatZero, fatGoal, waterTitle, waterZero, waterGoal;
    JButton prevDay, chooseDay, nextDay, addFood, addWater, addExercise, updateWeight;
    JProgressBar carbProgress, proteinProgress, fatProgress, waterProgress;

    SummaryGUI(TrackerControl control) {
        this.control = control;
        //this.user = user;
                
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
        addFood = new JButton("Add Food");
        addWater = new JButton("Add Water");
        addExercise = new JButton("Add Exercise");
        updateWeight = new JButton("Update Weight");
        summaryAddButtons.add(addFood); summaryAddButtons.add(addWater); summaryAddButtons.add(addExercise); summaryAddButtons.add(updateWeight);

        caloriesTitle = new JLabel("Calories");
        this.add(caloriesTitle); 

        caloriesPanel = new JPanel();
        caloriesLeft = new JLabel("Left: ");
        caloriesGraphic = new JPanel(); 
        this.add(caloriesPanel);
        caloriesPanel.add(caloriesLeft); caloriesPanel.add(caloriesGraphic);

        macrosTitle = new JLabel("Macros");
        this.add(macrosTitle);

        macros = new JPanel(); macros.setLayout(new BoxLayout(macros, BoxLayout.Y_AXIS));
        this.add(macros);

        /*macrosText = new JPanel(); macrosText.setLayout(new BoxLayout(macrosText, BoxLayout.Y_AXIS));
        macrosGraphic = new JPanel(); macrosGraphic.setLayout(new BoxLayout(macrosGraphic, BoxLayout.Y_AXIS));
        macros.add(macrosText); macros.add(macrosGraphic);*/

        macrosCarbs = new JPanel(); macros.add(macrosCarbs);
        carbs = new JLabel("Carbs: "); macrosCarbs.add(carbs);

        macrosProtein = new JPanel(); macros.add(macrosProtein);
        protein = new JLabel("Protein: "); macrosProtein.add(protein);

        macrosFat = new JPanel(); macros.add(macrosFat);
        fat = new JLabel("Fat: "); macrosFat.add(fat);
        //macrosText.add(carbs); macrosText.add(protein); macrosText.add(fat);

        carbsGraphic = new JPanel(); macrosCarbs.add(carbsGraphic); //macrosGraphic.add(carbsGraphic);
        proteinGraphic = new JPanel(); macrosProtein.add(proteinGraphic); //macrosGraphic.add(proteinGraphic);
        fatGraphic = new JPanel(); macrosFat.add(fatGraphic); //macrosGraphic.add(fatGraphic);

        carbsZero = new JLabel("0"); carbsGraphic.add(carbsZero);
        carbProgress = new JProgressBar(); carbsGraphic.add(carbProgress);
        carbsGoal = new JLabel(); carbsGraphic.add(carbsGoal);

        proteinZero = new JLabel("0"); proteinGraphic.add(proteinZero);
        proteinProgress = new JProgressBar(); proteinGraphic.add(proteinProgress);
        proteinGoal = new JLabel(); proteinGraphic.add(proteinGoal);

        fatZero = new JLabel("0"); fatGraphic.add(fatZero);
        fatProgress = new JProgressBar(); fatGraphic.add(fatProgress);
        fatGoal = new JLabel(); fatGraphic.add(fatGoal);


        waterTitle = new JLabel("Water");
        this.add(waterTitle);

        waterAll = new JPanel();
        this.add(waterAll);

        waterZero = new JLabel("0"); waterAll.add(waterZero);
        waterProgress = new JProgressBar(); waterAll.add(waterProgress);
        waterGoal = new JLabel(); waterAll.add(waterGoal);

        
        
    }

    void changeDate(String day, int date, String month) {
        String string = String.format("%s %d %s", day, date, month);
        chooseDay.setText(string);
    }

    void updateCalories(String string) {
        caloriesLeft.setText("Left: " + string);
    }

    void updateCarbs(String string, double carbs) {
        this.carbs.setText("Carbs: " + string);
        carbProgress.setValue((int)carbs);
        carbProgress.setString(Double.toString(carbs) + " g");
        carbProgress.setStringPainted(true);
    }

    void updateProtein(String string, double protein) {
        this.protein.setText("Protein: " + string);
        proteinProgress.setValue((int)protein);
        proteinProgress.setString(Double.toString(protein) + " g");
        proteinProgress.setStringPainted(true);
    }

    void updateFat(String string, double fat) {
        this.fat.setText("Fat: " + string);
        fatProgress.setValue((int)fat);
        fatProgress.setString(Double.toString(fat) + " g");
        fatProgress.setStringPainted(true);
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
        fatGoal.setText(String.format("%.1f g", fat));
    }

    void updateWater(int water) {
        waterProgress.setValue(water);
        waterProgress.setString(Integer.toString(water) + " glasses");
        waterProgress.setStringPainted(true);
    }


    class chooseDate implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            LocalDate current = control.showCurrentDate();
            CalendarGUI cGUI = new CalendarGUI(control, current);
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