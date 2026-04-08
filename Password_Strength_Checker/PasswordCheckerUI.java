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
    private JButton checkButton;
    private JProgressBar strengthBar;

    private int strength;

    @Override
    public void run() {
        // Doesn't apply to existing JFrames, so must be called first
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Password Strength Checker");
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JLabel title = new JLabel("Password Strength Checker", JLabel.CENTER);
        mainPanel.add(title, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.add(new JLabel("Enter Password:"));

        passwordBox = new JTextField(20);
        passwordBox.addActionListener(e -> {
            // This event gets fired when enter is pressed in the field
            getAndUpdateScore();
        });
        centerPanel.add(passwordBox);

        checkButton = new JButton("Check");
        checkButton.addActionListener(this);
        centerPanel.add(checkButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        // Center panel done

        strengthBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 30);
        strengthBar.setString("");
        strengthBar.setStringPainted(true);
        mainPanel.add(strengthBar, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getAndUpdateScore();
    }

    private void getAndUpdateScore() {
        calculateScore();
        setColor();
        strengthBar.setValue(strength);
        strengthBar.setString(String.valueOf(strength));
    }

    private void calculateScore() {
        String password = passwordBox.getText();

        // Reset strength
        strength = 0;

        // Length contributes 1 point per character
        strength = password.length();

        if (hasUpperCase(password)) {
            strength *= 26; //26 uppercase letters
        }

        if (hasLowerCase(password)) {
            strength *= 26; //26 lowercase letters
        }

        if (hasNum(password)) {
            strength *= 10; //10 numbers
        }

        if (hasSymbol(password)) {
            strength *= 32; //~32 symbols
        }

        //Need to scale this down, maybe with log?
        // strength /= 100;
    }

    private void setColor() {
        if (strength < 10)
            strengthBar.setForeground(Color.RED);
        else if (strength < 20)
            strengthBar.setForeground(Color.ORANGE);
        else if (strength < 30)
            strengthBar.setForeground(Color.YELLOW);
        else
            strengthBar.setForeground(Color.GREEN);
    }

    private boolean hasUpperCase(String password) {
        for (int i = 0; i < password.length(); i++)
            if (Character.isUpperCase(password.charAt(i)))
                return true;
        return false;
    }

    private boolean hasLowerCase(String password) {
        for (int i = 0; i < password.length(); i++)
            if (Character.isLowerCase(password.charAt(i)))
                return true;
        return false;
    }

    private boolean hasNum(String password) {
        for (int i = 0; i < password.length(); i++)
            if (Character.isDigit(password.charAt(i)))
                return true;
        return false;
    }

    private boolean hasSymbol(String password) {
        for (int i = 0; i < password.length(); i++)
            if (!Character.isDigit(password.charAt(i)) &&
                    !Character.isUpperCase(password.charAt(i)) &&
                    !Character.isLowerCase(password.charAt(i)))
                return true;
        return false;
    }
}