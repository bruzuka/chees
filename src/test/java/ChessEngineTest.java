import Game.*;
import Game.Pieces.*;
import Util.Triple;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Chess engine test
 * Small symbols stands for black pieces.
 * Capital symbols stands for white symbols.
 */
public class ChessEngineTest extends TestCase {
    private Piece[][][] boards;
    private Turn[] turns;
    private Player[] firstPlayers;
    private ArrayList<Triple<Piece, Position, Piece>>[] moves;
    private State[] states;
    private Piece[] attachedPieces;
    private Position[] specialPositions;
    private boolean[] isThereAnyoneOnPosition;

    /**
     * The method creates engine using given information
     * @param board board
     * @param turn current turn
     * @param firstPlayer first player
     * @return chess engine
     */
    private ChessEngine getEngine(Piece[][] board, Turn turn, Player firstPlayer){
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> whitePieces = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null){
                    if (board[i][j].player == Player.WHITE){
                        whitePieces.add(board[i][j]);
                    } else {
                        blackPieces.add(board[i][j]);
                    }
                }
            }
        }

        return new ChessEngine(whitePieces, new ArrayList<>(), blackPieces, new ArrayList<>(), turn, firstPlayer);
    }

    /**
     * The method returns empty board
     * @return empty board
     */
    private Piece[][] getEmptyBoard(){
        var board = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        return board;
    }

    /**
     * The method inits tests
     */
    protected void setUp(){
        boards = new Piece[5][8][8];
        turns = new Turn[]{Turn.WHITE, Turn.BLACK, Turn.WHITE, Turn.BLACK, Turn.BLACK};
        firstPlayers = new Player[]{Player.BLACK, Player.BLACK, Player.WHITE, Player.WHITE, Player.BLACK};
        states = new State[]{State.WIN_OF_WHITE, State.PARITY, State.STALEMATE, State.WIN_OF_BLACK, State.WIN_OF_WHITE };
        moves = new ArrayList[5];
        specialPositions = new Position[5];
        isThereAnyoneOnPosition = new boolean[]{false, false, true, true, false};
        attachedPieces = new Piece[5];

        for (int i = 0; i < 5; i++) {
            boards[i] = getEmptyBoard();
            moves[i] = new ArrayList<>();
        }

        /*
         * ___k____
         * RP______
         * ________
         * ________
         * ________
         * ________
         * ________
         * K_______
         *
         * b7-b8Q# (Checkmate)
         * */
        moves[0].add(new Triple<>(new Pawn(1, 6, Player.WHITE), new Position(1, 7),
                new Queen(1, 7, Player.WHITE)));
        boards[0][7][3] = new King(3, 7, Player.BLACK);
        boards[0][6][0] = new Rook(0, 6, Player.WHITE);
        boards[0][6][1] = moves[0].get(0).left;
        boards[0][0][0] = new King(0, 0, Player.WHITE);
        attachedPieces[0] = null;
        specialPositions[0] = new Position(1, 6);

        /*
         * ________
         * __pk____
         * ________
         * _P______
         * __R_____
         * ________
         * ________
         * K_______
         *
         * c7-c5
         * b5xc6+ (Check)
         * */
        moves[1].add(new Triple<>(new Pawn(2, 6, Player.BLACK), new Position(2, 4), null));
        moves[1].add(new Triple<>(new Pawn(1, 4, Player.WHITE), new Position(2, 5), null));
        boards[1][6][3] = new King(3, 6, Player.BLACK);
        boards[1][6][2] = moves[1].get(0).left;
        boards[1][4][1] = moves[1].get(1).left;
        boards[1][3][2] = new Rook(2, 3, Player.WHITE);
        boards[1][0][0] = new King(0, 0, Player.WHITE);
        attachedPieces[1] = moves[1].get(0).left;
        specialPositions[1] = new Position(2, 4);

        /*
         * k_______
         * ___K____
         * ________
         * ________
         * ________
         * ________
         * ________
         * __Q_____
         *
         * Qc1-c7= (Stalemate)
         * */
        moves[2].add(new Triple<>(new Queen(2, 0, Player.WHITE), new Position(2, 6), null));
        boards[2][7][0] = new King(0, 7, Player.BLACK);
        boards[2][6][3] = new King(3, 6, Player.WHITE);
        boards[2][0][2] = moves[2].get(0).left;
        attachedPieces[2] = null;
        specialPositions[2] = new Position(2, 6);

        /*
         * ________
         * ________
         * ________
         * ________
         * ________
         * ________
         * _____p__
         * _____kBK
         *
         * f2xg1Q# (Checkmate)
         * */
        moves[3].add(new Triple<>(new Pawn(5, 1, Player.BLACK), new Position(6, 0),
                new Queen(6, 0, Player.BLACK)));
        boards[3][0][5] = new King(5, 0, Player.BLACK);
        boards[3][0][6] = new Bishop(6, 0, Player.WHITE);
        boards[3][0][7] = new King(7, 0, Player.WHITE);
        boards[3][1][5] = moves[3].get(0).left;
        attachedPieces[3] = boards[3][0][6];
        specialPositions[3] = new Position(6, 0);

        /*
         * ____k___
         * _____Q__
         * ________
         * ________
         * __R_____
         * ________
         * ________
         * R___K___
         *
         * Ke8-d8
         * 0-0-0# (Checkmate)
         * */
        moves[4].add(new Triple<>(new King(4, 7, Player.BLACK), new Position(3, 7), null));
        moves[4].add(new Triple<>(new King(4, 0, Player.WHITE), new Position(2, 0), null));
        boards[4][7][4] = moves[4].get(0).left;
        boards[4][6][5] = new Queen(5, 6, Player.WHITE);
        boards[4][3][2] = new Rook(2, 3, Player.WHITE);
        boards[4][0][0] = new Rook(0, 0, Player.WHITE);
        boards[4][0][4] = moves[4].get(1).left;
        attachedPieces[4] = boards[4][0][0];
        specialPositions[4] = new Position(0, 0);
    }

    /**
     * Test for chess engine
     */
    public void testAfterMoveUpdating() {
        for (int i = 0; i < 5; i++) {
            ChessEngine engine = getEngine(boards[i], turns[i], firstPlayers[i]);
            State currentState = State.PARITY;
            Piece currentAttachedPiece = null;

            for (var move : moves[i]) {
                currentAttachedPiece = engine.moveTo(move.left, move.medium).right;
                currentState = engine.afterMoveUpdating(move.left, move.medium, move.right);
            }

            assertEquals(states[i], currentState);
            assertEquals(attachedPieces[i], currentAttachedPiece);
            assertEquals(isThereAnyoneOnPosition[i], engine.getBoard()[specialPositions[i].y][specialPositions[i].x] != null);
        }
    }
}
