package MainProgram;

import GUI.ChooseMenuGUI;
import GUI.CreatorGUI.CreatorGUI;
import GUI.GameGUI.GameGUI;
import GUI.MainMenuGUI;
import GUI.PreGameGUI;
import GUI.ViewerGUI;
import GameController.GameController;
import Listener.ChooseMenuListener.BackToMenuButtonListener;
import Listener.ChooseMenuListener.CustomGameButtonListener;
import Listener.ChooseMenuListener.DefaultGameButtonListener;
import Listener.MainMenuListener.ExitButtonListener;
import Listener.MainMenuListener.LoadButtonListener;
import Listener.MainMenuListener.NewGameButtonListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Class of Main program
 */
public class MainProgram {
    public GameController gameController; // Game controller
    public JFrame mainFrame; // Main frame
    public final int height; // Height of frame
    public final int width; // Width of frame
    public final int ADDITIONAL_HEIGHT = 24; // Additional height
    public final int HAS_TIME = 600; // Number of seconds for timer
    // GUIs
    public MainMenuGUI menuGUI; // Main menu GUI
    public ChooseMenuGUI chooseMenuGUI; // Choose menu GUI
    public PreGameGUI preGameGUI; // PreGame GUI
    public GameGUI gameGUI; // Game GUI
    public CreatorGUI creatorGUI; // Creator GUI
    public ViewerGUI viewerGUI; // Viewer GUI

    private final double SCALE = 0.8; // Scale
    private final String TITLE = "Chess"; // Title

    /**
     * Init program
     */
    public MainProgram(){
        mainFrame = new JFrame(TITLE);
        height = getDimension();
        width = height;

        init(width, height);

        try {
            mainFrame.setIconImage(ImageIO.read(new File("src/main/resources/Icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainFrame.setContentPane(menuGUI);
        mainFrame.setSize(width, height + ADDITIONAL_HEIGHT);
        mainFrame.setLayout(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * The method returns smallest dimension of the screen
     * @return smallest dimension
     */
    private int getDimension(){
        var dimensions = Toolkit.getDefaultToolkit().getScreenSize();
        return (int)(SCALE * Math.min(dimensions.height, dimensions.width));
    }

    /**
     * The method inits components for program
     * @param width width of frame
     * @param height height of frame
     */
    private void init(int width, int height){
        menuGUI = new MainMenuGUI(width, height);
        chooseMenuGUI = new ChooseMenuGUI(width, height);
        creatorGUI = null;
        gameController = null;
        gameGUI = null;
        preGameGUI = null;
        viewerGUI = null;

        menuGUI.newGameButton.addActionListener(new NewGameButtonListener(mainFrame, chooseMenuGUI));
        menuGUI.loadButton.addActionListener(new LoadButtonListener(this));
        menuGUI.exitButton.addActionListener(new ExitButtonListener(mainFrame));

        chooseMenuGUI.defaultGameButton.addActionListener(new DefaultGameButtonListener(this));
        chooseMenuGUI.customGameButton.addActionListener(new CustomGameButtonListener(this));
        chooseMenuGUI.backToMenuButton.addActionListener(new BackToMenuButtonListener(menuGUI, mainFrame));
    }
}
