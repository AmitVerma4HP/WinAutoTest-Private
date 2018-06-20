package com.hp.win.core;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
   


}
