package Game.Pieces;

import Game.ChessEngine;
import Game.Player;
import Game.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of Knight
 */
public class Knight extends Piece {
    /**
     * Default constructor
     * @See Piece()
     */
    public Knight() {
        super();
        pathToImage += Knight.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * First parametrized constructor
     * @see Piece( Position , Player )
     */
    public Knight(Position pos, Player player) {
        super(pos, player);
        pathToImage += Knight.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * Second parametrized constructor
     * @see Piece(int, int, Player)
     */
    public Knight(int x, int y, Player player) {
        super(x, y, player);
        pathToImage += Knight.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * Special method for generation possible moves
     * @param i special integer
     * @return shift of x coordinate
     */
    private int getSpecialX(int i){
        int factor = (i < 4) ? -1 : 1;
        return (((i + 1) / 2) % 2 == 0) ? factor : 2 * factor;
    }

    /**
     * Special method for generation possible moves
     * @param i special integer
     * @return shift of y coordinate
     */
    private int getSpecialY(int i){
        int factor = (i > 1 && i < 6) ? -1 : 1;
        return (((i + 1) / 2) % 2 != 0) ? factor : 2 * factor;
    }

    /**
     * The method returns all moves for knight
     * @param engine chess engine
     * @return possible moves for knight
     */
    @Override
    public List<Position> getPossibleMoves(ChessEngine engine) {
        var board = engine.getBoard();
        var possibleMoves = new ArrayList<Position>();

        for (int i = 0; i < 8; i++) {
            int cx = pos.x + getSpecialX(i);
            int cy = pos.y + getSpecialY(i);

            if (isLegal(cx, cy) && isEnemy(board[cy][cx])){
                possibleMoves.add(new Position(cx, cy));
            }
        }

        return possibleMoves;
    }

    /**
     * The method makes safe move for knight
     * @see Piece moveToSafely(Position, ChessEngine)
     */
    @Override
    public Piece moveToSafely(Position to, ChessEngine engine) {
        return simpleMove(to, engine);
    }

    /**
     * The method convert Knight to string
     * @see String toString()
     */
    @Override
    public String toString() {
        return toChar() + pos.toString();
    }
}
