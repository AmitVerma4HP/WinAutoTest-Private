package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;



public class OneNoteBase extends UwpAppBase {
    private static final Logger log = LogManager.getLogger(OneNoteBase.class);
    public static RemoteWebDriver OneNoteSession = null;


    // Method to open OneNote.
    public static RemoteWebDriver OpenOneNoteApp(String device_name, String test_filename)
            throws MalformedURLException, InterruptedException {
        
        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "Microsoft.Office.OneNote_8wekyb3d8bbwe!microsoft.onenoteim");
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", device_name);
            OneNoteSession = new WindowsDriver<WindowsElement> (new URL(WindowsApplicationDriverUrl), capabilities);
            Assert.assertNotNull(OneNoteSession);
            
            OneNoteSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
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