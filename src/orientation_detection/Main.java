package orientation_detection;
import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {
		ComponentFinder compFinder;
			compFinder = new ComponentFinder();
		
		if(compFinder != null){
			compFinder.captureImageData();
			String output = "X: " + compFinder.getXCenter() + 
					"\nY: " + compFinder.getYCenter() +
					"\nOrientation: " + compFinder.getOrientation();
			JOptionPane.showMessageDialog(null, output, "Component Orientation", JOptionPane.INFORMATION_MESSAGE);
			
		}
	}
}