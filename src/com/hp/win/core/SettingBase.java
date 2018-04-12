package com.hp.win.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;



public class SettingBase extends Base {
		private static final Logger log = LogManager.getLogger(SettingBase.class);
		protected static RemoteWebDriver CortanaSession = null;
		protected static RemoteWebDriver SettingSession = null;
		
		
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
	  public static void FindPrinter(String ptr_name) throws InterruptedException {
		  
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
				log.info("Total Printer Discovered => "+PrinterListItem.size());
				int printerFound=0;
				int i = 1; 
				for(WebElement el : PrinterListItem) {
					
					log.debug("Printer "+i+" => "+el.getText());
					i++;														
						if (el.getText().contains(ptr_name)) 
						{
							printerFound = 1;
							break;
						}						
				}
				Assert.assertEquals(printerFound,1,"Didnt find Target Printer => "+ptr_name);
				log.info("Found Target Printer => "+ptr_name);
		  
	  }
	  
	

	 

}
