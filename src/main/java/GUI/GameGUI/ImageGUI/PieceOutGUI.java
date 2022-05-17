package GUI.GameGUI.ImageGUI;

import Game.Pieces.Piece;

/**
 * Class of piecesOutGUI
 * It describes died pieces
 */
public class PieceOutGUI {
    public ImageGUI imageGUI; // ImageGUI

    /**
     * Default constructor
     * @param piece died piece
     * @param shiftX initial shift of x
     * @param shiftY initial shift of y
     * @param width width of component
     * @param height height of component
     * @param numberOfDied number of died piece with the same color
     */
    public PieceOutGUI(Piece piece, int shiftX, int shiftY, double width, double height, int numberOfDied){
        int realX = (int)(shiftX + numberOfDied * width);

        imageGUI = new ImageGUI(realX, shiftY, (int)width, (int)height, piece.pathToImage); // It draws died pieces on the same line
    }
}
