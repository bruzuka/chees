package Game.Pieces;

import Game.Player;
import Game.Position;

public abstract class Piece {
    public Position pos; // Position
    public Player player; // Player (White or Black)

    Piece(){
        this.pos = new Position();
        this.player = Player.WHITE;
    }

}
