package com.hp.win.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;



public class SettingBase extends Base {
		private static final Logger log = LogManager.getLogger(SettingBase.class);
		protected static RemoteWebDriver CortanaSession = null;
		public static RemoteWebDriver SettingSession = null;
		static WebDriverWait wait;		
		static int MethodCalledCount = 0;
		
		
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
	    	Assert.assertNotNull(CortanaSession);
			}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error getting Cortana session");
	            throw new RuntimeException(e);
	        	}
		log.info("Cortana session created successfully");
		return CortanaSession;
	  }
	  
	  
	  
	  public static RemoteWebDriver GetSettingSession(String device_name) throws MalformedURLException {
			
			try {
				DesktopSession = Base.GetDesktopSession(device_name);		
			    Thread.sleep(1000);
			    WebElement SettingWindow = DesktopSession.findElementByName("Settings");
			    Thread.sleep(1000);
			    String nativeWindowHandle = SettingWindow.getAttribute("NativeWindowHandle");
		    	int settingWindowHandle = Integer.parseInt(nativeWindowHandle);
		    	log.debug("int value:" + settingWindowHandle);
		    	String settingTopWindowHandle  = hex.concat(Integer.toHexString(settingWindowHandle));
		    	log.debug("Hex Value:" + settingTopWindowHandle);

		    	// Create a Setting session by attaching to its existing application top level window handle
		    	DesiredCapabilities appCapabilities = new DesiredCapabilities();
		    	appCapabilities.setCapability("appTopLevelWindow", settingTopWindowHandle);		    	
		    	appCapabilities.setCapability("deviceName", device_name);
		    	SettingSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), appCapabilities);
		    	Assert.assertNotNull(SettingSession);
				}catch(Exception e){
		            e.printStackTrace();
		            log.info("Error getting Settings session");
		            throw new RuntimeException(e);
		        	}
			log.info("Settings session created successfully");
			return SettingSession;
		  }
	  
	  	  
	  // Method to discover target printer - checks if already added then remove, confirm remove then add , confirm Add 
	  public static void DiscoverRemoveAddPrinter(String ptr_name,String device_name) throws InterruptedException, MalformedURLException {
		  	
		  	// Check if printer is already added
		  	if (!IsPrinterAlreadyAdded(ptr_name, device_name)) {
		  		
		  		log.info("Printer does not exists already under \"Printers & scanners\" so going for discovering");	
		  		
		  		 // Perform discovery
		  		PerformDiscovery();
		  		
				// Find Target Printer in the List of Printer
		  		FindTargetPrinterInList(ptr_name);		  		
		  		
		  		//Then Add Printer
		  		PerformAddPrinter(ptr_name,device_name);
									
			} else {
				log.info("Printer you are looking for was already added previously so Removing this printer =>"+ptr_name);
				// Remove the already added printers
				RemoveAlreadyAddedPrinter(ptr_name,device_name);				
				
				 // Perform discovery
		  		PerformDiscovery();
		  		
				// Find Target Printer in the List of Printer
		  		FindTargetPrinterInList(ptr_name);		  		
		  		
		  		//Then Add Printer
		  		PerformAddPrinter(ptr_name,device_name);				
			}
		  	
	  }
	  
	  
	  // Method to discover target printer - checks if already added then removes and then discover
	  public static void DiscoverRemoveDiscoverPrinter(String ptr_name,String device_name) throws InterruptedException, MalformedURLException {
		  	
		  	// Check if printer is already added
		  	if (!IsPrinterAlreadyAdded(ptr_name, device_name)) {
		  		log.info("Printer does not exists already under \"Printers & scanners\" so going for discovering");	
		  		
		  		 // Perform discovery
		  		PerformDiscovery();
		  		
				// Find Target Printer in the List of Printer
		  		FindTargetPrinterInList(ptr_name);
						
									
			} else {
				log.info("Printer you are looking for was already added previously so Removing this printer =>"+ptr_name);
				// Remove the already added printers
				RemoveAlreadyAddedPrinter(ptr_name,device_name);	
				Thread.sleep(2000);				
				
				 // Perform discovery
		  		PerformDiscovery();
		  		
				// Find Target Printer in the List of Printer
		  		FindTargetPrinterInList(ptr_name);				
				
			}
		  	
	  }
	  
	  
	  // Add Printer from discovered printer list - 1) IsAlreadyAdded if not then Discover and Add
	  public static void PerformAddPrinter(String ptr_name,String device_name) throws InterruptedException, MalformedURLException {
		  	boolean printerAdded = false;
		  	MoveMousePointerToPrinter(ptr_name);
		  	
		  	// if printer is way down in the list then moving mouse pointer does not work so check if printer is visible if not then scroll down further
		  	if(!SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")).isDisplayed()){
		  		log.info("Target printer \"+ptr_name\" still not visible so scrolling down");
		  		SettingSession.getKeyboard().pressKey(Keys.PAGE_DOWN);
		  	}		  	
		  	
		  		
			//Click on Discovered Printer
			try {
				SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")).click();
			}catch (Exception e) {
				log.info("Error in clicking on the printer =>"+ptr_name);
				throw new RuntimeException(e);
			}
			   
			//Click on Add Device to Add Discovered Printer
			try {
				SettingSession.findElement(By.xpath("//Button[contains(@Name,'Add device')]")).click();
				log.info("Clicked on Add device for Printer => "+ptr_name);
				
				// Wait until you see printer shows status as "Ready" indicating it is added successfully
				log.info("Waiting until printer gets added");
	    	    wait = new WebDriverWait(SettingSession, 60);
	    	    wait.until(ExpectedConditions.textToBePresentInElement(SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")), "Ready"));    	    
				printerAdded = true;			
				}catch (Exception e) {
					log.info("Error in adding printer =>"+ptr_name);
					throw new RuntimeException(e);
				}
				
				Assert.assertEquals(printerAdded,true,"Failed to Add Target Printer => "+ptr_name);
				log.info("Added Target Printer => "+ptr_name+" Successfully");				
					  
	  }
	  
	  
	  //if printer was found then determine whether it is already added or new one
	  //Better logic could be just see if printer is visible in already added printer list (before going for discovery)
	  // Store all printers in a List	  	
	  public static boolean IsPrinterAlreadyAdded(String ptr_name,String device_name) throws InterruptedException, MalformedURLException {	
		  	SettingSession.findElementByClassName("ScrollViewer").click();
		  	List<WebElement> PrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
			Assert.assertNotNull(PrinterListItem);				
			boolean printerExists=false;
			int ptrExistingCount = 0;			
			for(WebElement el : PrinterListItem) {
					ptrExistingCount++;					
					log.info("Already Added Printer "+ptrExistingCount+" => "+el.getText());
			}
			log.info("Total Added Printers Count => "+ptrExistingCount);
			
			
			for(WebElement el : PrinterListItem) {																						
					if (el.getText().contains(ptr_name)) 
					{
						printerExists = true;
						break;							
					}						
			}			
			return printerExists;		
		
	  } 
	  
	  
	  
	  // Move mouse pointer to the printer (PUT) in the list
	  public static void MoveMousePointerToPrinter(String ptr_name) throws InterruptedException {			  
		  	Actions action = new Actions(SettingSession);
			action.moveToElement(SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")));		    
			action.perform();
			SettingSession.getKeyboard().pressKey(Keys.ARROW_DOWN);
			log.info("Moved Mouse to Printer Name => "+ptr_name);
	  }
	  
	  
	  
	 //if printer was found then remove it and confirm the printer removal
	  public static void RemoveAlreadyAddedPrinter(String ptr_name,String device_name ) throws InterruptedException {
		  	//Move mouse pointer to printer under test and then remove it.
			MoveMousePointerToPrinter(ptr_name);
			Thread.sleep(1000);
			
			//Click on the printer that has to be removed
			try {
				SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")).click();
				log.info("Waiting until Remove button is clickable");
	    	    wait = new WebDriverWait(SettingSession, 60);
	    	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//Button[contains(@Name,'Remove device')]")));	    	    
			}catch(Exception e) {
				log.info("Error clicking on printer that has to be removed");
				throw new RuntimeException(e);
			}
    	    
			//Click on the Remove device button
			try {
				SettingSession.findElement(By.xpath("//Button[contains(@Name,'Remove device')]")).click();
				log.info("Clicked on \"Remove device\" Successfully");
			}catch(Exception e) {
				log.info("Error clicking on \"Remove device\" button for the printer that has to be removed");
				throw new RuntimeException(e);
			}			
			Thread.sleep(2000);
			
			try {
    	    //Confirm deletion
			DesktopSession = Base.GetDesktopSession(device_name);
			DesktopSession.findElement(By.xpath("//Button[contains(@Name,'Yes')]")).click();
    	    log.info("Clicked on \"Yes\" Successfully");
    	    Thread.sleep(4000);
			}catch(Exception e) {
				log.info("Error confirming deletion of the printer that has to be removed");
				throw new RuntimeException(e);
			}
			
			// Ensure printer is not visible anymore
			SettingSession.findElementByClassName("ScrollViewer").click();
			List<WebElement> PrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
			Assert.assertNotNull(PrinterListItem);			
			int ptrExistingCount = 0;			
			for(WebElement el : PrinterListItem) {
				ptrExistingCount++;					
				log.info("Existing Added Printer List After Removing Printer Under Test"+ptrExistingCount+" => "+el.getText());
			}
			log.info("Total Added Printers Count => "+ptrExistingCount);
			
			boolean printerRemoved=true;
			for(WebElement el : PrinterListItem) {
				log.info("Looking if removed printer is still there");
					if (el.getText().contains(ptr_name)) 
					{
						printerRemoved = false;
						break;
						
					}					
				}				
				Assert.assertEquals(printerRemoved,true,"Failed to Remove Target Printer => "+ptr_name);
				log.info("Successfully removed printer under test => "+ptr_name);
										
		}	
	  
	  // Method to perform discovery
	  public static void PerformDiscovery() throws InterruptedException {
		  	
		  	//Go to top of the screen
		  	SettingSession.getKeyboard().pressKey(Keys.PAGE_UP);
		  	Thread.sleep(1000);		  	
		  	SettingSession.getKeyboard().pressKey(Keys.PAGE_UP);
		  	Thread.sleep(1000);
		  
		  	//Move mouse pointer to Add a printer or scanner
		  	Actions action = new Actions(SettingSession);
			action.moveToElement(SettingSession.findElementByName("Add a printer or scanner"));		    
			action.perform();
			
			Thread.sleep(2000);
		  	SettingSession.findElementByName("Add a printer or scanner").click();
			Thread.sleep(1000);
			log.info("Clicked on \"Add a printer or scanner\" to Search All Printer in the Network");
			Thread.sleep(1000);
			
			do {
				log.info("Printer Discovery is in Progress");				
				Thread.sleep(3000);				
				}while(SettingSession.findElementsByName("Searching for printers and scanners").size()!=0);
				log.info("Printer Discovery completed");
	  }
	  
	  
	  
	  // Find Target Printer
	  public static void FindTargetPrinterInList(String ptr_name) throws InterruptedException {		  
		  	boolean printerFound = false;
		  	
		  	SettingSession.findElementByClassName("ScrollViewer").click();
		  	List<WebElement> PrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
			Assert.assertNotNull(PrinterListItem);					
			int ptrCount = 0;			
			for(WebElement el : PrinterListItem) {
				ptrCount++;					
				log.info("Printer "+ptrCount+" => "+el.getText());
			}
			
			log.info("Total Printers Discovered => "+ptrCount);
			
			
			for(WebElement el : PrinterListItem) {																						
					if (el.getText().contains(ptr_name))
					{
						printerFound = true;
						log.debug("Get Text Retuns =>"+el.getText());
						log.debug("Get Text lenght =>"+el.getText().length());
						log.debug("ptr_name => "+ptr_name);
						log.debug("ptr_name lenght => "+ptr_name.length());
						break;							
					}						
			}
			Assert.assertEquals(printerFound,true,"Didnt find Target Printer => "+ptr_name);
			log.info("Found Target Printer => "+ptr_name);
			
	  }

	  
	  public static void OpenSettings(String device_name) throws InterruptedException, MalformedURLException {
		  	CortanaSession=SettingBase.GetCortanaSession(device_name);	    	    
		    Thread.sleep(3000);
		    try {
		    	CortanaSession.findElementByName("Search box").sendKeys("Printers & scanners");
		    	log.info("Searching \"Printers & scanners\"");	      	           
		    	CortanaSession.findElementByName("Printers & scanners, System settings").click();
		    	log.info("Clicked on \"Printers & scanners\"");
		    	}catch(Exception e){
		    	e.printStackTrace();
		    	log.info("Error getting to Settings -> \"Printers & scanner\"");
		    	}
		    Thread.sleep(1000);
		    
		    SettingSession=SettingBase.GetSettingSession(device_name);
		    wait = new WebDriverWait(SettingSession, 60);
		    wait.until(ExpectedConditions.elementToBeClickable(By.name("Add a printer or scanner")));
		    log.info("Waited until \"Printers & scanner\" is clickable");	  	  
	  }
	  
	  
	  // Printing test page from PUT in the added printer list 
	  public static void TestPagePrint(String ptr_name,String device_name) throws InterruptedException, MalformedURLException {
		  
		  	OpenSettings(device_name); 
		  
		  	//Scrolling to the printer to which test page has to be printed.
		  	MoveMousePointerToPrinter(ptr_name);
		  	
		  	// if printer is way down in the list then moving mouse pointer does not work so check if printer is visible if not then scroll down further
		  	if(!SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")).isDisplayed()){
		  		log.info("Target printer \"+ptr_name\" still not visible so scrolling down");
		  		SettingSession.getKeyboard().pressKey(Keys.PAGE_DOWN);
		  	}		  	
		  			  		
			//Click on Added Printer
			try {
				SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")).click();
			}catch (Exception e) {
				log.info("Error in clicking on the printer =>"+ptr_name);
				throw new RuntimeException(e);
			}
			   
			//Click on Manage to view info about Added Printer
			try {
				SettingSession.findElement(By.xpath("//Button[@Name = 'Manage']")).click();
				log.info("Clicked on Manage for Printer => "+ptr_name);
				Thread.sleep(1000);
				
				}catch (Exception e) {
					log.info("Error in clicking Manage for printer =>"+ptr_name);
					throw new RuntimeException(e);
				}
			
			//Click on Print a test page option to print test page
			try {
				SettingSession.findElementByName("Print a test page").click();
				log.info("Clicked on Print test page for Printer => "+ptr_name);
		        Thread.sleep(1000);		        
	      		}catch (Exception e) {
					log.info("Error in clicking Print test page for printer =>"+ptr_name);
					throw new RuntimeException(e);
				}
			
			//moving back to printer settings screen
			SettingSession.getKeyboard().pressKey(Keys.BACK_SPACE);
			log.info("Moved back to Settings page with printers listing");
			Thread.sleep(1000);
				
	  }
	  
	  
}
	  
	

	 


