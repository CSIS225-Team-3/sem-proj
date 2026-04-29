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
        double current = strengthBar.getValue();
        while ((int) current != target) {
            double diff = target - current;
            current += diff / 7.0;

            // if (step == 0) {
            //     if (diff > 0) {
            //         step = 1;
            //     } else {
            //         step = -1;
            //     }
            // }
            // final int next = current + step;
            final int next = (int) current;
            SwingUtilities.invokeLater(() -> {
                strengthBar.setValue(next);
                strengthBar.setString(String.valueOf(next));
            });

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
