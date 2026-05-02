package Minesweeper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * The base class for Minesweeper buttons, containing shared properties and
 * methods for both Euclidean and Hyperbolic modes.
 * 
 * @author Ahyaan Malik & Patrick Kosmider
 * @version 4/30/2026
 */
public abstract class MinesweeperButtonBase extends JButton {
    public final static Color HIDDEN_COLOR = new Color(50, 80, 120);
    public final static Color REVEALED_COLOR = new Color(100, 160, 240);

    protected static final Image MINE_ICON = loadImage("MinesweeperMine.png");
    protected static final Image FLAG_ICON = loadImage("MinesweeperFlag.png");

    public MinesweeperButtonBase() {
        super((String) null);
    }

    /**
     * Reveal the button
     */
    public abstract void reveal();

    /**
     * Toggle the flagged status
     */
    public abstract void toggleFlagged();

    @Override
    /**
     * Override paintComponent to ensure the background color is painted correctly
     */
    protected void paintComponent(Graphics g) {
        // Paint the background color manually, ignoring the pressed state
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Paint the text and borders normally
        super.paintComponent(g);
    }

    /**
     * Loads an image from the resources folder
     * 
     * @param name the name of the image file to load
     * @return the loaded Image, or null if the image could not be loaded
     */
    private static Image loadImage(String name) {
        try {
            return new ImageIcon(MinesweeperButton.class.getResource(name)).getImage();
        } catch (Exception e) {
            System.err.println("Could not load \'" + name + "\' image: " + e.getMessage());
            return null;
        }
    }
}
