import import_g_code_ui.controller.UploadGCodeController;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GCodeUploadFileApp {

	public GCodeUploadFileApp() {
		System.out.println("Generating instance of G Code Upload File UI");
		initUI();
	}

	private void initUI(){
		//declare style
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Declare main JFrame and Default Close Operation
		mainFrame = new JFrame("PnP Machine Interface");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//create instance of upload g code panel and align
		UploadGCodeController mUploadGCodePanel = new UploadGCodeController();
		mUploadGCodePanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);

		//add panel to JFramefor and display
		mainFrame.add(mUploadGCodePanel);
		mainFrame.pack();
		mainFrame.setVisible(true);

	}

	/**
	 * GCodeUploadFileApp creates instance of Upload File UI for testing purposes
	 * @param args
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				GCodeUploadFileApp UI = new GCodeUploadFileApp();
			}
		});	
	}

	private JFrame mainFrame;

}
