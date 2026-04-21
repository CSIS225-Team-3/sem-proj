package Minesweeper;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Timer;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
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

    private JCheckBox randomMines;

    /** The label for displaying error messages */
    private JLabel errorLabel;

    private JButton twoDimension;

    private JButton threeDimension;

    private JButton fourDimension;

    private JButton hyperbolic;

    private JRadioButton easyBtn;
    private JRadioButton mediumBtn;
    private JRadioButton hardBtn;
    private JRadioButton extremeBtn;

    private JButton startButton;
    private String selectedMode;
    private JLabel[] difficultyInfoLabels;

    private JSpinner splicesSpinner;
    private JLabel splicesLabel;

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
    public static Color PRIMARY_COLOR = Color.PINK;

    /** The secondary color for the UI */
    public static Color SECONDARY_COLOR = new Color(207, 125, 151);

    /** The tertiary color for the UI */
    public static Color TERTIARY_COLOR = new Color(255, 190, 200);

    private float rainbowHue = 0;

    public final static int MAX_ROWS = 100;
    public final static int MAX_COLS = 100;
    public final static int MAX_SPLICES = 100;

    public final static int MAX_MINES_2D = MAX_ROWS * MAX_COLS - 1;
    public final static int MAX_MINES_3D = MAX_ROWS * MAX_COLS * MAX_SPLICES - 1;

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

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        JPanel topPanel = new JPanel(new BorderLayout());

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        registerButton = new JButton("Register");
        loginButton = new JButton("Login");
        logoutButton = new JButton("Log Out");
        deleteButton = new JButton("Delete Account");

        registerButton.addActionListener(this);
        loginButton.addActionListener(this);
        logoutButton.addActionListener(this);
        deleteButton.addActionListener(this);

        logoutButton.setEnabled(false);
        deleteButton.setEnabled(false);

        JPanel loginMainPanel = new JPanel(new BorderLayout(5, 5));

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.add(new JLabel("Username:", SwingConstants.RIGHT));
        fieldsPanel.add(usernameField);

        fieldsPanel.add(new JLabel("Password:", SwingConstants.RIGHT));
        fieldsPanel.add(passwordField);

        showPassword = new JCheckBox();
        showPassword.addActionListener(this);

        fieldsPanel.add(new JLabel("Show Password", SwingConstants.RIGHT));
        fieldsPanel.add(showPassword);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel topButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        topButtons.add(registerButton);
        topButtons.add(loginButton);

        JPanel bottomButtons = new JPanel(new GridLayout(1, 2, 5, 5));
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

        // TODO: Higher the location of it

        mainText = new JLabel("Welcome to Minesweeper!", SwingConstants.CENTER);
        mainText.setFont(mainText.getFont().deriveFont(48.0f));
        topPanel.add(mainText, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel modePanel = new JPanel(new GridLayout(4, 1, 0, 5));
        modePanel.setBorder(BorderFactory.createTitledBorder("Select Mode"));
        modePanel.setBackground(SECONDARY_COLOR);

        twoDimension = new JButton("2D Minesweeper");
        threeDimension = new JButton("3D Minesweeper (WIP)");
        fourDimension = new JButton("4D Minesweeper (Coming Soon!)");
        hyperbolic = new JButton("Hyperbolic Minesweeper (Coming Soon!)");

        JButton[] dimensions = { twoDimension, threeDimension, fourDimension, hyperbolic };
        for (JButton b : dimensions) {
            b.setBackground(TERTIARY_COLOR);
            b.addActionListener(this);
            modePanel.add(b);
        }

        centerPanel.add(modePanel, BorderLayout.NORTH);

        configPanel = new JPanel(new BorderLayout(0, 10));
        configPanel.setBackground(PRIMARY_COLOR);
        configPanel.setVisible(false);

        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Game Settings"));
        settingsPanel.setBackground(SECONDARY_COLOR);

        settingsPanel.add(new JLabel("Rows: "));
        rowsSpinner = new JSpinner(new SpinnerNumberModel(9, 2, MAX_ROWS, 1));
        rowsSpinner.setPreferredSize(new Dimension(60, 30));
        rowsSpinner.addChangeListener(this);
        settingsPanel.add(rowsSpinner);

        settingsPanel.add(new JLabel("Columns: "));
        colsSpinner = new JSpinner(new SpinnerNumberModel(9, 2, MAX_COLS, 1));
        colsSpinner.setPreferredSize(new Dimension(60, 30));
        colsSpinner.addChangeListener(this);
        settingsPanel.add(colsSpinner);

        splicesLabel = new JLabel("Splices: ");
        settingsPanel.add(splicesLabel);
        splicesSpinner = new JSpinner(new SpinnerNumberModel(9, 2, MAX_COLS, 1));
        splicesSpinner.setPreferredSize(new Dimension(60, 30));
        splicesSpinner.addChangeListener(this);
        settingsPanel.add(splicesSpinner);

        splicesLabel.setVisible(false);
        splicesSpinner.setVisible(false);

        JPanel mineConfigPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        mineConfigPanel.setBackground(SECONDARY_COLOR);
        mineConfigPanel.add(new JLabel("Mine Amount: "));
        minesSpinner = new JSpinner(new SpinnerNumberModel(10, 0, MAX_MINES_2D, 1));
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

        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
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

            JPanel each = new JPanel(new BorderLayout());
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

        startButton = new JButton("Start Game");
        startButton.setBackground(TERTIARY_COLOR);
        startButton.addActionListener(this);
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

        // WACKY RAINBOW TESTING
        // TODO: Make into its own method
        Timer rainbowTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rainbowHue = (rainbowHue + 0.002f) % 1f;

                PRIMARY_COLOR = Color.getHSBColor(rainbowHue, 0.3f, 1.0f);
                SECONDARY_COLOR = Color.getHSBColor((rainbowHue + 0.09f) % 1f, 0.5f, 0.85f);
                TERTIARY_COLOR = Color.getHSBColor((rainbowHue + 0.05f) % 1f, 0.25f, 1.0f);

                frame.setBackground(PRIMARY_COLOR);
                centerPanel.setBackground(PRIMARY_COLOR);
                configPanel.setBackground(PRIMARY_COLOR);
                bottomPanel.setBackground(PRIMARY_COLOR);
                errorPanel.setBackground(PRIMARY_COLOR);
                startPanel.setBackground(PRIMARY_COLOR);
                topPanel.setBackground(PRIMARY_COLOR);

                loginMainPanel.setBackground(SECONDARY_COLOR);
                buttonsPanel.setBackground(SECONDARY_COLOR);
                fieldsPanel.setBackground(SECONDARY_COLOR);
                topButtons.setBackground(SECONDARY_COLOR);
                bottomButtons.setBackground(SECONDARY_COLOR);
                showPassword.setBackground(SECONDARY_COLOR);

                modePanel.setBackground(SECONDARY_COLOR);
                settingsPanel.setBackground(SECONDARY_COLOR);
                mineConfigPanel.setBackground(SECONDARY_COLOR);
                difficultyPanel.setBackground(SECONDARY_COLOR);

                easyBtn.setBackground(SECONDARY_COLOR);
                mediumBtn.setBackground(SECONDARY_COLOR);
                hardBtn.setBackground(SECONDARY_COLOR);
                extremeBtn.setBackground(SECONDARY_COLOR);

                randomMines.setBackground(SECONDARY_COLOR);

                easyInfo.setBackground(SECONDARY_COLOR);
                mediumInfo.setBackground(SECONDARY_COLOR);
                hardInfo.setBackground(SECONDARY_COLOR);
                extremeInfo.setBackground(SECONDARY_COLOR);

                registerButton.setBackground(TERTIARY_COLOR);
                loginButton.setBackground(TERTIARY_COLOR);
                logoutButton.setBackground(TERTIARY_COLOR);
                deleteButton.setBackground(TERTIARY_COLOR);

                twoDimension.setBackground(TERTIARY_COLOR);
                threeDimension.setBackground(TERTIARY_COLOR);
                fourDimension.setBackground(TERTIARY_COLOR);
                hyperbolic.setBackground(TERTIARY_COLOR);

                startButton.setBackground(TERTIARY_COLOR);

            }
        });
        rainbowTimer.start();

        frame.setVisible(true);
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
        if (src == twoDimension || src == threeDimension || src == fourDimension || src == hyperbolic) {

            configPanel.setVisible(true);

            // USER SELECTING DIMENSION

            if (src == twoDimension) {

                selectedMode = TWO_DIMENSIONS;
                twoDimension.setEnabled(false);
                threeDimension.setEnabled(true);

                splicesLabel.setVisible(false);
                splicesSpinner.setVisible(false);

                difficultyInfoLabels[0].setText("9x9, 10 mines");
                difficultyInfoLabels[1].setText("16x16, 40 mines");
                difficultyInfoLabels[2].setText("16x30, 99 mines");
                difficultyInfoLabels[3].setText("30x50, 400 mines");
            } else if (src == threeDimension) {

                selectedMode = THREE_DIMENSIONS;
                twoDimension.setEnabled(true);
                threeDimension.setEnabled(false);

                splicesLabel.setVisible(true);
                splicesSpinner.setVisible(true);

                difficultyInfoLabels[0].setText("5x5x3, 15 mines");
                difficultyInfoLabels[1].setText("7x7x4, 60 mines");
                difficultyInfoLabels[2].setText("9x9x5, 150 mines");
                difficultyInfoLabels[3].setText("12x12x6, 400 mines");
            } else {
                selectedMode = "";
            }
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
                        splicesSpinner.setValue(3);
                        minesSpinner.setValue(15);
                    } else if (src == mediumBtn) {
                        rowsSpinner.setValue(7);
                        colsSpinner.setValue(7);
                        splicesSpinner.setValue(4);
                        minesSpinner.setValue(60);
                    } else if (src == hardBtn) {
                        rowsSpinner.setValue(9);
                        colsSpinner.setValue(9);
                        splicesSpinner.setValue(5);
                        minesSpinner.setValue(150);
                    } else if (src == extremeBtn) {
                        rowsSpinner.setValue(12);
                        colsSpinner.setValue(12);
                        splicesSpinner.setValue(6);
                        minesSpinner.setValue(400);
                    }
                    break;
                default:
                    return;
            }

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
            int splices;
            int mines;

            // TODO: Fix logic to work for other dimensions
            if (randomMines.isSelected()) {
                mines = new Random().nextInt(rows * cols - 1) + 1;
            } else {
                mines = (int) minesSpinner.getValue();
                if (mines > rows * cols - 1) {
                    errorLabel.setText("Too many mines!");
                    return;
                }
            }
            errorLabel.setText(" ");

            if (selectedMode.equals(TWO_DIMENSIONS)) {
                int[] dims = { cols, rows };
                cards.add(new MinesweeperEuclidean(dims, mines, cardLayout, cards), TWO_DIMENSIONS);
                cardLayout.show(cards, TWO_DIMENSIONS);
            } else if (selectedMode.equals(THREE_DIMENSIONS)) {
                // TODO: add 3D
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!difficultyAdjusted) {
            difficultyGroup.clearSelection();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        setBackground(PRIMARY_COLOR);

        super.paintComponent(g);
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
        usernameField.setText("");
        passwordField.setText("");
    }

    private void deleteAccount() {

        if (!deleteConfirmed) {
            deleteConfirmation();
        } else {
            if (accountManager.deleteAccount(loggedInAccount.getUsername(), loggedInAccount) == null) {
                deleteConfirmed = false;
                loginStatus.setForeground(Color.RED);
                loginStatus.setText("Account deleted.");

                registerButton.setEnabled(true);
                loginButton.setEnabled(true);

                logoutButton.setEnabled(false);
                deleteButton.setEnabled(false);
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

    /**
     * The main method is responsible for creating a thread
     * that will construct and show the GUI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainMenu(null, null));
    }
}
