package com.hp.win.utility;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadPropertyFile {
	protected Properties prop = null;
	protected InputStream input = ReadPropertyFile.class.getClassLoader().getResourceAsStream("dataFile.properties");
	
	public ReadPropertyFile() throws IOException {
		prop = new Properties();
		prop.load(input);

	}

//	public String getMsWordLocation() {
//		return prop.getProperty("locationMsWord");
//	}

	
}
