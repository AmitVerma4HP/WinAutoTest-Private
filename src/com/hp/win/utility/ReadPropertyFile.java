/*
Copyright 2018 Mopria Alliance, Inc.
Copyright 2018 HP Development Company, L.P.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


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
