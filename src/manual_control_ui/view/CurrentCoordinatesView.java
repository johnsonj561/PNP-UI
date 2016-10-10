package manual_control_ui.view;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * CurrentCoordinatesView displays current X Y Z R to user
 * @author Justin Johnson
 */
public class CurrentCoordinatesView extends JPanel{

	/**
	 * CurrentCoordinatesView displays current X Y Z R to user
	 */
	public CurrentCoordinatesView() {
		initUI();
	}

	/**
	 * Initialize current coordinate display elements
	 */
	private void initUI(){
		//Home button panel
		currentCoordinatesPanel = new JPanel();
		currentCoordinatesPanel.setLayout(new BoxLayout(currentCoordinatesPanel, BoxLayout.X_AXIS));
		currentCoordinatesPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 3));
		//title
		title = new JLabel("Position");
		//X Value
		xCoordinateLabel = new JLabel("X: ");
		xCoordinateValue = new JLabel("0.00");
		xCoordinateValue.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		xCoordinateValue.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		xCoordinateValue.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		
		//Y Value
		yCoordinateLabel = new JLabel("Y: ");
		yCoordinateValue = new JLabel("0.00");
		yCoordinateValue.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		yCoordinateValue.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		yCoordinateValue.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		//Z Value
		zCoordinateLabel = new JLabel("Z: ");
		zCoordinateValue = new JLabel("0.00");
		zCoordinateValue.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		zCoordinateValue.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		zCoordinateValue.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		//R Value
		rCoordinateLabel = new JLabel("R: ");
		rCoordinateValue = new JLabel("0.00");
		rCoordinateValue.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		rCoordinateValue.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		rCoordinateValue.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		
		
		currentCoordinatesPanel.add(title);
		currentCoordinatesPanel.add(Box.createRigidArea(new Dimension(40, 0)));
		currentCoordinatesPanel.add(xCoordinateLabel);
		currentCoordinatesPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		currentCoordinatesPanel.add(xCoordinateValue);
		currentCoordinatesPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		currentCoordinatesPanel.add(yCoordinateLabel);
		currentCoordinatesPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		currentCoordinatesPanel.add(yCoordinateValue);
		currentCoordinatesPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		currentCoordinatesPanel.add(zCoordinateLabel);
		currentCoordinatesPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		currentCoordinatesPanel.add(zCoordinateValue);
		currentCoordinatesPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		currentCoordinatesPanel.add(rCoordinateLabel);
		currentCoordinatesPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		currentCoordinatesPanel.add(rCoordinateValue);
		//add elements to this layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(currentCoordinatesPanel);
	}

	/**
	 * Update X value text with String X
	 * @param x
	 */
	public void updateXValue(String x){
		xCoordinateValue.setText(x);
	}
	
	/**
	 * Update Y value text with String y
	 * @param y
	 */
	public void updateYValue(String y){
		yCoordinateValue.setText(y);
	}
	
	/**
	 * Update Z value text with String z
	 * @param z
	 */
	public void updateZValue(String z){
		zCoordinateValue.setText(z);
	}
	
	/**
	 * Update R value text with String r
	 * @param r
	 */
	public void updateRValue(String r){
		rCoordinateValue.setText(r);
	}
	
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Variables
	 */
	private JPanel currentCoordinatesPanel;
	private JLabel title;
	private JLabel xCoordinateLabel;
	private JLabel xCoordinateValue;
	private JLabel yCoordinateLabel;
	private JLabel yCoordinateValue;
	private JLabel zCoordinateLabel;
	private JLabel zCoordinateValue;
	private JLabel rCoordinateLabel;
	private JLabel rCoordinateValue;
	private final int LABEL_WIDTH = 50;
	private final int LABEL_HEIGHT = 10;
}

