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
    
    

    // Method to open OneNote app
    public static WindowsDriver OpenOneNoteApp(String device_name, String test_filename) throws MalformedURLException, InterruptedException, IOException {
           
        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Program Files\\Microsoft Office\\Office16\\ONENOTE.exe");        
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
        
            OneNoteSession = new WindowsDriver(new URL(WindowsApplicationDriverUrl), capabilities);
        
            // Let the app have time to fully load before we try to interact with it
            Thread.sleep(5000);
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



    public static void PrintOneNoteFile(String ptr_name, String device_name, String test_filename) throws InterruptedException, MalformedURLException {
        //TODO stub for printing a file
        
        log.info("Going to open a OneNote file...");
        //OpenOneNoteFile(test_filename, device_name);
    }

    
    public static void OpenOneNoteFile(String test_filename, String device_name) throws InterruptedException, MalformedURLException {
        //TODO stub for opening a file

    }

}

