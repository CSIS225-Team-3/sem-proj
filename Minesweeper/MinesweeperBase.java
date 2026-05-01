package Minesweeper;

import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public abstract class MinesweeperBase extends JPanel {
    private boolean gameOver = false;

    protected Timer timer;
    protected int secondsElapsed;
    protected JLabel timerLabel;

    protected Account leaderboardAccount;
    protected String leaderboardDifficulty;
    protected String leaderboardMode;
    protected LeaderboardManager leaderboardManager;

    public void setLeaderboardInfo(LeaderboardManager leaderboardManager, Account account, String difficulty,
            String mode) {
        this.leaderboardManager = leaderboardManager;
        this.leaderboardAccount = account;
        this.leaderboardDifficulty = difficulty;
        this.leaderboardMode = mode;
    }

    protected boolean isEligibleForLeaderboard() {
        return true;
    }

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

            if (isEligibleForLeaderboard() && leaderboardManager != null
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
