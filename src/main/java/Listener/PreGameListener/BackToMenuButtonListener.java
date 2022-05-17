package Listener.PreGameListener;

import GUI.MainMenuGUI;
import Util.Swapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Back to menu for PreGema GUI
 */
public class BackToMenuButtonListener implements ActionListener {
    private MainMenuGUI menuGUI; // Main menu GUI
    private JFrame mainFrame; // Main frame

    /**
     * Default constructor
     * @param menuGUI main menu GUI
     * @param mainFrame main frame
     */
    public BackToMenuButtonListener(MainMenuGUI menuGUI, JFrame mainFrame) {
        this.menuGUI = menuGUI;
        this.mainFrame = mainFrame;
    }

    /**
     * The method changes current content pane and update mains frame
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Swapper.swapContentPane(mainFrame, menuGUI);
    }
}
