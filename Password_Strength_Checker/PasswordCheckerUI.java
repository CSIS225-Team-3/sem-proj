package Password_Strength_Checker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.*;

/**
 * User Interface for the password strength checker.
 */
public class PasswordCheckerUI implements Runnable, ActionListener {

    private JTextField passwordBox;
    private JLabel resultLabel;
    private JButton checkButton;

    private int strength;

    @Override
    public void run() {

        JFrame frame = new JFrame("Password Strength Checker");
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JLabel title = new JLabel("Password Strength Checker", JLabel.CENTER);
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.add(new JLabel("Enter Password:"));

        passwordBox = new JTextField(20);
        passwordBox.addActionListener(e -> {
            getAndUpdateScore();
        });
        centerPanel.add(passwordBox);

        checkButton = new JButton("Check");
        checkButton.addActionListener(this);
        centerPanel.add(checkButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        resultLabel = new JLabel("Strength: ", JLabel.CENTER);
        mainPanel.add(resultLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getAndUpdateScore();
    }

    private void getAndUpdateScore(){
        calculateScore();
        setColor();
        resultLabel.setText("Strength: " + strength);
    }

    private void calculateScore() {
        String password = passwordBox.getText();

        // reset strength
        strength = 0;

        // Length contributes 1 point per character
        strength += password.length();

        if (hasUpperCase(password)) {
            strength *= 1.2;
        }

        if (hasLowerCase(password)) {
            strength *= 1.2;
        }

        if (hasNum(password)) {
            strength *= 1.2;
        }

        if (hasSymbol(password)) {
            strength *= 1.2;
        }

    }

    private void setColor() {
        if (strength < 10) {
            resultLabel.setForeground(Color.RED);
        } else if (strength < 20) {
            resultLabel.setForeground(Color.ORANGE);
        } else if (strength < 30) {
            resultLabel.setForeground(Color.YELLOW);
        } else {
            resultLabel.setForeground(Color.GREEN);
        }
    }

    private boolean hasUpperCase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasLowerCase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNum(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSymbol(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (!Character.isDigit(password.charAt(i)) && !Character.isUpperCase(password.charAt(i))
                    && !Character.isLowerCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}