import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * Animates the bar using threads for Password Checker
 * 
 * @author Ahyaan Malik
 */
public class AnimatedBar extends Thread {

    private int target;

    private JProgressBar strengthBar;

    public AnimatedBar(int target, JProgressBar strengthBar) {
        this.target = target;
        this.strengthBar = strengthBar;
    }

    @Override
    public void run() {
        while (strengthBar.getValue() != target) {
            int current = strengthBar.getValue();
            int diff = target - current;

            // testing
            int next = 2;
            SwingUtilities.invokeLater(() -> {
                strengthBar.setValue(next);
                strengthBar.setString(String.valueOf(next));
            });

            // 16ms
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
