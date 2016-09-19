import g_code_generator_ui.controller.GCodeGeneratorController;
import import_g_code_ui.controller.UploadGCodeController;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import connection_settings_ui.controller.ConnectionSettingsController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

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
		initUI();
	}

	/**
	 * Initialize PNP Main UI Controller's Components
	 */
	private void initUI(){
		JTabbedPane tabbedPane = new JTabbedPane();
		//Connection settings
		ConnectionSettingsController connectionSettionsController = new ConnectionSettingsController();
		tabbedPane.addTab("Connect Settings", null, connectionSettionsController, 
				"Configure connection settings and connect to a PNP device");
		//G Code Generation
		GCodeGeneratorController gCodeGeneratorPanel = new GCodeGeneratorController();
		tabbedPane.addTab("Generate G Code", null, gCodeGeneratorPanel, 
				"Upload PCB design files and generate G Code Instructions");
		//G Code Upload To Machine
		UploadGCodeController importGCodePanel = new UploadGCodeController();
		tabbedPane.addTab("Upload To PNP", null, importGCodePanel, 
				"Upload entire projects to PNP machine for processing");
		//Manual PNP Control
		JComponent panel5 = makeTextPanel("Coming Soon:\nManual Jog Controls\nSend Manual Commands");
		tabbedPane.addTab("Manual PNP Control", null, panel5, "Manual PNP Control");
		//Create Parts Specifications
		JComponent panel4 = makeTextPanel("Coming Soon:\nCreate A Parts File");
		tabbedPane.addTab("Create Parts File", null, panel4, "Create Parts File");
		//add tabs to JPanel
		add(tabbedPane);
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
	 * Create the UI and show it.
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
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
}