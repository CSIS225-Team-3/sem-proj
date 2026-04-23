package Minesweeper;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Custom class for handling the locations on the minefield
 *
 * @author Ahyaan Malik & Patrick Kosmider
 * @version 4/14/2026
 */
public class MinesweeperButton extends JPanel {
    public final static Color HIDDEN_COLOR = new Color(200, 115, 115);
    public final static Color REVEALED_COLOR = new Color(255, 175, 175);

    MinesweeperBase game;
    private int idx;

    private boolean isRevealed = false;

    /** The number of adjacent mines */
    private int numAdjacent = 0;

    private boolean isMine = false;
    private boolean isFlagged = false;

    private JLabel label;

    private static final ImageIcon MINE_ICON = loadIcon("MinesweeperMine.png");
    private static final ImageIcon FLAG_ICON = loadIcon("MinesweeperFlag.png");

    /**
     * Constructor for MinesweeperButton
     */
    public MinesweeperButton(MinesweeperBase game, int idx) {
        super();
        // super(position[0] + " " + position[1]);

        this.game = game;
        this.idx = idx;

        setBackground(HIDDEN_COLOR);
        setForeground(Color.BLACK);

        label = new JLabel(" ");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);

        // setRolloverEnabled(false); //Mouse hover
        setOpaque(false);
        setPreferredSize(new Dimension(50, 50));
        setMinimumSize(new Dimension(50, 50));
        // setMaximumSize(new Dimension(50, 50));

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
            label.setText(null);
            label.setIcon(MINE_ICON);
            setBackground(Color.RED);
            game.onLoss();
        } else {
            label.setIcon(null);
            setBackground(REVEALED_COLOR);
            if (numAdjacent == 0) {
                label.setText(null);

                MinesweeperButton[] adjacents = game.getAdjacentButtons(getIdx());
                for (int i = 0; i < adjacents.length; i++) {
                    adjacents[i].reveal();
                }
            } else {
                label.setText(String.valueOf(numAdjacent));
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
            label.setIcon(FLAG_ICON);
        } else {
            label.setIcon(null);
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
        label.setText(null);
        label.setIcon(null);
        setBackground(HIDDEN_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (getBorder() != null) {
            g.setColor(getForeground());
        } else {
            g.setColor(getBackground());
        }
        super.paintBorder(g);
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
