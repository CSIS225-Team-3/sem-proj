package Minesweeper;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

/**
 * Custom class for handling the locations on the minefield
 *
 * @author Ahyaan Malik & Patrick Kosmider
 * @version 4/14/2026
 */
public class MinesweeperHBButton extends MinesweeperButtonBase {
    /** The game instance this button belongs to */
    MinesweeperHyperbolic game;

    /** The tile this button represents */
    private Tile tile = null;

    /**
     * Constructor for MinesweeperButton
     */
    public MinesweeperHBButton(MinesweeperHyperbolic game) {
        // super(position[0] + " " + position[1]);

        this.game = game;

        setBackground(HIDDEN_COLOR);
        setForeground(Color.BLACK);
        setFocusPainted(false);
        // setRolloverEnabled(false); //Mouse hover
        setContentAreaFilled(false);
        setOpaque(false);
        setPreferredSize(new Dimension(50, 50));
        setMinimumSize(new Dimension(50, 50));
        setMaximumSize(new Dimension(50, 50));

        // NOTE: Temp disabled for debug purposes
        // setOpaque(true);
        // setBorderPainted(false);
    }

    /**
     * Gets the tile this button is showing (or null if not representing a tile)
     * 
     * @return the tile this button is showing
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Sets the tile for this button to show (or null if nothing to show)
     * 
     * @param newTile The tile this button should show
     */
    public void setTile(Tile newTile) {
        tile = newTile;

        redraw();
    }

    /**
     * Redraws the button based on the current tile's properties, including revealed
     * status, mine status, and number of adjacent mines
     */
    public void redraw() {
        // if (tile == null)
        // setText("Null");
        // else
        // setText(String.valueOf(tile.id));

        setBackground(HIDDEN_COLOR);
        setForeground(Color.BLACK);
        setText(null);
        setIcon(null);

        if (tile == null)
            return;

        if (tile.getRevealed()) {
            if (tile.getMine()) {
                setBackground(Color.RED);
                setText(null);
                setIcon(scaledIcon(MINE_ICON));
            } else {
                setBackground(REVEALED_COLOR);
                if (tile.getNumAdjacent() != 0) {
                    setText(String.valueOf(tile.getNumAdjacent()));
                }
            }
        } else if (tile.getFlagged()) {
            setIcon(scaledIcon(FLAG_ICON));
        }
    }

    /**
     * Gets the number of adjacent mines
     * 
     * @return the number of adjacent mines
     */
    public int getNumAdjacent() {
        return tile.getNumAdjacent();
    }

    /**
     * Reveal the button
     */
    @Override
    public void reveal() {
        // If already revealed, do nothing
        // Block if it's flagged to avoid accidents
        if (tile.getRevealed() || tile.getFlagged())
            return;

        var wasMine = tile.reveal();
        if (wasMine) {
            game.onLoss();
        }
    }

    /**
     * Toggle the flagged status
     */
    @Override
    public void toggleFlagged() {
        if (getRevealed())
            return;
        tile.toggleFlagged();
        if (tile.getFlagged()) {
            setIcon(scaledIcon(FLAG_ICON));
        } else {
            setIcon(null);
        }
    }

    /**
     * Gets the flagged status
     * 
     * @return the flagged status
     */
    public boolean getFlagged() {
        return tile.getFlagged();
    }

    /**
     * Gets if the button is a mine or not
     * 
     * @return True if mine, false if not
     */
    public boolean getMine() {
        return tile.getMine();
    }

    /**
     * Gets if the button has been revealed or not
     * 
     * @return boolean on if the button has been revealed or not
     */
    public boolean getRevealed() {
        return tile.getRevealed();
    }

    /**
     * Gets the image icon for the mine, scaled to fit the button size
     * @param img the image to scale and convert to an ImageIcon
     * @return The image as an icon
     */
    private ImageIcon scaledIcon(Image img) {
        if (img == null)
            return null;
        final int buttonSize = 32;
        return new ImageIcon(img.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
    }
}
