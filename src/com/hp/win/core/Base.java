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
		public static RemoteWebDriver DesktopSession = null;
		public static RemoteWebDriver PrintQueueSession = null;
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
		

		public static void ClickButton(RemoteWebDriver session, String buttonName) throws InterruptedException{
		    try {
		        // There is something strange about clicking buttons with findElementByName() - Appium thinks the button
		        // has been clicked even though the cursor is not on the button when it clicks - this is regardless of whether
		        // the button's name is unique or not - EMC
		        session.findElementByXPath("//Button[starts-with(@Name, \"" + buttonName +"\")]").click();
            //session.findElementByName(buttonName).click();
		        log.info("Clicked '" + buttonName + "' button successfully.");
		        Thread.sleep(1000);
		    } catch (Exception e) {
		        log.info("Could not click on '" + buttonName + "' button.");
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
		
		
	      // Method to switch to a new app window
        public static void BringWindowToFront(String device_name, WindowsDriver session) throws MalformedURLException {
            try {
            
                DesktopSession = Base.GetDesktopSession(device_name);
                
                //Get handle to PrinterQueue window
                WebElement sessionWindow = DesktopSession.findElementByClassName("PrintUI_PrinterQueue");
                String nativeWindowHandle = sessionWindow.getAttribute("NativeWindowHandle");
                int sessionWindowHandle = Integer.parseInt(nativeWindowHandle);
                log.debug("int value:" + nativeWindowHandle);
                String sessionTopWindowHandle  = hex.concat(Integer.toHexString(sessionWindowHandle));
                log.debug("Hex Value:" + sessionTopWindowHandle);
    
                // Create an app session by attaching to an existing application top level window handle
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("appTopLevelWindow", sessionTopWindowHandle);
                capabilities.setCapability("platformName", "Windows");
                capabilities.setCapability("deviceName", device_name);
                session = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
                }catch(Exception e){
                    e.printStackTrace();
                    log.info("Error getting session window handle");
                    throw new RuntimeException(e);
                    }
            log.info("Successfully switched to different window.");
        }
	
}
