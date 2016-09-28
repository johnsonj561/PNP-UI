package manual_control_ui.view;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * JPanel that displays emergency stop button
 * @author Justin Johnson
 */
public class EmergencyStopView extends JPanel{

	/**
	 * Constructor initializes emergency stop button
	 */
	public EmergencyStopView() {
		initUI();
	}
	
	/**
	 * Initialize UI elements for JPanel
	 */
	private void initUI(){
		try {
			emergencyStopImage = ImageIO.read(getClass().getResource("/images/stop-button-150x150.png"));
		} catch (IOException e) {
			System.out.println("EmergencyStopView:\nError reading image path when declaring JButton");
			e.printStackTrace();
			emergencyStopImage = null;
		}
		emergencyStopButton = new JButton(new ImageIcon(emergencyStopImage));
		emergencyStopButton.setBorder(BorderFactory.createEmptyBorder());
		emergencyStopButton.setContentAreaFilled(false);
		emergencyStopButton.setFocusable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(emergencyStopButton);
	}
	
	
	
	
	
	/**
	 * Default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public JButton emergencyStopButton;
	private Image emergencyStopImage;
	
}
