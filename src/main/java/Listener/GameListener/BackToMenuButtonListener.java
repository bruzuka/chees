package Listener.GameListener;

import GUI.MainMenuGUI;
import GameController.GameController;
import Util.Swapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * BackToMenu listeners for Creator GUI
 */
public class BackToMenuButtonListener implements ActionListener {
    private MainMenuGUI menuGUI; // Main menu GUI
    private GameController gameController; // Game controller
    private JFrame mainFrame; // Main frame

    /**
     * Default constructor
     * @param menuGUI main menu GUI
     * @param gameController game controller
     * @param mainFrame main frame
     */
    public BackToMenuButtonListener(MainMenuGUI menuGUI, GameController gameController, JFrame mainFrame) {
        this.menuGUI = menuGUI;
        this.gameController = gameController;
        this.mainFrame = mainFrame;
    }

    /**
     * The method swaps contain pane to main menu GUI if user has clicked to "menu"
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        var options = new String[] {"Menu", "Back"};
        int response = JOptionPane.showOptionDialog(null, "All unsaved progress will be lost",
                "Back to menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (response == 0){
            gameController.stopGame(); // Stopping game controller
            Swapper.swapContentPane(mainFrame, menuGUI);
        }
    }
}
