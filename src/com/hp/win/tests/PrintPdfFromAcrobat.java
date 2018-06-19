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
import com.hp.win.core.MsWordAppBase;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;


@Listeners({ScreenshotUtility.class})
public class PrintPdfFromAcrobat extends AcrobatReaderBase{
	private static final Logger log = LogManager.getLogger(PrintPdfFromAcrobat.class);
	private static String currentClass;	

    @BeforeClass
	@Parameters({"device_name", "ptr_name", "test_filename","word2016_exe_loc"})
    public static void setup(String device_name, String ptr_name, @Optional("MicrosoftWord2016_Portrait_MultiPage_TestFile.docx")String test_filename,String word2016_exe_loc) 
    		throws InterruptedException, IOException {
        	
    	currentClass = PrintPdfFromAcrobat.class.getSimpleName();
		
		//Start PrintTrace log capturing 
    	PrintTraceCapture.StartLogCollection(currentClass);	
   		AcrobatSession = AcrobatReaderBase.OpenAcrobatReader(device_name, test_filename,word2016_exe_loc);    		
        Thread.sleep(1000); 
    }

	
	@Test
	@Parameters({"device_name","ptr_name","paper_size","duplex_option","orientation","collation","copies","pages_to_print","page_count","margin","pages_per_sheet"})
    public void PrintMsWordFile(String device_name,String ptr_name, @Optional("Letter")String paper_size,@Optional("Print One Sided")String duplex_option,
    	@Optional("Portrait Orientation")String orientation,@Optional("Collated")String collation,@Optional("1")String copies,
    	@Optional("Print All Pages")String pages_to_print, @Optional("NA")String page_count,@Optional("Normal")String margin,@Optional("1 Page Per Sheet")String pages_per_sheet) throws InterruptedException, IOException    {
		
		AcrobatSession.getKeyboard().pressKey(Keys.CONTROL+"p");
		log.info("Pressed CTRL+P to get to Print Option");
		Thread.sleep(1000);
		AcrobatSession.getKeyboard().releaseKey(Keys.CONTROL);
		Thread.sleep(2000);		
		
	
				
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

