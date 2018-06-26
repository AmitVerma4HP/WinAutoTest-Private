package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;



public class EdgeAppBase extends UwpAppBase {
		private static final Logger log = LogManager.getLogger(EdgeAppBase.class);
		public static RemoteWebDriver MsEdgeSession = null;
		

		// Method to open MsEdge browser with desired URL.
		public static RemoteWebDriver OpenEdgeApp(String device_name, String web_url)
				throws MalformedURLException {

			try {
				capabilities = new DesiredCapabilities();
				capabilities.setCapability("app", "Microsoft.MicrosoftEdge_8wekyb3d8bbwe!MicrosoftEdge");
				capabilities.setCapability("appArguments", web_url);
				capabilities.setCapability("platformName", "Windows");
				capabilities.setCapability("deviceName", device_name);
				MsEdgeSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
				Assert.assertNotNull(MsEdgeSession);
				MsEdgeSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				MsEdgeSession.manage().window().maximize();
				Thread.sleep(1000);
			} catch (Exception e) {
				log.info("Error opening Edge app");
			}
			
			if(MsEdgeSession.findElementByName("Search or enter web address").getText().contains(web_url)){
				log.info("Opened expected "+web_url+" from Edge browser");
			}else{
				log.info("Error in launching expected URL: " +web_url );
				MsEdgeSession.close();
			}		
			
			log.info("Opened MsEdgeSession successfully");
			return MsEdgeSession;
			
		}
		
		
		// Method to print web page from MsEdge Browser
		public static void PrintEdge(String ptr_name, String web_url) throws InterruptedException {
			// Go to More settings at the top right corner.
			MsEdgeSession.findElementByName("Settings and more").click();
			Thread.sleep(1000);
			
			// Tap on Print icon
			MsEdgeSession.findElementByName("Print").click();
			log.info("Clicked on Print button Successfully to launch the print options screen");
			Thread.sleep(1000);
		}

		// Method to open PDF File via MsEdge app.
		public static RemoteWebDriver OpenPdfEdgeApp(String device_name, String test_filename)
				throws MalformedURLException {

			try {
				capabilities = new DesiredCapabilities();
				capabilities.setCapability("app", "Microsoft.MicrosoftEdge_8wekyb3d8bbwe!MicrosoftEdge");
				capabilities.setCapability("appArguments", testfiles_loc + test_filename);
				capabilities.setCapability("platformName", "Windows");
				capabilities.setCapability("deviceName", device_name);
				capabilities.setCapability("appWorkingDir", testfiles_loc);
				MsEdgeSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
				Assert.assertNotNull(MsEdgeSession);
				MsEdgeSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Error opening PDF file");
				throw new RuntimeException(e);
			}
				
			log.info("Opened PDF File via MsEdge successfully");
			return MsEdgeSession;
			
		}
		
		
		public static void CloseErrorEdgePopup(){
			try{
				
				if (MsEdgeSession.findElementByXPath("//TextBlock[contains(@Name,'Microsoft Edge is printing a file.')]").isDisplayed()){
					MsEdgeSession.findElementByXPath("//Button[@Name = 'Close anyway']").click();
					log.info("Error popup Found and closed Successfully");
				} else {
					log.info("Error popup Found but failed to close");
				}
			} catch (Exception e){
				log.info("Error popup Not Found");
			}
			
			try{
				if (MsEdgeSession.findElementByXPath("//TextBlock[contains(@Name,'Do you want to close all tabs?')]").isDisplayed()){
					MsEdgeSession.findElementByXPath("//Button[@Name = 'Close all']").click();
					log.info("Close all tabs confirmation popup Found and closed Successfully");
				} else {
					log.info("Close all tabs confirmation popup Found but failed to close");
				}
			} catch (Exception e){
				log.info("Close all tabs confirmation popup Not Found");
			}
			
			
		}
		
			
}
