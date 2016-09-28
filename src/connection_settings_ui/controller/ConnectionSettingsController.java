package connection_settings_ui.controller;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import manual_control_ui.view.EmergencyStopView;

import jssc.SerialPortException;
import connection_settings_ui.model.ConnectionSettingsModel;
import connection_settings_ui.view.ManageConnectionButtonsView;
import connection_settings_ui.view.SettingsInputTextView;
import connection_settings_ui.view.SettingsPortSelectionView;
import connection_settings_ui.view.SettingsTitleView;

/**
 * Connection Settings Panel displays all PNP Connection settings and status to user
 * @author Justin Johnson
 *
 */
public class ConnectionSettingsController extends JPanel{

	/**
	 * Constructor builds all PNP Connection Setting views
	 * @throws SerialPortException 
	 */
	public ConnectionSettingsController() throws SerialPortException{
		//initialize settings to default values
		//initialize UI
		initUI();
	}

	/**
	 * Initialize Connection Settings Panel UI Elements
	 * Includes: Workspace dimensions, Connection Settings, Connect/Disconnect Buttons
	 * @throws SerialPortException 
	 */
	private void initUI(){
		//Dimensions Title
		dimensionsTitle = new SettingsTitleView("Workspace Dimensions");
		dimensionsTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//width input
		platformWidthInput = new SettingsInputTextView("Width", "Width (X) of machine workspace (mm)");
		platformWidthInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		platformWidthInput.setInputText(ConnectionSettingsModel.DEF_WIDTH + "");
		//depth input
		platformDepthInput = new SettingsInputTextView("Depth", "Depth (Y) of machine workspace (mm)");
		platformDepthInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		platformDepthInput.setInputText(ConnectionSettingsModel.DEF_DEPTH + "");
		//height input
		platformHeightInput = new SettingsInputTextView("Height", "Height (Z) of machine workspace (mm)");
		platformHeightInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		platformHeightInput.setInputText(ConnectionSettingsModel.DEF_HEIGHT + "");
		//Connections Settings Title
		connectionSettingsTitle = new SettingsTitleView("Connection Settings");
		connectionSettingsTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//baud rate
		baudRateInput = new SettingsInputTextView("Baud Rate", "Communication Speed (bps) typically 57600, 9600, or 115200");
		baudRateInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		baudRateInput.setInputText(ConnectionSettingsModel.DEF_BAUD + "");
		//feed rate
		feedRateInput = new SettingsInputTextView("Feed Rate", "Required for Jog controls (mm/min)");
		feedRateInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		feedRateInput.setInputText(ConnectionSettingsModel.DEF_FEED_RATE + "");
		//port selection
		portSelectionInput = new SettingsPortSelectionView();
		portSelectionInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);



		//Connection Status Title
		connectionStatusTitle = new SettingsTitleView("Connection Status");
		connectionStatusTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//connection buttons
		connectionButtons = new ManageConnectionButtonsView("Disconnected From PNP Machine: Connect to a device to begin");
		connectionButtons.setAlignmentX(JPanel.LEFT_ALIGNMENT);		
		//create panel to package Connection Status and Connection Buttons
		connectionButtonPanel = new JPanel();
		connectionButtonPanel.setLayout(new BoxLayout(connectionButtonPanel, BoxLayout.Y_AXIS));
		connectionButtonPanel.add(connectionStatusTitle);
		connectionButtonPanel.add(connectionButtons);
	
		//emergency stop panel
		emergencyStopPanel = new EmergencyStopView();
		emergencyStopPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		//create panel to package connection status/buttons and emergency stop button
		connectionButtonAndEmergencyStopPanel = new JPanel();
		connectionButtonAndEmergencyStopPanel.setLayout(new BoxLayout(connectionButtonAndEmergencyStopPanel, BoxLayout.X_AXIS));
		connectionButtonAndEmergencyStopPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		connectionButtonAndEmergencyStopPanel.add(connectionButtonPanel);
		connectionButtonAndEmergencyStopPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		connectionButtonAndEmergencyStopPanel.add(emergencyStopPanel);

		//Add individual components to this.JPanel for final display, laid on vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		this.add(dimensionsTitle);
		this.add(platformWidthInput);
		this.add(platformDepthInput);
		this.add(platformHeightInput);
		this.add(connectionSettingsTitle);
		this.add(baudRateInput);
		this.add(feedRateInput);
		this.add(portSelectionInput);
		this.add(connectionButtonAndEmergencyStopPanel);
	}

	/**
	 * Return workspace width defined by user
	 * @return String workspace width (X)
	 */
	public int getPlatformWidth(){
		return platformWidthInput.getInputAsInt();
	}

	/**
	 * Update input field with new width
	 * @param width
	 */
	public void setPlatformWidth(int width){
		platformWidthInput.setInputText(width + "");
	}

	/**
	 * Return workspace depth defined by user
	 * @return String workspace depth (Y)
	 */
	public int getPlatformDepth(){
		return platformDepthInput.getInputAsInt();
	}

	/**
	 * Update input field with new depth
	 * @param depth
	 */
	public void setPlatformDepth(int depth){
		platformDepthInput.setInputText(depth + "");
	}

	/**
	 * Return workspace height defined by user
	 * @return String workspace height (Z)
	 */
	public int getPlatformHeight(){
		return platformHeightInput.getInputAsInt();
	}

	/**
	 * Update input field with new height
	 * @param width
	 */
	public void setPlatformHeight(int height){
		platformHeightInput.setInputText(height + "");
	}

	/**
	 * Return Baud rate defined by user
	 * @return String baud rate
	 */
	public int getBaudRate(){
		return baudRateInput.getInputAsInt();
	}

	/**
	 * Update input field with new baud rate
	 * @param baudRate
	 */
	public void setBaudRate(int baudRate){
		baudRateInput.setInputText(baudRate + "");
	}

	/**
	 * Return Feed Rate defined by user
	 * @return String feed rate
	 */
	public int getFeedRate(){
		return feedRateInput.getInputAsInt();
	}

	/**
	 * Update input field with new feedrate
	 * @param feedrate
	 */
	public void setFeedRate(int feedrate){
		feedRateInput.setInputText(feedrate + "");
	}

	/**
	 * Update connection status to be displayed to user
	 * @param status
	 */
	public void setConnectionStatus(String status){
		connectionButtons.updateConnectionStatus(status);
	}

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;



	/**
	 * Class variables
	 */
	private SettingsTitleView dimensionsTitle;
	private SettingsInputTextView platformWidthInput;
	private SettingsInputTextView platformDepthInput;
	private SettingsInputTextView platformHeightInput;
	private SettingsTitleView connectionSettingsTitle;
	private SettingsTitleView connectionStatusTitle;
	private SettingsInputTextView baudRateInput;
	private SettingsInputTextView feedRateInput;
	public EmergencyStopView emergencyStopPanel;
	private JPanel connectionButtonPanel;
	private JPanel connectionButtonAndEmergencyStopPanel;
	public SettingsPortSelectionView portSelectionInput;
	public ManageConnectionButtonsView connectionButtons;
}
