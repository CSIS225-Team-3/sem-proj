import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * Animates the bar using threads for Password Checker
 * 
 * @author Ahyaan Malik
 */
public class AnimatedBar extends Thread {

    private float target;

    private JProgressBar strengthBar;

    public AnimatedBar(int target, JProgressBar strengthBar) {
        this.target = (float)(target * 20);
        this.strengthBar = strengthBar;
    }

    @Override
    public void run() {
        float current = strengthBar.getValue();
        while (current != target) {
            float diff = target - current;
            current += diff / 7.0f;

            // if (step == 0) {
            //     if (diff > 0) {
            //         step = 1;
            //     } else {
            //         step = -1;
            //     }
            // }
            // final int next = current + step;
            final float next = current;
            SwingUtilities.invokeLater(() -> {
                strengthBar.setValue((int)(next));
                strengthBar.setString(String.valueOf((int)(next0)));
            });

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
