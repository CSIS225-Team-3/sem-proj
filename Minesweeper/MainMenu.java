package Minesweeper;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Main Game to show the options for type of modes.
 * 
 * @author Ahyaan Malik (so far)
 * @version 4/14/2026
 */
public class MainMenu extends JPanel implements ActionListener, ChangeListener, Runnable {

    /** The name of the menu card */
    private static final String MENU_CARD = "Menu";

    private static final String TWO_DIMENSIONS = "2D Minesweeper";

    private static final String THREE_DIMENSIONS = "3D Minesweeper";

    /** The name of the gamble mode card */
    private static final String FOUR_DIMENSIONS = "4D Minesweeper";

    private static final String FIVE_DIMENSIONS = "5D+ Minesweeper";
    
    private static final String HYPERBOLIC = "Hyperbolic Minesweeper";

    /** The CardLayout for managing panels */
    private CardLayout cardLayout;

    /** The JPanel that holds the cards */
    private JPanel cards;

    /** The JFrame for the main window */
    private JFrame frame;

    /** The main text label for the menu */
    private JLabel mainText;

    /** The spinners for configuring the number of rows */
    private JSpinner rowsSpinner;

    /** The spinner for configuring the number of columns */
    private JSpinner colsSpinner;

    /** The spinner for configuring the number of mines */
    private JSpinner minesSpinner;

    private SpinnerNumberModel minesModel;

    private int buttonSize = 40;

    private JCheckBox randomMines;

    /** The label for displaying error messages */
    private JLabel errorLabel;

    private JButton twoDimension;

    private JButton threeDimension;

    private JButton fourDimension;

    private JButton fiveDimension;

    private JButton hyperbolic;

    private JRadioButton easyBtn;
    private JRadioButton mediumBtn;
    private JRadioButton hardBtn;
    private JRadioButton extremeBtn;

    private JButton startButton;
    private String selectedMode;
    private JLabel[] difficultyInfoLabels;

    private JSpinner splices3dSpinner;
    private JLabel splices3dLabel;

    private JSpinner splices4dSpinner;
    private JLabel splices4dLabel;

    // 5D+
    private JSpinner splices5dSpinner;
    private JLabel splices5dLabel;

    private JSpinner dimensionsSpinner;
    private JLabel dimensionsLabel;

    private ButtonGroup difficultyGroup;

    private boolean difficultyAdjusted;

    private JPanel configPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton registerButton;
    private JButton loginButton;
    private JButton logoutButton;
    private JButton deleteButton;

    private JLabel loginStatus;

    private boolean deleteConfirmed;

    private AccountManager accountManager;
    private Account loggedInAccount;

    private JCheckBox showPassword;

    JOptionPane deleteConfirmation;

    /** The primary color for the UI */
    public static Color PRIMARY_COLOR = Color.WHITE;

    /** The secondary color for the UI */
    public static Color SECONDARY_COLOR = new Color(30, 100, 200, 175);

    /** The tertiary color for the UI */
    public static Color TERTIARY_COLOR = new Color(65, 150, 255, 220);

    public static Color TRANSPARENT_COLOR = new Color(0, 0, 0, 255);

    private final static int MAX_ROWS = 100;
    private final static int MAX_COLS = 100;
    private final static int MAX_SPLICES3D = 100;
    private final static int MAX_SPLICES4D = 100;
    private final static int MAX_SPLICES5D = 100;

    private int maxMines = MAX_ROWS * MAX_COLS;

    private int maxRandomMines;

    private final static int MAX_DIMENSIONS = 100;

    private int dimensionsSelected;

    /**
     * Constructor for the MainMenu class.
     * 
     * @param cardLayout the CardLayout used to manage the different mode panels
     * @param cards      the JPanel that holds the different mode panels
     */
    public MainMenu(CardLayout cardLayout, JPanel cards) {
        this.cardLayout = cardLayout;
        this.cards = cards;

        accountManager = new AccountManager();

        deleteConfirmed = false;

        setLayout(new BorderLayout());
    }

    /**
     * The run method to set up the GUI.
     */
    @Override
    public void run() {
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setPreferredSize(new Dimension(800, 800));
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true);

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("MinesweeperMine.png"));
        frame.setIconImage(icon);

        BufferedImage background = null;
        TexturePaint texturepaint = null;
        try {
            background = ImageIO.read(new File("Minesweeper/Background.jpg"));
            texturepaint = new TexturePaint(background,
                    new Rectangle(0, 0, background.getWidth(), background.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BufferedImage bg = background;
        final TexturePaint tp = texturepaint;
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                }
            }
            /*
             * protected void paintComponent(Graphics g) {
             * super.paintComponent(g);
             * if (tp != null) {
             * Graphics2D g2d = (Graphics2D) g;
             * g2d.setPaint(tp);
             * g2d.fillRect(0, 0, getWidth(), getHeight());
             * }
             * }
             */
        };

        JPanel topPanel = new JPanel(new BorderLayout());

        usernameField = new JTextField();

        usernameField.setForeground(Color.WHITE);
        usernameField.setBackground(SECONDARY_COLOR);
        usernameField.setCaretColor(Color.WHITE);

        passwordField = new JPasswordField();

        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(SECONDARY_COLOR);
        passwordField.setCaretColor(Color.WHITE);

        registerButton = semiTransparentButton("Register");
        loginButton = semiTransparentButton("Login");
        logoutButton = semiTransparentButton("Log Out");
        deleteButton = semiTransparentButton("Delete Account");

        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false);

        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);

        logoutButton.setOpaque(false);
        logoutButton.setContentAreaFilled(false);

        deleteButton.setOpaque(false);
        deleteButton.setContentAreaFilled(false);

        registerButton.addActionListener(this);
        loginButton.addActionListener(this);
        logoutButton.addActionListener(this);
        deleteButton.addActionListener(this);

        logoutButton.setEnabled(false);
        deleteButton.setEnabled(false);

        JPanel loginMainPanel = semiTransparentPanel(new BorderLayout(5, 5));
        loginMainPanel.setOpaque(false);
        loginMainPanel.setBackground(SECONDARY_COLOR);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.setOpaque(false);

        JLabel usernameLabel = new JLabel("Username", SwingConstants.RIGHT);
        usernameLabel.setForeground(Color.WHITE);

        fieldsPanel.add(usernameLabel);
        fieldsPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password", SwingConstants.RIGHT);
        passwordLabel.setForeground(Color.WHITE);

        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);

        showPassword = new JCheckBox();
        showPassword.setOpaque(false);
        showPassword.setContentAreaFilled(false);
        showPassword.addActionListener(this);

        JLabel showPasswordLabel = new JLabel("Show Password", SwingConstants.RIGHT);
        showPasswordLabel.setForeground(Color.WHITE);
        fieldsPanel.add(showPasswordLabel);
        fieldsPanel.add(showPassword);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel topButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        topButtons.setOpaque(false);
        topButtons.add(registerButton);
        topButtons.add(loginButton);

        JPanel bottomButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        bottomButtons.setOpaque(false);
        bottomButtons.add(logoutButton);
        bottomButtons.add(deleteButton);

        buttonsPanel.add(topButtons);
        buttonsPanel.add(bottomButtons);

        loginStatus = new JLabel("Not logged in.", SwingConstants.CENTER);

        loginMainPanel.add(fieldsPanel, BorderLayout.NORTH);
        loginMainPanel.add(buttonsPanel, BorderLayout.CENTER);
        loginMainPanel.add(loginStatus, BorderLayout.SOUTH);

        loginMainPanel.setBorder(BorderFactory.createTitledBorder("Account"));

        topPanel.add(loginMainPanel, BorderLayout.EAST);

        mainText = new JLabel("Welcome to Minesweeper!", SwingConstants.CENTER);
        mainText.setForeground(Color.WHITE);
        mainText.setFont(mainText.getFont().deriveFont(48.0f));
        topPanel.add(mainText, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel modePanel = semiTransparentPanel(new GridLayout(5, 1, 0, 5));
        modePanel.setBorder(BorderFactory.createTitledBorder("Select Mode"));
        modePanel.setBackground(SECONDARY_COLOR);

        twoDimension = semiTransparentButton("2D Minesweeper");
        threeDimension = semiTransparentButton("3D Minesweeper");
        fourDimension = semiTransparentButton("4D Minesweeper");
        fiveDimension = semiTransparentButton("5D+ Minesweeper");
        hyperbolic = semiTransparentButton("Hyperbolic Minesweeper");

        JButton[] dimensions = { twoDimension, threeDimension, fourDimension, fiveDimension, hyperbolic };
        for (JButton b : dimensions) {
            // b.setBackground(TERTIARY_COLOR);
            // b.setForeground(Color.WHITE);
            // b.setOpaque(true);
            // b.setContentAreaFilled(true);
            b.addActionListener(this);
            modePanel.add(b);
        }

        centerPanel.add(modePanel, BorderLayout.NORTH);

        configPanel = new JPanel(new BorderLayout(0, 10));
        configPanel.setBackground(PRIMARY_COLOR);
        configPanel.setVisible(false);

        JPanel settingsPanel = semiTransparentPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Game Settings"));
        settingsPanel.setBackground(SECONDARY_COLOR);

        settingsPanel.add(new JLabel("Rows: "));
        rowsSpinner = new JSpinner(new SpinnerNumberModel(6, 2, MAX_ROWS, 1));
        rowsSpinner.setPreferredSize(new Dimension(60, 30));
        rowsSpinner.addChangeListener(this);
        settingsPanel.add(rowsSpinner);

        settingsPanel.add(new JLabel("Columns: "));
        colsSpinner = new JSpinner(new SpinnerNumberModel(9, 2, MAX_COLS, 1));
        colsSpinner.setPreferredSize(new Dimension(60, 30));
        colsSpinner.addChangeListener(this);
        settingsPanel.add(colsSpinner);

        splices3dLabel = new JLabel("3D Splices: ");
        settingsPanel.add(splices3dLabel);
        splices3dSpinner = new JSpinner(new SpinnerNumberModel(5, 2, MAX_SPLICES3D, 1));
        splices3dSpinner.setPreferredSize(new Dimension(60, 30));
        splices3dSpinner.addChangeListener(this);
        settingsPanel.add(splices3dSpinner);

        splices3dLabel.setVisible(false);
        splices3dSpinner.setVisible(false);

        splices4dLabel = new JLabel("4D Splices: ");
        settingsPanel.add(splices4dLabel);
        splices4dSpinner = new JSpinner(new SpinnerNumberModel(3, 2, MAX_SPLICES4D, 1));
        splices4dSpinner.setPreferredSize(new Dimension(60, 30));
        splices4dSpinner.addChangeListener(this);
        settingsPanel.add(splices4dSpinner);

        splices4dLabel.setVisible(false);
        splices4dSpinner.setVisible(false);

        splices5dLabel = new JLabel("5D Splices: ");
        settingsPanel.add(splices5dLabel);
        splices5dSpinner = new JSpinner(new SpinnerNumberModel(3, 2, MAX_SPLICES5D, 1));
        splices5dSpinner.setPreferredSize(new Dimension(60, 30));
        splices5dSpinner.addChangeListener(this);
        settingsPanel.add(splices5dSpinner);

        splices5dLabel.setVisible(false);
        splices5dSpinner.setVisible(false);

        dimensionsLabel = new JLabel("Dimension Count: ");
        settingsPanel.add(dimensionsLabel);
        dimensionsSpinner = new JSpinner(new SpinnerNumberModel(5, 5, MAX_DIMENSIONS, 1));
        dimensionsSpinner.setPreferredSize(new Dimension(60, 30));
        dimensionsSpinner.addChangeListener(this);
        settingsPanel.add(dimensionsSpinner);

        dimensionsLabel.setVisible(false);
        dimensionsSpinner.setVisible(false);

        JPanel mineConfigPanel = semiTransparentPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        mineConfigPanel.setBackground(SECONDARY_COLOR);
        mineConfigPanel.add(new JLabel("Mine Amount: "));

        minesModel = new SpinnerNumberModel(10, 0, maxMines, 1);
        minesSpinner = new JSpinner(minesModel);
        minesSpinner.setPreferredSize(new Dimension(60, 30));
        minesSpinner.addChangeListener(this);

        mineConfigPanel.add(minesSpinner);
        mineConfigPanel.add(new JLabel("Random # of Mines: "));
        randomMines = new JCheckBox();
        randomMines.setBackground(SECONDARY_COLOR);
        randomMines.addActionListener(this);
        mineConfigPanel.add(randomMines);
        settingsPanel.add(mineConfigPanel);

        configPanel.add(settingsPanel, BorderLayout.NORTH);

        JPanel difficultyPanel = semiTransparentPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        difficultyPanel.setBorder(BorderFactory.createTitledBorder("Preset Difficulties"));
        difficultyPanel.setBackground(SECONDARY_COLOR);

        easyBtn = new JRadioButton("Easy");
        mediumBtn = new JRadioButton("Medium");
        hardBtn = new JRadioButton("Hard");
        extremeBtn = new JRadioButton("Extreme");

        difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyBtn);
        difficultyGroup.add(mediumBtn);
        difficultyGroup.add(hardBtn);
        difficultyGroup.add(extremeBtn);

        JLabel easyInfo = new JLabel("", SwingConstants.CENTER);
        JLabel mediumInfo = new JLabel("", SwingConstants.CENTER);
        JLabel hardInfo = new JLabel("", SwingConstants.CENTER);
        JLabel extremeInfo = new JLabel("", SwingConstants.CENTER);

        JRadioButton[] btns = { easyBtn, mediumBtn, hardBtn, extremeBtn };
        JLabel[] infoLabels = { easyInfo, mediumInfo, hardInfo, extremeInfo };

        for (int i = 0; i < btns.length; i++) {
            btns[i].setBackground(SECONDARY_COLOR);
            btns[i].addActionListener(this);
            btns[i].setOpaque(false);
            btns[i].setContentAreaFilled(false);

            JPanel each = semiTransparentPanel(new BorderLayout());
            each.setBackground(SECONDARY_COLOR);

            infoLabels[i].setFont(infoLabels[i].getFont().deriveFont(10f));
            infoLabels[i].setOpaque(true);
            infoLabels[i].setBackground(SECONDARY_COLOR);

            each.add(btns[i], BorderLayout.CENTER);
            each.add(infoLabels[i], BorderLayout.SOUTH);

            difficultyPanel.add(each);

        }

        configPanel.add(difficultyPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(PRIMARY_COLOR);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setBackground(PRIMARY_COLOR);
        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorPanel.add(errorLabel);

        startButton = semiTransparentButton("Start Game");
        startButton.setBackground(TERTIARY_COLOR);
        startButton.addActionListener(this);

        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startPanel.setBackground(PRIMARY_COLOR);
        startPanel.add(startButton);

        bottomPanel.add(errorPanel, BorderLayout.NORTH);
        bottomPanel.add(startPanel, BorderLayout.SOUTH);

        configPanel.add(bottomPanel, BorderLayout.SOUTH);

        centerPanel.add(configPanel, BorderLayout.CENTER);

        difficultyInfoLabels = infoLabels;

        add(centerPanel, BorderLayout.CENTER);
        centerPanel.setBackground(PRIMARY_COLOR);
        modePanel.setBackground(SECONDARY_COLOR);
        setBackground(PRIMARY_COLOR);

        cards.add(this, MENU_CARD);
        cardLayout.show(cards, MENU_CARD);
        frame.add(cards);
        // frame.pack();

        /*
         * RAINBOW
         * Thread rainbowThread = new Thread(() -> {
         * while (true) {
         * rainbowHue = (rainbowHue + 0.002f) % 1f;
         * 
         * PRIMARY_COLOR = Color.getHSBColor(rainbowHue, 0.3f, 1.0f);
         * SECONDARY_COLOR = Color.getHSBColor((rainbowHue + 0.09f) % 1f, 0.5f, 0.85f);
         * TERTIARY_COLOR = Color.getHSBColor((rainbowHue + 0.05f) % 1f, 0.25f, 1.0f);
         * 
         * SwingUtilities.invokeLater(() -> {
         * frame.setBackground(PRIMARY_COLOR);
         * centerPanel.setBackground(PRIMARY_COLOR);
         * configPanel.setBackground(PRIMARY_COLOR);
         * bottomPanel.setBackground(PRIMARY_COLOR);
         * errorPanel.setBackground(PRIMARY_COLOR);
         * startPanel.setBackground(PRIMARY_COLOR);
         * topPanel.setBackground(PRIMARY_COLOR);
         * 
         * loginMainPanel.setBackground(SECONDARY_COLOR);
         * buttonsPanel.setBackground(SECONDARY_COLOR);
         * fieldsPanel.setBackground(SECONDARY_COLOR);
         * topButtons.setBackground(SECONDARY_COLOR);
         * bottomButtons.setBackground(SECONDARY_COLOR);
         * showPassword.setBackground(SECONDARY_COLOR);
         * 
         * modePanel.setBackground(SECONDARY_COLOR);
         * settingsPanel.setBackground(SECONDARY_COLOR);
         * mineConfigPanel.setBackground(SECONDARY_COLOR);
         * difficultyPanel.setBackground(SECONDARY_COLOR);
         * 
         * easyBtn.setBackground(SECONDARY_COLOR);
         * mediumBtn.setBackground(SECONDARY_COLOR);
         * hardBtn.setBackground(SECONDARY_COLOR);
         * extremeBtn.setBackground(SECONDARY_COLOR);
         * 
         * randomMines.setBackground(SECONDARY_COLOR);
         * 
         * easyInfo.setBackground(SECONDARY_COLOR);
         * mediumInfo.setBackground(SECONDARY_COLOR);
         * hardInfo.setBackground(SECONDARY_COLOR);
         * extremeInfo.setBackground(SECONDARY_COLOR);
         * 
         * registerButton.setBackground(TERTIARY_COLOR);
         * loginButton.setBackground(TERTIARY_COLOR);
         * logoutButton.setBackground(TERTIARY_COLOR);
         * deleteButton.setBackground(TERTIARY_COLOR);
         * 
         * twoDimension.setBackground(TERTIARY_COLOR);
         * threeDimension.setBackground(TERTIARY_COLOR);
         * fourDimension.setBackground(TERTIARY_COLOR);
         * fiveDimension.setBackground(TERTIARY_COLOR);
         * 
         * startButton.setBackground(TERTIARY_COLOR);
         * 
         * repaint();
         * });
         * try {
         * Thread.sleep(50);
         * } catch (InterruptedException ex) {
         * Thread.currentThread().interrupt();
         * break;
         * }
         * }
         * });
         * 
         * rainbowThread.setDaemon(true);
         * rainbowThread.start();
         */
        topPanel.setOpaque(false);
        loginMainPanel.setOpaque(false);
        fieldsPanel.setOpaque(false);
        buttonsPanel.setOpaque(false);
        topButtons.setOpaque(false);
        bottomButtons.setOpaque(false);
        centerPanel.setOpaque(false);
        configPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        errorPanel.setOpaque(false);
        startPanel.setOpaque(false);
        setOpaque(false);

        startButton.setContentAreaFilled(false);
        frame.setVisible(true);

        // Dev thing, leave it for now
        // int mines = 1;
        // cards.add(new MinesweeperHyperbolic(mines, cardLayout, cards),
        // HYPERBOLIC_MS);
        // cardLayout.show(cards, HYPERBOLIC_MS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // Password visibility
        if (src == showPassword) {
            togglePasswordVisibility();
            return;
        }

        if (src == registerButton) {
            register();
            return;
        }

        if (src == loginButton) {
            login();
            return;
        }

        if (src == logoutButton) {
            logout();
            return;
        }

        if (src == deleteButton) {
            deleteAccount();
            return;
        }

        // Mode buttons
        if (src == twoDimension || src == threeDimension || src == fourDimension || src == fiveDimension) {

            configPanel.setVisible(true);

            // USER SELECTING DIMENSION

            if (src == twoDimension) {

                selectedMode = TWO_DIMENSIONS;
                dimensionsSelected = 2;

                twoDimension.setEnabled(false);
                threeDimension.setEnabled(true);
                fourDimension.setEnabled(true);
                fiveDimension.setEnabled(true);

                splices3dLabel.setVisible(false);
                splices3dSpinner.setVisible(false);

                splices4dLabel.setVisible(false);
                splices4dSpinner.setVisible(false);

                splices5dLabel.setVisible(false);
                splices5dSpinner.setVisible(false);

                dimensionsLabel.setVisible(false);
                dimensionsSpinner.setVisible(false);

                difficultyInfoLabels[0].setText("9x9, 10 mines");
                difficultyInfoLabels[1].setText("16x16, 40 mines");
                difficultyInfoLabels[2].setText("16x30, 99 mines");
                difficultyInfoLabels[3].setText("30x50, 400 mines");

                minesSpinner.setValue(10);

            } else if (src == threeDimension) {

                selectedMode = THREE_DIMENSIONS;
                dimensionsSelected = 3;

                twoDimension.setEnabled(true);
                threeDimension.setEnabled(false);
                fourDimension.setEnabled(true);
                fiveDimension.setEnabled(true);

                splices3dLabel.setVisible(true);
                splices3dSpinner.setVisible(true);

                splices4dLabel.setVisible(false);
                splices4dSpinner.setVisible(false);

                splices5dLabel.setVisible(false);
                splices5dSpinner.setVisible(false);

                dimensionsLabel.setVisible(false);
                dimensionsSpinner.setVisible(false);

                difficultyInfoLabels[0].setText("5x5x3, 15 mines");
                difficultyInfoLabels[1].setText("7x7x4, 60 mines");
                difficultyInfoLabels[2].setText("11x11x6, 150 mines");
                difficultyInfoLabels[3].setText("18x18x8, 400 mines");

                minesSpinner.setValue(30);

            } else if (src == fourDimension) {

                selectedMode = FOUR_DIMENSIONS;
                dimensionsSelected = 4;

                twoDimension.setEnabled(true);
                threeDimension.setEnabled(true);
                fourDimension.setEnabled(false);
                fiveDimension.setEnabled(true);

                splices3dLabel.setVisible(true);
                splices3dSpinner.setVisible(true);

                splices4dLabel.setVisible(true);
                splices4dSpinner.setVisible(true);

                splices5dLabel.setVisible(false);
                splices5dSpinner.setVisible(false);

                dimensionsLabel.setVisible(false);
                dimensionsSpinner.setVisible(false);

                difficultyInfoLabels[0].setText("4x4x3x3, 20 mines");
                difficultyInfoLabels[1].setText("5x5x4x4, 80 mines");
                difficultyInfoLabels[2].setText("6x6x5x5, 225 mines");
                difficultyInfoLabels[3].setText("9x9x7x7, 850 mines");

                minesSpinner.setValue(120);

            } else if (src == fiveDimension) {
                selectedMode = FIVE_DIMENSIONS;
                dimensionsSelected = 5;

                twoDimension.setEnabled(true);
                threeDimension.setEnabled(true);
                fourDimension.setEnabled(true);
                fiveDimension.setEnabled(false);

                splices3dLabel.setVisible(true);
                splices3dSpinner.setVisible(true);

                splices4dLabel.setVisible(true);
                splices4dSpinner.setVisible(true);

                splices5dLabel.setVisible(true);
                splices5dSpinner.setVisible(true);

                dimensionsLabel.setVisible(true);
                dimensionsSpinner.setVisible(true);

                difficultyInfoLabels[0].setText("5D, 4x4x3x3x2, 20 mines");
                difficultyInfoLabels[1].setText("5D, 5x5x4x4x3, 225 mines");
                difficultyInfoLabels[2].setText("6D, 5x5x5x4x3x3, 1000 mines");
                difficultyInfoLabels[3].setText("7D, 5x5x5x5x3x3x3, 4125 mines");

                minesSpinner.setValue(500);
            } else {
                selectedMode = "";
            }

            updateMaxMines();
            minesModel.setMaximum(maxMines);

            mainText.setText("Mode: " + selectedMode);
            revalidate();
            repaint();
            return;
        }

        // USER SELECTING DIFFICULTY
        if (src == easyBtn || src == mediumBtn || src == hardBtn || src == extremeBtn) {

            randomMines.setSelected(false);
            minesSpinner.setEnabled(true);
            difficultyAdjusted = true;

            if (loggedInAccount == null) {
                errorLabel.setText("Progress will not save. Login to save progress.");
            } else {
                errorLabel.setText(" ");
            }

            switch (selectedMode) {
                case TWO_DIMENSIONS:
                    if (src == easyBtn) {
                        rowsSpinner.setValue(9);
                        colsSpinner.setValue(9);
                        minesSpinner.setValue(10);
                    } else if (src == mediumBtn) {
                        rowsSpinner.setValue(16);
                        colsSpinner.setValue(16);
                        minesSpinner.setValue(40);
                    } else if (src == hardBtn) {
                        rowsSpinner.setValue(16);
                        colsSpinner.setValue(30);
                        minesSpinner.setValue(99);
                    } else if (src == extremeBtn) {
                        rowsSpinner.setValue(30);
                        colsSpinner.setValue(50);
                        minesSpinner.setValue(400);
                    }
                    break;
                case THREE_DIMENSIONS:
                    if (src == easyBtn) {
                        rowsSpinner.setValue(5);
                        colsSpinner.setValue(5);
                        splices3dSpinner.setValue(3);
                        minesSpinner.setValue(15);
                    } else if (src == mediumBtn) {
                        rowsSpinner.setValue(7);
                        colsSpinner.setValue(7);
                        splices3dSpinner.setValue(4);
                        minesSpinner.setValue(60);
                    } else if (src == hardBtn) {
                        rowsSpinner.setValue(11);
                        colsSpinner.setValue(11);
                        splices3dSpinner.setValue(6);
                        minesSpinner.setValue(150);
                    } else if (src == extremeBtn) {
                        rowsSpinner.setValue(18);
                        colsSpinner.setValue(18);
                        splices3dSpinner.setValue(8);
                        minesSpinner.setValue(400);
                    }
                    break;
                case FOUR_DIMENSIONS:
                    if (src == easyBtn) {
                        rowsSpinner.setValue(4);
                        colsSpinner.setValue(4);
                        splices3dSpinner.setValue(3);
                        splices4dSpinner.setValue(3);
                        minesSpinner.setValue(20);
                    } else if (src == mediumBtn) {
                        rowsSpinner.setValue(5);
                        colsSpinner.setValue(5);
                        splices3dSpinner.setValue(4);
                        splices4dSpinner.setValue(4);
                        minesSpinner.setValue(80);
                    } else if (src == hardBtn) {
                        rowsSpinner.setValue(6);
                        colsSpinner.setValue(6);
                        splices3dSpinner.setValue(5);
                        splices4dSpinner.setValue(5);
                        minesSpinner.setValue(225);
                    } else if (src == extremeBtn) {
                        rowsSpinner.setValue(9);
                        colsSpinner.setValue(9);
                        splices3dSpinner.setValue(7);
                        splices4dSpinner.setValue(7);
                        minesSpinner.setValue(850);
                    }
                    break;
                case FIVE_DIMENSIONS:
                    if (src == easyBtn) {
                        rowsSpinner.setValue(4);
                        colsSpinner.setValue(4);
                        splices3dSpinner.setValue(3);
                        splices4dSpinner.setValue(2);
                        splices5dSpinner.setValue(2);
                        dimensionsSelected = 5;
                        dimensionsSpinner.setValue(dimensionsSelected);
                        minesSpinner.setValue(20);
                    } else if (src == mediumBtn) {
                        rowsSpinner.setValue(5);
                        colsSpinner.setValue(5);
                        splices3dSpinner.setValue(4);
                        splices4dSpinner.setValue(4);
                        splices5dSpinner.setValue(3);
                        dimensionsSelected = 5;
                        dimensionsSpinner.setValue(dimensionsSelected);
                        minesSpinner.setValue(225);
                    } else if (src == hardBtn) {
                        rowsSpinner.setValue(5);
                        colsSpinner.setValue(5);
                        splices3dSpinner.setValue(5);
                        splices4dSpinner.setValue(4);
                        splices5dSpinner.setValue(3);
                        dimensionsSelected = 6;
                        dimensionsSpinner.setValue(dimensionsSelected);
                        minesSpinner.setValue(1000);
                    } else if (src == extremeBtn) {
                        rowsSpinner.setValue(5);
                        colsSpinner.setValue(5);
                        splices3dSpinner.setValue(5);
                        splices4dSpinner.setValue(5);
                        splices5dSpinner.setValue(3);
                        dimensionsSelected = 7;
                        dimensionsSpinner.setValue(dimensionsSelected);
                        minesSpinner.setValue(4125);
                    }
                    break;
                default:
                    return;
            }
            updateMaxMines();
            difficultyAdjusted = false;
        }

        if (src == randomMines) {
            minesSpinner.setEnabled(!randomMines.isSelected());
            difficultyGroup.clearSelection();
        }

        if (src == startButton) {
            if (selectedMode == null) {
                return;
            }

            int rows = (int) rowsSpinner.getValue();
            int cols = (int) colsSpinner.getValue();
            int mines;

            errorLabel.setText(" ");

            if (dimensionsSelected == 2) {
                int[] dims = { cols, rows };

                if (randomMines.isSelected()) {
                    mines = new Random().nextInt(maxRandomMines) / 3 + 1;
                } else {
                    mines = (int) minesSpinner.getValue();
                    if (mines > maxRandomMines) {
                        errorLabel.setText("Too many mines!");
                        return;
                    }
                }

                cards.add(new MinesweeperEuclidean(dims, mines, buttonSize, cardLayout, cards), TWO_DIMENSIONS);
                cardLayout.show(cards, TWO_DIMENSIONS);
            } else if (dimensionsSelected == 3) {

                int splices3d = (int) splices3dSpinner.getValue();

                int[] dims = { cols, rows, splices3d };

                if (randomMines.isSelected()) {
                    mines = new Random().nextInt(maxRandomMines) / 3 + 1;
                } else {
                    mines = (int) minesSpinner.getValue();
                    if (mines > maxRandomMines) {
                        errorLabel.setText("Too many mines!");
                        return;
                    }
                }
                cards.add(new MinesweeperEuclidean(dims, mines, buttonSize, cardLayout, cards), THREE_DIMENSIONS);
                cardLayout.show(cards, THREE_DIMENSIONS);
            } else if (dimensionsSelected == 4) {

                int splices3d = (int) splices3dSpinner.getValue();
                int splices4d = (int) splices4dSpinner.getValue();

                int[] dims = { cols, rows, splices3d, splices4d };

                if (randomMines.isSelected()) {
                    mines = new Random().nextInt(maxRandomMines) / 3 + 1;
                } else {
                    mines = (int) minesSpinner.getValue();
                    if (mines > maxRandomMines) {
                        errorLabel.setText("Too many mines!");
                        return;
                    }
                }
                cards.add(new MinesweeperEuclidean(dims, mines, buttonSize, cardLayout, cards), FOUR_DIMENSIONS);
                cardLayout.show(cards, FOUR_DIMENSIONS);
            } else if (dimensionsSelected >= 5) {

                int splices3d = (int) splices3dSpinner.getValue();
                int splices4d = (int) splices4dSpinner.getValue();
                int splices5d = (int) splices5dSpinner.getValue();

                int[] dims = new int[dimensionsSelected];
                dims[0] = cols;
                dims[1] = rows;
                dims[2] = splices3d;
                dims[3] = splices4d;
                for (int i = 4; i < dimensionsSelected; i++) {
                    dims[i] = splices5d;
                }

                if (randomMines.isSelected()) {
                    mines = new Random().nextInt(maxRandomMines) / 3 + 1;
                } else {
                    mines = (int) minesSpinner.getValue();
                    if (mines > maxRandomMines) {
                        errorLabel.setText("Too many mines!");
                        return;
                    }
                }
                cards.add(new MinesweeperEuclidean(dims, mines, buttonSize, cardLayout, cards), FIVE_DIMENSIONS);
                cardLayout.show(cards, FIVE_DIMENSIONS);
            }

        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!difficultyAdjusted) {
            difficultyGroup.clearSelection();
        }
        int gridVolume = -1;
        if (dimensionsSelected == 2) {
            gridVolume = (int) colsSpinner.getValue() * (int) rowsSpinner.getValue();
        } else if (dimensionsSelected == 3) {
            gridVolume = (int) colsSpinner.getValue() * (int) rowsSpinner.getValue()
                    * (int) splices3dSpinner.getValue();
        } else if (dimensionsSelected == 4) {
            gridVolume = (int) colsSpinner.getValue() * (int) rowsSpinner.getValue()
                    * (int) splices3dSpinner.getValue() * (int) splices4dSpinner.getValue();
        } else if (dimensionsSelected >= 5) {
            gridVolume = (int) colsSpinner.getValue() * (int) rowsSpinner.getValue()
                    * (int) splices3dSpinner.getValue() * (int) splices4dSpinner.getValue()
                    * (dimensionsSelected * (int) (splices5dSpinner.getValue()));
        }

        // 20k cell before it gives warning
        if (gridVolume > 20000) {
            errorLabel.setText("Current grid size may cause game to crash. Proceed with caution.");
        } else {
            errorLabel.setText(" ");
        }
    }

    private void updateMaxMines() {
        if (dimensionsSelected == 2) {
            maxMines = MAX_ROWS * MAX_COLS - 1;
            maxRandomMines = (int) rowsSpinner.getValue() * (int) colsSpinner.getValue() - 1;
        } else if (dimensionsSelected == 3) {
            maxMines = MAX_ROWS * MAX_COLS - 1;
            maxRandomMines = (int) rowsSpinner.getValue() * (int) colsSpinner.getValue()
                    * (int) splices3dSpinner.getValue() - 1;
        } else if (dimensionsSelected == 4) {
            maxMines = MAX_ROWS * MAX_COLS * MAX_SPLICES3D * MAX_SPLICES4D - 1;
            maxRandomMines = (int) rowsSpinner.getValue() * (int) colsSpinner.getValue()
                    * (int) splices3dSpinner.getValue() * (int) splices4dSpinner.getValue() - 1;
        } else if (dimensionsSelected >= 5) {
            int BeyondCellCount = (MAX_SPLICES5D * (dimensionsSelected - 4));
            maxRandomMines = (int) rowsSpinner.getValue() * (int) colsSpinner.getValue()
                    * (int) splices3dSpinner.getValue() * (int) splices4dSpinner.getValue()
                    * ((int) splices5dSpinner.getValue() * (dimensionsSelected)) - 1;
            maxMines = MAX_ROWS * MAX_COLS * MAX_SPLICES3D * MAX_SPLICES4D * BeyondCellCount - 1;
        }
    }

    /**
     * Toggles password visability based on the state of the showPassword checkbox.
     */
    private void togglePasswordVisibility() {
        if (showPassword.isSelected()) {
            // Show password
            passwordField.setEchoChar((char) 0);
        } else {
            // Hide password
            passwordField.setEchoChar('*');
        }
    }

    private void register() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            loginStatus.setForeground(Color.RED);
            loginStatus.setText("Username/password cannot be empty.");
            return;
        }

        String err = accountManager.register(username, password);
        if (err == null) {
            loginStatus.setForeground(Color.GREEN.darker());
            loginStatus.setText("Registered! Please log in now.");
        } else {
            loginStatus.setForeground(Color.RED);
            loginStatus.setText(err);
        }

    }

    private void login() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            loginStatus.setForeground(Color.RED);
            loginStatus.setText("Username/password cannot be empty.");
            return;
        }

        Account account = accountManager.login(username, password);
        if (account != null) {
            loggedInAccount = account;
            loginStatus.setForeground(Color.GREEN.darker());
            loginStatus.setText("Welcome, " + account.getUsername() + "!");
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            logoutButton.setEnabled(true);
            deleteButton.setEnabled(true);

            usernameField.setText("");
            passwordField.setText("");
            usernameField.setEnabled(false);
            passwordField.setEnabled(false);
            showPassword.setEnabled(false);

            usernameField.setBackground(Color.LIGHT_GRAY);
            passwordField.setBackground(Color.LIGHT_GRAY);
        } else {
            loginStatus.setForeground(Color.RED);
            loginStatus.setText("Wrong username or password!");
        }

    }

    private void logout() {
        loggedInAccount = null;
        loginStatus.setForeground(Color.BLACK);
        loginStatus.setText("Logged out.");
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);
        logoutButton.setEnabled(false);
        deleteButton.setEnabled(false);
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        showPassword.setEnabled(true);

        usernameField.setBackground(Color.WHITE);
        passwordField.setBackground(Color.WHITE);
    }

    private void deleteAccount() {

        if (!deleteConfirmed) {
            deleteConfirmation();
        } else {
            if (accountManager.deleteAccount(loggedInAccount.getUsername(), loggedInAccount) == null) {
                deleteConfirmed = false;
                loginStatus.setForeground(Color.RED);
                loginStatus.setText("Account deleted.");

                loggedInAccount = null;

                registerButton.setEnabled(true);
                loginButton.setEnabled(true);

                logoutButton.setEnabled(false);
                deleteButton.setEnabled(false);

                usernameField.setEnabled(true);
                passwordField.setEnabled(true);
                showPassword.setEnabled(true);

                usernameField.setBackground(Color.WHITE);
                passwordField.setBackground(Color.WHITE);
            } else {
                loginStatus.setText(accountManager.deleteAccount(loggedInAccount.getUsername(), loggedInAccount));
            }
        }
    }

    private void deleteConfirmation() {
        int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to permenantly delete this account?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            deleteConfirmed = true;
            deleteAccount();
        } else {
            loginStatus.setForeground(Color.BLACK);
            loginStatus.setText("Deletion Cancelled");
        }

    }

    private static JPanel semiTransparentPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        return p;
    }

    public static JButton semiTransparentButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setBackground(TERTIARY_COLOR);
        return button;
    }

    /**
     * The main method is responsible for creating a thread
     * that will construct and show the GUI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainMenu(null, null));
    }
}
