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

    private String[] phrasesWeak;
    private String[] phrasesMedium;
    private String[] phrasesStrong;
    private String[] phrasesVeryStrong;

    private JLabel strengthMessage;

    private JTextField passwordBox;
    private JButton checkButton;
    private JProgressBar strengthBar;

    private int strength;


    private static final int LOW_STRENGTH = 10;
    private static final int MEDIUM_STRENGTH = 20;
    private static final int HIGH_STRENGTH = 30;

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

        strengthMessage = new JLabel(" ");
        centerPanel.add(strengthMessage, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        // Center panel done

        strengthBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 30);
        strengthBar.setString("");
        strengthBar.setStringPainted(true);
        mainPanel.add(strengthBar, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        initPhrases();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getAndUpdateScore();
    }

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

    private void setStrengthMessage() {
        if (strength < LOW_STRENGTH)
            strengthMessage.setText(phrasesWeak[(int) (Math.random() * phrasesWeak.length)]);
        else if (strength < MEDIUM_STRENGTH)
            strengthMessage.setText(phrasesMedium[(int) (Math.random() * phrasesMedium.length)]);
        else if (strength < HIGH_STRENGTH)
            strengthMessage.setText(phrasesStrong[(int) (Math.random() * phrasesStrong.length)]);
        else
            strengthMessage.setText(phrasesVeryStrong[(int) (Math.random() * phrasesVeryStrong.length)]);
    }

    private void getAndUpdateScore() {
        calculateScore();
        setColor();
        setStrengthMessage();
        strengthBar.setValue(strength);
        strengthBar.setString(String.valueOf(strength));
    }

    private void calculateScore() {
        String password = passwordBox.getText();

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