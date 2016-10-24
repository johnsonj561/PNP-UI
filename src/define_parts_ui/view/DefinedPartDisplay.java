package define_parts_ui.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel that lists a defined part's features horizontally and provides buttons to delete/edit
 * Features listed includes: Footprint, Value, Location of 1st Part (X,Y)
 * @author Justin Johnson
 *
 */
public class DefinedPartDisplay extends JPanel{

	/**
	 * Constructs a view that displays a part's features horizontally
	 * @param label String that defines the part # (ex - "Part 1:")
	 * 
	 */
	public DefinedPartDisplay(int partNumber, String footprint, String value) {
		this.partID = partNumber;
		this.label= "Part " + partNumber + ":";
		this.footprint = footprint;
		this.value = value;
		initUI();
	}
	
	public void updatePartDisplay(String footprint, String value){
		this.footprint = footprint;
		this.value = value;
		footprintValue.setText(footprint);
		componentValue.setText(value);
	}

	/**
	 * Initialize UI elements
	 */
	private void initUI(){
		//Part Label Panel
		partLabelPanel = new JPanel();
		partLabelPanel.setLayout(new BoxLayout(partLabelPanel, BoxLayout.X_AXIS));
		partLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		partLabelPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		partLabel = new JLabel(label);
		//we set min/max sizes to force alignment
		partLabel.setMinimumSize(new Dimension(LABEL_WIDTH, partLabel.getPreferredSize().height));
		partLabel.setPreferredSize(new Dimension(LABEL_WIDTH, partLabel.getPreferredSize().height));
		partLabel.setMaximumSize(new Dimension(LABEL_WIDTH, partLabel.getPreferredSize().height));
		partLabelPanel.add(partLabel);
		//Feature 1 - Footprint Panel
		footprintPanel = new JPanel();
		footprintPanel.setLayout(new BoxLayout(footprintPanel, BoxLayout.X_AXIS));
		footprintPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		footprintPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		footprintLabel = new JLabel("Footprint:");
		footprintValue = new JLabel(footprint);
		footprintValue.setFont(new Font("Verdana", Font.ITALIC, 12));
		footprintValue.setMinimumSize(new Dimension(LABEL_WIDTH, footprintValue.getPreferredSize().height));
		footprintValue.setPreferredSize(new Dimension(LABEL_WIDTH, footprintValue.getPreferredSize().height));
		footprintValue.setMaximumSize(new Dimension(LABEL_WIDTH, footprintValue.getPreferredSize().height));
		footprintPanel.add(footprintLabel);
		footprintPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		footprintPanel.add(footprintValue);
		//Feature 2 - Value of component
		valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.X_AXIS));
		valuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		valuePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		valueLabel = new JLabel("Value:");
		componentValue = new JLabel(value);
		componentValue.setFont(new Font("Verdana", Font.ITALIC, 12));
		componentValue.setMinimumSize(new Dimension(LABEL_WIDTH, componentValue.getPreferredSize().height));
		componentValue.setPreferredSize(new Dimension(LABEL_WIDTH, componentValue.getPreferredSize().height));
		componentValue.setMaximumSize(new Dimension(LABEL_WIDTH, componentValue.getPreferredSize().height));
		valuePanel.add(valueLabel);
		valuePanel.add(Box.createRigidArea(new Dimension(10, 0)));
		valuePanel.add(componentValue);
		//button panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		editButton = new JButton("Edit");
		deleteButton = new JButton("Delete");
		buttonPanel.add(editButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(deleteButton);
		//add components to this JPanel view
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(partLabelPanel);
		this.add(footprintPanel);
		this.add(Box.createRigidArea(new Dimension(20, 0)));
		this.add(valuePanel);
		this.add(Box.createRigidArea(new Dimension(20, 0)));
		this.add(buttonPanel);
	}
	
	/**
	 * Return part number as it appears in list
	 * @return int partNumber
	 */
	public int getPartNumber(){
		return partID;
	}
	
	public void print(){
		System.out.println("Part ID " + partID + ": " + footprint + "\t" + value);
	}
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */	
	//part title label
	private String label;
	private JPanel partLabelPanel;
	private int partID;
	private JLabel partLabel;
	//footprint label and value
	private String footprint;
	private JPanel footprintPanel;
	private JLabel footprintLabel;
	private JLabel footprintValue;
	//value label and value
	private String value;
	private JPanel valuePanel;
	private JLabel valueLabel;
	private JLabel componentValue;
	//button panel for editing/deleting part
	private JPanel buttonPanel;
	public JButton editButton;
	public JButton deleteButton;
	//set label width
	private final int LABEL_WIDTH = 50;
}
