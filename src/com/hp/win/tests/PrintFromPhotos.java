package com.hp.win.tests;


import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.utility.ScreenshotUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

	@Listeners({ScreenshotUtility.class})
	public class PrintFromPhotos extends Base {
		private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);
	   
		@BeforeClass
		@Parameters({ "device_name", "ptr_name", "test_filename"})
	    public static void setup(String device_name, String ptr_name, @Optional("Rose_Portrait.jpg")String test_filename) throws InterruptedException, IOException { 
	       
	    PhotosSession = Base.OpenPhotosFile(device_name, test_filename);
	    Thread.sleep(1000);
         
	    }
	
		
		@Test
		@Parameters({ "ptr_name", "test_filename","copies","orientation","paper_size","photo_size","photo_fit","page_margins","color_optn","duplex_optn","borderless","paper_tray"})
	    public void PrintPhotos(String ptr_name, String test_filename, String copies, String orientation, @Optional("Letter")String paper_size,  @Optional("Full page")String photo_size,  @Optional("Fill page")String photo_fit,  @Optional("Normal")String page_margins,  @Optional("Color")String color_optn,  @Optional("None")String duplex_optn,  @Optional("On")String borderless,  @Optional("Auto select")String paper_tray) throws InterruptedException, IOException
	    {
	    	
			// Method to Print Photo File to Printer Under Test
			Base.PrintPhoto(ptr_name,test_filename);
			
			// Method to select the desired printer.
			Base.SelectDesiredPrinter_Photos(ptr_name);
			
			//Enter desired Copies value.
			Base.SelectCopies_Photos(copies);
			
			//Select Desired Orientation Option.
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Orientation']").size()!=0){
				Base.SelectOrientation_Photos(orientation);
			}else{
				log.info("The desired Orientation feature is not supported by the printer ");
			}
	
			//Select Desired Paper Size Option
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Paper size']").size()!=0){
				Base.SelectPaperSize_Photos(paper_size);
			}else{
				log.info("The desired Paper Size feature is not supported by the printer ");
			}
			
			//Select Desired Photo Size Option.
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Photo size']").size()!=0){
				Base.SelectPhotoSize_Photos(photo_size);
			}else{
				log.info("The desired Photo Size feature is not supported by the printer ");
			}
			
			//Select Desired Photo Fit Option.
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Fit']").size()!=0){
				Base.SelectPhotoFit_Photos(photo_fit);
			}else{
				log.info("The desired Photo Fit feature is not supported by the printer ");
			}
			
			//Select Desired Page Margins Option.
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Page Margins']").size()!=0){
				Base.SelectPageMargins_Photos(page_margins);
			}else{
				log.info("The desired Page Margin feature is not supported by the printer ");
			}
			
			//Select Desired Borderless Printing Option.
			if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Borderless printing']").size()!=0){
				Base.SelectBorderless_Photos(borderless);
			}else{
				log.info("The desired Borderless feature is not supported by the printer ");
			}
			
			//Opening more settings to access more printing options.
			int MoreSettings = Base.OpenMoreSettings_Photos();
			if	(MoreSettings == 1){	
				//Select Desired Color Mode Option.			
				if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Color mode']").size()!=0){
					Base.SelectColorOrMono_Photos(color_optn);
				}else{
					log.info("The desired Color feature is not supported by the printer ");
				}
				
				//Select Desired Duplex Option.			
				if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Duplex printing']").size()!=0){
					Base.SelectDuplexOption_Photos(duplex_optn);
				}else{
					log.info("The desired Duplexing feature is not supported by the printer ");
				}
				
				//Select Desired Paper Tray Option.			
				if (PhotosSession.findElementsByXPath("//ComboBox[@Name = 'Paper tray']").size()!=0){
					Base.SelectPaperTray_Photos(paper_tray);
				}else{
					log.info("The desired Paper Tray feature is not supported by the printer ");
				}
				
				//Closing more settings after accessing more printing options.
				Base.CloseMoreSettings_Photos();
				
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
		    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
		    log.info("Found correct job in print queue => "+test_filename);
		    		
		}

	    
	    @AfterClass(alwaysRun=true)
	    public static void TearDown()
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
					
	    }
	}
