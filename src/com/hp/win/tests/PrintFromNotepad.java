package com.hp.win.tests;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.NotepadBase;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;


@Listeners({ScreenshotUtility.class})

public class PrintFromNotepad extends NotepadBase{
	private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);
	private static String currentClass;	


    
    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename" })
    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException {

		currentClass = PrintFromNotepad.class.getSimpleName();
	
		//Start PrintTrace log capturing 
		PrintTraceCapture.StartLogCollection(currentClass);	
		NotepadSession = NotepadBase.OpenNoteFile(device_name, test_filename);
       
    	Thread.sleep(1000);
        
    	// Method was originally called here, but print queue was getting in the way of the notepad test
    	// Moved to ValidatePrintQueue method
        // Base.OpenPrintQueue(ptr_name);                            
                   	
    }

	
	@Test
	@Parameters({ "ptr_name", "orientation", "duplex_optn", "color_optn", "prnt_quality", "paper_size", "device_name" })
    public void PrintNoteFile(String ptr_name, @Optional("Portrait")String orientation, @Optional("Simplex")String duplex_optn, @Optional("Color")String color_optn, @Optional("Draft")String prnt_quality, @Optional("Letter")String paper_size, String device_name) throws InterruptedException, IOException
    {   
		// Method to Print Notepad File to Printer Under Test
		PrintNotePadFile(ptr_name, orientation, duplex_optn, color_optn, prnt_quality, paper_size, device_name);
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

        // Leaving this here just in case it is necessary - EMC
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

      //Stop PrintTrace log capturing.
    		PrintTraceCapture.StopLogCollection(currentClass);	

	}

      
}

