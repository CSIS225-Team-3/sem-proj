import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * Animates the bar using threads for Password Checker
 * 
 * @author Ahyaan Malik
 */
public class AnimatedBar extends Thread {


    /* A varible to store the current target to interpolate the progress values */
    private float target;

    /* Integer to increase the divisions in the progress bar values to create smoother transitioning */
    final private int smoothnessScale = 20;

    /* A reference to the progressbar component to change its value */
    private JProgressBar strengthBar;

    /* Constructor for the Animmated bar, creates a target value for the bar utilizing scaling */
    public AnimatedBar(int target, JProgressBar strengthBar) {
        this.target = (float)(target * smoothnessScale);
        this.strengthBar = strengthBar;
    }

    /* Run function which updates the value of the strength bar */
    @Override
    public void run() {
        float current = strengthBar.getValue();
        while (current != target) {
            float diff = target - current;
            current += diff / 7.0f;

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
