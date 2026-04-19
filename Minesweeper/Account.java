package Minesweeper;
import java.io.Serializable;

/**
 * Simple Account System for Leaderboards
 * 
 * @author Ahyaan Malik (so far)
 * @version 4/19/2026
 */
public class Account implements Serializable {

    private final String USERNAME;
    private final String PASSWORD_HASHED;

    // Needed according to API
    private static final long serialVersionUID  = 1L;
    
    public Account(String username, String passwordHashed) {
        this.USERNAME = username;
        this.PASSWORD_HASHED = passwordHashed;
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getPasswordHash() {
        return PASSWORD_HASHED;
    }
}
