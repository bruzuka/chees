package Game.Pieces;

import Game.ChessEngine;
import Game.Player;
import Game.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of Rook
 */
public class Rook extends Piece {
    public boolean isFirstMove; //Is it first move?
    /**
     * Default constructor
     * @See Piece()
     */
    public Rook() {
        super();
        isFirstMove = true;
        pathToImage += Rook.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * First parametrized constructor
     * @see Piece( Position , Player )
     */
    public Rook(Position pos, Player player) {
        super(pos, player);
        isFirstMove = true;
        pathToImage += Rook.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * Second parametrized constructor
     * @see Piece(int, int, Player)
     */
    public Rook(int x, int y, Player player) {
        super(x, y, player);
        isFirstMove = true;
        pathToImage += Rook.class.getSimpleName() + Piece.suffixOfPath;
    }

    /**
     * The method returns all moves for rook
     * @param engine chess engine
     * @return possible moves for rook
     */
    @Override
    public List<Position> getPossibleMoves(ChessEngine engine) {
        var board = engine.getBoard();
        var possibleMoves = new ArrayList<Position>();

        addOrthogonalMoves(possibleMoves, board);

        return possibleMoves;
    }

    /**
     * The method makes safe move for rook
     * @see Piece moveToSafely(Position, ChessEngine)
     */
    @Override
    public Piece moveToSafely(Position to, ChessEngine engine) {
        return simpleMove(to, engine);
    }

    /**
     * The method convert Rook to string
     * @see String toString()
     */
    @Override
    public String toString() {
        return toChar() + pos.toString() + String.valueOf(isFirstMove).charAt(0);
    }
}
