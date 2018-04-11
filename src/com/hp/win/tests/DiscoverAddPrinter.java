package com.hp.win.tests;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
	    public void DiscoverPrinter(@Optional("ptr_name")String ptr_name) throws InterruptedException, IOException
	    {   
			SettingSession.findElementByName("Add a printer or scanner").click();
			Thread.sleep(1000);
			log.info("Clicked on \"Add a printer or scanner\" to Search All Printer in the Network");
			Thread.sleep(1000);
			
			do {
				log.info("Printer Discovery is in Progress");				
				Thread.sleep(3000);
				
				}while(SettingSession.findElementsByName("Searching for printers and scanners").size()!=0);
				log.info("Printer Discovery completed");
				
				// Store all discovered printer in a List
				
				List<WebElement> PrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
				Assert.assertNotNull(PrinterListItem);
				log.info("Total Printer Discovered => "+PrinterListItem.size());
				int printerFound=0;
				int i = 1; 
				for(WebElement el : PrinterListItem) {
					
					log.info("Printer "+i+" => "+el.getText());
					i++;
														
						if (el.getText().contains(ptr_name)) 
						{
							printerFound = 1;
							break;
						}						
				}
				Assert.assertEquals(printerFound,1,"Didnt find Target Printer => "+ptr_name);
				log.info("Found Target Printer => "+ptr_name);
								
	    }
		
		// Method to Add Printer Under Test
		@Test
		@Parameters({"ptr_name"})
	    public void AddPrinter(String ptr_name) throws InterruptedException, IOException
	    {   
			Thread.sleep(1000);
			SettingSession.findElementByName(ptr_name).click();
			
			//Add printer if printer is not added already
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
		   
		   if(SettingSession!=null)
		   {
			   SettingSession.quit();
		   }
		        	        
		}
		
}
