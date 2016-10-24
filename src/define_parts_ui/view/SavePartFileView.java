package define_parts_ui.view;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Justin Johnson
 *
 */
public class SavePartFileView extends JPanel{

	/**
	 * Constructor creates instance of GCodeEditorView
	 */
	public SavePartFileView() {
		initUI();
	}
	
	/**
	 * Initialize G Code Editor window's UI Elements
	 */
	private void initUI(){
		//create panel for import/export part file buttons
		buttonsPanel = new JPanel();
		//use flow layout and align buttons to right
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		//save button functionality
		importPartFileButton = new JButton("Import Part File");
		//clear button functionality
		exportPartFileButton = new JButton("Export Part File");
		//add buttons to button panel
		buttonsPanel.add(exportPartFileButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonsPanel.add(importPartFileButton);
		//components of GCodeEditorPanel will be laid out along Y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//add components to this.JPanel\
		this.add(buttonsPanel);
	}
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	private JPanel buttonsPanel;
	public JButton importPartFileButton;
	public JButton exportPartFileButton;


}
