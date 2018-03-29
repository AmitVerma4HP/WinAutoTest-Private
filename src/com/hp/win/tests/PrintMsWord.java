package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;



public class PrintMsWord extends Base{
	private static final Logger log = LogManager.getLogger(PrintMsWord.class);
	

    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename"})
    public static void setup(String device_name, String ptr_name, @Optional("MicrosoftWord2016_Portrait_MultiPage_TestFile.docx")String test_filename) throws InterruptedException, IOException {
        	
    		MsWordSession = Base.OpenMsWordFile(device_name, test_filename);    		
            Thread.sleep(1000); 
    }

	
	@Test
	@Parameters({"ptr_name"})
    public void PrintMsWordFile(String ptr_name) throws InterruptedException, IOException
    {   		
		MsWordSession.getKeyboard().pressKey(Keys.CONTROL+"p");
		log.info("Pressed CTRL+P to get to Print Option");
		Thread.sleep(1000);
		MsWordSession.getKeyboard().releaseKey(Keys.CONTROL);
		Thread.sleep(2000);		
		Base.SelectDesiredPrinter_Msword(ptr_name);        
		
		
		// Write method to select Print settings
		// TDB
		
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
	    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
	    log.info("Found correct job in print queue => "+test_filename);
	    
	}

    
    @AfterClass
    public static void TearDown()
    {	        
    
        	if (MsWordSession!= null)
        	{
        		MsWordSession.quit();
        	}
        	
    		if(DesktopSession!=null)
    		{
    			DesktopSession.quit();
    		}
    		
    		if(MsWordFirstSession!=null)
    		{
    			MsWordFirstSession.quit();
    		}
        	        
    }
    
    
  
}

