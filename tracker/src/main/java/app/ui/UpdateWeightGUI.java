package app.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

import exceptions.ExHandling;
import exceptions.NoNegativeException;
import exceptions.NoNullException;

class UpdateWeightGUI {
    TrackerControl control;
    LocalDate date;
    JFrame window;
    JPanel whole, text;
    JLabel instructions, unit;
    JTextField input;
    JButton button;

    UpdateWeightGUI(TrackerControl control, LocalDate date) {
        this.control = control;
        this.date = date;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

        window = new JFrame("Update Weight");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        whole = new JPanel(new BorderLayout(10,10));
        whole.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        window.add(whole);

        instructions = new JLabel("Input your new weight below:");
        whole.add(instructions, BorderLayout.NORTH);

        input = new JTextField(); 
        whole.add(input, BorderLayout.CENTER);

        button = new JButton("Update Weight"); button.addActionListener(new updateWeight(date));
        whole.add(button, BorderLayout.EAST);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

    class updateWeight implements ActionListener {
        LocalDate date;
        updateWeight(LocalDate date) {
            this.date = date;
        }
        @Override
        public void actionPerformed (ActionEvent e) {
            try {
                double weight = ExHandling.checkDoublesIncNullCheck("Weight", input.getText());
                control.updateWeight(date, weight);
                window.dispose();
            } catch (NumberFormatException | NoNegativeException | NoNullException n) {
                //TODO - output in window if invalue value entered
            }
        }
    }

}