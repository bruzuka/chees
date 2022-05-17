package Game.Pieces;

import Game.ChessEngine;
import Game.Player;
import Game.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of Pawn
 */
public class Pawn extends Piece {
    public boolean isPreviousMoveLong; // Is the previous move long?
    public boolean canMakeLongMove; // Can make long move? It's true if and only if initial position is canonical


    /**
     * Default constructor
     */
    public Pawn(){
        super();
        isPreviousMoveLong = false;
        canMakeLongMove = false;
        pathToImage += Pawn.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * First parametrized constructor
     * @param pos position of piece
     * @param player owner of this piece
     */
    public Pawn(Position pos, Player player){
        super(pos, player);
        isPreviousMoveLong = false;
        canMakeLongMove = (player == Player.BLACK && pos.y == 6) || (player == Player.WHITE && pos.y == 1);
        pathToImage += Pawn.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * Second parametrized constructor
     * @param x x coordinate of piece
     * @param y y coordinate of piece
     * @param player owner of this piece
     */
    public Pawn(int x, int y, Player player) {
        super(x, y, player);
        isPreviousMoveLong = false;
        canMakeLongMove = (player == Player.BLACK && y == 6) || (player == Player.WHITE && y == 1);
        pathToImage += Pawn.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * The method adds simple move to the list.
     * Example: a2-a3
     * Note: the simple move is when a pawn moves only forward.
     * @param possibleMoves list of possible moves
     * @param board Chess board
     */
    private void addSimpleMove(List<Position> possibleMoves, Piece[][] board){
        if (pos.y < 7 && player == Player.WHITE && board[pos.y + 1][pos.x] == null){
            possibleMoves.add(new Position(pos.x, pos.y + 1));
        }
        if (pos.y > 0 && player == Player.BLACK && board[pos.y - 1][pos.x] == null){
            possibleMoves.add(new Position(pos.x, pos.y - 1));
        }
    }

    /**
     * The method adds long move to the list.
     * Example: a2-a4
     * Note: the long move is when a pawn moves by two positions
     * @param possibleMoves list of possible moves
     * @param board Chess board
     */
    private void addLongMove(List<Position> possibleMoves, Piece[][] board){
        if (canMakeLongMove){
            if (player == Player.WHITE && board[pos.y + 2][pos.x] == null && board[pos.y + 1][pos.x] == null){
                possibleMoves.add(new Position(pos.x, pos.y + 2));
            }
            if (player == Player.BLACK && board[pos.y - 2][pos.x] == null && board[pos.y - 1][pos.x] == null){
                possibleMoves.add(new Position(pos.x, pos.y - 2));
            }
        }
    }

    /**
     * The method checks that there is capture of enemy pawn on the right
     * @param board Chess board
     * @return is there capture of enemy pawn
     */
    private boolean isThereCaptureInPassingRight(Piece[][] board){
        return pos.x < 7 && board[pos.y][pos.x + 1] instanceof Pawn &&
                isEnemy(board[pos.y][pos.x + 1]) && ((Pawn)board[pos.y][pos.x + 1]).isPreviousMoveLong;
    }

    /**
     * The method checks that there is capture of enemy pawn on the left
     * @param board Chess board
     * @return is there capture of enemy pawn
     */
    private boolean isThereCaptureInPassingLeft(Piece[][] board){
        return pos.x > 0 && board[pos.y][pos.x - 1] instanceof Pawn &&
                isEnemy(board[pos.y][pos.x - 1]) && ((Pawn)board[pos.y][pos.x - 1]).isPreviousMoveLong;
    }

    /**
     * The method checks that pawn can kill enemy piece on the right
     * @param board Chess board
     * @return is there possible killing on the right
     */
    private boolean canGoRight(Piece[][] board){
        if (pos.x < 7 && pos.y < 7 && player == Player.WHITE &&
                board[pos.y + 1][pos.x + 1] != null && isEnemy(board[pos.y + 1][pos.x + 1])){
            return true;
        }
        if (pos.x < 7 && pos.y > 0 && player == Player.BLACK &&
                board[pos.y - 1][pos.x + 1] != null && isEnemy(board[pos.y - 1][pos.x + 1])){
            return true;
        }
        if (isThereCaptureInPassingRight(board)){
            return true;
        }
        return false;
    }

    /**
     * The method checks that pawn can kill enemy piece on the left
     * @param board Chess board
     * @return is there possible killing on the left
     */
    private boolean canGoLeft(Piece[][] board){
        if (pos.x > 0 && pos.y < 7 && player == Player.WHITE &&
                board[pos.y + 1][pos.x - 1] != null && isEnemy(board[pos.y + 1][pos.x - 1])){
            return true;
        }
        if (pos.x > 0 && pos.y > 0 && player == Player.BLACK &&
                board[pos.y - 1][pos.x - 1] != null && isEnemy(board[pos.y - 1][pos.x - 1])){
            return true;
        }
        if (isThereCaptureInPassingLeft(board)){
            return true;
        }
        return false;
    }


    /**
     * The method adds left and right moves if it's possible
     * @param possibleMoves list of possible moves
     * @param board Chess board
     */
    private void addLeftOfRightMove(List<Position> possibleMoves, Piece[][] board){
        int factor = (player == Player.WHITE) ? 1 : -1;
        if (canGoRight(board)){
            possibleMoves.add(new Position(pos.x + 1, pos.y + factor));
        }
        if (canGoLeft(board)){
            possibleMoves.add(new Position(pos.x - 1, pos.y + factor));
        }
    }

    /**
     * The method makes left and right moves, if it's possible
     * @param to destination
     * @param engine Chess engine
     * @return piece that will be also changed by this move
     */
    private Piece makeLeftOrRightMove(Position to, ChessEngine engine){
        var board = engine.getBoard();

        Piece deleted = board[to.y][to.x];

        if (isThereCaptureInPassingLeft(engine.getBoard()) && to.x < pos.x){
            deleted = board[pos.y][pos.x - 1];
            board[pos.y][pos.x - 1] = null;
        }

        if (isThereCaptureInPassingRight(engine.getBoard()) && to.x > pos.x){
            deleted = board[pos.y][pos.x + 1];
            board[pos.y][pos.x + 1] = null;
        }

        board[pos.y][pos.x] = null;
        pos = to;
        board[pos.y][pos.x] = this;

        return deleted;
    }

    /**
     * The method returns all moves for pawn
     * @param engine  chess engine
     * @return possible moves for pawn
     */
    @Override
    public List<Position> getPossibleMoves(ChessEngine engine) {
        var board = engine.getBoard();
        var possibleMoves = new ArrayList<Position>();

        addSimpleMove(possibleMoves, board);
        addLongMove(possibleMoves, board);
        addLeftOfRightMove(possibleMoves, board);

        return possibleMoves;
    }

    /**
     * The method makes safe move for pawn
     * @see Piece moveToSafely(Position, ChessEngine)
     */
    @Override
    public Piece moveToSafely(Position to, ChessEngine engine) {
        if (to.x != pos.x){
            return makeLeftOrRightMove(to, engine);
        }
        return simpleMove(to, engine);
    }

    /**
     * The method convert Pawn to string
     * @see String toString()
     */
    @Override
    public String toString() {
        return toChar() + pos.toString() + String.valueOf(isPreviousMoveLong).charAt(0) + "" + String.valueOf(canMakeLongMove).charAt(0);
    }
}
