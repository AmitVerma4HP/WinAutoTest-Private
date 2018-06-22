package com.hp.win.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;






public class AcrobatReaderBase extends Base {

    private static final Logger log = LogManager.getLogger(AcrobatReaderBase.class);
	public static RemoteWebDriver AcrobatSession = null;
    public static WebDriverWait wait;
    
    // Method to open testfile with Acrobat Reader App 
    public static RemoteWebDriver OpenAcrobatReader(String device_name, String test_filename, String acrobat_exe_loc) throws InterruptedException {
    	
    	 log.info("Acrobat Reader exe location: " +acrobat_exe_loc);

		   try {
		    	capabilities = new DesiredCapabilities();
		        capabilities.setCapability("app", acrobat_exe_loc);
		        capabilities.setCapability("appArguments",test_filename );
		        capabilities.setCapability("appWorkingDir", testfiles_loc);
		        capabilities.setCapability("platformName", "Windows");
		        capabilities.setCapability("deviceName",device_name);
		        AcrobatSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
		        Assert.assertNotNull(AcrobatSession);
		        AcrobatSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);      			        
		   		}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error opening PDF file from Acrobat");
	            throw new RuntimeException(e);
	        	}
		    log.info("Opened"+test_filename+"file from "+testfiles_loc);
		    
		    Thread.sleep(3000); 
		    		    
			return AcrobatSession; 
    }
   

     // Method to select desired printer from printers list combo box
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectDesiredPrinter_AcrobatPdf(String ptr_name) throws MalformedURLException, InterruptedException {
		 
		 	WebElement PrinterListComboBox = AcrobatSession.findElementByXPath("//ComboBox[@Name ='Printer']");		
	        Assert.assertNotNull(PrinterListComboBox);           
	        if(!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) 
	        {
		        log.info("Desired printer  => "+ptr_name+" <=  is not selected so selecting it from drop down");
		        PrinterListComboBox.click();
		        Thread.sleep(1000);
		        try {
		        	PrinterListComboBox.findElement(By.name(ptr_name)).click();
		        	Thread.sleep(1000);
		        	log.info("Selected desired printer *****" +PrinterListComboBox.getText().toString()+"*****");		        	
		        	}catch(Exception e){
		        	log.info("Printer under test is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting printer under test so moving to next test");     
		            throw new RuntimeException(e);
		        	}		        		        
		    } else {
		    	log.info("Desired printer => " +PrinterListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
		 
	 }

}
