package Listener.MainMenuListener;

import GUI.GameGUI.GameGUI;
import GUI.ViewerGUI;
import Game.Player;
import GameController.GameController;
import Listener.ViewerListener.BackToMenuButtonListener;
import Listener.ViewerListener.NextButtonListener;
import Listener.ViewerListener.PreviousButtonListener;
import MainProgram.MainProgram;
import Util.BoardDecoder;
import Util.ChessTimer;
import Util.Move;
import Util.Swapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * LoadButton listener for Main menu GUI
 */
public class LoadButtonListener implements ActionListener {
    private MainProgram mainProgram; // Main program

    /**
     * Default constructor
     * @param mainProgram main program
     */
    public LoadButtonListener(MainProgram mainProgram) {
        this.mainProgram = mainProgram;
    }

    /**
     * The method opens viewer GUI or game GUI. It depends from user's response
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        var openFile = new JFileChooser();
        int option = openFile.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            try (var fileReader = new FileReader(openFile.getSelectedFile())){
                checkThatFileIsNotInjured(fileReader);
            } catch (Exception exception){
                JOptionPane.showMessageDialog(null,
                        "File injured!",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (var fileReader = new FileReader(openFile.getSelectedFile())){ // Read board
                var bufferedReader = new BufferedReader(fileReader);
                var boardDecoder = new BoardDecoder(bufferedReader);
                bufferedReader.close();

                var options = new String[] {"View", "Play"};
                int response = JOptionPane.showOptionDialog(null, "Choose option", "Choosing option",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);

                if (response == 0){ // View
                    goToViewerGUI(boardDecoder);
                } else if (response == 1) { // Play
                    goToGameGUI(boardDecoder);
                }
            } catch (IOException exception){
                JOptionPane.showMessageDialog(null,
                        "Something goes wrong",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * The method inits viewerGUI and swap content pane to it
     * @param boardDecoder board decoder
     */
    private void goToViewerGUI(BoardDecoder boardDecoder){
        mainProgram.viewerGUI = new ViewerGUI(mainProgram.width, mainProgram.height,
                boardDecoder.getFormattedMoves(), boardDecoder.getSeparatedMoves());
        mainProgram.viewerGUI.nextButton.addActionListener(new NextButtonListener(mainProgram.viewerGUI));
        mainProgram.viewerGUI.previousButton.addActionListener(new PreviousButtonListener(mainProgram.viewerGUI));
        mainProgram.viewerGUI.backButton.addActionListener(new BackToMenuButtonListener(mainProgram.menuGUI, mainProgram.mainFrame));
        Swapper.swapContentPane(mainProgram.mainFrame, mainProgram.viewerGUI);
    }

    /**
     * The method inits gameGUI and swap content pane to it
     * @param boardDecoder board decoder
     */
    private void goToGameGUI(BoardDecoder boardDecoder){
        Move move = new Move();
        var engine = boardDecoder.getEngine();
        mainProgram.gameGUI = new GameGUI(mainProgram.width, mainProgram.height, engine, move);
        mainProgram.gameGUI.movesHistoryText.setText(boardDecoder.joinMovesStrings());

        ChessTimer timer = null;
        if (boardDecoder.getBlackSeconds() != -1 || boardDecoder.getWhiteSeconds() != -1){
            timer = new ChessTimer(mainProgram.gameGUI, Player.toPlayer(engine.turn),
                    move, boardDecoder.getBlackSeconds(), boardDecoder.getWhiteSeconds());
        }

        mainProgram.gameController = new GameController(mainProgram.gameGUI, engine, move, timer, boardDecoder.isVsPC());
        mainProgram.gameGUI.addListeners(mainProgram, boardDecoder.isVsPC());
        mainProgram.gameController.start();
        Swapper.swapContentPane(mainProgram.mainFrame, mainProgram.gameGUI);
    }

    /**
     * The method checks that file is not injured, otherwise it raises exception
     * @param fileReader file reader
     * @throws Exception
     */
    private void checkThatFileIsNotInjured(FileReader fileReader) throws Exception{
        var bufferReader = new BufferedReader(fileReader);
        var boardDecoder = new BoardDecoder(bufferReader);
        boardDecoder.getFormattedMoves();
        bufferReader.close();
    }
}
