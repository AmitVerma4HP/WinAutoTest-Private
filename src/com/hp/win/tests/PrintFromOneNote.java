package com.hp.win.tests;


import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.EdgeAppBase;
import com.hp.win.core.OneNoteBase;
import com.hp.win.utility.ScreenshotUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hp.win.utility.*;                            

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

    @Listeners({ScreenshotUtility.class})
    public class PrintFromOneNote extends OneNoteBase {
        private static final Logger log = LogManager.getLogger(PrintFromOneNote.class);
        private static String currentClass; 
        public static String expectedPrintjob;
        
        public static WebDriverWait wait;
        
        @BeforeClass
        @Parameters({ "device_name", "ptr_name", "test_pagename"})
        public static void setup(String device_name, String ptr_name, @Optional("1pg OneNote")String test_pagename) throws InterruptedException, IOException { 
           
        currentClass = PrintFromOneNote.class.getSimpleName();
                
        //Start PrintTrace log capturing 
        PrintTraceCapture.StartLogCollection(currentClass);                                            
        OneNoteSession = OneNoteBase.OpenOneNoteApp(device_name, test_pagename);
        Thread.sleep(2000);
        
        }
    
        
    @Test
    @Parameters({ "device_name", "ptr_name", "test_pagename","copies","pages","orientation","paper_size","color_optn","duplex_optn","paper_tray","output_qlty","stapling_optn","collation_optn"})
    public void PrintOneNote(String device_name, String ptr_name, @Optional("1pg OneNote")String test_pagename, @Optional("1")String copies, @Optional("Page")String pages, @Optional("Portrait")String orientation, @Optional("Letter")String paper_size, @Optional("Color")String color_optn,  @Optional("None")String duplex_optn,  @Optional("Auto select")String paper_tray, @Optional("Normal")String output_qlty, @Optional("Staple")String stapling_optn, @Optional("Uncollated")String collation_optn) throws InterruptedException, IOException
    {

        OneNoteSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

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
        
        //Select Desired Orientation Option.
        if (OneNoteSession.findElementsByXPath("//ComboBox[@Name = 'Orientation']").size()!=0){
            OneNoteBase.SelectOrientation_Uwp(OneNoteSession,orientation);
        }else{
            log.info("The desired Orientation feature is not supported by the printer ");
        }
        
        //Enter desired Copies value.
        if (OneNoteSession.findElementsByName("Copies").size()!=0){
            OneNoteBase.SelectCopies_Uwp(OneNoteSession, copies);
        }else{
            log.info("The desired Copies selection feature is not supported by the printer ");
        }
        
                    
        //Select Desired Pages(page range) Option.
        
        if (OneNoteSession.findElementsByName("Pages").size()!=0){
            String pageSelection = "Current " + pages;
            OneNoteBase.SelectPageRange_Uwp(OneNoteSession, pageSelection);
        }else{
            log.info("The desired Pages(Page Range) feature is not supported by the printer ");
        }

        //Opening more settings to access more printing options.
        int MoreSettings = OneNoteBase.OpenMoreSettings(OneNoteSession);
        if  (MoreSettings == 1){    
            
            //Select Desired Duplex Option.         
            if (OneNoteSession.findElementsByName("Duplex printing").size()!=0){
                OneNoteBase.SelectDuplexOption_Uwp(OneNoteSession, duplex_optn);
            }else{
                log.info("The desired Duplexing feature is not supported by the printer ");
            }
            
            //Select Desired Collation Option.          
            if (OneNoteSession.findElementsByName("Collation").size()!=0){
                OneNoteBase.SelectCollation_Uwp(OneNoteSession, collation_optn);
            }else{
                log.info("The desired Collation feature is not supported by the printer ");
            }
            
            //Select Desired Paper Size Option
            if (OneNoteSession.findElementsByName("Paper size").size()!=0){
                OneNoteBase.SelectPaperSize_Uwp(OneNoteSession, paper_size);
            }else{
                log.info("The desired Paper Size feature is not supported by the printer ");
            }

            //Select Desired Paper Tray Option.         
            if (OneNoteSession.findElementsByName("Paper tray").size()!=0){
                OneNoteBase.SelectPaperTray_Uwp(OneNoteSession, paper_tray);
            }else{
                log.info("The desired Paper Tray feature is not supported by the printer ");
            }
                            
            //Select Desired Output Quality Option.         
            if (OneNoteSession.findElementsByName("Output quality").size()!=0){
                OneNoteBase.SelectOutputQuality_Uwp(OneNoteSession, output_qlty);
            }else{
                log.info("The desired Output Quality feature is not supported by the printer ");
            }
            
            //Select Desired Color Mode Option.         
            if (OneNoteSession.findElementsByName("Color mode").size()!=0){
                OneNoteBase.SelectColorOrMono_Uwp(OneNoteSession, color_optn);
            }else{
                log.info("The desired Color Mode feature is not supported by the printer ");
            }
                        
            //Select Desired Stapling Option.           
            if (OneNoteSession.findElementsByName("Stapling").size()!=0){
                EdgeAppBase.SelectStaplingOption_Uwp(OneNoteSession,stapling_optn);
            }else{
                log.info("The desired Stapling feature is not supported by the printer ");
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
    public void ValidatePrintQueue(String device_name, String ptr_name, @Optional("1pg OneNote")String test_pagename) throws IOException, InterruptedException 
    {
        // Open Print Queue
        Base.OpenPrintQueue(ptr_name);
                
        // Method to attach session to Printer Queue Window
        Base.SwitchToPrinterQueue(device_name,ptr_name);
        
        log.info("Expected queued job should be => " + test_pagename);
        
         //Validate Print Job Queued up
        try {
            Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains("OneNote"));
        }catch(NoSuchElementException e) {
            log.info("Expected Print job is not found in print queue");
            throw new RuntimeException(e);
        }catch(Exception e) {
            log.info("Error validating print job in print queue");
            throw new RuntimeException(e);
        }
        
        log.info("Found job in print queue");
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