package import_g_code_ui.view;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ProcessJobPanel constructs a JPanel object that displays job status to user and
 * provides functionality to update status programatically 
 * @author Justin Johnson
 */
public class ProcessJobButtonView extends JPanel{

	/**
	 * Constructor assigns job status to JLabel and creates builds JPanel
	 * @param status
	 */
	public ProcessJobButtonView(String status) {
		this.status = status;
		initUI();
	}

	/**
	 * Initialize Job Process and Status UI Elements
	 */
	private void initUI(){
		processJobHeadingLabel = new JLabel("Job Status:");
		processJobHeadingLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		jobStatusLabel = new JLabel(status);
		jobStatusLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		//add heading label, status label, and buttons to this JPanel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 10));
		this.add(processJobHeadingLabel);
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		this.add(jobStatusLabel);
	}

	/**
	 * Updates text field to display the file path selected by user
	 * @param path of the file selected by user
	 */
	public void updateJobStatus(String status){
		jobStatusLabel.setText(status);
	}
	

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Variables
	 */
	private JLabel processJobHeadingLabel;
	private JLabel jobStatusLabel;
	private String status;
}
