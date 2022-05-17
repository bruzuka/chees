package Listener.PreGameListener;

import GUI.GameGUI.GameGUI;
import Game.Player;
import GameController.GameController;
import MainProgram.MainProgram;
import Util.ChessTimer;
import Util.Move;
import Util.Swapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PlayButton for listener PreGame GUI
 */
public class PlayButtonListener implements ActionListener {
    private MainProgram mainProgram; // Main program

    /**
     * Default constructor
     * @param mainProgram main program
     */
    public PlayButtonListener(MainProgram mainProgram) {
        this.mainProgram = mainProgram;
    }

    /**
     * The method inits gameGUI and game controller, than swaps content pane to gameGUI
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainProgram.preGameGUI.engine.firstPlayer = (mainProgram.preGameGUI.selectColorButton.getText().equals("White")) ?
                Player.WHITE : Player.BLACK;
        var move = new Move();

        mainProgram.gameGUI = new GameGUI(mainProgram.width, mainProgram.height, mainProgram.preGameGUI.engine, move);

        ChessTimer timer = null;
        if (mainProgram.preGameGUI.enableTimerButton.getText().equals("Enabled")){
            timer = new ChessTimer(mainProgram.gameGUI, Player.WHITE, move, mainProgram.HAS_TIME);
        }

        mainProgram.gameController = new GameController(mainProgram.gameGUI, mainProgram.preGameGUI.engine, move, timer,
                mainProgram.preGameGUI.selectPlayerButton.getText().equals("Vs PC"));
        mainProgram.gameGUI.addListeners(mainProgram, mainProgram.preGameGUI.selectPlayerButton.getText().equals("Vs PC"));
        mainProgram.gameController.start(); // Start of game

        Swapper.swapContentPane(mainProgram.mainFrame, mainProgram.gameGUI);
    }
}
