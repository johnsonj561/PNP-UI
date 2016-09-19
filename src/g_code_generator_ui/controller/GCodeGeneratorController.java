package g_code_generator_ui.controller;

import g_code_generator_ui.view.GCodeEditorView;
import g_code_generator_ui.view.GCodeInputFileTypeView;
import g_code_generator_ui.view.GCodeInputFileView;
import g_code_generator_ui.view.GenerateGCodeButtonView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import connection_settings_ui.view.SettingsTitleView;

/**
 * GCodeGeneratorPanel allows user to select/enter PCB Centroid and Part input files,
 * produce corresponding G-Code output files, and store G-Code output files locally
 * GCodeGeneratorPanel is intended for use with Pick and Place Machine
 * @author Justin Johnson
 *
 */
public class GCodeGeneratorController extends JPanel{

	/**
	 * Default Constructor creates and displays all G Code Generator Panel components
	 */
	public GCodeGeneratorController(){
		initUI();
		initButtons();
	}

	/**
	 * Initialize all G Code Generator UI Components
	 * Views: GCodeInputFileView, GCodeInputFileTypeView,GCodeEditorView
	 */
	private void initUI(){
		//Dimensions Title
		gCodeGeneratorTitle = new SettingsTitleView("Create G Code Instructions From PCB Design");
		gCodeGeneratorTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Create instance of Centroid and Parts Input File Chooser Panels
		mCentroidInput = new GCodeInputFileView("Select an Altium or Eagle Centroid input file:");
		mCentroidInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		mPartsInput = new GCodeInputFileView("Select a corresponding parts file:");
		mPartsInput.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Create instance of Process Input Panel
		mProcessInputPanel = new GCodeInputFileTypeView();
		mProcessInputPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		mGenerateGCodeButtonPanel = new GenerateGCodeButtonView();
		mGenerateGCodeButtonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Create instance of G Code Editor panel
		//panel includes clear/save buttons to allow user to store G-Code output
		mGCodeEditorPanel = new GCodeEditorView();
		mGCodeEditorPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//start with empty gCodeOutput string
		gCodeOutput = "";
		//Add individual components to this.JPanel for final display, laid on vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		this.add(gCodeGeneratorTitle);
		this.add(mCentroidInput);
		this.add(mPartsInput);
		this.add(mProcessInputPanel);
		this.add(mGenerateGCodeButtonPanel);
		this.add(mGCodeEditorPanel);
	}

	/**
	 * Initialize GCodeGenerator Buttons
	 * 
	 */
	private void initButtons(){
		//Generate G Code Button creates G Code from input centroid files and appends it to Editor
		mGenerateGCodeButtonPanel.generateGCodeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Generate G Code Clicked!");
				writeGCodeToEditor();
			}
		});
		//Save G Code Button opens Save As File Dialog and writes all text from Editor to output file of choice
		mGCodeEditorPanel.saveGCodeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Save Button Clicked!");
				writeConsoleToFile();
			}
		});
		mGCodeEditorPanel.clearGCodeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Clear Button Clicked!");
				mGCodeEditorPanel.clearOutput();
			}
		});
	}

	/**
	 * Generate G Code from input files and write G Code to Editor for display
	 */
	private void writeGCodeToEditor(){
		if(mPartsInput.getInputPath().length() > 0 && mCentroidInput.getInputPath().length() > 0){
			GCodeGenerator mGCodeGenerator = new GCodeGenerator(
					mCentroidInput.getInputPath(), mPartsInput.getInputPath(), mProcessInputPanel.getJComboBoxValue());
			if(mGCodeGenerator.isValidInput()){
				gCodeOutput = mGCodeGenerator.initializeGCode();
				gCodeOutput += mGCodeGenerator.generateGCode();
				mGCodeEditorPanel.writeToEditor(gCodeOutput);
			}
			else{
				mGCodeEditorPanel.writeToEditor("Error Parsing Input Files" +
						"\nVerify that Centroid and Parts Files Have Matching File Types (Altium vs Eagle)");
			}
		}
		else{
			mGCodeEditorPanel.writeToEditor("Please select valid input file paths");
		}

	}

	/**
	 * Write text from G-Code Editor to output file and save locally as text file
	 */
	public void writeConsoleToFile(){
		FileNameExtensionFilter mExtensionFilter = new FileNameExtensionFilter("Text File", "txt");
		JFileChooser mSaveAsFileChooser = new JFileChooser();
		mSaveAsFileChooser.setApproveButtonText("Save");
		mSaveAsFileChooser.setFileFilter(mExtensionFilter);
		int actionDialog = mSaveAsFileChooser.showOpenDialog(this);
		if (actionDialog != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = mSaveAsFileChooser.getSelectedFile();
		if (!file.getName().endsWith(".txt")) {
			file = new File(file.getAbsolutePath() + ".txt");
		}

		BufferedWriter outFile = null;
		try {
			outFile = new BufferedWriter(new FileWriter(file));
			mGCodeEditorPanel.gCodeTextArea.write(outFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (outFile != null) {
				try {
					outFile.close();
				} catch (IOException e) {}
			}
		}
	}

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	private SettingsTitleView gCodeGeneratorTitle;
	private GCodeInputFileView mCentroidInput;
	private GCodeInputFileView mPartsInput;
	private GCodeInputFileTypeView mProcessInputPanel;
	private GenerateGCodeButtonView mGenerateGCodeButtonPanel;
	private GCodeEditorView mGCodeEditorPanel;
	private String gCodeOutput;
}
