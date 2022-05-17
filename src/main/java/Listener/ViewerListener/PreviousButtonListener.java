package Listener.ViewerListener;

import GUI.ViewerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PreviousButton listener for Viewer GUI
 */
public class PreviousButtonListener implements ActionListener {
    private ViewerGUI viewerGUI; // Viewer GUI

    /**
     * Default constructor
     * @param viewerGUI viewerGUI
     */
    public PreviousButtonListener(ViewerGUI viewerGUI) {
        this.viewerGUI = viewerGUI;
    }

    /**
     * The method decreases currentI and updates viewer
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (viewerGUI.currentI > 0){
            viewerGUI.clearBoard(viewerGUI.currentI);
            viewerGUI.currentI--;
            viewerGUI.drawBoardAndMove(viewerGUI.currentI);
            viewerGUI.nextButton.setEnabled(true);
            if (viewerGUI.currentI == 0){
                viewerGUI.previousButton.setEnabled(false); // Disallow to decrease currentI less than zero
            }
        }
    }
}
