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
		protected static RemoteWebDriver PrintSettingConflictSession = null;		
		protected static RemoteWebDriver MsWordFirstSession = null;
		protected static RemoteWebDriver PhotosSession = null;
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
		public static void PrintNotePadFile(String ptr_name, String duplex_optn, String color_optn) throws InterruptedException {
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
	    	
	    	// Open Preferences window
	    	ClickButton(NotepadSession, "Preferences");
	    	
	    	// Select Preferences
	        ChooseDuplexOrSimplex_Notepad(duplex_optn);
	        ChooseColorOrMono_Notepad(color_optn);
	        
	        // Close Preferences window
	        ClickButton(NotepadSession, "OK");
	    	
	    	//Select WiFi Printer
	        log.info("Looking for " + ptr_name + "...");
	    	NotepadSession.findElementByName(ptr_name).click();
	    	log.info("Selected Printer Successfully");
	    	Thread.sleep(1000); 
	    	
	    	//Tap on print icon (Give Print)    	
	    	ClickButton(NotepadSession, "Print");
		}
		
		public static void ClickButton(RemoteWebDriver session, String buttonName) throws InterruptedException{
		    session.findElementByXPath("//Button[starts-with(@Name, \"" + buttonName +"\")]").click();
		    log.info("Clicked '" + buttonName + "' button successfully.");
		    Thread.sleep(1000);
		}
		
		// Method to select the necessary tab to change the print preference options
		public static void SelectPreferencesTab_Notepad(String desiredTab) throws InterruptedException {
		    
		    try {
		        if(NotepadSession.findElementByName(desiredTab).isSelected()) {
		            log.info("'" + desiredTab + "' tab is already selected.");
		        }
		        else {
		            NotepadSession.findElementByName(desiredTab).click();
		            log.info("Successfully clicked on '" + desiredTab + ".'");
		            Thread.sleep(1000);
		        }
		    } catch (Exception e) {
		        log.info("Error: Tab '" + desiredTab + "' doesn't exist. Use 'Layout' or 'Paper/Quality.'");
		        throw new RuntimeException(e);
		    }

		}
		
		
		// Method to select duplex option
		// NOTE: Cannot currently select list items from the combo box's drop down menu
		// A workaround is to use the arrow keys to navigate to the selection
		// This issue has been already reported here: https://github.com/Microsoft/WinAppDriver/issues/389
		public static void ChooseDuplexOrSimplex_Notepad(String option) throws InterruptedException {

            String optn = option.toLowerCase();
            String simplex = "None";
            String shortEdge = "Flip on Short Edge";
            String longEdge = "Flip on Long Edge";
            
            SelectPreferencesTab_Notepad("Layout");
            
            // Create an object for the combo box and get the default selection value
            // This might be used for comparison later, but it's not doing anything yet - EMC
            WebElement DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox"); 
            String comboBoxText = DuplexComboBoxView.getText();
            Assert.assertNotNull(DuplexComboBoxView);

            // Select the correct duplex option from the drop down menu
		    switch(optn) {
		    case "simplex":
		        log.info("Selected '" + simplex + "'...");
		        DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox");
		        DuplexComboBoxView.click();
                comboBoxText = DuplexComboBoxView.getText();
		        Assert.assertEquals(comboBoxText, simplex);
                Thread.sleep(1000);
		        break;
		        
		    case "long edge":
		        
                log.info("Navigating to '" + longEdge + "'...");
		        try {
		            // Tap down arrow twice because the first tap opens the drop down menu
		            NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
		            NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
		            DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox");
		            DuplexComboBoxView.click();
		            Thread.sleep(1000);
		            
		            // Make sure the option we want is selected
		            DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox");
		            comboBoxText = DuplexComboBoxView.getText();
		            Assert.assertEquals(comboBoxText, longEdge);
		            
		            log.info("Selected '" + comboBoxText + ".'");
		            Thread.sleep(1000);
                } catch(Exception e)
		        {
                    log.info("Can't find '" + longEdge + ".'");     
                    throw new RuntimeException(e);
		        }
		        break;
		        
		    case "short edge":
		        
		        log.info("Navigating to " + shortEdge + "...");
	            try {
	                NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
	                NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
	                NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
	                DuplexComboBoxView.click();
	                Thread.sleep(1000);
	                
	                DuplexComboBoxView = NotepadSession.findElementByClassName("ComboBox");
	                comboBoxText = DuplexComboBoxView.getText();

	                Assert.assertEquals(comboBoxText, shortEdge);
	                log.info("Selected '" + comboBoxText + ".'");
	                Thread.sleep(1000);
	                
	            } catch(Exception e)
	            {
	                log.info("Can't find '" + shortEdge + ".'");    
	                throw new RuntimeException(e);
	            }
	            Thread.sleep(1000);
	            break;
	            
	        default:
	            log.info("Invalid duplex selection. Please use 'simplex,' 'long edge,' or 'short edge.'");
	        }
		}
		
		
		public static void ChooseColorOrMono_Notepad(String color_optn) throws InterruptedException {
		    String color = "Color";
		    String mono = "Black && White";
		    String color_choice;
		    
		    String color_sel = color_optn.toLowerCase();
		    
		    if(color_sel.equals("mono")) {
		        color_choice = mono;
		    }
		    else {
		        color_choice = color;
		    }
		    
		    SelectPreferencesTab_Notepad("Paper/Quality");
		    
	          try {
	                if(NotepadSession.findElementByName(color_choice).isSelected()) {
	                    log.info("'" + color_optn + "' is already selected.");
	                }
	                else {
	                    NotepadSession.findElementByName(color_choice).click();
	                    log.info("Successfully clicked on '" + color_choice + ".'");
	                }
	            } catch (Exception e) {
	                log.info("Error: Tab '" + color_choice + "' doesn't exist. Use 'color' or 'mono.'");
	                throw new RuntimeException(e);
	            }
		    
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
					throw new RuntimeException(e);
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
	            throw new RuntimeException(e);
	        	}
		 log.info("Opened"+test_filename+"file from "+testfiles_loc);
		 return NotepadSession;
	  }

		
		
	  // Method to Get Cortana Session	
	  public static RemoteWebDriver GetCortanaSession(String device_name) throws MalformedURLException {
			
		try {
			DesktopSession = Base.GetDesktopSession(device_name);		
		    DesktopSession.getKeyboard().sendKeys(Keys.META + "s" + Keys.META);
		    Thread.sleep(1000);
		    WebElement CortanaWindow = DesktopSession.findElementByName("Cortana");
		    Thread.sleep(1000);
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
	            throw new RuntimeException(e);
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
		            throw new RuntimeException(e);
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
	            throw new RuntimeException(e);
	        	}
			    
			    log.info("Relauching MSWord App Successful and Got Main Window Session");
			   
				return MsWordSession;
			
		}
	 
	 
	 
	 // Method to select desired printer from printers list combo box
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectDesiredPrinter_Msword(String ptr_name) throws MalformedURLException, InterruptedException {
		 
		 	WebElement PrinterListComboBox = MsWordSession.findElementByClassName("NetUIDropdownAnchor");		
	        Assert.assertNotNull(PrinterListComboBox);           
	        if(!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) 
	        {
		        log.info("Desired printer  => "+ptr_name+" <=  is not selected so selecting it from drop down");
		        PrinterListComboBox.click();
		        Thread.sleep(1000);
		        try {
		        	PrinterListComboBox.findElement(By.name(ptr_name)).click();
		        	}catch(Exception e){
		        	log.info("Printer under test is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting printer under test");     
		            throw new RuntimeException(e);
		        	}
		        Thread.sleep(1000);
		        log.info("Selected desired printer *****" +PrinterListComboBox.getText().toString()+"*****");
		    } else {
		    	log.info("Desired printer => " +PrinterListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
		 
	 }
	 
	 // Method to select desired paper size  
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectPaperSize_Msword(String paper_size) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement PaperSizeListComboBox = MsWordSession.findElementByName("Page Size");		 		
	        Assert.assertNotNull(PaperSizeListComboBox);           
	        if(!PaperSizeListComboBox.getText().toString().contentEquals(paper_size)) 
	        {
		        log.info("Desired paper size => "+paper_size+" <= is not selected so selecting it from drop down");
		        
		        // if paper size is not visible then WinAppDriver auto scroll to make list item visible and then selects it.
		        PaperSizeListComboBox.click();
		        Thread.sleep(1000);
		        try {
		        	PaperSizeListComboBox.findElement(By.name(paper_size)).click();		        	
		        	}catch(Exception e){
		        	log.info("Desired Paper Size is not found so make sure Printer Support this paper size OR have typed the paper size name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting desired paper size");     
		            throw new RuntimeException(e);
		        	}
		        Thread.sleep(1000);
		        log.info("Selected desired paper size *****" +PaperSizeListComboBox.getText().toString()+"*****");
		     } else {
		    	log.info("Desired paper size => " +PaperSizeListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
	 }
	 
	 
	 // Method to select desired duplex option
	 public static void SelectDuplexOption_Msword(String duplex_option) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement DuplexListComboBox = MsWordSession.findElementByName("Two-Sided Printing");		 		
	        Assert.assertNotNull(DuplexListComboBox);  
	        DuplexListComboBox.click();
	        Thread.sleep(1000);
	        if(duplex_option.toLowerCase().contains("long"))
	        	{
	        	try {	        		
	        		DuplexListComboBox.findElement(By.name("Print One Sided")).click();
	        		Thread.sleep(1000);
	        		DuplexListComboBox.click();
	        		Thread.sleep(1000);
	        		// Using keys as xpath does not work for longedge as it has only help text as unique property
	        		MsWordSession.getKeyboard().pressKey(Keys.ARROW_DOWN); 
	        		Thread.sleep(1000);
	        		MsWordSession.getKeyboard().pressKey(Keys.ENTER);
	        		
	        		}catch(Exception e){
		            log.info("Error selecting duplex long edge option");
		           	e.printStackTrace();
		            throw new RuntimeException(e);
	        		}
	        	Thread.sleep(1000);
	        	log.info("Selected => ***** Duplex LongEdge ***** Option");
	        	} 
	        else if(duplex_option.toLowerCase().contains("short"))
	        	{
	        	try {
	        		DuplexListComboBox.findElement(By.name("Print One Sided")).click();
	        		Thread.sleep(1000);
	        		DuplexListComboBox.click();
	        		Thread.sleep(1000);
	        		
	        		// Using keys as xpath does not work for shortedge as it has only help text as unique property
	        		MsWordSession.getKeyboard().pressKey(Keys.ARROW_DOWN); 
	        		Thread.sleep(1000);
	        		MsWordSession.getKeyboard().pressKey(Keys.ARROW_DOWN);
	        		Thread.sleep(1000);
	        		MsWordSession.getKeyboard().pressKey(Keys.ENTER);
	        		
	        		}catch(Exception e){
		            log.info("Error selecting duplex short option");
		           	e.printStackTrace();
		            throw new RuntimeException(e);
	        	}
	        	Thread.sleep(1000);
	        	log.info("Selected => ***** Duplex ShortEdge ***** Option");
	        	}
	        else {
	        	try {
	        	DuplexListComboBox.findElement(By.name(duplex_option)).click();
	        	Thread.sleep(1000);
	        	log.info("Selected desired duplex option *****" +DuplexListComboBox.getText().toString()+"*****");
	        	}catch(Exception e){
	        	log.info("Desired duplex option is not found so make sure Printer Support this duplex option OR have typed the duplex option name incorrectly in testsuite xml");
	        	e.printStackTrace();
	            log.info("Error selecting duplex option");     
	            throw new RuntimeException(e);
	        		}
	        	}   
	 }
	 
	 
	 // Method to select desired orientation  
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectOrientation_Msword(String orientation) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement OrientationListComboBox = MsWordSession.findElementByName("Orientation");		 		
	        Assert.assertNotNull(OrientationListComboBox);           
	        if(!OrientationListComboBox.getText().toString().contentEquals(orientation)) 
	        {
		        log.info("Desired duplex option => "+orientation+" <= is not selected so selecting it from drop down");
		        OrientationListComboBox.click();
		        Thread.sleep(1000);
		        try {		        	
		        	OrientationListComboBox.findElement(By.name(orientation)).click();		        	
		        	}catch(Exception e){
		        	log.info("Desired duplex option is not found so make sure Printer Support this duplex option OR have typed the duplex option name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting duplex option");     
		            throw new RuntimeException(e);
		        	}
		        Thread.sleep(1000);
		        log.info("Selected desired duplex option *****" +OrientationListComboBox.getText().toString()+"*****");
		     } else {
		    	log.info("Desired duplex option => " +OrientationListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
	 }
	 
	 
	 
	 // Method to select desired Collation option  
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectCollation_Msword(String collation) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement CollationListComboBox = MsWordSession.findElementByName("Collation");		 		
	        Assert.assertNotNull(CollationListComboBox);           
	        if(!CollationListComboBox.getText().toString().contentEquals(collation)) 
	        {
		        log.info("Desired duplex option => "+collation+" <= is not selected so selecting it from drop down");
		        CollationListComboBox.click();
		        Thread.sleep(1000);
		        try {
		        	CollationListComboBox.findElement(By.name(collation)).click();		        	
		        	}catch(Exception e){
		        	log.info("Desired duplex option is not found so make sure Printer Support this duplex option OR have typed the duplex option name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting duplex option");     
		            throw new RuntimeException(e);
		        	}
		        Thread.sleep(1000);
		        log.info("Selected desired duplex option *****" +CollationListComboBox.getText().toString()+"*****");
		     } else {
		    	log.info("Desired duplex option => " +CollationListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
	 }
	 
	 
	 
	 // Method to select desired Copies option  
	 // Possible candidate for changing approach  
	 public static void SelectCopies_Msword(String copies) throws MalformedURLException, InterruptedException {
		 	
		 	
		 	//Directly working with EditBox "Copies" is erroring out so trying to select working with copies using keys "tab" 
		 	
		 	//Click on Print in the Menu Item and then press 2 Tabs
	        MsWordSession.findElementByXPath("//TabItem[@Name='Print']").click();
	        Thread.sleep(1000);
	        
	        // Press Tab - Once
	        MsWordSession.getKeyboard().pressKey(Keys.TAB);
	        //MsWordSession.getKeyboard().releaseKey(Keys.TAB);	        
	        Thread.sleep(1000);
	        
	        // Press Tab - Twice and you should be on Copies EditBox now
	        MsWordSession.getKeyboard().pressKey(Keys.TAB);
	        //MsWordSession.getKeyboard().releaseKey(Keys.TAB);
	        Thread.sleep(1000);
	        
	        MsWordSession.getKeyboard().pressKey(copies);
	        Thread.sleep(1000);

		 	//Click on Print in the Menu Item (outside Print group screen to save entered copies value)
	        MsWordSession.findElementByXPath("//TabItem[@Name='Print']").click();
	        Thread.sleep(1000);
	        log.info("Entered copies value ***** "+copies+" *****");        	        

	 }

	 

	 // Method to select desired PagesToPrint option  
	 public static void SelectPagesToPrint_Msword(String pages_to_print, String page_count) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement PrintPagesComboBox = MsWordSession.findElementByName("Print What");		 		
	        Assert.assertNotNull(PrintPagesComboBox);
	        
	        if(!PrintPagesComboBox.getText().toString().contentEquals(pages_to_print)) 
	        {
	        	log.info("Desired pages to print option => "+pages_to_print+" <= is not selected so selecting it from drop down");
		        try 
		        {		        		        	
			        if(!page_count.contentEquals("NA"))
			        {
			        	// Go for selecting "Custom Print" and then enter page number
			        	PrintPagesComboBox.click();
			        	Thread.sleep(1000);
			        	PrintPagesComboBox.findElement(By.name(pages_to_print)).click();
			        	Thread.sleep(1000);
			        	log.info("Current selection indicates Custom Page Print so going with this option");
			        	
			        	// Pages combo is already selected with above steps to go ahead and enter page number to print		            
				        MsWordSession.getKeyboard().pressKey(page_count);
				        Thread.sleep(1000);
				        log.info("Entered desired page count value ***** "+page_count+" *****"); 			        	
			        }		
			        
			        else 
			        {	
			        	// Go for selecting non custom Print option from combo box
			        	PrintPagesComboBox.click();
			        	Thread.sleep(1000);
			        	PrintPagesComboBox.findElement(By.name(pages_to_print)).click();
			        	//log.info("Selected desired pages to print option ***** "+pages_to_print+" *****");     	
			        }
		        
		          }catch(Exception e){
			        	log.info("Desired pages to print option is not found so check you have selected relevant test file OR have typed the pages to print option name incorrectly in testsuite xml");
			        	e.printStackTrace();
			            log.info("Error selecting pages to print option");     
			            throw new RuntimeException(e);
			        }
		        Thread.sleep(1000);
		        log.info("Selected desired pages to print option *****" +PrintPagesComboBox.getText().toString()+"*****");
		  } 
	      else 
		  {
		    	log.info("Desired pages to print option => " +PrintPagesComboBox.getText().toString()+" <= is already selected so proceeding");
		  }
	 }
	 


	 // Method to open Photos test file
	 public static RemoteWebDriver OpenPhotosFile(String device_name, String test_filename) throws MalformedURLException {
			
			 try {
		    	    capabilities = new DesiredCapabilities();
		            capabilities.setCapability("app", "Microsoft.Windows.Photos_8wekyb3d8bbwe!App");
		            capabilities.setCapability("appArguments", test_filename);
		            capabilities.setCapability("appWorkingDir", testfiles_loc);
		            capabilities.setCapability("platformName", "Windows");
		            capabilities.setCapability("deviceName", device_name);
		            PhotosSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
		            Assert.assertNotNull(PhotosSession);
		            PhotosSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		            }catch(Exception e){
		            	log.info("Error opening Photos app");	            
		        }
			 
			// log.info("Opened"+test_filename+"file from "+testfiles_loc);
		 	log.info("Opened PhotosSession successfully");
			return PhotosSession;
	    	
		}
	 
	 
	 // Method to print from Photos
		public static void PrintPhoto(String ptr_name, String test_filename) throws InterruptedException {
			// Go to Folders Tab
	    	PhotosSession.findElementByName("Folders").click();
	    	log.info("Clicked on Folder Menu Successfully in PhotoApp");
	    	Thread.sleep(1000);
	    	
	    	// Search for Saved Pictures folder.
            PhotosSession.findElementByName("Search").sendKeys("testfiles");
            log.info("Searching \"Saved Pictures\"");
            Thread.sleep(1000);
            
            // Click on Saved Pictures
            PhotosSession.findElementByXPath("//Button[@Name = \"testfiles\"]").click();
            log.info("Clicked on \"Saved Pictures\"");
            Thread.sleep(1000);
                        
            // Select the Photo file
	    	PhotosSession.findElementByXPath("//Button[@AutomationId = '"+test_filename+"']").click();
	    	log.info("Pressed Save button to Save the File");
	        Thread.sleep(1000);
	        
	        // Tap on Print icon
		    PhotosSession.findElementByName("Print").click();
		    log.info("Clicked on Print Icon Successfully in PhotoApp");
		    Thread.sleep(1000);
		    
    	}
		
		
		 // Method to select desired printer from printers list combo box
		 // Possible candidate for re-factoring when there are multiple application in automation
		 public static void SelectDesiredPrinter_Photos(String ptr_name) throws MalformedURLException, InterruptedException {
			 
			 	WebElement PrinterListComboBox = PhotosSession.findElementByClassName("ComboBox");		
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
