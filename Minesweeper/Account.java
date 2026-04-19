package Minesweeper;
import java.io.Serializable;

/**
 * Simple Account System for Leaderboards
 * 
 * @author Ahyaan Malik (so far)
 * @version 4/19/2026
 */
public class Account implements Serializable {

    private final String username;
    private final String passwordHashed;
    
    public Account(String username, String passwordHashed) {
        this.username = username;
        this.passwordHashed = passwordHashed;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHashed;
    }
}
