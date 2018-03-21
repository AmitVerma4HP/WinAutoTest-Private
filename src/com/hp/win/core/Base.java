package com.hp.win.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

import java.io.InputStream;



public class Base {
		protected static Process process;
		protected static InputStream is;
		protected static InputStreamReader isr;
		protected static BufferedReader br;
		private static final Logger log = LogManager.getLogger(Base.class);
		protected static String testfiles_loc = System.getProperty("user.dir").concat("\\testfiles\\");
		protected static RemoteWebDriver NotepadSession = null;
		protected static RemoteWebDriver DesktopSession = null;
		protected static RemoteWebDriver PrintQueueSession = null;
		protected static DesiredCapabilities capabilities = null;
		protected static String WindowsApplicationDriverUrl = "http://127.0.0.1:4723/wd/hub";
		public static void openPrintQueue(String printerName) throws IOException {
			
			try {		
				process = new ProcessBuilder("rundll32.exe","printui.dll","PrintUIEntry","/o","/n",printerName).start();
				Base.startBuilder(process);
				log.info("Opened printer queue => " +printerName);
				
	    		} catch (Exception e) {
				log.info("Error Occured while getting device property");
				e.printStackTrace();

	    		}

			}
	    
	 
		// Method to start the process builder
		public static BufferedReader startBuilder(Object process) throws IOException {
			is = ((Process) process).getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			return br;

		}
		
		
		
	  // Method to print from Notepad
		public static void PrintNotePadFile(String ptr_name) throws InterruptedException {
			// Go to file Menu
	    	NotepadSession.findElementByName("File").click();
	    	log.info("Clicked on File Menu in Notepad");
	    	Thread.sleep(1000);
	    	
	    	// Save the file
	    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Save\")]").click();
	        log.info("Pressed Save button to Save the File");
	        Thread.sleep(1000);
	         
	        // Go to file Menu
		    NotepadSession.findElementByName("File").click();
		    log.info("Clicked on File Menu in Notepad");
		    Thread.sleep(1000);
		    	
	    	// Tap on Print
	    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Print\")]").click();
	    	log.info("Clicked on File -> Print option Successfully");
	    	Thread.sleep(1000);
	    	
	    	//Select WiFi Printer
	    	NotepadSession.findElementByName(ptr_name).click();
	    	log.info("Selected Printer Successfully");
	    	Thread.sleep(1000);
	    	
	    	//Tap on print icon (Give Print)
	    	NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Print\")]").click();
	    	log.info("Pressed Print Button Successfully");
	    	Thread.sleep(1000);
		}
		
	
	
		public static void SwitchToPrinterQueue(String device_name, String ptr_name) throws MalformedURLException {
			  // Create Desktop session 
		    capabilities = new DesiredCapabilities();
		    capabilities.setCapability("app","Root");
		    capabilities.setCapability("platformName", "Windows");
	        capabilities.setCapability("deviceName", device_name);
		    DesktopSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);

		    
		    //Get handle to PrinterQueue window
		    WebElement printerQueueWindow = DesktopSession.findElementByClassName("PrintUI_PrinterQueue");
	    	String nativeWindowHandle = printerQueueWindow.getAttribute("NativeWindowHandle");
	    	int printerQueueWindowHandle = Integer.parseInt(nativeWindowHandle);
	    	log.debug("int value:" + nativeWindowHandle);
	    	String printerQueueTopWindowHandle  = ("0x" + Integer.toHexString(printerQueueWindowHandle));
	    	log.debug("Hex Value:" + printerQueueTopWindowHandle);

	    	// Create a PrintQueueSession by attaching to an existing application top level window handle
	    	DesiredCapabilities capabilities = new DesiredCapabilities();
	    	capabilities.setCapability("appTopLevelWindow", printerQueueTopWindowHandle);
	    	capabilities.setCapability("platformName", "Windows");
	        capabilities.setCapability("deviceName", device_name);
	        PrintQueueSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
	    	log.info("PrintQueue session created successfully");
	    	
	    	// Ensure correct PrintQueue is opened    	
	    	Assert.assertTrue(PrintQueueSession.findElementByClassName("PrintUI_PrinterQueue").getAttribute("Name").contains(ptr_name));
		    log.info("Open printer queue is correct for the printer => "+ptr_name);
		}
	
	
	
}
