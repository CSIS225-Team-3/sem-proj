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
    
    /**
     * Constructs a account with the input username and hashed password.
     *
     * @param username the username for the account
     * @param passwordHashed the hashed password for secure storage
     */
    public Account(String username, String passwordHashed) {
        this.USERNAME = username;
        this.PASSWORD_HASHED = passwordHashed;
    }

    /**
     * Returns username of this account.
     *
     * @return the username
     */
    public String getUsername() {
        return USERNAME;
    }

    /**
     * Returns passwordhash of this account.
     *
     * @return the passwordhash
     */
    public String getPasswordHash() {
        return PASSWORD_HASHED;
    }
}
