package manual_control_ui.view;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Error Message View that displays feedback to user when using PNP Manual Controls
 * @author Justin Johnson
 */
public class ErrorMessageView extends JPanel{

	/**
	 * HomeButtonView displays Homing buttons for PNP machine along X, Y, and Z axis
	 */
	public ErrorMessageView() {
		initUI();
	}

	/**
	 * Initialize Home Button elements
	 */
	private void initUI(){
		//Home button panel
		errorMessagePanel = new JPanel();
		errorMessagePanel.setLayout(new BoxLayout(errorMessagePanel, BoxLayout.X_AXIS));
		errorMessagePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		//title
		errorMessage = new JLabel("");
		errorMessage.setFont(new Font("Verdana", Font.BOLD, 12));
		errorMessage.setForeground(Color.RED);
		errorMessagePanel.add(errorMessage);
		//add elements to this layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(errorMessagePanel);
	}

	public void updateErrorMessage(String message){
		errorMessage.setText(message);
	}
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Variables
	 */
	private JPanel errorMessagePanel;
	private JLabel errorMessage;
}

