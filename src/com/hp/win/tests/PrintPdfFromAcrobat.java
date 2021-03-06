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


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.AcrobatReaderBase;
import com.hp.win.core.Base;
import com.hp.win.utility.GetWindowsBuild;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;


@Listeners({ScreenshotUtility.class})
public class PrintPdfFromAcrobat extends AcrobatReaderBase{
	private static final Logger log = LogManager.getLogger(PrintPdfFromAcrobat.class);
	private static String currentClass;	

    @BeforeClass
	@Parameters({"device_name", "ptr_name", "test_filename","acrobat_exe_loc"})
    public static void setup(String device_name, String ptr_name,String test_filename,String acrobat_exe_loc) 
    		throws InterruptedException, IOException {
        	
    	currentClass = PrintPdfFromAcrobat.class.getSimpleName();
		
		//Start PrintTrace log capturing 
    	PrintTraceCapture.StartLogCollection(currentClass);	
    	
    	//Get windows build info
    	GetWindowsBuild.GetWindowsBuildInfo();
    	GetWindowsBuild.PrintWindowsBuildInfo();
    	
   		acrobatSession = AcrobatReaderBase.OpenAcrobatReader(device_name, test_filename,acrobat_exe_loc);    		
        Thread.sleep(1000);
        
    }

	
	@Test
	@Parameters({"device_name","ptr_name","copies", "page_count","duplex","orientation","color","scale","paper_size"})
    public void PrintPdfAcrobat(String device_name,String ptr_name, @Optional("1")String copies,@Optional("All")String page_count,
    		@Optional("None")String duplex,@Optional("Auto")String orientation,@Optional("Color")String color,
    		@Optional("Actual size")String scale,@Optional("Letter")String paper_size) throws InterruptedException, IOException    {
		
		acrobatSession.getKeyboard().pressKey(Keys.CONTROL+"p");
		log.info("Pressed CTRL+P to get to Print Option");
		Thread.sleep(1000);
		acrobatSession.getKeyboard().releaseKey(Keys.CONTROL+"p");
		Thread.sleep(2000);	
				
		//Select Desired Printer from ComboBox
		AcrobatReaderBase.SelectDesiredPrinter_AcrobatPdf(ptr_name,acrobatSession,device_name);
		
		//Select Paper Size Option		
		AcrobatReaderBase.SelectPaperSize_Acrobat(acrobatSession, paper_size,device_name);	
		
		//Enter Desired Copies Value		
		AcrobatReaderBase.SelectCopies_Acrobat(acrobatSession, copies);
		
		
		//Select Desired Page Count Value		
		AcrobatReaderBase.SelectPageCount_Acrobat(acrobatSession, page_count);
		
		//Select Desired Duplex Option		
		AcrobatReaderBase.SelectDuplex_Acrobat(acrobatSession, duplex);
		
		//Select Desired Orientation		
		AcrobatReaderBase.SelectOrientation_Acrobat(acrobatSession, orientation);
		
		//Select Desired Color Option		
		AcrobatReaderBase.SelectColor_Acrobat(acrobatSession, color);
		
		//Select Desired Scale Option		
		AcrobatReaderBase.SelectScale_Acrobat(acrobatSession, scale);			
		
		
		//After all print settings give print 
		acrobatSession.findElementByXPath("//Button[@Name ='Print']").click();	
		Thread.sleep(5000); // Print progress dialog takes more time
		log.info("Finally gave a print by clicking on PRINT button");	
	
				
	}
	
	

	@Test(dependsOnMethods={"PrintPdfAcrobat"})
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
    @Parameters({"device_name"})
    public static void TearDown(String device_name) throws IOException, InterruptedException
    {  
    	
			if(DesktopSession!=null)
			{
				DesktopSession.quit();
			}
    
			if(PrintQueueSession!=null)
			{
				PrintQueueSession.quit();
			}
			
        	if (acrobatSession!= null)
        	{           		
        		try {
        		acrobatSession.close();
        		acrobatSession.quit();
        		}catch(Exception e) {
        			log.info("Error Closing Acrobat Session. Looks like Acrobat Session is already closed");
        		}
        	}
      		
        	if (acrobatAppSession!= null)
        	{           		
        		try {
	        		acrobatAppSession.close();
	        		acrobatAppSession.quit();
        		}catch(Exception e) {
        			log.info("Error Closing Acrobat App Session. Looks like Acrobat App Session is already closed");
        		}
        	}
        	
        	//Stop PrintTrace log capturing.
        	PrintTraceCapture.StopLogCollection(currentClass);        	        
    }
    
    
  
}

