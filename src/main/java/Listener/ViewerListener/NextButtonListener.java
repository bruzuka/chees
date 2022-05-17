package Listener.ViewerListener;

import GUI.ViewerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * NextButton listener for Viewer GUI
 */
public class NextButtonListener implements ActionListener {
    private ViewerGUI viewerGUI; // Viewer GUI

    /**
     * Default constructor
     * @param viewerGUI viewer GUI
     */
    public NextButtonListener(ViewerGUI viewerGUI) {
        this.viewerGUI = viewerGUI;
    }

    /**
     * The method increments currentI and updates viewer
     * @param e action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (viewerGUI.currentI < viewerGUI.boards.size() - 1){
            viewerGUI.clearBoard(viewerGUI.currentI);
            viewerGUI.currentI++;
            viewerGUI.drawBoardAndMove(viewerGUI.currentI);
            viewerGUI.previousButton.setEnabled(true);
            if (viewerGUI.currentI == viewerGUI.boards.size() - 1){
                viewerGUI.nextButton.setEnabled(false); // Disallow to increase currentI grater than number of boards
            }
        }
    }
}
