package com.hp.win.tests;


import java.io.IOException;
import java.net.MalformedURLException;
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
import com.hp.win.utility.ScreenshotUtility;

@Listeners({ScreenshotUtility.class})
public class AddPrinterInSetting extends SettingBase {
	private static final Logger log = LogManager.getLogger(AddPrinterInSetting.class);
	static WebDriverWait wait;
	
		
		@BeforeClass
		@Parameters({"device_name"})
	    public static void setup(String device_name) throws MalformedURLException, InterruptedException {
	        SettingBase.OpenSettings(device_name);	    	    
	    }
		
			
		// Method to Add Printer (if not already added) Under Test
		@Test
		@Parameters({"ptr_name","device_name"})
	    public void Add_Printer(String ptr_name,String device_name) throws InterruptedException, IOException
	    {   			
			// Method to discover target printer - checks if already added then remove, confirm remove then add , confirm Add
			SettingBase.DiscoverRemoveAddPrinter(ptr_name,device_name);
	    }
			
			
			
		@AfterClass(alwaysRun=true)
		public static void TearDown()
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
		        	        
		}
		
}
