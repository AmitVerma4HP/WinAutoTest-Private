package com.hp.win.tests;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.SessionId;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;

import io.appium.java_client.windows.WindowsElement;


public class PrintMsWord extends Base{
	private static final Logger log = LogManager.getLogger(PrintMsWord.class);
	

    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename"})
    public static void setup(String device_name, String ptr_name, @Optional("MicrosoftWord2016_Portrait_MultiPage_TestFile.docx")String test_filename) throws InterruptedException, IOException {
        	
    		MsWordSession = Base.OpenMsWordFile(device_name, test_filename);    		
            Thread.sleep(1000); 
    }

	
	@Test
	@Parameters({"ptr_name","device_name"})
    public void PrintMsWordFile(String ptr_name ,String device_name) throws InterruptedException, IOException
    {   
		
		MsWordSession.getKeyboard().pressKey(Keys.CONTROL+"p");
		log.info("Pressed CTRL+P to get to Print Option");
		Thread.sleep(2000); 
		
		WebElement PrinterListComboBox = MsWordSession.findElementByClassName("NetUIDropdownAnchor");
        Assert.assertNotNull(PrinterListComboBox);   
        log.info("Currently selected printer is => " +PrinterListComboBox.getText().toString());
                
       
        
        
        /*
        if(PrinterListComboBox.getText().contentEquals(ptr_name)) {
        	log.info("Desired printer =>" +ptr_name+" is already selected");
        }else {
        PrinterListComboBox.findElementByName(ptr_name).click();
        log.info("Selected desired printer "+ptr_name+"from printers list dropdown");
        */
		
		
	}
	
	

	
	@Test
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
		
		// Method to attach session to Printer Queue Window
		//SwitchToPrinterQueue(device_name,ptr_name);
		
	    //Take out .txt from file name for validation in Assert.
	   // test_filename = test_filename.substring(0, test_filename.lastIndexOf('.'));
	    
	   // log.info("Expected queued job should be => "+test_filename);
	    //Validate Print Job Queued up
	    //Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
	   // log.info("Found correct job in print queue => "+test_filename);
	    
	}

    
    @AfterClass
    public static void TearDown()
    {	        
    
        	if (MsWordSession!= null)
        	{
        		MsWordSession.quit();
        	}
        	
    		if(DesktopSession!=null)
    		{
    			DesktopSession.quit();
    		}
    		
    		if(MsWordFirstSession!=null)
    		{
    			MsWordFirstSession.quit();
    		}
        	        
    }
    
    
  
}

