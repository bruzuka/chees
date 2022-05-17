package Util;

import GUI.GameGUI.GameGUI;
import Game.Player;

/**
 * Class of chess timer
 */
public class ChessTimer extends Thread {
    private int secondsOfBlack; // Seconds that black player has
    private int secondsOfWhite; // Seconds that white player has
    private final GameGUI gameGUI; // Game GUI
    private Player playersTimerRunning; // Current player which time is running out
    private final Move move; // Current move

    /**
     * First parametrized constructor
     * @param gameGUI game GUI
     * @param playersTimerRunning player
     * @param move current move
     * @param seconds seconds tht both players have
     */
    public ChessTimer(GameGUI gameGUI, Player playersTimerRunning, Move move, int seconds){
        secondsOfBlack = seconds;
        secondsOfWhite = seconds;
        this.gameGUI = gameGUI;
        this.playersTimerRunning = playersTimerRunning;
        this.move = move;
        displayTime();
    }

    /**
     * Second parametrized constructor
     * @param gameGUI game GUI
     * @param playersTimerRunning player
     * @param move current move
     * @param secondsOfBlack seconds that black player has
     * @param secondsOfWhite seconds that white player has
     */
    public ChessTimer(GameGUI gameGUI, Player playersTimerRunning, Move move, int secondsOfBlack, int secondsOfWhite){
        this.secondsOfBlack = secondsOfBlack;
        this.secondsOfWhite = secondsOfWhite;
        this.gameGUI = gameGUI;
        this.playersTimerRunning = playersTimerRunning;
        this.move = move;
        displayTime();
    }

    /**
     * Start timer
     */
    @Override
    public void run() {
        while (!isInterrupted() && secondsOfWhite > 0 && secondsOfBlack > 0){
            try {
                sleep(1000);
            } catch (InterruptedException ignored) { interrupt(); }

            if (playersTimerRunning == Player.WHITE){
                secondsOfWhite--;
            } else {
                secondsOfBlack--;
            }

            displayTime();
            gameGUI.repaint();
        }

        synchronized (move){
            if (secondsOfWhite == 0){
                move.whiteTimeLimit = true;
                move.notifyAll();
            }
            if (secondsOfBlack == 0){
                move.blackTimeLimit = true;
                move.notifyAll();
            }
        }
    }

    /**
     * The method switches player which time is running out
     */
    public synchronized void switchPlayer(){
        playersTimerRunning = playersTimerRunning.invert();
    }

    /**
     * The method displays time
     */
    private void displayTime(){
        gameGUI.whiteTime.setText("White time: " + timeToString(secondsOfWhite));
        gameGUI.blackTime.setText("Black time: " + timeToString(secondsOfBlack));
    }

    /**
     * The method converts time to string
     * @param seconds seconds for converting
     * @return seconds as string
     */
    private String timeToString(int seconds){
        return String.valueOf((seconds / 60) % 100) + ":" + secondsToString(seconds % 60);
    }

    /**
     * The method converts seconds to string
     * @param seconds seconds for converting
     * @return seconds as string
     */
    private String secondsToString(int seconds){
        return ((seconds < 10) ? "0" : "") + seconds;
    }
}
