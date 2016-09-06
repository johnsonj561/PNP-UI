package g_code_generator_ui.view;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * GCodeEditorView displays a text area with scroll bar, allowing user to view and edit
 * G-Code output prior to saving file
 * @author Justin Johnson
 *
 */
public class GCodeEditorView extends JPanel{

	/**
	 * Constructor creates instance of GCodeEditorView
	 */
	public GCodeEditorView() {
		//create Panel for text field (G Code Editor)
		textAreaPanel = new JPanel();
		textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.Y_AXIS));
		textAreaPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 25));
		gCodeTextArea = new JTextArea(20, 40);
		scrollPane = new JScrollPane(gCodeTextArea); 
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaPanel.add(scrollPane);	
		//create panel for save and clear buttons
		buttonsPanel = new JPanel();
		//use flow layout and align buttons to right
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 25));
		//save button functionality
		saveGCodeButton = new JButton("Save As");
		//clear button functionality
		clearGCodeButton = new JButton("Clear");
		//add buttons to button panel
		buttonsPanel.add(clearGCodeButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		buttonsPanel.add(saveGCodeButton);
		//components of GCodeEditorPanel will be laid out along Y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//add components to this.JPanel
		this.add(textAreaPanel);
		this.add(buttonsPanel);
	}

	/**
	 * Clear G Code Output editor of all text
	 */
	public void clearOutput(){
		gCodeTextArea.setText("");
	}

	public void write(String s){
		gCodeTextArea.setText(s);
	}
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	//CLASS MEMBERS
	private JPanel textAreaPanel;
	public JTextArea gCodeTextArea;
	private JScrollPane scrollPane;
	private JPanel buttonsPanel;
	public JButton saveGCodeButton;
	public JButton clearGCodeButton;


}
