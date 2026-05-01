package Minesweeper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LeaderboardManager {

    private static final String SAVE_FILE = "Minesweeper//leaderboard.dat";
    private static final int MAX_ENTRIES = 10;

    public static final String[] DIFFICULTIES = { "Easy", "Medium", "Hard", "Extreme" };
    public static final String[] MODES = {
            "2D Minesweeper", "3D Minesweeper", "4D Minesweeper",
            "5D+ Minesweeper", "Hyperbolic Minesweeper"
    };

    /* key: Mode|Difficulty, value: sorted list of entries */
    private HashMap<String, List<LeaderboardEntry>> leaderboards;

    public LeaderboardManager() {
        leaderboards = loadLeaderboards();
    }

    public void resetLeaderboard() {
        leaderboards.clear();
        saveLeaderboards();
    }

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

    public List<LeaderboardEntry> getEntries(String mode, String difficulty) {
        List<LeaderboardEntry> list = leaderboards.get(mode + "|" + difficulty);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    private HashMap<String, List<LeaderboardEntry>> loadLeaderboards() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (HashMap<String, List<LeaderboardEntry>>) in.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveLeaderboards() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(leaderboards);
        } catch (Exception e) {
            System.err.println("Leaderboard save failed: " + e.getMessage());
        }
    }

}
