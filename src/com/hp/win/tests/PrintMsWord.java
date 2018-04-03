package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;



public class PrintMsWord extends Base{
	private static final Logger log = LogManager.getLogger(PrintMsWord.class);
	

    @BeforeClass
	@Parameters({"device_name", "ptr_name", "test_filename"})
    public static void setup(String device_name, String ptr_name, @Optional("MicrosoftWord2016_Portrait_MultiPage_TestFile.docx")String test_filename) 
    		throws InterruptedException, IOException {
        	
    		MsWordSession = Base.OpenMsWordFile(device_name, test_filename);    		
            Thread.sleep(1000); 
    }

	
	@Test
	@Parameters({"device_name","ptr_name","paper_size","duplex_option","orientation","collation","copies","pages_to_print","page_count"})
    public void PrintMsWordFile(String device_name,String ptr_name, @Optional("Letter")String paper_size,@Optional("Print One Sided")String duplex_option,
    	@Optional("Portrait Orientation")String orientation,@Optional("Collated")String collation,@Optional("1")String copies,
    	@Optional("Print All Pages")String pages_to_print, @Optional("NA")String page_count) throws InterruptedException, IOException    {
		
		MsWordSession.getKeyboard().pressKey(Keys.CONTROL+"p");
		log.info("Pressed CTRL+P to get to Print Option");
		Thread.sleep(1000);
		MsWordSession.getKeyboard().releaseKey(Keys.CONTROL);
		Thread.sleep(2000);		
		
		//Selecting desired printer
		Base.SelectDesiredPrinter_Msword(ptr_name);        
		
		
		// Write method to select Print settings
		// Select Desired Paper Size
		Base.SelectPaperSize_Msword(paper_size);
		
		// Select Desired Duplex Option
		Base.SelectDuplexOption_Msword(duplex_option);
		
		// Select Desired Orientation Option
		Base.SelectOrientation_Msword(orientation);
		
		// Select Desired Collation Option
		Base.SelectCollation_Msword(collation);
		
		// Enter Desired Copies value
		Base.SelectCopies_Msword(copies);
		
		// Select Desired Pages to Print Value
		Base.SelectPagesToPrint_Msword(pages_to_print, page_count);
				
		//After all print settings give print 
		MsWordSession.findElementByXPath("//Button[@Name ='Print']").click();	
		Thread.sleep(1000);
		log.info("Finally gave a print by clicking on PRINT button");
		
	}
	
	

	
	@Test
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
		
		//Open Print Queue 
		Base.OpenPrintQueue(ptr_name);        
		 
		Thread.sleep(1000);
		
		// Method to attach session to Printer Queue Window
		SwitchToPrinterQueue(device_name,ptr_name);
		
	    //Take out .txt from file name for validation in Assert.
	    test_filename = test_filename.substring(0, test_filename.lastIndexOf('.'));	    
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
	    PrintQueueSession.close();
	    log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");	    
	    
	}


	
    @AfterClass
    public static void TearDown() throws InterruptedException
    {	        
			if(DesktopSession!=null)
			{
				DesktopSession.quit();
			}
    
			if(PrintQueueSession!=null)
			{
				PrintQueueSession.quit();
			}
			
			if(MsWordFirstSession!=null)
			{
				MsWordFirstSession.quit();
			}
			
        	if (MsWordSession!= null)
        	{      	       		
        		MsWordSession.close();
        		
        		if(MsWordSession.findElementByClassName("NetUINetUIDialog").isDisplayed())
					{
        				log.info("Found alert dialog to save test file");
						MsWordSession.findElementByName("Don't Save").click();
					}
        		MsWordSession.quit();
        		
        	}
      			        	

        	        
    }
    
    
  
}

