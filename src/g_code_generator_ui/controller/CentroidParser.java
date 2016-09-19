package g_code_generator_ui.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CentroidParser {

	/**
	 * Constructor assigns Centroid Input File to be parsed
	 * @param centroidInput file to be parsed **REquires complete path
	 */
	public CentroidParser(String centroidInput){
		System.out.println("CentroidParser init()");
		System.out.println(centroidInput);
		inputFile = centroidInput;
		//TODO check for bad input files
	}

	/**
	 * Return String value representing path to Centroid input
	 * @return String inputFile
	 */
	public String getInputFile(){
		return inputFile;
	}
	

	/**
	 * Generates an ArrayList<String> where each element of list contains the attributes of 1 SMT Component
	 * @return ArrayList<String> componentList
	 */
	public List<String> getComponentList(){
		List<String> componentList = null;
		//create file reader 
		try{
			 componentList = Files.readAllLines(Paths.get(inputFile), Charset.defaultCharset());
		}catch(IOException e){
			System.out.println("Unable to open Centroid File for parsing. See error message:");
			System.out.println(e);
		}	
		return componentList;
	}
	
	/**
	 * Parse 1 line of component attributes and generates String array that contains all elements
	 * Accepts white space and comma delimiter
	 * @param component String containing the component's attributes
	 * @param attributeCount Total number of attributes present in String component
	 * @return String[] componentAttributeArray which contains all component attributes
	 */
	public String[] parseComponentAttributes(String component, int attributeCount){
		//TODO use attributeCount to check for invalid input types
		return component.split("[ ,]+");
	}
	
	private String inputFile;
}