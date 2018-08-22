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


package com.hp.win.tests;


import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.EdgeAppBase;
import com.hp.win.core.OneNoteBase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.hp.win.utility.*;                            
import java.io.IOException;
import java.util.concurrent.TimeUnit;

    @Listeners({ScreenshotUtility.class})
    public class PrintFromOneNote extends OneNoteBase {
        private static final Logger log = LogManager.getLogger(PrintFromOneNote.class);
        private static String currentClass; 
        public static String expectedPrintjob;
        
        public static WebDriverWait wait;
        
        @BeforeClass
        @Parameters({ "device_name", "ptr_name", "test_pagename"})
        public static void setup(String device_name, String ptr_name, String test_pagename) throws InterruptedException, IOException { 
           
        currentClass = PrintFromOneNote.class.getSimpleName();
                
        //Start PrintTrace log capturing 
        PrintTraceCapture.StartLogCollection(currentClass);   
        
        //Get windows build info
    	GetWindowsBuild.GetWindowsBuildInfo();
    	GetWindowsBuild.PrintWindowsBuildInfo();
    	
        OneNoteSession = OneNoteBase.OpenOneNoteApp(device_name, test_pagename);
        Thread.sleep(2000);
        
        }
    
        
    @Test
    @Parameters({ "device_name", "ptr_name", "test_pagename","copies","pages_selection","orientation","paper_size","color_optn","duplex_optn","paper_tray","output_qlty","stapling_optn","collation_optn"})
    public void PrintOneNote(String device_name, String ptr_name, String test_pagename, @Optional("1")String copies, @Optional("Page")String pages_selection, @Optional("Portrait")String orientation, @Optional("Letter")String paper_size, @Optional("Color")String color_optn,  @Optional("None")String duplex_optn,  @Optional("Auto select")String paper_tray, @Optional("Normal")String output_qlty, @Optional("Staple")String stapling_optn, @Optional("Uncollated")String collation_optn) throws InterruptedException, IOException
    {

        OneNoteSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // OneNote may not allow clicking on opening - clicking its icon in the taskbar to minimize then restore it enables clicking again
        // A new desktop session is needed to click items in the taskbar 
        RemoteWebDriver TempSession = Base.GetDesktopSession(device_name);
        try {
        	TempSession.findElementByXPath("//Button[contains(@Name,'OneNote - 1 running window')]").click();
//        	TempSession.findElementByName("OneNote - 1 running window").click();
        	log.info("Successfully minimized OneNote by clicking icon in taskbar.");
        	Thread.sleep(1000);
        } catch (Exception e) {
        	log.info("Unable to click on button in taskbar.");
        }
        
        try {
        	TempSession.findElementByXPath("//Button[contains(@Name,'OneNote - 1 running window')]").click();
        	log.info("Successfully restored OneNote by clicking icon in taskbar.");
        	Thread.sleep(1000);
        } catch (Exception e) {
        	log.info("Unable to click on button in taskbar.");
        }
        
        if(TempSession != null) {
        	TempSession.quit();
        }
        
        // Bring focus back to OneNote now
        //Base.BringWindowToFront(device_name, OneNoteSession, "ApplicationFrameWindow");
        
        // Click on the OneNote page to ensure that it's in focus, otherwise 'ctrl + p' might not work
        try {
            OneNoteSession.findElementByXPath(".//Window[@Name='OneNote']//Pane[@AutomationId='Silhouette']//Tree//Custom").click();
            log.info("Successfully clicked on OneNote Page Surface.");
        } catch (Exception e) {
            log.info("Could not click on OneNote Page Surface.");
        }
        
        OneNoteSession.getKeyboard().sendKeys(Keys.CONTROL + "p");
        OneNoteSession.getKeyboard().releaseKey("p");
        OneNoteSession.getKeyboard().releaseKey(Keys.CONTROL);

        
        // Method to select the desired printer.
        OneNoteBase.SelectDesiredPrinter(OneNoteSession, ptr_name);
        
        
        // Uncheck option "“Let the app change my printing preferences” if it is checked in App Print UI
     	Base.UncheckAppChangePreference(OneNoteSession);
        
        //Select Desired Orientation Option.
        if (OneNoteSession.findElementsByXPath("//ComboBox[@Name = 'Orientation']").size()!=0){
            OneNoteBase.SelectOrientation_Uwp(OneNoteSession,orientation);
        }else{
			log.info("........................................................");
			log.info("The desired Orientation selection didnt appear in App UI");
			log.info("........................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log("........................................................");
			Reporter.log("The desired Orientation selection didnt appear in App UI");
			Reporter.log("........................................................");
        }
        
        //Enter desired Copies value.
        if (OneNoteSession.findElementsByName("Copies").size()!=0){
            OneNoteBase.SelectCopies_Uwp(OneNoteSession, copies);
        }else{
			log.info("...................................................");
			log.info("The desired Copies selection didnt appear in App UI");
			log.info("...................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log("...................................................");
			Reporter.log("The desired Copies selection didnt appear in App UI");
			Reporter.log("...................................................");
        }
        
                    
        //Select Desired Pages(page range) Option.        
        if (OneNoteSession.findElementsByName("Pages").size()!=0){
            String pageSelection = "Current " + pages_selection;
            OneNoteBase.SelectPageRange_Uwp(OneNoteSession, pageSelection);
        }else{
			log.info(".............................................................");
			log.info("The desired Pages(PageRange) selection didnt appear in App UI");
			log.info(".............................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log("..............................................................");
			Reporter.log("The desired Pages(PageRange)  selection didnt appear in App UI");
			Reporter.log("..............................................................");
        }
        
        //Opening more settings to access more printing options.
        int MoreSettings = OneNoteBase.OpenMoreSettings(OneNoteSession);
        if  (MoreSettings == 1){    
            
            //Select Desired Duplex Option.         
            if (OneNoteSession.findElementsByName("Duplex printing").size()!=0){
                OneNoteBase.SelectDuplexOption_Uwp(OneNoteSession, duplex_optn);
            }else{
				log.info("...................................................");
				log.info("The desired Duplex selection didnt appear in App UI");
				log.info("...................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("...................................................");
				Reporter.log("The desired Duplex selection didnt appear in App UI");
				Reporter.log("...................................................");
            }
            
            //Select Desired Collation Option.          
            if (OneNoteSession.findElementsByName("Collation").size()!=0){
                OneNoteBase.SelectCollation_Uwp(OneNoteSession, collation_optn);
            }else{
    			log.info("......................................................");
    			log.info("The desired Collation selection didnt appear in App UI");
    			log.info("......................................................");
    			
    			//This is to insert msg to TestNG emailable-report.html
    			Reporter.log("......................................................");
    			Reporter.log("The desired Collation selection didnt appear in App UI");
    			Reporter.log("......................................................");
            }
            
            //Select Desired Paper Size Option
            if (OneNoteSession.findElementsByName("Paper size").size()!=0){
                OneNoteBase.SelectPaperSize_Uwp(OneNoteSession, paper_size);
            }else{
    			log.info("......................................................");
    			log.info("The desired PaperSize selection didnt appear in App UI");
    			log.info("......................................................");
    			
    			//This is to insert msg to TestNG emailable-report.html
    			Reporter.log("......................................................");
    			Reporter.log("The desired PaperSize selection didnt appear in App UI");
    			Reporter.log("......................................................");
            }

            //Select Desired Paper Tray Option.         
            if (OneNoteSession.findElementsByName("Paper tray").size()!=0){
                OneNoteBase.SelectPaperTray_Uwp(OneNoteSession, paper_tray);
            }else{
				log.info("......................................................");
				log.info("The desired PaperTray selection didnt appear in App UI");
				log.info("......................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("......................................................");
				Reporter.log("The desired PaperTray selection didnt appear in App UI");
				Reporter.log("......................................................");
            }
                            
            //Select Desired Output Quality Option.         
            if (OneNoteSession.findElementsByName("Output quality").size()!=0){
                OneNoteBase.SelectOutputQuality_Uwp(OneNoteSession, output_qlty);
            }else{
				log.info("...........................................................");
				log.info("The desired Output Quality selection didnt appear in App UI");
				log.info("...........................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("...........................................................");
				Reporter.log("The desired Output Quality selection didnt appear in App UI");
				Reporter.log("...........................................................");
            }
            
            //Select Desired Color Mode Option.         
            if (OneNoteSession.findElementsByName("Color mode").size()!=0){
                OneNoteBase.SelectColorOrMono_Uwp(OneNoteSession, color_optn);
            }else{
				log.info(".......................................................");
				log.info("The desired Color Mode selection didnt appear in App UI");
				log.info(".......................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log(".......................................................");
				Reporter.log("The desired Color Mode selection didnt appear in App UI");
				Reporter.log(".......................................................");
            }
                        
            //Select Desired Stapling Option.           
            if (OneNoteSession.findElementsByName("Stapling").size()!=0){
                EdgeAppBase.SelectStaplingOption_Uwp(OneNoteSession,stapling_optn);
            }else{
				log.info(".....................................................");
				log.info("The desired Stapling selection didnt appear in App UI");
				log.info(".....................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log(".....................................................");
				Reporter.log("The desired Stapling selection didnt appear in App UI");
				Reporter.log(".....................................................");
            }
            
            
            //Closing more settings after accessing more printing options.
            OneNoteBase.CloseMoreSettings(OneNoteSession);
            
        }else{
            log.info("More Settings page could not be opened");
            OneNoteSession.findElementByXPath("//Button[@AutomationId = 'CloseButton']").click();
            Assert.fail();
        }
            
            // Tap on Print button(Give Print)
            OneNoteSession.findElementByXPath("//Button[@AutomationId = 'PrintButton']").click();
            log.info("Clicked on final Print button -> Print option Successfully");

            log.info("Waiting to allow app more time for job to print.");
            Thread.sleep(5000);
        }

 
        
    @Test(dependsOnMethods = { "PrintOneNote" })
    @Parameters({ "device_name", "ptr_name", "test_pagename"})
    public void ValidatePrintQueue(String device_name, String ptr_name, String test_pagename) throws IOException, InterruptedException 
    {
        // Open Print Queue
        Base.OpenPrintQueue(ptr_name);
                
        // Method to attach session to Printer Queue Window
        Base.SwitchToPrinterQueue(device_name,ptr_name);
        
        log.info("Expected queued job should be => " + test_pagename);
        
         //Validate Print Job Queued up
        try {
            String job = PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name");
            log.info("Found job '" + job + "' in queue.");
            Assert.assertTrue(job.contains(test_pagename));
        }catch(NoSuchElementException e) {
            log.info("Expected Print job is not found in print queue");
            throw new RuntimeException(e);
        }catch(Exception e) {
            log.info("Error validating print job in print queue");
            throw new RuntimeException(e);
        }
        
        //Waiting for the job to get spooled completely before closing the print queue window.
	    wait = new WebDriverWait(PrintQueueSession,60);
	    wait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("//ListItem[@AutomationId='ListViewItem-1']"),"Spooling"));
	    log.info("Waiting until the job spooling is completed");
        
	    //if explicit wait does not work then adding extra 1 second 
        Thread.sleep(1000);
        PrintQueueSession.close();
        log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");
        
    }
    
    @AfterClass(alwaysRun=true)
    public static void TearDown() throws IOException, InterruptedException 
    {           
    
        if (OneNoteSession!= null)
        {
            OneNoteSession.quit();
        }
                
        if (DesktopSession!=null)
        {
            DesktopSession.quit();
        }
        
        if (PrintQueueSession!=null)
        {
            PrintQueueSession.quit();
        }
                
        //Stop PrintTrace log capturing.
        PrintTraceCapture.StopLogCollection(currentClass);                     
    }
}
