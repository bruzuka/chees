package GUI;

import javax.swing.*;

/**
 * Class of chose menu GUI
 * It contains buttons: default game, custom game, back to menu
 */
public class ChooseMenuGUI extends JPanel {
    public JButton defaultGameButton; // Default game button
    public JButton customGameButton; // Custom game button
    public JButton backToMenuButton; // Back to menu button

    /**
     * Default constructor
     * @param width width of main frame
     * @param height height of main frame
     */
    public ChooseMenuGUI(int width, int height){
        defaultGameButton = new JButton("Default Game");
        customGameButton = new JButton("Custom Game");
        backToMenuButton = new JButton("Back");

        int centerX = (int)(0.5 * width);
        int centerY = (int)(0.4925 * height);
        int buttonH = (int)(0.17 * height);
        int buttonW = (int)(0.5 * width);
        int betweenB = (int)(0.045 * height);

        defaultGameButton.setBounds(centerX - buttonW / 2, centerY - 3 * buttonH / 2 - betweenB, buttonW, buttonH);
        customGameButton.setBounds(centerX - buttonW / 2, centerY - buttonH / 2, buttonW, buttonH);
        backToMenuButton.setBounds(centerX - buttonW / 2, centerY + buttonH / 2 + betweenB, buttonW, buttonH);

        add(defaultGameButton);
        add(customGameButton);
        add(backToMenuButton);
        setLayout(null);
        setBounds(0, 0, width, height);
        setVisible(true);
    }
}
