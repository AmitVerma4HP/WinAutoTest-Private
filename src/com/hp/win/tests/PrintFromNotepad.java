package com.hp.win.tests;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;


public class PrintFromNotepad extends Base{
	private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);
	

    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename"})
    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException {
        	
    		NotepadSession = Base.OpenNoteFile(device_name, test_filename);
           
        	Thread.sleep(1000);
            
            Base.OpenPrintQueue(ptr_name);                            
                   	
    }

	
	@Test
	@Parameters({"ptr_name"})
    public void PrintNoteFile(String ptr_name) throws InterruptedException, IOException
    {   
		// Method to Print Notepad File to Printer Under Test
		PrintNotePadFile(ptr_name);
		//System.out.println("Switching to printer queue...");
	}
	
	

	
	@Test
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
		System.out.println("Switching to printer queue...");
		// Method to attach session to Printer Queue Window
		SwitchToPrinterQueue(device_name,ptr_name);
		
		log.info("Altering " + test_filename + "...");
		System.out.println("Altering " + test_filename + "...\n");
	    //Take out .txt from file name for validation in Assert.
	    //test_filename = test_filename.substring(0, test_filename.lastIndexOf('.'));
		String test_name_no_extension = test_filename.substring(0, test_filename.lastIndexOf('.'));
	    log.info("Successfully changed to " + test_filename + "\n.......\n");
		log.info("Successfully changed to " + test_name_no_extension + "\n.......\n");
		System.out.println("Successfully changed to " + test_name_no_extension + "\n.......\n");
	    
	    log.info("Expected queued job should be => "+test_filename);
	    //Validate Print Job Queued up
	    //Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
	    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_name_no_extension));
	    log.info("Found correct job in print queue => "+test_filename);
	    log.info("Found correct job in print queue => "+test_name_no_extension);
	    
	}

    
    @AfterClass
    public static void TearDown()
    {	        
    
        	if (NotepadSession!= null)
        	{
        		NotepadSession.quit();
        	}
        	
    		if(DesktopSession!=null)
    		{
    			DesktopSession.quit();
    		}
    		
    		if(PrintQueueSession!=null)
    		{
    		   PrintQueueSession.quit();
    		}
        	        
    }
    
    
  
}

