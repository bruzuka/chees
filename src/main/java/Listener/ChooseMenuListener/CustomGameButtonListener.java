package Listener.ChooseMenuListener;

import GUI.CreatorGUI.CreatorGUI;
import MainProgram.MainProgram;
import Util.Swapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CustomGame listener for Choose menu GUI
 */
public class CustomGameButtonListener implements ActionListener {
    private MainProgram mainProgram; // Main program

    /**
     * Default constructor
     * @param mainProgram main program
     */
    public CustomGameButtonListener(MainProgram mainProgram) {
        this.mainProgram = mainProgram;
    }

    /**
     * The method inits creatorGUI and swaps content pane to it
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainProgram.creatorGUI = new CreatorGUI(mainProgram.width, mainProgram.height);
        mainProgram.creatorGUI.addListener(mainProgram);
        Swapper.swapContentPane(mainProgram.mainFrame, mainProgram.creatorGUI);
    }
}
