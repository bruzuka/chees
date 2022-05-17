package Game;

/**
 * Position class. It contains x and y coordinates.
 */
public class Position {
    public int x;
    public int y;

    /**
     * Default constructor
     */
    public Position(){
        x = 0;
        y = 0;
    }

    /**
     * First parametrized constructor
     * @param x x coordinate
     * @param y y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Second parametrized constructor (copy constructor)
     * @param position position for copy
     */
    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    /**
     * The method defines that one position equals to another one
     * @param o object for comparison
     * @return result of comparison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position)o;

        return x == position.x && y == position.y;
    }

    /**
     * The method converts position to string
     * @return converted position
     */
    @Override
    public String toString() {
        return x + "" + y;
    }

    /**
     * The method converts chars to position
     * @param xAsChar x coordinate as char
     * @param yAsChar y coordinate as char
     * @return converted string
     */
    public static Position stringToPosition(char xAsChar, char yAsChar){
        return new Position(Integer.parseInt(xAsChar + ""), Integer.parseInt(yAsChar + ""));
    }
}
