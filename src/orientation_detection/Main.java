package orientation_detection;
import javax.swing.JOptionPane;

/**
 * Main for testing of Vision System
 * @author Justin Johnson
 *
 */
public class Main {

	public static void main(String[] args) {
		ComponentFinder compFinder = new ComponentFinder();
		if(compFinder != null){
			compFinder.captureImageData();
			String output = "X: " + compFinder.getXCenter() + 
					"\nY: " + compFinder.getYCenter() +
					"\nOrientation: " + compFinder.getOrientation();
			JOptionPane.showMessageDialog(null, output, "Component Orientation", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}