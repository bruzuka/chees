package Game.Pieces;

import Game.ChessEngine;
import Game.Player;
import Game.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of King
 */
public class King extends Piece {
    public boolean isFirstMove; // Is first move for king?
    public boolean wasChecked; // Was the king checked?

    /**
     * Default constructor
     */
    public King() {
        super();
        isFirstMove = true;
        wasChecked = false;
        pathToImage += King.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * First parametrized constructor
     * @param pos position of piece
     * @param player owner of this piece
     */
    public King(Position pos, Player player) {
        super(pos, player);
        isFirstMove = true;
        wasChecked = false;
        pathToImage += King.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * Second parametrized constructor
     * @param x x coordinate of piece
     * @param y y coordinate of piece
     * @param player owner of this piece
     */
    public King(int x, int y, Player player) {
        super(x, y, player);
        isFirstMove = true;
        wasChecked = false;
        pathToImage += King.class.getSimpleName() + Piece.suffixOfPath;
    }


    /**
     * Special method for generation possible moves
     * @param i special integer
     * @return shift of x coordinate
     */
    private int getSpecialX(int i){
        if (i < 3 || (i > 3 && i < 7)){
            return (i < 3) ? -1 : 1;
        }
        return 0;
    }

    /**
     * Special method for generation possible moves
     * @param i special integer
     * @return shift of y coordinate
     */
    private int getSpecialY(int i){
        if (i == 0 || i >= 6){
            return -1;
        }
        if (i > 1 && i < 5){
            return 1;
        }
        return 0;
    }

    /**
     * The method checks that King on canonical position
     * @return checking for canonical position
     */
    private boolean isCanonicalPosition(){
        return ((pos.y == 0 && player == Player.WHITE) || (pos.y == 7 && player == Player.BLACK)) && pos.x == 4;
    }

    /**
     * The method checks that castling is possible
     * @param rook Rook for castiling
     * @param board Chess board
     * @return is castling possible
     */
    private boolean isCastlingPossible(Rook rook, Piece[][] board){
        if (isCanonicalPosition() && rook.isFirstMove && !wasChecked && isFirstMove){
            for (int cx = rook.pos.x + 1; cx < pos.x; cx++) {
                if (board[pos.y][cx] != null){
                    return false;
                }
            }
            for (int cx = rook.pos.x - 1; cx > pos.x; cx--) {
                if (board[pos.y][cx] != null){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * The method adds castling moves
     * @param possibleMoves list of possible moves
     * @param board Chess board
     */
    private void addCastlingMoves(List<Position> possibleMoves, Piece[][] board){
        if (board[pos.y][0] instanceof Rook && !isEnemy(board[pos.y][0]) && isCastlingPossible((Rook)board[pos.y][0], board)){
            possibleMoves.add(new Position(pos.x - 2, pos.y));
        }
        if (board[pos.y][7] instanceof Rook && !isEnemy(board[pos.y][7]) && isCastlingPossible((Rook)board[pos.y][7], board)){
            possibleMoves.add(new Position(pos.x + 2, pos.y));
        }
    }

    /**
     * The method makes castling for king
     * @param to destination
     * @param board Chess board
     * @return Rook for castling
     */
    private Piece makeCastling(Position to, Piece[][] board){
        board[pos.y][pos.x] = null;
        if (to.x > pos.x){
            board[pos.y][pos.x + 1] = board[pos.y][7];
            board[pos.y][7] = null;

            pos = to;
            board[pos.y][pos.x] = this;

            return board[pos.y][pos.x - 1];
        } else {
            board[pos.y][pos.x - 1] = board[pos.y][0];
            board[pos.y][0] = null;

            pos = to;
            board[pos.y][pos.x] = this;

            return board[pos.y][pos.x + 1];
        }
    }

    /**
     * The method returns all moves for king
     * @param engine chess engine
     * @return possible moves for king
     */
    @Override
    public List<Position> getPossibleMoves(ChessEngine engine) {
        var board = engine.getBoard();
        var possibleMoves = new ArrayList<Position>();

        for (int i = 0; i < 9; i++) {
            int cx = pos.x + getSpecialX(i);
            int cy = pos.y + getSpecialY(i);

            if (isLegal(cx, cy) && isEnemy(board[cy][cx])){
                possibleMoves.add(new Position(cx, cy));
            }
        }

        addCastlingMoves(possibleMoves, board);

        return possibleMoves;
    }

    /**
     * The method makes safe move for king
     * @see Piece moveToSafely(Position, ChessEngine)
     */
    @Override
    public Piece moveToSafely(Position to, ChessEngine engine) {
        if (Math.abs(to.x - pos.x) == 2){
            return makeCastling(to, engine.getBoard());
        }
        return simpleMove(to, engine);
    }

    /**
     * The method convert King to string
     * @see String toString()
     */
    @Override
    public String toString() {
        return toChar() + pos.toString() + String.valueOf(isFirstMove).charAt(0) + "" + String.valueOf(wasChecked).charAt(0);
    }
}
