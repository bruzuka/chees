package GUI.GameGUI;

import GUI.GameGUI.ImageGUI.BoardGUI;
import GUI.GameGUI.ImageGUI.MarkedCellGUI;
import GUI.GameGUI.ImageGUI.PieceGUI;
import GUI.GameGUI.ImageGUI.PieceOutGUI;
import Game.ChessEngine;
import Game.Pieces.King;
import Game.Pieces.Knight;
import Game.Pieces.Pawn;
import Game.Pieces.Piece;
import Game.Player;
import Game.Position;
import Listener.GameListener.BackToMenuButtonListener;
import Listener.GameListener.GameListener;
import Listener.GameListener.SaveButtonListener;
import MainProgram.MainProgram;
import Util.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class of gameGUI
 * It contains boardGUI, pieceGUIs, pieceOutGUIs, markedCellGUIs and buttons
 * Also gameGUI stores chess engine as reference
 */
public class GameGUI extends JPanel {
    public BoardGUI boardGUI; // BoardGUI
    public MarkedCellGUI[][][] markedCellGUIs; // MarkedCellGUIs
    public ArrayList<PieceGUI> pieces; // All pieceGUIs
    public ArrayList<PieceOutGUI> blackPiecesOut; // Died black pieces as pieceOutGUIs
    public ArrayList<PieceOutGUI> whitePiecesOut; // Died white pieces as pieceOutGUIs
    public ChessEngine engine; // Chess engine
    public int heightOfBoard; // Height of board
    public int widthOfBoard; // Width of board
    public int shiftX; // initial x's shift of board
    public int shiftY; // initial y's shift of board

    public JTextArea movesHistoryText; // History of moves
    public JButton saveButton; // Save button
    public JButton backToMenu; // Back to menu button
    public JLabel blackTime; // Black time as JLable
    public JLabel whiteTime; // White time as JLabel
    public JLabel resultLabel; // Result label
    public boolean isMoveMade; // Flag for making moves
    public Player currentPlayer; // Current player that controls gui
    public Move currentMove; // Current move
    private JScrollPane movesHistory; // Panel for correct showing of history

    /**
     * Default constructor
     * @param width width of main frame
     * @param height height of main frame
     * @param engine chess engine
     * @param move current move
     */
    public GameGUI(int width, int height, ChessEngine engine, Move move){
        this.engine = engine;
        this.currentMove = move;
        isMoveMade = false;
        currentPlayer = engine.firstPlayer;

        init(width, height);
        initMarkedCells();

        for (PieceGUI pieceGUI : pieces) {
            add(pieceGUI.imageGUI);
        }

        add(movesHistory);
        add(backToMenu);
        add(saveButton);
        add(blackTime);
        add(whiteTime);
        add(boardGUI.imageGUI, -1); // board GUI must be on the bottom of components
        setLayout(null);
        setVisible(true);
    }

    /**
     * The method updates black list of pieceOutGUI
     */
    public void updateBlackPiecesOut(){
        int n = Math.min(engine.getBlackPiecesOut().size(), 16); // number of components for drawing must be less or equal 16
        for (int i = blackPiecesOut.size(); i < n; i++) {
            var newPieceOutGUI = new PieceOutGUI(engine.getBlackPiecesOut().get(i), shiftX, shiftY - heightOfBoard / 16,
                    (double)widthOfBoard / 16, (double)heightOfBoard / 16, i);
            blackPiecesOut.add(newPieceOutGUI);
            add(newPieceOutGUI.imageGUI);
            repaint();
        }
    }

    /**
     * The method updates white list of pieceOutGUI
     */
    public void updateWhitePiecesOut(){
        int n = Math.min(engine.getWhitePiecesOut().size(), 16); // number of components for drawing must be less or equal 16
        for (int i = whitePiecesOut.size(); i < n; i++) {
            var newPieceOutGUI = new PieceOutGUI(engine.getWhitePiecesOut().get(i), shiftX, shiftY + heightOfBoard,
                    (double)widthOfBoard / 16, (double)heightOfBoard / 16, i);
            whitePiecesOut.add(newPieceOutGUI);
            add(newPieceOutGUI.imageGUI);
            repaint();
        }
    }

    /**
     * The method updates pieceGUIs
     * @param initiator piece that initiates updating
     * @param attachedPiece piece that attached to first one
     */
    public void updateBoard(Piece initiator, Piece attachedPiece){
        PieceGUI removedPieceGUI = null;
        for (PieceGUI pieceGUI : pieces) {
            if (attachedPiece != pieceGUI.piece || !shouldDeletePiece(initiator, attachedPiece)){ // Here is checking for deleting
                pieceGUI.toRealCoordinates(pieceGUI.piece.pos);
            } else {
                removedPieceGUI = pieceGUI;
            }
        }

        if (removedPieceGUI != null){
            pieces.remove(removedPieceGUI);
            remove(removedPieceGUI.imageGUI);
        }

        repaint();
    }

    /**
     * The method restores positions of all pieceGUIs
     */
    public void placeAllPiecesBack(){
        for (PieceGUI pieceGUI : pieces) {
            pieceGUI.toRealCoordinates(pieceGUI.piece.pos);
        }
    }

    /**
     * The method implements replacing pawn
     * @param oldPiece pawn that will be replaced
     * @param newPiece piece that will replace pawn
     */
    public void introduceNewPiece(Piece oldPiece, Piece newPiece){
        for (PieceGUI pieceGUI : pieces) {
            if (oldPiece == pieceGUI.piece ){
                pieces.remove(pieceGUI);
                remove(pieceGUI.imageGUI);
                break;
            }
        }

        var newPieceGUI = new PieceGUI(newPiece, shiftX, shiftY,
                (double)widthOfBoard / 8, (double)heightOfBoard / 8);
        pieces.add(newPieceGUI);
        add(newPieceGUI.imageGUI, 0);
    }

    /**
     * The method adds move to the history of moves
     * @param piece piece that was moved
     * @param pieceForPawn special peace for pawn
     * @param from source
     * @param to destination
     * @param wasThereKilled was there killing of piece?
     */
    public void addNewMoveToHistory(Piece piece, Piece pieceForPawn, Position from, Position to, boolean wasThereKilled){
        String currentText = movesHistoryText.getText();

        if (!currentText.isEmpty()){
            if (piece.player == Player.BLACK){
                currentText += " ";
            } else {
                currentText += "\n";
            }
        }

        if (currentText.isEmpty() && piece.player == Player.BLACK){ // Special case for initial turn == Black
            currentText += "1. ";
        }

        if (piece.player == Player.WHITE){
            currentText += (engine.numberOfMoves / 2 + 1) + ". ";
        }

        currentText = moveToString(currentText, piece, from, to, wasThereKilled);

        if (pieceForPawn != null){
            if (!(pieceForPawn instanceof Knight)){
                currentText += pieceForPawn.getClass().getSimpleName().charAt(0);
            } else {
                currentText += "N";
            }
        }

        if (engine.isCheck(Player.toPlayer(engine.turn)) && !engine.isCheckmate(Player.toPlayer(engine.turn))){
            currentText += "+";
        }

        movesHistoryText.setText(currentText);
    }

    /**
     * The method makes all marked cells invisible
     */
    public void makeAllCellsInvisible(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                markedCellGUIs[i][j][0].imageGUI.setVisible(false);
                markedCellGUIs[i][j][1].imageGUI.setVisible(false);
            }
        }
    }

    /**
     * The method adds listeners to this panel
     * @param mainProgram main program
     * @param vsPC flag vsPC
     */
    public void addListeners(MainProgram mainProgram, boolean vsPC){
        var gameListener = new GameListener(this, mainProgram.menuGUI, mainProgram.mainFrame);
        addMouseListener(gameListener);
        addMouseMotionListener(gameListener);
        backToMenu.addActionListener(new BackToMenuButtonListener(mainProgram.menuGUI, mainProgram.gameController, mainProgram.mainFrame));
        saveButton.addActionListener(new SaveButtonListener(mainProgram.gameGUI, mainProgram.mainFrame, vsPC));
    }

    /**
     * The method inits components
     * @param width width of main frame
     * @param height height of main frame
     */
    private void init(int width, int height){
        shiftX = (int)(0.05 * width);
        shiftY = (int)(0.2 * height);
        widthOfBoard = (int)(0.7 * width);
        heightOfBoard = (int)(0.7 * height);

        boardGUI = new BoardGUI(shiftX, shiftY, widthOfBoard, heightOfBoard);
        pieces = new ArrayList<>();
        blackPiecesOut = new ArrayList<>();
        whitePiecesOut = new ArrayList<>();

        initText();
        initButton();
        initLabel();

        for (Piece piece : engine.getBlackPieces()) {
            pieces.add(new PieceGUI(piece, shiftX, shiftY, (double)widthOfBoard / 8, (double)heightOfBoard / 8));
        }

        for (Piece piece : engine.getWhitePieces()) {
            pieces.add(new PieceGUI(piece, shiftX, shiftY, (double)widthOfBoard / 8, (double)heightOfBoard / 8));
        }
    }

    /**
     * The method adds marked cell to the panel
     */
    private void addAllCells(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                add(markedCellGUIs[i][j][0].imageGUI);
                add(markedCellGUIs[i][j][1].imageGUI);
            }
        }
    }

    /**
     * The method inits move history
     */
    private void initText(){
        movesHistoryText = new JTextArea();
        movesHistoryText.setFont(new Font("Dialog", Font.PLAIN, 14));
        movesHistoryText.setTabSize(10);
        movesHistoryText.setLineWrap(false);
        movesHistoryText.setEditable(false);
        movesHistory = new JScrollPane(movesHistoryText);
        movesHistory.setBounds((int)(1.5 * shiftX) + widthOfBoard, shiftY, (int)(0.25 * widthOfBoard), heightOfBoard);
    }

    /**
     * The method inits labels
     */
    private void initLabel(){
        blackTime = new JLabel("Black time: --:--", SwingConstants.CENTER);
        whiteTime = new JLabel("White time: --:--", SwingConstants.CENTER);
        resultLabel = new JLabel("", SwingConstants.CENTER);

        blackTime.setFont(new Font(Font.DIALOG,  Font.BOLD, 20));
        whiteTime.setFont(new Font(Font.DIALOG,  Font.BOLD, 20));
        resultLabel.setFont(new Font(Font.DIALOG,  Font.BOLD, 30));

        blackTime.setBounds(shiftX, (int)(0.05 * heightOfBoard), (int)(0.5 * widthOfBoard), (int)(0.1 * heightOfBoard));
        whiteTime.setBounds(shiftX + (int)(0.5 * heightOfBoard), (int)(0.05 * heightOfBoard), (int)(0.5 * widthOfBoard), (int)(0.1 * heightOfBoard));
        resultLabel.setBounds(shiftX, 0, widthOfBoard, shiftY);
    }

    /**
     * The method inits buttons
     */
    private void initButton(){
        backToMenu = new JButton("Menu");
        saveButton = new JButton("Save");

        backToMenu.setBounds((int)(1.5 * shiftX) + widthOfBoard, (int)(0.05 * heightOfBoard),
                (int)(0.25 * widthOfBoard), (int)(0.1 * heightOfBoard));
        saveButton.setBounds((int)(1.5 * shiftX) + widthOfBoard, (int)(0.15 * heightOfBoard),
                (int)(0.25 * widthOfBoard), (int)(0.1 * heightOfBoard));
    }

    /**
     * The method inits marked cells and makes them invisible
     */
    private void initMarkedCells(){
        markedCellGUIs = new MarkedCellGUI[8][8][2];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                markedCellGUIs[i][j][0] = new MarkedCellGUI(shiftX, shiftY, j, i,
                        (double)widthOfBoard / 8, (double)heightOfBoard / 8, true);
                markedCellGUIs[i][j][1] = new MarkedCellGUI(shiftX, shiftY, j, i,
                        (double)widthOfBoard / 8, (double)heightOfBoard / 8, false);
            }
        }

        makeAllCellsInvisible();
        addAllCells();
    }

    /**
     * The method checks that this piece must be deleted
     * @param initiator piece that initiate checking
     * @param attachedPiece attached piece
     * @return should this piece be deleted
     */
    private boolean shouldDeletePiece(Piece initiator, Piece attachedPiece){
        return attachedPiece != null && initiator.player != attachedPiece.player;
    }

    /**
     * The method translates move to string
     * @See Chess move notation
     * @param previous
     * @param piece
     * @param from
     * @param to
     * @param wasThereKilled
     * @return
     */
    private String moveToString(String previous, Piece piece, Position from, Position to, boolean wasThereKilled){
        if (from.x - to.x > 1 && piece instanceof King){
            return previous + "0-0-0";
        } else if (to.x - from.x > 1 && piece instanceof King){
            return previous + "0-0";
        } else {
            if (!(piece instanceof Pawn)){
                if (!(piece instanceof Knight)){
                    previous += piece.getClass().getSimpleName().charAt(0);
                } else {
                    previous += "N";
                }
            }

            return previous + (char)(from.x + 'a') + "" + (from.y + 1) + "" +
                    (wasThereKilled ? "x" : "-") + (char)(to.x + 'a') + "" + (to.y + 1);
        }
    }
}
