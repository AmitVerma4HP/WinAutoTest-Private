package com.hp.win.tests;


import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;


	public class PrintFromPhotos extends Base {
		private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);
	   
		@BeforeClass
		@Parameters({ "device_name", "ptr_name", "test_filename"})
	    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException { 
	       
	    PhotosSession = Base.OpenPhotosFile(device_name, test_filename);
	    Thread.sleep(1000);
            
	    }
	
		
		@Test
		@Parameters({ "ptr_name", "test_filename"})
	    public void PrintPhotos(String ptr_name, String test_filename) throws InterruptedException, IOException
	    {
	    	
			// Method to Print Photo File to Printer Under Test
			PrintPhoto(ptr_name,test_filename);
			
			// Method to select the desired printer.
			SelectDesiredPrinter_Photos(ptr_name);
			
			// Tap on Print button(Give Print)
	    	PhotosSession.findElementByXPath("//Button[@AutomationId = 'PrintButton']").click();
	    	log.info("Clicked on final Print button -> Print option Successfully");
		}
 
		
		@Test
		@Parameters({ "device_name", "ptr_name", "test_filename"})
		public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
		{
			// Open Print Queue
			Base.OpenPrintQueue(ptr_name);
			
			// Method to attach session to Printer Queue Window
			SwitchToPrinterQueue(device_name,ptr_name);
			
		    /*// Take out .txt from file name for validation in Assert.
		    test_filename = test_filename.substring(0, test_filename.lastIndexOf('.'));*/
		    log.info("Expected queuthated job should be => "+test_filename);
		    
		    //Validate Print Job Queued up
		    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
		    log.info("Found correct job in print queue => "+test_filename);
		    
		}

	    
	    @AfterClass
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
