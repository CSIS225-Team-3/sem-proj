package Password_Strength_Checker;
import Password_Strength_Checker.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        resultLabel.setText("Strength: " + strength);
        return;
    }
}