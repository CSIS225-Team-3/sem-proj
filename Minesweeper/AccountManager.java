package Minesweeper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Account Manager for all Accounts
 * Will use SHA-256 since it hasn't been broken yet
 * 
 * @author Ahyaan Malik (so far)
 * @version 4/19/2026
 */
public class AccountManager {

    private HashMap<String, Account> accounts;

    private static final String SAVE_FILE = "accounts.dat";

    public AccountManager() {

        accounts = loadAccounts();
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

    private HashMap<String, Account> loadAccounts() {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            // funny unchecked cast
            return (HashMap<String, Account>) in.readObject();

        } catch (Exception e) {
            // none exists yet
            return new HashMap<String, Account>();
        }

    }

    public boolean register(String username, String password) {
        if (accounts.containsKey(username)) {
            return false;
        } else {
            accounts.put(username, new Account(username, hashPassword(password)));

            // TODO: SAVE HERE

            return true;
        }
    }

    public Account login(String username, String password) {
        Account account = accounts.get(username);

        if (account == null) {
            return null;
        }
        if (!(account.getPasswordHash().equals(hashPassword(password)))) {
            return account;
        }
        return null;
    }
}
