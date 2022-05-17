package GUI.CreatorGUI;

import GUI.GameGUI.ImageGUI.ImageGUI;
import Game.Pieces.Piece;

/**
 * Class of StaticPieceGUI
 * Location of this component does not depend from the piece
 */
public class StaticPieceGUI {
    public ImageGUI imageGUI; //ImageGUI
    public Piece piece; //Piece
    public int realX; //Real x coordinate on th frame
    public int realY; //Real y coordinate on the frame
    public boolean pressed; //Is the mouse pressed on it?
    public int height; //Height of GUI
    public int width; //Width of GUI
    private final int staticX; //Static x coordinate
    private final int staticY; //Static y coordinate

    /**
     * Default constructor
     * @param piece piece
     * @param realX
     * @param realY
     * @param height
     * @param width
     */
    public StaticPieceGUI(Piece piece, int realX, int realY, int height, int width) {
        this.piece = piece;
        this.realX = realX;
        this.realY = realY;
        this.height = height;
        this.width = width;
        staticX = realX;
        staticY = realY;
        pressed = false;

        imageGUI = new ImageGUI(realX, realY, width, height, piece.pathToImage);
    }

    /**
     * The method restores position of this gui.
     * It means: realX <- staticX, realY <- staticY
     */
    public void restorePosition(){
        realX = staticX;
        realY = staticY;
        imageGUI.setBounds(staticX, staticY, width, height);
    }

    /**
     * The method checks that the cursor on the gui
     * @param x x coordinate of cursor
     * @param y y coordinate of cursor
     * @return is mouse on piece
     */
    public boolean isMouseOnPiece(int x, int y){
        return realX <= x && x <= realX + width && realY <= y && y <= realY + height;
    }
}
