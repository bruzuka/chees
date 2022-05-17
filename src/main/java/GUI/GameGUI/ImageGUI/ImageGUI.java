package GUI.GameGUI.ImageGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Class of imageGUI
 * It stores image of component, x and y coordinates, sx and sy scale factors
 */
public class ImageGUI extends JComponent {
    private BufferedImage img; // Image of component
    private final double sX; // Scale X factor
    private final double sY; // Scale Y factor
    public int realX; // x in frame coordinates
    public int realY; // y in frame coordinates

    /**
     * Default constructor
     * @param realX x in frame coordinates
     * @param realY y in frame coordinates
     * @param width width of component
     * @param height height of component
     * @param path path to image of component
     */
    public ImageGUI(int realX, int realY, int width, int height, String path){
        super();
        this.realX = realX;
        this.realY = realY;

        try {
            img = ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        sX = (double) width / img.getWidth(); // Image width -> width in frame coordinates
        sY = (double) height / img.getHeight(); // Image height -> height in frame coordinates

        setBounds(realX, realY, img.getWidth(), img.getHeight());
        setOpaque(true);
    }

    /**
     * The method draws component
     * @param g graphics
     */
    private void doDrawing(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.scale(sX, sY);
        g2d.drawImage(img, 0, 0, null);
    }

    /**
     * The method overrides classical one
     * It draws image
     * @param g graphics
     * @See paint(Graphics g)
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        doDrawing(g);
    }
}
