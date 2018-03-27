package com.hp.win.tests;


import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.hp.win.core.Base;


public class DiscoverAddPrinter extends Base {
	private static final Logger log = LogManager.getLogger(DiscoverAddPrinter.class);
		
		@BeforeClass
		@Parameters({"device_name"})
	    public static void setup(String device_name) throws MalformedURLException {
	        
	    	    CortanaSession=Base.GetCortanaSession(device_name);
	    	    try {
	            CortanaSession.findElementByName("Search box").sendKeys("Printers & scanners");
	            log.info("Searching \"Printers & scanners\"");
	            CortanaSession.getKeyboard().sendKeys(Keys.RETURN);	    	    
	    	    } catch(Exception e){
	    	    	e.printStackTrace();
	    	    	log.info("Error getting to Settings -> \"Printers & scanner\"");
	        	} 
	    }
		
		
		// Method to Discover Printer Under Test		
		@Test
		@Parameters({"ptr_name"})
	    public void DiscoverPrinter(@Optional("Dummy")String ptr_name) throws InterruptedException, IOException
	    {   
			// TDB
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
