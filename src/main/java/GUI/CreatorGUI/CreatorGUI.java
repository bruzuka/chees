package GUI.CreatorGUI;

import GUI.GameGUI.ImageGUI.BoardGUI;
import GUI.GameGUI.ImageGUI.PieceGUI;
import Game.Pieces.*;
import Game.Player;
import Listener.CreatorListener.BackToMenuButtonListener;
import Listener.CreatorListener.CreatorListener;
import Listener.CreatorListener.PlayButtonListener;
import MainProgram.MainProgram;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class of CreatorGUI.
 * It contains information about board dimensions and it's position
 * Also gui stores static pieces, usual pieces and butons.
 */
public class CreatorGUI extends JPanel {
    public int widthOfBoard; // Width of board
    public int heightOfBoard; // Height of board
    public int shiftX; // Shift x of board
    public int shiftY; // Shift y of board
    public ArrayList<StaticPieceGUI> staticPieces; // Static pieces
    public ArrayList<PieceGUI> pieces; // Usual pieces on the board
    public JButton backToMenuButton; // Back to menu button
    public JButton playButton; // Play button
    public JRadioButton whiteRadioButton; // White player moves first
    public JRadioButton blackRadioButton; // Black player moves first
    private BoardGUI boardGUI; // BoardGUI
    private JPanel panelRadio; // Panel for choosing first turn

    /**
     * Default constructor
     * @param width width of main frame
     * @param height height of main frame
     */
    public CreatorGUI(int width, int height){
        init(width, height);

        for (StaticPieceGUI staticPieceGUI : staticPieces) {
            add(staticPieceGUI.imageGUI);
        }

        add(playButton);
        add(backToMenuButton);
        add(panelRadio);
        add(boardGUI.imageGUI);
        setLayout(null);
        setVisible(true);
    }

    /**
     * The method inits button
     */
    public void initButton(){
        int heightButton = (int)(0.15 * heightOfBoard);
        int widthButton = (int)(0.25 * widthOfBoard);
        int between = (int)(0.05 * heightOfBoard);
        playButton.setBounds((int)(1.5 * shiftX) + widthOfBoard,
                shiftY + heightOfBoard / 2 - 3 * heightButton / 2 - between,
                widthButton, heightButton);
        backToMenuButton.setBounds((int)(1.5 * shiftX) + widthOfBoard,
                shiftY + heightOfBoard / 2 - heightButton / 2,
                widthButton, heightButton);
    }

    /**
     * The method inits choosing box
     */
    public void initChoosingBox(){
        int heightPanel = (int)(0.15 * heightOfBoard);
        int widthPanel = (int)(0.25 * widthOfBoard);
        int between = (int)(0.05 * heightOfBoard);

        panelRadio.setBorder(BorderFactory.createTitledBorder("First move"));
        var buttonsGroup = new ButtonGroup();
        whiteRadioButton.setSelected(true);
        buttonsGroup.add(whiteRadioButton);
        buttonsGroup.add(blackRadioButton);
        panelRadio.add(whiteRadioButton);
        panelRadio.add(blackRadioButton);

        panelRadio.setBounds((int)(1.5 * shiftX) + widthOfBoard,
                shiftY + heightOfBoard / 2 + heightPanel / 2 + between,
                widthPanel, heightPanel);
        add(panelRadio);
    }

    /**
     * The method adds new piece to the board and to the collection
     * @param piece new piece
     */
    public void addNewPiece(Piece piece){
        var newPieceGUI = new PieceGUI(Piece.copyPieceWithReinitialization(piece), shiftX, shiftY,
                (double)widthOfBoard / 8, (double)heightOfBoard / 8);
        pieces.add(newPieceGUI);
        add(newPieceGUI.imageGUI, getComponentCount() - 2);
    }

    /**
     * The method removes piece from the board and from collection using peaceGUI as container
     * @param pieceGUI peaceGUI
     */
    public void removePiece(PieceGUI pieceGUI){
        pieces.remove(pieceGUI);
        remove(pieceGUI.imageGUI);
    }

    /**
     * The method adds static peace to the board via staticPieceGUI
     * @param staticPieceGUI staticPieceGUI
     */
    public void addTemporaryStaticPieceGUI(StaticPieceGUI staticPieceGUI){
        add(staticPieceGUI.imageGUI,getComponentCount() - 2);
    }

    /**
     * The method removes static peace from the board via staticPieceGUI
     * @param staticPieceGUI staticPieceGUI
     */
    public void removeTemporaryStaticPieceGUI(StaticPieceGUI staticPieceGUI){
        remove(staticPieceGUI.imageGUI);
    }

    /**
     * The method check validity of the current board
     * It looks for multiple one color kings or
     * find that there is no king of particular color
     * @return validity of board
     */
    public boolean isValidBoard(){
        int blackKings = 0;
        int whiteKings = 0;

        for (PieceGUI pieceGUI : pieces) {
            if (pieceGUI.piece instanceof King){
                if (pieceGUI.piece.player == Player.WHITE){
                    whiteKings++;
                } else{
                    blackKings++;
                }
            }
        }

        return (blackKings == 1) && (whiteKings == 1);
    }

    /**
     * The method adds listener to gui
     * @param mainProgram main program
     */
    public void addListener(MainProgram mainProgram){
        var creatorListener = new CreatorListener(this);
        addMouseListener(creatorListener);
        addMouseMotionListener(creatorListener);
        playButton.addActionListener(new PlayButtonListener(mainProgram));
        backToMenuButton.addActionListener(new BackToMenuButtonListener(mainProgram.menuGUI, mainProgram.mainFrame));
    }

    /**
     * The method inits components
     * @param width width of main frame
     * @param height height of main frame
     */
    private void init(int width, int height){
        staticPieces = new ArrayList<>();
        pieces = new ArrayList<>();
        playButton = new JButton("Play");
        backToMenuButton = new JButton("Back");
        panelRadio = new JPanel(new GridLayout(0, 1, 0, 5));
        whiteRadioButton = new JRadioButton("White");
        blackRadioButton = new JRadioButton("Black");

        shiftX = (int)(0.05 * width);
        shiftY = (int)(0.15 * width);
        widthOfBoard = (int)(0.7 * width);
        heightOfBoard = (int)(0.7 * height);
        boardGUI = new BoardGUI(shiftX, shiftY, widthOfBoard, heightOfBoard);

        initStaticPieces(Player.WHITE);
        initStaticPieces(Player.BLACK);
        initButton();
        initChoosingBox();
    }

    /**
     * The method inits static pieces
     * @param player current player
     */
    private void initStaticPieces(Player player){
        int specialShiftY = ((player == Player.WHITE) ? shiftY + heightOfBoard : shiftY - heightOfBoard / 8);

        staticPieces.add(new StaticPieceGUI(new Pawn(0, 0, player),
                shiftX, specialShiftY, widthOfBoard / 8, heightOfBoard / 8));
        staticPieces.add(new StaticPieceGUI(new Rook(0, 0, player),
                shiftX + (widthOfBoard / 8), specialShiftY, widthOfBoard / 8, heightOfBoard / 8));
        staticPieces.add(new StaticPieceGUI(new Bishop(0, 0, player),
                shiftX + (2 * widthOfBoard / 8), specialShiftY, widthOfBoard / 8, heightOfBoard / 8));
        staticPieces.add(new StaticPieceGUI(new Knight(0, 0, player),
                shiftX + (3 * widthOfBoard / 8), specialShiftY, widthOfBoard / 8, heightOfBoard / 8));
        staticPieces.add(new StaticPieceGUI(new Queen(0, 0, player),
                shiftX + (4 * widthOfBoard / 8), specialShiftY, widthOfBoard / 8, heightOfBoard / 8));
        staticPieces.add(new StaticPieceGUI(new King(0, 0, player),
                shiftX + (5 * widthOfBoard / 8), specialShiftY, widthOfBoard / 8, heightOfBoard / 8));
    }
}
