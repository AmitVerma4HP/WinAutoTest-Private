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


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;



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
			PhotosSession.manage().window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error opening Photos app");
			throw new RuntimeException(e);
		}
		
		log.info("Opened PhotosSession successfully");
		return PhotosSession;
	}
	
	public static void AddTestFolder(String device_name, String test_filename, String testfiles_loc) throws Throwable{
		
		log.info("Adding the test folder");
		
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
            	    
		WindowsAddSession.findElementByXPath("//Button[@Name = 'Add this folder to Pictures']").click();
		Thread.sleep(1000);
		log.info("Added Test Folder => \"testfiles\" to the Photos app successfully");
		
		//Making sure we are on right tab - Folders tab.
		PhotosSession.findElementByName("Folders").click();
		
		//Clicking on the test folder to display the images.
		PhotosSession.findElementByXPath("//Button[@HelpText = '"+testfiles_loc+"']").click();
						
		try {
			WindowsAddSession.quit();
            log.debug("Closed WindowsAddSession...");
		} catch (Exception e) {
        	log.info("WindowsAddSession already terminated.");
        }
	
}

	
	// Method to print from Photos
	public static void PrintPhoto(String ptr_name, String test_filename) throws InterruptedException {
		
		Thread.sleep(2000);
		PhotosSession.getKeyboard().pressKey(Keys.TAB);
			
		List <WebElement> testimages = PhotosSession.findElements(By.xpath("//Button[@Name = 'Image']"));
		log.info("testfiles number: " + testimages.size());
		//PhotosSession.getKeyboard().pressKey(Keys.PAGE_DOWN);
		for (WebElement img : testimages){
			if(img.getAttribute("AutomationId").toString().equalsIgnoreCase(test_filename)){
				//img.click();
				log.info("Test image to be printed is found");
				break;
			}else{
				PhotosSession.getKeyboard().pressKey(Keys.PAGE_DOWN);
				Thread.sleep(1000);
				log.info("Image not found, hence scrolling down and searching");
			}
		}
	
	// Select the Photo file
		PhotosSession.findElementByXPath("//Button[@AutomationId = '" + test_filename + "']").click();
		log.info("Selected the Photo file for printing");
		Thread.sleep(1000);

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
				log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+photo_size+"\" Photo Size is NOT FOUND so either 1) your Printer does not support \""+photo_size+"\" Photo Size OR 2) you have typed the Photo Size option value incorrectly in testsuite xml.");
            	log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+photo_size+"\" Photo Size is NOT FOUND so either 1) your Printer does not support \""+photo_size+"\" Photo Size OR 2) you have typed the Photo Size option value incorrectly in testsuite xml.");
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
            	
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
					log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	            	log.info("\""+photo_fit+"\" Photo Fit value is NOT FOUND so either 1) your Printer does not support \""+photo_fit+"\" Photo Fit OR 2) you have typed the Photo Fit option value incorrectly in testsuite xml.");
	            	log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	            	
					//e.printStackTrace();
	            	//This is to insert msg to TestNG emailable-report.html
					Reporter.log("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					Reporter.log("\""+photo_fit+"\" Photo Fit value is NOT FOUND so either 1) your Printer does not support \""+photo_fit+"\" Photo Fit OR 2) you have typed the Photo Fit option value incorrectly in testsuite xml.");
					Reporter.log("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	            	
					log.info("Error selecting desired photo fit but continuing with rest of the print options");
					//throw new RuntimeException(e);
					//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
					PhotoFitListComboBox.click();
				}			
		} else {
			log.info("Desired photo fit => " + PhotoFitListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	public static void VerifyAddTestFolder(String device_name, String test_filename, String testfiles_loc) throws Throwable{
	 
		//Verifying test folder and adding if not already added
	    PhotosSession.findElementByName("Folders").click();
		Thread.sleep(1000);
		testfiles_loc = testfiles_loc.substring(0, testfiles_loc.length()-1);
		log.info(testfiles_loc);
	    try {
	    	if (PhotosSession.findElementByXPath("//Button[@HelpText = '"+testfiles_loc+"']") != null){
	    		log.info("TestFiles Folder already added");
	    		PhotosSession.findElementByXPath("//Button[@HelpText = '"+testfiles_loc+"']").click();
	    	}
		}catch (Exception e){
			log.info("TestFiles Folder not found");
		    PhotoAppBase.AddTestFolder(device_name, test_filename, testfiles_loc);
		 
	    }
	   
	}
	


}
