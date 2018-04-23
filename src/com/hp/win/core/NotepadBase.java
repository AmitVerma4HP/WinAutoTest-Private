/*package com.hp.win.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;



public class NotepadBase extends Win32Base {

    private static final Logger log = LogManager.getLogger(NotepadBase.class);
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

        PreferencesSession = GetDesktopSession(device_name);
        //Assert.assertNotNull(PreferencesSession);
        
        // Select Preferences on the Layout tab first
        Win32Base.ChooseDuplexOrSimplex_Win32(PreferencesSession, duplex_optn, device_name);
        ChooseOrientation_Win32(PreferencesSession, orientation, device_name);

        // Select settings on Paper/Quality tab after the Layout tab
        ChooseColorOrMono_Win32(PreferencesSession, color_optn);

        // Now open the Advanced settings
        ClickButton(PreferencesSession, "Advanced");
        
        try {
            PreferencesSession.quit();
        } catch (Exception e) {
            log.info("PreferencesSession already terminated.");
        }
        
        AdvancedSession = GetDesktopSession(device_name);
        //Assert.assertNotNull(AdvancedSession);
        ChoosePaperSize_Win32(AdvancedSession, paper_size, device_name);

        
        ClickButton(AdvancedSession, "OK");
        
        try {
            AdvancedSession.quit();
        } catch (Exception e) {
            log.info("AdvancedSession already terminated.");
        }
        
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

}
*/