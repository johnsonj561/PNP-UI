package g_code_generator_ui.view;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * GCodeInputFileView displays a Textfield (path) and Filechooser button to user,
 * allowing user to enter input files to application
 * @author Justin Johnson
 *
 */
public class GCodeInputFileView extends JPanel{

	

	public GCodeInputFileView(String heading) {
		//Input heading panel
		inputFileHeadingPanel = new JPanel();
		inputFileHeadingPanel.setLayout(new BoxLayout(inputFileHeadingPanel, BoxLayout.X_AXIS));
		inputFileHeadingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputFileHeadingPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
		getFileLabel = new JLabel(heading);
		inputFileHeadingPanel.add(getFileLabel);
		//Centroid Input file panel
		getFilePanel = new JPanel();
		getFilePanel.setLayout(new BoxLayout(getFilePanel, BoxLayout.X_AXIS));
		getFilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		getFilePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 25));
		inputFileTextField = new JTextField(30);
		inputFileTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, inputFileTextField.getPreferredSize().height));
		getFileButton = new JButton("Browse");
		getFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button Clicked!");
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Input");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					updateInputPath(chooser.getSelectedFile().toString());
				} 
			}
		});
		getFilePanel.add(inputFileTextField);
		getFilePanel.add(Box.createRigidArea(new Dimension(25, 0)));
		getFilePanel.add(getFileButton);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(inputFileHeadingPanel);
		this.add(getFilePanel);
	}
	
	/**
	 * Updates text field to display the file path selected by user
	 * @param path of the file selected by user
	 */
	private void updateInputPath(String path){
		inputFileTextField.setText(path);
	}
	
	/**
	 * Get input path from Text Field
	 * @return String input path
	 */
	public String getInputPath(){
		return inputFileTextField.getText();
	}
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	private JPanel inputFileHeadingPanel;
	private JPanel getFilePanel;
	private JLabel getFileLabel;
	private JTextField inputFileTextField;
	private JButton getFileButton;
}
