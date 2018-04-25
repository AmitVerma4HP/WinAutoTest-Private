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


@Listeners({ScreenshotUtility.class})
public class OneNoteBase extends Win32Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);
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



    public static void PrintOneNoteFile(String ptr_name, String device_name, String test_filename) throws InterruptedException {
        //TODO stub for printing a file
        
        log.info("Going to open a OneNote file...");
        OpenOneNoteFile(test_filename);
    }

    
    public static void OpenOneNoteFile(String test_filename) throws InterruptedException {
        //TODO stub for opening a file
        
        log.info("Closing sign-in prompts first...");
        CloseMicrosoftAccountPrompts();
        
    }

    //This method handles any popup prompts regarding a Microsoft account or security key
    public static void CloseMicrosoftAccountPrompts() throws InterruptedException {

        // Might want to try just clicking the Close button instead of the hyperlink
        Assert.assertTrue(OneNoteSession.findElementByClassName("NetUIHyperlink").getAttribute("Name").equals("I don't want to sign in or create an account"));
        log.info("Seeing the first prompt.");
        
        try {
            OneNoteSession.findElementByClassName("NetUIHyperlink").click();
            Thread.sleep(1000);
        } catch (Exception e) {
            log.info("Could not click on the hyperlink...");
        }
    }
}

