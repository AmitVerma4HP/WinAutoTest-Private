package com.hp.win.core;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Win32Base;
import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;


@Listeners({ScreenshotUtility.class})
public class OneNoteBase extends Win32Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);
    public static RemoteWebDriver SignInSession = null;
    public static RemoteWebDriver OneNoteSession = null;
    
    

    // Method to open Notepad test file
    public static RemoteWebDriver OpenOneNoteApp(String device_name, String test_filename) throws MalformedURLException {

        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Program Files (x86)\\Microsoft Office\\root\\Office16\\ONENOTE.exe");
            //capabilities.setCapability("appArguments",test_filename );
            //capabilities.setCapability("appWorkingDir", testfiles_loc);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
            OneNoteSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);   
            Assert.assertNotNull(OneNoteSession);
            OneNoteSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);                                                  
        }catch(Exception e){
            e.printStackTrace();
            //log.info("Error opening notepad file");
            log.info("Error opening OneNote session...");
            throw new RuntimeException(e);
        }
        //log.info("Opened"+test_filename+"file from "+testfiles_loc);
        log.info("Successfully opened OneNote.");
        return OneNoteSession;
    }



    public static void PrintOneNoteFile(String ptr_name, String device_name, String test_filename) throws InterruptedException, MalformedURLException {
        //TODO stub for printing a file
        
        log.info("Going to open a OneNote file...");
        OpenOneNoteFile(test_filename, device_name);
    }

    
    public static void OpenOneNoteFile(String test_filename, String device_name) throws InterruptedException, MalformedURLException {
        //TODO stub for opening a file
        
        log.info("Checking for 'Sign in to set up Office' window...");
        CloseMicrosoftAccountPrompts(device_name);
        
        log.info("Back to the main OneNote window now...");
        if(OneNoteSession == null) {
            log.info("OneNoteSession was closed. Opening another one...");
            OneNoteSession = GetDesktopSession(device_name);
        }
        
        log.info("OneNoteSession exists. Going to open the File tab...");
        Assert.assertNotNull(OneNoteSession.findElementByName("File Tab"));
        ClickButton(OneNoteSession, "File Tab");
    }

    
    //This method handles the 'Sign in to set up Office' prompt
    public static void CloseMicrosoftAccountPrompts(String device_name) throws InterruptedException, MalformedURLException {
        
        try {

            SignInSession = Base.GetDesktopSession(device_name);
            WebElement signInWindow = DesktopSession.findElementByClassName("NUIDialog");
            log.info("'" + signInWindow.getAttribute("Name").toString() + "' window is present. Going to close it.");
            ClickButton(SignInSession, "Close");

        } catch (Exception e) {
        
            log.info("'Sign in to set up Office' window is not present. Continuing test...");
        
        }

    }
}

