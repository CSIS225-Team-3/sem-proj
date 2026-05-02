package Minesweeper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class representing an entry in the leaderboard, containing the username,
 * time, and timestamp
 * 
 * @author Ahyaan Malik
 * @version 4/30/2026
 */
public class LeaderboardEntry implements Serializable, Comparable<LeaderboardEntry> {
    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The username for the account */
    private final String username;

    /** The time spent for the attempt */
    private final int secondsElapsed;

    /** The timestamp for the attempt */
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

    /**
     * Gets the username for this entry
     * 
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the time spent for this entry in seconds
     * 
     * @return the time spent for this entry in seconds
     */
    public int getSeconds() {
        return secondsElapsed;
    }

    /**
     * Gets the timestamp for this entry
     * 
     * @return the timestamp for this entry
     */
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    /**
     * Compares this entry to another entry based on time spent, for sorting
     * purposes
     * 
     * @param o the other entry to compare to
     * @return a negative integer, zero, or a positive integer as this entry is less
     *         than, equal to, or greater than the specified object.
     */
    public int compareTo(LeaderboardEntry o) {
        return Integer.compare(this.secondsElapsed, o.secondsElapsed);
    }

    /**
     * Gets the time spent for this entry in a formatted string of the form M:SS
     * @return the time spent for this entry in a formatted string of the form M:SS
     */
    public String getFormattedTime() {
        return (secondsElapsed / 60) + ":" + String.format("%02d", secondsElapsed % 60);
    }
}
