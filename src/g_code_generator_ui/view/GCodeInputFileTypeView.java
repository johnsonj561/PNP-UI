package g_code_generator_ui.view;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * GCodeInputFileTypeView allows user to select input file type (Eagle or Altium)
 * @author Justin Johnson
 */
public class GCodeInputFileTypeView extends JPanel{


	/**
	 * Constructor creates JPanel that displays file type chooser to allow
	 * user to select a file type: Altium or Eagle
	 */
	public GCodeInputFileTypeView() {
		initUI();
	}

	/**
	 * Initialize UI elements of G Code Input File view
	 */
	private void initUI(){
		//create main panel to store components, components to be laid out horizontally across x axis
		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//align components to let side of panel and assign empty border(padding)
		//label directing user to select file type
		selectFileTypeLabel = new JLabel("Select centroid file type:");
		//drop down menu to allow user to select file type, Eagle or Altium
		String[] fileTypes = {"Altium", "Eagle"};
		fileTypeDropDown = new JComboBox<String>(fileTypes);
		fileTypeDropDown.setSelectedIndex(0);
		fileTypeDropDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb= (JComboBox<String>)e.getSource();
				String fileType = cb.getSelectedItem().toString();
				System.out.println("Input File Type: " + fileType);
			}
		});

		mainPanel.add(selectFileTypeLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		mainPanel.add(fileTypeDropDown);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(mainPanel);
	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Get current value of JComboBox where
	 * @return int value of JComboBox (Eagle = 1, Altium = 2, Default = -1)
	 */
	public int getJComboBoxValue(){
		if(fileTypeDropDown.getSelectedItem().equals("Eagle")){
			return 1;
		}
		else if(fileTypeDropDown.getSelectedItem().equals("Altium")){
			return 2;
		}
		return -1;
	}

	/**
	 * Class Variables
	 */
	private JPanel mainPanel;
	private JLabel selectFileTypeLabel;
	private JComboBox<String> fileTypeDropDown;
}
