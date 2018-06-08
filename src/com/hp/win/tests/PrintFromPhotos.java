package com.hp.win.tests;


import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.PhotoAppBase;
import com.hp.win.core.UwpAppBase;
import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xpath.axes.WalkingIteratorSorted;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hp.win.utility.*;							
import java.io.IOException;
import java.net.URL;

	@Listeners({ScreenshotUtility.class})
	public class PrintFromPhotos extends PhotoAppBase {
		private static final Logger log = LogManager.getLogger(PrintFromPhotos.class);
		private static String currentClass;							
		public static RemoteWebDriver PhotosSession = null;
		static WebDriverWait wait;
		
		@BeforeClass
		@Parameters({ "device_name", "ptr_name", "test_filename"})
	    public static void setup(String device_name, String ptr_name, @Optional("Rose_Portrait.jpg")String test_filename) throws Throwable { 
	       
		currentClass = PrintFromPhotos.class.getSimpleName();
				
		//Start PrintTrace log capturing 
		PrintTraceCapture.StartLogCollection(currentClass);											   
	    PhotosSession = PhotoAppBase.OpenPhotosFile(device_name, test_filename);
	    
	    //Verify if the test folder is already added, if not add it.
		PhotoAppBase.VerifyAddTestFolder(device_name, test_filename, testfiles_loc);
		
		}
		
		
	@Test
	@Parameters({ "ptr_name", "test_filename","copies","orientation","paper_size","photo_size","photo_fit","page_margins","color_optn","duplex_optn","borderless","paper_tray","paper_type","output_qlty","stapling_optn"})
    public void PrintPhotos(String ptr_name, String test_filename, @Optional("1")String copies, @Optional("Portrait")String orientation, @Optional("Letter")String paper_size,  @Optional("Full page")String photo_size,  @Optional("Fill page")String photo_fit,  @Optional("Normal")String page_margins,  @Optional("Color")String color_optn,  @Optional("None")String duplex_optn,  @Optional("On")String borderless,  @Optional("Auto select")String paper_tray, @Optional("Plain Paper")String paper_type, @Optional("Normal")String output_qlty, @Optional("Staple")String stapling_optn) throws InterruptedException, IOException
    {
    	
		// Method to Print Photo File to Printer Under Test
		PhotoAppBase.PrintPhoto(ptr_name,test_filename);
		
		// Method to select the desired printer.
		PhotoAppBase.SelectDesiredPrinter(PhotosSession, ptr_name);

		//Enter desired Copies value.
		if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Copies']").size()!=0){
			PhotoAppBase.SelectCopies_Uwp(PhotosSession,copies);
		}else{
			log.info("The desired Copies selection feature is not supported by the printer ");
		}
	
		
		//Select Desired Orientation Option.
		if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Orientation']").size()!=0){
			PhotoAppBase.SelectOrientation_Uwp(PhotosSession, orientation);
		}else{
			log.info("The desired Orientation feature is not supported by the printer ");
		}
		
		//Select Desired Paper Size Option
		if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Paper size']").size()!=0){
			PhotoAppBase.SelectPaperSize_Uwp(PhotosSession, paper_size);
		}else{
			log.info("The desired Paper Size feature is not supported by the printer ");
		}
		
		//Select Desired Photo Size Option.
		if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Photo size']").size()!=0){
			PhotoAppBase.SelectPhotoSize_Photos(PhotosSession, photo_size);
		}else{
			log.info("The desired Photo Size feature is not supported by the printer ");
		}
		
		//Select Desired Page Margins Option.
		if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Page Margins']").size()!=0){
			PhotoAppBase.SelectPageMargins_Uwp(PhotosSession, page_margins);
		}else{
			log.info("The desired Page Margin feature is not supported by the printer ");
		}
		
		//Select Desired Photo Fit Option.
		if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Fit']").size()!=0){
			PhotoAppBase.SelectPhotoFit_Photos(PhotosSession, photo_fit);
		}else{
			log.info("The desired Photo Fit feature is not supported by the printer ");
		}
					
		//Select Desired Borderless Printing Option.
		if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Borderless printing']").size()!=0){
			PhotoAppBase.SelectBorderless_Uwp(PhotosSession, borderless);
		}else{
			log.info("The desired Borderless feature is not supported by the printer ");
		}
		
		//Opening more settings to access more printing options.
		int MoreSettings = UwpAppBase.OpenMoreSettings(PhotosSession);
		if	(MoreSettings == 1){	
			
			//Select Desired Duplex Option.			
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Duplex printing']").size()!=0){
				PhotoAppBase.SelectDuplexOption_Uwp(PhotosSession, duplex_optn);
			}else{
				log.info("The desired Duplexing feature is not supported by the printer ");
			}
			
			//Select Desired Paper Type Option.			
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Paper type']").size()!=0){
				PhotoAppBase.SelectPaperType_Uwp(PhotosSession, paper_type);
			}else{
				log.info("The desired Paper Type feature is not supported by the printer ");
			}
			
			//Select Desired Paper Tray Option.			
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Paper tray']").size()!=0){
				PhotoAppBase.SelectPaperTray_Uwp(PhotosSession, paper_tray);
			}else{
				log.info("The desired Paper Tray feature is not supported by the printer ");
			}
							
			//Select Desired Output Quality Option.			
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Output quality']").size()!=0){
				PhotoAppBase.SelectOutputQuality_Uwp(PhotosSession, output_qlty);
			}else{
				log.info("The desired Paper Type feature is not supported by the printer ");
			}
			
			//Select Desired Color Mode Option.			
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Color mode']").size()!=0){
				PhotoAppBase.SelectColorOrMono_Uwp(PhotosSession, color_optn);
			}else{
				log.info("The desired Color feature is not supported by the printer ");
			}
			
			//Select Desired Stapling Option.			
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Stapling']").size()!=0){
				PhotoAppBase.SelectStaplingOption_Uwp(PhotosSession, stapling_optn);
			}else{
				log.info("The desired Stapling feature is not supported by the printer ");
			}
			
			//Closing more settings after accessing more printing options.
			PhotoAppBase.CloseMoreSettings(PhotosSession);
			
		}else{
			log.info("More Settings page could not be opened");
			PhotosSession.findElementByXPath("//Button[@AutomationId = 'CloseButton']").click();
			Assert.fail();
		}
			
			// Tap on Print button(Give Print)
	    	PhotosSession.findElementByXPath("//Button[@AutomationId = 'PrintButton']").click();
	    	log.info("Clicked on final Print button -> Print option Successfully");
		    
	    }
 
		
	@Test(dependsOnMethods = { "PrintPhotos" })
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
		// Open Print Queue
		Base.OpenPrintQueue(ptr_name);
				
		// Method to attach session to Printer Queue Window
		Base.SwitchToPrinterQueue(device_name,ptr_name);
		
	    log.info("Expected queued job should be => "+test_filename);
	    
	    //Validate Print Job Queued up
	    try {
	    	Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
	    }catch(NoSuchElementException e) {
	    	log.info("Expected Print job is not found in print queue");
	     	throw new RuntimeException(e);
	    }catch(Exception e) {
	    	log.info("Error validating print job in print queue");
	    	throw new RuntimeException(e);
	    }
	    
	    log.info("Found correct job in print queue => "+test_filename);
	    
	    //Waiting for the job to get spooled completely before closing the print queue window.
	    wait = new WebDriverWait(PrintQueueSession,60);
	    wait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("//ListItem[@AutomationId='ListViewItem-1']"),"Spooling"));
	    log.info("Waiting until the job spooling is completed");
	    
	    PrintQueueSession.close();
	    log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");	    
	    Thread.sleep(1000);
	}
    
	
    @AfterClass(alwaysRun=true)
    public static void TearDown() throws IOException, InterruptedException 
    {	        
    
		if (PhotosSession!= null)
		{
			PhotosSession.quit();
		}
		
		if (DesktopSession!=null)
		{
			DesktopSession.quit();
		}
		
		if (PrintQueueSession!=null)
		{
			PrintQueueSession.quit();
		}
				
		//Stop PrintTrace log capturing.
		PrintTraceCapture.StopLogCollection(currentClass);					   
    }
}
