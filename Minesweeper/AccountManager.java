package Minesweeper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Account Manager for all Accounts
 * Will use SHA-256 since it hasn't been broken yet
 * 
 * @author Ahyaan Malik
 * @version 4/19/2026
 */
public class AccountManager {

    /** A HashMap to store all accounts */
    private HashMap<String, Account> accounts;

    /** The file path for saving accounts */
    private static final String SAVE_FILE = "Minesweeper//accounts.dat";

    /**
     * Constructs AccountManager and loads already made accounts from file.
     */
    public AccountManager() {

        accounts = loadAccounts();
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password the password to hash
     * @return the hashed password
     */
    public String hashPassword(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            return new String(hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return "";
    }

    /**
     * Loads saved accounts from the account save file.
     *
     * @return a HashMap containing saved accounts or empty
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, Account> loadAccounts() {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {

            return (HashMap<String, Account>) in.readObject();

        } catch (Exception e) {
            // none exists yet
            return new HashMap<String, Account>();
        }

    }

    /**
     * Saves the current accounts to the account save file.
     */
    private void saveAccounts() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(accounts);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Registers new account if the username and password.
     *
     * @param username the username for account
     * @param password the password for account
     * @return error message if registration fails or null if registration works
     */
    public String register(String username, String password) {
        // min 3, max 10
        if (username.length() < 3 || username.length() > 10) {
            return "Username must be 3-10 characters!";
        }
        if (!username.matches("[a-zA-Z0-9_]+")) {
            return "Username can only have letters, numbers, underscores!";
        }

        if (password.length() < 3) {
            return "Password must be at least 3 characters!";
        }

        if (accounts.containsKey(username)) {
            return "Username taken!";
        }

        accounts.put(username, new Account(username, hashPassword(password)));

        saveAccounts();

        // success
        return null;
    }

    /**
     * Attempts to log in with username and password.
     *
     * @param username the account username
     * @param password the account password
     * @return the matching Account if login succeeds or null if login fails
     */
    public Account login(String username, String password) {
        Account account = accounts.get(username);

        if (account == null) {
            return null;
        }
        if (account.getPasswordHash().equals(hashPassword(password))) {
            return account;
        }
        return null;
    }

    /**
     * Deletes a account and saves the updated account list.
     *
     * @param username the username of the account to delete
     * @param account the account object being deleted
     * @return an error message if deletion fails or null if deletion succeeds
     */
    public String deleteAccount(String username, Account account) {
        if (!accounts.containsKey(username)) {
            return "Account doesn't exist.";
        }
        accounts.remove(username);
        saveAccounts();

        // remember: remove leaderboard saves too

        // success
        return null;
    }

    /**
     * Prints all registered account usernames.
     */
    public void listAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts registered!");
            return;
        }
        System.out.println("=== Registered Accounts (" + accounts.size() + ") ===");
        for (String username : accounts.keySet()) {
            System.out.println("- " + username);
        }
    }

    /**
     * Creates an AccountManager and lists all registered accounts.
     *
     * @param args input arguments
     */
    public static void main(String[] args) {
        AccountManager am = new AccountManager();
        am.listAccounts();
    }
}
