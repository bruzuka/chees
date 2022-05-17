package Listener.GameListener;

import GUI.GameGUI.GameGUI;
import Util.BoardSaver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * SaveButton listener for game GUI
 */
public class SaveButtonListener implements ActionListener {
    private GameGUI gameGUI; // Game GUI
    private JFrame mainFrame; // Main frame
    private boolean vsPC; // Vs PC flag

    /**
     * Default constructor
     * @param gameGUI game GUI
     * @param mainFrame main frame
     * @param vsPC vs PC flag
     */
    public SaveButtonListener(GameGUI gameGUI, JFrame mainFrame, boolean vsPC){
        this.gameGUI = gameGUI;
        this.mainFrame = mainFrame;
        this.vsPC = vsPC;
    }

    /**
     * The method saves chess engine if selected file is valid
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        var saveFile = new JFileChooser();
        saveFile.setSelectedFile(new File("Board.txt")); // Default name
        saveFile.showSaveDialog(null);
        try (var writer = new FileWriter(saveFile.getSelectedFile())){
            BoardSaver.save(writer, gameGUI.engine, gameGUI, vsPC);
        } catch (IOException ignore){
            JOptionPane.showMessageDialog(null,
                    "Error!",
                    "Something goes wrong!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
