package Listener.MainMenuListener;

import GUI.ChooseMenuGUI;
import Util.Swapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PlayButton listener for main menu GUI
 */
public class NewGameButtonListener implements ActionListener {
    private JFrame mainFrame; // Main frame
    private ChooseMenuGUI chooseMenuGUI; // Chose menu GUI

    /**
     * Default constructor
     * @param mainFrame main frame
     * @param chooseMenuGUI choose menu GUI
     */
    public NewGameButtonListener(JFrame mainFrame, ChooseMenuGUI chooseMenuGUI) {
        this.mainFrame = mainFrame;
        this.chooseMenuGUI = chooseMenuGUI;
    }

    /**
     * The method swap content pane to choose menu GUI
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Swapper.swapContentPane(mainFrame, chooseMenuGUI);
    }
}
