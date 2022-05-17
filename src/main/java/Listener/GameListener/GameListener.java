package Listener.GameListener;

import GUI.GameGUI.GameGUI;
import GUI.GameGUI.ImageGUI.PieceGUI;
import GUI.MainMenuGUI;
import Game.Pieces.Piece;
import Game.Position;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

/**
 * MouseListener for Game GUI
 */
public class GameListener extends MouseAdapter implements MouseMotionListener {
    private GameGUI gameGUI; // Game GUI
    private MainMenuGUI menuGUI; // Main menu GUI
    private JFrame mainFrame; // Main Frame

    /**
     * Default constructor
     * @param gameGUI game GUI
     * @param menuGUI main menu GUI
     * @param mainFrame main frame
     */
    public GameListener(GameGUI gameGUI, MainMenuGUI menuGUI, JFrame mainFrame){
        this.gameGUI = gameGUI;
        this.menuGUI = menuGUI;
        this.mainFrame = mainFrame;
    }

    /**
     * The method implements mouse pressed event
     * @param e action
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameGUI.isMoveMade){ // Try to find pressed piece
            for (PieceGUI pieceGUI : gameGUI.pieces) {
                // Searching pressed piece
                if (pieceGUI.isMouseOnPiece(e.getX(), e.getY()) && gameGUI.currentPlayer == pieceGUI.piece.player){
                    pieceGUI.pressed = true;
                    break;
                }
            }
        }
    }

    /**
     * The method implements mouse released action
     * @param e action
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameGUI.isMoveMade){
            synchronized (gameGUI.currentMove){ // Wait until move object is locked
                for (PieceGUI pieceGUI : gameGUI.pieces) {
                    if (pieceGUI.pressed){
                        pieceGUI.pressed = false;
                        gameGUI.makeAllCellsInvisible();
                        var newPosition = findPosition(pieceGUI, e.getX(), e.getY());

                        if (newPosition != null){ // If finding valid position is successful
                            pieceGUI.toRealCoordinates(newPosition);
                            gameGUI.currentMove.to = newPosition;
                            gameGUI.currentMove.piece = pieceGUI.piece;
                            gameGUI.isMoveMade = true;
                            gameGUI.currentMove.notifyAll(); // notify game controller
                        } else {
                            pieceGUI.toRealCoordinates(pieceGUI.piece.pos);
                        }

                        gameGUI.repaint();
                        break;
                    }
                }
            }
        }
    }

    /**
     * The method implements mouse dragged event
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (!gameGUI.isMoveMade){
            for (PieceGUI pieceGUI : gameGUI.pieces) {
                // Moves piece
                if (pieceGUI.pressed){
                    pieceGUI.realX = e.getX() - pieceGUI.width / 2;
                    pieceGUI.realY = e.getY() - pieceGUI.height / 2;
                    pieceGUI.imageGUI.setBounds((int)pieceGUI.realX, (int)pieceGUI.realY, (int)pieceGUI.width, (int)pieceGUI.width);
                    gameGUI.repaint();
                    break;
                }
            }
        }
    }

    /**
     * The method implements mouse moved event
     * @param e action
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameGUI.isMoveMade){
            gameGUI.makeAllCellsInvisible();

            //Showing possible moves
            for (PieceGUI pieceGUI : gameGUI.pieces) {
                if (pieceGUI.isMouseOnPiece(e.getX(), e.getY()) && gameGUI.currentPlayer == pieceGUI.piece.player){
                    enableCells(gameGUI.engine.getPossiblePosition(pieceGUI.piece), pieceGUI.piece);
                }
            }

            gameGUI.repaint();
        }
    }

    /**
     * The method activates marked cells which positions belongs of the list
     * @param positions list of positions
     * @param piece current piece
     */
    private void enableCells(List<Position> positions, Piece piece){
        for (Position pos : positions) {
            int factor = gameGUI.engine.isThereKill(piece, pos) ? 1 : 0; // Looking for possible killings
            gameGUI.markedCellGUIs[pos.y][pos.x][factor].imageGUI.setVisible(true);
        }
    }

    /**
     * The method finds position that will attach piece
     * @param pieceGUI pieceGUI as container for piece
     * @param x x coordinate of cursor
     * @param y y coordinate of cursor
     * @return position that will attach piece, if it exists
     */
    private Position findPosition(PieceGUI pieceGUI, int x, int y){
        for (Position pos : gameGUI.engine.getPossiblePosition(pieceGUI.piece)){
            if (gameGUI.markedCellGUIs[pos.y][pos.x][0].isMouseOnPiece(x, y)){
                return pos;
            }
        }
        return null;
    }
}
