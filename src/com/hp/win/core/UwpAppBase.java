package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
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



public class UwpAppBase extends Base {
		private static final Logger log = LogManager.getLogger(UwpAppBase.class);
		public static RemoteWebDriver Session = null;
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
			Session = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
			Assert.assertNotNull(Session);
			Session.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error opening Photos app");
			throw new RuntimeException(e);
		}
		
		// Adding the Test Folder to the Photos app
		Session.findElementByName("Import").click();
		Thread.sleep(1000);
		
		Session.findElementByName("From a folder").click();
		Thread.sleep(2000);
		
		log.info("Opening WindowsAddSession...");
		WindowsAddSession = GetDesktopSession(device_name);
        Assert.assertNotNull(WindowsAddSession);
				
        WindowsAddSession.findElementByXPath("//Edit[@Name = 'Folder:']").click();
        WindowsAddSession.getKeyboard().pressKey(testfiles_loc);
        WindowsAddSession.getKeyboard().pressKey(Keys.ENTER);
		Thread.sleep(1000);

		WindowsAddSession.findElementByXPath("//Button[@Name = 'Add this folder to Pictures']").click();
		Thread.sleep(1000);
		log.info("Added the Test Folder to the Photos app successfully");
		
		 try {
			 WindowsAddSession.quit();
            log.info("Closed WindowsAddSession...");
        } catch (Exception e) {
            log.info("WindowsAddSession already terminated.");
        }
		
		log.info("Opened Session successfully");
		return Session;

	}

	
	// Method to print from Photos
	public static void PrintPhoto(String ptr_name, String test_filename) throws InterruptedException {
		// Go to Folders Tab
		Session.findElementByName("Folders").click();
		log.info("Clicked on Folder Menu Successfully in PhotoApp");
		Thread.sleep(2000);

		// Search for Saved Pictures folder.
		Session.findElementByName("Search").sendKeys("testfiles");
		log.info("Searching \"Test Folder - testfiles\"");
		Thread.sleep(2000);

		// Click on Saved Pictures
		Session.findElementByXPath("//Button[@Name = \"testfiles\"]").click();
		log.info("Clicked on \"Test Folder - testfiles\"");
		Thread.sleep(2000);

		// Select the Photo file
		Session.findElementByXPath("//Button[@AutomationId = '" + test_filename + "']").click();
		log.info("Selected the Photo file for printing");
		Thread.sleep(1000);

		// Tap on Print icon
		Session.findElementByName("Print").click();
		log.info("Clicked on Print Icon Successfully in PhotoApp");
		Thread.sleep(1000);

	}

	
	// Method to open MsEdge browser with desired URL.
	public static RemoteWebDriver OpenEdgeApp(String device_name, String web_url)
			throws MalformedURLException {

		try {
			capabilities = new DesiredCapabilities();
			capabilities.setCapability("app", "Microsoft.MicrosoftEdge_8wekyb3d8bbwe!MicrosoftEdge");
			capabilities.setCapability("appArguments", web_url);
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", device_name);
			Session = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
			Assert.assertNotNull(Session);
			Session.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			Thread.sleep(1000);
		} catch (Exception e) {
			log.info("Error opening Edge app");
		}
		
		if(Session.findElementByName("Search or enter web address").getText().contains(web_url)){
			log.info("Opened expected "+web_url+" from Edge browser");
		}else{
			log.info("Error in launching expected URL: " +web_url );
			Session.close();
		}		
		
		log.info("Opened MsEdgeSession successfully");
		return Session;
		
	}
	
	
	// Method to print web page from MsEdge Browser
	public static void PrintEdge(RemoteWebDriver Session, String ptr_name) throws InterruptedException {
		// Go to More settings at the top right corner.
		Session.findElementByName("Settings and more").click();
		Thread.sleep(1000);
		
		// Tap on Print icon
		Session.findElementByName("Print").click();
		log.info("Clicked on Print button Successfully to launch the print options screen");
		Thread.sleep(3000);
	}

	
	// Method to select desired printer from printers list combo box
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectDesiredPrinter(RemoteWebDriver Session, String ptr_name) throws MalformedURLException, InterruptedException {
		
		WebElement PrinterListComboBox = Session.findElementByClassName("ComboBox");
		Assert.assertNotNull(PrinterListComboBox);
		if (!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) 
		{
			log.info("Desired printer => "+ptr_name+" is not selected so selecting it from drop down");
			PrinterListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElement(By.name(ptr_name)).click();
				log.info("Selected desired printer *****" +PrinterListComboBox.getText().toString()+"*****");
				Thread.sleep(2000);
			} catch (Exception e) {
				log.info("Printer under test is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name correctly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting printer under test so moving to next test");
				throw new RuntimeException(e);
			}			
		} else {
			log.info("Desired printer => " + PrinterListComboBox.getText().toString() + " <= is already selected so proceeding");
		}

	}

	
	// Method to select desired Copies option
	public static void SelectCopies_Uwp(RemoteWebDriver Session, String copies) throws MalformedURLException, InterruptedException {

		// Clicking on Copies Edit box.
		Session.findElementByXPath("//*[contains(@AutomationId,'JobCopiesAllDocuments_NumberText')]").click();
		Thread.sleep(2000);

		Session.getKeyboard().pressKey(Keys.CLEAR);
		Session.getKeyboard().pressKey(Keys.BACK_SPACE);
		Thread.sleep(1000);

		Session.getKeyboard().pressKey(copies);
		Thread.sleep(1000);

		log.info("Entered copies value ***** " + copies + " *****");
		Thread.sleep(2000);
	}

	
	// Method to select desired orientation
	public static void SelectOrientation_Uwp(RemoteWebDriver Session, String orientation) throws MalformedURLException, InterruptedException {

		WebElement OrientationListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Orientation']");
		Assert.assertNotNull(OrientationListComboBox);
		if (!OrientationListComboBox.getText().toString().contentEquals(orientation)) 
		{
			log.info("Desired Orientation option => " + orientation + " <= is not selected so selecting it from drop down");
			OrientationListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(orientation).click();
				Thread.sleep(2000);
				log.info("Selected desired orientation option *****" + OrientationListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("Desired Orientation option is not found so either 1) your Printer does not support desired orientation OR 2) you have typed the orientation value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting orientation option but continuing test with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails. 
				OrientationListComboBox.click();
			}			
		} else {
			log.info("Desired orientation option => " + OrientationListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired paper size
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectPaperSize_Uwp(RemoteWebDriver Session, String paper_size) throws MalformedURLException, InterruptedException {
		
		WebElement PaperSizeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Paper size']");
		Assert.assertNotNull(PaperSizeListComboBox);
		if (!PaperSizeListComboBox.getText().toString().contentEquals(paper_size)) 
		{
			log.info("Desired paper size => " + paper_size + " <= is not selected so selecting it from drop down");
			PaperSizeListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(paper_size).click();
				Thread.sleep(2000);
				log.info("Selected desired paper size *****" + PaperSizeListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("Desired Paper Size is not found so either 1) your Printer does not support desired paper size OR 2) you have typed the paper size value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting paper size option but continuing test with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				PaperSizeListComboBox.click();
			}				
		} else {
			log.info("Desired paper size => " + PaperSizeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired photo size
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectPhotoSize_Uwp(RemoteWebDriver Session, String photo_size) throws MalformedURLException, InterruptedException {

		WebElement PhotoSizeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Photo size']");
		Assert.assertNotNull(PhotoSizeListComboBox);
		if (!PhotoSizeListComboBox.getText().toString().contentEquals(photo_size)) 
		{
			log.info("Desired photo size => " + photo_size + " <= is not selected so selecting it from drop down");
			PhotoSizeListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(photo_size).click();
				Thread.sleep(2000);
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
	public static void SelectPhotoFit_Uwp(RemoteWebDriver Session, String photo_fit) throws MalformedURLException, InterruptedException {
		
		WebElement PhotoFitListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Fit']");
		Assert.assertNotNull(PhotoFitListComboBox);
		if (!PhotoFitListComboBox.getText().toString().contentEquals(photo_fit)) 
		{
			log.info("Desired photo fit => " + photo_fit + " <= is not selected so selecting it from drop down");
			PhotoFitListComboBox.click();
			Thread.sleep(1000);										
				try {
					Session.findElementByName(photo_fit).click();
					Thread.sleep(2000);
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

	
	// Method to select desired Margins
	public static void SelectPageMargins_Uwp(RemoteWebDriver Session, String page_margins)throws MalformedURLException, InterruptedException {

		WebElement PageMarginListComboBox = Session.findElementByXPath("//ComboBox[contains(@Name, 'Margins')]");
		Assert.assertNotNull(PageMarginListComboBox);
		if (!PageMarginListComboBox.getText().toString().contentEquals(page_margins))
		{
			log.info("Desired Page Margins => " + page_margins + " <= is not selected so selecting it from drop down");
			PageMarginListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(page_margins).click();
				Thread.sleep(2000);
				log.info("Selected desired margins *****" + PageMarginListComboBox.getText().toString() + "*****");		
			} catch (Exception e) {
				log.info("Desired Page Margins is not found so either 1) your Printer does not support desired page margins OR 2) you have typed the page margins value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting desired Page Margin but continuing with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				PageMarginListComboBox.click();
			}			
		} else {
			log.info("Desired page margin => " + PageMarginListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select More Options link to access more print options.
	public static int OpenMoreSettings() throws InterruptedException {
		try {
			Session.findElementByName("More settings").click();
			log.info("Clicked 'More Settings' link successfully");
			Thread.sleep(2000);
			return 1;
		} catch (Exception e){
			log.info("More settings Option Not Found");
			return 0;
		}
		
	}

	
	// Method to return from More Options screen to access print button.
	public static void CloseMoreSettings() throws InterruptedException {
		Session.findElementByXPath("//Button[@Name = 'Ok']").click();
		log.info("Clicked 'OK' button successfully.");
		Thread.sleep(2000);
	}

	
	// Method to select Color options.
	public static void SelectColorOrMono_Uwp(RemoteWebDriver Session, String color_optn) throws InterruptedException {
					   
		WebElement ColorModeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Color mode']");
		Assert.assertNotNull(ColorModeListComboBox);           
        if(!ColorModeListComboBox.getText().toString().contentEquals(color_optn)) 
        {
	        log.info("Desired photo color mode => "+color_optn+" <= is not selected so selecting it from drop down");
	        ColorModeListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(color_optn).click();
				Thread.sleep(2000);
				log.info("Selected desired Color Option *****" + ColorModeListComboBox.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Color Option is not found so either 1) your Printer does not support desired color option OR 2) you have typed the color option value incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired color mode but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            ColorModeListComboBox.click();
            }         
        }else {
			log.info("Desired photo color mode  => " + ColorModeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired duplex option
	public static void SelectDuplexOption_Uwp(RemoteWebDriver Session, String duplex_optn)throws MalformedURLException, InterruptedException {
		
		String duplex_sel = duplex_optn.toLowerCase();
		if (duplex_sel.equals("none")) {
			duplex_sel = "one side";
		} else if(duplex_sel.equals("shortedge")) {
			duplex_sel = "short edge";
		} else {
			duplex_sel = "long edge";
		}

		WebElement DuplexListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Duplex printing']");
		Assert.assertNotNull(DuplexListComboBox);
		if (!DuplexListComboBox.getText().toString().contentEquals(duplex_sel)) 
		{
			log.info("Desired duplex option => " + duplex_sel + " <= is not selected so selecting it from drop down");
			DuplexListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByXPath("//*[contains(@Name,'"+duplex_sel+"')]").click();
				Thread.sleep(1000);
				log.info("Selected desired duplex option *****" + DuplexListComboBox.getText().toString() + " - " +duplex_optn +"*****");
			} catch (Exception e) {
				log.info("Desired Duplex Option is not found so either 1) your Printer does not support desired duplex option OR 2) you have typed the duplex option value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting duplex option but continuing with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				DuplexListComboBox.click();
			}			
		} else {
			log.info("Desired duplex option => " + DuplexListComboBox.getText().toString()	+ " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Borderless Printing Option
	public static void SelectBorderless_Uwp(RemoteWebDriver Session, String borderless) throws MalformedURLException, InterruptedException {

		WebElement BorderlessListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Borderless printing']");
		Assert.assertNotNull(BorderlessListComboBox);
		if (!BorderlessListComboBox.getText().toString().contentEquals(borderless)) 
		{
			log.info("Desired Borderless option => " + borderless + " <= is not selected so selecting it from drop down");
			BorderlessListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(borderless).click();
				Thread.sleep(2000);
				log.info("Selected desired borderless option *****" + BorderlessListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("Desired Borderless option is not found so either 1) your Printer does not support desired borderless option OR 2) you have typed the borderless option value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting desired Borderless option but continuing with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				BorderlessListComboBox.click();
				
			}		
		} else {
			log.info("Desired Borderless option => " + BorderlessListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Paper Tray Option
	public static void SelectPaperTray_Uwp(RemoteWebDriver Session, String paper_tray) throws InterruptedException {
		   
		WebElement PaperTrayListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Paper tray']");
		Assert.assertNotNull(PaperTrayListComboBox);           
        if(!PaperTrayListComboBox.getText().toString().contentEquals(paper_tray)) 
		{
	        log.info("Desired Paper tray => "+paper_tray+" <= is not selected so selecting it from drop down");
	        PaperTrayListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(paper_tray).click();
				Thread.sleep(2000);
				log.info("Selected desired paper tray option *****" + PaperTrayListComboBox.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Paper tray is not found so either 1) your Printer does not support desired paper tray OR 2) you have typed the paper tray incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Paper Tray but continuing with rest of the print options");     
				//throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            PaperTrayListComboBox.click();
            }   			
        }else {
			log.info("Desired Paper Tray option => " + PaperTrayListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Paper Type Option
	public static void SelectPaperType_Uwp(RemoteWebDriver Session, String paper_type) throws InterruptedException {
		   
		WebElement PaperTypeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Paper type']");
		Assert.assertNotNull(PaperTypeListComboBox);           
        if(!PaperTypeListComboBox.getText().toString().contentEquals(paper_type)) 
		{
	        log.info("Desired photo paper type => "+paper_type+" <= is not selected so selecting it from drop down");
	        PaperTypeListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(paper_type).click();
				Thread.sleep(2000);
				log.info("Selected desired paper type option *****" + PaperTypeListComboBox.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Paper type is not found so either 1) your Printer does not support desired paper type OR 2) you have typed the paper type incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Paper Type but continuing with rest of the print options");     
				//throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            PaperTypeListComboBox.click();
            }   			
        }else {
			log.info("Desired Paper Type option => " + PaperTypeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Output Quality Option
	public static void SelectOutputQuality_Uwp(RemoteWebDriver Session, String output_qlty) throws InterruptedException {
		   
		WebElement OutputQualityListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Output quality']");
		Assert.assertNotNull(OutputQualityListComboBox);           
        if(!OutputQualityListComboBox.getText().toString().contentEquals(output_qlty)) 
		{
	        log.info("Desired photo output quality => "+output_qlty+" <= is not selected so selecting it from drop down");
	        OutputQualityListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(output_qlty).click();
				Thread.sleep(2000);
				log.info("Selected desired Photo Output Quality option *****" + OutputQualityListComboBox.getText().toString() + "*****");
			} catch(Exception e){
	        	log.info("Desired Photo Output Quality is not found so either 1) your Printer does not support desired output quality OR 2) you have typed the output quality tray incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Photo Output Quality but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            OutputQualityListComboBox.click();
            }   			
        }else {
			log.info("Desired Photo Output Quality option => " + OutputQualityListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Stapling Option
	public static void SelectStaplingOption_Uwp(RemoteWebDriver Session, String stapling_optn) throws InterruptedException {
		   
		WebElement SelectStaplingOption = Session.findElementByXPath("//ComboBox[@Name = 'Stapling']");
		Assert.assertNotNull(SelectStaplingOption);           
        if(!SelectStaplingOption.getText().toString().contentEquals(stapling_optn)) 
		{
	        log.info("Desired Stapling Option => "+stapling_optn+" <= is not selected so selecting it from drop down");
	        SelectStaplingOption.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(stapling_optn).click();
				Thread.sleep(2000);
				log.info("Selected desired Stapling Option *****" + SelectStaplingOption.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Stapling Option is not found so either 1) your Printer does not support desired stapling option OR 2) you have typed the stapling option incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Stapling Option but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            SelectStaplingOption.click();
            } 	
        }else {
			log.info("Desired Stapling Option => " + SelectStaplingOption.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Header and Footer Options
		public static void SelectHeadersAndFooters_Uwp(RemoteWebDriver Session, String headerandfooter_optn) throws InterruptedException {
			 
			WebElement HeadersAndFooters = Session.findElementByXPath("//ComboBox[@Name = 'Headers and footers']");
			Assert.assertNotNull(HeadersAndFooters);           
	        if(!HeadersAndFooters.getText().toString().contentEquals(headerandfooter_optn)) 
			{
		        log.info("Desired Header and Footer Option => "+headerandfooter_optn+" <= is not selected so selecting it from drop down");
		        HeadersAndFooters.click();
		        Thread.sleep(1000);
		        try {
					//Session.findElementByXPath("//ComboBoxItem[@Name = '"+headerandfooter_optn+"']").click();
		        	Session.findElementByName(headerandfooter_optn).click();
		        	Thread.sleep(2000);
					log.info("Selected desired Header and Footer Option *****" + HeadersAndFooters.getText().toString() + "*****");
	            } catch(Exception e){
		        	log.info("Desired Header and Footer Option is not found so either 1) your Printer does not support desired header and footer option OR 2) you have typed the header and footer option incorrectly in testsuite xml");
		        	//e.printStackTrace();
		            log.info("Error selecting desired Header and Footer Option but continuing with rest of the print options");     
		            //throw new RuntimeException(e);
		            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
		            HeadersAndFooters.click();
	            } 	
	        }else {
				log.info("Desired Header and Footer Option => " + HeadersAndFooters.getText().toString() + " <= is already selected so proceeding");
			}
		} 

		
	// Method to select desired Scaling Option
	public static void SelectScale_Uwp(RemoteWebDriver Session, String scale_optn) throws InterruptedException {
		   
		WebElement ScaleListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Scale']");
		Assert.assertNotNull(ScaleListComboBox);           
        if(!ScaleListComboBox.getText().toString().contentEquals(scale_optn)) 
		{
	        log.info("Desired Scaling Option => "+scale_optn+" <= is not selected so selecting it from drop down");
	        ScaleListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(scale_optn).click();
				Thread.sleep(2000);
				log.info("Selected desired Scaling and Footer Option *****" + ScaleListComboBox.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired scaling Option is not found so either 1) your Printer does not support desired scaling option OR 2) you have typed the scaling option incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired scaling Option but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            ScaleListComboBox.click();
            } 	
        }else {
			log.info("Desired Scaling Option => " + ScaleListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	} 
	
	
	// Method to select desired Page Range Option
	public static void SelectPageRange_Uwp(RemoteWebDriver Session, String page_range) throws InterruptedException {
		   
		WebElement PageRangeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Pages']");
		Assert.assertNotNull(PageRangeListComboBox);           
        if(!PageRangeListComboBox.getText().toString().contentEquals(page_range)) 
		{
	        log.info("Desired Page Range => "+page_range+" <= is not selected so selecting it from drop down");
	        PageRangeListComboBox.click();
	        Thread.sleep(1000);
	        try {
				if((!(page_range.equalsIgnoreCase("All pages"))) &&  (!(page_range.equalsIgnoreCase("Current page")))){
					
					// Selecting the Custom option for Pages to enter desired page range to print.
					Session.findElementByName("Custom range").click();
					Thread.sleep(2000);
					
					// Clicking on Page Range Edit box.
					Session.findElementByXPath("//*[@AutomationId = 'com.microsoft.JobCustomPageRange_ValueText']").click();
					Thread.sleep(2000);

					Session.getKeyboard().pressKey(page_range);
					Thread.sleep(1000);

					log.info("Entered page range value ***** " + page_range + " *****");
				}else{
					Session.findElementByName(page_range).click();
					Thread.sleep(2000);
				}
				log.info("Selected desired Page Range *****" + PageRangeListComboBox.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Page Range is not found so either 1) your Printer does not support desired Page Range OR 2) you have typed the Page Range option incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Page Range but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            PageRangeListComboBox.click();
            } 	
        }else {
			log.info("Desired Page Range => " + PageRangeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	} 
	
	
	 // Method to select desired Collation option  
	 public static void SelectCollation_Uwp(RemoteWebDriver Session, String collation) throws MalformedURLException, InterruptedException {
	 		 
	 	WebElement CollationListComboBox = Session.findElementByName("Collation");		 		
        Assert.assertNotNull(CollationListComboBox);           
        if(!CollationListComboBox.getText().toString().contentEquals(collation)) 
        {
	        log.info("Desired collation option => "+collation+" <= is not selected so selecting it from drop down");
	        CollationListComboBox.click();
	        Thread.sleep(1000);
	        try {
	        	CollationListComboBox.findElement(By.name(collation)).click();		
	        	Thread.sleep(2000);
		        log.info("Selected collation option *****" +CollationListComboBox.getText().toString()+"*****");
	        	}catch(Exception e){
	        	log.info("Desired collation option is not found so either 1) your Printer does not Support collation option OR 2) you have typed the collation option value incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting collation option but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            CollationListComboBox.click();
	        	}		        
	     } else {
	    	log.info("Desired collation option => " +CollationListComboBox.getText().toString()+" <= is already selected so proceeding");
        }
	 }		 		 
		 
}
