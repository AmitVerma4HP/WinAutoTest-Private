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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class GetWindowsBuild {
	
	
	private static String cmdLine = "systeminfo | findstr /B /C:\"OS Version\"";
	private static final Logger log = LogManager.getLogger(GetWindowsBuild.class);
	public static Process pc;
	public static InputStream is;
	public static InputStreamReader isr;
	public static BufferedReader br;
	public static String line;
	public static String winbuildverinfo = null;
	

	public static String GetWindowsBuildInfo() throws IOException, InterruptedException {
		
		try {
			ProcessBuilder pb = new ProcessBuilder("cmd.exe","/c",cmdLine);
			pb.redirectErrorStream(true);		
			pc = pb.start();
			log.debug("executed the cmd successfully");		
				InputStreamReader isr = new InputStreamReader(pc.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				while ((line = br.readLine()) != null) {						
						String winbuildver[] = line.split(":");
						winbuildverinfo = winbuildver[1].toString().toUpperCase().trim();													
					}						
					br.close();
				} catch (Exception e) {
				  log.info("Error getting WinBuildVer");				  
				  pc.destroyForcibly();
			}		
		return winbuildverinfo;  
	}
	
	public static void PrintWindowsBuildInfo() throws IOException, InterruptedException {
		log.info("**********************************************************");
		log.info("Windows Build Version Info => "+winbuildverinfo);
		log.info("**********************************************************");
		
	}
}	
	

