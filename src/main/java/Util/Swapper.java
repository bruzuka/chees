package Util;

import javax.swing.*;

/**
 * Class of swapper
 */
public class Swapper {
    private Swapper(){} // Prohibited to create

    /**
     * The method changes current content pane
     * @param frame main frame
     * @param newJPanel new JPanel
     */
    static public void swapContentPane(JFrame frame, JPanel newJPanel){
        frame.getContentPane().setVisible(false);
        newJPanel.setVisible(true);
        frame.setContentPane(newJPanel);
    }
}
