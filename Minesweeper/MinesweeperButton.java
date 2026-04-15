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
    public final static Color HIDDEN_COLOR = new Color(200, 115, 115);
    public final static Color REVEALED_COLOR = new Color(255, 175, 175);

    MinesweeperBase game;
    private int[] position;

    private boolean isRevealed = false;
    
    /** The number of adjacent mines */
    private int numAdjacent = 0;

    private boolean isMine = false;
    private boolean isFlagged = false;

    private static final ImageIcon MINE_ICON = loadIcon("MinesweeperMine.png");
    private static final ImageIcon FLAG_ICON = loadIcon("MinesweeperFlag.png");

    /**
     * Constructor for MinesweeperButton
     */
    public MinesweeperButton(MinesweeperBase game, int[] position) {
        super((String)null);
        // super(position[0] + " " + position[1]);

        this.game = game;
        this.position = position;

        setBackground(HIDDEN_COLOR);
        setForeground(Color.BLACK);
        //NOTE: Temp disabled for debug purposes
        // setOpaque(true);
        // setBorderPainted(false);
    }

    /**
     * Gets the position of this button
     * @return the position of this button
     */
    public int[] getPosition(){
        //Technically this is mutable, but should be fine
        return position;
    }

    /**
     * Gets the number of adjacent mines
     * @return the number of adjacent mines
     */
    public int getNumAdjacent() {
        return numAdjacent;
    }

    /**
     * Reveal the button
     */
    public void reveal() {
        //Block if it's flagged to avoid accidents
        if (isFlagged)
            return;

        if (isMine) {
            setText("M");
            setIcon(MINE_ICON);
            setBackground(Color.RED);
        } else {
            setText(String.valueOf(numAdjacent));
            setIcon(null);
            setBackground(REVEALED_COLOR);
        }
        isRevealed = true;
    }

    /**
     * Toggle the flagged status
     */
    public void toggleFlagged(){
        if (isRevealed)
            return;
        isFlagged = !isFlagged;
        if (isFlagged){
            setIcon(FLAG_ICON);
        } else{
            setIcon(null);
        }
    }

    /**
     * Gets the flagged status
     * @return the flagged status
     */
    public boolean getFlagged(){
        return isFlagged;
    }

    /**
     * Sets if the button is a mine or not
     * @param isMine True if mine, false if not
     */
    public void setMine(boolean isMine) {
        this.isMine = isMine;
        
        MinesweeperButton[] adjacents = game.getAdjacentButtons(getPosition());
        for (int i = 0; i < adjacents.length; i++){
            adjacents[i].numAdjacent++;
        }
    }

    /**
     * Gets if the button is a mine or not
     * @return True if mine, false if not
     */
    public boolean getMine() {
        return isMine;
    }

    /**
     * Gets if the button has been revealed or not
     * @return boolean on if the button has been revealed or not
     */
    public boolean getRevealed() {
        return isRevealed;
    }
    


    private static ImageIcon loadIcon(String name) {
        try {
            ImageIcon icon = new ImageIcon(MinesweeperButton.class.getResource(name));
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("Could not load \'" + name + "\' image: " + e.getMessage());
            return null;
        }
    }
}
