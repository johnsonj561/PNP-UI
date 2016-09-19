package import_g_code_ui.view;
import java.awt.Color;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

/**
 * CommandConsoleView creates instance of JPanel object that contains a large text editor window with
 * scroll bar. Panel also contains buttons to Start, Pause, and Terminate current job.
 * @author Justin Johnson
 *
 */
public class CommandConsoleView extends JPanel{

	/**
	 * Constructor declares and configures individual components
	 */
	public CommandConsoleView() {
		initUI();
	}

	/**
	 * Initialize UI Elements of the G Code command console
	 */
	private void initUI(){
		//create Panel for text field (G Code Editor)
		textAreaPanel = new JPanel();
		textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.Y_AXIS));
		textAreaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		gCodeTextArea = new JTextArea(20, 40);
		scrollPane = new JScrollPane(gCodeTextArea); 
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaPanel.add(scrollPane);	
		//create panel for Start and Stop Job Buttons
		buttonsPanel = new JPanel();
		//use flow layout and align buttons to right
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		//save button functionality
		startJobButton = new JButton("Start Job");
		//pause current job
		pauseJobButton = new JButton("Pause");
		pauseJobButton.setEnabled(false);
		//clear button functionality
		stopJobButton = new JButton("Terminate");
		stopJobButton.setEnabled(false);
		//add buttons to button panel
		buttonsPanel.add(startJobButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		buttonsPanel.add(pauseJobButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		buttonsPanel.add(stopJobButton);
		//components of GCodeEditorPanel will be laid out along Y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//add components to this.JPanel
		this.add(textAreaPanel);
		this.add(buttonsPanel);
	}
	
	/**
	 * Write String to G Code Console
	 * @param String s to write to console
	 */
	public void writeToConsole(String s){
		gCodeTextArea.setText(s);
	}

	/**
	 * Get content from G Code Console
	 * @return String containing all G Code from Console
	 */
	public String getConsoleContent(){
		return gCodeTextArea.getText();
	}

	/**
	 * Highlights a given line number of Console
	 * @param int lineNumber the line to be highlighted, begins with 0 index
	 */
	public void highlightLine(int lineNumber){
		if(lineNumber >= 0 && lineNumber <= gCodeTextArea.getLineCount()){
			DefaultHighlighter highlighter =  (DefaultHighlighter)gCodeTextArea.getHighlighter();
			DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
			highlighter.setDrawsLayeredHighlights(false);
			try
			{
				int start =  gCodeTextArea.getLineStartOffset(lineNumber);
				int end =    gCodeTextArea.getLineEndOffset(lineNumber);
				highlighter.addHighlight(start, end, painter );
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}

	/**
	 * Removes the first line of text from the console
	 */
	public String removeFirstLineFromConsole(){
		int endOffset = 0;
		String firstLine = "";
		try {
			endOffset = gCodeTextArea.getLineEndOffset(0);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		try {
			firstLine = gCodeTextArea.getDocument().getText(0, endOffset);
			gCodeTextArea.getDocument().remove(0, endOffset);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return firstLine;
	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class variables
	 */
	private JPanel textAreaPanel;
	private JScrollPane scrollPane;
	private JPanel buttonsPanel;
	public JTextArea gCodeTextArea;
	public JButton startJobButton;
	public JButton pauseJobButton;
	public JButton stopJobButton;




}
