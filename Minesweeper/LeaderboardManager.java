package Minesweeper;

public class LeaderboardManager {
    private static LeaderboardManager instance;
 
    private static final String SAVE_FILE   = "Minesweeper//leaderboard.dat";
    private static final int    MAX_ENTRIES = 10;
 
    public static final String[] DIFFICULTIES = { "Easy", "Medium", "Hard", "Extreme" };
    public static final String[] MODES = {
        "2D Minesweeper", "3D Minesweeper", "4D Minesweeper",
        "5D+ Minesweeper", "Hyperbolic Minesweeper"
    };

    //TODO: figure out a good data structure to store leaderboard, hashmap most likely

    public LeaderboardManager() {
        //LOAD DATA
    }
}
