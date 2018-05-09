package com.hp.win.core;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;

import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;


@Listeners({ScreenshotUtility.class})
public class OneNoteBase extends Win32Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);
    public static WindowsDriver<WindowsElement> OneNoteSession = null;
    public static RemoteWebDriver FileBrowseSession = null;
    public static RemoteWebDriver PrintDialogSession = null;
    public static RemoteWebDriver PreferencesSession = null;
    public static RemoteWebDriver AdvancedSession = null;
    
    

    // Method to open OneNote app
    public static WindowsDriver<WindowsElement> OpenOneNoteApp(String device_name, String oneNote2016_exe_loc, String test_notebook) throws MalformedURLException, InterruptedException, IOException {
           
        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", oneNote2016_exe_loc);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
        
            OneNoteSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
            Assert.assertNotNull(OneNoteSession);
            
        }catch(Exception e){
            log.info("There was a problem opening OneNote. Going to try to get the window handle.");

            // Get the OneNote window handle so that we can switch back to the app and close it
            Base.BringWindowToFront(device_name, OneNoteSession, "Framework::CFrame");
        }
        
        log.info("Successfully opened OneNote.");
        return OneNoteSession;
    }



    public static void PrintOneNoteFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String prnt_quality, String paper_size, String device_name, String test_notebook) throws InterruptedException, MalformedURLException {
              
        
        OpenOneNoteFile(test_notebook, device_name);
               
/*        // Let the app have time to fully load before we try to interact with it
        wait = new WebDriverWait(OneNoteSession, 30); // Wait 30 seconds for OneNote to open
        
        // Using the 'File' button status to determine if app can be interacted with yet
        wait.until(ExpectedConditions.elementToBeClickable(By.name("File Tab")));
        log.info("Waiting for OneNote to finish opening...");*/
        
        //Base.BringWindowToFront(device_name, OneNoteSession, "Framework::CFrame");
        
        // Use ctrl + p shortcut to open print dialog
        OneNoteSession.getKeyboard().pressKey(Keys.CONTROL + "p");
        OneNoteSession.getKeyboard().releaseKey("p");
        OneNoteSession.getKeyboard().releaseKey(Keys.CONTROL);
        
        log.info("Opening PrintDialogSession...");
        PrintDialogSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PrintDialogSession);
        
        ConfirmDialogBox(PrintDialogSession, "Print");
        
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
        
        // Open Preferences dialog
        //ClickButton(PrintDialogSession, "Preferences");

        if(PrintDialogSession == null) {
            PrintDialogSession = Base.GetDesktopSession(device_name);
        }
        
        PrintDialogSession.getKeyboard().pressKey(Keys.ALT + "r");
        PrintDialogSession.getKeyboard().releaseKey("r");
        PrintDialogSession.getKeyboard().releaseKey(Keys.ALT);
        
        
        if(PreferencesSession == null) {
            // In order to access the Preferences dialog, we need to start a new desktop session
            log.info("Opening PreferencesSession...");
            PreferencesSession = GetDesktopSession(device_name);
            Assert.assertNotNull(PreferencesSession);
        }

        ConfirmDialogBox(PreferencesSession, "Printing Preferences");
        
/*        // Select Preferences on the Layout tab first
        ChooseDuplexOrSimplex_Win32(PreferencesSession, duplex_optn, device_name);
        ChooseOrientation_Win32(PreferencesSession, orientation, device_name);*/

/*        // Select settings on Paper/Quality tab after the Layout tab
        SelectPreferencesTab_Win32(PreferencesSession, "Paper/Quality");
        ChooseColorOrMono_Win32(PreferencesSession, color_optn);*/
        
/*        ChoosePrintQuality_Win32(PreferencesSession, prnt_quality);*/
        
        
        if(PreferencesSession == null) {
            PreferencesSession = Base.GetDesktopSession(device_name);
        }
        
        
        // Now open the Advanced settings
        //ClickButton(PreferencesSession, "Advanced...");

        PreferencesSession.getKeyboard().pressKey(Keys.ALT + "v");
        PreferencesSession.getKeyboard().releaseKey("v");
        PreferencesSession.getKeyboard().releaseKey(Keys.ALT);
        
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
                
        ConfirmDialogBox(AdvancedSession, "Microsoft IPP Class Driver Advanced Document Settings");
        
        if(AdvancedSession == null) {
            AdvancedSession = Base.GetDesktopSession(device_name);
        }
        
        ChoosePaperSize_Win32(AdvancedSession, paper_size, device_name);

        if(AdvancedSession == null) {
            AdvancedSession = Base.GetDesktopSession(device_name);
        }
        
        Base.ClickButton(AdvancedSession, "OK");
        
/*        try {
            while(AdvancedSession.findElementByName("OK").getAttribute("HasKeyboardFocus").equals("False")) {
                log.info("Tabbing to next element.");
                AdvancedSession.getKeyboard().pressKey(Keys.TAB);
                AdvancedSession.getKeyboard().releaseKey(Keys.TAB);
            }
            log.info("Found OK button.");
            Assert.assertEquals(AdvancedSession.findElementByName("OK").getAttribute("HasKeyboardFocus"), "False");
            AdvancedSession.getKeyboard().pressKey(Keys.ENTER);
            AdvancedSession.getKeyboard().pressKey(Keys.ENTER);
            //AdvancedSession.getKeyboard().releaseKey(Keys.ENTER);
            
        } catch (Exception e) {
            log.info("Could not tab to OK button.");
        }*/
        
        if(AdvancedSession == null) {
            AdvancedSession = Base.GetDesktopSession(device_name);
        }
        
        ConfirmDialogBox(AdvancedSession, "Printing Preferences");

        ClickButton(AdvancedSession, "OK");

        
        if(AdvancedSession == null) {
            AdvancedSession = Base.GetDesktopSession(device_name);
        }
        
        ConfirmDialogBox(AdvancedSession, "Print");
        
        Base.ClickButton(AdvancedSession, "Print");
        
        //ClickButton(AdvancedSession, "OK");
        //ClickButton(AdvancedSession, "OK");
        //ClickButton(AdvancedSession, "Print");

        if(OneNoteSession == null) {
            OneNoteSession = (WindowsDriver<WindowsElement>) Base.GetDesktopSession(device_name);
            Base.BringWindowToFront(device_name, OneNoteSession, "Framework::CFrame");
            Assert.assertNotNull(OneNoteSession);
        }

        //CloseNotebook(OneNoteSession);
    }

    
    public static void OpenOneNoteFile(String test_notebook, String device_name) throws InterruptedException, MalformedURLException {
        
        log.info("Going to open file '" + test_notebook + "' on laptop '" + device_name + "'...");
        
        if(OneNoteSession == null) {
            log.info("OneNote session is null. Getting a new session.");
            OneNoteSession = (WindowsDriver<WindowsElement>) Base.GetDesktopSession(device_name);
            Assert.assertNotNull(OneNoteSession);
        }
        
        WebDriverWait wait = new WebDriverWait(OneNoteSession, 30);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("File Tab")));
        log.info("Waiting until menu buttons are clickable.");
               
        // Keyboard shortcut to get to File => Open
        log.info("Using 'ctrl' + 'o' to open file...");
        OneNoteSession.getKeyboard().pressKey(Keys.CONTROL + "o");
        OneNoteSession.getKeyboard().releaseKey("o");
        OneNoteSession.getKeyboard().releaseKey(Keys.CONTROL);
        

        
        //log.info("Used keyboard shortcuts to navigate to file open menu.");
        
        // Use page down to get to the bottom of the screen without scrolling
        log.info("Using PgDn button...");
        OneNoteSession.getKeyboard().pressKey(Keys.PAGE_DOWN);
        OneNoteSession.getKeyboard().releaseKey(Keys.PAGE_DOWN);
        
        
        // Click on the browse button to open the dialog to open a file
        Base.ClickButton(OneNoteSession, "Browse");
        
        // Open a new desktop session to handle the open file dialog
        FileBrowseSession = Base.GetDesktopSession(device_name);
        log.info("Created FileBrowseSession.");
        try {
            // Keyboard shortcut to highlight the address edit field
            log.info("Typing in file location...");
            FileBrowseSession.getKeyboard().pressKey(Keys.ALT + "d");
            FileBrowseSession.getKeyboard().releaseKey("d");
            FileBrowseSession.getKeyboard().releaseKey(Keys.ALT);
            
            // Type in the path to the file and open the folder
            FileBrowseSession.getKeyboard().sendKeys(testfiles_loc);
            FileBrowseSession.getKeyboard().pressKey(Keys.ENTER);
            FileBrowseSession.getKeyboard().releaseKey(Keys.ENTER);

            
            
            // Keyboard shortcut to highlight the name edit field
            log.info("Typing in file name...");
            FileBrowseSession.getKeyboard().pressKey(Keys.ALT + "n");
            FileBrowseSession.getKeyboard().releaseKey("n");
            FileBrowseSession.getKeyboard().releaseKey(Keys.ALT);
            
            // Type in the file name and hit enter to open it
            FileBrowseSession.getKeyboard().sendKeys(test_notebook);
            FileBrowseSession.getKeyboard().pressKey(Keys.ENTER);
            FileBrowseSession.getKeyboard().releaseKey(Keys.ENTER);

            log.info("Successfully opened the OneNote test file '" + test_notebook + "'.");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("There was a problem opening the OneNote test file.");
            throw new RuntimeException(e);
        }
    }
    
    
    // This method will close the notebook currently open in OneNote
    public static void CloseNotebook(WindowsDriver<WindowsElement> session) {
        log.info("Going to close the OneNote Notebook now...");
        // Move focus to the Notebook area (place where Notebook name is shown from drop down menu)
        session.getKeyboard().pressKey(Keys.CONTROL + "g");
        session.getKeyboard().releaseKey("g");
        session.getKeyboard().releaseKey(Keys.CONTROL);
        
        // Use keyboard shortcut to open menu (keyboard alternative to right click)
/*        session.getKeyboard().pressKey(Keys.SHIFT);
        session.getKeyboard().pressKey(Keys.F10);*/
        session.getKeyboard().sendKeys(Keys.SHIFT, Keys.F10);
        session.getKeyboard().releaseKey(Keys.F10);
        session.getKeyboard().releaseKey(Keys.SHIFT);
        
        // Use keyboard shortcut to select Close Notebook from the menu
        session.getKeyboard().pressKey("c");
        session.getKeyboard().releaseKey("c");
    }

}

