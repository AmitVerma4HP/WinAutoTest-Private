package com.hp.win.tests;


import org.testng.Assert;
import org.testng.annotations.*;

import com.hp.win.core.Base;
import com.hp.win.core.ChromeAppBase;
import com.hp.win.utility.GetWindowsBuild;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

	@Listeners({ScreenshotUtility.class})
	public class PrintWebPageFromChrome extends ChromeAppBase {
		private static final Logger log = LogManager.getLogger(PrintWebPageFromChrome.class);
		private static String currentClass;	
		public static RemoteWebDriver ChromeSession = null;
		public static String expectedPrintjob;
		static WebDriverWait wait;
		
		@BeforeClass
		@Parameters({ "device_name", "ptr_name", "html_filename", "chrome_exe_loc"})
	    public static void setup(String device_name, String ptr_name, String html_filename, String chrome_exe_loc) throws InterruptedException, IOException { 
	       
			currentClass = PrintWebPageFromChrome.class.getSimpleName();
					
			//Start PrintTrace log capturing 
			PrintTraceCapture.StartLogCollection(currentClass);	
			
			//Get windows build info
	    	GetWindowsBuild.GetWindowsBuildInfo();
	    	GetWindowsBuild.PrintWindowsBuildInfo();
	    	
		    ChromeSession = ChromeAppBase.OpenChromeApp(device_name, html_filename, chrome_exe_loc);
		    Thread.sleep(2000);
		    
			expectedPrintjob = ChromeSession.getTitle().toString();	
		    log.info("Expected Print Job: " + expectedPrintjob);
		    Thread.sleep(1000);	    
	    }
	
		
		@Test
		@Parameters({ "ptr_name", "orientation", "duplex_optn", "color_optn", "prnt_quality", "paper_size", "device_name" })
	    public void PrintFromChrome(String ptr_name, @Optional("Portrait")String orientation, @Optional("None")String duplex_optn, @Optional("Color")String color_optn, @Optional("Draft")String prnt_quality, @Optional("Letter")String paper_size, String device_name) throws InterruptedException, IOException
	    {   
			// Method to Print Web page from Chrome to Printer Under Test
			PrintChromeFile(ptr_name, orientation, duplex_optn, color_optn, prnt_quality, paper_size, device_name);
						
			
		}

		
		@Test(dependsOnMethods = { "PrintFromChrome" })
		@Parameters({ "device_name", "ptr_name", "html_filename"})
		public void ValidatePrintQueue(String device_name, String ptr_name, String html_filename) throws IOException, InterruptedException 
		{
						
			// Open Print Queue
			Base.OpenPrintQueue(ptr_name);
					
			// Method to attach session to Printer Queue Window
			Base.SwitchToPrinterQueue(device_name,ptr_name);
				    
			 //Validate Print Job Queued up
		    try {
		    	String printJob = PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").toString();
		    	Assert.assertTrue(printJob.contains(expectedPrintjob.substring(0, 30).trim()));
		    	log.info("Found expected job in print queue => "+printJob);
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
	        
		    PrintQueueSession.close();
		    log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");	    
		    
		    
	    
		}
    
    @AfterClass(alwaysRun=true)
    @Parameters({ "device_name"})
    public static void TearDown(String device_name) throws IOException, InterruptedException 
    {	
    	// Method to attach session to Chrome Window
    	ChromeAppBase.SwitchToChromeSession(device_name);
    	
    	try{
    		ChromeSession.close();
    		ChromeSession.quit();
	    } catch (Exception e)
	    {
	        log.info("ChromeSession has already been terminated.");
	    }

    	try{
    		ChromeSession1.close();
    		ChromeSession1.quit();
	    } catch (Exception e)
	    {
	        log.info("ChromeSession1 has already been terminated.");
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
