package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.OneNoteBase;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;


@Listeners({ScreenshotUtility.class})

public class PrintFromOneNote extends OneNoteBase{
	private static final Logger log = LogManager.getLogger(PrintFromOneNote.class);
	private static String currentClass;
	
	
    @BeforeClass
    @Parameters({ "device_name", "ptr_name", "test_filename" })
    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException {

        currentClass = PrintFromOneNote.class.getSimpleName();
    
        //Start PrintTrace log capturing 
        PrintTraceCapture.StartLogCollection(currentClass); 
        OneNoteSession = OneNoteBase.OpenOneNoteApp(device_name, test_filename);
       
        Thread.sleep(1000);                         
                    
    }

    
    @Test
    @Parameters({ "ptr_name", "orientation", "duplex_optn", "color_optn", "prnt_quality", "paper_size", "device_name", "test_filename" })
    public void PrintOneNote(String ptr_name, @Optional("Portrait")String orientation, @Optional("None")String duplex_optn, @Optional("Color")String color_optn, @Optional("Draft")String prnt_quality, @Optional("Letter")String paper_size, String device_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException
    {   
        // Method to Print Notepad File to Printer Under Test
        //PrintOneNoteFile(ptr_name, orientation, duplex_optn, color_optn, prnt_quality, paper_size, device_name);
        OneNoteBase.PrintOneNoteFile(ptr_name, test_filename, device_name);
    }
    
    
    @AfterClass
    public static void TearDown() throws NoSuchSessionException, IOException, InterruptedException
    {           

        try {
            OneNoteSession.quit();
        } catch (Exception e)
        {
            log.info("NotepadSession has already been terminated.");
        }

/*        try {
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
        }*/

      //Stop PrintTrace log capturing.
        PrintTraceCapture.StopLogCollection(currentClass);  

    }
      
}

