package Game;

/**
 * State enum
 */
public enum State {
    WIN_OF_WHITE,
    WIN_OF_BLACK,
    PARITY,
    DRAW,
    STALEMATE;


    /**
     * The method reverses turn
     * @param turn turn for reversing
     * @return reversed turn as State
     */
    public static State toReverseState(Turn turn){
        if (turn == Turn.WHITE){
            return WIN_OF_BLACK;
        } else {
            return WIN_OF_WHITE;
        }
    }

    /**
     * The method reverses state
     * @param player player for reversing
     * @return reversed player as State
     */
    public static State toReverseState(Player player){
        if (player == Player.BLACK){
            return WIN_OF_BLACK;
        } else {
            return WIN_OF_WHITE;
        }
    }

    /**
     * The method convert state to string
     * @return converted state
     */
    @Override
    public String toString(){
        if (this.equals(WIN_OF_WHITE)){
            return "Win of white";
        } else if (this.equals(WIN_OF_BLACK)){
            return "Win of black";
        } else if (this.equals(DRAW)){
            return "Draw";
        } else if (this.equals(STALEMATE)){
            return "Stalemate";
        } else {
            return "Parity";
        }
    }
}
