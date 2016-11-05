package define_parts_ui.view;

import define_parts_ui.model.PartConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * JPanel that enables user to add a new part to the Defined Part List or edit a current part in Defined Part List
 * @author Justin Johnson
 *
 */
public class AddNewPartView extends JPanel{

	/**
	 * Constructs a view that displays input fields for adding a new part to Defined Part List
	 * 
	 */
	public AddNewPartView() {
		initUI();
		//initialize (x,y) coordinates to -1, validation will confirm that values have been properly defined
		//before adding part to part list
		xInitial = -1;
		yInitial = -1;
		xFinal = -1;
		yFinal = -1;
	}

	/**
	 * Initialize UI elements
	 */
	private void initUI(){
		//Footprint selection panel
		footprintSelectionPanel = new JPanel();
		footprintSelectionPanel.setLayout(new BoxLayout(footprintSelectionPanel, BoxLayout.X_AXIS));
		footprintSelectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		footprintSelectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		footprintSelectionLabel = new JLabel("Footprint");
		footprintSelectionLabel.setMinimumSize(new Dimension(LABEL_WIDTH, footprintSelectionLabel.getPreferredSize().height));
		footprintSelectionLabel.setPreferredSize(new Dimension(LABEL_WIDTH, footprintSelectionLabel.getPreferredSize().height));
		footprintSelectionLabel.setMaximumSize(new Dimension(LABEL_WIDTH, footprintSelectionLabel.getPreferredSize().height));
		footprintSelectionComboBox = new JComboBox<String>(PartConstants.getAvailableFootprints());
		footprintSelectionComboBox.setPreferredSize(new Dimension(COMBO_BOX_WIDTH, footprintSelectionComboBox.getPreferredSize().height));
		footprintSelectionComboBox.setMaximumSize(new Dimension(COMBO_BOX_WIDTH, footprintSelectionComboBox.getPreferredSize().height));
		footprintSelectionComboBox.setMinimumSize(new Dimension(COMBO_BOX_WIDTH, footprintSelectionComboBox.getPreferredSize().height));
		footprintSelectionComboBox.setSelectedIndex(0);
		footprintSelectionPanel.add(footprintSelectionLabel);
		footprintSelectionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		footprintSelectionPanel.add(footprintSelectionComboBox);
		//Component value selection panel
		componentValueSelectionPanel = new JPanel();
		componentValueSelectionPanel.setLayout(new BoxLayout(componentValueSelectionPanel, BoxLayout.X_AXIS));
		componentValueSelectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		componentValueSelectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		componentValueSelectionLabel = new JLabel("Value");
		componentValueSelectionLabel.setMinimumSize(new Dimension(LABEL_WIDTH, footprintSelectionLabel.getPreferredSize().height));
		componentValueSelectionLabel.setPreferredSize(new Dimension(LABEL_WIDTH, footprintSelectionLabel.getPreferredSize().height));
		componentValueSelectionLabel.setMaximumSize(new Dimension(LABEL_WIDTH, footprintSelectionLabel.getPreferredSize().height));
		componentValueTextField = new JTextField();
		componentValueTextField.setMaximumSize(new Dimension(TEXT_FIELD_WIDTH, componentValueTextField.getPreferredSize().height));
		componentValueDescription = new JLabel(VALUE_DESCRIPTION);
		componentValueDescription.setFont(new Font("Verdana", Font.ITALIC, 12));
		componentValueSelectionPanel.add(componentValueSelectionLabel);
		componentValueSelectionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		componentValueSelectionPanel.add(componentValueTextField);
		componentValueSelectionPanel.add(Box.createRigidArea(new Dimension(30, 0)));
		componentValueSelectionPanel.add(componentValueDescription);
		//Cavity pitch selection panel
		tapeCavityPitchPanel = new JPanel();
		tapeCavityPitchPanel.setLayout(new BoxLayout(tapeCavityPitchPanel, BoxLayout.X_AXIS));
		tapeCavityPitchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		tapeCavityPitchPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		tapeCavityPitchLabel = new JLabel("Cavity Pitch");
		tapeCavityPitchLabel.setMinimumSize(new Dimension(LABEL_WIDTH, tapeCavityPitchLabel.getPreferredSize().height));
		tapeCavityPitchLabel.setPreferredSize(new Dimension(LABEL_WIDTH, tapeCavityPitchLabel.getPreferredSize().height));
		tapeCavityPitchLabel.setMaximumSize(new Dimension(LABEL_WIDTH, tapeCavityPitchLabel.getPreferredSize().height));
		tapeCavityPitchComboBox = new JComboBox<String>(PartConstants.getTapeCavityPitchValues());
		tapeCavityPitchComboBox.setPreferredSize(new Dimension(COMBO_BOX_WIDTH, tapeCavityPitchComboBox.getPreferredSize().height));
		tapeCavityPitchComboBox.setMaximumSize(new Dimension(COMBO_BOX_WIDTH, tapeCavityPitchComboBox.getPreferredSize().height));
		tapeCavityPitchComboBox.setMinimumSize(new Dimension(COMBO_BOX_WIDTH, tapeCavityPitchComboBox.getPreferredSize().height));
		tapeCavityPitchComboBox.setSelectedIndex(0);
		tapeCavityPitchDescription = new JLabel(CAVITY_PITCH_DESCRIPTION);
		tapeCavityPitchDescription.setFont(new Font("Verdana", Font.ITALIC, 12));
		tapeCavityPitchPanel.add(tapeCavityPitchLabel);
		tapeCavityPitchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		tapeCavityPitchPanel.add(tapeCavityPitchComboBox);
		tapeCavityPitchPanel.add(Box.createRigidArea(new Dimension(30, 0)));
		tapeCavityPitchPanel.add(tapeCavityPitchDescription);
		//First part location selection
		firstPartPositionPanel = new JPanel();
		firstPartPositionPanel.setLayout(new BoxLayout(firstPartPositionPanel, BoxLayout.X_AXIS));
		firstPartPositionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		firstPartPositionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		firstPartPositionLabel = new JLabel("First Part (X,Y)");
		firstPartPositionLabel.setMinimumSize(new Dimension(LABEL_WIDTH, firstPartPositionLabel.getPreferredSize().height));
		firstPartPositionLabel.setPreferredSize(new Dimension(LABEL_WIDTH, firstPartPositionLabel.getPreferredSize().height));
		firstPartPositionLabel.setMaximumSize(new Dimension(LABEL_WIDTH, firstPartPositionLabel.getPreferredSize().height));
		firstPartPositionButton = new JButton("Define Location");
		firstPartPositionButton.setMinimumSize(new Dimension(BUTTON_WIDTH, firstPartPositionButton.getPreferredSize().height));
		firstPartPositionButton.setPreferredSize(new Dimension(BUTTON_WIDTH, firstPartPositionButton.getPreferredSize().height));
		firstPartPositionButton.setMaximumSize(new Dimension(BUTTON_WIDTH, firstPartPositionButton.getPreferredSize().height));
		firstPartPositionDescription = new JLabel(FIRST_PART_DESCRIPTION);
		firstPartPositionDescription.setFont(new Font("Verdana", Font.ITALIC, 12));
		firstPartPositionPanel.add(firstPartPositionLabel);
		firstPartPositionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		firstPartPositionPanel.add(firstPartPositionButton);
		firstPartPositionPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		try {
			firstPartStatusImage = ImageIO.read(getClass().getResource("/images/red-circle-20x20.png"));
			firstPartStatusLabel = new JLabel(new ImageIcon(firstPartStatusImage));
		} catch (IOException e) {
			firstPartStatusLabel = new JLabel("");
			e.printStackTrace();
		}
		firstPartPositionPanel.add(firstPartStatusLabel);
		firstPartPositionPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		firstPartPositionPanel.add(firstPartPositionDescription);
		//Last part location selection
		lastPartPositionPanel = new JPanel();
		lastPartPositionPanel.setLayout(new BoxLayout(lastPartPositionPanel, BoxLayout.X_AXIS));
		lastPartPositionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		lastPartPositionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		lastPartPositionLabel = new JLabel("Last Part (X,Y)");
		lastPartPositionLabel.setMinimumSize(new Dimension(LABEL_WIDTH, lastPartPositionLabel.getPreferredSize().height));
		lastPartPositionLabel.setPreferredSize(new Dimension(LABEL_WIDTH, lastPartPositionLabel.getPreferredSize().height));
		lastPartPositionLabel.setMaximumSize(new Dimension(LABEL_WIDTH, lastPartPositionLabel.getPreferredSize().height));
		lastPartPositionButton = new JButton("Define Location");
		lastPartPositionButton.setMinimumSize(new Dimension(BUTTON_WIDTH, lastPartPositionButton.getPreferredSize().height));
		lastPartPositionButton.setPreferredSize(new Dimension(BUTTON_WIDTH, lastPartPositionButton.getPreferredSize().height));
		lastPartPositionButton.setMaximumSize(new Dimension(BUTTON_WIDTH, lastPartPositionButton.getPreferredSize().height));
		lastPartPositionDescription = new JLabel(LAST_PART_DESCRIPTION);
		lastPartPositionDescription.setFont(new Font("Verdana", Font.ITALIC, 12));
		lastPartPositionPanel.add(lastPartPositionLabel);
		lastPartPositionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		lastPartPositionPanel.add(lastPartPositionButton);
		lastPartPositionPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		try {
			lastPartStatusImage = ImageIO.read(getClass().getResource("/images/red-circle-20x20.png"));
			lastPartStatusLabel = new JLabel(new ImageIcon(lastPartStatusImage));
		} catch (IOException e) {
			lastPartStatusLabel = new JLabel("");
			e.printStackTrace();
		}
		lastPartPositionPanel.add(lastPartStatusLabel);
		lastPartPositionPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		lastPartPositionPanel.add(lastPartPositionDescription);
		//Part count
		partCountPanel = new JPanel();
		partCountPanel.setLayout(new BoxLayout(partCountPanel, BoxLayout.X_AXIS));
		partCountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		partCountPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		partCountLabel = new JLabel("Part Count");
		partCountLabel.setMinimumSize(new Dimension(LABEL_WIDTH, partCountLabel.getPreferredSize().height));
		partCountLabel.setPreferredSize(new Dimension(LABEL_WIDTH, partCountLabel.getPreferredSize().height));
		partCountLabel.setMaximumSize(new Dimension(LABEL_WIDTH, partCountLabel.getPreferredSize().height));
		partCountComboBox = new JComboBox<String>(PartConstants.getPartsCounter(20));
		partCountComboBox.setPreferredSize(new Dimension(COMBO_BOX_WIDTH, partCountComboBox.getPreferredSize().height));
		partCountComboBox.setMaximumSize(new Dimension(COMBO_BOX_WIDTH, partCountComboBox.getPreferredSize().height));
		partCountComboBox.setMinimumSize(new Dimension(COMBO_BOX_WIDTH, partCountComboBox.getPreferredSize().height));
		partCountComboBox.setSelectedIndex(0);
		partCountDescription = new JLabel(PART_COUNT_DESCRIPTION);
		partCountDescription.setFont(new Font("Verdana", Font.ITALIC, 12));
		partCountPanel.add(partCountLabel);
		partCountPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		partCountPanel.add(partCountComboBox);
		partCountPanel.add(Box.createRigidArea(new Dimension(30, 0)));
		partCountPanel.add(partCountDescription);
		//save part panel
		savePartPanel = new JPanel();
		savePartPanel.setLayout(new BoxLayout(savePartPanel, BoxLayout.X_AXIS));
		savePartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		savePartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		addPartButton = new JButton("Add Part");
		savePartButton = new JButton("Save Changes");
		savePartButton.setEnabled(false);
		savePartErrorLabel = new JLabel("");
		savePartErrorLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		//savePartButton.setEnabled(false);
		savePartPanel.add(addPartButton);
		savePartPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		savePartPanel.add(savePartButton);
		savePartPanel.add(Box.createRigidArea(new Dimension(5,0)));
		savePartPanel.add(savePartErrorLabel);
		//add all views to this JPanel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(footprintSelectionPanel);
		this.add(componentValueSelectionPanel);
		this.add(tapeCavityPitchPanel);
		this.add(firstPartPositionPanel);
		this.add(lastPartPositionPanel);
		this.add(partCountPanel);
		this.add(savePartPanel);
	}

	/**
	 * Update error message feedback with new error
	 * @param String error
	 */
	public void setNewPartErrorMessage(String error){
		savePartErrorLabel.setText(error);
	}
	
	/**
	 * Set first part button definition status to positive, display green check mark
	 */
	public void setFirstPartDefinitionStatusPositive(){
		try {
			firstPartStatusImage = ImageIO.read(getClass().getResource("/images/green-check-20x20.png"));
			firstPartStatusLabel.setIcon(new ImageIcon(firstPartStatusImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Set first part button definition status to negative, display red circle with cross
	 */
	public void setFirstPartDefinitionStatusNegative(){
		try {
			firstPartStatusImage = ImageIO.read(getClass().getResource("/images/red-circle-20x20.png"));
			firstPartStatusLabel.setIcon(new ImageIcon(firstPartStatusImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set last button definition status to positive, display green check mark
	 */
	public void setLastPartDefinitionStatusPositive(){
		try {
			lastPartStatusImage = ImageIO.read(getClass().getResource("/images/green-check-20x20.png"));
			lastPartStatusLabel.setIcon(new ImageIcon(lastPartStatusImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set last button definition status to negative, display red circle cross
	 */
	public void setLastPartDefinitionStatusNegative(){
		try {
			lastPartStatusImage = ImageIO.read(getClass().getResource("/images/red-circle-20x20.png"));
			lastPartStatusLabel.setIcon(new ImageIcon(lastPartStatusImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return current selected footprint from JComboBox
	 * @return String selected footprint
	 */
	public String getFootprint(){
		return (String) footprintSelectionComboBox.getSelectedItem();
	}
	
	/**
	 * Set selected footprint value
	 * If value is not an option, no changes will be made
	 * @param String footprint value being set
	 */
	public void setFootprint(String footprint){
		footprintSelectionComboBox.setSelectedItem(footprint);
	}

	/**
	 * Return value displayed in componentValue text input field
	 * @return String componentValue
	 */
	public String getComponentValue(){
		return componentValueTextField.getText();
	}
	
	/**
	 * Set text field for component value to assigned value
	 * @param String value
	 */
	public void setComponentValue(String value){
		componentValueTextField.setText(value);
	}
	
	/**
	 * Get Cavity Pitch
	 * @return double cavityPitch
	 */
	public String getCavityPitch(){
		return (String) tapeCavityPitchComboBox.getSelectedItem();
	}
	
	/**
	 * Set the cavity pitch of current part tape
	 * @param pitch
	 */
	public void setCavityPitch(String pitch){
		tapeCavityPitchComboBox.setSelectedItem(pitch);
	}
	
	/**
	 * Return value of x initial, the x coordinate of 1st part in row
	 * @return double xInitial
	 */
	public double getXinitial(){
		return xInitial;
	}
	
	/**
	 * Set value of x initial, the x coordinate of 1st part in row
	 * @param double x
	 */
	public void setXinitial(double x){
		xInitial = x;
	}
	
	/**
	 * Return value of y initial, the y coordinate of 1st part in row
	 * @return double yInitial
	 */
	public double getYinitial(){
		return yInitial;
	}
	
	/**
	 * Set value of y initial, the y coordinate of 1st part in row
	 * @param double y
	 */
	public void setYinitial(double y){
		yInitial = y;
	}
	
	/**
	 * Return value of x final, the x coordinate of last part in row
	 * @return double Xfinal
	 */
	public double getXfinal(){
		return xFinal;
	}
	
	/**
	 * Set value of x final, the x coordinate of last part in row
	 * @param double x
	 */
	public void setXfinal(double x){
		xFinal = x;
	}
	
	/**
	 * Return value of y final, the y coordinate of last part in row
	 * @return double yfinal
	 */
	public double getYfinal(){
		return yFinal;
	}
	
	/**
	 * Set value of y final, the y coordinate of last part in row
	 * @param double y
	 */
	public void setYfinal(double y){
		yFinal = y;
	}
	
	/**
	 * Returns total number of parts in this row of parts
	 * @return int partCount = total number of parts in row
	 */
	public String getPartCount(){
		return (String) partCountComboBox.getSelectedItem();
	}
	
	/**
	 * Set the total number of parts
	 * If number provided is not an option, no changes are made
	 * @param String count = total number of parts in current row
	 */
	public void setPartCount(String count){
		partCountComboBox.setSelectedItem(count);
	}
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */	
	//footprint selection options
	private JPanel footprintSelectionPanel;
	private JLabel footprintSelectionLabel;
	private JComboBox<String> footprintSelectionComboBox;
	//set component value 
	private JPanel componentValueSelectionPanel;
	private JLabel componentValueSelectionLabel;
	private JTextField componentValueTextField;
	private JLabel componentValueDescription;
	private String VALUE_DESCRIPTION = "Ex) 10u, 100R, 100kR";
	//set cavity pitch of component tape
	private JPanel tapeCavityPitchPanel;
	private JLabel tapeCavityPitchLabel;
	private JComboBox<String> tapeCavityPitchComboBox;
	private JLabel tapeCavityPitchDescription;
	private String CAVITY_PITCH_DESCRIPTION = "(mm) Verify pitch with tape data sheet";
	//define first component position
	private JPanel firstPartPositionPanel;
	private JLabel firstPartPositionLabel;
	public JButton firstPartPositionButton;
	private Image firstPartStatusImage;
	private JLabel firstPartStatusLabel;
	private JLabel firstPartPositionDescription;
	private String FIRST_PART_DESCRIPTION = "Jog to first, click to define location of first part.";
	//define last component position
	private JPanel lastPartPositionPanel;
	private JLabel lastPartPositionLabel;
	public JButton lastPartPositionButton;
	private Image lastPartStatusImage;
	private JLabel lastPartStatusLabel;
	private JLabel lastPartPositionDescription;
	private String LAST_PART_DESCRIPTION = "Jog to last part, click to define location of last part.";
	//total number of parts in row
	private JPanel partCountPanel;
	private JLabel partCountLabel;
	private JComboBox<String> partCountComboBox;
	private JLabel partCountDescription;
	private String PART_COUNT_DESCRIPTION = "Number or components on tape between first and last part";
	//save/import/export buttons
	private JPanel savePartPanel;
	public JButton addPartButton;
	public JButton savePartButton;
	private JLabel savePartErrorLabel;
	//label width defined to maintain alignment
	private final int LABEL_WIDTH = 100;
	private final int TEXT_FIELD_WIDTH = 130;
	private final int COMBO_BOX_WIDTH = 130;
	private final int BUTTON_WIDTH = 130;
	//variables to store coordinates of first and last parts
	private double xInitial;
	private double yInitial;
	private double xFinal;
	private double yFinal;
}
