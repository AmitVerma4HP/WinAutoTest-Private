package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.*;

import com.hp.win.core.AcrobatReaderBase;
import com.hp.win.core.Base;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;


@Listeners({ScreenshotUtility.class})
public class PrintPdfFromAcrobat extends AcrobatReaderBase{
	private static final Logger log = LogManager.getLogger(PrintPdfFromAcrobat.class);
	private static String currentClass;	

    @BeforeClass
	@Parameters({"device_name", "ptr_name", "test_filename","acrobat_exe_loc"})
    public static void setup(String device_name, String ptr_name, @Optional("4Pages-ISO_IEC_24735_2009_Test_Pages.pdf")String test_filename,String acrobat_exe_loc) 
    		throws InterruptedException, IOException {
        	
    	currentClass = PrintPdfFromAcrobat.class.getSimpleName();
		
		//Start PrintTrace log capturing 
    	PrintTraceCapture.StartLogCollection(currentClass);	
   		AcrobatSession = AcrobatReaderBase.OpenAcrobatReader(device_name, test_filename,acrobat_exe_loc);    		
        Thread.sleep(1000); 
    }

	
	@Test
	@Parameters({"device_name","ptr_name"})
    public void PrintPdfFile(String device_name,String ptr_name) throws InterruptedException, IOException    {
		
		AcrobatSession.getKeyboard().pressKey(Keys.CONTROL+"p");
		log.info("Pressed CTRL+P to get to Print Option");
		Thread.sleep(1000);
		AcrobatSession.getKeyboard().releaseKey(Keys.CONTROL);
		Thread.sleep(2000);	
		
		// Select Desired Printer from ComboBox
		PrintPdfFromAcrobat.SelectDesiredPrinter_AcrobatPdf(ptr_name);
		
		//After all print settings give print 
		AcrobatSession.findElementByXPath("//Button[@Name ='Print']").click();	
		Thread.sleep(1000);
		log.info("Finally gave a print by clicking on PRINT button");	
	
				
	}
	
	
	@Test(dependsOnMethods={"PrintPdfFile"})
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
			

			
        	if (AcrobatSession!= null)
        	{       		
        		AcrobatSession.quit();        		
        	}
      			        	
        	//Stop PrintTrace log capturing.
        	PrintTraceCapture.StopLogCollection(currentClass);	
        	        
    }
    
    
  
}

