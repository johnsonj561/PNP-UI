package import_g_code_ui.view;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * HorizontalInputFilePanel displays a Textfield (path) and Filechooser button to user,
 * allowing user to enter input files to application and provides functinality to access/modify
 * the text of input path. Variant of InputFilePanel that displays all text components in same horizontal row
 * @author Justin Johnson
 *
 */
public class SelectGCodeInputView extends JPanel{

	/**
	 * Constructor builds custom JPanel that contains a heading, file path text field, and filechooser button
	 * @param heading
	 */
	public SelectGCodeInputView(String heading) {
		//Input heading panel
		inputFileHeadingPanel = new JPanel();
		inputFileHeadingPanel.setLayout(new BoxLayout(inputFileHeadingPanel, BoxLayout.X_AXIS));
		inputFileHeadingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputFileHeadingPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 5));
		getFileLabel = new JLabel(heading);
		inputFileHeadingPanel.add(getFileLabel);
		//Input file panel
		getFilePanel = new JPanel();
		getFilePanel.setLayout(new BoxLayout(getFilePanel, BoxLayout.X_AXIS));
		getFilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		getFilePanel.setBorder(BorderFactory.createEmptyBorder(25, 5, 10, 10));
		inputFileTextField = new JTextField(30);
		inputFileTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, inputFileTextField.getPreferredSize().height));
		getFileButton = new JButton("Browse");
		//add components to panel
		getFilePanel.add(inputFileTextField);
		getFilePanel.add(Box.createRigidArea(new Dimension(25, 0)));
		getFilePanel.add(getFileButton);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(inputFileHeadingPanel);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
		this.add(getFilePanel);
	}
	
	/**
	 * Updates text field to display the file path selected by user
	 * @param path of the file selected by user
	 */
	public void updateInputPath(String path){
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
	 * Gets content from file and returns content as a single String object
	 * @param inputFile path to file being read
	 * @return String containing all content fount at inputPath
	 */
	public String getFileContent(){
		//print path to console for testing
		System.out.println(inputFileTextField.getText());
		String inputContent = "";
		Scanner mScanner;
		try {
			mScanner = new Scanner(new File(inputFileTextField.getText()));
			inputContent = mScanner.useDelimiter("\\Z").next();
			mScanner.close();
		} catch (FileNotFoundException e) {
			inputContent = "File Not Found Exception:\n" + e;
			e.printStackTrace();
		}
		
		return inputContent;
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
	public JButton getFileButton;
}
