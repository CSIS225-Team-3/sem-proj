package Minesweeper;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Main Game to show the options for type of modes.
 * 
 * @author Ahyaan Malik
 * @version 4/14/2026
 */
public class MainMenu extends JPanel implements ActionListener, ChangeListener, Runnable {

    /** The name of the menu card */
    private static final String MENU_CARD = "Menu";

    /** The name of the 2D minesweeper card */
    private static final String TWO_DIMENSIONS = "2D Minesweeper";

    /** The name of the 3D minesweeper card */
    private static final String THREE_DIMENSIONS = "3D Minesweeper";

    /** The name of the 4D minesweeper card */
    private static final String FOUR_DIMENSIONS = "4D Minesweeper";

    /** The name of the 5D+ minesweeper card */
    private static final String FIVE_DIMENSIONS = "5D+ Minesweeper";

    /** The name of the hyperbolic minesweeper card */
    private static final String HYPERBOLIC = "Hyperbolic Minesweeper";

    /** The name of the leaderboard card */
    private static final String LEADERBOARD_CARD = "Leaderboard";

    /** The button for accessing the leaderboard */
    private JButton leaderboardButton;

    /** The panel for displaying the leaderboard */
    private LeaderboardPanel leaderboardPanel;

    /** The button for resetting the leaderboard */
    private JButton resetLeaderboardButton;

    /** The selected difficulty level */
    private String selectedDifficulty;

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

    /** The label for the rows spinner */
    private JLabel rowsLabel;

    /** The label for the columns spinner */
    private JLabel colsLabel;

    /** The spinner for configuring the number of mines */
    private JSpinner minesSpinner;

    /** The model for the mines spinner */
    private SpinnerNumberModel minesModel;

    /** The size of each button */
    private int buttonSize = 40;

    /** The checkbox for randomizing mine placement */
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

    /** The primary color for the UI */
    public static Color PRIMARY_COLOR = Color.WHITE;

    /** The secondary color for the UI */
    public static Color SECONDARY_COLOR = new Color(30, 100, 200, 175);

    /** The tertiary color for the UI */
    public static Color TERTIARY_COLOR = new Color(65, 150, 255, 220);

    public static Color TRANSPARENT_COLOR = new Color(0, 0, 0, 255);

    public static Color TEXT_COLOR = Color.WHITE;

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
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("MinesweeperMine.png"));
        frame.setIconImage(icon);

        cards = buildCardsPanel();

        leaderboardPanel = new LeaderboardPanel(cardLayout, cards, new LeaderboardManager());
        cards.add(leaderboardPanel, LEADERBOARD_CARD);

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        setBackground(PRIMARY_COLOR);

        cards.add(this, MENU_CARD);
        cardLayout.show(cards, MENU_CARD);
        frame.add(cards);
        setOpaque(false);
        frame.setVisible(true);
    }

    /**
     * Handles button clicks and user interactions within the menu.
     *
     * @param e action event triggered by user interaction
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == leaderboardButton) {
            cardLayout.show(cards, LEADERBOARD_CARD);
            return;
        }

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
        if (src == twoDimension || src == threeDimension || src == fourDimension || src == fiveDimension
                || src == hyperbolic) {
            handleModeSelection(src);
            return;
        }

        // USER SELECTING DIFFICULTY
        if (src == easyBtn || src == mediumBtn || src == hardBtn || src == extremeBtn) {
            handleDifficultySelection(src);
            return;
        }

        if (src == randomMines) {
            minesSpinner.setEnabled(!randomMines.isSelected());
            difficultyGroup.clearSelection();
            return;
        }

        // USER STARTING GAME
        if (src == startButton) {
            handleStartGame(src);
            return;
        }
    }

    /**
     * Handles changes to spinner values and updates UI.
     *
     * @param e the change event triggered by a spinner
     */
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

        updateMaxMines();
    }

    /**
     * Creates main card panel.
     *
     * @return JPanel with CardLayout and background
     */
    private JPanel buildCardsPanel() {
        BufferedImage background = null;
        try {
            background = ImageIO.read(new File("Minesweeper/Background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BufferedImage bg = background;
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout) {
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
        return panel;
    }

    /**
     * Builds top section of the UI including account controls,
     * login or register buttons, and the title.
     *
     * @return the constructed top panel
     */
    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        usernameField = new JTextField();

        usernameField.setForeground(TEXT_COLOR);
        usernameField.setBackground(SECONDARY_COLOR);
        usernameField.setOpaque(false);
        usernameField.setForeground(TEXT_COLOR);

        passwordField = new JPasswordField();

        passwordField.setForeground(TEXT_COLOR);
        passwordField.setBackground(SECONDARY_COLOR);
        passwordField.setOpaque(false);
        passwordField.setForeground(TEXT_COLOR);

        registerButton = styledButton("Register");
        loginButton = styledButton("Login");
        logoutButton = styledButton("Log Out");
        deleteButton = styledButton("Delete Account");

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

        JPanel loginMainPanel = styledPanel(new BorderLayout(5, 5));
        loginMainPanel.setOpaque(false);
        loginMainPanel.setBackground(SECONDARY_COLOR);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.setOpaque(false);

        JLabel usernameLabel = new JLabel("Username", SwingConstants.RIGHT);
        usernameLabel.setForeground(TEXT_COLOR);

        fieldsPanel.add(usernameLabel);
        fieldsPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password", SwingConstants.RIGHT);
        passwordLabel.setForeground(TEXT_COLOR);

        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);

        showPassword = new JCheckBox();
        showPassword.setOpaque(false);
        showPassword.setContentAreaFilled(false);
        showPassword.addActionListener(this);

        JLabel showPasswordLabel = new JLabel("Show Password", SwingConstants.RIGHT);
        showPasswordLabel.setForeground(TEXT_COLOR);
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
        loginStatus.setForeground(TEXT_COLOR);

        loginMainPanel.add(fieldsPanel, BorderLayout.NORTH);
        loginMainPanel.add(buttonsPanel, BorderLayout.CENTER);
        loginMainPanel.add(loginStatus, BorderLayout.SOUTH);

        TitledBorder loginBorder = BorderFactory.createTitledBorder("Account");
        loginBorder.setTitleColor(TEXT_COLOR);
        loginMainPanel.setBorder(loginBorder);

        topPanel.add(loginMainPanel, BorderLayout.EAST);

        mainText = new JLabel("Welcome to Minesweeper!", SwingConstants.CENTER);
        mainText.setForeground(TEXT_COLOR);
        mainText.setFont(mainText.getFont().deriveFont(48.0f));
        topPanel.add(mainText, BorderLayout.NORTH);

        JPanel leaderboardSide = new JPanel();
        leaderboardSide.setLayout(new BoxLayout(leaderboardSide, BoxLayout.Y_AXIS));
        leaderboardSide.setOpaque(false);

        leaderboardButton = styledButton("Leaderboard");
        leaderboardButton.setFont(leaderboardButton.getFont().deriveFont(Font.BOLD, 18f));
        leaderboardButton.setMaximumSize(new Dimension(180, 80));
        leaderboardButton.addActionListener(this);

        resetLeaderboardButton = styledButton("Reset Leaderboard");
        resetLeaderboardButton.setFont(resetLeaderboardButton.getFont().deriveFont(Font.BOLD, 12f));
        resetLeaderboardButton.setMaximumSize(new Dimension(180, 40));
        resetLeaderboardButton.setVisible(false);
        resetLeaderboardButton.addActionListener(e -> {
            leaderboardPanel.getLeaderboardManager().resetLeaderboard();
            leaderboardPanel.refresh();
            JOptionPane.showMessageDialog(frame, "Leaderboard reset.");
        });

        leaderboardSide.add(leaderboardButton);
        leaderboardSide.add(Box.createVerticalStrut(6));
        leaderboardSide.add(resetLeaderboardButton);

        topPanel.add(leaderboardSide, BorderLayout.WEST);

        topPanel.setOpaque(false);
        loginMainPanel.setOpaque(false);
        fieldsPanel.setOpaque(false);
        buttonsPanel.setOpaque(false);
        topButtons.setOpaque(false);
        bottomButtons.setOpaque(false);

        return topPanel;
    }

    /**
     * Builds the center section of the UI including mode selecting,
     * game options, and the start button.
     *
     * @return the center panel
     */
    private JPanel buildCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel modePanel = styledPanel(new GridLayout(5, 1, 0, 5));

        TitledBorder modeBorder = BorderFactory.createTitledBorder("Select Mode");
        modeBorder.setTitleColor(TEXT_COLOR);
        modePanel.setBorder(modeBorder);
        modePanel.setBackground(SECONDARY_COLOR);

        twoDimension = styledButton("2D Minesweeper");
        threeDimension = styledButton("3D Minesweeper");
        fourDimension = styledButton("4D Minesweeper");
        fiveDimension = styledButton("5D+ Minesweeper");
        hyperbolic = styledButton("Hyperbolic Minesweeper");
        // hyperbolic.setVisible(false);

        JButton[] dimensions = { twoDimension, threeDimension, fourDimension, fiveDimension, hyperbolic };
        for (JButton b : dimensions) {
            b.addActionListener(this);
            modePanel.add(b);
        }

        centerPanel.add(modePanel, BorderLayout.NORTH);

        configPanel = new JPanel(new BorderLayout(0, 10));
        configPanel.setBackground(PRIMARY_COLOR);
        configPanel.setVisible(false);

        JPanel settingsPanel = styledPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        TitledBorder settingsBorder = BorderFactory.createTitledBorder("Game Settings");
        settingsBorder.setTitleColor(TEXT_COLOR);
        settingsPanel.setBorder(settingsBorder);
        settingsPanel.setBackground(SECONDARY_COLOR);

        rowsLabel = new JLabel("Rows: ");
        rowsLabel.setForeground(TEXT_COLOR);
        settingsPanel.add(rowsLabel);
        rowsSpinner = new JSpinner(new SpinnerNumberModel(6, 2, MAX_ROWS, 1));
        rowsSpinner.setPreferredSize(new Dimension(60, 30));
        rowsSpinner.addChangeListener(this);
        settingsPanel.add(rowsSpinner);

        colsLabel = new JLabel("Columns: ");
        colsLabel.setForeground(TEXT_COLOR);
        settingsPanel.add(colsLabel);
        colsSpinner = new JSpinner(new SpinnerNumberModel(9, 2, MAX_COLS, 1));
        colsSpinner.setPreferredSize(new Dimension(60, 30));
        colsSpinner.addChangeListener(this);
        settingsPanel.add(colsSpinner);

        splices3dLabel = new JLabel("3D Splices: ");
        splices3dLabel.setForeground(TEXT_COLOR);
        settingsPanel.add(splices3dLabel);
        splices3dSpinner = new JSpinner(new SpinnerNumberModel(5, 2, MAX_SPLICES3D, 1));
        splices3dSpinner.setPreferredSize(new Dimension(60, 30));
        splices3dSpinner.addChangeListener(this);
        settingsPanel.add(splices3dSpinner);

        splices3dLabel.setVisible(false);
        splices3dSpinner.setVisible(false);

        splices4dLabel = new JLabel("4D Splices: ");
        splices4dLabel.setForeground(TEXT_COLOR);
        settingsPanel.add(splices4dLabel);
        splices4dSpinner = new JSpinner(new SpinnerNumberModel(3, 2, MAX_SPLICES4D, 1));
        splices4dSpinner.setPreferredSize(new Dimension(60, 30));
        splices4dSpinner.addChangeListener(this);
        settingsPanel.add(splices4dSpinner);

        splices4dLabel.setVisible(false);
        splices4dSpinner.setVisible(false);

        splices5dLabel = new JLabel("5D Splices: ");
        splices5dLabel.setForeground(TEXT_COLOR);
        settingsPanel.add(splices5dLabel);
        splices5dSpinner = new JSpinner(new SpinnerNumberModel(3, 2, MAX_SPLICES5D, 1));
        splices5dSpinner.setPreferredSize(new Dimension(60, 30));
        splices5dSpinner.addChangeListener(this);
        settingsPanel.add(splices5dSpinner);

        splices5dLabel.setVisible(false);
        splices5dSpinner.setVisible(false);

        dimensionsLabel = new JLabel("Dimension Count: ");
        dimensionsLabel.setForeground(TEXT_COLOR);
        settingsPanel.add(dimensionsLabel);
        dimensionsSpinner = new JSpinner(new SpinnerNumberModel(5, 5, MAX_DIMENSIONS, 1));
        dimensionsSpinner.setPreferredSize(new Dimension(60, 30));
        dimensionsSpinner.addChangeListener(this);
        settingsPanel.add(dimensionsSpinner);

        dimensionsLabel.setVisible(false);
        dimensionsSpinner.setVisible(false);

        JPanel mineConfigPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        mineConfigPanel.setOpaque(false);
        JLabel mineAmountLabel = new JLabel("Mine Amount: ");
        mineAmountLabel.setForeground(TEXT_COLOR);
        mineConfigPanel.add(mineAmountLabel);

        minesModel = new SpinnerNumberModel(10, 0, maxMines, 1);
        minesSpinner = new JSpinner(minesModel);
        minesSpinner.setPreferredSize(new Dimension(60, 30));
        minesSpinner.addChangeListener(this);

        mineConfigPanel.add(minesSpinner);
        JLabel randomMinesLabel = new JLabel("Random # of Mines: ");
        randomMinesLabel.setForeground(TEXT_COLOR);
        mineConfigPanel.add(randomMinesLabel);
        randomMines = new JCheckBox();
        // randomMines.setBackground(SECONDARY_COLOR);
        randomMines.setOpaque(false);
        randomMines.addActionListener(this);
        mineConfigPanel.add(randomMines);
        settingsPanel.add(mineConfigPanel);

        configPanel.add(settingsPanel, BorderLayout.NORTH);

        JPanel difficultyPanel = styledPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        TitledBorder difficultyBorder = BorderFactory.createTitledBorder("Preset Difficulties");
        difficultyBorder.setTitleColor(TEXT_COLOR);
        difficultyPanel.setBorder(difficultyBorder);
        difficultyPanel.setBackground(SECONDARY_COLOR);

        easyBtn = new JRadioButton("Easy");
        easyBtn.setForeground(TEXT_COLOR);
        mediumBtn = new JRadioButton("Medium");
        mediumBtn.setForeground(TEXT_COLOR);
        hardBtn = new JRadioButton("Hard");
        hardBtn.setForeground(TEXT_COLOR);
        extremeBtn = new JRadioButton("Extreme");
        extremeBtn.setForeground(TEXT_COLOR);

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

            JPanel each = new JPanel(new BorderLayout());
            each.setOpaque(false);
            // each.setBackground(SECONDARY_COLOR);

            infoLabels[i].setFont(infoLabels[i].getFont().deriveFont(10f));
            infoLabels[i].setOpaque(false);
            infoLabels[i].setForeground(TEXT_COLOR);
            // infoLabels[i].setBackground(SECONDARY_COLOR);

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

        startButton = styledButton("Start Game");
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

        centerPanel.setBackground(PRIMARY_COLOR);
        modePanel.setBackground(SECONDARY_COLOR);

        centerPanel.setOpaque(false);
        configPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        errorPanel.setOpaque(false);
        startPanel.setOpaque(false);

        startButton.setContentAreaFilled(false);

        return centerPanel;
    }

    /**
     * Updates the maximum number of mines allowed based on selected dimensions.
     */
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

    /**
     * Registers a new user account using the entered username and password.
     */
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

    /**
     * Attempts to log in a user with the provided credentials.
     */
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
            if (account.getUsername().equals("admin")) {
                resetLeaderboardButton.setVisible(true);
            }
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

    /**
     * Logs out user that is logged in and resets UI.
     */
    private void logout() {
        loggedInAccount = null;
        loginStatus.setForeground(TEXT_COLOR);
        loginStatus.setText("Logged out.");
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);
        logoutButton.setEnabled(false);
        deleteButton.setEnabled(false);
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        showPassword.setEnabled(true);
        resetLeaderboardButton.setVisible(false);

        usernameField.setBackground(Color.WHITE);
        passwordField.setBackground(Color.WHITE);
    }

    /**
     * Deletes the logged in account.
     */
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

                resetLeaderboardButton.setVisible(false);
            } else {
                loginStatus.setText(accountManager.deleteAccount(loggedInAccount.getUsername(), loggedInAccount));
            }
        }
    }

    /**
     * Prompts user to confirm account deletion.
     */
    private void deleteConfirmation() {
        int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to permenantly delete this account?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            deleteConfirmed = true;
            deleteAccount();
        } else {
            loginStatus.setForeground(TEXT_COLOR);
            loginStatus.setText("Deletion Cancelled");
        }

    }

    /**
     * Handles user seelcting game mode and updates the UI.
     *
     * @param src the source of the action event (selected mode button)
     */
    private void handleModeSelection(Object src) {

        configPanel.setVisible(true);

        // USER SELECTING DIMENSION

        if (src == twoDimension) {

            selectedMode = TWO_DIMENSIONS;
            dimensionsSelected = 2;

            twoDimension.setEnabled(false);
            threeDimension.setEnabled(true);
            fourDimension.setEnabled(true);
            fiveDimension.setEnabled(true);
            hyperbolic.setEnabled(true);

            rowsSpinner.setVisible(true);
            rowsLabel.setVisible(true);
            colsSpinner.setVisible(true);
            colsLabel.setVisible(true);

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
            hyperbolic.setEnabled(true);

            rowsSpinner.setVisible(true);
            rowsLabel.setVisible(true);
            colsSpinner.setVisible(true);
            colsLabel.setVisible(true);

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
            hyperbolic.setEnabled(true);

            rowsSpinner.setVisible(true);
            rowsLabel.setVisible(true);
            colsSpinner.setVisible(true);
            colsLabel.setVisible(true);

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
            hyperbolic.setEnabled(true);

            rowsSpinner.setVisible(true);
            rowsLabel.setVisible(true);
            colsSpinner.setVisible(true);
            colsLabel.setVisible(true);

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
        } else if (src == hyperbolic) {
            selectedMode = HYPERBOLIC;
            dimensionsSelected = -2;

            twoDimension.setEnabled(true);
            threeDimension.setEnabled(true);
            fourDimension.setEnabled(true);
            fiveDimension.setEnabled(true);
            hyperbolic.setEnabled(false);

            rowsSpinner.setVisible(false);
            rowsLabel.setVisible(false);
            colsSpinner.setVisible(false);
            colsLabel.setVisible(false);

            splices3dLabel.setVisible(false);
            splices3dSpinner.setVisible(false);

            splices4dLabel.setVisible(false);
            splices4dSpinner.setVisible(false);

            splices5dLabel.setVisible(false);
            splices5dSpinner.setVisible(false);

            dimensionsLabel.setVisible(false);
            dimensionsSpinner.setVisible(false);

            difficultyInfoLabels[0].setText("5 mines");
            difficultyInfoLabels[1].setText("8 mines");
            difficultyInfoLabels[2].setText("10 mines");
            difficultyInfoLabels[3].setText("12 mines");

            minesSpinner.setValue(20);
        } else {
            selectedMode = "";
        }

        updateMaxMines();
        minesModel.setMaximum(maxMines);

        mainText.setText("Mode: " + selectedMode);
        revalidate();
        repaint();
    }

    /**
     * Applies a preset difficulty setting based on selected button.
     *
     * @param src the source of the action event (selected difficulty button)
     */
    private void handleDifficultySelection(Object src) {

        if (src == easyBtn) {
            selectedDifficulty = "Easy";
        } else if (src == mediumBtn) {
            selectedDifficulty = "Medium";
        } else if (src == hardBtn) {
            selectedDifficulty = "Hard";
        } else if (src == extremeBtn) {
            selectedDifficulty = "Extreme";
        }

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
            case HYPERBOLIC:
                if (src == easyBtn) {
                    minesSpinner.setValue(5);
                } else if (src == mediumBtn) {
                    minesSpinner.setValue(8);
                } else if (src == hardBtn) {
                    minesSpinner.setValue(10);
                } else if (src == extremeBtn) {
                    minesSpinner.setValue(12);
                }
                break;
            default:
                return;
        }
        updateMaxMines();
        difficultyAdjusted = false;

    }

    /**
     * Starts the game using the selected mode.
     *
     * @param src the source of the action event (start button)
     */
    private void handleStartGame(Object src) {
        if (selectedMode == null) {
            return;
        }

        // hyperbolic
        if (dimensionsSelected == -2) {
            int mines;
            if (randomMines.isSelected()) {
                mines = new Random().nextInt(12) + 1;
            } else {
                mines = (int) minesSpinner.getValue();
            }

            String difficulty;
            if (randomMines.isSelected()) {
                difficulty = null;
            } else {
                difficulty = selectedDifficulty;
            }

            MinesweeperHyperbolic hypGame = new MinesweeperHyperbolic(mines, buttonSize, cardLayout, cards);
            hypGame.setLeaderboardInfo(leaderboardPanel.getLeaderboardManager(), loggedInAccount, difficulty,
                    HYPERBOLIC);
            cards.add(hypGame, HYPERBOLIC);
            cardLayout.show(cards, HYPERBOLIC);
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

            String difficulty;
            if (randomMines.isSelected()) {
                difficulty = null;
            } else {
                difficulty = selectedDifficulty;
            }

            MinesweeperEuclidean game = new MinesweeperEuclidean(dims, mines, buttonSize, cardLayout, cards);
            game.setLeaderboardInfo(leaderboardPanel.getLeaderboardManager(), loggedInAccount, difficulty,
                    TWO_DIMENSIONS);
            cards.add(game, TWO_DIMENSIONS);
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

            String difficulty;
            if (randomMines.isSelected()) {
                difficulty = null;
            } else {
                difficulty = selectedDifficulty;
            }

            MinesweeperEuclidean game = new MinesweeperEuclidean(dims, mines, buttonSize, cardLayout, cards);
            game.setLeaderboardInfo(leaderboardPanel.getLeaderboardManager(), loggedInAccount, difficulty,
                    THREE_DIMENSIONS);
            cards.add(game, THREE_DIMENSIONS);
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

            String difficulty;
            if (randomMines.isSelected()) {
                difficulty = null;
            } else {
                difficulty = selectedDifficulty;
            }

            MinesweeperEuclidean game = new MinesweeperEuclidean(dims, mines, buttonSize, cardLayout, cards);
            game.setLeaderboardInfo(leaderboardPanel.getLeaderboardManager(), loggedInAccount, difficulty,
                    FOUR_DIMENSIONS);
            cards.add(game, FOUR_DIMENSIONS);
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

            String difficulty;
            if (randomMines.isSelected()) {
                difficulty = null;
            } else {
                difficulty = selectedDifficulty;
            }

            MinesweeperEuclidean game = new MinesweeperEuclidean(dims, mines, buttonSize, cardLayout, cards);
            game.setLeaderboardInfo(leaderboardPanel.getLeaderboardManager(), loggedInAccount, difficulty,
                    FIVE_DIMENSIONS);
            cards.add(game, FIVE_DIMENSIONS);
            cardLayout.show(cards, FIVE_DIMENSIONS);
        }

    }

    /**
     * Creates a panel with a background.
     *
     * @param layout the layout manager to apply panel
     * @return a JPanel with style
     */
    public static JPanel styledPanel(LayoutManager layout) {
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

    /**
     * Creates a styled button.
     *
     * @param text text for display on the button
     * @return a styled JButton
     */
    public static JButton styledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        // button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setForeground(TEXT_COLOR);
        button.setBackground(TERTIARY_COLOR);
        return button;
    }

    /**
     * The main method is responsible for creating a thread
     * that will construct and show the GUI.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new MainMenu(null, null));
    }
}
