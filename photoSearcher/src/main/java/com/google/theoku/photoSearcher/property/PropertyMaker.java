package com.google.theoku.photoSearcher.property;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertyMaker {
	
	public static void main(String[] args) throws ConfigurationException, IOException {
		File aFile = new File("photoSearcher.properties");
		aFile.createNewFile();
		PropertiesConfiguration config = 
				new PropertiesConfiguration(aFile);
		
		config.setProperty("mongoDB", "photos");
		config.setProperty("mongoCollection", "photoEntries");
		config.setProperty("mongoHost", "127.0.0.1");
		config.setProperty("mongoPort", 27017);
		
		config.save();
	}

}
