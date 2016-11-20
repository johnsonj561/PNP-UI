package manual_control_ui.view;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *Displays text field and send button to allow user to send single line commands to PNP Machine
 * @author Justin Johnson
 *
 */
public class ManualInstructionView extends JPanel{

	/**
	 * Constructor builds custom JPanel that contains a heading, file path text field, and filechooser button
	 * @param heading
	 */
	public ManualInstructionView(String heading) {
		this.heading = heading;
		initUI();
	}

	/**
	 * Initialize UI elements for JPanel
	 */
	private void initUI(){
		//Input heading panel
		instructionHeadingPanel = new JPanel();
		instructionHeadingPanel.setLayout(new BoxLayout(instructionHeadingPanel, BoxLayout.X_AXIS));
		instructionHeadingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		instructionHeadingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		sendInstructionLabel = new JLabel(heading);
		instructionHeadingPanel.add(sendInstructionLabel);
		//Input file panel
		instructionTextFieldPanel = new JPanel();
		instructionTextFieldPanel.setLayout(new BoxLayout(instructionTextFieldPanel, BoxLayout.X_AXIS));
		instructionTextFieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		instructionTextFieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		instructionInput = new JTextField(30);
		instructionInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, instructionInput.getPreferredSize().height));
		sendInstructionButton = new JButton("Send Command");
		//add components to panel
		instructionTextFieldPanel.add(instructionInput);
		instructionTextFieldPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		instructionTextFieldPanel.add(sendInstructionButton);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(instructionHeadingPanel);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
		this.add(instructionTextFieldPanel);
	}

	/**
	 * Updates text field to display the file path selected by user
	 * @param path of the file selected by user
	 */
	public void updateInstructionText(String path){
		instructionInput.setText(path);
	}

	/**
	 * Get input path from Text Field
	 * @return String input path
	 */
	public String getInstructionText(){
		return instructionInput.getText();
	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	private JPanel instructionHeadingPanel;
	private JPanel instructionTextFieldPanel;
	private JLabel sendInstructionLabel;
	public JTextField instructionInput;
	private String heading;
	public JButton sendInstructionButton;
}
