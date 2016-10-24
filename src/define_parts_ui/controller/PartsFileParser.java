package define_parts_ui.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import define_parts_ui.model.Part;

public class PartsFileParser {

	/**
	 * Constructor assigns input file and parses Part data
	 * @param inputPath
	 */
	public PartsFileParser(String inputPath) {
		partInputPath = inputPath;
		partsList = makePartStringList();
	}

	/**
	 * Return String value representing path to parts file
	 * @return String inputFile
	 */
	public String getPartFilePath(){
		return partInputPath;
	}

	/**
	 * Generates an List<String> where each element of list contains the features of 1 part
	 * @return ArrayList<String> partList
	 */
	public List<String> makePartStringList(){
		List<String> partList = null;
		try{
			 partList = Files.readAllLines(Paths.get(partInputPath), Charset.defaultCharset());
		}catch(IOException e){
			System.out.println("Unable to open Part File for parsing. See error message:");
			System.out.println(e);
		}	
		return partList;
	}
	
	/**
	 * Return ArrayList of all parts, where each part is defined by comma separated list of features
	 * @return List<String> partsList
	 */
	public List<String> getPartStringList(){
		return partsList;
	}
	
	/**
	 * Parse 1 line from parts file, corresponding to data for 1 part
	 * Accepts white space and comma delimiter
	 * @param part String containing the component's attributes
	 * @return String[] which contains all part attributes
	 */
	public String[] parsePartAttributes(String part){
		return part.split("[ ,]+");
	}
	
	/**
	 * Build an ArrayList of type Part that contains all parts found at partInputPath
	 * @return ArrayList<Part>
	 */
	public ArrayList<Part> makePartsArray(){
		ArrayList<Part> partArray = new ArrayList<Part>();
		for(String part : partsList){
			String[] features = parsePartAttributes(part);
			partArray.add(new Part(Integer.parseInt(features[0]), features[1], features[2], 
					Double.parseDouble(features[3]), Double.parseDouble(features[4]), Double.parseDouble(features[5]),
					Double.parseDouble(features[6]), features[7]));
		}
		return partArray;
	}
	

	private String partInputPath;
	private List<String> partsList;
}
