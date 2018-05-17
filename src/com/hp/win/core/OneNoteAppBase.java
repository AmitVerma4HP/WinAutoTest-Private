package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;



public class OneNoteAppBase extends UwpAppBase {
		private static final Logger log = LogManager.getLogger(OneNoteAppBase.class);
		//public static RemoteWebDriver OneNoteSession = null;
		

		// Method to open OneNote 
		public static RemoteWebDriver OpenOneNoteApp(String device_name)throws MalformedURLException, InterruptedException {

			try {
				capabilities = new DesiredCapabilities();
				capabilities.setCapability("app", "Microsoft.Office.OneNote_8wekyb3d8bbwe!microsoft.onenoteim");
				//capabilities.setCapability("appArguments", web_url);
				capabilities.setCapability("platformName", "Windows");
				capabilities.setCapability("deviceName", device_name);
				OneNoteSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
				Assert.assertNotNull(OneNoteSession);
				OneNoteSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				OneNoteSession.manage().window().maximize();
				Thread.sleep(2000);
			} catch (Exception e) {
				log.info("Error opening OneNote app");
			}
						
			log.info("Opened First OneNoteSession successfully");		
			
			
			// Attached session to OneNote
				/*		
					DesktopSession = Base.GetDesktopSession(device_name);
				    
				    //Get handle to PrinterQueue window
				    try {
					WebElement OneNoteWindow = DesktopSession.findElementByName("My Notebook ‎- OneNote");
			    	String nativeWindowHandle = OneNoteWindow.getAttribute("NativeWindowHandle");
			    	int printerQueueWindowHandle = Integer.parseInt(nativeWindowHandle);
			    	log.debug("int value:" + nativeWindowHandle);
			    	String printerQueueTopWindowHandle  = hex.concat(Integer.toHexString(printerQueueWindowHandle));
			    	log.debug("Hex Value:" + printerQueueTopWindowHandle);
		
			    	// Create a PrintQueueSession by attaching to an existing application top level window handle
			    	DesiredCapabilities capabilities = new DesiredCapabilities();
			    	capabilities.setCapability("appTopLevelWindow", printerQueueTopWindowHandle);
			    	capabilities.setCapability("platformName", "Windows");
			        capabilities.setCapability("deviceName", device_name);
			        OneNoteSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
					}catch(Exception e){
						e.printStackTrace();
						log.info("Error attaching to OneNote session");
						throw new RuntimeException(e);
			        	}
				log.info("Attached session to OneNote successfully");
		
			*/
			
			
			/*
			String currentWindowHandle = OneNoteSession.getWindowHandle();
			Thread.sleep(5000);
			
			log.info("Current Window handle =>"+currentWindowHandle);
			
			Collection<String> allWindowHandle = OneNoteSession.getWindowHandles();
						
			for(Iterator i=allWindowHandle.iterator();i.hasNext();){
				log.info("Window handle => "+i.next());				
			}
			*/
			return OneNoteSession;
			
		}
		
		
		// Method to print web page from MsEdge Browser
		public static void PrintOneNote(String ptr_name) throws InterruptedException {
			// Go to More settings at the top right corner.
			
			Actions action = new Actions(OneNoteSession);
			action.moveToElement(OneNoteSession.findElementByName("More"));    
			action.perform();
			Thread.sleep(1000);
			OneNoteSession.findElementByName("More").click();
			Thread.sleep(1000);
			
			// Tap on Print icon
			OneNoteSession.findElementByName("Print").click();
			log.info("Clicked on Print button Successfully to launch the print options screen");
			Thread.sleep(1000);
		}

		// Method to open test File via OneNote app.
			public static RemoteWebDriver OpenTestFileOneNoteApp(String device_name, String test_filename)
					throws MalformedURLException {

				try {
					capabilities = new DesiredCapabilities();
					capabilities.setCapability("app", "Microsoft.MicrosoftEdge_8wekyb3d8bbwe!MicrosoftEdge");
					capabilities.setCapability("appArguments", testfiles_loc + test_filename);
					capabilities.setCapability("platformName", "Windows");
					capabilities.setCapability("deviceName", device_name);
					capabilities.setCapability("appWorkingDir", testfiles_loc);
					OneNoteSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
					Assert.assertNotNull(OneNoteSession);
					OneNoteSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Error opening PDF file");
					throw new RuntimeException(e);
				}
					
				log.info("Opened PDF File via MsEdge successfully");
				return OneNoteSession;
				
			}
		
}
