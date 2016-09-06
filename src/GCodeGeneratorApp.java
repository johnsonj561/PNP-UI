import g_code_generator_ui.controller.GCodeGeneratorController;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * GCodeGeneratorApp creates instance of GCodeGenerator Panel and displays to user for testing purposes.
 * GCodeGeneratorApp translates input centroid files (txt) into G Code commands for PNP Machine
 * @author Justin Johnson
 *
 */
public class GCodeGeneratorApp{

	/**
	 * Default Constructor initializes UI
	 */
	public GCodeGeneratorApp(){
		System.out.println("Creating instance of G Code Generator UI");
		initUI();
	}

	/**
	 * Initialize UI components to allow user to select centroid/parts input files 
	 * and generate/download corresponding G-Code
	 */
	private void initUI(){
		//declare style
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		//Declare main JFrame and Default Close Operation
		mainFrame = new JFrame("PnP Machine Interface");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		GCodeGeneratorController mGCodeGeneratorPanel = new GCodeGeneratorController();
		mGCodeGeneratorPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		mainFrame.add(mGCodeGeneratorPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	/**
	 * GCodeGeneratorApp generates instace of GCodeGenerator Application for testing
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				GCodeGeneratorApp UI = new GCodeGeneratorApp();
			}
		});	
	}
	
	//CLASS MEMBERS
	private JFrame mainFrame;
}
