package orientation_detection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ComponentFinder {

	/**
	 * Define Python Script and Path to execute Python OpenCV script
	 * Python OpenCV script captures image of component and calculates orientation
	 */
	public ComponentFinder(){
		processBuilder = new ProcessBuilder("python", "PrintComponentOrientation.py");
		processBuilder.directory(new File("C:\\java\\PnPWorkspace\\PnPMachine\\src\\orientation_detection\\"));
		processBuilder.redirectErrorStream(true);
		xCenter = -1;
		yCenter = -1;
		orientation = -1;
	}
	
	/**
	 * Capture an image of component and calculate the component's orientation and position
	 */
	public boolean captureImageData(){
		Process process;
		try {
			process = processBuilder.start();
			inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
			parseScriptOutput(inputStream.readLine());
			return true;
		} catch (IOException e) {
			System.out.println("ComponentFinder: -> Unable to launch OpenCV script. No image captured.");
			return false;
		}
		
	}
	
	/**
	 * Splits python script's return value and assigns xCenter, yCenter, and orientation accordingly
	 * @param String python script output
	 */
	private void parseScriptOutput(String output){
		String[] returnValues = output.split("[ ,]+");
		if(returnValues.length == 3){
			xCenter = Double.parseDouble(returnValues[0].replace("X", ""));
			yCenter = Double.parseDouble(returnValues[1].replace("Y", ""));
			orientation = Double.parseDouble(returnValues[2]);
		}
		//else no components were found
		//TODO handle no component found error
		//take another image, or home and get new part
		
	}
	
	/**
	 * Get component center's x-coordinate as defined by last call to captureComponentImage()
	 * @return double xCenter
	 */
	public double getXCenter(){
		return xCenter;
	}
	
	/**
	 * Get component center's y-coordinate as defined by last call to captureComponentImage()
	 * @return double yCenter
	 */
	public double getYCenter(){
		return yCenter;
	}
	
	/**
	 * Get component's orientation (angle of rotation) as defined by last call to captureComponentImage()
	 * @return double orientation
	 */
	public double getOrientation(){
		return orientation;
	}
	
	
	private ProcessBuilder processBuilder;
	private BufferedReader inputStream;
	private double xCenter;
	private double yCenter;
	private double orientation;
}
