package import_g_code_ui.controller;

import import_g_code_ui.view.CommandConsoleView;
import import_g_code_ui.view.ProcessJobButtonView;
import import_g_code_ui.view.SelectGCodeInputView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * ImportGCodeController assigns functionality to ImportGCodeView's buttons
 * @author Justin Johnson
 *
 */
public class ImportGCodeController extends JPanel{

	/**
	 * Constructor creates instance of individual components(InputFilePanel, GCodeConsolPanel, and
	 * ProcessJobPanel), combines them into one panel, and defines button functionality
	 */
	public ImportGCodeController() {
		initUI();
		initButtons();
		jobState = STOPPED_STATE;
	}

	/**
	 * Add all component views to display
	 * Views: Input Selection, G Code Console Display, and Job Processing Buttons
	 */
	private void initUI(){
		//Input File Panel
		selectInputView = new SelectGCodeInputView("G Code Input Path:");
		selectInputView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Console Panel (displays g code being processed)
		gCodeConsoleView = new CommandConsoleView();
		gCodeConsoleView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Job Processing Buttons
		processJobButtonsView = new ProcessJobButtonView(DISCONNECTED);
		processJobButtonsView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Add individual components to this.JPanel for final display, laid on vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
				System.out.println("Button Clicked!");
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Input");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					selectInputView.updateInputPath(chooser.getSelectedFile().toString());
					setConsoleContent(selectInputView.getFileContent());
				} 
			}
		});
		//Start Job Button
		gCodeConsoleView.startJobButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startJob();
			}
		});
		//Stop Job Button
		gCodeConsoleView.stopJobButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopJob();
			}
		});
		//Pause/Resume Job Button
		gCodeConsoleView.pauseJobButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jobState == PROCESSING_STATE){
					nextInstruction = pauseJob();
				}
				else{
					resumeJob(nextInstruction);
				}
			}
		});
	}

	/**
	 * Update console display's text 
	 * @param String content to be applied to console's TextView
	 */
	private void setConsoleContent(String content){
		gCodeConsoleView.write(content);
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
		//remove comments

		return Arrays.asList(gCodeConsoleView.getConsoleContent().split("\n"));
	}

	/**
	 * Initiate PNP Job
	 */
	private void startJob(){
		//TODO check if valid g code is present in console 1st
		//validateGCode();
		
		//TODO MUST BE THREAD SAFE TO ALLOW ACCESS TO UI
		//new thread
		//change state to processing
		jobState = PROCESSING_STATE;
		
		//update button displays
		gCodeConsoleView.startJobButton.setEnabled(false);
		gCodeConsoleView.pauseJobButton.setEnabled(true);
		gCodeConsoleView.stopJobButton.setEnabled(true);
	}

	/**
	 * Pause PNP Job
	 * @return nextInstruction integer value that points to the next line of G Code to be executed
	 */
	private int pauseJob(){
		//TODO complete current instruction, store pointer so we know where to resume, pause transmission
		int nextInstruction = 0;
		gCodeConsoleView.removeFirstLineFromConsole();
		gCodeConsoleView.highlightLine(0);
		//change state to paused
		jobState = PAUSED_STATE;
		//update button displays
		gCodeConsoleView.pauseJobButton.setText("Resume");

		return nextInstruction;
	}

	/**
	 * Resume PNP Job
	 * @param nextInstruction integer value that points to the next line of G Code to be executed
	 */
	private void resumeJob(int nextInstruction){
		//TODO get nextInstruction from console and resume processing
		//change state to processing
		jobState = PROCESSING_STATE;
		//adjust button display settings
		gCodeConsoleView.pauseJobButton.setText("Pause");
	}

	/**
	 * WARNING: Entirely terminates PNP Job being processed
	 * 
	 */
	private void stopJob(){
		//TODO store 
		jobState = STOPPED_STATE;
		//adjust button display settings
		gCodeConsoleView.startJobButton.setEnabled(true);
		gCodeConsoleView.pauseJobButton.setText("Pause");
		gCodeConsoleView.pauseJobButton.setEnabled(false);
		gCodeConsoleView.stopJobButton.setEnabled(false);
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
	private static final String DISCONNECTED = "Device Disconnected. Visit Settings Page to connect to a device.";
	//private static final String CONNECTED = "Device Connected. Click 'Start Job' to begin processing G Code.";
	private static final int STOPPED_STATE = 0;
	private static final int PROCESSING_STATE = 1;
	private static final int PAUSED_STATE = 2;

	/**
	 * Class variables
	 */
	private SelectGCodeInputView selectInputView;
	private CommandConsoleView gCodeConsoleView;
	private ProcessJobButtonView processJobButtonsView;
	private int jobState;
	private int nextInstruction;


}
