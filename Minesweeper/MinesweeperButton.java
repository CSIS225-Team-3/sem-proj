package Minesweeper;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

/**
 * Custom class for handling the locations on the minefield
 *
 * @author Ahyaan Malik & Patrick Kosmider
 * @version 4/14/2026
 */
public class MinesweeperButton extends JButton {
    public final static Color HIDDEN_COLOR = new Color(50, 80, 120);
    public final static Color REVEALED_COLOR = new Color(100, 160, 240);

    MinesweeperEuclidean game;
    private int idx;

    private int buttonSize;

    private boolean isRevealed = false;

    /** The number of adjacent mines */
    private int numAdjacent = 0;

    private boolean isMine = false;
    private boolean isFlagged = false;

    private static final Image MINE_ICON = loadImage("MinesweeperMine.png");
    private static final Image FLAG_ICON = loadImage("MinesweeperFlag.png");

    /**
     * Constructor for MinesweeperButton
     */
    public MinesweeperButton(MinesweeperEuclidean game, int buttonSize, int idx) {
        super((String) null);
        // super(position[0] + " " + position[1]);

        this.game = game;
        this.idx = idx;
        this.buttonSize = buttonSize;

        setBackground(HIDDEN_COLOR);
        setForeground(Color.BLACK);
        setFocusPainted(false);
        // setRolloverEnabled(false); //Mouse hover
        setContentAreaFilled(false);
        setOpaque(false);
        setPreferredSize(new Dimension(buttonSize, buttonSize));
        setMinimumSize(new Dimension(buttonSize, buttonSize));
        setMaximumSize(new Dimension(buttonSize, buttonSize));
        setMargin(new Insets(0, 0, 0, 0));
        setFont(getFont().deriveFont((float) buttonSize / 3));

        // NOTE: Temp disabled for debug purposes
        // setOpaque(true);
        // setBorderPainted(false);
    }

    /**
     * Gets the index of this button
     * 
     * @return the index of this button
     */
    public int getIdx() {
        return idx;
    }

    /**
     * Gets the number of adjacent mines
     * 
     * @return the number of adjacent mines
     */
    public int getNumAdjacent() {
        return numAdjacent;
    }

    /**
     * Reveal the button
     */
    public void reveal() {
        // If already revealed, do nothing
        // Block if it's flagged to avoid accidents
        if (isRevealed || isFlagged)
            return;

        isRevealed = true;

        if (isMine) {
            setText(null);
            setIcon(scaledIcon(MINE_ICON));
            setBackground(Color.RED);
            game.onLoss();
        } else {
            setIcon(null);
            setBackground(REVEALED_COLOR);
            if (numAdjacent == 0) {
                setText(null);

                MinesweeperButton[] adjacents = game.getAdjacentButtons(getIdx());
                for (int i = 0; i < adjacents.length; i++) {
                    adjacents[i].reveal();
                }
            } else {
                String text = String.valueOf(numAdjacent);
                setText(text);
                if (text.length() >= 3) {
                    setFont(getFont().deriveFont((float) buttonSize / 20));
                }
            }
        }

    }

    /**
     * Toggle the flagged status
     */
    public void toggleFlagged() {
        if (isRevealed)
            return;
        isFlagged = !isFlagged;
        if (isFlagged) {
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
        return isFlagged;
    }

    /**
     * Sets if the button is a mine or not
     * 
     * @param isMine True if mine, false if not
     */
    public void setMine(boolean isMine) {
        this.isMine = isMine;

        MinesweeperButton[] adjacents = game.getAdjacentButtons(getIdx());
        for (int i = 0; i < adjacents.length; i++) {
            adjacents[i].numAdjacent++;
        }
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

    public void hide() {
        isRevealed = false;
        setText(null);
        setIcon(null);
        setBackground(HIDDEN_COLOR);
    }

    public void resizeButton(int newSize) {
        this.buttonSize = newSize;
        setPreferredSize(new Dimension(newSize, newSize));
        setMinimumSize(new Dimension(newSize, newSize));
        setMaximumSize(new Dimension(newSize, newSize));
        setFont(getFont().deriveFont((float) newSize / 3));
        if (isRevealed && numAdjacent >= 3) {
            String text = getText();
            if (text != null && text.length() >= 3) {
                setFont(getFont().deriveFont((float) newSize / 20));
            }
        }

        if (isMine && isRevealed) {
            setIcon(scaledIcon(MINE_ICON));
        } else if (isFlagged) {
            setIcon(scaledIcon(FLAG_ICON));
        }
    }

    public void updateDisplayedNumber(boolean subtractFlagged) {
        if (!isRevealed || isMine || numAdjacent == 0) {
            return;
        }

        int display = numAdjacent;
        if (subtractFlagged) {
            for (MinesweeperButton a : game.getAdjacentButtons(idx)) {
                if (a.getFlagged()) {
                    display--;
                }
            }
        }
        setText(String.valueOf(display));
        if (display < 0) {
            setForeground(Color.RED);
        } else {
            setForeground(Color.BLACK);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint the background color manually, ignoring the pressed state
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Paint the text and borders normally
        super.paintComponent(g);
    }

    private static Image loadImage(String name) {
        try {
            return new ImageIcon(MinesweeperButton.class.getResource(name)).getImage();
        } catch (Exception e) {
            System.err.println("Could not load \'" + name + "\' image: " + e.getMessage());
            return null;
        }
    }

    private ImageIcon scaledIcon(Image img) {
        if (img == null)
            return null;
        return new ImageIcon(img.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
    }
}
