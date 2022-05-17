package Listener.CreatorListener;

import GUI.GameGUI.ImageGUI.PieceGUI;
import GUI.PreGameGUI;
import Game.ChessEngine;
import Game.Pieces.Piece;
import Game.Player;
import Game.Turn;
import MainProgram.MainProgram;
import Util.Swapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * PlayButton listener for Creator GUI
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
     * The method swap content pane to preGameGUI if board is valid
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (mainProgram.creatorGUI.isValidBoard()){ //Checking validity
            ArrayList<Piece> blackPieces = new ArrayList<>();
            ArrayList<Piece> whitePieces = new ArrayList<>();

            for (PieceGUI pieceGUI : mainProgram.creatorGUI.pieces) {
                if (pieceGUI.piece.player == Player.WHITE){
                    whitePieces.add(pieceGUI.piece);
                } else {
                    blackPieces.add(pieceGUI.piece);
                }
            }

            Turn turn = Turn.WHITE;
            if (mainProgram.creatorGUI.blackRadioButton.isSelected()){
                turn = Turn.BLACK;
            }

            ChessEngine chessEngine = new ChessEngine(whitePieces, new ArrayList<>(), blackPieces, new ArrayList<>(),
                    turn, Player.WHITE); //Init engine
            mainProgram.preGameGUI = new PreGameGUI(mainProgram.width, mainProgram.height, chessEngine);
            mainProgram.preGameGUI.addListeners(mainProgram);

            Swapper.swapContentPane(mainProgram.mainFrame, mainProgram.preGameGUI);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Board contains to many or to few kings",
                    "Invalid board",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
