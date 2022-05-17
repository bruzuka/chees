package GUI.GameGUI.ImageGUI;

import Game.Pieces.Piece;
import Game.Position;

/**
 * Class of PieceGUI
 * It differs from the static one that it recalculates real position
 */
public class PieceGUI {
    public ImageGUI imageGUI; //ImageGUI
    public Piece piece; // Piece which gui draws
    public double realX; // x in frame coordinates
    public double realY; // y in frame coordinates
    public boolean pressed; // Is the mouse pressed on it?
    public double height; // height of gui
    public double width; // width of gui
    private int shiftX; // initial x shift
    private int shiftY; // initial y shift

    /**
     * Default constructor
     * @param piece piece which gui will draw
     * @param shiftX initial shift of x
     * @param shiftY initial shift of y
     * @param width width of gui
     * @param height height of gui
     */
    public PieceGUI(Piece piece, int shiftX, int shiftY, double width, double height){
        this.width = width;
        this.height = height;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.piece = piece;
        pressed = false;
        realX = piece.pos.x * width + shiftX;
        realY = (7 - piece.pos.y) * height + shiftY;

        imageGUI = new ImageGUI((int)realX, (int)realY, (int)width, (int)height, piece.pathToImage);
    }

    /**
     * The method translates chess coordinates to real one
     * @param x x in chess coordinates
     * @param y y in chess coordinates
     */
    public void toRealCoordinates(int x, int y){
        realX = x * width + shiftX;
        realY = (7 - y) * height + shiftY;

        imageGUI.setBounds((int)realX, (int)realY, (int)width, (int)height);
    }

    /**
     * The method translates chess coordinates to real one
     * @param pos position
     */
    public void toRealCoordinates(Position pos){
        realX = pos.x * width + shiftX;
        realY = (7 - pos.y) * height + shiftY;

        imageGUI.setBounds((int)realX, (int)realY, (int)width, (int)height);
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
