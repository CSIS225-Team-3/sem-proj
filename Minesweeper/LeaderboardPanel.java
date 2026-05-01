package Minesweeper;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class LeaderboardPanel extends JPanel {

    private static final String[] MODES = { "2D", "3D", "4D", "5D+", "Hyperbolic" };
    private static final String[] DIFFICULTIES = { "Easy", "Medium", "Hard", "Extreme" };

    private static final Color TAB_ACTIVE = new Color(65, 150, 255, 220);
    private static final Color TAB_INACTIVE = new Color(30, 100, 200, 120);
    private static final Color ROW_EVEN = MainMenu.SECONDARY_COLOR;
    private static final Color ROW_ODD = new Color(20, 70, 160, 190);
    private static final Color RANK_GOLD = new Color(255, 210, 50);
    private static final Color RANK_SILVER = new Color(200, 200, 200);
    private static final Color RANK_BRONZE = new Color(200, 130, 60);

    private CardLayout cardLayout;
    private JPanel cards;
    private LeaderboardManager leaderboardManager;

    private JButton[] modeButtons;
    private JButton[] diffButtons;
    private int selectedMode = 0;
    private int selectedDiff = 0;

    private JPanel rowsPanel;

    public LeaderboardPanel(CardLayout cardLayout, JPanel cards, LeaderboardManager leaderboardManager) {
        this.cardLayout = cardLayout;
        this.cards = cards;
        this.leaderboardManager = leaderboardManager;

        setLayout(new BorderLayout(0, 0));
        setOpaque(false);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 18, 6, 18));

        JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 34f));
        bar.add(title, BorderLayout.CENTER);

        JButton back = MainMenu.styledButton("Back");
        back.setPreferredSize(new Dimension(110, 36));
        back.addActionListener(e -> cardLayout.show(cards, "Menu"));
        bar.add(back, BorderLayout.WEST);

        return bar;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));

        JPanel modeRow = buildTabRow(MODES, modeButtons = new JButton[MODES.length], i -> {
            selectedMode = i;
            refreshRows();
        });
        JPanel diffRow = buildTabRow(DIFFICULTIES, diffButtons = new JButton[DIFFICULTIES.length], i -> {
            selectedDiff = i;
            refreshRows();
        });

        JPanel tabsPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        tabsPanel.setOpaque(false);
        tabsPanel.add(modeRow);
        tabsPanel.add(diffRow);

        rowsPanel = MainMenu.styledPanel(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
        rowsPanel.setBackground(MainMenu.SECONDARY_COLOR);
        rowsPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(rowsPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(TAB_ACTIVE, 2));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        center.add(tabsPanel, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        setActive(modeButtons, 0);
        setActive(diffButtons, 0);
        refreshRows();

        return center;
    }

    private JPanel buildTabRow(String[] labels, JButton[] buttons, IntConsumer onSelect) {
        JPanel row = new JPanel(new GridLayout(1, labels.length, 0, 0));
        row.setOpaque(false);
        for (int i = 0; i < labels.length; i++) {
            final int index = i;
            JButton btn = styledTabButton(labels[i]);
            btn.addActionListener(e -> {
                setActive(buttons, index);
                onSelect.accept(index);
            });
            buttons[i] = btn;
            row.add(btn);
        }
        return row;
    }

    private void refreshRows() {
        rowsPanel.removeAll();

        String modeFull = LeaderboardManager.MODES[selectedMode];
        String diff = DIFFICULTIES[selectedDiff];
        List<LeaderboardEntry> entries = leaderboardManager.getEntries(modeFull, diff);

        if (entries.isEmpty()) {
            JLabel empty = new JLabel("No scores yet", SwingConstants.CENTER);
            empty.setForeground(new Color(180, 180, 180));
            empty.setFont(empty.getFont().deriveFont(Font.ITALIC, 16f));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            rowsPanel.add(empty);
        } else {
            for (int i = 0; i < entries.size(); i++) {
                rowsPanel.add(buildRow(i + 1, entries.get(i), i % 2 == 0));
            }
        }

        rowsPanel.revalidate();
        rowsPanel.repaint();
    }

    private JPanel buildRow(int rank, LeaderboardEntry entry, boolean even) {
        Color bg;
        if (even) {
            bg = ROW_EVEN;
        } else {
            bg = ROW_ODD;
        }

        JPanel row = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(bg);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel rankLabel = new JLabel("#" + rank);
        rankLabel.setFont(rankLabel.getFont().deriveFont(Font.BOLD, 18f));
        rankLabel.setForeground(rankColor(rank));
        rankLabel.setPreferredSize(new Dimension(46, 40));
        rankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        row.add(rankLabel, BorderLayout.WEST);

        JLabel nameLabel = new JLabel(entry.getUsername());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        nameLabel.setForeground(Color.WHITE);
        row.add(nameLabel, BorderLayout.CENTER);

        JPanel timePanel = new JPanel(new BorderLayout(0, 2));
        timePanel.setOpaque(false);

        JLabel timeLabel = new JLabel(entry.getFormattedTime(), SwingConstants.RIGHT);
        timeLabel.setFont(timeLabel.getFont().deriveFont(Font.BOLD, 16f));
        timeLabel.setForeground(Color.WHITE);

        JLabel dateLabel = new JLabel(entry.getTimestamp(), SwingConstants.RIGHT);
        dateLabel.setFont(dateLabel.getFont().deriveFont(10f));
        dateLabel.setForeground(new Color(180, 210, 255));

        timePanel.add(timeLabel, BorderLayout.CENTER);
        timePanel.add(dateLabel, BorderLayout.SOUTH);
        row.add(timePanel, BorderLayout.EAST);

        return row;
    }

    private Color rankColor(int rank) {
        if (rank == 1)
            return RANK_GOLD;
        if (rank == 2)
            return RANK_SILVER;
        if (rank == 3)
            return RANK_BRONZE;
        return new Color(180, 180, 180);
    }

    private JButton styledTabButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(TAB_INACTIVE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 13f));
        return btn;
    }

    private void setActive(JButton[] buttons, int activeIdx) {
        for (int i = 0; i < buttons.length; i++) {
            if (i == activeIdx) {
                buttons[i].setBackground(TAB_ACTIVE);
            } else {
                buttons[i].setBackground(TAB_INACTIVE);
            }
        }
    }

    public void refresh() {
        refreshRows();
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }
}