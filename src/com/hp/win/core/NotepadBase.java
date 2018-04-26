package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Win32Base;
import com.hp.win.utility.ScreenshotUtility;


@Listeners({ScreenshotUtility.class})
public class NotepadBase extends Win32Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);
    public static RemoteWebDriver NotepadSession = null;
    public static RemoteWebDriver PrintDialogSession = null;
    public static RemoteWebDriver PreferencesSession = null;
    public static RemoteWebDriver AdvancedSession = null;


    // Method to print from Notepad
    public static void PrintNotePadFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String prnt_quality, String paper_size, String device_name) throws InterruptedException, MalformedURLException  {
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

        PrintDialogSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PrintDialogSession);
        
        //Select WiFi Printer
        log.info("Looking for '" + ptr_name + "'...");
        if(PrintDialogSession.findElementByName(ptr_name).isSelected()) {
            log.info("'" + ptr_name + "' is already selected.");
        }
        else {
            try {
                PrintDialogSession.findElementByName(ptr_name).click();
                log.info("Clicked on '" + ptr_name + "' successfully.");
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info("Printer under test is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name incorrectly in testsuite xml");
                throw new RuntimeException(e);
            }
        }

        
        
        // Open Preferences window
        ClickButton(PrintDialogSession, "Preferences");

        // A new desktop session must be created every time a dialog box is created or destroyed
        try {
            PrintDialogSession.quit();
        } catch (Exception e) {
            log.info("PrintDialogSession already terminated.");
        }
        
        // In order to access the Preferences dialog, we need to start a new desktop session
        PreferencesSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PreferencesSession);
        
        // Select Preferences on the Layout tab first
        ChooseDuplexOrSimplex_Win32(PreferencesSession, duplex_optn, device_name);
        ChooseOrientation_Win32(PreferencesSession, orientation, device_name);

        // Select settings on Paper/Quality tab after the Layout tab
        SelectPreferencesTab_Win32(PreferencesSession, "Paper/Quality");
        ChooseColorOrMono_Win32(PreferencesSession, color_optn);
        
        ChoosePrintQuality_Win32(PreferencesSession, prnt_quality);

        // Now open the Advanced settings
        ClickButton(PreferencesSession, "Advanced...");
        
        // Close the session for the Preferences dialog box
        try {
            PreferencesSession.quit();
        } catch (Exception e) {
            log.info("PreferencesSession already terminated.");
        }
        
        
        // Open a session for the Advanced dialog box
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
        Assert.assertNotNull(PreferencesSession);
        
        // Close print option dialogs
        ClickButton(PreferencesSession, "OK");

        // Close the preferences session
        try {
            PreferencesSession.quit();
        } catch (Exception e) {
            log.info("PreferencesSession already terminated.");
        }
        
        // Get a new print dialog session
        PrintDialogSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PrintDialogSession);
        
        //Tap on print icon (Give Print)        
        ClickButton(PrintDialogSession, "Print");
        
        try {
            PrintDialogSession.quit();
        } catch (Exception e) {
            log.info("PrintDialogSession already terminated.");
        }
     
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
   
    
  
}

