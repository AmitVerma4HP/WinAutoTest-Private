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
import com.hp.win.core.Win32Base;
import com.hp.win.utility.ScreenshotUtility;


@Listeners({ScreenshotUtility.class})
public class PrintFromNotepad extends Win32Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);
    public static RemoteWebDriver NotepadSession = null;
    public static RemoteWebDriver PreferencesSession = null;
    public static RemoteWebDriver AdvancedSession = null;


    // Method to print from Notepad
    public static void PrintNotePadFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String paper_size, String device_name) throws InterruptedException, MalformedURLException  {
        // Go to file Menu
        NotepadSession.findElementByName("File").click();
        log.info("Clicked on File Menu in Notepad");
        Thread.sleep(1000);

        // Save the file
        NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Save\")]").click();
        log.info("Pressed Save button to Save the File");
        Thread.sleep(1000);

        // Go to file Menu
        NotepadSession.findElementByName("File").click();
        log.info("Clicked on File Menu in Notepad");
        Thread.sleep(1000);

        // Tap on Print
        NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Print\")]").click();
        log.info("Clicked on File -> Print option Successfully");
        Thread.sleep(1000);

        //Select WiFi Printer
        log.info("Looking for " + ptr_name + "...");
        NotepadSession.findElementByName(ptr_name).click();
        log.info("Selected Printer Successfully");
        Thread.sleep(1000); 

        // Open Preferences window
        ClickButton(NotepadSession, "Preferences");

        // In order to access the Preferences dialog, we need to start a new desktop session
        PreferencesSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PreferencesSession);
        
        // Select Preferences on the Layout tab first
        ChooseDuplexOrSimplex_Win32(PreferencesSession, duplex_optn, device_name);
        ChooseOrientation_Win32(PreferencesSession, orientation, device_name);

        // Select settings on Paper/Quality tab after the Layout tab
        ChooseColorOrMono_Win32(PreferencesSession, color_optn);

        // Now open the Advanced settings
        ClickButton(PreferencesSession, "Advanced");
        
        
        // A new desktop session must be created to access the Advanced dialog
        // so the Preferences dialog session must be closed here
        try {
            PreferencesSession.quit();
        } catch (Exception e) {
            log.info("PreferencesSession already terminated.");
        }
        
        
        AdvancedSession = GetDesktopSession(device_name);
        Assert.assertNotNull(AdvancedSession);
        
        ChoosePaperSize_Win32(AdvancedSession, paper_size, device_name);
        
        ClickButton(AdvancedSession, "OK");
        
        
        // The Advanced desktop session must be closed here instead of at tear down
        try {
            AdvancedSession.quit();
        } catch (Exception e) {
            log.info("AdvancedSession already terminated.");
        }
        
        
        // A new Preferences desktop session must be opened here in order to continue the test 
        PreferencesSession = GetDesktopSession(device_name);
        
        // Close print option dialogs
        ClickButton(PreferencesSession, "OK");

        //Tap on print icon (Give Print)        
        ClickButton(PreferencesSession, "Print");
     
    }


    // Method to open Notepad test file
    public static RemoteWebDriver OpenNoteFile(String device_name, String test_filename) throws MalformedURLException {

        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
            capabilities.setCapability("appArguments",test_filename );
            capabilities.setCapability("appWorkingDir", testfiles_loc);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
            NotepadSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);   
            Assert.assertNotNull(NotepadSession);
            NotepadSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);                                                  
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error opening notepad file");
            throw new RuntimeException(e);
        }
        log.info("Opened"+test_filename+"file from "+testfiles_loc);
        return NotepadSession;
    }

    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename" })
    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException {
        	
    		NotepadSession = OpenNoteFile(device_name, test_filename);
           
        	Thread.sleep(1000);                       
                   	
    }

	
	@Test
	@Parameters({ "ptr_name", "orientation", "duplex_optn", "color_optn", "paper_size", "device_name" })
    public void PrintNoteFile(String ptr_name, @Optional("Portrait")String orientation, @Optional("Simplex")String duplex_optn, @Optional("Color")String color_optn, @Optional("Letter")String paper_size, String device_name) throws InterruptedException, IOException
    {   
		// Method to Print Notepad File to Printer Under Test
		PrintNotePadFile(ptr_name, orientation, duplex_optn, color_optn, paper_size, device_name);
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


	}
    
    
  
}

