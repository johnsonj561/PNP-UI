package connection_settings_ui.controller;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
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
	 */
	public ConnectionSettingsController(){
		//initialize settings to default values
		settingsData = new ConnectionSettingsModel();
		//initialize UI
		initUI();
	}

	/**
	 * Initialize Connection Settings Panel UI Elements
	 * Includes: Workspace dimensions, Connection Settings, Connect/Disconnect Buttons
	 */
	private void initUI(){
		//Dimensions Title
		dimensionsTitle = new SettingsTitleView("Workspace Dimensions");
		dimensionsTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//width input
		platformWidthInput = new SettingsInputTextView("Width", "Width (X) of machine workspace (mm)");
		platformWidthInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		platformWidthInput.setInputText(settingsData.getWidth() + "");
		//depth input
		platformDepthInput = new SettingsInputTextView("Depth", "Depth (Y) of machine workspace (mm)");
		platformDepthInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		platformDepthInput.setInputText(settingsData.getDepth() + "");
		//height input
		platformHeightInput = new SettingsInputTextView("Height", "Height (Z) of machine workspace (mm)");
		platformHeightInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		platformHeightInput.setInputText(settingsData.getHeight() + "");
		//Connections Settings Title
		connectionSettingsTitle = new SettingsTitleView("Connection Settings");
		connectionSettingsTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//baud rate
		baudRateInput = new SettingsInputTextView("Baud Rate", "Communication Speed (bps) typically 57600, 9600, or 115200");
		baudRateInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		baudRateInput.setInputText(settingsData.getBaudRate() + "");
		//feed rate
		feedRateInput = new SettingsInputTextView("Feed Rate", "Required for Jog controls (mm/min)");
		feedRateInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		feedRateInput.setInputText(settingsData.getFeedRate() + "");
		//port selection
		portSelectionInput = new SettingsPortSelectionView();
		portSelectionInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Connection Status Title
		connectionStatusTitle = new SettingsTitleView("Connection Status");
		connectionStatusTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//connection buttons
		connectionButtons = new ManageConnectionButtonsView("Disconnected from PNP machine. Click 'Connect To Device' to begin.");
		connectionButtons.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		
		//TODO add Button to connect device!
		
		
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
		this.add(connectionStatusTitle);
		this.add(connectionButtons);
	}

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class variables
	 */
	public ConnectionSettingsModel settingsData;
	private SettingsTitleView dimensionsTitle;
	private SettingsInputTextView platformWidthInput;
	private SettingsInputTextView platformDepthInput;
	private SettingsInputTextView platformHeightInput;
	private SettingsTitleView connectionSettingsTitle;
	private SettingsTitleView connectionStatusTitle;
	private SettingsInputTextView baudRateInput;
	private SettingsInputTextView feedRateInput;
	private SettingsPortSelectionView portSelectionInput;
	private ManageConnectionButtonsView connectionButtons;
}
