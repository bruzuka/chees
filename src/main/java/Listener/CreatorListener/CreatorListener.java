package Listener.CreatorListener;

import GUI.CreatorGUI.CreatorGUI;
import GUI.CreatorGUI.StaticPieceGUI;
import GUI.GameGUI.ImageGUI.PieceGUI;
import Game.Pieces.Piece;
import Game.Position;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Mouse listener for Creator GUI
 */
public class CreatorListener extends MouseAdapter implements MouseMotionListener {
    private CreatorGUI creatorGUI; // Creator GUI
    private StaticPieceGUI currentStaticPiece; // Current static peace
    private PieceGUI currentPiece; // Current usual peace

    /**
     * Default constructor
     * @param creatorGUI creatorGUI
     */
    public CreatorListener(CreatorGUI creatorGUI) {
        this.creatorGUI = creatorGUI;
        currentStaticPiece = null;
        currentPiece = null;
    }

    /**
     * THe method implements mouse pressed event
     * @param e action
     */
    @Override
    public void mousePressed(MouseEvent e) {
        for (StaticPieceGUI staticPieceGUI : creatorGUI.staticPieces) {
            if (staticPieceGUI.isMouseOnPiece(e.getX(), e.getY())){ // Finding pressed static piece
                currentStaticPiece = new StaticPieceGUI(staticPieceGUI.piece, staticPieceGUI.realX, staticPieceGUI.realY,
                        creatorGUI.widthOfBoard / 8, creatorGUI.heightOfBoard / 8);
                creatorGUI.addTemporaryStaticPieceGUI(currentStaticPiece); // Add temporary static piece
                creatorGUI.repaint();
                return;
            }
        }

        for (PieceGUI pieceGUI : creatorGUI.pieces){
            if (pieceGUI.isMouseOnPiece(e.getX(), e.getY())){ // Finding pressed usual piece
                currentPiece = pieceGUI;
                break;
            }
        }

        creatorGUI.repaint();
    }

    /**
     * The method implements mouse released event
     * @param e action
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        pieceReleased(e.getX(), e.getY());
        creatorGUI.repaint();
    }

    /**
     * The method implements mouse dragged event
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        movePiece(e.getX(), e.getY());
        creatorGUI.repaint();
    }

    /**
     * The method moves pressed piece
     * @param x x coordinate of cursor
     * @param y y coordinate of cursor
     */
    private void movePiece(int x, int y){
        if (currentStaticPiece != null){
            currentStaticPiece.realX = x - currentStaticPiece.width / 2;
            currentStaticPiece.realY = y - currentStaticPiece.height / 2;
            currentStaticPiece.imageGUI.setBounds(currentStaticPiece.realX, currentStaticPiece.realY,
                    currentStaticPiece.width, currentStaticPiece.height);
            return;
        }
        if (currentPiece != null){
            currentPiece.realX = x - currentPiece.width / 2;
            currentPiece.realY = y - currentPiece.height / 2;
            currentPiece.imageGUI.setBounds((int)currentPiece.realX, (int)currentPiece.realY,
                    (int)currentPiece.width, (int)currentPiece.height);
        }
    }

    /**
     * The method releases pressed piece
     * @param x x coordinate of cursor
     * @param y y coordinate of cursor
     */
    private void pieceReleased(int x, int y){
        var newPosition = toBoardCoordinates(x, y);
        if (currentStaticPiece != null){
            if (!positionIsOccupied(newPosition)){ // Adding to the board condition
                currentStaticPiece.piece.pos = newPosition;
                creatorGUI.addNewPiece(currentStaticPiece.piece);
            }
            creatorGUI.removeTemporaryStaticPieceGUI(currentStaticPiece); // Remove static piece from temporary
            currentStaticPiece = null;
            return;
        }

        if (currentPiece != null){
            if (newPosition != null){ // Replacing condition
                if (!positionIsOccupied(newPosition)){ // Resolving collisions
                    currentPiece.piece.pos = newPosition;
                    currentPiece.piece = Piece.copyPieceWithReinitialization(currentPiece.piece);
                }
                currentPiece.toRealCoordinates(currentPiece.piece.pos);
            } else {
                creatorGUI.removePiece(currentPiece);
            }
            currentPiece = null;
        }
    }

    /**
     * The method verifies that cursor on the board
     * @param realX x coordinate of cursor
     * @param realY y coordinate of cursor
     * @return result of verifying
     */
    private Position toBoardCoordinates(int realX, int realY){
        if (realX >= creatorGUI.shiftX && realX <= creatorGUI.shiftX + creatorGUI.widthOfBoard &&
                realY >= creatorGUI.shiftY && realY <= creatorGUI.shiftY + creatorGUI.heightOfBoard){
            int boardX = (realX - creatorGUI.shiftX) / (creatorGUI.widthOfBoard / 8);
            int boardY = (realY - creatorGUI.shiftY) / (creatorGUI.heightOfBoard / 8);
            boardY = 7 - boardY;
            return new Position(boardX, boardY);
        }
        return null;
    }

    /**
     * The method checks that given position empty
     * @param position position
     * @return is position occupied
     */
    private boolean positionIsOccupied(Position position){
        if (position != null){
            for (PieceGUI pieceGUI : creatorGUI.pieces) {
                if (pieceGUI.piece.pos.equals(position)){
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }
}
