package pnp_main_controller;
import g_code_generator_ui.controller.GCodeGeneratorController;
import import_g_code_ui.controller.UploadGCodeController;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import manual_control_ui.controller.ManualController;
import connection_settings_ui.controller.ConnectionSettingsController;
import connection_settings_ui.model.ConnectionSettingsModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jssc.SerialPortException;
import jssc_usb.UsbDevice;

/**
 * Main Controller for Pic N Place UI application.
 * Displays Tabbed Interfaced that allows user to select between PNP Control Views:
 * 1) Connect To Device - displays connection settings and status
 * 2) Generate G Code - converts PCB Design centroid files to G Code Instructions
 * 3) Upload To PNP - parses file of G Code Instructions and sends to PNP's embedded system
 * 4) Manual Control - manually control PNP Machine through Jog controls and Single Line Entry point
 * 5) Create Parts File - create parts file that specifies available parts and their position on placement platform
 * @author Justin Johnson
 *
 */
public class PNPMainController extends JPanel {

	/**
	 * Constructor configures JTabbedPane to display all PNP Control Panels to user 
	 */
	public PNPMainController() {
		super(new GridLayout(1, 1));
		//we initialize machine to 0, 0, 0
		this.currentX = 0;
		this.currentY = 0;
		this.currentZ = 0;
		//initialize connection settings to default values
		connectionSettings = new ConnectionSettingsModel();
		//initialize UI elements and Button functionality
		initUI();
		initButtons();
	}

	/**
	 * Initialize PNP Main UI Controller's Components
	 */
	private void initUI(){
		JTabbedPane tabbedPane = new JTabbedPane();
		//Connection settings
		try {
			connectionSettingsController = new ConnectionSettingsController();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tabbedPane.addTab("Connect Settings", null, connectionSettingsController, 
				"Configure connection settings and connect to a PNP device");
		//G Code Generation
		gCodeGeneratorPanel = new GCodeGeneratorController();
		tabbedPane.addTab("Generate G Code", null, gCodeGeneratorPanel, 
				"Upload PCB design files and generate G Code Instructions");
		//G Code Upload To Machine
		importGCodePanel = new UploadGCodeController();
		tabbedPane.addTab("Upload To PNP", null, importGCodePanel, 
				"Upload entire projects to PNP machine for processing");
		//Manual PNP Control
		manualController = new ManualController();
		tabbedPane.addTab("Manual PNP Control", null, manualController, "Manual PNP Control");
		//Create Parts Specifications
		JComponent panel4 = makeTextPanel("Coming Soon:\nCreate A Parts File");
		tabbedPane.addTab("Define Parts", null, panel4, "Define Parts");
		//add tabs to JPanel
		add(tabbedPane);
	}

	/**
	 * Initialize all button functionality
	 */
	private void initButtons(){
		//connect to device button
		connectionSettingsController.connectionButtons.connectToDeviceButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String buttonText = connectionSettingsController.connectionButtons.connectToDeviceButton.getText();
						if(buttonText.equalsIgnoreCase("connect to device")){
							connectUSB();
						}
						else{
							disconnectUSB();
						}
					}
				});
		//save settings button stores user input values to ConnectionSettingsModel object
		connectionSettingsController.connectionButtons.saveSettingsButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						connectionSettings.setBaudRate(connectionSettingsController.getBaudRate());
						connectionSettings.setWidth(connectionSettingsController.getPlatformWidth());
						connectionSettings.setDepth(connectionSettingsController.getPlatformDepth());
						connectionSettings.setHeight(connectionSettingsController.getPlatformHeight());
						connectionSettings.setStepSize(manualController.getStepSize());
						if(usbDevice != null && usbDevice.isOpen()){
							connectionSettings.setConnected(true);
						}
						else{
							connectionSettings.setConnected(false);
						}
					}
				});
		//restore default values from our ConnectionSettingsModel
		connectionSettingsController.connectionButtons.restoreDefaultsButton.addActionListener(
				new ActionListener() {
					@Override			
					public void actionPerformed(ActionEvent e) {
						restoreDefaults();

					}
				});
		//jog x plus button
		manualController.jogButtonView.jogXPlusButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						jogXPlus();
					}
				});
		//jog x minus button
		manualController.jogButtonView.jogXMinusButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						jogXMinus();
					}
				});
		//jog y plus button
		manualController.jogButtonView.jogYPlusButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						jogYPlus();
					}
				});
		//jog y minus button
		manualController.jogButtonView.jogYMinusButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						jogYMinus();
					}
				});
		//jog z plus button
		manualController.jogButtonView.jogZPlusButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						jogZPlus();
					}
				});	
		//jog z minus button
		manualController.jogButtonView.jogZMinusButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						jogZMinus();
					}
				});	
		//manual send line to machine
		manualController.manualInstructionView.sendInstructionButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(manualController.getManualInstruction());
					}
				});
		//home x moves PNP to defined home on X
		manualController.homeButtonView.homeXButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(HOME_X);
					}
				});
		//home y moves PNP to defined home on Y
		manualController.homeButtonView.homeYButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(HOME_Y);
					}
				});
		//home z moves PNP to defined home on Z
		manualController.homeButtonView.homeZButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(HOME_Z);
					}
				});
		//zero X moves PNP to X0
		manualController.homeButtonView.zeroXButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(ZERO_X);
					}
				});
		//zero Y moves PNP to Y0
		manualController.homeButtonView.zeroYButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(ZERO_Y);
					}
				});
		//zero Z moves PNP to Z0
		manualController.homeButtonView.zeroZButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(ZERO_Z);
					}
				});


	}

	/**
	 * Helper method creates a JPanel with label
	 * @param text displayed on JPabel
	 * @return JPanel object containing text
	 */
	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	/**
	 * Create the UI, set preferred size, pack components, and display
	 * For thread safety, this method should be invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Pic N Place");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//set preferred size, min size, and window start up position
		frame.setPreferredSize(new Dimension(850, 700));
		frame.setMinimumSize(new Dimension(700, 600));
		//create TabbedController object and add to frame
		//Note - TabbedController handles the initialization of individual views/panels
		frame.add(new PNPMainController(), BorderLayout.CENTER);
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Main schedules job for event dispatch thread and displays UI
	 * @param args
	 */
	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Connect to USB device defined by Port selected in Connection Settings Panel 
	 * @return true if connection was successful
	 */
	private boolean connectUSB(){
		//initializing UsbDevice connects device and opens port at constructor
		usbDevice = new UsbDevice(connectionSettingsController.portSelectionInput.getSelectedPort(),
				connectionSettings.getBaudRate());
		//we test that device is open, and if it is not we attempt to open
		if(!usbDevice.isOpen()){
			if(usbDevice.openPort()){
				System.out.println("PNPMainController:\nconnectUSB(): Port " + 
						usbDevice.getPortName() + " opened with no error");
				connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Disconnect");
				updateConnectionStatusMessage(STATUS_CONNECTED);
				return true;
			}
			else{
				System.out.println("PNPMainController:\nconnectUSB(): Port " + 
						usbDevice.getPortName() + " unable to open");
				updateConnectionStatusMessage(STATUS_CONNECT_ERROR);
				return true;
			}
		}
		else{
			System.out.println("PNPMainController:\nconnectUsb(): Usb device connected: " + 
					usbDevice.getPortName());
			connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Disconnect");
			updateConnectionStatusMessage(STATUS_CONNECTED);

			return true;
		}
	}

	/**
	 * Closes port connection defined by UsbDevice
	 * @return true if port closes without error
	 */
	private boolean disconnectUSB(){
		if(usbDevice.isOpen()){
			if(usbDevice.close()){
				System.out.println("PNPMainController:\ndisconnectUSB(): Port " + usbDevice.getPortName() +
						" closed without error");
				connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Connect To Device");
				updateConnectionStatusMessage(STATUS_DISCONNECTED);
				return true;
			}
			else{
				System.out.println("PNPMainController:\ndisconnectUSB(): Port " + usbDevice.getPortName() +
						" was not able to be closed");
				updateConnectionStatusMessage(STATUS_DISCONNECT_ERROR);
				return false;
			}
		}
		System.out.println("PNPMainController:\ndisconnectUSB(): Port " + usbDevice.getPortName() +
				" was already closed, no changes made");
		connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Connect To Device");
		updateConnectionStatusMessage(STATUS_DISCONNECTED);
		return false;
	}

	/**
	 * Update connection status message displayed to user on ConnectionSettingsController
	 * @param connectionStatus
	 */
	private void updateConnectionStatusMessage(int connectionStatus){
		switch(connectionStatus){
		case STATUS_DISCONNECTED:
			connectionSettingsController.setConnectionStatus("Disconnected From PNP Machine: Connect to a device to begin");
			break;
		case STATUS_CONNECTED:
			connectionSettingsController.setConnectionStatus("Currently Connected on Port: " + 
					usbDevice.getPortName());
			break;
		case STATUS_SEND_ERROR:
			connectionSettingsController.setConnectionStatus("An error occurred while sending last message");
			break;
		case STATUS_CONNECT_ERROR:
			connectionSettingsController.setConnectionStatus("An error occurred while connecting to port: " +
					usbDevice.getPortName() + ", double check connection settings");
			break;
		case STATUS_DISCONNECT_ERROR:
			connectionSettingsController.setConnectionStatus("An error occurred while disconnecting from port: " +
					usbDevice.getPortName());
			break;
		default:
			connectionSettingsController.setConnectionStatus("");
		}
	}

	/**
	 * Send message to USB device currently connected
	 * @param message String to be sent to USB Devvice
	 * @return true if message sent with out error
	 */
	private boolean sendMessage(String message){
		if(usbDevice != null){
			if(usbDevice.writeMessage(message)){
				System.out.println("PNPMainController:\nsendMessage(String message): Message sent with out error " +
						message);
				return true;
			}
			else{
				return false;
			}
		}
		else{
			System.out.println("PNPMainController:\nsendMessage(String message): UsbDevice not initialized");
			System.out.println("Message not sent");
			return false;
		}	
	}

	private void restoreDefaults(){
		//update our settings data model with defaults
		connectionSettings.restoreDefaultValues();
		//then update UI input fields with default values
		connectionSettingsController.setPlatformDepth(connectionSettings.getDepth());
		connectionSettingsController.setPlatformWidth(connectionSettings.getWidth());
		connectionSettingsController.setPlatformHeight(connectionSettings.getHeight());
		connectionSettingsController.setFeedRate(connectionSettings.getFeedRate());
		connectionSettingsController.setBaudRate(connectionSettings.getBaudRate());
		manualController.setStepSize(connectionSettings.getStepSize());
	}

	/**
	 * Jog machine in positive Y direction
	 */
	private void jogYPlus(){
		currentY += manualController.getStepSize();
		if(currentY <= connectionSettings.getDepth() && currentY >= 0){
			if(sendMessage("G1 Y" + currentY + "\n")){
				System.out.println("PNPMainController:\nJog Y Plus Button: " + 
						"Jog Y To " + currentY);
				return;
			}
			else{
				System.out.println("PNPMainController:\nJog Y Plus Button: Message not sent");
			}
		}
		else{
			System.out.println("PNPMainController:\nJog Y Plus Button: " +
					" Unable to jog, check workspace dimensions");
		}
		//UNDO jog calculation because message was not sent
		currentY -= manualController.getStepSize();
	}

	/**
	 * Jog machine in negative Y direction
	 */
	private void jogYMinus(){
		currentY -= manualController.getStepSize();
		if(currentY <= connectionSettings.getDepth() && currentY >= 0){
			if(sendMessage("G1 Y" + currentY + "\n")){
				System.out.println("PNPMainController:\nJog Y Minus Button: " + 
						"Jog Y To " + currentY);
				return;
			}
			else{
				System.out.println("PNPMainController:\nJog Y Minus Button: Message not sent");
			}
		}
		else{
			System.out.println("PNPMainController:\nJog Y Minus Button: " +
					" Unable to jog, check workspace dimensions");
		}
		//UNDO jog calculation because message was not sent
		currentY += manualController.getStepSize();
	}

	/**
	 * Jog machine in positive X direction
	 */
	private void jogXPlus(){
		currentX += manualController.getStepSize();
		if(currentX <= connectionSettings.getWidth() && currentX >= 0){
			if(sendMessage("G1 X" + currentX + "\n")){
				System.out.println("PNPMainController:\nJog X Plus Button: " + 
						"Jog X To " + currentX);
				return;
			}
			else{
				System.out.println("PNPMainController:\nJog X Plus Button: Message not sent");
			}
		}
		else{
			System.out.println("PNPMainController:\nJog X Plus Button: " +
					" Unable to jog, check workspace dimensions");
		}
		//UNDO jog calculation because message was not sent
		currentX -= manualController.getStepSize();
	}

	/**
	 * Jog machine in negative X direction
	 */
	private void jogXMinus(){
		currentX -= manualController.getStepSize();
		if(currentX <= connectionSettings.getWidth() && currentX >= 0){
			if(sendMessage("G1 X" + currentX + "\n")){
				System.out.println("PNPMainController:\nJog X Minus Button: " + 
						"Jog X To " + currentX);
				return;
			}
			else{
				System.out.println("PNPMainController:\nJog X Minus Button: Message not sent");
			}
		}
		else{
			System.out.println("PNPMainController:\nJog X Minus Button: " +
					" Unable to jog, check workspace dimensions");
		}
		//UNDO jog calculation because message was not sent
		currentX += manualController.getStepSize();
	}

	/**
	 * Jog machine in positive Z direction
	 */
	private void jogZPlus(){
		currentZ += manualController.getStepSize();
		//TODO check workspace Z dimension and compatibility
		if(currentZ <= connectionSettings.getHeight() && currentZ >= 0){
			if(sendMessage("G1 Z" + currentZ + "\n")){
				System.out.println("PNPMainController:\nJog Z Plus Button: " + 
						"Jog Z To " + currentZ);
				return;
			}
			else{
				System.out.println("PNPMainController:\nJog Z Plus Button: Message not sent");
			}
		}
		else{
			System.out.println("PNPMainController:\nJog Z Plus Button: " +
					" Unable to jog, check workspace dimensions");
		}
		//UNDO jog calculation because message was not sent
		currentZ -= manualController.getStepSize();
	}

	/**
	 * Jog machine in negative Z direction
	 */
	private void jogZMinus(){
		currentZ -= manualController.getStepSize();
		//TODO check workspace Z dimension and compatibility
		if(currentZ <= connectionSettings.getHeight() && currentZ >= 0){
			if(sendMessage("G1 Z" + currentZ + "\n")){
				System.out.println("PNPMainController:\nJog Z Minus Button: " + 
						"Jog Z To " + currentZ);
				return;
			}
			else{
				System.out.println("PNPMainController:\nJog Z Minus Button: Message not sent");
			}
		}
		else{
			System.out.println("PNPMainController:\nJog Z Minus Button: " +
					" Unable to jog, check workspace dimensions");
		}
		//UNDO jog calculation because message was not sent
		currentZ += manualController.getStepSize();
	}

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	public ConnectionSettingsController connectionSettingsController;
	public GCodeGeneratorController gCodeGeneratorPanel;
	public UploadGCodeController importGCodePanel;
	public ManualController manualController;
	private UsbDevice usbDevice;
	private ConnectionSettingsModel connectionSettings;
	private double currentX;
	private double currentY;
	private double currentZ;
	private final int STATUS_CONNECTED = 1;
	private final int STATUS_DISCONNECTED = 2;
	private final int STATUS_CONNECT_ERROR = 3;
	private final int STATUS_DISCONNECT_ERROR = 4;
	private final int STATUS_SEND_ERROR = 5;
	private final String HOME_X = "G1 X0";
	private final String HOME_Y = "G1 Y0";
	private final String HOME_Z = "G1 Z0";
	private final String ZERO_X = "G1 X0";
	private final String ZERO_Y = "G1 Y0";
	private final String ZERO_Z = "G1 Z0";
}