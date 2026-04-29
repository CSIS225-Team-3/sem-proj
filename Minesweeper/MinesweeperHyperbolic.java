package Minesweeper;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A 2D game of minesweeper
 *
 * @author Patrick Kosmider
 * @version 4/28/2026
 */
public class MinesweeperHyperbolic extends MinesweeperBase implements ActionListener {
    /** The array of buttons for the game */
    private MinesweeperHBButton[] buttons;

    private Tile[] allTiles;
    private HashMap<Tile, MinesweeperHBButton> visibleTiles = new HashMap<>();

    private int mineCount;

    private boolean firstClick = true;

    /** The label for the top of the window */
    private JLabel topLabel;

    /** The button to start a new game */
    private JButton newGame;

    /** The button to reset the game */
    private JButton reset;

    /** The round number for the game */
    private int roundNum;

    private JScrollPane scrollPane;

    /** The size of the grid for the game (amount of buttons) */
    private int gridVolume;

    /** The CardLayout for managing panels */
    private CardLayout cardLayout;

    /** The JPanel that holds the cards */
    private JPanel cards;

    private BufferedImage bgImage;

    /**
     * Constructor for the MinesweeperEuclidean class that initializes the game with
     * the given parameters.
     * 
     * @param mines      the number of mines for the game
     * @param cardLayout the CardLayout for managing panels
     * @param cards      the JPanel that holds the cards
     */
    public MinesweeperHyperbolic(int mines, CardLayout cardLayout, JPanel cards) {
        super();

        this.mineCount = mines;
        this.cardLayout = cardLayout;
        this.cards = cards;

        allTiles = HyperbolicGraphGen.gen();

        try {
            bgImage = ImageIO.read(new File("Minesweeper/InGameBackground.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        gridVolume = 1;
        // for (int i = 0; i < dims.length; i++) {
        //     gridVolume *= dims[i];
        // }

        reset();
    }

    /**
     * Builds the GUI for the game
     */
    private void build() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel topText = new JPanel(new FlowLayout());

        topText.setLayout(new BorderLayout());

        topLabel = new JLabel();
        updateTitleText();
        topLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        timerLabel = new JLabel("00:00");
        timerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        topText.add(topLabel, BorderLayout.WEST);
        topText.add(timerLabel, BorderLayout.EAST);

        mainPanel.add(topText, BorderLayout.NORTH);

        JPanel bottomButtons = new JPanel(new FlowLayout());
        newGame = new JButton("New Game");
        reset = new JButton("Reset");
        newGame.addActionListener(this);
        reset.addActionListener(this);

        bottomButtons.add(newGame);
        bottomButtons.add(reset);

        mainPanel.add(bottomButtons, BorderLayout.SOUTH);

        final int viewButtons = 13;
        buttons = new MinesweeperHBButton[viewButtons];

        for (int i = 0; i < viewButtons; i++) {
            MinesweeperHBButton button = buttons[i] = new MinesweeperHBButton(this);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBackground(MainMenu.PRIMARY_COLOR);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (firstClick && !SwingUtilities.isRightMouseButton(e)) {
                        firstClick = false;

                        ActionListener timerListener = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                secondsElapsed++;
                                int m = secondsElapsed / 60;
                                int s = secondsElapsed % 60;
                                timerLabel.setText(String.format("%02d:%02d", m, s));
                            }
                        };
                        timer = new Timer(1000, timerListener);
                        timer.start();

                        placeMines(((MinesweeperHBButton) e.getSource()).getTile());
                    }
                    onTileClick(e);
                    updateTitleText();
                    checkWin();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    MinesweeperHBButton b = (MinesweeperHBButton) e.getSource();
                    highlightNeighbors(b);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    clearHighlights();
                }
            });
        }

        JPanel gamePanel = new JPanel();
        gamePanel.setOpaque(false);

        gamePanel.setLayout(new GridLayout(5, 5));
        int btnIdx = 0;
        //-###-
        gamePanel.add(new JPanel());
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(new JPanel());
        //#---#
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(new JPanel());
        gamePanel.add(new JPanel());
        gamePanel.add(new JPanel());
        gamePanel.add(buttons[btnIdx++]);
        //#-#-#
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(new JPanel());
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(new JPanel());
        gamePanel.add(buttons[btnIdx++]);
        //#---#
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(new JPanel());
        gamePanel.add(new JPanel());
        gamePanel.add(new JPanel());
        gamePanel.add(buttons[btnIdx++]);
        //-###-
        gamePanel.add(new JPanel());
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(buttons[btnIdx++]);
        gamePanel.add(new JPanel());
        if (btnIdx != viewButtons)
            throw new IllegalStateException("Wrong number of buttons");

        scrollPane = new JScrollPane(gamePanel);
        
        gamePanel.setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));

        // for (int j = 0; j < dims[1]; j++){
        // for (int i = 0; i < dims[0]; i++){
        // buttons[i][j].reveal();
        // }
        // }
        
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        newGame.setBackground(MainMenu.TERTIARY_COLOR);
        reset.setBackground(MainMenu.TERTIARY_COLOR);
        bottomButtons.setOpaque(false);
        topText.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
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
            if (e.getSource() instanceof MinesweeperHBButton) {
                MinesweeperHBButton button = (MinesweeperHBButton) e.getSource();
                button.reveal();
            }
        }
    }

    private void placeMines(Tile startTile) {
        throw new IllegalStateException("Not implemented yet");
        // int[] clickPos = idxToPos(clickIdx);

        // ArrayList<int[]> allPositions = new ArrayList<>();
        // for (MinesweeperHBButton b : buttons) {
        //     int[] pos = idxToPos(b.getIdx());
        //     for (int d = 0; d < pos.length; d++) {

        //         // If the point is more than 1 units away in any dimension
        //         if (Math.abs(pos[d] - clickPos[d]) > 1) {
        //             allPositions.add(pos);
        //             break;
        //         }

        //     }
        // }

        // java.util.Collections.shuffle(allPositions);

        // int actualMineCount = Math.min(mineCount, allPositions.size());

        // for (int i = 0; i < actualMineCount; i++) {
        //     int[] pos = allPositions.get(i);
        //     int idx = posToIdx(pos);
        //     if (buttons[idx].getMine())
        //         throw new IllegalStateException("Placing mine on existing mine");
        //     buttons[idx].setMine(true);
        // }
    }

    /**
     * Updates the round title text at the top of the window
     */
    private void updateTitleText() {
        topLabel.setText("Round " + roundNum);
    }

    /**
     * Resets the game to the starting state
     */
    @Override
    public void reset() {
        super.reset();

        removeAll();

        if (timer != null) {
            timer.stop();
        }
        secondsElapsed = 0;
        build();
        revalidate();
        repaint();

        firstClick = true;
        roundNum++;
    }

    private void checkWin() {
        for (MinesweeperHBButton b : buttons) {
            if (!b.getMine() && !b.getRevealed()) {
                return;
            }
        }

        onWin();
    }

    @Override
    protected void revealMines() {
        for (MinesweeperHBButton b : buttons)
            if (b.getMine())
                b.reveal();
    }

    /**
     * Gets the list of buttons adjacent to one
     * @param position The index of the reference button
     * @return the list of buttons adjacent to one
     */
    public MinesweeperHBButton[] getAdjacentButtons(MinesweeperHBButton ref) {
        Tile t = ref.getTile();

        if (t == null)
            return new MinesweeperHBButton[0];

        HashSet<Tile> adjs = new HashSet<>();
        for (Vert v : t.vertices)
            adjs.addAll(v.tiles);
        
        return adjs.stream().map(visibleTiles::get).toArray(MinesweeperHBButton[]::new);
    }

    private void highlightNeighbors(MinesweeperHBButton button) {
        MinesweeperHBButton[] adj = getAdjacentButtons(button);

        for (MinesweeperHBButton b : adj) {
            if (!b.getRevealed()) {
                b.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 3));
            }
        }
    }

    private void clearHighlights() {
        for (MinesweeperHBButton b : buttons) {
            b.setBorder(UIManager.getBorder("Button.border"));
        }
    }

    @Override
    public int numTiles() {
        return gridVolume;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            super.paintComponent(g);
        }
    }
}
