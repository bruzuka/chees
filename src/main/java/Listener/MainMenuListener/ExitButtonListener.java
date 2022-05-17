package Listener.MainMenuListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButtonListener implements ActionListener {
    private JFrame mainFrame; // Main frame

    /**
     * Default constructor
     *
     * @param mainFrame main frame
     */
    public ExitButtonListener(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * The method closes frame
     *
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainFrame.setVisible(false);
        mainFrame.dispose();

    }
}