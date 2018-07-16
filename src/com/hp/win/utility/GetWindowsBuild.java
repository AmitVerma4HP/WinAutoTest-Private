package com.hp.win.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.hp.win.core.SettingBase;


public class GetWindowsBuild {
	
	
	private static String cmdLine = "systeminfo | findstr /B /C:\"OS Version\"";
	private static final Logger log = LogManager.getLogger(GetWindowsBuild.class);
	public static Process pc;
	public static InputStream is;
	public static InputStreamReader isr;
	public static BufferedReader br;
	public static String line;
	public static String winbuildverinfo = null;
	//public static List <String> winbuildver = new ArrayList<String>();
	

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
		log.info ("Windows Build Version Info => "+winbuildverinfo);		
		
	}
}	
	

