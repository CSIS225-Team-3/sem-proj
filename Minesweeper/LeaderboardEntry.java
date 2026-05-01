package Minesweeper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LeaderboardEntry implements Serializable, Comparable<LeaderboardEntry> {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final int secondsElapsed;
    private final String timestamp;

    /**
     * Constructs a leaderboard entry with the input username and elapsed seconds.
     *
     * @param username       the username for the account
     * @param secondsElapsed the time spent for the attempt
     */
    public LeaderboardEntry(String username, int secondsElapsed) {
        this.username = username;
        this.secondsElapsed = secondsElapsed;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getUsername() {
        return username;
    }

    public int getSeconds() {
        return secondsElapsed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(LeaderboardEntry o) {
        return Integer.compare(this.secondsElapsed, o.secondsElapsed);
    }

    public String getFormattedTime() {
        return (secondsElapsed / 60) + ":" + String.format("%02d", secondsElapsed % 60);
    }
}
