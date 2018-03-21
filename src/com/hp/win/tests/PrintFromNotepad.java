package com.hp.win.tests;


import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;


public class PrintFromNotepad extends Base{
	private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);
	

    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename"})
    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) {
        try {
    	    capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
            capabilities.setCapability("appArguments",test_filename );
            capabilities.setCapability("appWorkingDir", testfiles_loc);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
            NotepadSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
            Assert.assertNotNull(NotepadSession);
            NotepadSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);            
            log.info("Opened"+test_filename+"file from "+testfiles_loc); 
            Thread.sleep(1000);
            
            Base.openPrintQueue(ptr_name);                            
            
        	}catch(Exception e){
            e.printStackTrace();
        	} finally {
        }
    }

	
	@Test
	@Parameters({"ptr_name"})
    public void PrintNoteFile(String ptr_name) throws InterruptedException, IOException
    {   
		// Method to Print Notepad File to Printer Under Test
		PrintNotePadFile(ptr_name);
	}
	
	

	
	@Test
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
		
		// Method to attach session to Printer Queue Window
		SwitchToPrinterQueue(device_name,ptr_name);
		
	    //Take out .txt from file name for validation in Assert.
	    test_filename = test_filename.substring(0, test_filename.lastIndexOf('.'));
	    
	    log.info("Expected queued job should be => "+test_filename);
	    //Validate Print Job Queued up
	    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
	    log.info("Found correct job in print queue => "+test_filename);
	    
	}

    
    @AfterClass
    public static void TearDown()
    {	        
    
        	if (NotepadSession!= null)
        	{
        		NotepadSession.quit();
        	}
        	
    		if(DesktopSession!=null)
    		{
    			DesktopSession.quit();
    		}
    		
    		if(PrintQueueSession!=null)
    		{
    		   PrintQueueSession.quit();
    		}
        	        
    }
    
    
  
}

