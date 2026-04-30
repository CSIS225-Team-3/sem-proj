package Minesweeper;

import javax.swing.JButton;

public abstract class MinesweeperButtonBase extends JButton {
    public MinesweeperButtonBase(){
        super((String)null);
    }

    /**
     * Reveal the button
     */
    public abstract void reveal();

    /**
     * Toggle the flagged status
     */
    public abstract void toggleFlagged();
}
