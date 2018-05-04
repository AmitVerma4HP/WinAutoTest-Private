package com.hp.win.tests;


import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.hp.win.core.SettingBase;
import com.hp.win.utility.ScreenshotUtility;

@Listeners({ScreenshotUtility.class})
public class AddDiscoveredPrinterInSetting extends SettingBase {
	private static final Logger log = LogManager.getLogger(AddDiscoveredPrinterInSetting.class);
	static WebDriverWait wait;
	
		
		@BeforeClass
		@Parameters({"device_name"})
	    public static void setup(String device_name) throws MalformedURLException, InterruptedException {
	        
	    	    CortanaSession=SettingBase.GetCortanaSession(device_name);	    	    
	    	    Thread.sleep(3000);
	    	    try {
	            CortanaSession.findElementByName("Search box").sendKeys("Printers & scanners");
	            log.info("Searching \"Printers & scanners\"");
	            	            
	            CortanaSession.findElementByName("Printers & scanners, System settings").click();
	            log.info("Clicked on \"Printers & scanners\"");
	    	    } catch(Exception e){
	    	    	e.printStackTrace();
	    	    	log.info("Error getting to Settings -> \"Printers & scanner\"");
	        	}
	    	    Thread.sleep(1000);
	    	    
	    	    SettingSession=SettingBase.GetSettingSession(device_name);
	    	    wait = new WebDriverWait(SettingSession, 60);
	    	    wait.until(ExpectedConditions.elementToBeClickable(By.name("Add a printer or scanner")));
	    	    log.info("Waited until \"Printers & scanner\" is clickable");
	    	    
	    }
		
		
		// Method to Discover Printer Under Test		
		@Test
		@Parameters({"ptr_name"})
	    public void Discover_Printer(String ptr_name) throws InterruptedException, IOException
	    {   
			SettingBase.DiscoverPrinter(ptr_name);
								
	    }
		
		
		// Method to Add Printer (if not already added) Under Test
		@Test(dependsOnMethods={"DiscoverPrinter"})
		@Parameters({"ptr_name"})
	    public void Add_Printer(String ptr_name) throws InterruptedException, IOException
	    {   
			
			SettingBase.AddPrinter(ptr_name);
	    }
			
			
			
		@AfterClass(alwaysRun=true)
		public static void TearDown()
		{	        
		          
		        	
		   if(DesktopSession!=null)
		   {
			   DesktopSession.quit();
		   }
		   
		   if(CortanaSession!=null)
		   {
		   	  CortanaSession.quit();
		   }
		   
		   if(SettingSession!=null)
		   {
			  SettingSession.close();
			  SettingSession.quit();
		   }
		        	        
		}
		
}
