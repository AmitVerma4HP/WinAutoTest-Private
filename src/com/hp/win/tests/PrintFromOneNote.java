package com.hp.win.tests;


import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.OneNoteBase;
import com.hp.win.utility.ScreenshotUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.hp.win.utility.*;                            

import java.io.IOException;
import java.util.List;

    @Listeners({ScreenshotUtility.class})
    public class PrintFromOneNote extends OneNoteBase {
        private static final Logger log = LogManager.getLogger(PrintFromOneNote.class);
        private static String currentClass; 
        public static RemoteWebDriver OneNoteSession = null;
        public static RemoteWebDriver OneNoteWindowSession = null;
        public static String expectedPrintjob;
        
        @BeforeClass
        @Parameters({ "device_name", "ptr_name", "test_filename"})
        public static void setup(String device_name, String ptr_name, String test_filename) throws InterruptedException, IOException { 
           
        currentClass = PrintFromOneNote.class.getSimpleName();
                
        //Start PrintTrace log capturing 
        PrintTraceCapture.StartLogCollection(currentClass);                                            
        OneNoteSession = OneNoteBase.OpenOneNoteApp(device_name, test_filename);
        Thread.sleep(2000);
        
        }
    
        
    @Test
    @Parameters({ "device_name", "ptr_name", "test_filename","copies","page_range","orientation","paper_size","page_margins","color_optn","duplex_optn","borderless","paper_tray","paper_type","output_qlty","stapling_optn","headerandfooter_optn","scale_optn","collation_optn"})
    public void PrintOneNote(String device_name, String ptr_name, String test_filename, @Optional("1")String copies,@Optional("All pages")String page_range, @Optional("Portrait")String orientation, @Optional("Letter")String paper_size, @Optional("Normal")String page_margins,  @Optional("Color")String color_optn,  @Optional("None")String duplex_optn,  @Optional("On")String borderless,  @Optional("Auto select")String paper_tray, @Optional("Plain Paper")String paper_type, @Optional("Normal")String output_qlty, @Optional("Staple")String stapling_optn, @Optional("On")String headerandfooter_optn, @Optional("Shrink to fit")String scale_optn, @Optional("Uncollated")String collation_optn) throws InterruptedException, IOException
    {

/*        Base.ClickButton(OneNoteSession, "Start taking notes! Tap or click here to create a new notebook. You can also open one of your existing notebooks from the notebook list");
        OneNoteSession.getKeyboard().sendKeys("Test1");
        Base.ClickButton(OneNoteSession, "Create Notebook");*/
        
        log.info("Using ctrl + o...");

        OneNoteSession.getKeyboard().sendKeys(Keys.CONTROL + "o");
        OneNoteSession.getKeyboard().releaseKey("o");
        OneNoteSession.getKeyboard().releaseKey(Keys.CONTROL);
        
        Thread.sleep(3000);
        
        try {
            OneNoteSession.findElementByName("OneNoteTestNotebook1").click();
            log.info("Successfully clicked on " + OneNoteSession.findElementByName("OneNoteTestNotebook1").getAttribute("Name").toString());
        } catch (Exception e) {
            log.info("Couldn't click on Notebook");
        }
        
        Base.ClickButton(OneNoteSession, "Open Notebook");
        
        Thread.sleep(3000);
        

/*        // Method to Print Web page to Printer Under Test
        OneNoteBase.PrintOneNote(ptr_name, test_filename);
        
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
            OneNoteBase.SelectCopies_Uwp(OneNoteSession,copies);
        }else{
            log.info("The desired Copies selection feature is not supported by the printer ");
        }
        
                    
        //Select Desired Pages(page range) Option.
        if (OneNoteSession.findElementsByName("Pages").size()!=0){
            OneNoteBase.SelectPageRange_Uwp(OneNoteSession,page_range);
        }else{
            log.info("The desired Pages(Page Range) feature is not supported by the printer ");
        }
        
        //Select Desired Scaling Option.
        if (OneNoteSession.findElementsByName("Scale").size()!=0){
            OneNoteBase.SelectScale_Uwp(OneNoteSession,scale_optn);
        }else{
            log.info("The desired Scaling feature is not supported by the printer ");
        }
        
        //Select Desired Page Margins Option.
        if (OneNoteSession.findElementsByName("Margins").size()!=0){
            OneNoteBase.SelectPageMargins_Uwp(OneNoteSession,page_margins);
        }else{
            log.info("The desired Page Margin feature is not supported by the printer ");
        }
        
        //Select Desired Borderless Printing Option.
        if (OneNoteSession.findElementsByName("Borderless printing").size()!=0){
            OneNoteBase.SelectBorderless_Uwp(OneNoteSession,borderless);
        }else{
            log.info("The desired Borderless feature is not supported by the printer ");
        }
        
        //Select Desired Header and Footer Option.
        if (OneNoteSession.findElementsByName("Headers and footers").size()!=0){
            OneNoteBase.SelectHeadersAndFooters_Uwp(OneNoteSession,headerandfooter_optn);
        }else{
            log.info("The desired Header and Footer feature is not supported by the printer ");
        }
        
        //Opening more settings to access more printing options.
        int MoreSettings = OneNoteBase.OpenMoreSettings(OneNoteSession);
        if  (MoreSettings == 1){    
            
            //Select Desired Duplex Option.         
            if (OneNoteSession.findElementsByName("Duplex printing").size()!=0){
                OneNoteBase.SelectDuplexOption_Uwp(OneNoteSession,duplex_optn);
            }else{
                log.info("The desired Duplexing feature is not supported by the printer ");
            }
            
            //Select Desired Collation Option.          
            if (OneNoteSession.findElementsByName("Collation").size()!=0){
                OneNoteBase.SelectCollation_Uwp(OneNoteSession,collation_optn);
            }else{
                log.info("The desired Collation feature is not supported by the printer ");
            }
            
            //Select Desired Paper Size Option
            if (OneNoteSession.findElementsByName("Paper size").size()!=0){
                OneNoteBase.SelectPaperSize_Uwp(OneNoteSession,paper_size);
            }else{
                log.info("The desired Paper Size feature is not supported by the printer ");
            }
                        
            //Select Desired Paper Type Option.         
            if (OneNoteSession.findElementsByName("Paper type").size()!=0){
                OneNoteBase.SelectPaperType_Uwp(OneNoteSession,paper_type);
            }else{
                log.info("The desired Paper Type feature is not supported by the printer ");
            }
            
            //Select Desired Paper Tray Option.         
            if (OneNoteSession.findElementsByName("Paper tray").size()!=0){
                OneNoteBase.SelectPaperTray_Uwp(OneNoteSession,paper_tray);
            }else{
                log.info("The desired Paper Tray feature is not supported by the printer ");
            }
                            
            //Select Desired Output Quality Option.         
            if (OneNoteSession.findElementsByName("Output quality").size()!=0){
                OneNoteBase.SelectOutputQuality_Uwp(OneNoteSession,output_qlty);
            }else{
                log.info("The desired Output Quality feature is not supported by the printer ");
            }
            
            //Select Desired Color Mode Option.         
            if (OneNoteSession.findElementsByName("Color mode").size()!=0){
                OneNoteBase.SelectColorOrMono_Uwp(OneNoteSession,color_optn);
            }else{
                log.info("The desired Color Mode feature is not supported by the printer ");
            }
            
            //Select Desired Stapling Option.           
            if (OneNoteSession.findElementsByName("Stapling").size()!=0){
                OneNoteBase.SelectStaplingOption_Uwp(OneNoteSession,stapling_optn);
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
            log.info("Clicked on final Print button -> Print option Successfully");*/
            
        }
 
        
/*    @Test(dependsOnMethods = { "PrintOneNote" })
    @Parameters({ "device_name", "ptr_name", "test_filename"})
    public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
    {
        // Open Print Queue
        Base.OpenPrintQueue(ptr_name);
                
        // Method to attach session to Printer Queue Window
        Base.SwitchToPrinterQueue(device_name,ptr_name);
        
        log.info("Expected queued job should be => "+test_filename);
        
         //Validate Print Job Queued up
        try {
            //Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
            Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains("PDF"));
        }catch(NoSuchElementException e) {
            log.info("Expected Print job is not found in print queue");
            throw new RuntimeException(e);
        }catch(Exception e) {
            log.info("Error validating print job in print queue");
            throw new RuntimeException(e);
        }
        
        //log.info("Found correct job in print queue => "+test_filename);
        log.info("Found PDF job in print queue");
        PrintQueueSession.close();
        log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");      
        
    }*/
    
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





/*package com.hp.win.tests;


import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.OneNoteBase;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;


@Listeners({ScreenshotUtility.class})

public class PrintFromOneNote extends OneNoteBase{
	private static final Logger log = LogManager.getLogger(PrintFromOneNote.class);
	private static String currentClass;
	private static String device_under_test; // Use this parameter for BringWindowToFront() method when necessary
//	private static WindowsDriver NotepadSession = null;
	
	
    @BeforeClass
    @Parameters({ "device_name", "oneNote2016_exe_loc", "ptr_name", "test_filename" })
    public static void setup(String device_name, String oneNote2016_exe_loc, String ptr_name, @Optional("OneNoteTestFile1")String test_filename) throws InterruptedException, IOException {

        currentClass = PrintFromOneNote.class.getSimpleName();
        device_under_test = device_name;
        
        //Start PrintTrace log capturing 
        PrintTraceCapture.StartLogCollection(currentClass); 
        OneNoteSession = OneNoteBase.OpenOneNoteApp(device_name, oneNote2016_exe_loc, test_filename);
       
        Thread.sleep(1000);                         
                    
    }

    
    @Test
    @Parameters({ "ptr_name", "oneNote2016_exe_loc", "orientation", "duplex_optn", "color_optn", "prnt_quality", "paper_size", "device_name", "test_notebook", "test_filename" })
    public void PrintOneNote(String ptr_name, String oneNote2016_exe_loc, @Optional("Portrait")String orientation, @Optional("None")String duplex_optn, @Optional("Color")String color_optn, @Optional("Draft")String prnt_quality, @Optional("Letter")String paper_size, String device_name, @Optional("OneNoteTestNotebook1\\TestNotebook1.onetoc2")String test_notebook, @Optional("OneNoteTestFile1")String test_filename) throws InterruptedException, IOException
    {   

        // Method to Print OneNote File to Printer Under Test
        OneNoteBase.PrintOneNoteFile(ptr_name, orientation, duplex_optn, color_optn, prnt_quality, paper_size, device_name, test_notebook);

        
    }

    
    @Test(dependsOnMethods = { "PrintOneNote" })
    @Parameters({ "device_name", "ptr_name", "test_filename"})
    public void ValidatePrintQueue(String device_name, String ptr_name, @Optional("OneNoteTestFile1")String test_filename) throws IOException, InterruptedException 
    {

        // Method to open the print queue (Moved from setup() method)
        Base.OpenPrintQueue(ptr_name);
        
        // Method to attach session to Printer Queue Window
        Base.SwitchToPrinterQueue(device_name, ptr_name);
        
        log.info("Expected queued job should be => " + test_filename);

        //Validate Print Job Queued up
        Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
        log.info("Found correct job in print queue => " + test_filename);
            
            //Get handle to OneNote window
            
            Base.BringWindowToFront(device_name, OneNoteSession, "Framework::CFrame");
                        
    }
    
    
    @AfterClass(alwaysRun=true)
    public static void TearDown() throws NoSuchSessionException, IOException, InterruptedException
    {           
        
        try {
            OneNoteSession.close();
            OneNoteSession.quit();
        } catch (Exception e)
        {
            log.info("OneNoteSession has already been terminated.");
        }

        
        try {
            DesktopSession.close();
            DesktopSession.quit();
        } catch (Exception e)
        {
            log.info("DesktopSession has already been terminated.");
        }
        
        
        try {
            PrintQueueSession.close();
            PrintQueueSession.quit();
        } catch (Exception e)
        {
            log.info("PrintQueueSession has already been terminated.");
        }
        

      //Stop PrintTrace log capturing.
        PrintTraceCapture.StopLogCollection(currentClass);  
    }
      
}

*/