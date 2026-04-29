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
            int step = diff / 7;

            if (step == 0) {
                if (diff > 0) {
                    step = 1;
                } else {
                    step = -1;
                }
            }
            final int next = current + step;
            SwingUtilities.invokeLater(() -> {
                strengthBar.setValue(next);
                strengthBar.setString(String.valueOf(next));
            });

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
