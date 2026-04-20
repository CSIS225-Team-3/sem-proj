package Minesweeper;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public abstract class MinesweeperBase extends JPanel {
    private boolean gameOver = false;

    /**
     * Gets the list of buttons adjacent to one
     * 
     * @param position The position of the reference button
     * @return the list of buttons adjacent to one
     */
    public abstract MinesweeperButton[] getAdjacentButtons(int[] position);

    public void reset(){
        gameOver = false;
    }
    
    /**
     * Runs the victory logic (if the game isn't over)
     */
    public void onWin() {
        if (!gameOver) {
            gameOver = true;
            revealMines();
            if (timer != null) {
                timer.stop();
            }
            JOptionPane.showMessageDialog(this, "You win! Congratulations! \n Time spent: " + timerLabel.getText());
            reset();
        }
    }
    
    /**
     * Runs the loss logic (if the game isn't over)
     */
    public void onLoss() {
        if (!gameOver) {
            gameOver = true;
            revealMines();

            if (timer != null) {
                timer.stop();
            }
            JOptionPane.showMessageDialog(this,
                    "You lose! Better luck next time. \n Time spent: " + timerLabel.getText());
            reset();
        }
    }

    /**
     * Reveals all mines on the board
     */
    protected abstract void revealMines();
    
    /**
     * Gets the number of tiles on this board
     * @return The number of tiles
     */
    public abstract int numTiles();

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
