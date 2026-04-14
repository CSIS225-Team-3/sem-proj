package Minesweeper;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A proper game of concentration with a mxn grid of buttons
 *
 * @author Ahyaan Malik & Blue Pyle
 * @version 3/24/2026
 */
public class MinesweeperTwoDimensions extends JPanel implements ActionListener {

    /** The list of numbers for the game */
    private ArrayList<Integer> nums;

    /** The array of buttons for the game */
    private MinesweeperButtons[] buttons;

    /** The label for the top of the window */
    private JLabel topLabel;

    /** The button to start a new game */
    private JButton newGame;

    /** The button to reset the game */
    private JButton reset;

    /** The round number for the game */
    private int roundNum;

    /** The number of rows for the game */
    private int rows;

    /** The number of columns for the game */
    private int cols;

    /** The size of the grid for the game (amount of buttons) */
    private int gridSize;

    /** The CardLayout for managing panels */
    private CardLayout cardLayout;

    /** The JPanel that holds the cards */
    private JPanel cards;

    /**
     * Constructor for the ConcentrationBonus class that initializes the game with
     * the given parameters.
     * 
     * @param rows       the number of rows for the game
     * @param cols       the number of columns for the game
     * @param cardLayout the CardLayout for managing panels
     * @param cards      the JPanel that holds the cards
     */
    public MinesweeperTwoDimensions(int rows, int cols, CardLayout cardLayout, JPanel cards) {
        setLayout(new BorderLayout());
        this.cardLayout = cardLayout;
        this.cards = cards;

        this.rows = rows;
        this.cols = cols;

        gridSize = rows * cols;

        build();
        fullReset();
    }

    /**
     * Private method to build the GUI for the game since run isn't used anymore
     */
    private void build() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topText = new JPanel(new FlowLayout());

        topLabel = new JLabel("Welcome to Minesweeper!");
        updateRoundTitleText();
        topText.add(topLabel);

        mainPanel.add(topText, BorderLayout.NORTH);
        JPanel gamePanel = new JPanel(new GridLayout(rows, cols));

        JPanel bottomButtons = new JPanel(new FlowLayout());
        newGame = new JButton("New Game");
        reset = new JButton("Reset");
        newGame.addActionListener(this);
        reset.addActionListener(this);

        bottomButtons.add(newGame);
        bottomButtons.add(reset);

        mainPanel.add(bottomButtons, BorderLayout.SOUTH);

        nums = new ArrayList<Integer>();
        Random rand = new Random();

        buttons = new MinesweeperButtons[gridSize];
        for (int i = 0; i < gridSize; i++) {
            // TODO: CHANGE LOGIC TO ACTUALLY REFLECT
            buttons[i] = new MinesweeperButtons(" ", i);
            if (rand.nextInt(0, 2) == 0) {
                buttons[i].setMine(true);
            }

            gamePanel.add(buttons[i]);
            buttons[i].addActionListener(this);
        }
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        add(mainPanel);

        setBackground(MainMenu.PRIMARY_COLOR);
        newGame.setBackground(MainMenu.TERTIARY_COLOR);
        reset.setBackground(MainMenu.TERTIARY_COLOR);
        mainPanel.setBackground(MainMenu.SECONDARY_COLOR);
        bottomButtons.setBackground(MainMenu.SECONDARY_COLOR);
        topText.setBackground(MainMenu.SECONDARY_COLOR);

    }

    /**
     * Listens for JButton actions and responds
     * 
     * @param e The JButton to listen for
     */
    public void actionPerformed(ActionEvent e) {

        // Get the text of the pressed JButton
        String pressed = e.getActionCommand();

        // Check text of the pressed JButton
        if (pressed.equals("New Game")) {
            fullReset();
            cardLayout.show(cards, "Menu");

        } else if (pressed.equals("Reset")) {
            reset();

            // Do Logic
        } else {
            // TODO: DO THE ACTUAL LOGIC OF THE GAME
            MinesweeperButtons pressedButton = (MinesweeperButtons) e.getSource();
            pressedButton.onClick();
        }
    }

    /**
     * Private method to update the round title text at the top of the window
     * 
     */
    private void updateRoundTitleText() {
        topLabel.setText("Round " + roundNum);
    }

    /**
     * Private method to reset the game to the start without changing the numbers
     */
    private void reset() {
        roundNum = 1;
        updateRoundTitleText();

        for (int i = 0; i < gridSize; i++) {
            // TODO: FIX
        }
    }

    /**
     * Private method to fully reset the game to the start
     */
    private void fullReset() {
        // TODO: ACTUALLY DO A FULL RESET
        reset();

    }

    /**
     * Private method to check and print if the game is completed
     * 
     */
    private void onWin() {

        // TODO: ACTUALLY CHECK AND PRINT IF COMPLETED

    }
}
