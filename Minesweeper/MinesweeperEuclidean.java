package Minesweeper;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.awt.event.ActionEvent;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.util.ArrayList;

/**
 * A 2D game of minesweeper
 *
 * @author Ahyaan Malik & Patrick Kosmider
 * @version 4/14/2026
 */
public class MinesweeperEuclidean extends MinesweeperBase implements ActionListener {
    /** The array of buttons for the game */
    private MinesweeperButton[] buttons;

    private int mineCount;

    private boolean firstClick = true;

    private boolean subtractFlagged = false;

    private JCheckBox subtractBox;

    /** The label for the top of the window */
    private JLabel topLabel;

    /** The button to start a new game */
    private JButton newGame;

    /** The button to reset the game */
    private JButton reset;

    private JButton autoplay;

    /** If the autoplay feature has ever been toggled on, used to validate leaderboards */
    private boolean autoplayActive;

    private JPanel autoplayPanel;

    private JButton autoplayStart;

    private JButton autoplayPause;

    private JSlider autoplaySpeedSlider;

    private Timer autoplayTimer;

    /** The dimensions of the game */
    private int[] dims;

    private int buttonSize;

    private JPanel[] splices;

    private JPanel gamePanel;

    private JScrollPane scrollPane;

    private int numSplices;
    private int outerCols;
    private int outerRows;

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
     * @param rows       the number of rows for the game
     * @param cols       the number of columns for the game
     * @param mines      the number of mines for the game
     * @param cardLayout the CardLayout for managing panels
     * @param cards      the JPanel that holds the cards
     */
    public MinesweeperEuclidean(int[] dims, int mines, int buttonSize, CardLayout cardLayout, JPanel cards) {
        super();

        if (dims.length == 0)
            throw new InvalidParameterException("0-dimensional space not supported");

        this.dims = dims;
        this.mineCount = mines;
        this.buttonSize = buttonSize;
        this.cardLayout = cardLayout;
        this.cards = cards;

        try {
            bgImage = ImageIO.read(new File("Minesweeper/InGameBackground.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        gridVolume = 1;
        for (int i = 0; i < dims.length; i++) {
            gridVolume *= dims[i];
        }

        reset();
    }

    /**
     * Builds the GUI for the game
     */
    private void build() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel topText = new JPanel(new BorderLayout());
        topText.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        topLabel = new JLabel();
        updateTitleText();
        topLabel.setForeground(Color.WHITE);
        topLabel.setFont(topLabel.getFont().deriveFont(Font.BOLD, 15f));
        topLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        timerLabel = new JLabel("00:00");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(timerLabel.getFont().deriveFont(Font.BOLD, 15f));

        JSlider sizeSlider = new JSlider(16, 64, buttonSize);
        sizeSlider.setOpaque(false);
        sizeSlider.setMaximumSize(new Dimension(140, 30));
        sizeSlider.setPreferredSize(new Dimension(140, 30));
        sizeSlider.addChangeListener(e -> updateButtonSize(sizeSlider.getValue()));

        JLabel sizeIcon = new JLabel("\uD83D\uDD0D"); // will make this icon: 🔍
        sizeIcon.setForeground(Color.LIGHT_GRAY);
        sizeIcon.setFont(sizeIcon.getFont().deriveFont(13f));
        sizeIcon.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 2));

        JPanel sliderGroup = new JPanel();
        sliderGroup.setLayout(new BoxLayout(sliderGroup, BoxLayout.X_AXIS));
        sliderGroup.setOpaque(false);
        sliderGroup.add(Box.createHorizontalStrut(20)); // gap
        sliderGroup.add(sizeIcon);
        sliderGroup.add(sizeSlider);

        subtractBox = new JCheckBox("Smart Numbers");
        subtractBox.setOpaque(false);
        subtractBox.setFocusPainted(false);
        subtractBox.setForeground(Color.WHITE);
        subtractBox.setSelected(subtractFlagged);
        subtractBox.addActionListener(e -> {
            subtractFlagged = subtractBox.isSelected();
            refreshNumbers();
        });

        autoplayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        autoplayPanel.setOpaque(false);
        TitledBorder autoplayBorder = BorderFactory.createTitledBorder("Autoplay");
        autoplayBorder.setTitleColor(Color.WHITE);
        autoplayPanel.setBorder(autoplayBorder);
        autoplayPanel.setPreferredSize(new Dimension(260, 58));
        autoplayPanel.setMaximumSize(new Dimension(260, 58));
        autoplayStart = MainMenu.styledButton("Start");
        autoplayStart.addActionListener(e -> {
            startAutoplay();
        });

        autoplayPause = MainMenu.styledButton("Pause");
        autoplayPause.addActionListener(e -> {
            stopAutoplay();
        });

        JLabel speedIcon = new JLabel("\u26A1");
        speedIcon.setForeground(Color.LIGHT_GRAY);
        speedIcon.setFont(speedIcon.getFont().deriveFont(13f));
        speedIcon.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 2));

        autoplaySpeedSlider = new JSlider(1, 10, 5);
        autoplaySpeedSlider.setOpaque(false);
        autoplaySpeedSlider.setPreferredSize(new Dimension(80, 26));
        autoplaySpeedSlider.setMaximumSize(new Dimension(80, 26));

        autoplayPanel.add(autoplayStart);
        autoplayPanel.add(autoplayPause);
        autoplayPanel.add(speedIcon);
        autoplayPanel.add(autoplaySpeedSlider);
        autoplayPanel.setVisible(false);

        JPanel autoplayWrapper = new JPanel(new BorderLayout());
        autoplayWrapper.setOpaque(false);
        autoplayWrapper.setPreferredSize(new Dimension(260, 58));
        autoplayWrapper.setMaximumSize(new Dimension(260, 58));
        autoplayWrapper.setMinimumSize(new Dimension(260, 58));
        autoplayWrapper.add(autoplayPanel, BorderLayout.CENTER);

        JPanel centerGroup = new JPanel();
        centerGroup.setLayout(new BoxLayout(centerGroup, BoxLayout.X_AXIS));
        centerGroup.setOpaque(false);
        centerGroup.add(sliderGroup);
        centerGroup.add(Box.createHorizontalStrut(16));
        centerGroup.add(subtractBox);
        centerGroup.add(autoplayWrapper);

        topText.add(topLabel, BorderLayout.WEST);
        topText.add(centerGroup, BorderLayout.CENTER);
        topText.add(timerLabel, BorderLayout.EAST);

        mainPanel.add(topText, BorderLayout.NORTH);

        JPanel bottomButtons = new JPanel(new FlowLayout());
        newGame = MainMenu.styledButton("New Game");
        reset = MainMenu.styledButton("Reset");
        newGame.addActionListener(this);
        reset.addActionListener(this);

        autoplay = MainMenu.styledButton("Enable Autoplay Mode");
        autoplay.addActionListener(e -> {
            // TODO: Thing here to prevent score from submitting to leaderboards
            autoplayActive = true;
            addAutoplay();
        });

        bottomButtons.add(newGame);
        bottomButtons.add(reset);
        bottomButtons.add(autoplay);

        mainPanel.add(bottomButtons, BorderLayout.SOUTH);

        buttons = new MinesweeperButton[gridVolume];

        gamePanel = null;

        for (int i = 0; i < gridVolume; i++) {
            MinesweeperButton button = buttons[i] = new MinesweeperButton(this, buttonSize, i);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            // button.setBackground(MainMenu.PRIMARY_COLOR);
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

                        int clickedIdx = ((MinesweeperButton) e.getSource()).getIdx();
                        placeMines(clickedIdx);
                    }
                    onTileClick(e);
                    updateTitleText();
                    refreshNumbers();
                    checkWin();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    MinesweeperButton b = (MinesweeperButton) e.getSource();
                    highlightNeighbors(b);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    clearHighlights();
                }
            });
        }

        if (dims.length >= 3) {
            numSplices = gridVolume / (dims[0] * dims[1]);
            outerCols = dims[2];
        } else {
            numSplices = 1;
            outerCols = 1;
        }
        outerRows = numSplices / outerCols;

        gamePanel = new JPanel();
        gamePanel.setOpaque(false);

        if (dims.length >= 3) {
            gamePanel.setLayout(new GridLayout(outerRows, outerCols, buttonSize, buttonSize));
            splices = new JPanel[numSplices];
            for (int i = 0; i < numSplices; i++) {
                splices[i] = new JPanel(new GridLayout(dims[1], dims[0]));
                splices[i].setOpaque(false);
                Dimension spliceSize = new Dimension(dims[0] * buttonSize, dims[1] * buttonSize);
                splices[i].setPreferredSize(spliceSize);
                splices[i].setMinimumSize(spliceSize);
                splices[i].setMaximumSize(spliceSize);
                // splices[i].setBorder(BorderFactory.createTitledBorder("Splice " + (i+1)));

                JPanel spliceWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                spliceWrapper.add(splices[i]);
                gamePanel.add(spliceWrapper);
                for (int j = 0; j < dims[1]; j++) {
                    for (int k = 0; k < dims[0]; k++) {
                        int[] pos = new int[dims.length];
                        pos[0] = k;
                        pos[1] = j;

                        int temp = i;

                        for (int d = 2; d < dims.length; d++) {
                            pos[d] = temp % dims[d];
                            temp /= dims[d];
                        }
                        splices[i].add(buttons[posToIdx(pos)]);
                    }
                }
            }

            JPanel centeringPanel = new JPanel(new GridBagLayout());
            centeringPanel.setOpaque(false);
            centeringPanel.add(gamePanel);
            scrollPane = new JScrollPane(centeringPanel);

        } else {
            gamePanel.setLayout(new GridLayout(dims[1], dims[0]));
            for (int i = 0; i < gridVolume; i++) {
                gamePanel.add(buttons[i]);
            }
            scrollPane = new JScrollPane(gamePanel);
        }

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
            if (e.getSource() instanceof MinesweeperButton) {
                MinesweeperButton button = (MinesweeperButton) e.getSource();
                button.reveal();
            }
        }
    }

    private void addAutoplay() {
        autoplayPanel.setVisible(true);
        autoplay.setEnabled(false);
    }

    private void startAutoplay() {
        autoplayStart.setEnabled(false);
        autoplayPause.setEnabled(true);

        autoplayTimer = new Timer(speedToDelay(autoplaySpeedSlider.getValue()), e -> {
            ((Timer) e.getSource()).setDelay(speedToDelay(autoplaySpeedSlider.getValue()));
            autoplayStep();
        });

        autoplayTimer.start();
    }

    private int speedToDelay(int speed) {
        return 2000 - (speed - 1) * (1950 / 9);
    }

    private void stopAutoplay() {
        autoplayStart.setEnabled(true);
        autoplayPause.setEnabled(false);
        if (autoplayTimer != null) {
            autoplayTimer.stop();
            autoplayTimer = null;
        }
    }

    private void updateButtonSize(int newSize) {
        this.buttonSize = newSize;

        for (MinesweeperButton b : buttons) {
            b.resizeButton(newSize);
        }

        if (dims.length >= 3 && splices != null) {
            for (JPanel splice : splices) {
                Dimension spliceSize = new Dimension(dims[0] * newSize, dims[1] * newSize);
                splice.setPreferredSize(spliceSize);
                splice.setMinimumSize(spliceSize);
                splice.setMaximumSize(spliceSize);
            }
            if (gamePanel != null) {
                gamePanel.setLayout(new GridLayout(outerRows, outerCols, newSize, newSize));
            }
        }

        revalidate();

        repaint();
    }

    private void placeMines(int clickIdx) {
        int[] clickPos = idxToPos(clickIdx);

        DimensionIterator<MinesweeperButton> iter = new DimensionIterator<>(buttons, dims, (axis, length, context) -> {
            return new int[] { 0, length };
        }, null);

        ArrayList<int[]> allPositions = new ArrayList<>();
        while (iter.hasNext()) {
            MinesweeperButton b = iter.next();
            int[] pos = idxToPos(b.getIdx());
            for (int d = 0; d < pos.length; d++) {

                if (Math.abs(pos[d] - clickPos[d]) > 2) {
                    allPositions.add(pos);
                    break;
                } else if (Math.abs(pos[d] - clickPos[d]) == 2 && Math.random() > 0.5) {
                    allPositions.add(pos);
                    break;
                }

            }
        }

        java.util.Collections.shuffle(allPositions);
        int actualMineCount = Math.min(mineCount, allPositions.size());

        for (int i = 0; i < actualMineCount; i++) {
            int[] pos = allPositions.get(i);
            int idx = posToIdx(pos);
            if (buttons[idx].getMine())
                throw new IllegalStateException("Placing mine on existing mine");
            buttons[idx].setMine(true);
        }
    }

    /**
     * Updates the mine title text at the top of the window
     */
    private void updateTitleText() {
        // First created
        if (buttons == null) {
            topLabel.setText("\uD83D\uDCA3 " + mineCount);
            return;
        }

        int flagged = 0;
        for (MinesweeperButton b : buttons) {
            if (b != null && b.getFlagged()) {
                flagged++;
            }
        }
        topLabel.setText((mineCount - flagged) + "\uD83D\uDCA3 ");
        topLabel.setForeground(Color.WHITE);
    }

    /**
     * Resets the game to the starting state
     */
    @Override
    public void reset() {
        super.reset();

        removeAll();

        if (autoplayTimer != null) {
            autoplayTimer.stop();
            autoplayTimer = null;
        }
        if (timer != null) {
            timer.stop();
        }
        secondsElapsed = 0;
        build();
        updateTitleText();
        revalidate();
        repaint();

        firstClick = true;
    }

    private void checkWin() {
        DimensionIterator<MinesweeperButton> iter = new DimensionIterator<>(buttons, dims, (axis, length, context) -> {
            return new int[] { 0, length };
        }, null);

        while (iter.hasNext()) {
            MinesweeperButton b = iter.next();
            if (!b.getMine() && !b.getRevealed()) {
                return;
            }
        }

        onWin();
    }

    @Override
    protected void revealMines() {
        DimensionIterator<MinesweeperButton> iter = new DimensionIterator<>(buttons, dims, (axis, length, context) -> {
            return new int[] { 0, length };
        }, null);

        while (iter.hasNext()) {
            MinesweeperButton b = iter.next();
            if (b.getMine()) {
                b.reveal();
            }
        }
    }

    /**
     * Gets the list of buttons adjacent to one
     * 
     * @param position The index of the reference button
     * @return the list of buttons adjacent to one
     */
    public MinesweeperButton[] getAdjacentButtons(int idx) {
        int[] targetPos = idxToPos(idx);

        DimensionIterator<MinesweeperButton> iter = new DimensionIterator<>(buttons, dims, (axis, length, context) -> {
            int[] ctx = (int[]) context;
            int min = Math.max(0, ctx[axis] - 1);
            int max = Math.min(length - 1, ctx[axis] + 1) + 1; // +1 because max is exclusive
            return new int[] { min, max };
        }, targetPos);

        ArrayList<MinesweeperButton> adjs = new ArrayList<>();
        while (iter.hasNext()) {
            MinesweeperButton b = iter.next();
            int[] pos = idxToPos(b.getIdx());

            boolean equal = true;
            for (int i = 0; i < pos.length; i++) {
                if (pos[i] != targetPos[i]) {
                    equal = false;
                    break;
                }
            }

            if (!equal)
                adjs.add(b);
        }

        return adjs.toArray(new MinesweeperButton[0]);
    }

    private void highlightNeighbors(MinesweeperButton button) {
        MinesweeperButton[] adj = getAdjacentButtons(button.getIdx());

        for (MinesweeperButton b : adj) {
            if (!b.getRevealed()) {
                b.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 3));
            }
        }
    }

    private void clearHighlights() {
        for (MinesweeperButton b : buttons) {
            b.setBorder(UIManager.getBorder("Button.border"));
        }
    }

    @Override
    public int numTiles() {
        return gridVolume;
    }

    @Override
    public void onWin() {
        if (autoplayTimer != null) {
            autoplayTimer.stop();
            autoplayTimer = null;
        }
        super.onWin();
    }

    @Override
    public void onLoss() {
        if (autoplayTimer != null) {
            autoplayTimer.stop();
            autoplayTimer = null;
        }
        super.onLoss();
    }

    private void autoplayStep() {
        if (firstClick) {
            doAutoClick((int) (Math.random() * gridVolume));
            return;
        }

        // loop through all revealed non-mine buttons with neighbors
        for (MinesweeperButton button : buttons) {
            if (button.getRevealed() && !button.getMine() && button.getNumAdjacent() > 0) {
                MinesweeperButton[] adjacentButtons = getAdjacentButtons(button.getIdx());

                int flaggedCount = 0;
                ArrayList<MinesweeperButton> unrevealed = new ArrayList<>();

                for (MinesweeperButton adjButton : adjacentButtons) {
                    if (adjButton.getFlagged()) {
                        flaggedCount++;
                    } else if (!adjButton.getRevealed()) {
                        unrevealed.add(adjButton);
                    }
                }

                int remainingMines = button.getNumAdjacent() - flaggedCount;

                // all unrevealed neighbors are mines, flag rest
                if (remainingMines == unrevealed.size() && !unrevealed.isEmpty()) {
                    for (MinesweeperButton a : unrevealed) {
                        a.toggleFlagged();
                    }
                    updateTitleText();
                    return;
                }

                // all mines are flagged, reveal rest
                if (remainingMines == 0 && !unrevealed.isEmpty()) {
                    for (MinesweeperButton a : unrevealed) {
                        doAutoClick(a.getIdx());
                    }
                    return;
                }
            }
        }

        // 50/50, but it'll always choose right :)
        MinesweeperButton bestButton = null;
        int bestScore = Integer.MAX_VALUE;

        for (MinesweeperButton button : buttons) {
            if (!button.getRevealed() && !button.getMine() && !button.getFlagged()) {
                MinesweeperButton[] adj = getAdjacentButtons(button.getIdx());
                int minNeighborCount = Integer.MAX_VALUE;
                boolean hasRevealedNeighbor = false;

                for (MinesweeperButton a : adj) {
                    if (a.getRevealed() && !a.getMine()) {
                        hasRevealedNeighbor = true;
                        if (a.getNumAdjacent() < minNeighborCount)
                            minNeighborCount = a.getNumAdjacent();
                    }
                }

                if (hasRevealedNeighbor && minNeighborCount < bestScore) {
                    bestScore = minNeighborCount;
                    bestButton = button;
                }
            }
        }

        if (bestButton != null) {
            doAutoClick(bestButton.getIdx());
        } else {
            // Island created, click randomly again, but ensure its not a mine
            ArrayList<MinesweeperButton> candidates = new ArrayList<>();
            for (MinesweeperButton b : buttons) {
                if (!b.getRevealed() && !b.getFlagged() && !b.getMine()) {
                    candidates.add(b);
                }
            }
            if (!candidates.isEmpty()) {
                MinesweeperButton randomButton = candidates.get((int) (Math.random() * candidates.size()));
                doAutoClick(randomButton.getIdx());
            } else {
                stopAutoplay();
            }
        }
    }

    private void doAutoClick(int idx) {
        if (firstClick) {
            firstClick = false;
            timer = new Timer(1000, e2 -> {
                secondsElapsed++;
                timerLabel.setText(String.format("%02d:%02d", secondsElapsed / 60, secondsElapsed % 60));
            });
            timer.start();
            placeMines(idx);
        }
        buttons[idx].reveal();
        updateTitleText();
        refreshNumbers();
        checkWin();
    }

    private int[] idxToPos(int idx) {
        int[] pos = new int[dims.length];
        int tempIdx = idx;

        for (int i = 0; i < dims.length; i++) {
            pos[i] = tempIdx % dims[i];
            tempIdx /= dims[i];
        }
        return pos;
    }

    private int posToIdx(int[] pos) {
        int idx = 0;
        int stride = 1;

        for (int i = 0; i < dims.length; i++) {
            idx += pos[i] * stride;
            stride *= dims[i];
        }
        return idx;
    }

    private void refreshNumbers() {
        for (MinesweeperButton b : buttons) {
            if (b != null) {
                b.updateDisplayedNumber(subtractFlagged);
            }
        }
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
