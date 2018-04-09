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
	// Possible candidate for re-factoring when there are multiple application
	// in automation
	public static void SelectDesiredPrinter_Photos(String ptr_name) throws MalformedURLException, InterruptedException {

		WebElement PrinterListComboBox = PhotosSession.findElementByClassName("ComboBox");
		Assert.assertNotNull(PrinterListComboBox);
		if (!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) {
			log.info("Desired printer is not selected so selecting it from drop down");
			PrinterListComboBox.click();
			Thread.sleep(1000);

			try {
				PhotosSession.findElement(By.name(ptr_name)).click();
			} catch (Exception e) {
				log.info(
						"Printer under test is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name correctly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting printer under test");
				throw new RuntimeException(e);
			}
			Thread.sleep(1000);
			log.info("Selected desired printer =>" + PrinterListComboBox.getText().toString());
			
		} else {
			// PhotosSession.findElementByXPath("//ComboBoxItem[@Name =
			// '"+ptr_name+"']").click();
			log.info("Desired printer => " + PrinterListComboBox.getText().toString() + " <= is already selected so proceeding");
		}

	}

	
	// Method to select desired Copies option
	// Possible candidate for changing approach
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
	// Possible candidate for re-factoring when there are multiple application
	// in automation
	public static void SelectOrientation_Photos(String orientation) throws MalformedURLException, InterruptedException {

		WebElement OrientationListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Orientation']");
		Assert.assertNotNull(OrientationListComboBox);
		if (!OrientationListComboBox.getText().toString().contentEquals(orientation)) {
			log.info("Desired Orientation option => " + orientation + " <= is not selected so selecting it from drop down");
			OrientationListComboBox.click();
			Thread.sleep(1000);
			
			try {
				OrientationListComboBox.findElement(By.name(orientation)).click();
			} catch (Exception e) {
				log.info(
						"Desired Orientation option is not found so make sure Printer Support this orientation option OR have typed the orientation option name incorrectly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting orientation option");
				throw new RuntimeException(e);
			}
			Thread.sleep(1000);
			log.info("Selected desired orientation option *****" + OrientationListComboBox.getText().toString() + "*****");
			
		} else {
			log.info("Desired orientation option => " + OrientationListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired paper size
	// Possible candidate for re-factoring when there are multiple application
	// in automation
	public static void SelectPaperSize_Photos(String paper_size) throws MalformedURLException, InterruptedException {
		
			WebElement PaperSizeListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Paper size']");
			Assert.assertNotNull(PaperSizeListComboBox);
			if (!PaperSizeListComboBox.getText().toString().contentEquals(paper_size)) {
				log.info("Desired paper size => " + paper_size + " <= is not selected so selecting it from drop down");
				PaperSizeListComboBox.click();
				Thread.sleep(1000);
				
				try {
					PaperSizeListComboBox.findElement(By.name(paper_size)).click();
					// Its big List so if needed Scroll Down Twice (if needed) - TBD
				} catch (Exception e) {
					log.info("Desired Paper Size is not found so make sure Printer Support this paper size OR have typed the paper size name incorrectly in testsuite xml");
					e.printStackTrace();
					log.info("Error selecting desired paper size");
					throw new RuntimeException(e);
				}
				Thread.sleep(1000);
				log.info("Selected desired paper size *****" + PaperSizeListComboBox.getText().toString() + "*****");
				
			} else {
				log.info("Desired paper size => " + PaperSizeListComboBox.getText().toString() + " <= is already selected so proceeding");
			}
		}
	
	// Method to select desired photo size
	// Possible candidate for re-factoring when there are multiple application
	// in automation
	public static void SelectPhotoSize_Photos(String photo_size) throws MalformedURLException, InterruptedException {

		WebElement PhotoSizeListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Photo size']");
		Assert.assertNotNull(PhotoSizeListComboBox);
		if (!PhotoSizeListComboBox.getText().toString().contentEquals(photo_size)) {
			log.info("Desired photo size => " + photo_size + " <= is not selected so selecting it from drop down");
			PhotoSizeListComboBox.click();
			Thread.sleep(1000);
			
			try {
				PhotoSizeListComboBox.findElement(By.name(photo_size)).click();
				// Its big List so if needed Scroll Down Twice (if needed) - TBD
			} catch (Exception e) {
				log.info("Desired Photo Size is not found so make sure Printer Support this photo size OR have typed the photo size name incorrectly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting desired photo size");
				throw new RuntimeException(e);
			}
			Thread.sleep(1000);
			log.info("Selected desired photo size *****" + PhotoSizeListComboBox.getText().toString() + "*****");
			
		} else {
			log.info("Desired photo size => " + PhotoSizeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Fit
	// Possible candidate for re-factoring when there are multiple application
	// in automation
	public static void SelectPhotoFit_Photos(String photo_fit) throws MalformedURLException, InterruptedException {

		WebElement PhotoFitListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Fit']");
		Assert.assertNotNull(PhotoFitListComboBox);
		if (!PhotoFitListComboBox.getText().toString().contentEquals(photo_fit)) {
			log.info("Desired photo fit => " + photo_fit + " <= is not selected so selecting it from drop down");
			PhotoFitListComboBox.click();
			Thread.sleep(1000);
			
			try {
				PhotoFitListComboBox.findElement(By.name(photo_fit)).click();
			} catch (Exception e) {
				log.info("Desired Photo Fit is not found so make sure Printer Support this photo Fit OR have typed the photo Fit name incorrectly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting desired photo fit");
				throw new RuntimeException(e);
			}
			Thread.sleep(1000);
			log.info("Selected desired photo fit *****" + PhotoFitListComboBox.getText().toString() + "*****");
			
		} else {
			log.info("Desired photo fit => " + PhotoFitListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Margins
	// Possible candidate for re-factoring when there are multiple application
	// in automation
	public static void SelectPageMargins_Photos(String page_margins)
			throws MalformedURLException, InterruptedException {

		WebElement PhotoPageMarginListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Page Margins']");
		Assert.assertNotNull(PhotoPageMarginListComboBox);
		if (!PhotoPageMarginListComboBox.getText().toString().contentEquals(page_margins)) {
			log.info("Desired Photo Margins => " + page_margins + " <= is not selected so selecting it from drop down");
			PhotoPageMarginListComboBox.click();
			Thread.sleep(1000);
			
			try {
				PhotoPageMarginListComboBox.findElement(By.name(page_margins)).click();
			} catch (Exception e) {
				log.info("Desired Photo Margins is not found so make sure Printer Support this photo margins OR have typed the photo margins name incorrectly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting desired photo fit");
				throw new RuntimeException(e);
			}
			Thread.sleep(1000);
			log.info("Selected desired photo fit *****" + PhotoPageMarginListComboBox.getText().toString() + "*****");
			
		} else {
			log.info("Desired photo fit => " + PhotoPageMarginListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
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

	
	public static void CloseMoreSettings_Photos() throws InterruptedException {
		PhotosSession.findElementByXPath("//Button[@Name = 'Ok']").click();
		log.info("Clicked 'OK' button successfully.");
		Thread.sleep(2000);
	}

	
	public static void SelectColorOrMono_Photos(String color_optn) throws InterruptedException {
					   
		WebElement PhotoColorModeListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Color mode']");
		Assert.assertNotNull(PhotoColorModeListComboBox);           
        if(!PhotoColorModeListComboBox.getText().toString().contentEquals(color_optn)) 
        {
	        log.info("Desired photo color mode => "+color_optn+" <= is not selected so selecting it from drop down");
	        PhotoColorModeListComboBox.click();
	        Thread.sleep(1000);
	        
          try {
        	  PhotoColorModeListComboBox.findElement(By.name(color_optn)).click();
               log.info("'" + color_optn + "' is selected successfully.");
            } catch(Exception e){
	        	log.info("Desired Photo Color mode is not found so make sure Printer Support this photo color mode OR have typed the photo color mode name incorrectly in testsuite xml");
	        	e.printStackTrace();
	            log.info("Error selecting desired color mode");     
	            throw new RuntimeException(e);
            }
         
        }else {
			log.info("Desired photo color mode  => " + PhotoColorModeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

		
	// Method to select desired duplex option
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectDuplexOption_Photos(String duplex_optn)
			throws MalformedURLException, InterruptedException {
		
		String duplex_sel = duplex_optn.toLowerCase();

		if (duplex_sel.equals("none")) {
			duplex_sel = "Print on only one side of the page";
		} else if(duplex_sel.equals("shortedge")) {
			duplex_sel = "Flip the short edge";
		} else {
			duplex_sel = "Flip the long edge";
		}

		WebElement PhotoDuplexListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Duplex printing']");
		Assert.assertNotNull(PhotoDuplexListComboBox);
		if (!PhotoDuplexListComboBox.getText().toString().contentEquals(duplex_sel)) {
			log.info("Desired duplex option => " + duplex_sel + " <= is not selected so selecting it from drop down");
			PhotoDuplexListComboBox.click();
			Thread.sleep(1000);
			
			try {
				PhotosSession.findElementByXPath("//*[contains(@Name,'"+duplex_sel+"')]").click();
			} catch (Exception e) {
				log.info("Desired duplex option is not found so make sure Printer Support this duplex option OR have typed the duplex option name incorrectly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting duplex option");
				throw new RuntimeException(e);
			}
			Thread.sleep(1000);
			log.info("Selected desired duplex option *****" + PhotoDuplexListComboBox.getText().toString() + " - " +duplex_optn +"*****");
			
		} else {
			log.info("Desired duplex option => " + PhotoDuplexListComboBox.getText().toString()	+ " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Borderless Printing Option
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectBorderless_Photos(String borderless) throws MalformedURLException, InterruptedException {

		WebElement PhotoBorderlessListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Borderless printing']");
		Assert.assertNotNull(PhotoBorderlessListComboBox);
		if (!PhotoBorderlessListComboBox.getText().toString().contentEquals(borderless)) {
			log.info("Desired Borderless option => " + borderless + " <= is not selected so selecting it from drop down");
			PhotoBorderlessListComboBox.click();
			Thread.sleep(1000);
			
			try {
				PhotoBorderlessListComboBox.findElement(By.name(borderless)).click();
			} catch (Exception e) {
				log.info("Desired Borderless option is not found so make sure Printer Support this photo borderless option OR have typed the photo borderless name incorrectly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting desired Borderless option");
				throw new RuntimeException(e);
			}
			Thread.sleep(1000);
			log.info("Selected desired borderless option *****" + PhotoBorderlessListComboBox.getText().toString() + "*****");
		
		} else {
			log.info("Desired Borderless option => " + PhotoBorderlessListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Paper Tray Option
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectPaperTray_Photos(String paper_tray) throws InterruptedException {
		   
		WebElement PhotoPaperTrayListComboBox = PhotosSession.findElementByXPath("//ComboBox[@Name = 'Paper tray']");
		Assert.assertNotNull(PhotoPaperTrayListComboBox);           
        if(!PhotoPaperTrayListComboBox.getText().toString().contentEquals(paper_tray)) {
	        log.info("Desired photo paper tray => "+paper_tray+" <= is not selected so selecting it from drop down");
	        PhotoPaperTrayListComboBox.click();
	        Thread.sleep(1000);
	        
          try {
        	  PhotoPaperTrayListComboBox.findElement(By.name(paper_tray)).click();              
            } catch(Exception e){
	        	log.info("Desired Photo Paper tray is not found so make sure Printer Support this photo paper tray option OR have typed the photo paper tray name incorrectly in testsuite xml");
	        	e.printStackTrace();
	            log.info("Error selecting desired Paper Tray");     
	            throw new RuntimeException(e);
            }   
          	Thread.sleep(1000);
			log.info("Selected desired paper tray option *****" + PhotoPaperTrayListComboBox.getText().toString() + "*****");
			
        }else {
			log.info("Desired Paper Tray option => " + PhotoPaperTrayListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
}
