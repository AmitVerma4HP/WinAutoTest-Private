package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;



public class PhotoAppBase extends Base {
		private static final Logger log = LogManager.getLogger(PhotoAppBase.class);
		public static RemoteWebDriver PhotosSession = null;
		

	// Method to open Photos test file
	public static RemoteWebDriver OpenPhotosFile(String device_name, String test_filename)
			throws MalformedURLException {

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
			log.info("Error opening Photos app");
		}

		// log.info("Opened"+test_filename+"file from "+testfiles_loc);
		log.info("Opened PhotosSession successfully");
		return PhotosSession;

	}

	
	// Method to print from Photos
	public static void PrintPhoto(String ptr_name, String test_filename) throws InterruptedException {
		// Go to Folders Tab
		PhotosSession.findElementByName("Folders").click();
		log.info("Clicked on Folder Menu Successfully in PhotoApp");
		Thread.sleep(1000);

		// Search for Saved Pictures folder.
		PhotosSession.findElementByName("Search").sendKeys("testfiles");
		log.info("Searching \"Test Folder - testfiles\"");
		Thread.sleep(1000);

		// Click on Saved Pictures
		PhotosSession.findElementByXPath("//Button[@Name = \"testfiles\"]").click();
		log.info("Clicked on \"Test Folder - testfiles\"");
		Thread.sleep(1000);

		// Select the Photo file
		PhotosSession.findElementByXPath("//Button[@AutomationId = '" + test_filename + "']").click();
		log.info("Selected the Photo file for printing");
		Thread.sleep(1000);

		// Tap on Print icon
		PhotosSession.findElementByName("Print").click();
		log.info("Clicked on Print Icon Successfully in PhotoApp");
		Thread.sleep(1000);

	}

	
	// Method to select desired printer from printers list combo box
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectDesiredPrinter_Photos(String ptr_name) throws MalformedURLException, InterruptedException {

		WebElement PrinterListComboBox = PhotosSession.findElementByClassName("ComboBox");
		Assert.assertNotNull(PrinterListComboBox);
		if (!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) 
		{
			log.info("Desired printer => "+ptr_name+" is not selected so selecting it from drop down");
			PrinterListComboBox.click();
			Thread.sleep(1000);
			try {
				PhotosSession.findElement(By.name(ptr_name)).click();
				Thread.sleep(1000);
				log.info("Selected desired printer *****" +PrinterListComboBox.getText().toString()+"*****");
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
	public static void SelectCopies_Photos(String copies) throws MalformedURLException, InterruptedException {

		// Clicking on Copies Edit box.
		PhotosSession.findElementByXPath("//*[contains(@AutomationId,'JobCopiesAllDocuments_NumberText')]").click();
		Thread.sleep(2000);

		PhotosSession.getKeyboard().pressKey(Keys.CLEAR);
		PhotosSession.getKeyboard().pressKey(Keys.BACK_SPACE);
		Thread.sleep(1000);

		PhotosSession.getKeyboard().pressKey(copies);
		Thread.sleep(1000);

		log.info("Entered copies value ***** " + copies + " *****");

	}

	
	// Method to select desired orientation
	public static void SelectOrientation_Photos(String orientation) throws MalformedURLException, InterruptedException {

		WebElement OrientationListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Orientation']");
		Assert.assertNotNull(OrientationListComboBox);
		if (!OrientationListComboBox.getText().toString().contentEquals(orientation)) 
		{
			log.info("Desired Orientation option => " + orientation + " <= is not selected so selecting it from drop down");
			OrientationListComboBox.click();
			Thread.sleep(1000);
			try {
				PhotosSession.findElementByName(orientation).click();
				Thread.sleep(1000);
				log.info("Selected desired orientation option *****" + OrientationListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("Desired Orientation option is not found so either 1) your Printer does not support desired orientation OR 2) you have typed the orientation value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting orientation option but continuing test with rest of the print options");
				//throw new RuntimeException(e);
				OrientationListComboBox.click();
			}			
		} else {
			log.info("Desired orientation option => " + OrientationListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired paper size
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectPaperSize_Photos(String paper_size) throws MalformedURLException, InterruptedException {
		
		WebElement PaperSizeListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Paper size']");
		Assert.assertNotNull(PaperSizeListComboBox);
		if (!PaperSizeListComboBox.getText().toString().contentEquals(paper_size)) 
		{
			log.info("Desired paper size => " + paper_size + " <= is not selected so selecting it from drop down");
			PaperSizeListComboBox.click();
			Thread.sleep(1000);
			try {
				PhotosSession.findElementByName(paper_size).click();
				Thread.sleep(1000);
				log.info("Selected desired paper size *****" + PaperSizeListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("Desired Paper Size is not found so either 1) your Printer does not support desired paper size OR 2) you have typed the paper size value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting paper size option but continuing test with rest of the print options");
				//throw new RuntimeException(e);
				PaperSizeListComboBox.click();
			}				
		} else {
			log.info("Desired paper size => " + PaperSizeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	// Method to select desired photo size
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectPhotoSize_Photos(String photo_size) throws MalformedURLException, InterruptedException {

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
				PhotoSizeListComboBox.click();
			}
		} else {
			log.info("Desired photo size => " + PhotoSizeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Fit
	public static void SelectPhotoFit_Photos(String photo_fit) throws MalformedURLException, InterruptedException {

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
					PhotoFitListComboBox.click();
				}			
		} else {
			log.info("Desired photo fit => " + PhotoFitListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Margins
	public static void SelectPageMargins_Photos(String page_margins)throws MalformedURLException, InterruptedException {

		WebElement PhotoPageMarginListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Page Margins']");
		Assert.assertNotNull(PhotoPageMarginListComboBox);
		if (!PhotoPageMarginListComboBox.getText().toString().contentEquals(page_margins)) 
		{
			log.info("Desired Photo Margins => " + page_margins + " <= is not selected so selecting it from drop down");
			PhotoPageMarginListComboBox.click();
			Thread.sleep(1000);
			try {
				PhotosSession.findElementByName(page_margins).click();
				Thread.sleep(1000);
				log.info("Selected desired photo margins *****" + PhotoPageMarginListComboBox.getText().toString() + "*****");		
			} catch (Exception e) {
				log.info("Desired Page Margins is not found so either 1) your Printer does not support desired page margins OR 2) you have typed the page margins value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting desired photo fit but continuing with rest of the print options");
				//throw new RuntimeException(e);
				PhotoPageMarginListComboBox.click();
			}			
		} else {
			log.info("Desired photo fit => " + PhotoPageMarginListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	// Method to select More Oprions link to access more print options.
	public static int OpenMoreSettings_Photos() throws InterruptedException {
		try {
			PhotosSession.findElementByName("More settings").click();
			log.info("Clicked 'More Settings' link successfully");
			return 1;
		} catch (Exception e){
			log.info("More settings Option Not Found");
			return 0;
		}
		
	}

	// Method to return from More Oprions screen to access print button.
	public static void CloseMoreSettings_Photos() throws InterruptedException {
		PhotosSession.findElementByXPath("//Button[@Name = 'Ok']").click();
		log.info("Clicked 'OK' button successfully.");
		Thread.sleep(2000);
	}

	// Method to select Color options.
	public static void SelectColorOrMono_Photos(String color_optn) throws InterruptedException {
					   
		WebElement PhotoColorModeListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Color mode']");
		Assert.assertNotNull(PhotoColorModeListComboBox);           
        if(!PhotoColorModeListComboBox.getText().toString().contentEquals(color_optn)) 
        {
	        log.info("Desired photo color mode => "+color_optn+" <= is not selected so selecting it from drop down");
	        PhotoColorModeListComboBox.click();
	        Thread.sleep(1000);
	        try {
				PhotosSession.findElementByName(color_optn).click();
				Thread.sleep(1000);
				log.info("Selected desired Color Option *****" + PhotoColorModeListComboBox.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Color Option is not found so either 1) your Printer does not support desired color option OR 2) you have typed the color option value incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired color mode but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            PhotoColorModeListComboBox.click();
            }         
        }else {
			log.info("Desired photo color mode  => " + PhotoColorModeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	// Method to select desired duplex option
	public static void SelectDuplexOption_Photos(String duplex_optn)throws MalformedURLException, InterruptedException {
		
		String duplex_sel = duplex_optn.toLowerCase();
		if (duplex_sel.equals("none")) {
			duplex_sel = "one side";
		} else if(duplex_sel.equals("shortedge")) {
			duplex_sel = "short edge";
		} else {
			duplex_sel = "long edge";
		}

		WebElement PhotoDuplexListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Duplex printing']");
		Assert.assertNotNull(PhotoDuplexListComboBox);
		if (!PhotoDuplexListComboBox.getText().toString().contentEquals(duplex_sel)) 
		{
			log.info("Desired duplex option => " + duplex_sel + " <= is not selected so selecting it from drop down");
			PhotoDuplexListComboBox.click();
			Thread.sleep(1000);
			try {
				PhotosSession.findElementByXPath("//*[contains(@Name,'"+duplex_sel+"')]").click();
				Thread.sleep(1000);
				log.info("Selected desired duplex option *****" + PhotoDuplexListComboBox.getText().toString() + " - " +duplex_optn +"*****");
			} catch (Exception e) {
				log.info("Desired Duplex Option is not found so either 1) your Printer does not support desired duplex option OR 2) you have typed the duplex option value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting duplex option but continuing with rest of the print options");
				//throw new RuntimeException(e);
				PhotoDuplexListComboBox.click();
			}			
		} else {
			log.info("Desired duplex option => " + PhotoDuplexListComboBox.getText().toString()	+ " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Borderless Printing Option
	public static void SelectBorderless_Photos(String borderless) throws MalformedURLException, InterruptedException {

		WebElement PhotoBorderlessListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Borderless printing']");
		Assert.assertNotNull(PhotoBorderlessListComboBox);
		if (!PhotoBorderlessListComboBox.getText().toString().contentEquals(borderless)) 
		{
			log.info("Desired Borderless option => " + borderless + " <= is not selected so selecting it from drop down");
			PhotoBorderlessListComboBox.click();
			Thread.sleep(1000);
			try {
				PhotosSession.findElementByName(borderless).click();
				Thread.sleep(1000);
				log.info("Selected desired borderless option *****" + PhotoBorderlessListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("Desired Borderless option is not found so either 1) your Printer does not support desired borderless option OR 2) you have typed the borderless option value incorrectly in testsuite xml");
				//e.printStackTrace();
				log.info("Error selecting desired Borderless option but continuing with rest of the print options");
				//PhotosSession.findElementByXPath("//ComboBoxLightDismiss[@Name = 'Close']").click();
				PhotoBorderlessListComboBox.click();
				//throw new RuntimeException(e);
			}		
		} else {
			log.info("Desired Borderless option => " + PhotoBorderlessListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Paper Tray Option
	public static void SelectPaperTray_Photos(String paper_tray) throws InterruptedException {
		   
		WebElement PhotoPaperTrayListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Paper tray']");
		Assert.assertNotNull(PhotoPaperTrayListComboBox);           
        if(!PhotoPaperTrayListComboBox.getText().toString().contentEquals(paper_tray)) 
		{
	        log.info("Desired photo paper tray => "+paper_tray+" <= is not selected so selecting it from drop down");
	        PhotoPaperTrayListComboBox.click();
	        Thread.sleep(1000);
	        try {
				PhotosSession.findElementByName(paper_tray).click();
				Thread.sleep(1000);
				log.info("Selected desired paper tray option *****" + PhotoPaperTrayListComboBox.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Photo Paper tray is not found so either 1) your Printer does not support desired paper tray OR 2) you have typed the paper tray incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Paper Tray but continuing with rest of the print options");     
				//throw new RuntimeException(e);
	            PhotoPaperTrayListComboBox.click();
            }   			
        }else {
			log.info("Desired Paper Tray option => " + PhotoPaperTrayListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	// Method to select desired Paper Type Option
	public static void SelectPaperType_Photos(String paper_type) throws InterruptedException {
		   
		WebElement PhotoPaperTypeListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Paper type']");
		Assert.assertNotNull(PhotoPaperTypeListComboBox);           
        if(!PhotoPaperTypeListComboBox.getText().toString().contentEquals(paper_type)) 
		{
	        log.info("Desired photo paper type => "+paper_type+" <= is not selected so selecting it from drop down");
	        PhotoPaperTypeListComboBox.click();
	        Thread.sleep(1000);
	        try {
				PhotosSession.findElementByName(paper_type).click();
				Thread.sleep(1000);
				log.info("Selected desired paper type option *****" + PhotoPaperTypeListComboBox.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Paper type is not found so either 1) your Printer does not support desired paper type OR 2) you have typed the paper type incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Paper Type but continuing with rest of the print options");     
				//throw new RuntimeException(e);
	            PhotoPaperTypeListComboBox.click();
            }   			
        }else {
			log.info("Desired Paper Type option => " + PhotoPaperTypeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	// Method to select desired Output Quality Option
	public static void SelectOutputQuality_Photos(String output_qlty) throws InterruptedException {
		   
		WebElement PhotoOutputQualityListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Output quality']");
		Assert.assertNotNull(PhotoOutputQualityListComboBox);           
        if(!PhotoOutputQualityListComboBox.getText().toString().contentEquals(output_qlty)) 
		{
	        log.info("Desired photo output quality => "+output_qlty+" <= is not selected so selecting it from drop down");
	        PhotoOutputQualityListComboBox.click();
	        Thread.sleep(1000);
	        try {
				PhotosSession.findElementByName(output_qlty).click();
				Thread.sleep(1000);
				log.info("Selected desired Photo Output Quality option *****" + PhotoOutputQualityListComboBox.getText().toString() + "*****");
			} catch(Exception e){
	        	log.info("Desired Photo Output Quality is not found so either 1) your Printer does not support desired output quality OR 2) you have typed the output quality tray incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Photo Output Quality but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            PhotoOutputQualityListComboBox.click();
            }   			
        }else {
			log.info("Desired Photo Output Quality option => " + PhotoOutputQualityListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Stapling Option
	public static void SelectStaplingOption_Photos(String stapling_optn) throws InterruptedException {
		   
		WebElement SelectStaplingOption_Photos = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Stapling']");
		Assert.assertNotNull(SelectStaplingOption_Photos);           
        if(!SelectStaplingOption_Photos.getText().toString().contentEquals(stapling_optn)) 
		{
	        log.info("Desired photo Stapling Option => "+stapling_optn+" <= is not selected so selecting it from drop down");
	        SelectStaplingOption_Photos.click();
	        Thread.sleep(1000);
	        try {
				PhotosSession.findElementByName(stapling_optn).click();
				Thread.sleep(1000);
				log.info("Selected desired Photo Stapling Option *****" + SelectStaplingOption_Photos.getText().toString() + "*****");
            } catch(Exception e){
	        	log.info("Desired Photo Stapling Option is not found so either 1) your Printer does not support desired stapling option OR 2) you have typed the stapling option incorrectly in testsuite xml");
	        	//e.printStackTrace();
	            log.info("Error selecting desired Photo Stapling Option but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            SelectStaplingOption_Photos.click();
            } 	
        }else {
			log.info("Desired Photo Stapling Option => " + SelectStaplingOption_Photos.getText().toString() + " <= is already selected so proceeding");
		}
	}
}
