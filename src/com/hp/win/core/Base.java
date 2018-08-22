/*
Copyright 2018 Mopria Alliance, Inc.
Copyright 2018 HP Development Company, L.P.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package com.hp.win.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
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
		        log.info("Clicked '" + buttonName + "' button successfully.");
		        Thread.sleep(1000);
		    } catch (Exception e) {
		        log.info("Could not find button using xpath. Going to try finding button by name.");
		        try {
		            session.findElementByName(buttonName).click();
		            log.info("Clicked '" + buttonName + "' button successfully.");
		            Thread.sleep(1000);
		        }
		        catch (Exception e1) {
		              log.info("Could not click on '" + buttonName + "' button.");
		              throw new RuntimeException(e1);
		        }

		    }
		}
		

		
		// Method to Create Desktop Session
		public static RemoteWebDriver GetDesktopSession(String device_name) throws MalformedURLException {	
			try {
		    capabilities = new DesiredCapabilities();
		    capabilities.setCapability("app","Root");
		    capabilities.setCapability("platformName", "Windows");
	        capabilities.setCapability("deviceName", device_name);
	        
	        //Increased default command timeout to 60 seconds as Add printer takes little longer 
	        capabilities.setCapability("newCommandTimeout", "60000"); 
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
	
		    	log.info("Successfully got Print Queue handle.");
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
        public static void BringWindowToFront(String device_name, RemoteWebDriver session, String className) throws MalformedURLException {
            try {
            
                DesktopSession = Base.GetDesktopSession(device_name);
                
                //Get handle to PrinterQueue window
                WebElement sessionWindow = DesktopSession.findElementByClassName(className);
                log.info("Found window '" + sessionWindow.getAttribute("Name").toString() + "'.");
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
                Assert.assertNotNull(session);
                }catch(Exception e){
                    e.printStackTrace();
                    log.info("Error getting session window handle");
                    throw new RuntimeException(e);
                    }
            log.info("Successfully switched to different window.");
        }
        
        
        // Uncheck option "“Let the app change my printing preferences” if it is checked in App Print UI
        public static void UncheckAppChangePreference(RemoteWebDriver session) throws InterruptedException {
        	
        	try {
    			if(session.findElementByName("Let the app change my printing preferences").isSelected())
    			{
    				log.info("Option \"Let the app change my printing preferences\" is CHECKED so UNCHECKING it");
    				Thread.sleep(1000);    				
    				session.findElementByName("Let the app change my printing preferences").click();
    				Thread.sleep(1000); 
    			}else {
    				log.info("Option \"Let the app change my printing preferences\" is UNCHECKED so No Action needed");
    			}
    		}catch(NoSuchElementException e) {
    				log.info("There is no \"Let the app change my printing preferences\" option in App Print UI");
    		}
        	
        	
        }
            
}
