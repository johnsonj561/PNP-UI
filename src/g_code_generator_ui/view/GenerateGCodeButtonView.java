package g_code_generator_ui.view;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * GenerageGCodeButtonView is JPanel that displays Generate G Code Button to user
 * @author Justin Johnson
 */
public class GenerateGCodeButtonView extends JPanel{

	/**
	 * Constructor places Generate G Code button in UI display
	 */
	public GenerateGCodeButtonView() {
		initUI();
	}
	
	/**
	 * Initialize UI Elements for Generate G Code Button view
	 */
	private void initUI(){
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		generateGCodeButton = new JButton("Generate G Code");
		mainPanel.add(generateGCodeButton);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(mainPanel);
	}
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	private JPanel mainPanel;
	public JButton generateGCodeButton;
}
