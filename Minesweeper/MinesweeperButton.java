package Minesweeper;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Image;

/**
 * Custom class for handling the locations on the minefield
 *
 * @author Ahyaan Malik & Patrick Kosmider
 * @version 4/14/2026
 */
public class MinesweeperButton extends JButton {
    private boolean isRevealed = false;
    
    /** The number of adjacent mines */
    private int adjacent;

    private boolean isMine = false;
    private boolean isFlagged = false;

    private static final ImageIcon MINE_ICON = loadMineIcon();
    private static final ImageIcon FLAG_ICON = loadFlagIcon();

    /**
     * Constructor for MinesweeperButton
     */
    public MinesweeperButton(int index) {
        super(" ");

        setBackground(MainMenu.PRIMARY_COLOR);
        setForeground(Color.BLACK);
        //NOTE: Temp disabled for debug purposes
        // setOpaque(true);
        // setBorderPainted(false);
    }

    private static ImageIcon loadMineIcon() {
        try {
            ImageIcon icon = new ImageIcon(MinesweeperButton.class.getResource("MinesweeperMine.png"));
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("Could not load mine image: " + e.getMessage());
            return null;
        }
    }

    private static ImageIcon loadFlagIcon() {
        try {
            ImageIcon icon = new ImageIcon(MinesweeperButton.class.getResource("MinesweeperFlag.png"));
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("Could not load flag image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the number of adjacent mines
     * @return the number of adjacent mines
     */
    public int getAdjacent() {
        return adjacent;
    }

    /**
     * Reveal the button
     */
    public void reveal() {
        // TODO: HAVE FLAGS BE IMPLEMENTED AND NOT JUST BE MINES
        if (isMine) {
            setText("");
            setIcon(MINE_ICON);
        } else {
            setText(String.valueOf(adjacent));
        }
        isRevealed = true;
    }

    /**
     * Sets if the button is a mine or not
     * 
     * @param isMine True if mine, false if not
     */
    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    /**
     * Gets if the button is a mine or not
     * 
     * @return True if mine, false if not
     */
    public boolean getMine() {
        return isMine;
    }

    /**
     * Gets if the button has been revealed or not
     * 
     * @return boolean on if the button has been revealed or not
     */
    public boolean getRevealed() {
        return isRevealed;
    }
}
