package com.hp.win.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
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
		protected static RemoteWebDriver MsWordSession = null;
		protected static RemoteWebDriver MsWordFirstSession = null;
		protected static DesiredCapabilities capabilities = null;
		protected static String WindowsApplicationDriverUrl = "http://127.0.0.1:4723/wd/hub";
		protected static final String hex = "0x";
		
		
		// Method to Open Print Queue for Given Printer
		public static void OpenPrintQueue(String printerName) throws IOException {
			
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
		public static void PrintNotePadFile(String ptr_name, String duplex_optn) throws InterruptedException {
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
	    	
	    	OpenPreferences();
	    	
	        ChooseDuplexOrSimplex(duplex_optn);
	    	
/*	    	//Select WiFi Printer
	    	NotepadSession.findElementByName(ptr_name).click();
	    	log.info("Selected Printer Successfully");
	    	Thread.sleep(1000);
	    	
	    	//Tap on print icon (Give Print)
	    	NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Print\")]").click();
	    	log.info("Pressed Print Button Successfully");
	    	Thread.sleep(1000);*/
		}
		
		public static void OpenPreferences() throws InterruptedException{
            NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Preferences\")]").click();
            log.info("Pressed Preferences Button Successfully");
            Thread.sleep(1000);
		}
				
		// Method to select duplex option
		// NOTE: Cannot currently select list items from the combo box's drop down menu
		// Currently the way a duplex option is chosen is with the arrow keys navigating to the selection
		public static void ChooseDuplexOrSimplex(String option) throws InterruptedException {

            String optn = option.toLowerCase();
            String simplex = "None";
            String shortEdge = "Flip on Short Edge";
            String longEdge = "Flip on Long Edge";
            
            // Get the default selection in the combo box
            // This can be used for comparison later, but it's not doing anything yet EMC
            WebElement DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox"); 
            String comboBoxText = DuplexComboBoxView.getText();
            Assert.assertNotNull(DuplexComboBoxView);

            // Select the right duplex option from the drop down menu
		    switch(optn) {
		    case "simplex":
		        Thread.sleep(1000);
		        log.info("Selecting '" + simplex + "'...");
                //DuplexComboBoxView.click();
		        DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox");
		        DuplexComboBoxView.click();
                comboBoxText = DuplexComboBoxView.getText();
		        Assert.assertEquals(comboBoxText, simplex);
		        NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"OK\")]").click();
                Thread.sleep(1000);
		        break;
		        
		    case "long edge":
		        Thread.sleep(1000);
                log.info("Navigating to '" + longEdge + "'...");
		        try {
                NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
                NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
                DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox");
                DuplexComboBoxView.click();
                comboBoxText = DuplexComboBoxView.getText();
                //Assert.assertNotNull(DuplexComboBoxView);
                Assert.assertEquals(comboBoxText, longEdge);
                log.info("Selecting '" + comboBoxText + ".'");
                NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"OK\")]").click();
                Thread.sleep(1000);
                
                } catch(Exception e)
		        {
                    log.info("Can't find '" + longEdge + ".'");
                    e.printStackTrace();
                    log.info("Error selecting duplex option.");     
                    throw new RuntimeException(e);
		        }
            Thread.sleep(1000);
		        break;
		        
		    case "short edge":
		        log.info("Navigating to " + shortEdge + "...");

	            try {
	                NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
	                NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
	                NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
	                DuplexComboBoxView.click();
	                DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox");
	                comboBoxText = DuplexComboBoxView.getText();
	                //Assert.assertNotNull(DuplexComboBoxView);
	                Assert.assertEquals(comboBoxText, shortEdge);
	                log.info("Selecting '" + comboBoxText + ".'");
	                NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"OK\")]").click();
	                Thread.sleep(1000);
	                
	            } catch(Exception e)
	            {
	                log.info("Can't find '" + shortEdge + ".'");
	                e.printStackTrace();
	                log.info("Error selecting duplex option.");     
	                throw new RuntimeException(e);
	            }
	            Thread.sleep(1000);
	            break;
	            
	        default:
	            log.info("Invalid duplex selection. Please use 'simplex,' 'long edge,' or 'short edge.'");
	        }

		    NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Print\")]").click();
		}
		
		
		
		
		// Method to Create Desktop Session
		public static RemoteWebDriver GetDesktopSession(String device_name) throws MalformedURLException {	
			try {
		    capabilities = new DesiredCapabilities();
		    capabilities.setCapability("app","Root");
		    capabilities.setCapability("platformName", "Windows");
	        capabilities.setCapability("deviceName", device_name);
		    DesktopSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
			}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error getting Desktop session");	            
	        	}
			log.info("Desktop session created successfully");
		    return DesktopSession;
		}

						
		// Method to switch from NotePad session to PrintQueue Window
		public static void SwitchToPrinterQueue(String device_name, String ptr_name) throws MalformedURLException {
			try {
			
				DesktopSession = Base.GetDesktopSession(device_name);
			    
			    //Get handle to PrinterQueue window
			    WebElement printerQueueWindow = DesktopSession.findElementByClassName("PrintUI_PrinterQueue");
		    	String nativeWindowHandle = printerQueueWindow.getAttribute("NativeWindowHandle");
		    	int printerQueueWindowHandle = Integer.parseInt(nativeWindowHandle);
		    	log.debug("int value:" + nativeWindowHandle);
		    	String printerQueueTopWindowHandle  = hex.concat(Integer.toHexString(printerQueueWindowHandle));
		    	log.debug("Hex Value:" + printerQueueTopWindowHandle);
	
		    	// Create a PrintQueueSession by attaching to an existing application top level window handle
		    	DesiredCapabilities capabilities = new DesiredCapabilities();
		    	capabilities.setCapability("appTopLevelWindow", printerQueueTopWindowHandle);
		    	capabilities.setCapability("platformName", "Windows");
		        capabilities.setCapability("deviceName", device_name);
		        PrintQueueSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
				}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error getting Print Queue session");	            
	        	}
			
	    	log.info("PrintQueue session created successfully");
	    	
	    	// Ensure correct PrintQueue is opened    	
	    	Assert.assertTrue(PrintQueueSession.findElementByClassName("PrintUI_PrinterQueue").getAttribute("Name").contains(ptr_name));
		    log.info("Opened printer queue is correct for the printer => "+ptr_name);
		}
	

		// Method to open Notepad test file
		public static RemoteWebDriver OpenNoteFile(String device_name, String test_filename) throws MalformedURLException {
			
		  try {
		    	capabilities = new DesiredCapabilities();
		        capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
		        capabilities.setCapability("appArguments",test_filename );
		        capabilities.setCapability("appWorkingDir", testfiles_loc);
		        capabilities.setCapability("platformName", "Windows");
		        capabilities.setCapability("deviceName",device_name);
		        NotepadSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
		        Assert.assertNotNull(NotepadSession);
		        NotepadSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);        		        		         			 
		   		}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error opening notepad file");	            
	        	}
		 log.info("Opened"+test_filename+"file from "+testfiles_loc);
		 return NotepadSession;
	  }

		
		
	  // Method to Get Cortana Session	
	  public static RemoteWebDriver GetCortanaSession(String device_name) throws MalformedURLException {
			
		try {
			DesktopSession = Base.GetDesktopSession(device_name);		
		    //Get handle to Cortana window
		    DesktopSession.getKeyboard().sendKeys(Keys.META + "s" + Keys.META);
		    WebElement CortanaWindow = DesktopSession.findElementByName("Cortana");
	    	String nativeWindowHandle = CortanaWindow.getAttribute("NativeWindowHandle");
	    	int cortanaWindowHandle = Integer.parseInt(nativeWindowHandle);
	    	log.debug("int value:" + cortanaWindowHandle);
	    	String cortanaTopWindowHandle  = hex.concat(Integer.toHexString(cortanaWindowHandle));
	    	log.debug("Hex Value:" + cortanaTopWindowHandle);

	    	// Create a Cortana session by attaching to its existing application top level window handle
	    	DesiredCapabilities appCapabilities = new DesiredCapabilities();
	    	appCapabilities.setCapability("appTopLevelWindow", cortanaTopWindowHandle);		    	
	    	appCapabilities.setCapability("deviceName", device_name);
	    	CortanaSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), appCapabilities);	    	
			}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error getting Cortana session");	            
	        	}
		log.info("Cortana session created successfully");
		return CortanaSession;
	  }
	  
	
	  
	 // Method to open MS Word test file
	 public static RemoteWebDriver OpenMsWordFile(String device_name, String test_filename) throws MalformedURLException, InterruptedException {
			
			   try {
			    	capabilities = new DesiredCapabilities();
			        capabilities.setCapability("app", "C:\\Program Files (x86)\\Microsoft Office\\root\\Office16\\WINWORD.EXE");
			        capabilities.setCapability("appArguments",test_filename );
			        capabilities.setCapability("appWorkingDir", testfiles_loc);
			        capabilities.setCapability("platformName", "Windows");
			        capabilities.setCapability("deviceName",device_name);
			        MsWordFirstSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
			        Assert.assertNotNull(MsWordFirstSession);
			        MsWordFirstSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);      			        
			   		}catch(Exception e){
		            e.printStackTrace();
		            log.info("Error opening MS Word file");	            
		        	}
			    log.info("Opened"+test_filename+"file from "+testfiles_loc);
			    
			    Thread.sleep(3000); 
			    
			    log.info("Relauching MSWord App to Get Main Window Session");
			    
			    try {
			    capabilities = new DesiredCapabilities();
		        capabilities.setCapability("app", "C:\\Program Files (x86)\\Microsoft Office\\root\\Office16\\WINWORD.EXE");
		        capabilities.setCapability("appArguments",test_filename );
		        capabilities.setCapability("appWorkingDir", testfiles_loc);
		        capabilities.setCapability("platformName", "Windows");
		        capabilities.setCapability("deviceName",device_name);
		        MsWordSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
		        Assert.assertNotNull(MsWordSession);
		        MsWordSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);      			        
		   		}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error opening MS Word file");	            
	        	}
			    
			    log.info("Relauching MSWord App Successful and Got Main Window Session");
			   
				return MsWordSession;
			
		}
	 
	 
	 
	 // Method to select desired printer from printers list combo box
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectDesiredPrinter_Msword(String ptr_name) throws MalformedURLException, InterruptedException {
		 
		 	WebElement PrinterListComboBox = MsWordSession.findElementByClassName("NetUIDropdownAnchor");		
	        Assert.assertNotNull(PrinterListComboBox);           
	        if(!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) {
	        log.info("Desired printer is not selected so selecting it from drop down");
	        PrinterListComboBox.click();
	        Thread.sleep(1000);
	        try {
	        	PrinterListComboBox.findElement(By.name(ptr_name)).click();
	        	}catch(Exception e){
	        	log.info("Printer under test is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name correctly in testsuite xml");
	        	e.printStackTrace();
	            log.info("Error selecting printer under test");     
	            throw new RuntimeException(e);
	        	}
	        Thread.sleep(1000);
	        log.info("Selected desired printer =>" +PrinterListComboBox.getText().toString());
	        } else {
	        log.info("Desired printer => " +PrinterListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
		 
	 }
	
	
}
