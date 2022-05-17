package Game.Pieces;

import Game.ChessEngine;
import Game.Player;
import Game.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of Queen
 */
public class Queen extends Piece {
    /**
     * Default constructor
     * @See Piece()
     */
    public Queen() {
        super();
        pathToImage += Queen.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * First parametrized constructor
     * @see Piece( Position , Player )
     */
    public Queen(Position pos, Player player) {
        super(pos, player);
        pathToImage += Queen.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * Second parametrized constructor
     * @see Piece(int, int, Player)
     */
    public Queen(int x, int y, Player player) {
        super(x, y, player);
        pathToImage += Queen.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * The method returns all moves for queen
     * @param engine chess engine
     * @return possible moves for bishop
     */
    @Override
    public List<Position> getPossibleMoves(ChessEngine engine) {
        var board = engine.getBoard();
        var possibleMoves = new ArrayList<Position>();

        addDiagonalMoves(possibleMoves, board);
        addOrthogonalMoves(possibleMoves, board);

        return possibleMoves;
    }

    /**
     * The method makes safe move for queen
     * @see Piece moveToSafely(Position, ChessEngine)
     */
    @Override
    public Piece moveToSafely(Position to, ChessEngine engine) {
        return simpleMove(to, engine);
    }

    /**
     * The method convert Queen to string
     * @see String toString()
     */
    @Override
    public String toString() {
        return toChar() + pos.toString();
    }
}
