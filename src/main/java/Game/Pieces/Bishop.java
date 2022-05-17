package Game.Pieces;

import Game.ChessEngine;
import Game.Player;
import Game.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of Bishop
 */
public class Bishop extends Piece {
    /**
     * Default constructor
     * @See Piece()
     */
    public Bishop() {
        super();
        pathToImage += Bishop.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * First parametrized constructor
     * @see Piece( Position , Player )
     */
    public Bishop(Position pos, Player player) {
        super(pos, player);
        pathToImage += Bishop.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * Second parametrized constructor
     * @see Piece(int, int, Player)
     */
    public Bishop(int x, int y, Player player) {
        super(x, y, player);
        pathToImage += Bishop.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * The method returns all moves for bishop
     * @param engine chess engine
     * @return possible moves for bishop
     */
    @Override
    public List<Position> getPossibleMoves(ChessEngine engine) {
        var board = engine.getBoard();
        var possibleMoves = new ArrayList<Position>();

        addDiagonalMoves(possibleMoves, board);

        return possibleMoves;
    }

    /**
     * The method makes safe move for bishop
     * @see Piece moveToSafely(Position, ChessEngine)
     */
    @Override
    public Piece moveToSafely(Position to, ChessEngine engine) {
        return simpleMove(to, engine);
    }

    /**
     * The method convert Bishop to string
     * @see String toString()
     */
    @Override
    public String toString() {
        return toChar() + pos.toString();
    }
}
