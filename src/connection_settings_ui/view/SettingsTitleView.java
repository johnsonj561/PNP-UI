package connection_settings_ui.view;

import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SettingsTitleView extends JPanel{

	/**
	 * Constructor builds View that contains title
	 * @param title
	 */
	public SettingsTitleView(String title) {
		this.title = title;
		initUI();
	}

	/**
	 * Initialize UI elements for Title view
	 */
	private void initUI(){
		titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 5));
		titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		titlePanel.add(titleLabel);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(titlePanel);
	}

	/**
	 * Updates the input's text field with new value
	 * @param inpuText of the file selected by user
	 */
	public void setTitle(String inpuText){
		titleLabel.setText(inpuText);
	}

	/**
	 * Get input path from Text Field
	 * @return String input path
	 */
	public String getTitle(){
		return titleLabel.getText();
	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	private String title;
	private JPanel titlePanel;
	private JLabel titleLabel;
}
