package com.hp.win.core;


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



public class PhotoAppBase extends UwpAppBase {
		private static final Logger log = LogManager.getLogger(PhotoAppBase.class);
		public static RemoteWebDriver PhotosSession = null;
		public static RemoteWebDriver WindowsAddSession = null;
		
		
	// Method to open Photos test file
	public static RemoteWebDriver OpenPhotosFile(String device_name, String test_filename)
			throws MalformedURLException, InterruptedException {

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
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error opening Photos app");
			throw new RuntimeException(e);
		}
		
		// Search for Saved Pictures folder.
		PhotosSession.findElementByName("Search").sendKeys("testfiles");
		Thread.sleep(1000);
		
		if(PhotosSession.findElementsByName("testfiles").size()==0)
		{
			// Adding the Test Folder to the Photos app
			PhotosSession.findElementByName("Import").click();
			Thread.sleep(1000);
			
			PhotosSession.findElementByName("From a folder").click();
			Thread.sleep(1000);
			
			log.info("Opening WindowsAddSession...");
			WindowsAddSession = GetDesktopSession(device_name);
	        Assert.assertNotNull(WindowsAddSession);
					
	        WindowsAddSession.findElementByXPath("//Edit[@Name = 'Folder:']").click();
	        WindowsAddSession.getKeyboard().pressKey(testfiles_loc);
	        WindowsAddSession.getKeyboard().pressKey(Keys.ENTER);
			Thread.sleep(1000);
	
			WindowsAddSession.findElementByXPath("//Button[@Name = 'Add this folder to Pictures']").click();
			Thread.sleep(1000);
			log.info("Added Test Folder => \"testfiles\" to the Photos app successfully");
			
			PhotosSession.findElementByName("Search").clear();
		}else{
			log.info("Test Folder => \"testfiles\" has already been imported");
			PhotosSession.findElementByName("Search").clear();
			
		}
		
		try {
			WindowsAddSession.quit();
            log.debug("Closed WindowsAddSession...");
        } catch (Exception e) {
        	log.info("WindowsAddSession already terminated.");
        }
		
		log.info("Opened PhotosSession successfully");
		return PhotosSession;

	}

	
	// Method to print from Photos
	public static void PrintPhoto(String ptr_name, String test_filename) throws InterruptedException {
			
		// Search for Saved Pictures folder.
		PhotosSession.findElementByName("Search").sendKeys("testfiles");
		log.info("Searching \"Test Folder - testfiles\"");
		Thread.sleep(2000);

		// Click on Saved Pictures
		PhotosSession.findElementByXPath("//Button[@Name = \"testfiles\"]").click();
		log.info("Clicked on \"Test Folder - testfiles\"");
		Thread.sleep(2000);

		// Select the Photo file
		PhotosSession.findElementByXPath("//Button[@AutomationId = '" + test_filename + "']").click();
		log.info("Selected the Photo file for printing");
		Thread.sleep(2000);

		// Tap on Print icon
		PhotosSession.findElementByXPath("//*[@Name = 'Print']").click();
		log.info("Clicked on Print Icon Successfully in PhotoApp");
		Thread.sleep(1000);

	}
	
	
	// Method to select desired photo size
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectPhotoSize_Photos(RemoteWebDriver PhotosSession, String photo_size) throws MalformedURLException, InterruptedException {
		
		WebElement PhotoSizeListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Photo size']");
		Assert.assertNotNull(PhotoSizeListComboBox);
		if (!PhotoSizeListComboBox.getText().toString().contentEquals(photo_size)) 
		{
			log.info("Desired photo size => " + photo_size + " <= is not selected so selecting it from drop down");
			PhotoSizeListComboBox.click();
			Thread.sleep(1000);
			try {
				PhotosSession.findElementByName(photo_size).click();
				Thread.sleep(1000);
				log.info("Selected desired photo size *****" + PhotoSizeListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("Desired Photo Size is not found so either 1) your Printer does not support desired photo size OR 2) you have typed the photo size value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting desired photo size but continuing with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				PhotoSizeListComboBox.click();
			}
		} else {
			log.info("Desired photo size => " + PhotoSizeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Fit
	public static void SelectPhotoFit_Photos(RemoteWebDriver photosSession, String photo_fit) throws MalformedURLException, InterruptedException {
			
		WebElement PhotoFitListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Fit']");
		Assert.assertNotNull(PhotoFitListComboBox);
		if (!PhotoFitListComboBox.getText().toString().contentEquals(photo_fit)) 
		{
			log.info("Desired photo fit => " + photo_fit + " <= is not selected so selecting it from drop down");
			PhotoFitListComboBox.click();
			Thread.sleep(1000);										
				try {
					PhotosSession.findElementByName(photo_fit).click();
					Thread.sleep(1000);
					log.info("Selected desired photo fit *****" + PhotoFitListComboBox.getText().toString() + "*****");
				} catch (Exception e) {
					log.info("Desired Photo Fit is not found so either 1) your Printer does not support desired photo fit OR 2) you have typed the photo fit value incorrectly in testsuite xml");
					//e.printStackTrace();
					log.info("Error selecting desired photo fit but continuing with rest of the print options");
					//throw new RuntimeException(e);
					//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
					PhotoFitListComboBox.click();
				}			
		} else {
			log.info("Desired photo fit => " + PhotoFitListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}


}
