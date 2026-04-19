package Minesweeper;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Account Manager for all Accounts
 * Will use SHA-256 since it hasn't been broken yet
 * 
 * @author Ahyaan Malik (so far)
 * @version 4/19/2026
 */
public class AccountManager {



    public AccountManager() {

    }

    public String hashPassword(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            return new String(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public Account login(String username, String password) {
        //TODO: Implement a way to load accounts
        Account account = null;

        if (account == null) {
            return null;
        }
        if (!(account.getPasswordHash().equals(hashPassword(password)))) {
            return account;
        } 
        return null;
    }
}
