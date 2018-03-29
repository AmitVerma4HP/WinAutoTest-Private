package com.hp.win.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
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
		protected static RemoteWebDriver CortanaSession = null;
		protected static DesiredCapabilities capabilities = null;
		protected static String WindowsApplicationDriverUrl = "http://127.0.0.1:4723/wd/hub";
		protected static final String hex = "0x";
		
		public static void OpenPrintQueue(String printerName) throws IOException {
			
			try {		
				process = new ProcessBuilder("rundll32.exe","printui.dll","PrintUIEntry","/o","/n",printerName).start();
				Base.startBuilder(process);
				log.info("Opened printer queue => " +printerName);
				
	    		} catch (Exception e) {
				log.info("Error Occured while getting device property");
	    			System.out.println("Error occurred while getting device property.\n");
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
			
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ** WORKAROUND -- the named file is not opening when the session is started - this will create new file content
		// in order to continue the test - it will save in Notepad's default directory (the last one used)
			NotepadSession.getKeyboard().sendKeys("************************ THIS IS THE NOTEPAD TEST FILE *****************************************\n");
			Thread.sleep(1000);
		// ** END WORKAROUND
	    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			// Go to file Menu
	    	NotepadSession.findElementByName("File").click();
	    	log.info("Clicked on File Menu in Notepad");
	    	Thread.sleep(1000);
	    	
	    	// Save the file
	    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Save\")]").click();
	        log.info("Pressed Save button to Save the File");
	        Thread.sleep(1000);
	        
	    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    // ** WORKAROUND -- the named file is not opening when the session is started - this will create a new file name
	    // in order to continue the test
	        NotepadSession.getKeyboard().sendKeys("NotepadTestFile1.txt");
	        Thread.sleep(1000);
	        NotepadSession.findElementByName("Save").click();
	        Thread.sleep(1000);
	        
	        //choose to save over the existing file
	        if(NotepadSession.findElementByName("Yes").isEnabled()) {
	        	NotepadSession.findElementByName("Yes").click();
	        	Thread.sleep(1000);
	        }
	    // ** END WORKAROUND
	    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	        
	        // Go to file Menu
		    NotepadSession.findElementByName("File").click();
		    log.info("Clicked on File Menu in Notepad");
		    Thread.sleep(1000);
		    	
	    	// Tap on Print
	    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Print\")]").click();
	    	log.info("Clicked on File -> Print option Successfully");
	    	Thread.sleep(1000);
	    	

	    	
	    	// If PUT is the previously selected printer, click 'print'
	    	if(NotepadSession.findElementByName(ptr_name).isSelected()) {
	    		NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Print\")]").click();
	    		System.out.println("Pressed Print Button Successfully");
	    		//NotepadSession.findElementByName("Print").click();
	    		Thread.sleep(1000);
	    	}
	    	else {
		    	// If PUT is not the previously selected printer, select it and click 'print'
		    	NotepadSession.findElementByName(ptr_name).click();
		    	log.info("Selected Printer Successfully\n");
		    	Thread.sleep(1000);
		    	
		    	//NotepadSession.findElementByName("Print").click();
		    	NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Print\")]").click();
	    		log.info("Pressed Print Button Successfully");
	    		System.out.println("Pressed Print Button Successfully\n");
	    		Thread.sleep(1000);
	    	}
/*
	    	//Tap on print icon (Give Print)
	    	NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Print\")]").click();
	    	log.info("Pressed Print Button Successfully");
	    	Thread.sleep(1000);*/
		}
		
	
	
		public static void SwitchToPrinterQueue(String device_name, String ptr_name) throws MalformedURLException {
			  
			System.out.println("Creating desktop session...");
			// Create Desktop session 
		    capabilities = new DesiredCapabilities();
		    capabilities.setCapability("app","Root");
		    capabilities.setCapability("platformName", "Windows");
	        capabilities.setCapability("deviceName", device_name);
		    DesktopSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);

		    
		    System.out.println("Getting printer queue window handle...");
		    //Get handle to PrinterQueue window
		    WebElement printerQueueWindow = DesktopSession.findElementByClassName("PrintUI_PrinterQueue");
	    	String nativeWindowHandle = printerQueueWindow.getAttribute("NativeWindowHandle");
	    	int printerQueueWindowHandle = Integer.parseInt(nativeWindowHandle);
	    	log.debug("int value:" + nativeWindowHandle);
	    	System.out.println("int value:" + nativeWindowHandle);
	    	String printerQueueTopWindowHandle  = hex.concat(Integer.toHexString(printerQueueWindowHandle));
	    	log.debug("Hex Value:" + printerQueueTopWindowHandle);
	    	System.out.println("Hex Value:" + printerQueueTopWindowHandle);

/*	          System.out.println("Getting printer queue window handle...");
	            //Get handle to PrinterQueue window
	            try {
	                WebElement printerQueueWindow = DesktopSession.findElementByClassName("PrintUI_PrinterQueue");
	                String nativeWindowHandle = printerQueueWindow.getAttribute("NativeWindowHandle");
	                int printerQueueWindowHandle = Integer.parseInt(nativeWindowHandle);
	                log.debug("int value:" + nativeWindowHandle);
	                System.out.println("int value:" + nativeWindowHandle);
	                String printerQueueTopWindowHandle  = hex.concat(Integer.toHexString(printerQueueWindowHandle));
	            log.debug("Hex Value:" + printerQueueTopWindowHandle);
	                System.out.println("Hex Value:" + printerQueueTopWindowHandle);
	            } catch (Exception e) {
	                  System.out.println("Failed to get the printer queue window handle...");
	            }*/
	    	
	    	System.out.println("Creating a print queue session...");
	    	// Create a PrintQueueSession by attaching to an existing application top level window handle
	    	DesiredCapabilities capabilities = new DesiredCapabilities();
	    	capabilities.setCapability("appTopLevelWindow", printerQueueTopWindowHandle);
	    	capabilities.setCapability("platformName", "Windows");
	        capabilities.setCapability("deviceName", device_name);
	        PrintQueueSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
	    	log.info("PrintQueue session created successfully");
	    	System.out.println("PrintQueue session created successfully");
	    	
	    	// Ensure correct PrintQueue is opened    	
	    	Assert.assertTrue(PrintQueueSession.findElementByClassName("PrintUI_PrinterQueue").getAttribute("Name").contains(ptr_name));
		    log.info("Open printer queue is correct for the printer => "+ptr_name);
	    	System.out.println("Open printer queue is correct for the printer => "+ptr_name);
		    
		}
	

		
		public static RemoteWebDriver OpenNoteFile(String device_name, String test_filename) throws MalformedURLException {
			
		   try {
		    	capabilities = new DesiredCapabilities();
		        capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
		        capabilities.setCapability("appArguments", testfiles_loc + test_filename );
		        capabilities.setCapability("appWorkingDir", testfiles_loc);
		        capabilities.setCapability("platformName", "Windows");
		        capabilities.setCapability("deviceName",device_name);
		        NotepadSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
		        Assert.assertNotNull(NotepadSession);
		        NotepadSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);            
		        log.info("Opened "+test_filename+" file from "+testfiles_loc);
		         	
			 
		   		}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error opening notepad file");	            
	        	}
			
			return NotepadSession;
		
		}

		
		public static RemoteWebDriver GetCortanaSession(String device_name) throws MalformedURLException {
			
		try {
		    DesiredCapabilities capabilities = new DesiredCapabilities();
		    capabilities.setCapability("app","Root");
	        capabilities.setCapability("deviceName", device_name);
		    RemoteWebDriver DesktopSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
		
		    //Get handle to Cortana window
		    DesktopSession.getKeyboard().sendKeys(Keys.META + "s" + Keys.META);
		    WebElement CortanaWindow = DesktopSession.findElementByName("Cortana");
	    	String nativeWindowHandle = CortanaWindow.getAttribute("NativeWindowHandle");
	    	int cortanaWindowHandle = Integer.parseInt(nativeWindowHandle);
	    	log.debug("int value:" + cortanaWindowHandle);
	    	String cortanaTopWindowHandle  = hex.concat(Integer.toHexString(cortanaWindowHandle));
	    	log.debug("Hex Value:" + cortanaTopWindowHandle);

	    	// Create a PrintQueueSession by attaching to an existing application top level window handle
	    	DesiredCapabilities appCapabilities = new DesiredCapabilities();
	    	appCapabilities.setCapability("appTopLevelWindow", cortanaTopWindowHandle);		    	
	    	appCapabilities.setCapability("deviceName", device_name);
	    	CortanaSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), appCapabilities);
	    	log.info("Cortana session created successfully");
	    	
			 
			}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error getting Cortana session");	            
	        	}
			
			return CortanaSession;
		
		}
	
	
}
