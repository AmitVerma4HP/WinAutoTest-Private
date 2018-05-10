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
		protected static RemoteWebDriver SettingSession = null;
		static WebDriverWait wait;
		
		
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
	  
	  	  
	  // Method to discover target printer
	  public static boolean DiscoverPrinter(String ptr_name) throws InterruptedException {
		  	boolean printerFound=false;
		  	// Check if printer is already added
		  	if (!IsPrinterAlreadyAdded(ptr_name)) {
		  	log.info("Printer does not exists already under \"Printers & scanners\" so going for discovering");		  	
		  	SettingSession.findElementByName("Add a printer or scanner").click();
			Thread.sleep(1000);
			log.info("Clicked on \"Add a printer or scanner\" to Search All Printer in the Network");
			Thread.sleep(1000);
			
			do {
				log.info("Printer Discovery is in Progress");				
				Thread.sleep(3000);				
				}while(SettingSession.findElementsByName("Searching for printers and scanners").size()!=0);
				log.info("Printer Discovery completed");
				
				// Store all printers in a List				
				List<WebElement> PrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
				Assert.assertNotNull(PrinterListItem);					
				int ippCount = 0; 
				int i = 0;
				for(WebElement el : PrinterListItem) {
					if(el.getText().contains("ipp:")) {
						ippCount++;
						i++;
						log.info("IPP Printer "+i+" => "+el.getText());
					}
				}
				log.info("Total IPP Printers Discovered => "+ippCount);
				
				
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
			} else {
				log.info("Printer you are looking for was already added previously so SKIPPING Printer Discovery");
				// Can also go for remove printer and then discover
			}
		  	return printerFound;
	  }
	  
	  
	  // Add Printer from discovered printer list - 1) IsAlreadyAdded if not then Discover and Add
	  public static void AddPrinter(String ptr_name) throws InterruptedException {
		  	boolean printerAdded = false;
		  	//if printer discovery is successful then add it
		  	if(DiscoverPrinter(ptr_name)) {
		    
		    // Scroll to the discovered printer
		  	Actions action = new Actions(SettingSession);
			action.moveToElement(SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")));		    
			action.perform();
			SettingSession.getKeyboard().pressKey(Keys.ARROW_DOWN);
			log.info("Moved Mouse to Printer Name => "+ptr_name);
			
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
			
			//Check successful addition and then change printerAdd = 1
			 
		  	}
					  
	  }
	  
	  
	  //if IPP printer was found then determine whether it is already added or new one
	  public static boolean IsPrinterAlreadyAdded(String ptr_name) throws InterruptedException {	
		  	
		  	// Move mouse to the printer (PUT) in the list
		  	
		   /*
		  	Actions action = new Actions(SettingSession);
		    action.moveToElement(SettingSession.findElement(By.xpath("//ListItem[contains(@Name,'"+ptr_name+"')]")));		    
		    action.perform();
		    log.info("Moved Mouse to Printer Name => "+ptr_name);
		    */
				
			//Better logic could be just see if printer is visible in already added printer list
			// Store all printers in a List				
			List<WebElement> PrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
			Assert.assertNotNull(PrinterListItem);				
			boolean printerExists=false;
			int ippExistingCount = 0; 
			int i = 0;
			for(WebElement el : PrinterListItem) {
				if(el.getText().contains("ipp:")) {
					ippExistingCount++;
					i++;
					log.info("Already Added IPP Printer "+i+" => "+el.getText());
				}
			}
			log.info("Total Added IPP Printers Exists => "+ippExistingCount);
			
			
			for(WebElement el : PrinterListItem) {																						
					if (el.getText().contains(ptr_name)) 
					{
						printerExists = true;
						break;							
					}						
			}
			
			return printerExists;
			
			
			//Older logic to determine if printer was already added
			
			/*
			if(SettingSession.findElement(By.xpath("//Button[contains(@Name,'Remove device')]")).isDisplayed()) {
				log.info("This IPP Printer => "+ptr_name+" is already added");
				return true;
			}else
				{						
				log.info("This IPP Printer => "+ptr_name+" not added yet");
				return false;
				}	
		  */
	  } 

	  
	 //if IPP printer was found then remove it
	  public static boolean RemoveAlreadyAddedPrinter(String ptr_name) throws InterruptedException {
		return false;	
		  
	  }
	  
	

	 

}
