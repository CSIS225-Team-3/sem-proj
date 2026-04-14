package Minesweeper;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Image;

/**
 * Custom JButton with more information
 *
 * @author Ahyaan Malik & Blue Pyle
 * @version 3/24/2026
 */
public class MinesweeperButtons extends JButton {

    /** The index of the button */
    private int index;

    /** The number of the button */
    private String value;

    private boolean isSelected;

    private boolean isMine;

    private static final ImageIcon MINE_ICON = loadMineIcon();

    /**
     * Constructor for ConcentrationButton
     * 
     * @param value the number of the button
     * @param index the index of the button
     */
    public MinesweeperButtons(String value, int index) {
        super(" ");
        this.value = value;
        this.index = index;

        setBackground(MainMenu.PRIMARY_COLOR);
        setForeground(Color.BLACK);
        setOpaque(true);
        setBorderPainted(false);
        isSelected = false;
        isMine = false;

    }

    private static ImageIcon loadMineIcon() {
        try {
            ImageIcon icon = new ImageIcon(MinesweeperButtons.class.getResource("MinesweeperFlag.png"));

            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("Could not load mine image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the value of the button
     * 
     * @return the value of the button
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the index of the button
     * 
     * @return the index of the button
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the color of the button
     * 
     * @param color the color to set the button to
     */
    public void setColor(Color color) {
        setForeground(color);
    }

    /**
     * Sets the value of the button
     * 
     * @param value the value to set the button
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the button on click
     */
    public void onClick() {
        if (isMine && MINE_ICON != null) {
            setText("");
            setIcon(MINE_ICON);
        } else {
            setText(value);
        }
        isSelected = true;
    }

    /**
     * Sets if the button is a mine or not
     * 
     * @param isMine boolean on if the button is a mine or not
     */
    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    /**
     * Gets if the button is a mine or not
     * 
     * @return boolean on if the button is a mine or not
     */
    public boolean getMine() {
        return isMine;
    }

    /**
     * Gets if the button has been selected or not
     * 
     * @return boolean on if the button has been selected or not
     */
    public boolean getSelected() {
        return isSelected;
    }

}
