package Password_Strength_Checker;

import java.util.List;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Random;
import java.awt.GridLayout;

/**
 * User Interface for the password strength checker.
 */
public class PasswordChecker implements Runnable, ActionListener {

    /** Array of phrases for weak passwords */
    private String[] phrasesWeak;

    /** Array of phrases for medium passwords */
    private String[] phrasesMedium;

    /** Array of phrases for strong passwords */
    private String[] phrasesStrong;

    /** Array of phrases for very strong passwords */
    private String[] phrasesVeryStrong;

    /** Array of common passwords */
    private String[] commonPasswords;

    /** Label for displaying password strength message */
    private JLabel strengthMessage;

    /** Password input field */
    private JPasswordField passwordBox;

    /** Checkbox for showing/hiding password */
    private JCheckBox showPassword;

    /** Progress bar for displaying password strength */
    private JProgressBar strengthBar;

    /** The current password strength */
    private int strength;

    /**
     * Panel to store labels which guide the user on the qualifications of a strong
     * password
     */
    private JPanel passwordChecksPanel;

    /** List of JLabels which reveal aspects of the password */
    private List<JLabel> checksLabels = new ArrayList<JLabel>();

    /** Constants for password strength levels */
    private static final int LOW_STRENGTH = 10;
    private static final int MEDIUM_STRENGTH = 20;
    private static final int HIGH_STRENGTH = 30;

    private static final Color PRIMARY_COLOR = new Color(15, 30, 20);
    private static final Color SECONDARY_COLOR = new Color(30, 55, 35);
    private static final Color TERTIARY_COLOR = new Color(20, 40, 25);

    private static final Color TEXT_COLOR = new Color(200, 200, 200);

    @Override
    /**
     * Initializes the interface and sets up the components.
     */
    public void run() {
        // Doesn't apply to existing JFrames, so must be called first
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Password Strength Checker");
        frame.setPreferredSize(new Dimension(450, 320));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JPanel topPanel = new JPanel(new GridLayout(2, 0));

        JLabel title = new JLabel("Password Strength Checker", JLabel.CENTER);
        topPanel.add(title);

        JPanel passwordPanel = new JPanel(new FlowLayout());
        JLabel passwordLabel = new JLabel("Enter Password: ");
        passwordPanel.add(passwordLabel);

        passwordBox = new JPasswordField(20);
        passwordBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                getAndUpdateScore();
                setGuides();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                getAndUpdateScore();
                setGuides();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        passwordPanel.add(passwordBox);

        topPanel.add(passwordPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel showPasswordPanel = new JPanel(new FlowLayout());
        showPassword = new JCheckBox("Show Password");
        showPassword.addActionListener(this);
        showPasswordPanel.add(showPassword);
        centerPanel.add(showPasswordPanel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel(new FlowLayout());
        strengthMessage = new JLabel(" ");
        messagePanel.add(strengthMessage);
        centerPanel.add(messagePanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        // Center panel done
        JPanel passwordChecksWrapperPanel = new JPanel(new FlowLayout());
        TitledBorder border = BorderFactory.createTitledBorder("Password Requirements");
        passwordChecksWrapperPanel.setBorder(border);

        passwordChecksPanel = new JPanel();
        passwordChecksPanel.setLayout(new BoxLayout(passwordChecksPanel, BoxLayout.Y_AXIS));
        // Populating password checking labels
        checksLabels.add(new JLabel(" Uppercase Letter (A-Z)", SwingConstants.CENTER));
        checksLabels.add(new JLabel(" Lowercase Letter (a-z)", SwingConstants.CENTER));
        checksLabels.add(new JLabel(" Symbol (!#%^?./)", SwingConstants.CENTER));
        checksLabels.add(new JLabel(" Number (0-9)", SwingConstants.CENTER));
        checksLabels.add(new JLabel(" Has a commonly used password phrase", SwingConstants.CENTER));
        checksLabels.add(new JLabel(" Has an ascending or descending seqence of numbers", SwingConstants.CENTER));



        for (JLabel label : checksLabels) {
            label.setForeground(Color.RED);
            passwordChecksPanel.add(label);
        }
        passwordChecksWrapperPanel.add(passwordChecksPanel);
        centerPanel.add(passwordChecksWrapperPanel, BorderLayout.SOUTH);

        // Strength bar
        strengthBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 30);
        strengthBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        strengthBar.setString("");
        strengthBar.setStringPainted(true);
        mainPanel.add(strengthBar, BorderLayout.SOUTH);

        showPassword.setForeground(TEXT_COLOR);
        strengthMessage.setForeground(TEXT_COLOR);
        strengthBar.setForeground(TEXT_COLOR);
        passwordLabel.setForeground(TEXT_COLOR);
        title.setForeground(TEXT_COLOR);
        border.setTitleColor(TEXT_COLOR);

        frame.setBackground(PRIMARY_COLOR);
        mainPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBackground(PRIMARY_COLOR);
        passwordPanel.setBackground(PRIMARY_COLOR);
        centerPanel.setBackground(PRIMARY_COLOR);
        messagePanel.setBackground(PRIMARY_COLOR);
        showPasswordPanel.setBackground(PRIMARY_COLOR);
        showPassword.setBackground(PRIMARY_COLOR);

        passwordChecksWrapperPanel.setBackground(SECONDARY_COLOR);
        passwordChecksPanel.setBackground(SECONDARY_COLOR);

        strengthBar.setBackground(PRIMARY_COLOR);

        frame.pack();
        frame.setVisible(true);

        initPhrases();
        initCommonPasswords();
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
     * Initializes the arrays of phrases for different password strength levels.
     */
    private void initCommonPasswords() {
        commonPasswords = new String[] {
            "password", "password1", "password123", "passw0rd",
            "qwerty", "qwerty123", "qwertyuiop",
            "abc123", "111111", "000000", "123123", "654321",
            "admin", "admin123", "root", "letmein", "welcome", "login",
            "test", "test123", "guest", "user",
            "iloveyou", "dragon", "baseball", "football",
            "superman", "batman", "trustno1", "asdfgh", "asdfghjkl", "zxcvbnm",
            "1q2w3e4r", "qazwsx", "hello", "sunshine"
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
        strength = (int) Math.log(combinations) / 2 -2;

        //checking for sequence or common password
        if (isCommonPassword(password) || isSequential(password)) {
            strength = strength - 10;
            if (strength < 2 ){
                strength = 2;
            }
        }

        if (strength < 0){
            strength = 0;
        }
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

    private void setGuides() {
        String password = new String(passwordBox.getPassword());

        if (hasUpperCase(password)) {
            checksLabels.get(0).setForeground(new Color(0, 200, 0));
        } else {
            checksLabels.get(0).setForeground(Color.RED);
        }

        if (hasLowerCase(password)) {
            checksLabels.get(1).setForeground(new Color(0, 200, 0));
        } else {
            checksLabels.get(1).setForeground(Color.RED);
        }

        if (hasSymbol(password)) {
            checksLabels.get(2).setForeground(new Color(0, 200, 0));
        } else {
            checksLabels.get(2).setForeground(Color.RED);
        }

        if (hasNum(password)) {
            checksLabels.get(3).setForeground(new Color(0, 200, 0));
        } else {
            checksLabels.get(3).setForeground(Color.RED);
        }

        if(isCommonPassword(password)) {
            checksLabels.get(4).setForeground(Color.RED);
        } else{
            checksLabels.get(4).setForeground(new Color(0, 200, 0));
        }

        if(isSequential(password)) {
            checksLabels.get(5).setForeground(Color.RED);
        } else{
            checksLabels.get(5).setForeground(new Color(0, 200, 0));
        }
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

    /**
     * Check if password is common
     * 
     * @param password the password to check
     * @return true if the password is included in the list of common passwords, false otherwise
     */
    private boolean isCommonPassword(String password) {
        String lower = password.toLowerCase();
        for (String p : commonPasswords) {
            if (lower.equals(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check for if password is ascending or descending string of numbers.
     * 
     * @param password the password to check
     * @return true if the password is sequence, false otherwise
     */
    private boolean isSequential(String password) {
        if (password.length() < 3) return false;
        boolean ascending = true;
        boolean descending = true;

        for (int i = 1; i < password.length(); i++) {
            if (password.charAt(i) != password.charAt(i - 1) + 1) {
                ascending = false;
            }
            if (password.charAt(i) != password.charAt(i - 1) - 1) {
                descending = false;
            }
        }

        return ascending || descending;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new PasswordChecker());
    }
}