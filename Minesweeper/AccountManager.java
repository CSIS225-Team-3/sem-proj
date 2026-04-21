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
 * @author Ahyaan Malik (so far)
 * @version 4/19/2026
 */
public class AccountManager {

    private HashMap<String, Account> accounts;

    private static final String SAVE_FILE = "Minesweeper//accounts.dat";

    public AccountManager() {

        accounts = loadAccounts();
    }

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

    private HashMap<String, Account> loadAccounts() {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            // funny unchecked cast
            return (HashMap<String, Account>) in.readObject();

        } catch (Exception e) {
            // none exists yet
            return new HashMap<String, Account>();
        }

    }

    private void saveAccounts() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(accounts);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

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

    public String deleteAccount(String username, Account account) {
        if (!accounts.containsKey(username)) {
            return "Account doesn't exist.";
        }
        accounts.remove(username);

        //remember: remove leaderboard saves too
        
        //success
        return null;
    }

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

    public static void main(String[] args) {
        AccountManager am = new AccountManager();
        am.listAccounts();
    }
}
