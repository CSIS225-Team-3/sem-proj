package Minesweeper;

import javax.swing.JPanel;

public abstract class MinesweeperBase extends JPanel {
    /**
     * Gets the list of buttons adjacent to one
     * @param position The position of the reference button
     * @return the list of buttons adjacent to one
     */
    public abstract MinesweeperButton[] getAdjacentButtons(int[] position);
}
