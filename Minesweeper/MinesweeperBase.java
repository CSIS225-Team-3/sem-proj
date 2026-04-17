package Minesweeper;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public abstract class MinesweeperBase extends JPanel {
    /**
     * Gets the list of buttons adjacent to one
     * 
     * @param position The position of the reference button
     * @return the list of buttons adjacent to one
     */
    public abstract MinesweeperButton[] getAdjacentButtons(int[] position);

    public abstract void reset();

    public abstract void onWin();

    public abstract void onLoss();

    public void onTileClick(MouseEvent e) {
        MinesweeperButton pressedButton = (MinesweeperButton) e.getSource();
        if (SwingUtilities.isRightMouseButton(e)) {
            // Right click to flag
            pressedButton.toggleFlagged();
        } else {
            // Normal event
            pressedButton.reveal();
        }
    }

}
