package GUI.GameGUI.ImageGUI;

import javax.swing.*;

/**
 * Class of boardGUI
 */
public class BoardGUI extends JPanel {
    public ImageGUI imageGUI; // ImageGUI of board

    /**
     * Default constructor
     * @param shiftX x in frame coordinates
     * @param shiftY y in frame coordinates
     * @param width width
     * @param height height
     */
    public BoardGUI(int shiftX, int shiftY, int width, int height){
        imageGUI = new ImageGUI(shiftX, shiftY, width, height, "src/main/resources/Board.png");
    }
}
