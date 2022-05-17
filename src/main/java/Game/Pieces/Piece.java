package Game.Pieces;

import Game.ChessEngine;
import Game.Player;
import Game.Position;

import java.util.List;


/**
 * Class Piece represents abstract parameters of chess piece,
 * such as position and color(player). It also contains a path to the image and
 * type of image file(prefix).
 */
public abstract class Piece {
    public Position pos; // Position
    public Player player; // Player (White or Black)
    public String pathToImage; // Path to image
    protected static final String suffixOfPath = ".png"; //Format of image

    /**
     * Default constructor
     */
    Piece(){
        this.pos = new Position();
        this.player = Player.WHITE;
        pathToImage = "src/main/resources/" + player.toString();
    }

    /**
     * First parametrized constructor
     * @param pos position of piece
     * @param player owner of this piece
     */
    Piece(Position pos, Player player){
        this.pos = new Position(pos);
        this.player = player;
        pathToImage = "src/main/resources/" + player.toString();
    }

    /**
     * Second parametrized constructor
     * @param x x coordinate of piece
     * @param y y coordinate of piece
     * @param player owner of this piece
     */
    Piece(int x, int y, Player player){
        this.pos = new Position(x, y);
        this.player = player;
        pathToImage = "src/main/resources/" + player.toString();
    }

    /**
     * The method creates copy of piece and reinitialize of additional fields
     * @param piece piece for copying
     * @return copy of piece, but all additional parameters recalculated
     */
    public static Piece copyPieceWithReinitialization(Piece piece){
        if (piece instanceof Pawn){
            return new Pawn(piece.pos.x, piece.pos.y, piece.player);
        } else if (piece instanceof Rook){
            return new Rook(piece.pos.x, piece.pos.y, piece.player);
        } else if (piece instanceof Bishop){
            return new Bishop(piece.pos.x, piece.pos.y, piece.player);
        } else if (piece instanceof Knight){
            return new Knight(piece.pos.x, piece.pos.y, piece.player);
        } else if (piece instanceof Queen){
            return new Queen(piece.pos.x, piece.pos.y, piece.player);
        } else {
            return new King(piece.pos.x, piece.pos.y, piece.player);
        }
    }

    /**
     * The method copies piece
     * @param piece piece for copying
     * @return copy of piece
     */
    public static Piece copyPiece(Piece piece){
        var copyPiece = copyPieceWithReinitialization(piece);

        if (copyPiece instanceof Pawn){
            ((Pawn) copyPiece).isPreviousMoveLong = ((Pawn) piece).isPreviousMoveLong;
            ((Pawn) copyPiece).canMakeLongMove = ((Pawn) piece).canMakeLongMove;
        } else if (copyPiece instanceof Rook){
            ((Rook) copyPiece).isFirstMove = ((Rook) piece).isFirstMove;
        } else if (copyPiece instanceof King){
            ((King) copyPiece).isFirstMove = ((King) piece).isFirstMove;
            ((King) copyPiece).wasChecked = ((King) piece).wasChecked;
        }

        return copyPiece;
    }

    /**
     * The method returns char representation of piece.
     * Examples: White King -> K, Black Pawn -> p, White Knight -> N
     * Note: Small symbols stand for black pieces; Capital symbols stand for white ones
     * @return char representation of piece
     */
    public char toChar() {
        if (!(this instanceof Knight)){
            String simpleName = getClass().getSimpleName();
            return (player == Player.WHITE) ? simpleName.charAt(0) : simpleName.toLowerCase().charAt(0);
        } else {
            return (player == Player.WHITE) ? 'N' : 'n';
        }
    }

    /**
     * The method restores piece from char
     * @param x x coordinate
     * @param y y coordinate
     * @param p char representation
     * @return new piece built from char
     */
    public static Piece charToPiece(int x, int y, char p){
        Player player = ((p + "").toLowerCase().equals(p + "")) ? Player.BLACK : Player.WHITE;
        return switch ((p + "").toLowerCase()) {
            case "r" -> new Rook(x, y, player);
            case "p" -> new Pawn(x, y, player);
            case "b" -> new Bishop(x, y, player);
            case "k" -> new King(x, y, player);
            case "q" -> new Queen(x, y, player);
            case "n" -> new Knight(x, y, player);
            default -> null;
        };
    }

    /**
     * The method restores piece from string
     * @param pieceAsString advance representation piece as string
     * @return restored piece
     */
    public static Piece stringToPiece(String pieceAsString){
        var position = Position.stringToPosition(pieceAsString.charAt(1), pieceAsString.charAt(2));
        var piece = charToPiece(position.x, position.y, pieceAsString.charAt(0));

        if (piece instanceof Pawn){
            ((Pawn) piece).isPreviousMoveLong = (pieceAsString.charAt(3) == 't');
            ((Pawn) piece).canMakeLongMove = (pieceAsString.charAt(4) == 't');
        } else if (piece instanceof Rook){
            ((Rook) piece).isFirstMove = (pieceAsString.charAt(3) == 't');
        } else if (piece instanceof King){
            ((King) piece).isFirstMove = (pieceAsString.charAt(3) == 't');
            ((King) piece).wasChecked = (pieceAsString.charAt(4) == 't');
        }

        return piece;
    }

    /**
     * The method returns all legal moves
     * @param engine chess engine
     * @return list of all legal moves including ones that provide check
     */
    public abstract List<Position> getPossibleMoves(ChessEngine engine);

    /**
     * The method move safely piece to destination. Restoring is possible.
     * @param to destination
     * @param engine chess engine
     * @return piece that will be also changed by this move
     */
    public abstract Piece moveToSafely(Position to, ChessEngine engine);

    /**
     * The method creates and returns string representation of piece
     * Example: White Bishop(3, 3) -> B33, Black Pawn(4, 5) -> p45[additional information]
     * Note: Small symbols stand for black pieces; Capital symbols stand for white ones
     * @return string representation of piece
     */
    @Override
    public abstract String toString();

    /**
     * The method checks that (x, y) belongs board's coordinates
     * Note: It means that 0 <= x and x < 8, 0 <= y and y < 8
     * @param x x coordinate
     * @param y y coordinate
     * @return result of checking
     */
    protected boolean isLegal(int x, int y){
        return x < 8 && x >= 0 && y < 8 && y >= 0;
    }

    /**
     * The method checks that piece is enemy for it
     * @param piece piece for checking
     * @return true, if those piece have different owners, false otherwise
     */
    protected boolean isEnemy(Piece piece){
        if (piece == null){
            return true;
        }
        return piece.player != player;
    }

    /**
     * The method adds all empty position, that can be represented as (x0 + k * dx, y0 + k * dy)
     * where k is integer.
     * @param possibleMoves list of possible moves
     * @param board chess board
     * @param dx shift for x coordinate
     * @param dy shift for y coordinate
     */
    private void addSpecialMoves(List<Position> possibleMoves, Piece[][] board, int dx, int dy){
        int cx = pos.x + dx;
        int cy = pos.y + dy;
        while (isLegal(cx, cy) && board[cy][cx] == null){
            possibleMoves.add(new Position(cx, cy));
            cx += dx;
            cy += dy;
        }

        if (isLegal(cx, cy) && isEnemy(board[cy][cx])){
            possibleMoves.add(new Position(cx, cy));
        }
    }

    /**
     * The method adds all diagonal positions
     * @param possibleMoves list of possible moves
     * @param board chess board
     */
    protected void addDiagonalMoves(List<Position> possibleMoves, Piece[][] board){
        for (int i = 0; i < 4; i++) {
            int dx = (i < 2) ? -1 : 1;
            int dy = (i == 0 || i == 3) ? -1 : 1;

            addSpecialMoves(possibleMoves, board, dx, dy);
        }
    }

    /**
     * The method adds all orthogonal positions
     * @param possibleMoves list of possible moves
     * @param board chess board
     */
    protected void addOrthogonalMoves(List<Position> possibleMoves, Piece[][] board){
        for (int i = 0; i < 4; i++) {
            int dx = 0;
            int dy = 0;
            if (i == 1 || i == 3){
                dx = (i == 1) ? -1 : 1;
            }
            if (i == 0 || i == 2){
                dy = (i == 0) ? -1 : 1;
            }

            addSpecialMoves(possibleMoves, board, dx, dy);
        }
    }

    /**
     * The method makes simple move
     * @param to destination
     * @param engine chess engine
     * @return piece on position (to)
     */
    protected Piece simpleMove(Position to, ChessEngine engine){
        var board = engine.getBoard();
        board[pos.y][pos.x] = null;
        pos = to;
        var deletedPieces = board[pos.y][pos.x];
        board[pos.y][pos.x] = this;
        return deletedPieces;
    }
}
