package import_g_code_ui.controller;

import g_code_generator_ui.model.GCodeCommand;
import import_g_code_ui.view.CommandConsoleView;
import import_g_code_ui.view.ProcessJobButtonView;
import import_g_code_ui.view.SelectGCodeInputView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import connection_settings_ui.view.SettingsTitleView;

/**
 * ImportGCodeController assigns functionality to buttons in Upload G Code Views
 * @author Justin Johnson
 *
 */
public class UploadGCodeController extends JPanel{

	/**
	 * Constructor creates instance of individual components(InputFilePanel, GCodeConsolPanel, and
	 * ProcessJobPanel), combines them into one panel, and defines button functionality
	 */
	public UploadGCodeController() {
		initUI();
		initButtons();
		jobState = STOPPED_STATE;
	}

	/**
	 * Add all component views to display
	 * Views: Input Selection, G Code Console Display, and Job Processing Buttons
	 */
	private void initUI(){
		//Dimensions Title
		uploadGCodeTitle = new SettingsTitleView("Upload Job To PNP Machine");
		uploadGCodeTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Input File Panel
		selectInputView = new SelectGCodeInputView("G Code Input Path:");
		selectInputView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Console Panel (displays g code being processed)
		gCodeConsoleView = new CommandConsoleView();
		gCodeConsoleView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Job Processing Buttons
		processJobButtonsView = new ProcessJobButtonView(SELECT_INPUT);
		processJobButtonsView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Add individual components to this.JPanel for final display, laid on vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		this.add(uploadGCodeTitle);
		this.add(selectInputView);
		this.add(gCodeConsoleView);
		this.add(processJobButtonsView);
	}

	/**
	 * Initialize button functionality
	 * Select Input File Button
	 * Start Job Button
	 * Pause Job Button
	 * Terminate Job Button
	 */
	private void initButtons(){
		//Select Input File Button selects a file then displays file's content in console
		selectInputView.getFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Input");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					selectInputView.updateInputPath(chooser.getSelectedFile().toString());
					setConsoleContent(selectInputView.getFileContent());
					selectInputView.validateGCodeButton.setEnabled(true);
					gCodeConsoleView.startJobButton.setEnabled(false);
					gCodeConsoleView.pauseJobButton.setEnabled(false);
					gCodeConsoleView.pauseJobButton.setText("Pause");
					gCodeConsoleView.stopJobButton.setEnabled(false);
					processJobButtonsView.updateJobStatus("File Found. Please Validate G Code");
				} 
			}
		});
	}

	/**
	 * Update console display's text 
	 * @param String content to be applied to console's TextView
	 */
	private void setConsoleContent(String content){
		gCodeConsoleView.writeToConsole(content);
		gCodeConsoleView.gCodeTextArea.setSelectionStart(0);
		gCodeConsoleView.gCodeTextArea.setSelectionEnd(10); 
		gCodeConsoleView.gCodeTextArea.setSelectedTextColor(Color.BLUE);
	}

	/**
	 * Generate a List where each element of array contains String representing 1 line
	 * from the G Code Console
	 * @return String[] containing all lines of G Code found in G Code Console
	 */
	public List<String> getConsoleContentArray(){
		return Arrays.asList(gCodeConsoleView.getConsoleContent().split("\n"));
	}

	/**
	 * Traverse array of G Code Instructions found in Console View and validate that no errors exist
	 * @return true if no G Code errors are found
	 */
	public boolean validateGCode(double xMax, double yMax, double zMax){
		List<String> gCodeList = getConsoleContentArray();
		int lineNumber = 1;
		clearHighlights();
		for(String gCodeLine : gCodeList){
			//if not a blank line or comment -> process line
			if(!(gCodeLine.matches("\\s*") || gCodeLine.trim().charAt(0) == ';')){
				GCodeCommand command = new GCodeCommand(gCodeLine, xMax, yMax, zMax);
				//if bad command found, return false and highlight line number
				if(command.isBadCommand()){
					gCodeConsoleView.highlightLine(lineNumber-1);
					processJobButtonsView.updateJobStatus("Invalid G Code Command Found On Line: " + lineNumber);
					return false;
				}
			}
			lineNumber++;
		}
		processJobButtonsView.updateJobStatus("Validation Completed Without Errors. Press 'Start' To Begin Job");
		System.out.println("\nUploadGCodeController -> No G Code Errors Found\n");
		gCodeConsoleView.startJobButton.setEnabled(true);
		selectInputView.validateGCodeButton.setEnabled(false);
		return true;
	}
	
	/**
	 * Remove all highlights from console view
	 */
	private void clearHighlights(){
		List<String> gCodeList = getConsoleContentArray();
		for(int i = 0; i < gCodeList.size(); i++){
			gCodeConsoleView.removeHighlightLine(i);
		}
	}
	
	/**
	 * Update view's button states after start job button has been pressed
	 */
	public void startJobButtonStates(){
		gCodeConsoleView.startJobButton.setEnabled(false);
		gCodeConsoleView.pauseJobButton.setEnabled(true);
		gCodeConsoleView.stopJobButton.setEnabled(true);
		selectInputView.getFileButton.setEnabled(false);
		jobState = PROCESSING_STATE;
	}
	
	/**
	 * Upate view's button states after pause job button has been pressed
	 */
	public void pauseJobButtonStates(){
		gCodeConsoleView.pauseJobButton.setText("Resume");
		selectInputView.getFileButton.setEnabled(true);
		jobState = PAUSED_STATE;
	}

	/**
	 * Update view's button states after resume job button has been pressed
	 */
	public void resumeJobButtonStates(){
		gCodeConsoleView.pauseJobButton.setText("Pause");
		selectInputView.getFileButton.setEnabled(false);
		jobState = PROCESSING_STATE;
	}

	/**
	 * Update view's button states after stop job button has been pressed
	 */
	public void stopJobButtonStates(){
		//adjust button display settings
		gCodeConsoleView.startJobButton.setEnabled(true);
		gCodeConsoleView.pauseJobButton.setText("Pause");
		gCodeConsoleView.pauseJobButton.setEnabled(false);
		gCodeConsoleView.stopJobButton.setEnabled(false);
		selectInputView.getFileButton.setEnabled(true);
		jobState = STOPPED_STATE;
	}

	/**
	 * Update PNP Machine's current job status
	 * @param String status to be applied to UI
	 */
	public void updateJobStatus(String status){
		processJobButtonsView.updateJobStatus(status);
	}
	
	/**
	 * Get current state of job
	 * @return jobState
	 */
	public int getJobState(){
		return jobState;
	}

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Constants
	 */
	//private static final String DISCONNECTED = "Device Disconnected. Visit Settings Page to connect to a device";
	private static final String SELECT_INPUT = "Please select an input file to begin working";
	//private static final String CONNECTED = "Device Connected. Click 'Start Job' to begin processing G Code.";
	public static final int STOPPED_STATE = 0;
	public static final int PROCESSING_STATE = 1;
	public static final int PAUSED_STATE = 2;

	/**
	 * Class variables
	 */
	private SettingsTitleView uploadGCodeTitle;
	public SelectGCodeInputView selectInputView;
	public CommandConsoleView gCodeConsoleView;
	public ProcessJobButtonView processJobButtonsView;
	private int jobState;
	//private int nextInstruction;
	

}
