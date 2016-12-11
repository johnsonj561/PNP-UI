package connection_settings_ui.view;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ProcessJobPanel constructs a JPanel object that displays job status to user and
 * provides functionality to update status programatically 
 * @author Justin Johnson
 */
public class LogFileView extends JPanel{

	/**
	 * Constructor assigns job status to JLabel and creates builds JPanel
	 * @param status
	 */
	public LogFileView() {
		initUI();
	}

	/**
	 * Initialize Job Process and Status UI Elements
	 */
	private void initUI(){
		//panel to enable logging
		enableLogPanel = new JPanel();
		enableLogPanel.setLayout(new BoxLayout(enableLogPanel, BoxLayout.X_AXIS));
		enableLogPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		enableLogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		enableLogLabel = new JLabel(ENABLE_LOG_OPTION);
		//enableLogLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		enableLogCheckBox = new JCheckBox();
		enableLogPanel.add(enableLogLabel);
		enableLogPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		enableLogPanel.add(enableLogCheckBox);
		//panel to display log file location
		logFilePanel = new JPanel();
		logFilePanel.setLayout(new BoxLayout(logFilePanel, BoxLayout.X_AXIS));
		logFilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		logFilePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		logFileLabel = new JLabel("");
		logFileLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		logFilePanel.add(logFileLabel);
		//add components to this.JPanel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(enableLogPanel);
		this.add(logFilePanel);
	}
	
	/**
	 * Update the path defining location of log file
	 * @param path
	 */
	public void updateLogFileLocation(String path){
		logFileLabel.setText(path);
	}


	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Variables
	 */
	private JPanel enableLogPanel;
	private JLabel enableLogLabel;
	public JCheckBox enableLogCheckBox;
	private final String ENABLE_LOG_OPTION = "Enable Debug Logging";
	private JPanel logFilePanel;
	private JLabel logFileLabel;
	
}
