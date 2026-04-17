package Minesweeper;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

/**
 * A 2D game of minesweeper
 *
 * @author Ahyaan Malik & Patrick Kosmider
 * @version 4/14/2026
 */
public class MinesweeperTwoDimensions extends MinesweeperBase implements ActionListener {
    /** The array of buttons for the game */
    private MinesweeperButton[][] buttons;

    private int mineCount;

    private boolean firstClick;

    /** The label for the top of the window */
    private JLabel topLabel;

    /** The button to start a new game */
    private JButton newGame;

    /** The button to reset the game */
    private JButton reset;

    /** The round number for the game */
    private int roundNum;

    /** The dimensions of the game */
    private int[] dims;

    /** The size of the grid for the game (amount of buttons) */
    private int gridSize;

    /** The CardLayout for managing panels */
    private CardLayout cardLayout;

    /** The JPanel that holds the cards */
    private JPanel cards;

    private boolean gameOver = false;

    /**
     * Constructor for the ConcentrationBonus class that initializes the game with
     * the given parameters.
     * 
     * @param rows       the number of rows for the game
     * @param cols       the number of columns for the game
     * @param mines      the number of mines for the game
     * @param cardLayout the CardLayout for managing panels
     * @param cards      the JPanel that holds the cards
     */
    public MinesweeperTwoDimensions(int rows, int cols, int mines, CardLayout cardLayout, JPanel cards) {
        setLayout(new BorderLayout());
        this.cardLayout = cardLayout;
        this.cards = cards;
        this.mineCount = mines;

        firstClick = true;

        dims = new int[] {
                cols,
                rows,
        };

        gridSize = rows * cols;

        reset();
    }

    /**
     * Private method to build the GUI for the game since run isn't used anymore
     */
    private void build() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topText = new JPanel(new FlowLayout());

        topLabel = new JLabel("Welcome to Minesweeper!");
        updateTitleText();
        topText.add(topLabel);

        mainPanel.add(topText, BorderLayout.NORTH);
        JPanel gamePanel = new JPanel(new GridLayout(dims[1], dims[0]));

        JPanel bottomButtons = new JPanel(new FlowLayout());
        newGame = new JButton("New Game");
        reset = new JButton("Reset");
        newGame.addActionListener(this);
        reset.addActionListener(this);

        bottomButtons.add(newGame);
        bottomButtons.add(reset);

        mainPanel.add(bottomButtons, BorderLayout.SOUTH);

        buttons = new MinesweeperButton[dims[0]][dims[1]];
        for (int j = 0; j < dims[1]; j++) {
            for (int i = 0; i < dims[0]; i++) {
                int[] pos = { i, j };
                MinesweeperButton button = buttons[i][j] = new MinesweeperButton(this, pos);
                gamePanel.add(button);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (firstClick && !SwingUtilities.isRightMouseButton(e)) {
                            firstClick = false;
                            int[] clickedPos = ((MinesweeperButton) e.getSource()).getPosition();
                            placeMines(clickedPos[0], clickedPos[1]);
                        }
                        onTileClick(e);
                        updateTitleText();
                        checkWin();
                    }
                });
            }
        }

        // for (int j = 0; j < dims[1]; j++){
        // for (int i = 0; i < dims[0]; i++){
        // buttons[i][j].reveal();
        // }
        // }
        JScrollPane scrollPane = new JScrollPane(gamePanel);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        setBackground(MainMenu.PRIMARY_COLOR);
        newGame.setBackground(MainMenu.TERTIARY_COLOR);
        reset.setBackground(MainMenu.TERTIARY_COLOR);
        mainPanel.setBackground(MainMenu.SECONDARY_COLOR);
        bottomButtons.setBackground(MainMenu.SECONDARY_COLOR);
        topText.setBackground(MainMenu.SECONDARY_COLOR);
        scrollPane.getViewport().setBackground(MainMenu.SECONDARY_COLOR);
        scrollPane.getVerticalScrollBar().setBackground(MainMenu.PRIMARY_COLOR);
        scrollPane.getHorizontalScrollBar().setBackground(MainMenu.PRIMARY_COLOR);
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
            reset();
            cardLayout.show(cards, "Menu");

        } else if (pressed.equals("Reset")) {
            reset();
        } else {
            if (e.getSource() instanceof MinesweeperButton) {
                MinesweeperButton button = (MinesweeperButton) e.getSource();
                button.reveal();
            }
        }
    }

    private void placeMines(int x, int y) {

        ArrayList<int[]> allPositions = new ArrayList<>();
        for (int i = 0; i < dims[0]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                if (Math.abs(i - x) > 1 || Math.abs(j - y) > 1) {
                    allPositions.add(new int[] { i, j });
                }
            }
        }
        java.util.Collections.shuffle(allPositions);

        int actualMineCount = Math.min(mineCount, allPositions.size());

        for (int i = 0; i < actualMineCount; i++) {
            int[] pos = allPositions.get(i);
            buttons[pos[0]][pos[1]].setMine(true);
        }
    }

    /**
     * Private method to update the round title text at the top of the window
     * 
     */
    private void updateTitleText() {
        topLabel.setText("Round " + roundNum);
    }

    /**
     * Private method to reset the game to the start
     */
    private void reset() {
        removeAll();
        build();
        revalidate();
        repaint();

        gameOver = false;
        firstClick = true;
        roundNum++;
    }

    private void checkWin() {
        for (int j = 0; j < dims[1]; j++) {
            for (int i = 0; i < dims[0]; i++) {
                if (!buttons[i][j].getMine() && !buttons[i][j].getRevealed()) {
                    return;
                }
            }
        }

        onWin();
    }

    private void revealMines() {
        for (int j = 0; j < dims[1]; j++) {
            for (int i = 0; i < dims[0]; i++) {
                if (buttons[i][j].getMine()) {
                    buttons[i][j].reveal();
                }
            }
        }
    }

    /**
     * Private method to check and print if the game is completed
     * 
     */
    @Override
    public void onWin() {
        if (!gameOver) {
            gameOver = true;
            revealMines();
            JOptionPane.showMessageDialog(this, "You win! Congratulations!");
            reset();
        }
    }

    @Override
    public void onLoss() {
        if (!gameOver) {
            gameOver = true;
            revealMines();

            JOptionPane.showMessageDialog(this, "You lose! Better luck next time.");

            reset();
        }
    }

    @Override
    public MinesweeperButton[] getAdjacentButtons(int[] position) {
        int x = position[0];
        int y = position[1];

        int minX = Math.max(0, x - 1);
        int minY = Math.max(0, y - 1);

        int maxX = Math.min(dims[0] - 1, x + 1);
        int maxY = Math.min(dims[1] - 1, y + 1);

        ArrayList<MinesweeperButton> adjs = new ArrayList<>();
        for (int a = minX; a <= maxX; a++) {
            for (int b = minY; b <= maxY; b++) {
                if (a == x && b == y)
                    continue;
                adjs.add(buttons[a][b]);
            }
        }

        return adjs.toArray(new MinesweeperButton[0]);
    }

    public int getGridSize() {
        return gridSize;
    }
}
