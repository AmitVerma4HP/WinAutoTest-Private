package com.hp.win.tests;


import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.OneNoteBase;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;


@Listeners({ScreenshotUtility.class})

public class PrintFromOneNote extends OneNoteBase{
	private static final Logger log = LogManager.getLogger(PrintFromOneNote.class);
	private static String currentClass;
	private static String device_under_test; // Use this parameter for BringWindowToFront() method when necessary
//	private static WindowsDriver NotepadSession = null;
	
	
    @BeforeClass
    @Parameters({ "device_name", "oneNote2016_exe_loc", "ptr_name", "test_filename" })
    public static void setup(String device_name, String oneNote2016_exe_loc, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException {

        currentClass = PrintFromOneNote.class.getSimpleName();
        device_under_test = device_name;
        
        //Start PrintTrace log capturing 
        PrintTraceCapture.StartLogCollection(currentClass); 
        OneNoteSession = OneNoteBase.OpenOneNoteApp(device_name, oneNote2016_exe_loc, test_filename);
       
        Thread.sleep(1000);                         
                    
    }

    
    @Test
    @Parameters({ "ptr_name", "orientation", "duplex_optn", "color_optn", "prnt_quality", "paper_size", "device_name", "test_notebook", "test_filename" })
    public void PrintOneNote(String ptr_name, @Optional("Portrait")String orientation, @Optional("None")String duplex_optn, @Optional("Color")String color_optn, @Optional("Draft")String prnt_quality, @Optional("Letter")String paper_size, String device_name, @Optional("TestNotebook1.onetoc2")String test_notebook, @Optional("OneNoteTest1")String test_filename) throws InterruptedException, IOException
    {   

        // Method to Print OneNote File to Printer Under Test
        OneNoteBase.PrintOneNoteFile(ptr_name, orientation, duplex_optn, color_optn, prnt_quality, paper_size, device_name, test_notebook);
               
        log.info("Going to close the OneNote Notebook now...");
        OneNoteBase.CloseNotebook(OneNoteSession);

    }

    
    @Test(dependsOnMethods = { "PrintOneNote" })
    @Parameters({ "device_name", "ptr_name", "test_filename"})
    public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
    {
        // Method to open the print queue (Moved from setup() method)
        Base.OpenPrintQueue(ptr_name);
        
        // Method to attach session to Printer Queue Window
        Base.SwitchToPrinterQueue(device_name,ptr_name);
        
        log.info("Expected queued job should be => " + test_filename);

        //Validate Print Job Queued up
        Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
        log.info("Found correct job in print queue => " + test_filename);
        
        Base.BringWindowToFront(device_under_test, OneNoteSession);
        
    }
    
    
    @AfterClass(alwaysRun=true)
    public static void TearDown() throws NoSuchSessionException, IOException, InterruptedException
    {           
        
        log.info("Closing OneNote session...");
        try {
            OneNoteSession.close();
            OneNoteSession.quit();
            log.info("Successfully closed OneNote.");
        } catch (Exception e)
        {
            /* log.info("Couldn't close OneNote immediately. Going to grab the window handle...");
            Base.BringWindowToFront(device_under_test, OneNoteSession);
            try {
                OneNoteSession.close();
                OneNoteSession.quit();
                log.info("Successfully closed OneNote.");
            } catch(Exception e1) {*/
                log.info("OneNoteSession has already been terminated.");
/*            }*/
        }

        try {
            PrintQueueSession.close();
            PrintQueueSession.quit();
        } catch (Exception e)
        {
            log.info("PrintQueueSession has already been terminated.");
        }

      //Stop PrintTrace log capturing.
        PrintTraceCapture.StopLogCollection(currentClass);  

    }
      
}

