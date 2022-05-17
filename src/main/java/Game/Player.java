package Game;

/**
 * Player enum
 */
public enum Player {
    WHITE,
    BLACK;

    /**
     * The method convert turn to player
     * White turn -> white player, Black turn -> black turn
     * @param turn particular turn
     * @return converted turn
     */
    public static Player toPlayer(Turn turn){
        if (turn == Turn.BLACK){
            return Player.BLACK;
        } else {
            return Player.WHITE;
        }
    }

    /**
     * The method converts player to string
     * @return converted player
     */
    @Override
    public String toString(){
        if (this.equals(BLACK)){
            return "Black";
        } else {
            return "White";
        }
    }

    /**
     * The method inverts player: White -> Black, Black -> White
     * @return inverted player
     */
    public Player invert(){
        if (this.equals(BLACK)){
            return Player.WHITE;
        } else {
            return Player.BLACK;
        }
    }
}
