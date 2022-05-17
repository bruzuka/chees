package Game;

import Game.Pieces.*;
import Util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Chess engine class. It contains information about black and white pieces,
 * initial positions, all legal positions for any piece of the current turn.
 * Also chess engine store initial turn, number of moves and first player.
 *
 * Note: First player is always human
 */
public class ChessEngine {
    private ArrayList<Piece> blackPieces; // Black pieces
    private ArrayList<Piece> blackPiecesOut; // Died black pieces
    private ArrayList<Piece> whitePieces; // White pieces
    private ArrayList<Piece> whitePiecesOut; // Died white pieces
    private ArrayList<Piece> initialPieces; // Initial pieces
    private Piece[][] board; // Board as two dimensional array
    private HashMap<Piece, List<Position>> allPossibleMovesForTurn; // Map of moves
    public int numberOfMoves; // Number of moves
    public Turn turn; // Current turn
    public Turn initialTurn; // Initial turn
    public Player firstPlayer; // First player

    /**
     * Parametrized constructor
     * @param whitePieces white pieces
     * @param whitePiecesOut died white pieces
     * @param blackPieces black pieces
     * @param blackPiecesOut died black pieces
     * @param turn current turn
     * @param firstPlayer first player
     */
    public ChessEngine(List<Piece> whitePieces, List<Piece> whitePiecesOut,
                       List<Piece> blackPieces, List<Piece> blackPiecesOut, Turn turn, Player firstPlayer) {
        this.blackPieces = new ArrayList<>(blackPieces);
        this.whitePieces = new ArrayList<>(whitePieces);
        this.blackPiecesOut = new ArrayList<>(blackPiecesOut);
        this.whitePiecesOut = new ArrayList<>(whitePiecesOut);
        this.turn = turn;
        this.firstPlayer = firstPlayer;
        initialPieces = new ArrayList<>();
        initialTurn = turn;
        allPossibleMovesForTurn = new HashMap<>();
        numberOfMoves = 0;

        initBoard();
    }

    /**
     * Default constructor
     */
    public ChessEngine() {
        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
        blackPiecesOut = new ArrayList<>();
        whitePiecesOut = new ArrayList<>();
        initialPieces = new ArrayList<>();
        turn = Turn.WHITE;
        initialTurn = turn;

        for (int i = 0; i < 8; i++) {
            whitePieces.add(new Pawn(i, 1, Player.WHITE));
            blackPieces.add(new Pawn(i, 6, Player.BLACK));
        }

        whitePieces.add(new Rook(0,0, Player.WHITE));
        whitePieces.add(new Knight(1,0, Player.WHITE));
        whitePieces.add(new Bishop(2,0, Player.WHITE));
        whitePieces.add(new Queen(3,0, Player.WHITE));
        whitePieces.add(new King(4,0, Player.WHITE));
        whitePieces.add(new Bishop(5,0, Player.WHITE));
        whitePieces.add(new Knight(6,0, Player.WHITE));
        whitePieces.add(new Rook(7,0, Player.WHITE));

        blackPieces.add(new Rook(0,7, Player.BLACK));
        blackPieces.add(new Knight(1,7, Player.BLACK));
        blackPieces.add(new Bishop(2,7, Player.BLACK));
        blackPieces.add(new Queen(3,7, Player.BLACK));
        blackPieces.add(new King(4,7, Player.BLACK));
        blackPieces.add(new Bishop(5,7, Player.BLACK));
        blackPieces.add(new Knight(6,7, Player.BLACK));
        blackPieces.add(new Rook(7,7, Player.BLACK));

        allPossibleMovesForTurn = new HashMap<>();
        firstPlayer = Player.WHITE;
        initBoard();
    }

    /**
     * The method returns black pieces
     * @return black pieces
     */
    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    /**
     * The method returns died black pieces
     * @return died black pieces
     */
    public List<Piece> getBlackPiecesOut() {
        return blackPiecesOut;
    }

    /**
     * The method returns white pieces
     * @return white pieces
     */
    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    /**
     * The method returns died white pieces
     * @return died white pieces
     */
    public List<Piece> getWhitePiecesOut() {
        return whitePiecesOut;
    }

    /**
     * The method returns chess board
     * @return Chess board
     */
    public Piece[][] getBoard() { return board; }

    /**
     * The method returns initial pieces (They store initial positions and additional fields)
     * @return initial pieces
     */
    public ArrayList<Piece> getInitialPieces() {
        return initialPieces;
    }

    /**
     * The method returns allies for player
     * @param player player for which the method returns allies
     * @return ally pieces
     */
    public List<Piece> getAllies(Player player){
        return (player == Player.WHITE) ? whitePieces : blackPieces;
    }

    /**
     * The method returns enemies for player
     * @param player player for which the method returns enemies
     * @return enemy pieces
     */
    public List<Piece> getEnemies(Player player){
        return (player == Player.WHITE) ? blackPieces : whitePieces;
    }

    /**
     * The method returns died allies for player
     * @param player player for which method returns died allies
     * @return died allies pieces
     */
    public List<Piece> getAlliesOut(Player player){
        return (player == Player.WHITE) ? whitePiecesOut : blackPiecesOut;
    }

    /**
     * The method returns died enemies for player
     * @param player player for which the method returns died enemies
     * @return died enemies pieces
     */
    public List<Piece> getEnemiesOut(Player player){
        return (player == Player.WHITE) ? blackPiecesOut : whitePiecesOut;
    }

    /**
     * The method checks that the king of player was checked
     * @param player player for which the method checks king
     * @return is the king of player checked
     */
    public boolean isCheck(Player player){
        var enemies = getEnemies(player);

        for (Piece enemy : enemies) {
            for (Position pos : enemy.getPossibleMoves(this)) {
                if (pos.equals(kingPosition(player))){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * The method checks that there is checkmate
     * @param player player for which the method checks for checkmate
     * @return is there checkmate
     */
    public boolean isCheckmate(Player player){
        var allies = getAllies(player);

        if (isCheck(player)){
            for (Piece ally : allies) {
                if (!getPossiblePosition(ally).isEmpty()){
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * The method checks that there is stalemate
     * @param player player for which the method checks for stalemate
     * @return is there stalemate
     */
    public boolean isStalemate(Player player){
        var allies = getAllies(player);

        if (!isCheck(player)){
            for (Piece ally : allies) {
                if (!getPossiblePosition(ally).isEmpty()){
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * The method returns all legal moves for particular piece
     * @param piece particular piece
     * @return list of legal moves
     */
    public List<Position> getPossiblePosition(Piece piece){
        if (!allPossibleMovesForTurn.containsKey(piece)){
            var possibleMoves = new ArrayList<Position>();
            var allLegalPosition = piece.getPossibleMoves(this);

            for (Position pos : allLegalPosition) {
                if (!checkValidityOfPosition(piece, pos)){
                    possibleMoves.add(pos);
                }
            }
            allPossibleMovesForTurn.put(piece, possibleMoves);
        }
        return allPossibleMovesForTurn.get(piece);
    }

    /**
     * The method returns attached pieces and checks for swapping pawn
     * @param piece particular piece
     * @param to destination
     * @return Pair<Is there swapping pawn, Attached piece>
     */
    public Pair<Boolean, Piece> moveTo(Piece piece, Position to){
        checkSpecialConditions(piece, to);
        var secondaryPiece = moveToSafely(piece, to);

        if (secondaryPiece != null){
            if (secondaryPiece.player != piece.player){
                getEnemies(piece.player).remove(secondaryPiece);
                getEnemiesOut(piece.player).add(secondaryPiece);
            } else {
                if (secondaryPiece.pos.x == 0){
                    secondaryPiece.pos = new Position(piece.pos.x + 1, piece.pos.y);
                } else {
                    secondaryPiece.pos = new Position(piece.pos.x - 1, piece.pos.y);
                }
            }
        }

        disableIsPreviousMoveLong(piece.player);
        numberOfMoves++;

        return new Pair<>((piece instanceof Pawn) &&
                ((piece.pos.y == 0 && piece.player == Player.BLACK) ||
                        (piece.pos.y == 7 && piece.player == Player.WHITE)),
                secondaryPiece);
    }

    /**
     * The method maintains special cases after making move
     * Example: swapping pawn, checking for immediate end
     * @param piece piece which was moved
     * @param to destination
     * @param pieceForPawn special piece for replacing pawn
     * @return State of game
     */
    public State afterMoveUpdating(Piece piece, Position to, Piece pieceForPawn){
        if (isThereSwitchingOfPawn(piece, to)){
            getAllies(piece.player).remove(piece);
            getAllies(piece.player).add(pieceForPawn);
            getBoard()[piece.pos.y][piece.pos.x] = pieceForPawn;
        }

        switchTurn();
        allPossibleMovesForTurn.clear();

        setPossibleInitialCheck();

        return checkForImmediateEnd(Player.toPlayer(turn));
    }

    /**
     * The method checks that moved piece killed an enemy piece
     * @param piece moved piece
     * @param to destination
     * @return is there kill
     */
    public boolean isThereKill(Piece piece, Position to){
        if (piece instanceof Pawn && Math.abs(piece.pos.x - to.x) == 1 && board[to.y][to.x] == null){
            return true;
        }
        return board[to.y][to.x] != null;
    }

    /**
     * The method checks that this pawn should be replaced.
     * @param piece checked piece
     * @param to destination
     * @return is there switching og pawn
     */
    public boolean isThereSwitchingOfPawn(Piece piece, Position to){
        return piece instanceof Pawn && ((to.y == 0 && piece.player == Player.BLACK) || (to.y == 7 && piece.player == Player.WHITE));
    }

    /**
     * The method converts engine to string
     * First line is current turn
     * Second line is number of moves
     * First char of third line is initial turn
     * Second char of third line is first player
     * 4th line is died white pieces
     * 5th line is died black pieces
     * 6th line is white pieces
     * 7th line is black pieces
     * 8th line is initial pieces
     * @return engine as string
     */
    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();

        stringBuilder.append(turn.toString().charAt(0));
        stringBuilder.append('\n');
        stringBuilder.append(numberOfMoves);
        stringBuilder.append('\n');
        stringBuilder.append(initialTurn.toString().charAt(0));
        stringBuilder.append(firstPlayer.toString().charAt(0));
        stringBuilder.append('\n');

        piecesToString(whitePiecesOut, stringBuilder);
        piecesToString(blackPiecesOut, stringBuilder);
        piecesToString(whitePieces, stringBuilder);
        piecesToString(blackPieces, stringBuilder);
        piecesToString(initialPieces, stringBuilder);

        return stringBuilder.toString();
    }

    /**
     * The method convert strings to engine
     * @param lines string lines as engine
     * @return converted engine
     */
    public static ChessEngine readBoard(String[] lines){
        Turn currentTurn = (lines[0].equals("W")) ? Turn.WHITE : Turn.BLACK;
        int numberOfMoves = Integer.parseInt(lines[1]);
        Turn initialTurn = (lines[2].charAt(0) == 'W') ? Turn.WHITE : Turn.BLACK;
        Player firstPlayer = (lines[2].charAt(1) == 'W') ? Player.WHITE : Player.BLACK;

        ArrayList<Piece> whitePiecesOut = stringsToPieces(lines[3]);
        ArrayList<Piece> whitePieces = stringsToPieces(lines[5]);
        ArrayList<Piece> blackPiecesOut = stringsToPieces(lines[4]);
        ArrayList<Piece> blackPieces = stringsToPieces(lines[6]);
        ArrayList<Piece> initialPieces = stringsToPieces(lines[7]);

        var engine = new ChessEngine(whitePieces, whitePiecesOut, blackPieces, blackPiecesOut, currentTurn, firstPlayer);
        engine.numberOfMoves = numberOfMoves;
        engine.initialTurn = initialTurn;
        engine.initialPieces = initialPieces;
        return engine;
    }

    /**
     * The method checks that player lost
     * @param player player for checking
     * @return State of game
     */
    public State checkForImmediateEnd(Player player){
        if (isCheckmate(player) || (isCheck(player) && Player.toPlayer(turn) != player)){
            return State.toReverseState(player.invert());
        }

        if (isStalemate(player) && Player.toPlayer(turn) == player){
            return State.STALEMATE;
        }

        if ((getWhitePieces().size() == 1 && getBlackPieces().size() == 1) || numberOfMoves > 100){
            return State.DRAW;
        }

        return State.PARITY;
    }

    /**
     * The method sets flag wasChecked for any king if it is checked
     */
    public void setPossibleInitialCheck(){
        if (isCheck(Player.toPlayer(turn))){
            ((King)findKing(Player.toPlayer(turn))).wasChecked = true;
        }
    }

    /**
     * The method returns pawn of player that should be replaced
     * @param player checked player
     * @return swapping pawn
     */
    public Pawn isThereInitialPawnReplacing(Player player){
        var allies = getAllies(player);

        for (Piece ally : allies) {
            if (ally instanceof Pawn &&
                    ((ally.pos.y == 0 && ally.player == Player.BLACK) || (ally.pos.y == 7 && ally.player == Player.WHITE))){
                return (Pawn)ally;
            }
        }

        return null;
    }

    /**
     * The method switches turn;
     */
    public void switchTurn(){
        if (turn == Turn.WHITE){
            turn = Turn.BLACK;
        } else {
            turn = Turn.WHITE;
        }
    }

    /**
     * The method init board;
     */
    private void initBoard(){
        board = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        for (Piece piece : blackPieces) {
            initialPieces.add(Piece.copyPiece(piece));
            board[piece.pos.y][piece.pos.x] = piece;
        }

        for (Piece piece : whitePieces) {
            initialPieces.add(Piece.copyPiece(piece));
            board[piece.pos.y][piece.pos.x] = piece;
        }
    }

    /**
     * The method finds and returns king of player
     * @param player particular player
     * @return king of player
     */
    private Piece findKing(Player player){
        var allies = getAllies(player);

        for (Piece ally : allies) {
            if (ally instanceof King){
                return ally;
            }
        }

        return null;
    }

    /**
     * The method returns king's position of player
     * @param player particular player
     * @return king's position
     */
    private Position kingPosition(Player player){
        return findKing(player).pos;
    }

    /**
     * The method moves piece to destination safely. Restoring is possible.
     * @param piece Moving piece
     * @param to destination
     * @return changed(attached) piece
     */
    private Piece moveToSafely(Piece piece, Position to){
        Piece secondaryPiece = piece.moveToSafely(to, this);

        if (secondaryPiece != null){
            if (secondaryPiece.player != piece.player){
                getEnemies(piece.player).remove(secondaryPiece);
            }
        }

        return secondaryPiece;
    }

    /**
     * The method restores piece position
     * @param piece Restoring piece
     * @param from source
     * @param secondaryPiece Changed(Attached) piece
     */
    private void restore(Piece piece, Position from, Piece secondaryPiece){
        board[piece.pos.y][piece.pos.x] = null;

        if (secondaryPiece != null && secondaryPiece.player != piece.player){
            getEnemies(piece.player).add(secondaryPiece);
            board[secondaryPiece.pos.y][secondaryPiece.pos.x] = secondaryPiece;
        }

        if (secondaryPiece != null && secondaryPiece.player == piece.player){
            if (secondaryPiece.pos.x == 0){
                board[secondaryPiece.pos.y][3] = null;
            }
            else {
                board[secondaryPiece.pos.y][5] = null;
            }
            board[secondaryPiece.pos.y][secondaryPiece.pos.x] = secondaryPiece;
        }

        piece.pos = from;
        board[piece.pos.y][piece.pos.x] = piece;
    }

    /**
     * The method checks that destination is valid (for example, it does not conduct check)
     * @param piece particular piece
     * @param to destination
     * @return validity of destination
     */
    private boolean checkValidityOfPosition(Piece piece, Position to) {
        var currentPos = piece.pos;
        Piece secondaryPiece = moveToSafely(piece, to);
        boolean result = isCheck(piece.player);
        restore(piece, currentPos, secondaryPiece);
        return result;
    }

    /**
     * The method updates special fields for pawn.
     * @param pawn checked pawn
     * @param to destination
     */
    private void checkConditionsForPawn(Pawn pawn, Position to){
        if (Math.abs(to.y - pawn.pos.y) == 2){
            pawn.canMakeLongMove = false;
            pawn.isPreviousMoveLong = true;
        }
        pawn.canMakeLongMove = false;
    }

    /**
     * The method reset flag isPreviousMoveLong for any player's pawn
     * @param player particular pawn
     */
    private void disableIsPreviousMoveLong(Player player){
        var enemies = getEnemies(player);

        for (Piece enemy : enemies) {
            if (enemy instanceof Pawn && ((Pawn)enemy).isPreviousMoveLong){
                ((Pawn)enemy).isPreviousMoveLong = false;
            }
        }
    }

    /**
     * The method maintains special additional field for any player's piece
     * @param piece particular piece
     * @param to destination
     */
    private void checkSpecialConditions(Piece piece, Position to){
        if (piece instanceof Pawn){
            checkConditionsForPawn((Pawn)piece, to);
        } else if (piece instanceof Rook){
            if (((Rook)piece).isFirstMove){
                ((Rook)piece).isFirstMove = false;
            }
        } else if (piece instanceof King){
            if (((King)piece).isFirstMove){
                ((King)piece).isFirstMove = false;
            }
        }
    }

    /**
     * The method translates list of pieces to continuous string using toString()
     * @param pieces particular list of pieces
     * @param stringBuilder current string builder
     */
    private void piecesToString(List<Piece> pieces, StringBuilder stringBuilder){
        for (Piece piece : pieces) {
            stringBuilder.append(piece.toString());
            stringBuilder.append(" ");
        }
        stringBuilder.append('\n');
    }

    /**
     * The method translates continuous string to list of piece using stringToPiece()
     * @param piecesAsString continuous string
     * @returnlist of pieces
     */
    private static ArrayList<Piece> stringsToPieces(String piecesAsString){
        ArrayList<Piece> pieces = new ArrayList<>();

        for (String pieceAsString : piecesAsString.split(" ")) {
            if (pieceAsString.length() > 0){
                pieces.add(Piece.stringToPiece(pieceAsString));
            }
        }

        return pieces;
    }
}
