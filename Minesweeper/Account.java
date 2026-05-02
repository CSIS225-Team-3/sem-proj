package Minesweeper;
import java.io.Serializable;

/**
 * Simple Account System for Leaderboards
 * 
 * @author Ahyaan Malik
 * @version 4/19/2026
 */
public class Account implements Serializable {

    /** The username for the account */
    private final String username;

    /** The hashed password for the account */
    private final String passwordHashed;

    /** The serial version UID */
    private static final long serialVersionUID  = 1L;
    
    /**
     * Constructs a account with the input username and hashed password.
     *
     * @param username the username for the account
     * @param passwordHashed the hashed password for secure storage
     */
    public Account(String username, String passwordHashed) {
        this.username = username;
        this.passwordHashed = passwordHashed;
    }

    /**
     * Returns username of this account.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns passwordhash of this account.
     *
     * @return the passwordhash
     */
    public String getPasswordHash() {
        return passwordHashed;
    }
}
