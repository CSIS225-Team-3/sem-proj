package Minesweeper;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;

public class LeaderboardPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel cards;

    public LeaderboardPanel(CardLayout cardLayout, JPanel cards) {
        super();
        this.cardLayout = cardLayout;
        this.cards = cards;
        setLayout(new BorderLayout(0, 0));
        setOpaque(false);
    }
}