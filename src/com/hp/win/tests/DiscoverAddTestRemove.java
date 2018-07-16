package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.hp.win.core.Base;
import com.hp.win.core.SettingBase;
import com.hp.win.utility.GetWindowsBuild;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;

@Listeners({ScreenshotUtility.class})
public class DiscoverAddTestRemove extends SettingBase {
	private static final Logger log = LogManager.getLogger(DiscoverAddTestRemove.class);
	static WebDriverWait wait;
	private static String currentClass;
	
		
	@BeforeClass
	@Parameters({"device_name"})
    public static void setup(String device_name) throws InterruptedException, Throwable {
        
		currentClass = DiscoverAddTestRemove.class.getSimpleName();
		
		//Start PrintTrace log capturing 
    	PrintTraceCapture.StartLogCollection(currentClass);	
    	
    	//Get windows build info
    	GetWindowsBuild.GetWindowsBuildInfo();
    	GetWindowsBuild.PrintWindowsBuildInfo();
		SettingBase.OpenSettings(device_name);	    	    
    }
	
		
	// Method to Add Printer (if not already added) Under Test
	@Test(priority = 1)
	@Parameters({"ptr_name","device_name"})
    public void Print_TestPage(String ptr_name,String device_name) throws InterruptedException, IOException
    {   
		// Method to Add printer 
	  	SettingBase.DiscoverRemoveAddPrinter(ptr_name,device_name);
	  	SettingSession.close();		  	
		
		// Method to Print test page.
		SettingBase.TestPagePrint(ptr_name,device_name);
				
    }
		
		
	@Test(priority = 2, dependsOnMethods = { "Print_TestPage" })
	@Parameters({ "device_name", "ptr_name"})
	public void ValidatePrintQueue(String device_name, String ptr_name) throws IOException, InterruptedException 
	{
		// Open Print Queue
		Base.OpenPrintQueue(ptr_name);
				
		// Method to attach session to Printer Queue Window
		Base.SwitchToPrinterQueue(device_name,ptr_name);
			    
		//Validate Print Job Queued up
	    try {
	    	String printJob = PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").toString();
	    	Assert.assertTrue(printJob.contains("Test Page"));
	    	log.info("Found expected job in print queue => "+printJob);
	    }catch(NoSuchElementException e) {
	    	PrintQueueSession.close();
	    	log.info("Expected Print job is not found in print queue");
	    	throw new RuntimeException(e);
	    }catch(Exception e) {
           	log.info("Error validating print job in print queue");
	    	throw new RuntimeException(e);
	    }
	    
	   PrintQueueSession.close(); 
	   log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");
	   		    
	}	
	
	@Test(priority = 3, dependsOnMethods = { "Print_TestPage" })
	@Parameters({ "device_name", "ptr_name", "remove_ptr"})
	public void RemovePrinter(String device_name, String ptr_name, String remove_ptr) throws InterruptedException{
	   
	   //Remove printer based on user choice Y/N.
	   if (remove_ptr.equalsIgnoreCase("Y")){
		   RemoveAlreadyAddedPrinter(ptr_name,device_name);
	   }else{
		   log.info("Printer under test not removed as per user Choice => "+ptr_name);
	   } 
	}   
	
	
	@AfterClass(alwaysRun=true)
	public static void TearDown() throws Throwable, InterruptedException
	{	        
	          
		try {
			if(DesktopSession!=null)
			{
				DesktopSession.quit();
			}
	   }catch (NoSuchSessionException e) {
		   log.info("Desktop Session is already terminated");
	   }
			   
	   try {
		   if(CortanaSession!=null)
		   {
			   CortanaSession.quit();
		   }
	   }catch (NoSuchSessionException e) {
		   log.info("Cortana Session is already terminated");
	   }
			
	   try {
		   if(SettingSession!=null)
		   {
			   SettingSession.close();
			   SettingSession.quit();
		   }
	   }catch (NoSuchSessionException e) {
		   log.info("Setting Session is already terminated");
	   }
	   
	   //Stop PrintTrace log capturing.
   	   PrintTraceCapture.StopLogCollection(currentClass);	
		        	        
	}
		
}
