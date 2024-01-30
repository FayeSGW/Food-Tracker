package app.ui;

import java.awt.*;
import javax.swing.*;

class HistoryGUI extends JPanel {
    TrackerControl tControl;
    HistoryControl hControl;

    HistoryGUI(HistoryControl hControl, TrackerControl tControl) {
        this.tControl = tControl;
        this.hControl = hControl;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }

    }
}