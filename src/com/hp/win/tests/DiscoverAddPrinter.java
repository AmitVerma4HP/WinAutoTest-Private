package com.hp.win.tests;


import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.hp.win.core.SettingBase;
import com.hp.win.utility.ScreenshotUtility;

@Listeners({ScreenshotUtility.class})
public class DiscoverAddPrinter extends SettingBase {
	private static final Logger log = LogManager.getLogger(DiscoverAddPrinter.class);
	static WebDriverWait wait;
	
		
		@BeforeClass
		@Parameters({"device_name"})
	    public static void setup(String device_name) throws MalformedURLException, InterruptedException {
	        
	    	    CortanaSession=SettingBase.GetCortanaSession(device_name);
	    	    wait = new WebDriverWait(CortanaSession, 60);
	    	    Thread.sleep(3000);
	    	    try {
	            CortanaSession.findElementByName("Search box").sendKeys("Printers & scanners");
	            log.info("Searching \"Printers & scanners\"");
	            CortanaSession.getKeyboard().sendKeys(Keys.RETURN);
	            log.info("Pressed ENTER key to open \"Printers & scanners\"");
	    	    } catch(Exception e){
	    	    	e.printStackTrace();
	    	    	log.info("Error getting to Settings -> \"Printers & scanner\"");
	        	}
	    	    Thread.sleep(1000);
	    	    
	    	    //Ensure to release pressed key
	    	    CortanaSession.getKeyboard().releaseKey(Keys.RETURN);
	    	    
	    	    //Switch session to Printers & scanner screen
	    	    //TBD
	    	    //wait.until(ExpectedConditions.elementToBeClickable(By.name("Add a printer or scanner")));
	    	    log.info("Waited until \"Printers & scanner\" show up");
	    	    Thread.sleep(3000);
	    }
		
		
		// Method to Discover Printer Under Test		
		@Test
		@Parameters({"ptr_name"})
	    public void DiscoverPrinter(@Optional("Dummy")String ptr_name) throws InterruptedException, IOException
	    {   
			CortanaSession.findElementByName("Add a printer or scanner").click();
			Thread.sleep(1000);
			log.info("Clicked on \"Add a printer or scanner\" to Search All Printer in the Network");
			Thread.sleep(1000);
			//do {
				log.info("Printer Discovery is in Progress");				
				Thread.sleep(1000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Searching for printers and scanners")));
				log.info("Waiting until discovery is completed");
			//}while(CortanaSession.findElementByName("Searching for printers and scanners").isDisplayed());
				log.info("Printer Discovery completed");	
		}
		
		
		// Method to Add Printer Under Test
		@Test
		@Parameters({"ptr_name"})
	    public void AddPrinter(@Optional("Dummy")String ptr_name) throws InterruptedException, IOException
	    {   
			// TDB
		}

		
		
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
		        	        
		}
		
}
