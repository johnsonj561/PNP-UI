package manual_control_ui.view;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * HomeButtonView displays PNP Machine's Home buttons for X, Y, and Z axis 
 * @author Justin Johnson
 */
public class HomeButtonView extends JPanel{

	/**
	 * HomeButtonView displays Homing buttons for PNP machine along X, Y, and Z axis
	 */
	public HomeButtonView() {
		initUI();
	}

	/**
	 * Initialize Home Button elements
	 */
	private void initUI(){
		//Home button panel
		homeButtonPanel = new JPanel();
		homeButtonPanel.setLayout(new BoxLayout(homeButtonPanel, BoxLayout.X_AXIS));
		homeButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		homeXButton = new JButton("X Home");
		homeYButton = new JButton("Y Home");
		lightBoxButton = new JButton("Light Box");
		
		homeAllButton = new JButton("Home All");
		homeButtonPanel.add(homeAllButton);
		homeButtonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		homeButtonPanel.add(homeXButton);
		homeButtonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		homeButtonPanel.add(homeYButton);
		homeButtonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		homeButtonPanel.add(lightBoxButton);
		
		//add elements to this layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(homeButtonPanel);
	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Variables
	 */
	private JPanel homeButtonPanel;
	public JButton homeXButton;
	public JButton homeYButton;
	public JButton homeAllButton;
	public JButton lightBoxButton;
}
