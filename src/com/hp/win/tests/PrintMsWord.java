package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.MsWordAppBase;
import com.hp.win.utility.GetWindowsBuild;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;


@Listeners({ScreenshotUtility.class})
public class PrintMsWord extends MsWordAppBase{
	private static final Logger log = LogManager.getLogger(PrintMsWord.class);
	private static String currentClass;	
	static WebDriverWait wait;

    @BeforeClass
	@Parameters({"device_name", "ptr_name", "test_filename","word2016_exe_loc"})
    public static void setup(String device_name, String ptr_name, @Optional("MicrosoftWord2016_Portrait_MultiPage_TestFile.docx")String test_filename,String word2016_exe_loc) 
    		throws InterruptedException, IOException {
        	
    	currentClass = PrintMsWord.class.getSimpleName();
		
		//Start PrintTrace log capturing 
    	PrintTraceCapture.StartLogCollection(currentClass);	
    	
    	//Get windows build info
    	GetWindowsBuild.GetWindowsBuildInfo();
    	GetWindowsBuild.PrintWindowsBuildInfo();
    	
    	MsWordSession = MsWordAppBase.OpenMsWordFile(device_name, test_filename,word2016_exe_loc);    		
        Thread.sleep(1000); 
    }

	
	@Test
	@Parameters({"device_name","ptr_name","paper_size","duplex_option","orientation","collation","copies","pages_to_print","page_count","margin","pages_per_sheet"})
    public void PrintMsWordFile(String device_name,String ptr_name, @Optional("Letter")String paper_size,@Optional("Print One Sided")String duplex_option,
    	@Optional("Portrait Orientation")String orientation,@Optional("Collated")String collation,@Optional("1")String copies,
    	@Optional("Print All Pages")String pages_to_print, @Optional("NA")String page_count,@Optional("Normal")String margin,@Optional("1 Page Per Sheet")String pages_per_sheet) throws InterruptedException, IOException    {
		
		MsWordSession.getKeyboard().pressKey(Keys.CONTROL+"p");
		log.info("Pressed CTRL+P to get to Print Option");
		Thread.sleep(1000);
		MsWordSession.getKeyboard().releaseKey(Keys.CONTROL);
		Thread.sleep(2000);		
		
		//Selecting desired printer
		MsWordAppBase.SelectDesiredPrinter_Msword(ptr_name);      
		
		// Write method to select Print settings
		// Select Desired Paper Size
		MsWordAppBase.SelectPaperSize_Msword(paper_size);
		
		// Select Desired Duplex Option
		MsWordAppBase.SelectDuplexOption_Msword(duplex_option);
		
		// Select Desired Orientation Option
		MsWordAppBase.SelectOrientation_Msword(orientation);
		
		// Select Desired Collation Option
		MsWordAppBase.SelectCollation_Msword(collation);
		
		// Enter Desired Copies value
		MsWordAppBase.SelectCopies_Msword(copies);
		
		// Select Desired Pages to Print Value
		MsWordAppBase.SelectPagesToPrint_Msword(pages_to_print, page_count);
		
		
		// Select Desired Print Margin
		MsWordAppBase.SelectMargins_Msword(margin);
		
		// Select Desired Print Margin
		MsWordAppBase.SelectPagesPerSheet_Msword(pages_per_sheet);		
		Thread.sleep(1000);		
		
		//After all print settings give print 
		MsWordSession.findElementByXPath("//Button[@Name ='Print']").click();	
		Thread.sleep(1000);
		log.info("Finally gave a print by clicking on PRINT button");
		
		//Watch for Print Conflict Pop up and message to user that print settings needs to be changed and don't proceed with Print
		
		try {
			if(MsWordSession.findElementByXPath("//*[contains(@Name,'Print Settings Conflict')]").isDisplayed())
			{
				log.info("Found \"Print Settings Conflict\" Dialog so not attempting print. Resolve Conflicting Print Setting and re-Run the test ");
				Thread.sleep(1000);
				MsWordSession.getKeyboard().pressKey(Keys.ESCAPE);
				Thread.sleep(1000);
				//Fail the test and call quit
				Assert.fail();

			}
		}catch(NoSuchElementException e) {
				log.info("There is no \"Print Settings Conflict\" Dialog so continuing test without taking any action");
		}
				
	}
	
	

	
	@Test(dependsOnMethods={"PrintMsWordFile"})
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
		
		//Open Print Queue 
		Base.OpenPrintQueue(ptr_name);        
		 
		Thread.sleep(1000);
		
		// Method to attach session to Printer Queue Window
		SwitchToPrinterQueue(device_name,ptr_name);
		
	    //Take out .txt from file name for validation in Assert.
	    test_filename = test_filename.substring(0, test_filename.lastIndexOf('.'));	    
	    log.info("Expected queued job should be => "+test_filename);
	    
	    //Validate Print Job Queued up
	    try {
	    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));	    
	    }catch(NoSuchElementException e) {
	    	log.info("Expected Print job is not found in print queue");	 
	    	throw new RuntimeException(e);
	    }catch(Exception e) {
	    	log.info("Error validating print job in print queue");
	    	throw new RuntimeException(e);
	    }
	    
	    log.info("Found correct job in print queue => "+test_filename);
	    
        //Waiting for the job to get spooled completely before closing the print queue window.
	    wait = new WebDriverWait(PrintQueueSession,60);
	    wait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("//ListItem[@AutomationId='ListViewItem-1']"),"Spooling"));
	    log.info("Waiting until the job spooling is completed");

	    
        PrintQueueSession.close();
	    log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");	    
	    
	}


	
    @AfterClass(alwaysRun=true)
    public static void TearDown() throws IOException, InterruptedException
    {  
    	
			if(DesktopSession!=null)
			{
				DesktopSession.quit();
			}
    
			if(PrintQueueSession!=null)
			{
				PrintQueueSession.quit();
			}
			
//			if(MsWordFirstSession!=null)
//			{
//				MsWordFirstSession.quit();
//			}
			
        	if (MsWordSession!= null)
        	{      	       		
        		MsWordSession.close();
        		
        		try {
        			if(MsWordSession.findElementByClassName("NetUINetUIDialog").isDisplayed())
					{
        				log.info("Found alert dialog to save test file");
						MsWordSession.findElementByName("Don't Save").click();
					}
        		//this could cause either NoSuchElementException or NoSuchWindowException so better use Exception object
        		}catch(Exception e) {
        				log.info("There is no alert dialog to save test file so continuing test without taking any action");
        		}
        		MsWordSession.quit();
        		
        	}
      			        	
        	//Stop PrintTrace log capturing.
        	PrintTraceCapture.StopLogCollection(currentClass);	
        	        
    }
    
    
  
}

