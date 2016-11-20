package connection_settings_ui.view;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jssc.SerialPortException;
import jssc_usb.UsbDevice;

/**
 * View that allows user to select from the available Ports
 * @author Justin Johnson
 */
public class SettingsPortSelectionView extends JPanel{

	/**
	 * Constructor defines panel components
	 * @throws SerialPortException 
	 */
	public SettingsPortSelectionView() {
		initUI();
		initButtons();
	}

	/**
	 * Initialize UI Elements of Port Selection View
	 * @throws SerialPortException 
	 */
	private void initUI(){
		//Panel for storing label 'Select Port'
		selectPortLabelPanel = new JPanel();
		selectPortLabelPanel.setLayout(new BoxLayout(selectPortLabelPanel, BoxLayout.X_AXIS));
		selectPortLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		selectPortLabelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		//label
		selectPortLabel = new JLabel("Select Port");
		selectPortLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		selectPortLabel.setMinimumSize(new Dimension(LABEL_WIDTH, selectPortLabel.getPreferredSize().height));
		selectPortLabel.setPreferredSize(new Dimension(LABEL_WIDTH, selectPortLabel.getPreferredSize().height));
		selectPortLabel.setMaximumSize(new Dimension(LABEL_WIDTH, selectPortLabel.getPreferredSize().height));
		selectPortLabelPanel.add(selectPortLabel);
		//combo box panel that displays system's available ports
		comboBoxPanel = new JPanel();
		comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.X_AXIS));
		comboBoxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		comboBoxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		//get list of current available ports and add them to JComboBox
		String[] serialPorts = UsbDevice.findSerialPorts();
		portOptionsDropDown = new JComboBox<String>(serialPorts);
		if(portOptionsDropDown.getItemCount() > 0){
				portOptionsDropDown.setSelectedIndex(0);
		}
		portOptionsDropDown.setPreferredSize(new Dimension(COMBO_BOX_WIDTH, portOptionsDropDown.getPreferredSize().height));
		portOptionsDropDown.setMaximumSize(new Dimension(COMBO_BOX_WIDTH, portOptionsDropDown.getPreferredSize().height));
		portOptionsDropDown.setMinimumSize(new Dimension(COMBO_BOX_WIDTH, portOptionsDropDown.getPreferredSize().height));
		comboBoxPanel.add(portOptionsDropDown);
		//refresh ports button Panel
		refreshButtonPanel = new JPanel();
		refreshButtonPanel.setLayout(new BoxLayout(refreshButtonPanel, BoxLayout.X_AXIS));
		refreshButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		refreshButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		//Refresh Button to update the available ports
		refreshPortsButton = new JButton("Refresh Ports");
		refreshButtonPanel.add(refreshPortsButton);
		//add components to this JPanel
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(selectPortLabelPanel);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
		this.add(comboBoxPanel);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
		this.add(refreshButtonPanel);
	}

	/**
	 * Initialize Button Action Listener
	 */
	private void initButtons(){
		refreshPortsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshPortList();
			}
		});
	}

	/**
	 * Get current value of port selection 
	 * @return String port selected by combo box
	 */
	public String getSelectedPort(){
		return portOptionsDropDown.getSelectedItem().toString();
	}


	/**
	 * Update JComboBox with active port list
	 */
	public void refreshPortList(){
		portList = UsbDevice.findSerialPorts();
		portOptionsDropDown.removeAllItems();
		for(int i = 0; i < portList.length; i++){
			System.out.println(portList[i]);
			portOptionsDropDown.addItem(portList[i]);
		}

	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Variables
	 */
	private JPanel selectPortLabelPanel;
	private JLabel selectPortLabel;
	private JPanel comboBoxPanel;
	private JComboBox<String> portOptionsDropDown;
	private JPanel refreshButtonPanel;
	private JButton refreshPortsButton;
	private String[] portList;
	private final int LABEL_WIDTH = 70;
	private final int COMBO_BOX_WIDTH = 100;
}
