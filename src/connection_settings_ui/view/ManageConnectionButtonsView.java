package connection_settings_ui.view;
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
 * ProcessJobPanel constructs a JPanel object that displays job status to user and
 * provides functionality to update status programatically 
 * @author Justin Johnson
 */
public class ManageConnectionButtonsView extends JPanel{

	/**
	 * Constructor assigns job status to JLabel and creates builds JPanel
	 * @param status
	 */
	public ManageConnectionButtonsView(String status) {
		this.status = status;
		initUI();
	}

	/**
	 * Initialize Job Process and Status UI Elements
	 */
	private void initUI(){
		//Button Panel to contain Connect/Disconnect and Restore Defaults Buttons
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		buttonsPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		connectToDeviceButton = new JButton("Connect To Device");
		restoreDefaultsButton = new JButton("Restore Defaults");
		buttonsPanel.add(connectToDeviceButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		buttonsPanel.add(restoreDefaultsButton);
		
		//description panel to display connection status
		connectionStatusPanel = new JPanel();
		connectionStatusPanel.setLayout(new BoxLayout(connectionStatusPanel, BoxLayout.X_AXIS));
		connectionStatusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		connectionStatusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		connectionStatusLabel = new JLabel(status);
		connectionStatusLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		connectionStatusPanel.add(connectionStatusLabel);
		
		//add components to this.JPanel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(connectionStatusPanel);
		this.add(buttonsPanel);
		
	}

	/**
	 * Updates connection status label
	 * @param path of the file selected by user
	 */
	public void updateConnectionStatus(String status){
		connectionStatusLabel.setText(status);
	}


	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Variables
	 */
	private JPanel	buttonsPanel;
	private JPanel connectionStatusPanel;
	public JButton connectToDeviceButton;
	public JButton restoreDefaultsButton;
	private String status;
	private JLabel connectionStatusLabel;
}
