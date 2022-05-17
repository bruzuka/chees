package GUI.GameGUI.ImageGUI;

/**
 * Class of MarkedCellGUI
 * It uses for showing possible moves
 */
public class MarkedCellGUI {
    public ImageGUI imageGUI; //ImageGUI
    private double realX; // x in frame coordinates
    private double realY; // y in frame coordinates
    private double height; // height of component
    private double width; // width of component
    private int shiftX; // initial shift of x
    private int shiftY; // initial shift of y

    /**
     * Default constructor
     * Blue stands for usual move
     * Red stands for move with killing
     * @param shiftX initial shift x
     * @param shiftY initial shift y
     * @param x x in chess coordinates [0-7]
     * @param y y in chess coordinates [0-7]
     * @param width width of component
     * @param height height of component
     * @param isBlue is blue cell?
     */
    public MarkedCellGUI(int shiftX, int shiftY, int x, int y, double width, double height, boolean isBlue){
        this.width = width;
        this.height = height;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        String path = "src/main/resources/" + (isBlue ? "Blue.png" : "Red.png");
        toRealCoordinates(x, y);

        imageGUI = new ImageGUI((int)realX, (int)realY, (int)width, (int)height, path);
    }

    /**
     * The method translates chess coordinates to real
     * @param x x in chess coordinates
     * @param y y in chess coordinates
     */
    private void toRealCoordinates(int x, int y){
        realX = x * width + shiftX;
        realY = (7 - y) * height + shiftY;
    }

    /**
     * The method checks that the cursor on the gui
     * @param x x coordinate of cursor
     * @param y y coordinate of cursor
     * @return is mouse on piece
     */
    public boolean isMouseOnPiece(int x, int y){
        return realX <= x && x <= realX + width && realY <= y && y <= realY + height;
    }
}
