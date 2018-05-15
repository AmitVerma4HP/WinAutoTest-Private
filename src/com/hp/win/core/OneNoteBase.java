package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;



public class OneNoteBase extends UwpAppBase {
        private static final Logger log = LogManager.getLogger(OneNoteBase.class);
        public static RemoteWebDriver OneNoteSession = null;
        

        // Method to open OneNote browser with desired URL.
        public static RemoteWebDriver OpenOneNote(String device_name, String web_url)
                throws MalformedURLException {

            try {
                capabilities = new DesiredCapabilities();
                capabilities.setCapability("app", "Microsoft.MicrosoftEdge_8wekyb3d8bbwe!MicrosoftEdge");
                //capabilities.setCapability("appArguments", "onenote:https:///d.docs.live.net//85b0aae1c28ff01f//Documents//OneNoteTestNotebook//");
                capabilities.setCapability("platformName", "Windows");
                capabilities.setCapability("deviceName", device_name);
                OneNoteSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
                Assert.assertNotNull(OneNoteSession);
                OneNoteSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                OneNoteSession.manage().window().maximize();
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info("Error opening Edge app");
            }
            
            log.info("Opened OneNoteSession successfully");
            return OneNoteSession;
            
        }
        
        
        // Method to print web page from MsEdge Browser
        public static void PrintOneNote(String ptr_name, String web_url) throws InterruptedException {
            // Go to More settings at the top right corner.
            
            // Tap on Print icon
            OneNoteSession.findElementByName("Print").click();
            log.info("Clicked on Print button Successfully to launch the print options screen");
            Thread.sleep(1000);
        }

        // Method to open OneNote.
            public static RemoteWebDriver OpenOneNoteApp(String device_name, String test_filename)
                    throws MalformedURLException {

                try {
                    capabilities = new DesiredCapabilities();
                    capabilities.setCapability("app", "Microsoft.Office.OneNote_8wekyb3d8bbwe!microsoft.onenoteim");
                    //capabilities.setCapability("appArguments", testfiles_loc + test_filename);
                    capabilities.setCapability("platformName", "Windows");
                    capabilities.setCapability("deviceName", device_name);
                    //capabilities.setCapability("appWorkingDir", testfiles_loc);
                    OneNoteSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
                    Assert.assertNotNull(OneNoteSession);
                    OneNoteSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("Error opening OneNote");
                    throw new RuntimeException(e);
                }
                    
                log.info("Opened OneNote successfully");
                return OneNoteSession;
                
            }
        
}



/*package com.hp.win.core;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
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
    //public static WindowsDriver<WindowsElement> OneNoteSession = null;
    public static RemoteWebDriver OneNoteSession = null;
    public static RemoteWebDriver FirstOneNoteSession = null;
    public static RemoteWebDriver FileBrowseSession = null;
    public static RemoteWebDriver PrintDialogSession = null;
    public static RemoteWebDriver PreferencesSession = null;
    public static RemoteWebDriver AdvancedSession = null;
    
    

    // Method to open OneNote app
    public static RemoteWebDriver OpenOneNoteApp(String device_name, String oneNote2016_exe_loc, String test_notebook) throws MalformedURLException, InterruptedException, IOException {
                
        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", oneNote2016_exe_loc);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
        
            FirstOneNoteSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
            Assert.assertNotNull(FirstOneNoteSession);
            FirstOneNoteSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error opening OneNote app.");
            throw new RuntimeException(e);
        }
        
        
        log.info("Opened "+ test_notebook +" file from "+ testfiles_loc);
        
        Thread.sleep(3000); 
        
        log.info("Relauching OneNote App to Get Main Window Session");
        
        
        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", oneNote2016_exe_loc);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
        
            OneNoteSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
            Assert.assertNotNull(OneNoteSession);
            OneNoteSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            
        }catch(Exception e){
                       
            Thread.sleep(3000);
                e.printStackTrace();
                log.info("Error opening OneNote app.");
                throw new RuntimeException(e);
            }

        try {
            FirstOneNoteSession.close();
            FirstOneNoteSession.quit();
        } catch (Exception e) {
            log.info("First OneNote session already closed.");
        }
        
        log.info("Successfully opened OneNote.");
        return OneNoteSession;
        }


    
    public static void OpenOneNoteFile(String test_notebook, String device_name) throws InterruptedException, MalformedURLException, WebDriverException {
        
        log.info("Going to open file '" + test_notebook + "' on laptop '" + device_name + "'...");
        
        Base.BringWindowToFront(device_name, OneNoteSession, "Framework::CFrame");
        try {
            WebDriverWait wait = new WebDriverWait(OneNoteSession, 30);
            //  Using File Tab menu button to test that buttons are clickable and OneNote can be interacted with
            wait.until(ExpectedConditions.elementToBeClickable(By.name("File Tab")));
            log.info("Waiting to be able to interact with OneNote.");
        } catch (Exception e) {
            log.info("Unable to wait for OneNote via File Tab button.");
            throw new RuntimeException(e);
        }
        
               
        // Keyboard shortcut to get to File => Open
        log.info("Using 'ctrl' + 'o' to open file...");
        OneNoteSession.getKeyboard().pressKey(Keys.CONTROL + "o");
        OneNoteSession.getKeyboard().releaseKey("o");
        OneNoteSession.getKeyboard().releaseKey(Keys.CONTROL);
        
        Thread.sleep(1000);
        
        // Use page down to get to the bottom of the screen without scrolling
        log.info("Navigating to bottom of menu...");
        OneNoteSession.getKeyboard().pressKey(Keys.PAGE_DOWN);
        OneNoteSession.getKeyboard().releaseKey(Keys.PAGE_DOWN);
        
        Thread.sleep(1000);
        
        // Click on the browse button to open the dialog to open a file
        Base.ClickButton(OneNoteSession, "Browse");
        
        // Open a new desktop session to handle the open file dialog
        FileBrowseSession = Base.GetDesktopSession(device_name);
        log.info("Created FileBrowseSession.");
        Win32Base.ConfirmDialogBox(device_name, FileBrowseSession, "Open Notebook");
        try {
            // Keyboard shortcut to highlight the address edit field
            log.info("Typing in file location...");
            FileBrowseSession.getKeyboard().pressKey(Keys.ALT + "d");
            FileBrowseSession.getKeyboard().releaseKey("d");
            FileBrowseSession.getKeyboard().releaseKey(Keys.ALT);
            
            Thread.sleep(1000);
            
            // Type in the path to the file and open the folder
            FileBrowseSession.getKeyboard().sendKeys(testfiles_loc);
            FileBrowseSession.getKeyboard().pressKey(Keys.ENTER);
            //FileBrowseSession.getKeyboard().releaseKey(Keys.ENTER);

            Thread.sleep(1000);
            
            // Keyboard shortcut to highlight the name edit field
            log.info("Typing in file name...");
            FileBrowseSession.getKeyboard().pressKey(Keys.ALT + "n");
            FileBrowseSession.getKeyboard().releaseKey("n");
            FileBrowseSession.getKeyboard().releaseKey(Keys.ALT);
            
            Thread.sleep(1000);
            
            // Type in the file name and hit enter to open it
            FileBrowseSession.getKeyboard().sendKeys(test_notebook);
            //FileBrowseSession.getKeyboard().pressKey(Keys.ENTER);
            
            FileBrowseSession.getKeyboard().pressKey(Keys.ALT + "o");
            FileBrowseSession.getKeyboard().releaseKey("o");
            FileBrowseSession.getKeyboard().releaseKey(Keys.ALT);

            Thread.sleep(1000);
            
            log.info("Successfully opened the OneNote test file '" + test_notebook + "'.");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("There was a problem opening the OneNote test file.");
            throw new RuntimeException(e);
        }
        
        try {
            log.info("Going to quit FileBrowseSession...");
            FileBrowseSession.quit();
        } catch (Exception e) {
            log.info("FileBrowseSession already terminated.");
        }
        
    }


    public static void PrintOneNoteFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String prnt_quality, String paper_size, String device_name, String test_notebook) throws InterruptedException, MalformedURLException {
              
        
        OpenOneNoteFile(test_notebook, device_name);
        
        // Use ctrl + p shortcut to open print dialog
        OneNoteSession.getKeyboard().pressKey(Keys.CONTROL + "p");
        OneNoteSession.getKeyboard().releaseKey("p");
        OneNoteSession.getKeyboard().releaseKey(Keys.CONTROL);

        
        Base.ClickButton(OneNoteSession, "File Tab");
        
        try {
            OneNoteSession.findElementByName("Print").click();
            log.info("Successfully clicked on 'Print' tab item.");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error clicking on Tab Item.");
            throw new RuntimeException(e);  
        }
        
        Base.ClickButton(OneNoteSession, "Print");
        
        Thread.sleep(1000);
        
        log.info("Opening PrintDialogSession...");
        PrintDialogSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PrintDialogSession);
        
        ConfirmDialogBox(device_name, PrintDialogSession, "Print");
                        
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

        log.info("Opening 'Preferences' menu...");
        PrintDialogSession.getKeyboard().pressKey(Keys.ALT + "r");
        PrintDialogSession.getKeyboard().releaseKey("r");
        PrintDialogSession.getKeyboard().releaseKey(Keys.ALT);
        Base.ClickButton(PrintDialogSession, "Preferences");
        
        try {
            PrintDialogSession.quit();
            log.info("Closed PrintDialogSession...");
        } catch (Exception e) {
            log.info("PrintDialogSession already terminated.");
        }
        
        PreferencesSession = GetDesktopSession(device_name);
        PreferencesSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Assert.assertNotNull(PreferencesSession);
        
        ConfirmDialogBox(device_name, PreferencesSession, "Printing Preferences");
        
        //Win32Base.ChooseOrientation_Win32(PreferencesSession, orientation, device_name);
        
        
        // Select Preferences on the Layout tab first
        log.info("Looking for orientation settings...");
        //Win32Base.ComboBoxHotkeySelect(PreferencesSession, "Orientation: ", "o", orientation, device_name);
        Win32Base.ChooseOrientation_Win32(PreferencesSession, orientation, device_name);
        
        RemoteWebDriver FirstNewPreferencesSession = Base.GetDesktopSession(device_name);
        
        log.info("Looking for duplex settings....");
        //Win32Base.ComboBoxHotkeySelect(PreferencesSession, "Print on Both Sides: ", "b", duplex_optn, device_name);
        Win32Base.ChooseDuplexOrSimplex_Win32(FirstNewPreferencesSession, duplex_optn, device_name);
        
        // Select settings on Paper/Quality tab after the Layout tab
        
        //log.info("Changing menu tabs.");
        SelectPreferencesTab_Win32(PreferencesSession, "Paper/Quality");
        PreferencesSession.getKeyboard().sendKeys(Keys.CONTROL, Keys.TAB);
        PreferencesSession.getKeyboard().releaseKey(Keys.TAB);
        PreferencesSession.getKeyboard().releaseKey(Keys.CONTROL);
        
        try {
            PreferencesSession.quit();
        } catch (Exception e) {
            log.info("PreferencesSession already terminated.");
        }
        
        RemoteWebDriver QualitySession = Base.GetDesktopSession(device_name);
                
        //log.info("Looking for print quality settings.");
        //ChoosePrintQuality_Win32(QualitySession, prnt_quality);
        

        //log.info("Looking for color settings.");
        //ChooseColorOrMono_Win32(QualitySession, color_optn);
        
        
               
        //PreferencesSession.getKeyboard().pressKey(Keys.ENTER);
        //PreferencesSession.getKeyboard().pressKey(Keys.TAB);
        Base.ClickButton(QualitySession, "OK");
        
        try {
            QualitySession.quit();
            log.info("Successfully quit QualitySession.");
        } catch (Exception e) {
            log.info("QualitySession already terminated.");
        }
        
        RemoteWebDriver SecondNewPreferencesSession = Base.GetDesktopSession(device_name);
        Base.ClickButton(SecondNewPreferencesSession, "OK");
        
        try {
            SecondNewPreferencesSession.quit();
            log.info("Successfully quit SecondNewPreferencesSession.");
        } catch (Exception e) {
            log.info("SecondNewPreferencesSession already terminated.");
        }
        
        PrintDialogSession = Base.GetDesktopSession(device_name);
        ConfirmDialogBox(device_name, PrintDialogSession, "Print");
        
        //PrintDialogSession.getKeyboard().pressKey(Keys.TAB);
        
        log.info("Going to select 'Print' now.");
        Base.ClickButton(PrintDialogSession, "Print");
        PrintDialogSession.getKeyboard().pressKey(Keys.ALT + "p");
        PrintDialogSession.getKeyboard().releaseKey("p");
        PrintDialogSession.getKeyboard().releaseKey(Keys.ALT);
        
        
       
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
                
        ConfirmDialogBox(device_name, AdvancedSession, "Microsoft IPP Class Driver Advanced Document Settings");
        
        ChoosePaperSize_Win32(AdvancedSession, paper_size, device_name);
        
        Base.ClickButton(AdvancedSession, "OK");
        
        ConfirmDialogBox(device_name, AdvancedSession, "Printing Preferences");

        ClickButton(AdvancedSession, "OK");
        
        ConfirmDialogBox(device_name, AdvancedSession, "Print");
        
        Base.ClickButton(AdvancedSession, "Print");
        
        //ClickButton(AdvancedSession, "OK");
        //ClickButton(AdvancedSession, "OK");
        //ClickButton(AdvancedSession, "Print");




        
    }

    
    
    // This method will close the notebook currently open in OneNote
    public static void CloseNotebook(RemoteWebDriver session) {
        log.info("Going to close the OneNote Notebook now...");
        // Move focus to the Notebook area (place where Notebook name is shown from drop down menu)
        session.getKeyboard().pressKey(Keys.CONTROL + "g");
        session.getKeyboard().releaseKey("g");
        session.getKeyboard().releaseKey(Keys.CONTROL);
        
        // Use keyboard shortcut to open menu (keyboard alternative to right click)
        session.getKeyboard().pressKey(Keys.SHIFT);
        session.getKeyboard().pressKey(Keys.F10);
        session.getKeyboard().sendKeys(Keys.SHIFT, Keys.F10);
        session.getKeyboard().releaseKey(Keys.F10);
        session.getKeyboard().releaseKey(Keys.SHIFT);
        
        // Use keyboard shortcut to select Close Notebook from the menu
        session.getKeyboard().pressKey("c");
        session.getKeyboard().releaseKey("c");
    }

}

*/