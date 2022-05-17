package Util;

import GUI.GameGUI.GameGUI;
import Game.ChessEngine;
import Game.Player;
import Game.State;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class of Board Saver
 */
public class BoardSaver {
    private BoardSaver(){} // Prohibited to create

    /**
     * The method saves engine and game state as txt file
     * @param writer file writer
     * @param engine chess engine
     * @param gameGUI game GUI
     * @param vsPC vsPC flag
     * @throws IOException
     */
    public static void save(FileWriter writer, ChessEngine engine, GameGUI gameGUI, boolean vsPC) throws IOException {
        writePreamble(writer, engine);
        writer.write(vsPC + "\n");
        writer.write(gameGUI.whiteTime.getText() + "\n");
        writer.write(gameGUI.blackTime.getText() + "\n");
        writer.write(gameGUI.movesHistoryText.getText() + "\n");
        writer.write(engine.toString()); // Write encoded chess engine
    }

    /**
     * The method writes to the beginning of file PGN preamble
     * @param writer file writer
     * @param engine chess engine
     * @throws IOException
     */
    private static void writePreamble(FileWriter writer, ChessEngine engine) throws IOException{
        var formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        var stateOfWhite = engine.checkForImmediateEnd(Player.WHITE);
        var stateOfBlack = engine.checkForImmediateEnd(Player.BLACK);
        String result = "*";

        if (stateOfWhite == State.WIN_OF_BLACK){
            result = "0-1";
        } else if (stateOfBlack == State.WIN_OF_WHITE){
            result = "1-0";
        } else if (stateOfWhite == State.DRAW || stateOfWhite == State.STALEMATE ||
                stateOfBlack == State.DRAW || stateOfBlack == State.STALEMATE){
            result = "1/2-1/2";
        }

        String initialString = String.format("""
                [Event "Unknown"]
                [Site "Unknown"]
                [Date "%s"]
                [Round "Unknown"]
                [White "Unknown"]
                [Black "Unknown"]
                [Result "%s"]
                """, formatter.format(date), result);

        writer.write(initialString);
    }
}
