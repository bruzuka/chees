package Listener.ChooseMenuListener;

import GUI.PreGameGUI;
import Game.ChessEngine;
import MainProgram.MainProgram;
import Util.Swapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * DefaultGame listener for Choose menu GUI
 */
public class DefaultGameButtonListener implements ActionListener {
    private MainProgram mainProgram; //Main Program

    /**
     * Default constructor
     * @param mainProgram main program
     */
    public DefaultGameButtonListener(MainProgram mainProgram) {
        this.mainProgram = mainProgram;
    }

    /**
     * The method inits preGameGUI and swaps content pane to it
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainProgram.preGameGUI = new PreGameGUI(mainProgram.width, mainProgram.height, new ChessEngine());
        mainProgram.preGameGUI.addListeners(mainProgram);
        Swapper.swapContentPane(mainProgram.mainFrame, mainProgram.preGameGUI);
    }
}
