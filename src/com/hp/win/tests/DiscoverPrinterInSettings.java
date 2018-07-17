package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.hp.win.core.SettingBase;
import com.hp.win.utility.GetWindowsBuild;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;

@Listeners({ScreenshotUtility.class})
public class DiscoverPrinterInSettings extends SettingBase {
	private static final Logger log = LogManager.getLogger(DiscoverPrinterInSettings.class);
	static WebDriverWait wait;
	private static String currentClass;
	
		
		@BeforeClass
		@Parameters({"device_name"})
	    public static void setup(String device_name) throws InterruptedException, Throwable {
			currentClass = DiscoverPrinterInSettings.class.getSimpleName();
			
			//Start PrintTrace log capturing 
	    	PrintTraceCapture.StartLogCollection(currentClass);
	    	
	    	//Get windows build info
	    	GetWindowsBuild.GetWindowsBuildInfo();
	    	GetWindowsBuild.PrintWindowsBuildInfo();
	    	
	        SettingBase.OpenSettings(device_name);
	    	    
	    }
		
		
		// Method to Discover Printer Under Test		
		@Test
		@Parameters({"ptr_name","device_name"})
	    public void Discover_Printer(String ptr_name,String device_name) throws InterruptedException, IOException
	    {   
			//If printer is already added then remove it and go for discovery
			
			SettingBase.DiscoverRemoveDiscoverPrinter(ptr_name,device_name);
								
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
