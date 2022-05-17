package GUI;

import GUI.GameGUI.ImageGUI.BoardGUI;
import GUI.GameGUI.ImageGUI.PieceGUI;
import Game.Pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class of ViewerGUI
 * It contains buttons: Next, Previous, Back
 */
public class ViewerGUI extends JPanel {
    private BoardGUI boardGUI; // BoardGUI
    public JButton nextButton; // Next button
    public JButton previousButton; // Previous button
    public JButton backButton; // Back to menu button
    public JLabel currentMoveLabel; // Current move label
    public ArrayList<Piece[][]> boards; // Boards for any stage of game
    public ArrayList<String> moveAsStrings; // Moves as string
    public int currentI; // Index of current board
    private PieceGUI[][] pieceGUIS; // PieceGUIs which represent board
    private int shiftX; // Initial x's shift
    private int shiftY; // Initial y's shift
    private int widthOfBoard; // Width of board
    private int heightOfBoard; // Height of board

    /**
     * Default game constructor
     * @param width width of main frame
     * @param height height of main frame
     * @param boards boards
     * @param moveAsStrings all moves
     */
    public ViewerGUI(int width, int height, ArrayList<Piece[][]> boards, ArrayList<String> moveAsStrings){
        this.boards = boards;
        this.moveAsStrings = new ArrayList<>(moveAsStrings);
        this.moveAsStrings.add(0, "Initial moves");
        this.currentI = 0;
        pieceGUIS = new PieceGUI[8][8];

        init(width, height);

        add(nextButton);
        add(previousButton);
        add(backButton);
        add(boardGUI.imageGUI);
        setLayout(null);
        setVisible(true);
    }

    /**
     * The methods draws current board and updates currentMoveLabel using given index
     * @param i index of current board
     */
    public void drawBoardAndMove(int i){
        var board = boards.get(i);
        currentMoveLabel.setText(moveAsStrings.get(i));

        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                if (board[j][k] != null){
                    pieceGUIS[j][k] = new PieceGUI(board[j][k], shiftX, shiftY,
                            (double)widthOfBoard / 8, (double)heightOfBoard / 8);
                    add(pieceGUIS[j][k].imageGUI, getComponentCount() - 1);
                } else {
                    pieceGUIS[j][k] = null;
                }
            }
        }
        repaint();
    }

    /**
     * The methods clear board using given index
     * @param i index of the board
     */
    public void clearBoard(int i){
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                if (pieceGUIS[j][k] != null){
                    remove(pieceGUIS[j][k].imageGUI);
                    pieceGUIS[j][k] = null;
                }
            }
        }
        repaint();
    }

    /**
     * The method inits components
     * @param width width of main frame
     * @param height height of main frame
     */
    private void init(int width, int height){
        shiftX = (int)(0.09 * width);
        shiftY = (int)(0.15 * height);
        widthOfBoard  = (int)(0.8 * width);
        heightOfBoard  = (int)(0.8 * height);
        boardGUI = new BoardGUI(shiftX, shiftY, widthOfBoard, heightOfBoard);
        nextButton = new JButton("Next");
        previousButton = new JButton("Previous");
        backButton = new JButton("Back");
        previousButton.setEnabled(false);

        nextButton.setBounds(shiftX, (int)(0.02 * height), (int)(0.19 * widthOfBoard), (int)(0.11 * height));
        previousButton.setBounds(shiftX + (int)(0.27 * widthOfBoard), (int)(0.02 * height), (int)(0.19 * widthOfBoard), (int)(0.11 * height));
        backButton.setBounds(shiftX + (int)(0.54 * widthOfBoard), (int)(0.02 * height), (int)(0.19 * widthOfBoard), (int)(0.11 * height));

        currentMoveLabel = new JLabel();
        var panel = new JPanel(new GridLayout(0, 1, 0, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Current move"));
        panel.setBounds(shiftX + (int)(0.81 * widthOfBoard), (int)(0.02 * height), (int)(0.19 * widthOfBoard), (int)(0.11 * height));
        panel.add(currentMoveLabel);
        add(panel);

        drawBoardAndMove(0);
    }
}
