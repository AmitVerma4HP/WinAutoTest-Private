/*
Copyright 2018 Mopria Alliance, Inc.
Copyright 2018 HP Development Company, L.P.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package com.hp.win.utility;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
public class DiscoverPrinterListingUtility extends SettingBase {
	private static final Logger log = LogManager.getLogger(DiscoverPrinterListingUtility.class);
			
	@BeforeClass
	@Parameters({"device_name"})
	public static void setup(String device_name) throws InterruptedException, IOException {
		//Get windows build info
		GetWindowsBuild.GetWindowsBuildInfo();
		GetWindowsBuild.PrintWindowsBuildInfo();
		
		SettingBase.OpenSettings(device_name);
  	    
    }
		
		
	// Method to Discover Printer Under Test		
	@Test
	@Parameters({"device_name"})
    public void Discover_Printer_Utility(String device_name) throws InterruptedException, IOException
    {   
		
		String PrinterListFile = System.getProperty("user.dir") + "\\resources\\" + "PrinterListOnYourNetwork.txt";
		File file = new File(PrinterListFile);
		FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
		
        List<WebElement> AlreadyAddedPrinterListItem = SettingSession.findElementsByClassName("ListViewItem");
		Assert.assertNotNull(AlreadyAddedPrinterListItem);		
		
		bw.append("*****************************************");
		bw.newLine();
		bw.append("********** Added Printers List **********");
		bw.newLine();
		bw.append("*****************************************");
		bw.newLine();
		int addedCount = 0;
		for(WebElement el : AlreadyAddedPrinterListItem) {
			if((el.getText().contains("Bluetooth & other devices")) || (el.getText().contains("Printers & scanners")) || (el.getText().contains("Mouse"))||
					(el.getText().contains("Touchpad")) || (el.getText().contains("Typing")) || (el.getText().contains("Pen & Windows Ink")) ||
					(el.getText().contains("AutoPlay")) || (el.getText().contains("USB")) || (el.getText().contains("Fax")) ||
					(el.getText().contains("Microsoft Print to PDF")) || (el.getText().contains("Microsoft XPS Document Writer")) || (el.getText().contains("Send To OneNote 16"))) {
				log.info("Found element to be removed: "+el.getText());
			}else{
				addedCount++;					
				log.info("Added Printer "+addedCount+" => "+el.getText());
				bw.append(addedCount + ". " + el.getText());
				bw.newLine();
			}
		}
		bw.newLine();
		bw.append("Total Number Of Already Added Printers => "+addedCount);
		bw.newLine();
		bw.append("*****************************************");
		bw.newLine();
		bw.newLine();
		log.info("Total Number Of Already Added Printers => "+addedCount);
	
        
        // Perform discovery - can ptr_name be taken off the Perform Discovery method as its not used.
	    PerformDiscovery();
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
				log.info("Discovered Printer "+ptrCount+" => "+el.getText().split(",")[0]);
			    bw.append(ptrCount + ". " + el.getText().split(",")[0]);
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
	
        log.info("Printer list on your network captured => "+PrinterListFile);
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
