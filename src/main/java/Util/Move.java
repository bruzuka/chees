package Util;

import Game.Pieces.Piece;
import Game.Position;

/**
 * Class of move
 */
public class Move {
    public Piece piece; // Current piece
    public Piece pieceForPawn; // Piece for pawn
    public Position to; // Destination
    public boolean whiteTimeLimit; // Time limit flag for white player
    public boolean blackTimeLimit; // Time limit flag for black player
    public boolean isInterruptedMove; // Flag of interruption

    /**
     * Default constructor
     */
    public Move(){
        piece = null;
        pieceForPawn = null;
        to = null;
        whiteTimeLimit = false;
        blackTimeLimit = false;
        isInterruptedMove = false;
    }
}
