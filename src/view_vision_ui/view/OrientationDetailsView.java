package view_vision_ui.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel that displays Component Angle of Rotation and (X,Y) Positioning
 * @author Justin Johnson
 */
public class OrientationDetailsView extends JPanel{

	/**
	 * Constructor initializes view elements
	 */
	public OrientationDetailsView() {
		initUI();
	}

	/**
	 * Initialize UI Elements and add them to this JPanel
	 */
	private void initUI(){
		//Main panel to package all components
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		//labels for (X,Y) Coordinates
		
		positionTitle = new JLabel(POSITION_TITLE);
		positionLabel = new JLabel("(12.53, 24.92) mm");
		positionLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		//labels for rotation
		angleTitle = new JLabel(ROTATION_TITLE);
		angleLabel = new JLabel("-43.55 degrees");
		angleLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		//vision protocol status
		routineStatusTitle = new JLabel("Vision Routine Status");
		routineStatusDescription = new JLabel("Preparing to rotate component...");
		routineStatusDescription.setFont(new Font("Verdana", Font.ITALIC, 12));
		//add components to this.JPanel
		mainPanel.add(positionTitle);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(positionLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,25)));
		mainPanel.add(angleTitle);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(angleLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,25)));
		mainPanel.add(routineStatusTitle);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(routineStatusDescription);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(mainPanel);
	}
		
	
	/**
	 * Set value of component position (X,Y)
	 * @param String position
	 */
	public void setPositionLabel(String position){
		positionLabel.setText(position);
	}
	
	/**
	 * Set value of component angle of rotation
	 * @param String angle
	 */
	public void setAngleLabel(String angle){
		angleLabel.setText(angle);
	}
	
	/**
	 * Set description of vision routine
	 * @param String description
	 */
	public void setRoutineDescription(String description){
		routineStatusDescription.setText(description);
	}
	
	private JPanel mainPanel;
	private JLabel positionTitle;
	private JLabel positionLabel;
	private JLabel angleTitle;
	private JLabel angleLabel;
	private JLabel routineStatusTitle;
	private JLabel routineStatusDescription;
	private final String POSITION_TITLE = "Position (X, Y) mm";
	private final String ROTATION_TITLE = "Rotation (R) deg";
	/**
	 * Default Serial ID
	 */
	private static final long serialVersionUID = 1L;
}
