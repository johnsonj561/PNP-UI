package pnp_main.controller;
import g_code_generator_ui.model.GCodeCommand;
import g_code_generator_ui.model.PICReturnValue;
import g_code_generator_ui.controller.GCodeGeneratorController;
import import_g_code_ui.controller.UploadGCodeController;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import orientation_detection.ComponentFinder;
import pnp_main.model.PNPConstants;
import manual_control_ui.controller.ManualController;
import manual_control_ui.view.JogSizeSlider;
import connection_settings_ui.controller.ConnectionSettingsController;
import connection_settings_ui.model.ConnectionSettingsModel;
import define_parts_ui.controller.DefinePartsController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jssc.SerialPortException;
import jssc_usb.UsbDevice;
import jssc_usb.UsbEvent;

/**
 * Main Controller for Pic N Place UI application.
 * Displays Tabbed Interfaced that allows user to select between PNP Control Views:
 * 1) Connect To Device - displays connection settings and status
 * 2) Generate G Code - converts PCB Design centroid files to G Code Instructions
 * 3) Upload To PNP - parses file of G Code Instructions and sends to PNP's embedded system
 * 4) Manual Control - manually control PNP Machine through Jog controls and Single Line Entry point
 * 5) Create Parts File - create parts file that specifies available parts and their position on placement platform
 * @author Justin Johnson 
 */
public class PNPMainController extends JPanel implements UsbEvent {

	/**
	 * Constructor configures JTabbedPane to display all PNP Control Panels to user 
	 */
	public PNPMainController() {
		super(new GridLayout(1, 1));
		//initialize connection settings to defaults defined in ConnectionsettingsModel
		connectionSettings = new ConnectionSettingsModel();
		//we initialize machine coordinates (X,Y,Z), rotation R, feed rate F
		this.currentX = 0;
		this.currentY = 0;
		this.currentZ = 0;
		this.currentR = 0;
		this.currentFeedRate = connectionSettings.getDefaultFeedRate();
		//not clear to send until connection is established
		CTS = false;
		//processingJob set to true only when uploading project file to PNP
		processingJob = false;
		vacuumOn = false;
		//initialize all views
		initUI();
		//initialize buttons
		initButtons();
		//set std output to log file
		defineLogFile();
		//System.setOut(pnpLog);
		//System.setErr(pnpLog);
		System.out.println("New PNPMainController()\n");

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
			System.out.println("PNPMainController: Error creating tabbed interface (ConnectionSettingsController)\n");
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
		definePartsController = new DefinePartsController();
		tabbedPane.addTab("Define Project Parts", null, definePartsController, "Define Footprints, Values, and Positioning");
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
						connectionSettings.setFeedRate(connectionSettingsController.getFeedRate());
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
		//Rotate vacuum head CW
		manualController.jogButtonView.spindleCWButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						spindleCW();
					}
				});
		//Rotate vacuum head CCW
		manualController.jogButtonView.spindleCCWButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						spindleCCW();
					}
				});
		//Toggle vacuum on/off
		manualController.jogButtonView.vacuumButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(vacuumOn){
							toggleVacuum(false);
						}
						else{
							toggleVacuum(true);
						}
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
		manualController.manualInstructionView.instructionInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(manualController.getManualInstruction());
			}
		});
		//home all button returns x-y-z-r to 0 position
		manualController.homeButtonView.homeAllButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(PNPConstants.HOME_ALL);
					}
				});
		//home x moves PNP to defined home on X
		manualController.homeButtonView.homeXButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(PNPConstants.HOME_X);
					}
				});
		//home y moves PNP to defined home on Y
		manualController.homeButtonView.homeYButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(PNPConstants.HOME_Y);
					}
				});
		//home z moves PNP to defined home on Z
		manualController.homeButtonView.homeZButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(PNPConstants.HOME_Z);
					}
				});
		//connection page emergency stop button
		manualController.jogButtonView.emergencyStopPanel.emergencyStopButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						emergencyStop();
					}
				});
		//manual control emergency stop button
		connectionSettingsController.emergencyStopPanel.emergencyStopButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						emergencyStop();
					}
				});
		//start job from the upload g code controller
		importGCodePanel.gCodeConsoleView.startJobButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(CTS){
							processGCodeFromConsole();
						}
						else{
							importGCodePanel.updateJobStatus("Unable To Start Job. Check Connection Status");
						}
					}
				});
		//handle pause Job button events from upload g code controller
		importGCodePanel.gCodeConsoleView.pauseJobButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						pauseJobRoutine();
					}
				});
		//handle terminate job button events from the upload g code controller
		importGCodePanel.gCodeConsoleView.stopJobButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						importGCodePanel.stopJobButtonStates();
						processingJob = false;
					}
				});
		//handle validate job button so that it traverses g code and checks for errors
		importGCodePanel.selectInputView.validateGCodeButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						importGCodePanel.validateGCode(connectionSettings.getWidth(), connectionSettings.getDepth(), 
								connectionSettings.getHeight());

					}
				});
		//firstPartPositionButton on definePartsController
		definePartsController.addNewPartsView.firstPartPositionButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						definePartsController.addNewPartsView.setXinitial(currentX);
						definePartsController.addNewPartsView.setYinitial(currentY);
						definePartsController.addNewPartsView.setFirstPartDefinitionStatusPositive();
					}
				});
		//lastPartPositionButton on definePartsController
		definePartsController.addNewPartsView.lastPartPositionButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						definePartsController.addNewPartsView.setXfinal(currentX);
						definePartsController.addNewPartsView.setYfinal(currentY);
						definePartsController.addNewPartsView.setLastPartDefinitionStatusPositive();
					}
				});
	}

	/**
	 * Create the UI, set preferred size, pack components, and display
	 * For thread safety, this method should be invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("PIC N Place");
		//ImageIcon img1 = new ImageIcon(res)
		ImageIcon img = new ImageIcon("pnp-logo-32x32.png");
		frame.setIconImage(img.getImage());
		frame.setLocation(PNPConstants.JFRAME_X_LOC, PNPConstants.JFRAME_Y_LOC);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//set preferred size, min size, and window start up position
		frame.setPreferredSize(new Dimension(PNPConstants.PREFERRED_WIDTH, PNPConstants.PREFERRED_HEIGHT));
		frame.setMinimumSize(new Dimension(PNPConstants.PREFERRED_WIDTH, PNPConstants.PREFERRED_HEIGHT));
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
	 * Create a log file to write std out and std err to
	 * PNP Application will generate PNPLog directory in C directory if it does not exist
	 * C:\PNPLog\MM.dd.yyyy-HH.mm.ss.txt
	 */
	private void defineLogFile(){
		//create directory for PNP log files if it does not exist
		File logDirectory = new File("C:/PNPLog");
		if(!logDirectory.exists()){
			logDirectory.mkdir();
		}
		//create text file with timestamp as name
		String logFile = new SimpleDateFormat("MM.dd.yyyy-HH.mm.ss").format(new Date());
		File file = new File("C:/PNPLog/" + logFile + ".txt");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				updateConnectionStatusMessage(PNPConstants.STATUS_LOG_FILE_ERROR);
			}
		}
		try {
			PrintStream pnpLog = new PrintStream(file);
			System.setOut(pnpLog);
			System.setErr(pnpLog);
		} catch (FileNotFoundException e) {
			updateConnectionStatusMessage(PNPConstants.STATUS_LOG_FILE_ERROR);
		}

	}

	/**
	 * Connect to USB device defined by Port selected in Connection Settings Panel 
	 * @return true if connection was successful
	 */
	private boolean connectUSB(){
		//initializing UsbDevice connects device and opens port at constructor
		usbDevice = new UsbDevice(this, connectionSettingsController.portSelectionInput.getSelectedPort(),
				connectionSettings.getBaudRate());
		//we test that device is open, and if it is not we attempt to open
		if(!usbDevice.isOpen()){
			if(usbDevice.openPort()){
				System.out.println("PNPMainController: connectUSB(): Port " + 
						usbDevice.getPortName() + " opened with no error\n");
				connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Disconnect");
				updateConnectionStatusMessage(PNPConstants.STATUS_CONNECTED);
				connectionSettings.setConnected(true);
				CTS = true;
				return true;
			}
			else{
				System.out.println("PNPMainController: connectUSB(): Port " + 
						usbDevice.getPortName() + " unable to open\n");
				updateConnectionStatusMessage(PNPConstants.STATUS_CONNECT_ERROR);
				connectionSettings.setConnected(false);
				return true;
			}
		}
		else{
			System.out.println("PNPMainController: connectUsb(): Usb device connected: " + 
					usbDevice.getPortName() + "\n");
			connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Disconnect");
			updateConnectionStatusMessage(PNPConstants.STATUS_CONNECTED);
			CTS = true;
			connectionSettings.setConnected(true);
			toggleVacuum(false);
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
				System.out.println("PNPMainController: -> disconnectUSB(): Port " + usbDevice.getPortName() +
						" closed without error\n");
				connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Connect To Device");
				updateConnectionStatusMessage(PNPConstants.STATUS_DISCONNECTED);
				CTS = false;
				connectionSettings.setConnected(false);
				return true;
			}
			else{
				System.out.println("PNPMainController: -> disconnectUSB(): Port " + usbDevice.getPortName() +
						" was not able to be closed\n");
				updateConnectionStatusMessage(PNPConstants.STATUS_DISCONNECT_ERROR);
				connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Connect To Device");
				updateConnectionStatusMessage(PNPConstants.STATUS_DISCONNECTED);
				CTS = false;
				connectionSettings.setConnected(false);
				return true;
			}
		}
		System.out.println("PNPMainController: -> disconnectUSB(): Port " + usbDevice.getPortName() +
				" was already closed, no changes made\n");
		connectionSettingsController.connectionButtons.connectToDeviceButton.setText("Connect To Device");
		updateConnectionStatusMessage(PNPConstants.STATUS_DISCONNECTED);
		CTS = false;
		connectionSettings.setConnected(false);
		return false;
	}

	/**
	 * Pause Job routine will pause or resume PNP job depending on current state of machine
	 */
	private void pauseJobRoutine(){
		if(processingJob){
			//if in PAUSED_STATE, resume processing
			if(importGCodePanel.getJobState() == UploadGCodeController.PAUSED_STATE){
				importGCodePanel.resumeJobButtonStates();
				jobPaused = false;
				processInstruction(currentInstructionIndex);
			}
			//else if in PROCESSING_STATE, pause machine
			else if(importGCodePanel.getJobState() == UploadGCodeController.PROCESSING_STATE){
				importGCodePanel.pauseJobButtonStates();
				jobPaused = true;
				int nextInstruction = currentInstructionIndex + 1;
				importGCodePanel.updateJobStatus("Machine Paused. Next Line To Be Executed: " + nextInstruction);
			}
		}
	}

	/**
	 * Update connection status message displayed to user on ConnectionSettingsController
	 * @param connectionStatus
	 */
	private void updateConnectionStatusMessage(int connectionStatus){
		switch(connectionStatus){
		case PNPConstants.STATUS_DISCONNECTED:
			connectionSettingsController.setConnectionStatus("Disconnected From PNP Machine: Connect to a device to begin");
			break;
		case PNPConstants.STATUS_CONNECTED:
			connectionSettingsController.setConnectionStatus("Currently Connected on Port: " + 
					usbDevice.getPortName());
			break;
		case PNPConstants.STATUS_SEND_ERROR:
			connectionSettingsController.setConnectionStatus("An error occurred while sending last message");
			break;
		case PNPConstants.STATUS_CONNECT_ERROR:
			connectionSettingsController.setConnectionStatus("An error occurred while connecting to port: " +
					usbDevice.getPortName() + ", double check connection settings");
			break;
		case PNPConstants.STATUS_DISCONNECT_ERROR:
			connectionSettingsController.setConnectionStatus("An error occurred while disconnecting from port: " +
					usbDevice.getPortName());
			break;
		case PNPConstants.STATUS_LOG_FILE_ERROR:
			connectionSettingsController.setConnectionStatus("An error occured while writing to PNP log file");
			break;
		case PNPConstants.STATUS_EMERGENCY_STOPPED:
			connectionSettingsController.setConnectionStatus("Emergency stop button has disabled machine." +
					"Job must be restarted.");
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
		//don't process message if usb device isn't connected or !CTS
		if(usbDevice != null && CTS){
			//package command into GCodeCommand object
			GCodeCommand command = new GCodeCommand(message, connectionSettings.getWidth(),
					connectionSettings.getDepth(), connectionSettings.getHeight());
			if(command.isValidCommand()){
				//if computer vision command
				if(command.isG56Command()){
					zeroComponentOrientation();
				}
				//else update values for x y and z then send message
				else if(updateValuesFromCommand(command)){
					//if command contains a Z value or F value, do not append feed rate
					if(command.iszMove() || command.isfCommand()){
						message += "F" + command.getfValue();
					}
					//send message, if succeeds then update CTS and return true
					if(usbDevice.writeMessage(message + "\n")){
						updateManualControllerPositionValues();
						System.out.println("PNPMainController: sendMessage(String message): Message sent with out error " +
								message + "\n");
						//CTS set to false, disabling UI from sending another command until a response is received
						CTS = false;
						return true;
					}
					//else transmission failed, restore previous values and report error
					else{
						System.out.println("PNPMainController: sendMessage(String message): Error Occurred, Message Not Sent\n");
						restorePreviousValues();
						return false;
					}
				}
			}
			else{
				System.out.println("PNPMainController: sendMessage(String message): Invalid Command Found\n");
				System.out.println(message);
				restorePreviousValues();
				CTS = true;
				return false;
			}
		}
		//Emergency stop button is often clicked when machine is in motion, therefore CTS will not be true
		//Emergency stop routine must be handled separately
		//Send emergency stop signal to PNP and update UI accordingly
		else if(message.contentEquals(PNPConstants.EMERGENCY_STOP)){
			usbDevice.writeMessage(PNPConstants.EMERGENCY_STOP);
			CTS = false;
			System.out.println("PNPMainController: sendMessage(String message): Emergency Stop Message Sent to PNP Machine");
			updateConnectionStatusMessage(PNPConstants.STATUS_EMERGENCY_STOPPED);
			manualController.updateErrorMessage("Emergency stop button has disabled PNP job.");
			importGCodePanel.updateJobStatus("Emergency stop button has disabled PNP job.");
			importGCodePanel.pauseJobButtonStates();
			return true;
		}
		else{
			System.out.println("PNPMainController: sendMessage(String message): Message not sent. " + 
					"UsbDevice not initialized or CTS == FALSE\n");
			CTS = false;
			return false;
		}
		return false;
	}

	/**
	 * Update ManualControllers view with current values
	 */
	private void updateManualControllerPositionValues(){
		manualController.updateCurrentXValue(currentX + "");
		manualController.updateCurrentYValue(currentY + "");
		manualController.updateCurrentZValue(currentZ + "");
		manualController.updateCurrentRValue(currentR + "");
	}

	/**
	 * Updates PNP Machine's current values for x, y, z, r, f by parsing argument GCodeCommand
	 * Values are first stored in back up variables using storeCurrentValues(), enabling us to undo any changes
	 * if an error is detected
	 * @param command GCodeCommand being used to update current values
	 * @return false if command is invalid (ie: out of bounds)
	 */
	private boolean updateValuesFromCommand(GCodeCommand command){
		//let's store old values first, then if we need to revert them we can easily
		storeCurrentValues();
		//G Move Commmands
		if(command.isgCommand()){
			//Home command resets X Y to 0
			if(command.getgValue() == 28){
				currentX = 0;
				currentY = 0;
			}
			//Store X Value
			if(command.isxMove()){
				if(command.getxValue() >= 0 && command.getxValue() <= connectionSettings.getWidth()){
					currentX = command.getxValue();
				}
				else{
					System.out.println("PNPMainController: updateValuesFromCommand(): Unable to send" +
							"\nVerify X dimension is not out of bounds\n");
					return false;
				}
			}
			//Store Y Value
			if(command.isyMove()){
				if(command.getyValue() >= 0 && command.getyValue() <= connectionSettings.getDepth()){
					currentY = command.getyValue();
				}
				else{
					System.out.println("PNPMainController: updateValuesFromCommand(): Unable to send" +
							"\nVerify Y dimension is not out of bounds\n");
					return false;
				}
			}
			//Store Z Value
			if(command.iszMove()){
				if(command.getzValue() >= 0 && command.getzValue() <= connectionSettings.getHeight()){
					currentZ = command.getzValue();
				}
				else{
					System.out.println("PNPMainController: updateValuesFromCommand(): Unable to send" +
							"\nVerify Z dimension is not out of bounds\n");
					return false;
				}
			}
			//Store R Value
			if(command.isrMove()){
				currentR = command.getrValue();
			}
		}
		//Feed Rate Commands
		if(command.isfCommand()){
			currentFeedRate = command.getfValue();
		}			
		//if command is G1 FXXX, save new feed rate to connection settings and update UI
		if(command.isG1FCommand()){
			connectionSettings.setFeedRate(command.getfValue());
			connectionSettingsController.setFeedRate(command.getfValue());
		}
		//M Vacuum Commands
		if(command.ismCommand()){
			if(command.ismCommand()){
				if(command.getmValue() == 10){
					System.out.println("PNPMainController: updateValuesFromCommand(): Updating Vacuum State: VACUUM ON\n");
					//vacuumOn = true;
				}
				else if(command.getmValue() == 11){
					System.out.println("PNPMainController: updateValuesFromCommand(): Updating Vacuum State: VACUUM OF\n");
					//vacuumOn = false;
				}
				else{
					System.out.println("PNPMainController: updateValuesFromCommand(): Invalid Value Detected for 'M'\n");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Stores current values for X, Y, Z, R, and F in back up variables
	 * To be used in case error is detected and values need to be restored
	 */
	private void storeCurrentValues(){
		previousX = currentX;
		previousY = currentY;
		previousZ = currentZ;
		previousR = currentR;
		previousFeedRate = currentFeedRate;
	}

	/**
	 * Restore values for X, Y, Z, R, and F from previous instruction. 
	 * To be used after a transmission error that failed.
	 * Example) currentX = previousX
	 */
	private void restorePreviousValues(){
		System.out.println("PNPMainController: restorePreviousValues(): transmission failed, restoring previous values from memory\n");
		currentX = previousX;
		currentY = previousY;
		currentZ = previousZ;
		currentR = previousR;
		currentFeedRate = previousFeedRate;
	}

	/**
	 * Stops machine's current operation
	 * @return true if stop procedure was successful
	 */
	private boolean emergencyStop(){
		// TODO we can not send this message through
		if(sendMessage(PNPConstants.EMERGENCY_STOP)){
			System.out.println("PNPMainController: emergencyStop(): PNP Machine Stopped\n");
			CTS = true;
			return true;
		}
		else{
			System.out.println("PNPMainController: emergencyStop(): unable to send emergency stop command\n");
			CTS = true;
			return false;
		}
	}

	/**
	 * Restore default values found on Connection Settings View
	 * Default values are defined in the ConnectionSettingsModel class
	 */
	private void restoreDefaults(){
		connectionSettings.restoreDefaultValues();
		//update UI input fields with defaults
		connectionSettingsController.setPlatformDepth(connectionSettings.getDepth());
		connectionSettingsController.setPlatformWidth(connectionSettings.getWidth());
		connectionSettingsController.setPlatformHeight(connectionSettings.getHeight());
		connectionSettingsController.setFeedRate(connectionSettings.getFeedRate());
		connectionSettingsController.setBaudRate(connectionSettings.getBaudRate());
		manualController.setStepSize(JogSizeSlider.SLIDER_5);
	}

	/**
	 * Jog machine in positive Y direction
	 */
	private void jogYPlus(){
		double jogYPosition = currentY + manualController.getStepSize();
		sendMessage("G1 Y" + jogYPosition);
	}

	/**
	 * Jog machine in negative Y direction
	 */
	private void jogYMinus(){
		double jogYPosition = currentY - manualController.getStepSize();
		sendMessage("G1 Y" + jogYPosition);
	}

	/**
	 * Jog machine in positive X direction
	 */
	private void jogXPlus(){
		double jogXPosition = currentX + manualController.getStepSize();
		sendMessage("G1 X" + jogXPosition);
	}

	/**
	 * Jog machine in negative X direction
	 */
	private void jogXMinus(){
		double jogXPosition = currentX - manualController.getStepSize();
		sendMessage("G1 X" + jogXPosition);
	}

	/**
	 * Jog machine in positive Z direction
	 */
	private void jogZPlus(){
		double jogZPosition = currentZ + manualController.getStepSize();
		sendMessage("G1 Z" + jogZPosition);
	}

	/**
	 * Jog machine in negative Z direction
	 */
	private void jogZMinus(){
		double jogZPosition = currentZ - manualController.getStepSize();
		sendMessage("G1 Z" + jogZPosition);
	}

	/**
	 * Rotate vacuum nozzle in CW direction
	 */
	private void spindleCW(){
		double spindlePosition = currentR + manualController.getStepSize();
		sendMessage("G1 R" + spindlePosition);
	}

	/**
	 * Rotate vacuum nozzle in CCW direction
	 */
	private void spindleCCW(){
		double spindlePosition = currentR - manualController.getStepSize();
		sendMessage("G1 R" + spindlePosition);
	}

	/**
	 * Toggle vacuum on/off
	 * @param boolean turnOn
	 */
	private void toggleVacuum(boolean turnOn){
		if(turnOn){
			vacuumOn = true;
			sendMessage("M10");
		}
		else{
			vacuumOn = false;
			sendMessage("M11");
		}
	}

	/**
	 * Handle read events from UsbDevice
	 * Verify if PIC return value matches Applications current values for X Y Z
	 */
	@Override
	public void UsbReadEvent(String message) {
		System.out.println("PNPMainController: UsbReadEvent: PIC Return Value: " + message.replace("\n", "") + "\n");
		PICReturnValue picReturnCommand = new PICReturnValue(message);
		if(picReturnCommand.isValidPicReturn()){
			if(picReturnCommand.isErrorMessage()){
				System.out.println("PNPMainController: UsbReadEvent: PIC Returned Error: " + picReturnCommand.getErrorType());
				importGCodePanel.updateJobStatus(picReturnCommand.getErrorType());
				JDialog.setDefaultLookAndFeelDecorated(true);
				JOptionPane.showMessageDialog(null, picReturnCommand.getErrorType(), "PIC Error Received", JOptionPane.INFORMATION_MESSAGE);
				pauseJobRoutine();
			}
			else{
				verifyUIPositionAgainstPICPosition(picReturnCommand);
				//if we are processing a PNP File, send the next instruction
				if(processingJob){
					CTS = true;
					importGCodePanel.gCodeConsoleView.removeHighlightLine(currentInstructionIndex);
					currentInstructionIndex++;
					if(currentInstructionIndex < gCodeInstructions.size() && !jobPaused){
						processInstruction(currentInstructionIndex);
						importGCodePanel.gCodeConsoleView.highlightLine(currentInstructionIndex);
					}
					else if(jobPaused){
						System.out.println("\nPNPMainController: JOB PAUSED\n");
					}
					else{
						System.out.println("\nPNPMainController: JOB COMPLETED WITHOUT ERROR\n");
						processingJob = false;
						importGCodePanel.stopJobButtonStates();
						importGCodePanel.updateJobStatus("PNP Job Complete");
						//JOB COMPLETE - ALLOW USER TO SAVE UPDATED PARTS FILE
						displaySavePartFilesDialog();
					}
				}
				//if processing image data in zeroComponentOrientation() method
				//continue to zeroComponentOrientation()
				else if(processingImageData){
					CTS = true;
					zeroComponentOrientation();
				}
				else{
					CTS = true;
				}

			}
		}
		//TODO
		//How to handle an invalid PIC return message? This should never happen...
	}

	/**
	 * Display a dialog to user asking them to save updated parts file after PNP job completes
	 * Saving parts file after job completion allows the next job to use same parts
	 * @return true if user selects "Yes" to save updated parts files
	 */
	private boolean displaySavePartFilesDialog(){
		JDialog.setDefaultLookAndFeelDecorated(true);
		int response = JOptionPane.showConfirmDialog(null, "Click Yes To Store Updated Part Files.",
				"Available Parts File Updated",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		//IF YES - SAVE UPDATEDPARTSFILE TO FILENAME SPECIFIED BY USER
		if (response == JOptionPane.YES_OPTION) {
			String newPartsFile = gCodeGeneratorPanel.getUpdatedPartsFileAsString();
			FileNameExtensionFilter mExtensionFilter = new FileNameExtensionFilter("Text File", "txt");
			JFileChooser mSaveAsFileChooser = new JFileChooser();
			mSaveAsFileChooser.setApproveButtonText("Save");
			mSaveAsFileChooser.setFileFilter(mExtensionFilter);
			int actionDialog = mSaveAsFileChooser.showOpenDialog(this);
			if (actionDialog != JFileChooser.APPROVE_OPTION) {
				return false;
			}
			File file = mSaveAsFileChooser.getSelectedFile();
			if (!file.getName().endsWith(".txt")) {
				file = new File(file.getAbsolutePath() + ".txt");
			}
			BufferedWriter outFile = null;
			try {
				outFile = new BufferedWriter(new FileWriter(file));
				outFile.append(newPartsFile);
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (outFile != null) {
					try {
						outFile.close();
					} catch (IOException e) {}
				}
			}
			return true;
		} 
		//IF NO - RETURN FALSE
		return false;
	}

	/**
	 * Check value of PIC return command against PNP UI current values for X Y Z
	 * @param picReturnCommand GCodeCommand received from PIC as return value
	 * @return true of PIC return value matches PNP Applications current values for X Y Z
	 */
	private boolean verifyUIPositionAgainstPICPosition(PICReturnValue picReturnCommand){
		if(picReturnCommand.getxValue() == currentX && picReturnCommand.getyValue() == currentY
				&& picReturnCommand.getzValue() == currentZ){
			return true;
		}
		else{
			System.out.println("PNPMainController: UsbReadEvent: PNP App & PIC Coordinates Do Not Match\n");
			System.out.println("UI Values: X" + currentX + " Y" + currentY + " Z" + currentZ);
			System.out.println("PIC Returned: " + picReturnCommand.getPicReturnValue());
			System.out.println("Defaulting to PIC's values. Updating values now.");
			//we will assign the UI the coordinates that PIC believes to be right
			this.currentX = picReturnCommand.getxValue();
			this.currentY = picReturnCommand.getyValue();
			this.currentZ = picReturnCommand.getzValue();
			return false;
		}
	}

	/**
	 * Process the G Code instructions found in Import G Code View line by line until complete
	 * @return
	 */
	private void processGCodeFromConsole(){
		importGCodePanel.startJobButtonStates();
		gCodeInstructions = importGCodePanel.getConsoleContentArray();
		currentInstructionIndex = 0;
		processingJob = true;
		importGCodePanel.updateJobStatus("Transmitting G Code at Line: " + currentInstructionIndex);
		processInstruction(currentInstructionIndex);
	}

	/**
	 * Sends the next instruction to the PNP machine for processing and updates all UI elements
	 * @param currentInstructionIndex
	 */
	private void processInstruction(int currentInstructionIndex){
		//skip over blank lines and comments
		while(gCodeInstructions.get(currentInstructionIndex).matches("\\s*") || 
				gCodeInstructions.get(currentInstructionIndex).trim().charAt(0) == ';'){
			currentInstructionIndex++;
		}
		importGCodePanel.updateJobStatus("Transmitting G Code at Line: " + currentInstructionIndex);
		if(sendMessage(gCodeInstructions.get(currentInstructionIndex))){
			System.out.println("PNPMainController -> processInstruction: Instruction at line " + 
					currentInstructionIndex + " was processed without error\n");
		}
		else{
			System.out.println("PNPMainController: processInstruction: Instruction at line " + 
					currentInstructionIndex + " received error message during transmission\n");
			importGCodePanel.updateJobStatus("Error Transmitting G Code at Line: " + currentInstructionIndex);
		}
	}

	/**
	 * Captures image of component held over light box and rotates component to 0 deg
	 * @return true if
	 */
	private void zeroComponentOrientation(){
		ComponentFinder mComponentFinder = new ComponentFinder();
		mComponentFinder.captureImageData();
		//check if THRESHOLD < Orientation < THRESHOLD
		if(Math.abs(mComponentFinder.getOrientation()) > PNPConstants.IMAGE_ORIENTATION_THRESHOLD){
			sendMessage("G1 R" + mComponentFinder.getOrientation());
			processingImageData = true;
		}
		//else component does not require rotation
		//processingImageData routine is complete
		else{
			processingImageData = false;
		}
	}

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	//5 application views for user interface
	public ConnectionSettingsController connectionSettingsController;
	private GCodeGeneratorController gCodeGeneratorPanel;
	private UploadGCodeController importGCodePanel;
	private ManualController manualController;
	private DefinePartsController definePartsController;
	//UsbDevice for communicating to PIC
	private UsbDevice usbDevice;
	//Model for storing/manipulating connection settings
	private ConnectionSettingsModel connectionSettings;
	//clear to send flag used to prevent multiple instructions from sending
	private boolean CTS;
	//current coordinates of PNP machine head
	private double currentX;
	private double currentY;
	private double currentZ;
	private double currentR;
	private int currentFeedRate;
	private boolean vacuumOn;
	//previous coordinates of PNP machine head
	//used to restore values after failed transmission
	private double previousX;
	private double previousY;
	private double previousZ;
	private double previousR;
	private int previousFeedRate;
	//processing G Code jobs
	private boolean processingJob;
	List<String> gCodeInstructions;
	private int currentInstructionIndex;
	private boolean jobPaused;
	//processing Computer Vision routine
	private boolean processingImageData;
}