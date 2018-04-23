package com.hp.win.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.hp.win.core.SettingBase;


public class PrintTraceCapture extends SettingBase {
	
	static DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
	private static String providersFileLoc = System.getProperty("user.dir").concat("\\resources\\");
	private static String cmdStart = "logman -ets start WindowsippTrace -pf providers.cfg";
	private static String cmdStop = "logman -ets stop WindowsippTrace";
	private static final Logger log = LogManager.getLogger(PrintTraceCapture.class);
	private static String curTestName = "";
	

	public static void StartLogCollection(String currentClass)
			throws IOException, InterruptedException {
		
		ProcessBuilder pb = new ProcessBuilder("cmd.exe","/k", cmdStart);
		pb.redirectErrorStream(true);
		pb.directory(new File(providersFileLoc));
		Process pc = pb.start();
		log.debug("executed the cmd successfully");
		try {
			InputStreamReader isr = new InputStreamReader(pc.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			while ((br.readLine()) == null) {
				pc.waitFor();
			}	
		} catch (InterruptedException e) {
			pc.destroyForcibly();
		}
		log.info("Started log tracing from PrintTraceCapture \"logman -ets start WindowsippTrace -pf providers.cfg\".");
	}
	
	
	public static void StopLogCollection(String currentClass)
			throws IOException, InterruptedException {
		
		ProcessBuilder pb = new ProcessBuilder("cmd.exe","/k",cmdStop);
		pb.directory(new File(providersFileLoc));
		Process pc = pb.start();
		log.debug("executed the cmd successfully");
		try {
			InputStreamReader isr = new InputStreamReader(pc.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			while ((br.readLine()) == null) {
				pc.waitFor();
			}
		} catch (InterruptedException e) {
			pc.destroyForcibly();
		}
		log.info("Stopped log tracing from PrintTraceCapture \"logman -ets stop WindowsippTrace\".");
		
		PrintTraceSave(currentClass);
			
	}
	
	
	public static void PrintTraceSave(String currentClass){
		
			
		String DirectoryToSavePrintTrace = System.getProperty("user.dir") + "\\logs\\PrintTrace\\"
				+ (dateFormat.format(new Date()).substring(0, 11)) + "\\";
		new File(DirectoryToSavePrintTrace).mkdirs();
		
		curTestName = ScreenshotUtility.getTestName().substring(0, 7);
		
		String destFile = curTestName + "-" +currentClass + "-" + dateFormat.format(new Date()) + ".etl";
		log.info("PrintTrace captured file name: " + destFile);
	
        File srcFile = new File(providersFileLoc + "\\WindowsIppTrace.etl");
        File newFile = new File((DirectoryToSavePrintTrace + destFile));
        
        if(srcFile.renameTo(newFile)){
            log.info("PrintTrace log capture is stored in location: " + newFile);
        }else{
            log.info("Log capturing of PrintTrace failed");
        }

	}
	
}	
	

