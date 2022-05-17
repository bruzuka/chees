package GUI;

import Game.ChessEngine;
import Game.Player;
import Listener.PreGameListener.BackToMenuButtonListener;
import Listener.PreGameListener.PlayButtonListener;
import MainProgram.MainProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class of pre game GUI
 * It contains buttons: play, back, buttons for selection
 */
public class PreGameGUI extends JPanel {
    public JButton selectColorButton; // Select color button
    public JButton selectPlayerButton; // Select player button (VS player or VS PC)
    public JButton enableTimerButton; // Enable timer button
    public JButton backToMenuButton; // Back to menu button
    public JButton playButton; // Play button
    public ChessEngine engine; // Chess engine
    private JLabel selectColorLabel; // Select color label
    private JLabel selectPlayerLabel; // Select player label
    private JLabel enableTimerLabel; // Enable timer label


    /**
     * @param width width of main frame
     * @param height height of main frame
     * @param engine chess engine
     */
    public PreGameGUI(int width, int height, ChessEngine engine){
        int centerX = (int)(0.5 * width);
        int centerY = (int)(0.4925 * height);
        this.engine = engine;
        initButton(width, height, centerX, centerY);
        initLabels(width, height, centerX, centerY);

        add(selectColorButton);
        add(selectPlayerButton);
        add(enableTimerButton);
        add(backToMenuButton);
        add(playButton);

        add(selectColorLabel);
        add(selectPlayerLabel);
        add(enableTimerLabel);

        setLayout(null);
        setBounds(0, 0, width, height);
        setVisible(true);
    }

    /**
     * The method adds listener to the panel
     * @param mainProgram main program
     */
    public void addListeners(MainProgram mainProgram){
        backToMenuButton.addActionListener(new BackToMenuButtonListener(mainProgram.menuGUI, mainProgram.mainFrame));
        playButton.addActionListener(new PlayButtonListener(mainProgram));

        // All listeners bellow are simple
        selectColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectColorButton.getText() == Player.WHITE.toString()){
                    selectColorButton.setText(Player.BLACK.toString());
                } else {
                    selectColorButton.setText(Player.WHITE.toString());
                }
                repaint();
            }
        });

        selectPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectPlayerButton.getText() == "Vs Player"){
                    selectPlayerButton.setText("Vs PC");
                    selectColorButton.setEnabled(true); // Enable selection of color, when vs PC flag is raised
                } else {
                    selectPlayerButton.setText("Vs Player");
                    selectColorButton.setEnabled(false); // Disable selection of color, when vs PC flag is false
                }
                repaint();
            }
        });

        enableTimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enableTimerButton.getText() == "Enabled"){
                    enableTimerButton.setText("Disabled");
                } else {
                    enableTimerButton.setText("Enabled");
                }
                repaint();
            }
        });
    }

    /**
     * The method inits buttons
     * @param width width of main frame
     * @param height height of main frame
     * @param centerX x coordinate of center
     * @param centerY y coordinate of center
     */
    private void initButton(int width, int height, int centerX, int centerY){
        selectColorButton = new JButton("White");
        selectPlayerButton = new JButton("Vs Player");
        enableTimerButton = new JButton("Enabled");
        backToMenuButton = new JButton("Back");
        playButton = new JButton("Play");

        int buttonH = (int)(0.15 * height);
        int buttonW = (int)(0.4 * width);
        int betweenB = (int)(0.05 * height);
        int shiftX = (int)(0.025 * width);

        selectColorButton.setBounds(centerX + shiftX, centerY - 3 * betweenB / 2 - 2 * buttonH, buttonW, buttonH);
        selectPlayerButton.setBounds(centerX + shiftX, centerY - betweenB / 2 - buttonH, buttonW, buttonH);
        enableTimerButton.setBounds(centerX + shiftX, centerY + betweenB / 2, buttonW, buttonH);
        backToMenuButton.setBounds(centerX + shiftX, centerY + 3 * betweenB / 2 + buttonH, buttonW, buttonH);
        playButton.setBounds(centerX - buttonW - shiftX, centerY + 3 * betweenB / 2 + buttonH, buttonW, buttonH);

        selectColorButton.setEnabled(false);
    }

    /**
     * The methods inits labels
     * @param width width of main frame
     * @param height height of main frame
     * @param centerX x coordinate of center
     * @param centerY y coordinate of center
     */
    private void initLabels(int width, int height, int centerX, int centerY){
        Font f = new javax.swing.plaf.FontUIResource("Label.font",Font.BOLD,25);

        selectColorLabel = new JLabel("Select Color: ");
        selectPlayerLabel = new JLabel("Select opponent: ");
        enableTimerLabel = new JLabel("Enable Timer: ");

        selectColorLabel.setFont(f);
        selectPlayerLabel.setFont(f);
        enableTimerLabel.setFont(f);

        int labelH = (int)(0.15 * height);
        int labelW = (int)(0.4 * width);
        int betweenB = (int)(0.05 * height);
        int shiftX = (int)(0.025 * width);

        selectColorLabel.setBounds(centerX - labelW - shiftX, centerY - 3 * betweenB / 2 - 2 * labelH, labelW, labelH);
        selectPlayerLabel.setBounds(centerX - labelW - shiftX, centerY - betweenB / 2 - labelH, labelW, labelH);
        enableTimerLabel.setBounds(centerX - labelW - shiftX, centerY + betweenB / 2, labelW, labelH);
    }
}
