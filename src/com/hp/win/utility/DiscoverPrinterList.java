package com.hp.win.utility;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.hp.win.core.SettingBase;
import com.hp.win.utility.ScreenshotUtility;

@Listeners({ScreenshotUtility.class})
public class DiscoverPrinterList extends SettingBase {
	private static final Logger log = LogManager.getLogger(DiscoverPrinterList.class);
			
	@BeforeClass
	@Parameters({"device_name"})
	public static void setup(String device_name) throws MalformedURLException, InterruptedException {
	SettingBase.OpenSettings(device_name);
	    	    
    }
		
		
	// Method to Discover Printer Under Test		
	@Test
	@Parameters({"ptr_name","device_name"})
    public void Discover_Printer_Utility(String ptr_name,String device_name) throws InterruptedException, IOException
    {   
		
		String PrinterListFile = System.getProperty("user.dir") + "\\resources\\" + "\\DiscoverPrinterList.txt\\";
		File file = new File(PrinterListFile);
		FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
		
        List<WebElement> AlreadyAddedPrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
		Assert.assertNotNull(AlreadyAddedPrinterListItem);					
		int addedCount = 0;
		
		bw.append("*****************************************");
		bw.newLine();
		bw.append("********** Added Printers List **********");
		bw.newLine();
		bw.append("*****************************************");
		bw.newLine();
		for(WebElement el : AlreadyAddedPrinterListItem) {
			addedCount++;					
			log.info("Added Printer "+addedCount+" => "+el.getText());
			bw.append(addedCount + ". " + el.getText());
			bw.newLine();

		}
		bw.newLine();
		bw.append("Total Number Of Already Added Printers => "+addedCount);
		bw.newLine();
		bw.append("*****************************************");
		bw.newLine();
		bw.newLine();
		log.info("Total Number Of Already Added Printers => "+addedCount);
	
        
        // Perform discovery - can ptr_name be taken off the Perform Discovery method as its not used.
	    PerformDiscovery(ptr_name,device_name);
        try{
		    List<WebElement> DiscoveredPrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
			Assert.assertNotNull(DiscoveredPrinterListItem);	
			
			@SuppressWarnings("unused")
			boolean uniqueList = DiscoveredPrinterListItem.removeAll(AlreadyAddedPrinterListItem);
				         			
			int ptrCount = 0;
			bw.append("**********************************************");
			bw.newLine();
			bw.append("********** Discovered Printers List **********");
			bw.newLine();
			bw.append("**********************************************");
			bw.newLine();
			for(WebElement el : DiscoveredPrinterListItem) {
				ptrCount++;					
				log.info("Discovered Printer "+ptrCount+" => "+el.getText());
			    bw.append(ptrCount + ". " + el.getText());
			    bw.newLine();

			}
			bw.newLine();
			bw.append("Total Number Of Printers Discovered => "+ptrCount);
			bw.newLine();
			bw.append("*****************************************");
			bw.newLine();
			bw.close();
	        writer.close();
	        log.info("Total Number Of Printers Discovered => "+ptrCount);
		}catch (Exception e) {
		    e.printStackTrace();
		}
	
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
