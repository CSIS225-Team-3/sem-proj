package Minesweeper;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.CardLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;

/**
 * Main Game to show the options for type of modes.
 * 
 * @author Ahyaan Malik (so far)
 * @version 4/14/2026
 */
public class MainMenu extends JPanel implements ActionListener, Runnable {

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

    /** The label for displaying error messages */
    private JLabel errorLabel;

    private JButton twoDimension;

    private JButton threeDimension;

    private JButton fourDimension;

    private JButton hyperbolic;

    /** The primary color for the UI */
    public final static Color PRIMARY_COLOR = Color.PINK;

    /** The secondary color for the UI */
    public final static Color SECONDARY_COLOR = new Color(207, 125, 151);

    /** The tertiary color for the UI */
    public final static Color TERTIARY_COLOR = new Color(255, 190, 200);

    /**
     * Constructor for the MainMenu class.
     * 
     * @param cardLayout the CardLayout used to manage the different mode panels
     * @param cards      the JPanel that holds the different mode panels
     */
    public MainMenu(CardLayout cardLayout, JPanel cards) {
        this.cardLayout = cardLayout;
        this.cards = cards;

        setLayout(new BorderLayout());
    }

    /**
     * The run method to set up the GUI.
     */
    @Override
    public void run() {
        // Our basic GUI setup, a JFrame with a JPanel inside it.
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        mainText = new JLabel("Welcome to Minesweeper!", SwingConstants.CENTER);
        mainText.setFont(mainText.getFont().deriveFont(48.0f));
        add(mainText, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel configPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        configPanel.setBorder(BorderFactory.createTitledBorder("Game Settings"));

        configPanel.add(new JLabel("Rows: "));
        rowsSpinner = new JSpinner(new SpinnerNumberModel(9, 2, 40, 1));
        rowsSpinner.setPreferredSize(new Dimension(60, 30));
        configPanel.add(rowsSpinner);

        configPanel.add(new JLabel("Columns: "));
        colsSpinner = new JSpinner(new SpinnerNumberModel(9, 2, 40, 1));
        colsSpinner.setPreferredSize(new Dimension(60, 30));
        configPanel.add(colsSpinner);

        // configPanel.add(new JLabel("Mine Amount: "));
        // colsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1600, 1));
        // colsSpinner.setPreferredSize(new Dimension(60, 30));
        // configPanel.add(colsSpinner);

        centerPanel.add(configPanel, BorderLayout.NORTH);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorPanel.add(errorLabel);
        centerPanel.add(errorPanel, BorderLayout.CENTER);

        JPanel modesPanel = new JPanel(new GridLayout(6, 1, 0, 5));

        twoDimension = new JButton("2D Minesweeper (WIP)");
        twoDimension.addActionListener(this);
        threeDimension = new JButton("3D Minesweeper (Coming Soon!)");
        threeDimension.addActionListener(this);
        fourDimension = new JButton("4D Minesweeper (Coming Soon!)");
        fourDimension.addActionListener(this);
        hyperbolic = new JButton("Hyperbolic Minesweeper (Coming Soon!)");
        hyperbolic.addActionListener(this);

        modesPanel.add(twoDimension);
        modesPanel.add(threeDimension);
        modesPanel.add(fourDimension);
        modesPanel.add(hyperbolic);

        centerPanel.add(modesPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // CardLayout
        cards.add(this, MENU_CARD);

        cardLayout.show(cards, MENU_CARD);

        centerPanel.setBackground(PRIMARY_COLOR);
        modesPanel.setBackground(SECONDARY_COLOR);
        configPanel.setBackground(SECONDARY_COLOR);
        errorPanel.setBackground(PRIMARY_COLOR);

        twoDimension.setBackground(TERTIARY_COLOR);
        threeDimension.setBackground(TERTIARY_COLOR);
        fourDimension.setBackground(TERTIARY_COLOR);
        hyperbolic.setBackground(TERTIARY_COLOR);

        rowsSpinner.setBackground(TERTIARY_COLOR);
        colsSpinner.setBackground(TERTIARY_COLOR);

        setBackground(PRIMARY_COLOR);

        frame.add(cards);
        // Display the window we've created.
        frame.pack();
        frame.setVisible(true);

        // NOTE: Temp debug code since it's annoying to click
        cards.add(new MinesweeperTwoDimensions(10, 10, cardLayout, cards),
                TWO_DIMENSIONS);
        cardLayout.show(cards, TWO_DIMENSIONS);
    }

    /**
     * Listens for JButton actions and responds
     * 
     * @param e The JButton to listen for
     */
    public void actionPerformed(ActionEvent e) {

        int rows = (int) rowsSpinner.getValue();
        int cols = (int) colsSpinner.getValue();

        Object src = e.getSource();

        if (src == twoDimension) {
            cards.add(new MinesweeperTwoDimensions(rows, cols, cardLayout, cards), TWO_DIMENSIONS);
            cardLayout.show(cards, TWO_DIMENSIONS);
        } else if (src == threeDimension) {
            System.out.println("3D Minesweeper coming soon!");
        } else if (src == fourDimension) {
            System.out.println("4D Minesweeper coming soon!");
        } else if (src == hyperbolic) {
            System.out.println("Hyperbolic Minesweeper coming soon!");
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
