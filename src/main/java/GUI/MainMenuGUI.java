package GUI;

import javax.swing.*;

/**
 * Class of Main menu GUI
 * It contains buttons: new game, load game, exit
 */
public class MainMenuGUI extends JPanel {
    public JButton newGameButton; // New game button
    public JButton loadButton; // Load button
    public JButton exitButton; // Exit button

    /**
     * Default constructor
     * @param width width of main frame
     * @param height height of main frame
     */
    public MainMenuGUI( int width, int height){
        newGameButton = new JButton("New Game");
        loadButton = new JButton("Load");
        exitButton = new JButton("Exit");

        int centerX = (int)(0.5 * width);
        int centerY = (int)(0.4925 * height);
        int buttonH = (int)(0.17 * height);
        int buttonW = (int)(0.5 * width);
        int betweenB = (int)(0.045 * height);

        newGameButton.setBounds(centerX - buttonW / 2, centerY - 3 * buttonH / 2 - betweenB, buttonW, buttonH);
        loadButton.setBounds(centerX - buttonW / 2, centerY - buttonH / 2, buttonW, buttonH);
        exitButton.setBounds(centerX - buttonW / 2, centerY + buttonH / 2 + betweenB, buttonW, buttonH);

        add(newGameButton);
        add(loadButton);
        add(exitButton);
        setLayout(null);
        setBounds(0, 0, width, height);
        setVisible(true);
    }
}
