/*
Copyright 2018 Mopria Alliance, Inc.
Copyright 2018 HP Development Company, L.P.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;

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
    
    
    // This is an overloaded method from UwpAppBase - it is to be used specifically with OneNote
    // OneNote does not have a numbered page range, rather, it allows you to print the currently open page,
    // the currently open section, or the currently open notebook
    public static void SelectPageRange_Uwp(RemoteWebDriver Session, String page_selection) throws InterruptedException {
        
        WebElement PageRangeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Pages']");
        Assert.assertNotNull(PageRangeListComboBox);           
        if(!PageRangeListComboBox.getText().toString().contentEquals(page_selection)) 
        {
            log.info("Desired Page Selection => "+page_selection+" <= is not selected so selecting it from drop down");
            PageRangeListComboBox.click();
            Thread.sleep(1000);
            try {
                Session.findElementByXPath("//*[contains(@Name,'"+page_selection+"')]").click();
                Thread.sleep(2000);
                log.info("Selected desired Page Selection *****" +page_selection+ "*****");
            } catch(Exception e){
            	log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+page_selection+"\" Page Selection is NOT FOUND so either 1) your Printer does not support \""+page_selection+"\" Page Selection OR 2) you have typed the Page Selection option incorrectly in testsuite xml.");
            	log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
            	//This is to insert msg to TestNG emailable-report.html
            	Reporter.log("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	Reporter.log("\""+page_selection+"\" Page Selection is NOT FOUND so either 1) your Printer does not support \""+page_selection+"\" Page Selection OR 2) you have typed the Page Selection option incorrectly in testsuite xml.");
            	Reporter.log("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
                log.info("Error selecting desired Page Selection but continuing with rest of the print options");     
                //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
                PageRangeListComboBox.click();
                Thread.sleep(2000);
            }   
        }else {
            log.info("Desired Page Selection => " + PageRangeListComboBox.getText().toString() + " <= is already selected so proceeding");
        }
    }

}