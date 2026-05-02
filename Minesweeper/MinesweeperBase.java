package Minesweeper;

import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The base class for all Minesweeper game panels, containing shared logic for
 * handling game over conditions, timers, and leaderboard management.
 * 
 * @version 4/30/2026
 * @author Ahyaan Malik & Patrick Kosmider
 */
public abstract class MinesweeperBase extends JPanel {
    /** Indicates whether the game is over */
    private boolean gameOver = false;

    /** The timer for the game */
    protected Timer timer;

    /** The number of seconds elapsed in the game */
    protected int secondsElapsed;

    /** The label for displaying the timer */
    protected JLabel timerLabel;

    /** The account associated with the leaderboard entry for this game */
    protected Account leaderboardAccount;

    /** The difficulty for the leaderboard entry for this game */
    protected String leaderboardDifficulty;

    /** The mode for the leaderboard entry for this game */
    protected String leaderboardMode;

    /** The leaderboard manager for managing leaderboard entries */
    protected LeaderboardManager leaderboardManager;

    /**
     * Constructs a new MinesweeperBase with the specified parameters for
     * leaderboard
     * 
     * @param leaderboardManager the leaderboard manager to use for managing
     *                           leaderboard entries
     * @param account            the account to associate with the leaderboard entry
     *                           for this game
     * @param difficulty         the difficulty to associate with the leaderboard
     *                           entry for this game
     * @param mode               the mode to associate with the leaderboard entry
     *                           for this game
     */
    public void setLeaderboardInfo(LeaderboardManager leaderboardManager, Account account, String difficulty,
            String mode) {
        this.leaderboardManager = leaderboardManager;
        this.leaderboardAccount = account;
        this.leaderboardDifficulty = difficulty;
        this.leaderboardMode = mode;
    }

    /**
     * Resets the game by setting gameOver to false and resetting any necessary game
     * state. Should be called after a win or loss.
     */
    public void reset() {
        gameOver = false;
    }

    /**
     * Runs the victory logic (if the game isn't over)
     */
    public void onWin() {
        if (!gameOver) {
            gameOver = true;
            revealMines();
            if (timer != null)
                timer.stop();

            String message = "You win! Congratulations!\nTime spent: " + timerLabel.getText();

            if (leaderboardManager != null
                    && leaderboardAccount != null && leaderboardDifficulty != null) {
                boolean madeBoard = leaderboardManager.addEntry(
                        leaderboardMode, leaderboardDifficulty,
                        leaderboardAccount.getUsername(), secondsElapsed);
                if (madeBoard) {
                    message += "\nYou made the leaderboard!";
                }
            }

            JOptionPane.showMessageDialog(this, message);
            reset();
        }
    }

    /**
     * Runs the loss logic (if the game isn't over)
     */
    public void onLoss() {
        if (!gameOver) {
            gameOver = true;
            revealMines();

            if (timer != null) {
                timer.stop();
            }
            JOptionPane.showMessageDialog(this,
                    "You lose! Better luck next time. \n Time spent: " + timerLabel.getText());
            reset();
        }
    }

    /**
     * Reveals all mines on the board
     */
    protected abstract void revealMines();

    public void onTileClick(MouseEvent e) {
        MinesweeperButtonBase pressedButton = (MinesweeperButtonBase) e.getSource();
        if (SwingUtilities.isRightMouseButton(e)) {
            // Right click to flag
            pressedButton.toggleFlagged();
        } else {
            // Normal event
            pressedButton.reveal();
        }
    }
}
