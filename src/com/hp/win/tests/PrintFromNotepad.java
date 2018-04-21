package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.NotepadBase;
import com.hp.win.utility.ScreenshotUtility;

@Listeners({ScreenshotUtility.class})
public class PrintFromNotepad extends NotepadBase{
	private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);
	

    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename" })
    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException {
        	
    		NotepadSession = NotepadBase.OpenNoteFile(device_name, test_filename);
           
        	Thread.sleep(1000);
            
        	// Method was originally called here, but print queue was getting in the way of the notepad test
        	// Moved to ValidatePrintQueue method
            //Base.OpenPrintQueue(ptr_name);                            
                   	
    }

	
	@Test
	@Parameters({ "ptr_name", "orientation", "duplex_optn", "color_optn", "paper_size", "device_name" })
    public void PrintNoteFile(String ptr_name, @Optional("Portrait")String orientation, @Optional("Simplex")String duplex_optn, @Optional("Color")String color_optn, @Optional("Letter")String paper_size, String device_name) throws InterruptedException, IOException
    {   
		// Method to Print Notepad File to Printer Under Test
		NotepadBase.PrintNotePadFile(ptr_name, orientation, duplex_optn, color_optn, paper_size, device_name);
	}
	
	
	@Test
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
	    // Method to open the print queue (Moved from setup() method)
	    Base.OpenPrintQueue(ptr_name);
	    
		// Method to attach session to Printer Queue Window
		Base.SwitchToPrinterQueue(device_name,ptr_name);
		
	    //Take out .txt from file name for validation in Assert.
	    test_filename = test_filename.substring(0, test_filename.lastIndexOf('.'));
	    
	    log.info("Expected queued job should be => "+test_filename);
	    //Validate Print Job Queued up
	    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
	    log.info("Found correct job in print queue => "+test_filename);
	    
	}

    
	@AfterClass
	public static void TearDown() throws NoSuchSessionException
	{	        

/*	    try {
	        PreferencesSession.quit();
	    } catch (Exception e) {
	        log.info("PreferencesSession already terminated.");
	    }

	    try {
	        AdvancedSession.quit();
	    } catch (Exception e) {
	        log.info("AdvancedSession already terminated.");
	    }*/

	    try {
	        NotepadSession.quit();
	    } catch (Exception e)
	    {
	        log.info("NotepadSession has already been terminated.");
	    }

	    try {
	        DesktopSession.quit();
	    } catch (Exception e)
	    {
	        log.info("DesktopSession has already been terminated.");
	    }

	    try {
	        PrintQueueSession.quit();
	    } catch (Exception e)
	    {
	        log.info("PrintQueueSession has already been terminated.");
	    }


	}
    
    
  
}

