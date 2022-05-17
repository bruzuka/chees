package Util;

import Game.ChessEngine;
import Game.Pieces.Piece;
import Game.Player;
import Game.Position;
import Game.Turn;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of Board Decoder
 * It decodes txt filt to chess engine and to sequence of moves
 */
public class BoardDecoder {
    private final ArrayList<String> strings; // Strings of file
    private int blackSeconds; // Seconds of black player
    private int whiteSeconds; // Seconds of white player
    private String[] movesAsStrings; // Moves as strings
    private ArrayList<String> separatedMoves; // Separated moves
    private ChessEngine engine; // Chess engine
    private int startPositionForChess; // Start position for decoding engine
    private boolean vsPC; // Vs PC flag

    /**
     * Default constructor
     * @param reader bufferedReader
     * @throws IOException
     */
    public BoardDecoder(BufferedReader reader) throws IOException {
        strings = new ArrayList<>();
        String current = reader.readLine();
        while (current != null){
            strings.add(current);
            current = reader.readLine();
        }

        parseVsPCFlag();
        parseSeconds();
        extractMoves();
        extractBoard();
    }

    /**
     * The method returns concatenated movesASStrings
     * @return strings for moves history
     */
    public String joinMovesStrings(){
        return String.join("\n", movesAsStrings);
    }

    /**
     * The method returns flag Vs PC
     * @return flag Vs PC
     */
    public boolean isVsPC() {
        return vsPC;
    }

    /**
     * The method returns seconds for black player
     * @return seconds for black player
     */
    public int getBlackSeconds() {
        return blackSeconds;
    }

    /**
     * The method returns seconds for white player
     * @return seconds for white player
     */
    public int getWhiteSeconds() {
        return whiteSeconds;
    }

    /**
     * The method returns decoded chess engine
     * @return chess engine
     */
    public ChessEngine getEngine() {
        return engine;
    }

    /**
     * The method returns list of boards for any stage of the game
     * @return list of boards
     */
    public ArrayList<Piece[][]> getFormattedMoves(){
        ArrayList<Piece[][]> boards = new ArrayList<>();
        extractSeparatedMoves();
        resetBoard();

        addCurrentBoard(boards);

        for (String moveAsString : separatedMoves) {
            updateBoard(moveAsString);
            addCurrentBoard(boards);
        }

        return boards;
    }

    /**
     * The method return separated moves
     * @return separated moves
     */
    public ArrayList<String> getSeparatedMoves() {
        return separatedMoves;
    }

    /**
     * The method parses VsPC flag
     */
    private void parseVsPCFlag(){
        vsPC = Boolean.parseBoolean(strings.get(7));
    }

    /**
     * The method parses seconds
     */
    private void parseSeconds(){
        whiteSeconds = extractSeconds(extractStringTime(strings.get(8)));
        blackSeconds = extractSeconds(extractStringTime(strings.get(9)));
    }

    /**
     * The method extracts substring that contains time information
     * @param str string for extracting
     * @return extracted substring
     */
    private String extractStringTime(String str){
        var stringBuilder = new StringBuilder();
        for (int i = 12; i < str.length(); i++) {
            stringBuilder.append(str.charAt(i));
        }
        return stringBuilder.toString();
    }

    /**
     * The method extracts time from the string
     * @param str string for extracting
     * @return extracted seconds
     */
    private int extractSeconds(String str){
        if (str.charAt(0) == '-'){
            return -1;
        } else {
            var minutesAsString = new StringBuilder();
            var secondsAsString = new StringBuilder();

            int k = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != ':'){
                    minutesAsString.append(str.charAt(i));
                } else {
                    k = i + 1;
                    break;
                }
            }

            for (int i = k; i < str.length(); i++) {
                secondsAsString.append(str.charAt(i));
            }

            return Integer.parseInt(minutesAsString.toString()) * 60 + Integer.parseInt(secondsAsString.toString());
        }
    }

    /**
     * The method extracts moves from txt data
     */
    private void extractMoves(){
        int endPosition = 0;
        for (int i = 10; i < strings.size(); i++) {
            try {
                if (strings.get(i).charAt(0) == 'W' || strings.get(i).charAt(0) == 'B'){
                    endPosition = i;
                    break;
                }
            } catch (StringIndexOutOfBoundsException ignore){
                // Ignore, because it is not problem
            }
        }

        movesAsStrings = new String[endPosition - 10];

        for (int i = 10; i < endPosition; i++) {
            movesAsStrings[i - 10] = strings.get(i);
        }

        startPositionForChess = endPosition;
    }

    /**
     * The method extracts lines for decoding chess engine
     */
    private void extractBoard(){
        var lines = new String[strings.size() - startPositionForChess];

        for (int i = startPositionForChess; i < strings.size(); i++) {
            lines[i - startPositionForChess] = strings.get(i);
        }

        engine = ChessEngine.readBoard(lines);
    }

    /**
     * The method separates moves
     */
    private void extractSeparatedMoves(){
        separatedMoves = new ArrayList<>();

        for (String move : movesAsStrings) {
            String[] splittedMoves = move.split(" ");

            for (String separatedMove : splittedMoves) {
                if (isValidMoveString(separatedMove)){
                    separatedMoves.add(separatedMove);
                }
            }
        }
    }

    /**
     * The method verifies that this string is encoded move
     * @param moveAsString
     * @return validity of string
     */
    private boolean isValidMoveString(String moveAsString){
        return moveAsString.length() > 0 &&!(moveAsString.charAt(moveAsString.length() - 1) == '.' ||
                moveAsString.charAt(0) == '#' || moveAsString.charAt(0) == '=');
    }

    /**
     * The method updates chess engine using extracted moves
     * @param moveAsString
     */
    private void updateBoard(String moveAsString){
        var board = engine.getBoard();
        if (moveAsString.equals("0-0")){ // Castling
            int y = (engine.turn == Turn.WHITE) ? 0 : 7;
            var king = board[y][4];
            engine.moveTo(king, new Position(6, y));
            engine.afterMoveUpdating(king, new Position(6, y), null);
        } else if (moveAsString.equals("0-0-0")){ // Also castling
            int y = (engine.turn == Turn.WHITE) ? 0 : 7;
            var king = board[y][4];
            engine.moveTo(king, new Position(2, y));
            engine.afterMoveUpdating(king, new Position(2, y), null);
        } else {
            var move = moveToString(moveAsString);
            var piece = board[move.left.y][move.left.x];
            engine.moveTo(piece, move.medium);
            engine.afterMoveUpdating(piece, move.medium, move.right);
        }
    }

    /**
     * The method adds current stage of the game to the list of boards
     * @param boards list of boards
     */
    private void addCurrentBoard(List<Piece[][]> boards){
        var newBoard = new Piece[8][8];
        var currentBoard = engine.getBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currentBoard[i][j] != null){
                    newBoard[i][j] = Piece.copyPiece(currentBoard[i][j]);
                } else {
                    newBoard[i][j] = null;
                }
            }
        }

        boards.add(newBoard);
    }

    /**
     * The method decodes string to acceptable move: (from, to, piece for pawn)
     * @param moveAsString move as string
     * @return
     */
    private Triple<Position, Position, Piece> moveToString(String moveAsString){
        int shift = (moveAsString.charAt(0) + "").equals((moveAsString.charAt(0) + "").toUpperCase()) ? 1 : 0;
        int x1 = moveAsString.charAt(shift) - 'a';
        int y1 = moveAsString.charAt(shift + 1) - '0' - 1;
        int x2 = moveAsString.charAt(shift + 3) - 'a';
        int y2 = moveAsString.charAt(shift + 4) - '0' - 1;

        Piece piece = null;
        if (shift + 5 < moveAsString.length()){
            piece = Piece.charToPiece(x2, y2, moveAsString.charAt(shift + 5));
        }

        return new Triple<>(new Position(x1, y1), new Position(x2, y2), piece);
    }

    /**
     * The method resets engine to initial positions
     */
    private void resetBoard(){
        var initialPieces = engine.getInitialPieces();
        ArrayList<Piece> whitePieces = new ArrayList<>();
        ArrayList<Piece> blackPieces = new ArrayList<>();

        for (Piece piece : initialPieces) {
            if (piece.player == Player.WHITE){
                whitePieces.add(piece);
            } else {
                blackPieces.add(piece);
            }
        }

        engine = new ChessEngine(whitePieces, new ArrayList<>(), blackPieces, new ArrayList<>(), engine.initialTurn, engine.firstPlayer);
    }
}
