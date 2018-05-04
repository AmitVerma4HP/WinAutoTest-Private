package com.hp.win.core;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;

import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;


@Listeners({ScreenshotUtility.class})
public class OneNoteBase extends Win32Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);
    public static WindowsDriver FirstOneNoteSession = null;
    public static WindowsDriver OneNoteSession = null;
    public static RemoteWebDriver FileBrowseSession = null;
    public static RemoteWebDriver PreferencesSession = null;
    public static RemoteWebDriver AdvancedSession = null;
    
    

    // Method to open OneNote app
    public static WindowsDriver OpenOneNoteApp(String device_name, String test_notebook) throws MalformedURLException, InterruptedException, IOException {
           
        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Program Files\\Microsoft Office\\Office16\\ONENOTE.exe");        
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
        
            OneNoteSession = new WindowsDriver(new URL(WindowsApplicationDriverUrl), capabilities);
        
            // Let the app have time to fully load before we try to interact with it
            Thread.sleep(10000);
            Assert.assertNotNull(OneNoteSession);
            
            // Give the app more time to finish loading
            OneNoteSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            
        }catch(Exception e){
            log.info("There was a problem opening OneNote. Going to try to get the window handle.");

            // Get the OneNote window handle so that we can switch back to the app and close it
            Base.BringWindowToFront(device_name, OneNoteSession);
        }
        
        log.info("Successfully opened OneNote.");
        return OneNoteSession;
    }



    public static void PrintOneNoteFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String prnt_quality, String paper_size, String device_name, String test_notebook) throws InterruptedException, MalformedURLException {
        
        log.info("Going to call 'OpenOneNoteFile() method...");
        OpenOneNoteFile(test_notebook, device_name);
        
        Base.BringWindowToFront(device_name, OneNoteSession);
        
        OneNoteSession.getKeyboard().pressKey(Keys.CONTROL + "p");
        OneNoteSession.getKeyboard().releaseKey(Keys.CONTROL);
        
        log.info("Opening PrintDialogSession...");
        RemoteWebDriver PrintDialogSession = GetDesktopSession(device_name);
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
        
/*        // Open Preferences window
        ClickButton(PrintDialogSession, "Preferences");

        // A new desktop session must be created every time a dialog box is created or destroyed
        try {
            PrintDialogSession.quit();
            log.info("Closed PrintDialogSession...");
        } catch (Exception e) {
            log.info("PrintDialogSession already terminated.");
        }

        
        // In order to access the Preferences dialog, we need to start a new desktop session
        log.info("Opening PreferencesSession...");
        PreferencesSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PreferencesSession);
        
        // Select Preferences on the Layout tab first
        ChooseDuplexOrSimplex_Win32(PreferencesSession, duplex_optn, device_name);
        ChooseOrientation_Win32(PreferencesSession, orientation, device_name);*/

/*        // Select settings on Paper/Quality tab after the Layout tab
        SelectPreferencesTab_Win32(PreferencesSession, "Paper/Quality");
        ChooseColorOrMono_Win32(PreferencesSession, color_optn);
        
        ChoosePrintQuality_Win32(PreferencesSession, prnt_quality);*/

/*        // Now open the Advanced settings
                ClickButton(PreferencesSession, "Advanced...");
        
        // Close the session for the Preferences dialog box
        try {
            PreferencesSession.quit();
            log.info("Closed PreferencesSession...");
        } catch (Exception e) {
            log.info("PreferencesSession already terminated.");
        }
        
        
        // Open a session for the Advanced dialog box
        log.info("Opening AdvancedSession...");
        AdvancedSession = GetDesktopSession(device_name);
        Assert.assertNotNull(AdvancedSession);
        
        ChoosePaperSize_Win32(AdvancedSession, paper_size, device_name);
        
        ClickButton(AdvancedSession, "OK");
        ClickButton(AdvancedSession, "OK");
        ClickButton(AdvancedSession, "Print");*/
        
/*        ClickButton(PreferencesSession, "OK");
        ClickButton(PreferencesSession, "Print");

        try {
            PreferencesSession.quit();
            log.info("Closed PreferencesSession...");
        } catch (Exception e) {
            log.info("PreferencesSession already terminated.");
        }*/
        
        OneNoteSession = (WindowsDriver) Base.GetDesktopSession(device_name);
        Assert.assertNotNull(OneNoteSession);

        CloseNotebook(OneNoteSession);
    }

    
    public static void OpenOneNoteFile(String test_notebook, String device_name) throws InterruptedException, MalformedURLException {
        
        log.info("Going to open file '" + test_notebook + "' on laptop '" + device_name + "'...");
        
        if(OneNoteSession == null) {
            log.info("OneNote session is null. Getting a new session.");
            OneNoteSession = (WindowsDriver) Base.GetDesktopSession(device_name);
            Assert.assertNotNull(OneNoteSession);
        }
        
        // Keyboard shortcut to get to File => Open
        OneNoteSession.getKeyboard().pressKey(Keys.CONTROL + "o");
        OneNoteSession.getKeyboard().releaseKey(Keys.CONTROL);
        
        log.info("Used keyboard shortcuts to navigate to file open menu.");
        
        // Use page down to get to the bottom of the screen without scrolling
        OneNoteSession.getKeyboard().pressKey(Keys.PAGE_DOWN);
        OneNoteSession.getKeyboard().releaseKey(Keys.PAGE_DOWN);
        
        log.info("Used keyboard shortcuts to page down.");
        
        // Click on the browse button to open the dialog to open a file
        Base.ClickButton(OneNoteSession, "Browse");
        log.info("Clicked 'Browse' button.");
        
        // Open a new desktop session to handle the open file dialog
        FileBrowseSession = Base.GetDesktopSession(device_name);
        log.info("Created FileBrowseSession.");
        try {
            // Keyboard shortcut to highlight the address edit field
            FileBrowseSession.getKeyboard().pressKey(Keys.ALT + "d");
            FileBrowseSession.getKeyboard().releaseKey(Keys.ALT);
            log.info("Used keyboard shortcuts to edit address.");
            
            // Type in the path to the file and open the folder
            FileBrowseSession.getKeyboard().sendKeys(testfiles_loc.concat("OneNoteTestNotebook"));
            FileBrowseSession.getKeyboard().pressKey(Keys.ENTER);
            FileBrowseSession.getKeyboard().releaseKey(Keys.ENTER);
            log.info("Used keyboard to open file folder.");
            
            // Keyboard shortcut to highlight the name edit field
            FileBrowseSession.getKeyboard().pressKey(Keys.ALT + "n");
            FileBrowseSession.getKeyboard().releaseKey(Keys.ALT);
            log.info("Used keyboard shortcuts to navigate to file edit box.");
            
            // Type in the file name and hit enter to open it
            FileBrowseSession.getKeyboard().sendKeys(test_notebook);
            FileBrowseSession.getKeyboard().pressKey(Keys.ENTER);
            FileBrowseSession.getKeyboard().releaseKey(Keys.ENTER);
            log.info("Used keyboard shortcuts to open file.");
            
            log.info("Successfully opened the OneNote test file '" + test_notebook + "'.");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("There was a problem opening the OneNote test file.");
            throw new RuntimeException(e);
        }
    }
    
    
    // This method will close the notebook currently open in OneNote
    public static void CloseNotebook(WindowsDriver session) {
        
        // Move focus to the Notebook area (place where Notebook name is shown from drop down menu)
        session.getKeyboard().pressKey(Keys.CONTROL + "g");
        session.getKeyboard().releaseKey(Keys.CONTROL);
        
        // Use keyboard shortcut to open menu (keyboard alternative to right click)
        session.getKeyboard().pressKey(Keys.SHIFT);
        session.getKeyboard().pressKey(Keys.F10);
        session.getKeyboard().releaseKey(Keys.F10);
        session.getKeyboard().releaseKey(Keys.SHIFT);
        
        // Use keyboard shortcut to select Close Notebook from the menu
        session.getKeyboard().pressKey("c");
        session.getKeyboard().releaseKey("c");
    }

}

