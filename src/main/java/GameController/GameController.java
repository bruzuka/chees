package GameController;

import GUI.GameGUI.GameGUI;
import Game.ChessEngine;
import Game.Pieces.*;
import Game.Player;
import Game.Position;
import Util.ChessTimer;
import Util.Move;
import Util.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class of game controller.
 * It contains chess engine, gameGUI, current move, vsPC flag and chess timer.
 * Game controller works in another thread
 */
public class GameController extends Thread{
    private final ChessEngine engine; // Chess engine
    private final GameGUI gameGUI; // GameGUI
    private final Move move; // Current move
    private final boolean vsPC; // VsPC flag
    private final ChessTimer timer; // Chess time
    private final Random random; // Random

    /**
     * Default constructor
     * @param gameGUI gameGUI
     * @param engine chess engine
     * @param move current move
     * @param timer chess timer
     * @param vsPC vsPC flag
     */
    public GameController(GameGUI gameGUI, ChessEngine engine, Move move, ChessTimer timer, boolean vsPC){
        this.gameGUI = gameGUI;
        this.engine = engine;
        this.move = move;
        this.vsPC = vsPC;
        this.timer = timer;
        random = new Random();

        if (timer != null){
            timer.start();
        }
    }

    /**
     * Run method. Entry point of starting game
     */
    @Override
    public void run() {
        super.run();
        game();
    }

    /**
     * This method stops this thread and game
     * It rises flag move.isMoveInterrupted and interrupts game thread
     */
    public synchronized void stopGame(){
        if (timer != null){
            timer.interrupt();
        }

        synchronized (move){
            move.isInterruptedMove = true;
            interrupt();
            move.notifyAll();
        }
    }

    /**
     * This method waits while player is making move, than packs it
     * @return pair<moved piece, destination>
     */
    private Pair<Piece, Position> playerTryToMakeMove(){
        synchronized (move){
            while (!gameGUI.isMoveMade && !move.isInterruptedMove && !move.blackTimeLimit && !move.whiteTimeLimit){
                try {
                    move.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                    interrupt();
                }
            }
        }

        return new Pair<>(move.piece, move.to);
    }

    /**
     * The method waits while player is choosing piece, than returns it
     * @param piece swapping pawn
     * @param to destination
     * @return piece that replace a pawn
     */
    private Piece replacePawn(Piece piece, Position to){
        String[] options = new String[] {"Queen", "Knight", "Rook", "Bishop"};
        int response = JOptionPane.showOptionDialog(null, "Choose piece", "Choosing piece",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        switch (response){
            case 0 : return new Queen(to, piece.player);
            case 1 : return new Knight(to, piece.player);
            case 2 : return new Rook(to, piece.player);
            default : return new Bishop(to, piece.player);
        }
    }

    /**
     * The methods implements pc move logic and returns the consequence of pc's decision
     * @return state after pc turn
     */
    private Game.State pcMove(){
        var replacedPawn = engine.isThereInitialPawnReplacing(Player.toPlayer(engine.turn));
        if (replacedPawn != null && !engine.isCheck(Player.toPlayer(engine.turn))){
            return maintainingExtraReplacingPawn(replacedPawn);
        }

        try{
            if (checkTimeLimits() == Game.State.PARITY){
                //If there is no time limit
                var allies = new ArrayList<>(engine.getAllies(engine.firstPlayer.invert()));

                var ally = allies.get(random.nextInt(allies.size()));
                while (engine.getPossiblePosition(ally).isEmpty() && !allies.isEmpty()){
                    allies.remove(ally);
                    ally = allies.get(random.nextInt(allies.size()));
                }

                var positions = engine.getPossiblePosition(ally);
                Pair<Piece, Position> pcsMove = new Pair<>(ally, positions.get(random.nextInt(positions.size())));

                return maintainMove(pcsMove);
            } else {
                //Time limit
                return checkTimeLimits();
            }
        } catch (NullPointerException e){
            interrupt();
            return Game.State.PARITY;
        }
    }

    /**
     * The methods implements player move logic and returns the consequence of player's decision
     * @return state after player move
     */
    private Game.State playersMove(){
        var replacedPawn = engine.isThereInitialPawnReplacing(Player.toPlayer(engine.turn));
        if (replacedPawn != null && !engine.isCheck(Player.toPlayer(engine.turn))){
            return maintainingExtraReplacingPawn(replacedPawn);
        }

        var playersMove = new Pair<>(playerTryToMakeMove());

        try {
            if (checkTimeLimits() == Game.State.PARITY){
                return maintainMove(playersMove);
            } else {
                return checkTimeLimits();
            }
        } catch (NullPointerException e){
            interrupt();
            return Game.State.PARITY;
        }
    }

    /**
     * Main loop
     */
    private void game(){
        preGameChecking(); // Checking for immediate end

        while (!isInterrupted()){
            Game.State state;

            if (vsPC && Player.toPlayer(engine.turn) != engine.firstPlayer){
                state = pcMove();
            } else {
                gameGUI.currentPlayer = Player.toPlayer(engine.turn);
                state = playersMove();
            }

            if (state != Game.State.PARITY){
                endgame(state);
                break;
            }
        }

        finalizeWork(); // Finalize work of gui
    }

    /**
     * The method updates gui
     * @param playerMove player's move
     * @param attachedPiece changed(attached) piece
     */
    private void updateGUI(Pair<Piece, Position> playerMove, Piece attachedPiece){
        gameGUI.updateBoard(playerMove.left, attachedPiece);
        gameGUI.updateBlackPiecesOut();
        gameGUI.updateWhitePiecesOut();
        gameGUI.repaint();
    }

    /**
     * The method implements endgame logic
     * It closes timers, freezes gui and updates moves history
     * @param state state of game
     */
    private void endgame(Game.State state){
        gameGUI.isMoveMade = true;
        gameGUI.remove(gameGUI.blackTime);
        gameGUI.remove(gameGUI.whiteTime);
        gameGUI.resultLabel.setText(state.toString());
        var currentText = gameGUI.movesHistoryText.getText();

        if ((currentText.length() > 0 && currentText.charAt(currentText.length() - 1) != '#' && currentText.charAt(currentText.length() - 1) != '=') ||
                currentText.isEmpty()){ //Adding final symbols if and only if there is no one of them
            if (state == Game.State.WIN_OF_BLACK || state == Game.State.WIN_OF_WHITE){
                gameGUI.movesHistoryText.setText(gameGUI.movesHistoryText.getText() + "#");
            } else {
                gameGUI.movesHistoryText.setText(gameGUI.movesHistoryText.getText() + "=");
            }
        }

        gameGUI.add(gameGUI.resultLabel);
        gameGUI.repaint();
    }

    /**
     * The method maintains move
     * First it moves piece
     * Than it checks for replacing pawn
     * Finally it makes maintaining the consequences of moving
     * @param playersMove player's move
     * @return state of game
     */
    private Game.State maintainMove(Pair<Piece, Position> playersMove){
        boolean wasThereKill = engine.isThereKill(playersMove.left, playersMove.right);
        Position from = playersMove.left.pos;

        var moveValidation = engine.moveTo(playersMove.left, playersMove.right);
        updateGUI(playersMove, moveValidation.right);

        Piece pieceForPawn = null;
        if (engine.isThereSwitchingOfPawn(playersMove.left, playersMove.right)){
            pieceForPawn = (vsPC && Player.toPlayer(engine.turn) != engine.firstPlayer) ?
                    getRandomPieceForReplacingPawn(playersMove.left, playersMove.right) :
                    replacePawn(playersMove.left, playersMove.right);
            gameGUI.introduceNewPiece(playersMove.left, pieceForPawn);
            gameGUI.repaint();
        }

        var stateOfGame = engine.afterMoveUpdating(playersMove.left, playersMove.right, pieceForPawn);
        gameGUI.addNewMoveToHistory(playersMove.left, pieceForPawn, from, playersMove.right, wasThereKill);

        if (!vsPC){
            gameGUI.currentPlayer = gameGUI.currentPlayer.invert();
        }

        if (stateOfGame == Game.State.PARITY){
            gameGUI.isMoveMade = false;
        }

        if (timer != null){
            timer.switchPlayer();
        }

        return stateOfGame;
    }

    /**
     * The method takes random piece from the set {Queen, Rook, Bishop, Knight}
     * @param piece pawn for replacing
     * @param to destination
     * @return random piece
     */
    private Piece getRandomPieceForReplacingPawn(Piece piece, Position to){
        return switch (random.nextInt(4)) {
            case 0 -> new Queen(to, piece.player);
            case 1 -> new Knight(to, piece.player);
            case 2 -> new Rook(to, piece.player);
            default -> new Bishop(to, piece.player);
        };
    }

    /**
     * The method checks for time limits
     * @return state of game
     */
    private Game.State checkTimeLimits(){
        if (move.whiteTimeLimit){
            return Game.State.WIN_OF_BLACK;
        }

        if (move.blackTimeLimit){
            return Game.State.WIN_OF_WHITE;
        }

        return Game.State.PARITY;
    }

    /**
     * The method finalizes work of gui
     */
    private void finalizeWork(){
        if (timer != null){
            timer.interrupt();
        }

        gameGUI.isMoveMade = true;
        gameGUI.makeAllCellsInvisible();

        try {
            gameGUI.removeMouseListener(gameGUI.getMouseListeners()[0]);
        } catch (ArrayIndexOutOfBoundsException ignored) { /*Ignore exception*/ }

        try {
            gameGUI.removeMouseMotionListener(gameGUI.getMouseMotionListeners()[0]);
        } catch (ArrayIndexOutOfBoundsException ignored) { /*Ignore exception*/ }

        gameGUI.placeAllPiecesBack();
    }

    /**
     * The method makes pregame maintaining.
     * If it finds that game is already finished, than it interrupts this thread
     */
    private void preGameChecking(){
        engine.setPossibleInitialCheck();

        if(preGameCheckingOfPlayer(Player.WHITE) == Game.State.PARITY){
            preGameCheckingOfPlayer(Player.BLACK);
        }

        gameGUI.updateBlackPiecesOut();
        gameGUI.updateWhitePiecesOut();
    }

    /**
     * The method checks that this particular player lose
     * Also it looks for draw
     * @param player particular player
     * @return state of game
     */
    private Game.State preGameCheckingOfPlayer(Player player){
        var state = engine.checkForImmediateEnd(player);

        if (state != Game.State.PARITY){
            endgame(state);
            finalizeWork();
            interrupt();
        }

        return state;
    }

    /**
     * The method maintains replacing pawn at initial step of game
     * @param pawn swapping pawn
     * @return state of game
     */
    private Game.State maintainingExtraReplacingPawn(Pawn pawn){
        var pieceForPawn = (vsPC && Player.toPlayer(engine.turn) != engine.firstPlayer) ?
                getRandomPieceForReplacingPawn(pawn, pawn.pos) : replacePawn(pawn, pawn.pos);

        var stateOfGame = engine.afterMoveUpdating(pawn, pawn.pos, pieceForPawn);
        engine.numberOfMoves++;
        gameGUI.addNewMoveToHistory(pawn, pieceForPawn, pawn.pos, pawn.pos, false);
        gameGUI.introduceNewPiece(pawn, pieceForPawn);
        gameGUI.repaint();

        if (timer != null){
            timer.switchPlayer();
        }

        if (!vsPC){
            gameGUI.currentPlayer = gameGUI.currentPlayer.invert();
        }

        if (stateOfGame == Game.State.PARITY){
            gameGUI.isMoveMade = false;
        }

        return stateOfGame;
    }
}
