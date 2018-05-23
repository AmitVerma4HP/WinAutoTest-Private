package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Win32Base;
import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;


@Listeners({ScreenshotUtility.class})
public class NotepadBase extends Win32Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);
    @SuppressWarnings("rawtypes")
	public static WindowsDriver   NotepadSession     = null;
    public static RemoteWebDriver PrintDialogSession = null;
    public static RemoteWebDriver PreferencesSession = null;
    public static RemoteWebDriver AdvancedSession    = null;


    // Method to print from Notepad
    public static void PrintNotePadFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String prnt_quality, String paper_size, String device_name) throws InterruptedException, MalformedURLException  {

        // Maximize the Notepad window to prevent false errors if Notepad is partially off-screen
        NotepadSession.getKeyboard().sendKeys(Keys.COMMAND, Keys.ARROW_UP);
        NotepadSession.getKeyboard().releaseKey(Keys.ARROW_UP);
        NotepadSession.getKeyboard().releaseKey(Keys.COMMAND);
        
        // Go to file Menu
        NotepadSession.findElementByName("File").click();
        log.info("Clicked on File Menu in Notepad");
        Thread.sleep(1000);

        // Tap on Print
        NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Print\")]").click();
        log.info("Clicked on File -> Print option Successfully");
        Thread.sleep(1000);

        log.info("Opening PrintDialogSession...");
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
            log.info("Closed PrintDialogSession...");
        } catch (Exception e) {
            log.info("PrintDialogSession already terminated.");
        }

        // In order to access the Preferences dialog, we need to start a new desktop session
        log.info("Opening PreferencesSession...");
        PreferencesSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PreferencesSession);
        
        // Get a list of all the tabs in the preferences window
        List<WebElement> tabs = PreferencesSession.findElementsByTagName("TabItem");
        
        // Create a list of all of the parameters to be passed to different print setting tests
        List<String> parameters = Arrays.asList(orientation, duplex_optn, color_optn, prnt_quality, paper_size);
        
        if(tabs.size() > 0) {
            
            // If the first tab is the "Layout" tab, the print settings are in a Win32 dialog box
            if(tabs.get(0).getText().equals("Layout")) {
                log.info("Going to use Win32 interface.");
                RunWin32Test(PreferencesSession, tabs, parameters, device_name);
            }
            // If the first tab is the "Printing Shortcuts" tab, the print settings are in a WPF window
            else if(tabs.get(0).getText().equals("Printing Shortcuts")) {
                log.info("Going to use WPF interface.");
            
                try {
                    PreferencesSession.findElementByXPath("//Button[starts-with(@AutomationId, 'CancelButton')]").click();
                    log.info("Successfully clicked on Cancel button.");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    log.info("Could not click on Cancel button.");
                    throw new RuntimeException(e);
                }           
            }
            else {
                    log.info("Error: Unknown tab item '" + tabs.get(0).getText() + "'.");
                    return;
            }
        }
        else {
            log.info("Error: No tabs found.");
        }

        PrintDialogSession = Base.GetDesktopSession(device_name);
        ClickButton(PrintDialogSession, "Cancel");
        
    }


    // Method to open Notepad test file
    @SuppressWarnings("rawtypes")
	public static WindowsDriver OpenNoteFile(String device_name, String test_filename) throws MalformedURLException {

        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
            capabilities.setCapability("appArguments",test_filename );
            capabilities.setCapability("appWorkingDir", testfiles_loc);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
            NotepadSession = new WindowsDriver(new URL(WindowsApplicationDriverUrl), capabilities);   
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
   
    
    public static void RunWin32Test(RemoteWebDriver session, List<WebElement> tabs, List<String> parameters, String device_name) throws InterruptedException, MalformedURLException {

        // Store the tab elements in reader-friendly variables
        // Make sure tab is what we're expecting
        Assert.assertEquals("Layout", tabs.get(0).getText());
        WebElement layoutTab = tabs.get(0);
        // Make sure tab is what we're expecting
        Assert.assertEquals("Paper/Quality", tabs.get(1).getText());
        WebElement paperQuality = tabs.get(1);
        
        // Store the list elements in reader-friendly variables 
        String orientation = parameters.get(0);
        String duplex_optn = parameters.get(1);
        String color_optn = parameters.get(2);
        String prnt_quality = parameters.get(3);
        String paper_size = parameters.get(4);
        
        // Change the settings on the Layout tab
        SelectPreferencesTab_Win32(session, layoutTab.getText());
        
        ChooseDuplexOrSimplex_Win32(session, duplex_optn, device_name);
        ChooseOrientation_Win32(session, orientation, device_name);


        // Change the settings on the Paper/Quality tab
        SelectPreferencesTab_Win32(session, paperQuality.getText());
        
        ChooseColorOrMono_Win32(session, color_optn);
        ChoosePrintQuality_Win32(session, prnt_quality);

        
        // Now open the Advanced settings
        ClickButton(session, "Advanced...");
        
        // Close the session for the Preferences dialog box
        try {
            session.quit();
            log.info("Closed " + session + "...");
        } catch (Exception e) {
            log.info("'" + session + "' already terminated.");
        }
        
        
        // Open a session for the Advanced dialog box
        log.info("Opening AdvancedSession...");
        AdvancedSession = GetDesktopSession(device_name);
        Assert.assertNotNull(AdvancedSession);
        
        ChoosePaperSize_Win32(AdvancedSession, paper_size, device_name);
        
        ClickButton(AdvancedSession, "OK");
        ClickButton(AdvancedSession, "OK");
        
        try {
            AdvancedSession.quit();
            log.info("Closed " + AdvancedSession + "...");
        } catch (Exception e) {
            log.info("'" + AdvancedSession + "' already terminated.");
        }
    }

    
    public static void RunWPFTest(RemoteWebDriver session, List<WebElement> tabs, List<String> parameters, String device_name) throws InterruptedException, MalformedURLException {

        // Store the tab elements in reader-friendly variables
        
        // Make sure tabs are what we're expecting with Asserts
        Assert.assertEquals("Printing Shortcuts", tabs.get(0).getText());
        WebElement shortcutsTab = tabs.get(0);
        log.info("Found tab " + shortcutsTab);
        
        Assert.assertEquals("Paper/Quality", tabs.get(1).getText());
        WebElement paperQualityTab = tabs.get(1);
        log.info("Found tab " + paperQualityTab);
        
        Assert.assertEquals("Layout", tabs.get(2).getText());
        WebElement layoutTab = tabs.get(2);
        log.info("Found tab " + layoutTab);
        
        Assert.assertEquals("Advanced", tabs.get(3).getText());
        WebElement advancedTab = tabs.get(3);
        log.info("Found tab " + advancedTab);
        
        
        
        
        
        // Store the list elements in reader-friendly variables 
        String orientation = parameters.get(0);
        String duplex_optn = parameters.get(1);
        String color_optn = parameters.get(2);
        String prnt_quality = parameters.get(3);
        String paper_size = parameters.get(4);
        
        // Change the settings on the Layout tab
        SelectPreferencesTab_Win32(session, layoutTab.getText());

        SelectPreferencesTab_Win32(session, shortcutsTab.getText());
        
        SelectPreferencesTab_Win32(session, paperQualityTab.getText());
        
        SelectPreferencesTab_Win32(session, advancedTab.getText());
        
        try {
            session.findElementByXPath("//Button[starts-with(@AutomationId, 'CancelButton')]").click();
            log.info("Successfully clicked on Cancel button.");
            Thread.sleep(1000);
        } catch (Exception e) {
            log.info("Could not click on Cancel button.");
            throw new RuntimeException(e);
        }  
    }
}

