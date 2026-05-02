package Minesweeper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Manages the leaderboard for the Minesweeper game.
 * 
 * @author Ahyaan Malik
 * @version 4/30/2026
 */
public class LeaderboardManager {
    /** The file path for saving the leaderboard data */
    private static final String SAVE_FILE = "Minesweeper//leaderboard.dat";

    /** The maximum number of entries to keep in each leaderboard */
    private static final int MAX_ENTRIES = 10;

    /** The list of valid difficulties */
    public static final String[] DIFFICULTIES = { "Easy", "Medium", "Hard", "Extreme" };

    /** The list of valid modes */
    public static final String[] MODES = {
            "2D Minesweeper", "3D Minesweeper", "4D Minesweeper",
            "5D+ Minesweeper", "Hyperbolic Minesweeper"
    };

    /** key: Mode|Difficulty, value: sorted list of entries */
    private HashMap<String, List<LeaderboardEntry>> leaderboards;

    /** Constructs a new leaderboard manager */
    public LeaderboardManager() {
        leaderboards = loadLeaderboards();
    }

    /**
     * Resets the leaderboard by clearing all entries and saving the empty
     * leaderboards
     */
    public void resetLeaderboard() {
        leaderboards.clear();
        saveLeaderboards();
    }

    /**
     * Adds an entry to the leaderboard for the given mode and difficulty if it
     * qualifies, and saves the updated leaderboards. Returns true if the entry made
     * the leaderboard, false otherwise.
     * 
     * @param mode       The mode
     * @param difficulty The difficulty
     * @param username   The username for the entry
     * @param seconds    The time in seconds for the entry
     * @return true if the entry made the leaderboard, false otherwise
     */
    public boolean addEntry(String mode, String difficulty, String username, int seconds) {
        if (mode == null || difficulty == null || username == null) {
            return false;
        }

        boolean modeGood = false;
        for (String m : MODES) {
            if (m.equals(mode)) {
                modeGood = true;
            }
        }

        boolean diffGood = false;
        for (String d : DIFFICULTIES) {
            if (d.equals(difficulty)) {
                diffGood = true;
            }
        }

        if (!modeGood || !diffGood) {
            return false;
        }

        String key = mode + "|" + difficulty;

        if (!leaderboards.containsKey(key)) {
            leaderboards.put(key, new ArrayList<>());
        }
        List<LeaderboardEntry> list = leaderboards.get(key);
        LeaderboardEntry entry = new LeaderboardEntry(username, seconds);
        LeaderboardEntry existing = null;
        for (LeaderboardEntry e : list) {
            if (e.getUsername().equals(username)) {
                existing = e;
                break;
            }
        }

        if (existing != null) {
            if (seconds < existing.getSeconds()) {
                list.remove(existing);
            } else {
                return false;
            }
        }
        list.add(entry);
        Collections.sort(list);

        boolean madeBoard = list.indexOf(entry) < MAX_ENTRIES;
        while (list.size() > MAX_ENTRIES) {
            list.remove(list.size() - 1);
        }
        saveLeaderboards();
        return madeBoard;
    }

    /**
     * Gets the list of leaderboard entries for the given mode and difficulty,
     * sorted
     * 
     * @param mode       The mode
     * @param difficulty The difficulty
     * @return the list of leaderboard entries for the given mode and difficulty,
     *         sorted
     */
    public List<LeaderboardEntry> getEntries(String mode, String difficulty) {
        List<LeaderboardEntry> list = leaderboards.get(mode + "|" + difficulty);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * Loads the leaderboards from the save file, or returns an empty leaderboard if
     * 
     * @return the loaded leaderboards, or an empty leaderboard if loading failed
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, List<LeaderboardEntry>> loadLeaderboards() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (HashMap<String, List<LeaderboardEntry>>) in.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * Saves the leaderboards to the save file
     */
    private void saveLeaderboards() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(leaderboards);
        } catch (Exception e) {
            System.err.println("Leaderboard save failed: " + e.getMessage());
        }
    }

}
