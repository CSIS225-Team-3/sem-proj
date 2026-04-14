package Password_Strength_Checker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.*;
import java.util.Random;

/**
 * User Interface for the password strength checker.
 */
public class PasswordCheckerUI implements Runnable, ActionListener {

    /** Array of phrases for weak passwords */
    private String[] phrasesWeak;

    /** Array of phrases for medium passwords */
    private String[] phrasesMedium;

    /** Array of phrases for strong passwords */
    private String[] phrasesStrong;

    /** Array of phrases for very strong passwords */
    private String[] phrasesVeryStrong;

    /** Label for displaying password strength message */
    private JLabel strengthMessage;

    /** Password input field */
    private JPasswordField passwordBox;

    /** Checkbox for showing/hiding password */
    private JCheckBox showPassword;

    /** Button for checking password strength */
    private JButton checkButton;

    /** Progress bar for displaying password strength */
    private JProgressBar strengthBar;

    /** The current password strength */
    private int strength;

    /** Constants for password strength levels */
    private static final int LOW_STRENGTH = 10;
    private static final int MEDIUM_STRENGTH = 20;
    private static final int HIGH_STRENGTH = 30;

    @Override
    /**
     * Initializes the interface and sets up the components.
     */
    public void run() {
        // Doesn't apply to existing JFrames, so must be called first
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Password Strength Checker");
        frame.setPreferredSize(new Dimension(450, 300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JLabel title = new JLabel("Password Strength Checker", JLabel.CENTER);
        mainPanel.add(title, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();

        inputPanel.add(new JLabel("Enter Password:"));
        passwordBox = new JPasswordField(20);

        passwordBox.addActionListener(e -> {
            // This event gets fired when enter is pressed in the field
            getAndUpdateScore();
        });
        inputPanel.add(passwordBox);

        checkButton = new JButton("Check");
        checkButton.addActionListener(this);
        inputPanel.add(checkButton);
        centerPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();

        showPassword = new JCheckBox("Show Password");
        showPassword.addActionListener(this);
        optionsPanel.add(showPassword);

        strengthMessage = new JLabel(" ");
        optionsPanel.add(strengthMessage, BorderLayout.CENTER);

        centerPanel.add(optionsPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        // Center panel done

        strengthBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 30);
        strengthBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        strengthBar.setString("");
        strengthBar.setStringPainted(true);
        mainPanel.add(strengthBar, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        initPhrases();
    }

    @Override
    /**
     * Handles action events
     * 
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showPassword) {
            togglePasswordVisibility();
        } else {
            getAndUpdateScore();
        }
    }

    /**
     * Toggles password visability based on the state of the showPassword checkbox.
     */
    private void togglePasswordVisibility() {
        if (showPassword.isSelected()) {
            // Show password
            passwordBox.setEchoChar((char) 0);
        } else {
            // Hide password
            passwordBox.setEchoChar('*');
        }
    }

    /**
     * Initializes the arrays of phrases for different password strength levels.
     */
    private void initPhrases() {
        phrasesWeak = new String[] {
                "Your password is very weak. Consider a stronger password.",
                "Your password is weak. Consider changing your password.",
                "Your password is weak. Consider adding more complexity."
        };

        phrasesMedium = new String[] {
                "Your password is mildly weak. Consider a stronger password.",
                "Your password is decent. Consider adding more complexity.",
                "Your password is not bad, but could be stronger."
        };

        phrasesStrong = new String[] {
                "Your password is good, but it could be stronger.",
                "Your password is stronger than most, but it could be stronger.",
                "Your password is great, but it could be even better."
        };

        phrasesVeryStrong = new String[] {
                "Your password is very strong!",
                "Your password is excellent!",
                "Your password is amazing!"
        };
    }

    /**
     * Sets the strength message based on password strength randomly.
     */
    private void setStrengthMessage() {
        Random rand = new Random();

        if (strength < LOW_STRENGTH)
            strengthMessage.setText(phrasesWeak[rand.nextInt(0, phrasesWeak.length)]);
        else if (strength < MEDIUM_STRENGTH)
            strengthMessage.setText(phrasesMedium[rand.nextInt(0, phrasesMedium.length)]);
        else if (strength < HIGH_STRENGTH)
            strengthMessage.setText(phrasesStrong[rand.nextInt(0, phrasesStrong.length)]);
        else
            strengthMessage.setText(phrasesVeryStrong[rand.nextInt(0, phrasesVeryStrong.length)]);
    }

    /**
     * Calculates and updates the password strength score, color, message, and bar.
     */
    private void getAndUpdateScore() {
        calculateScore();
        setColor();
        setStrengthMessage();
        strengthBar.setValue(strength);
        strengthBar.setString(String.valueOf(strength));
    }

    /**
     * Calculates password strength
     */
    private void calculateScore() {
        String password = new String(passwordBox.getPassword());
        
        int choices = 0;

        if (hasUpperCase(password)) {
            choices += 26; // 26 uppercase letters
        }

        if (hasLowerCase(password)) {
            choices += 26; // 26 lowercase letters
        }

        if (hasNum(password)) {
            choices += 10; // 10 numbers
        }

        if (hasSymbol(password)) {
            choices += 32; // ~32 symbols
        }

        // Length ^ options per character
        // double is required because the result quickly becomes extremely large
        double combinations = Math.pow(choices, password.length());

        // Scale down the massive number
        strength = (int) Math.log(combinations) / 2;
    }

    /**
     * Sets color of the strength bar based on the password strength.
     */
    private void setColor() {
        if (strength < LOW_STRENGTH)
            strengthBar.setForeground(Color.RED);
        else if (strength < MEDIUM_STRENGTH)
            strengthBar.setForeground(Color.ORANGE);
        else if (strength < HIGH_STRENGTH)
            strengthBar.setForeground(new Color(220, 220, 0));
        else
            strengthBar.setForeground(new Color(0, 200, 0));
    }

    /**
     * Check for uppercase
     * 
     * @param password the password to check
     * @return true if the password contains an uppercase letter, false otherwise
     */
    private boolean hasUpperCase(String password) {
        for (int i = 0; i < password.length(); i++)
            if (Character.isUpperCase(password.charAt(i)))
                return true;
        return false;
    }

    /**
     * Check for lowercase
     * 
     * @param password the password to check
     * @return true if the password contains a lowercase letter, false otherwise
     */
    private boolean hasLowerCase(String password) {
        for (int i = 0; i < password.length(); i++)
            if (Character.isLowerCase(password.charAt(i)))
                return true;
        return false;
    }

    /**
     * Check for numbers
     * 
     * @param password the password to check
     * @return true if the password contains a number, false otherwise
     */
    private boolean hasNum(String password) {
        for (int i = 0; i < password.length(); i++)
            if (Character.isDigit(password.charAt(i)))
                return true;
        return false;
    }

    /**
     * Check for symbols
     * 
     * @param password the password to check
     * @return true if the password contains a symbol, false otherwise
     */
    private boolean hasSymbol(String password) {
        for (int i = 0; i < password.length(); i++)
            if (!Character.isDigit(password.charAt(i)) &&
                    !Character.isUpperCase(password.charAt(i)) &&
                    !Character.isLowerCase(password.charAt(i)))
                return true;
        return false;
    }
}