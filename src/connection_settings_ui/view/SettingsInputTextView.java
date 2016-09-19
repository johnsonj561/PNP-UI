package connection_settings_ui.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * JPanel view that includes a label, input text field, and description laid horizontally
 * @author Justin Johnson
 *
 */
public class SettingsInputTextView extends JPanel{

	/**
	 * Constructor builds UI View that contains label, textfield input, and description
	 * @param String label to be displayed to left of text field
	 * @param String description to be displayed to right of text field
	 */
	public SettingsInputTextView(String label, String description) {
		this.label = label;
		this.description = description;
		initUI();
	}

	/**
	 * Initialize UI elements of Settings Input Text Field view
	 */
	private void initUI(){
		//Input field label panel
		inputLabelPanel = new JPanel();
		inputLabelPanel.setLayout(new BoxLayout(inputLabelPanel, BoxLayout.X_AXIS));
		inputLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputLabelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		inputLabel = new JLabel(label);
		//Consider setting minimum size to force alignment!
		//inputLabel.setSize(new Dimension(500, inputLabel.getPreferredSize().height));
		inputLabel.setMinimumSize(new Dimension(LABEL_WIDTH, inputLabel.getPreferredSize().height));
		inputLabel.setPreferredSize(new Dimension(LABEL_WIDTH, inputLabel.getPreferredSize().height));
		inputLabel.setMaximumSize(new Dimension(LABEL_WIDTH, inputLabel.getPreferredSize().height));
		inputLabelPanel.add(inputLabel);
		//Text Input Field
		textInputPanel = new JPanel();
		textInputPanel.setLayout(new BoxLayout(textInputPanel, BoxLayout.X_AXIS));
		textInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		inputFileTextField = new JTextField();
		inputFileTextField.setMaximumSize(new Dimension(TEXT_FIELD_WIDTH, inputFileTextField.getPreferredSize().height));
		textInputPanel.add(inputFileTextField);

		//description text field
		descriptionPanel = new JPanel();
		descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.X_AXIS));
		descriptionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		descriptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		descriptionLabel = new JLabel(description);
		descriptionLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		descriptionPanel.add(descriptionLabel);

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(inputLabelPanel);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
		this.add(textInputPanel);
		this.add(Box.createRigidArea(new Dimension(25,0)));
		this.add(descriptionPanel);
	}
	
	/**
	 * Updates the input's text field with new value
	 * @param inpuText of the file selected by user
	 */
	public void setInputText(String inpuText){
		inputFileTextField.setText(inpuText);
	}

	/**
	 * Get input path from Text Field
	 * @return String input path
	 */
	public String getInputText(){
		return inputFileTextField.getText();
	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	private String label;
	private String description;
	private JPanel inputLabelPanel;
	private JPanel textInputPanel;
	private JPanel descriptionPanel;
	private JLabel inputLabel;
	private JLabel descriptionLabel;
	private JTextField inputFileTextField;
	private final int LABEL_WIDTH = 70;
	private final int TEXT_FIELD_WIDTH = 100;
}
