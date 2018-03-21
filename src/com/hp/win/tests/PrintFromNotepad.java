package com.hp.win.tests;


import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.hp.win.core.Base;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

public class PrintFromNotepad {

    private static RemoteWebDriver NotepadSession = null;
    private static RemoteWebDriver DesktopSession = null;
    private static RemoteWebDriver PrintQueueSession = null;
    private static DesiredCapabilities capabilities;
    private static String WindowsApplicationDriverUrl = "http://127.0.0.1:4723/wd/hub";
    
    public static Process process;
	public static String line;
	public static String printerName = null;
	public static String currentWindowHandle = null;
	private Set<String> allWindowHandles =  new HashSet<String>();
	private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);

    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename"})
    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) {
        try {
        	Path path = FileSystems.getDefault().getPath("test_filename").toAbsolutePath();
        	log.info("Test file complete location =>" +path);
    	    capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
            capabilities.setCapability("appArguments",test_filename );
            capabilities.setCapability("appWorkingDir", path);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
            NotepadSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
            Assert.assertNotNull(NotepadSession);
            NotepadSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);            
            log.info("Opened"+test_filename+"file from "+path);            
            Base.openPrintQueue(ptr_name);                            
            
        	}catch(Exception e){
            e.printStackTrace();
        	} finally {
        }
    }

	
	@Test
	@Parameters({ "device_name", "ptr_name", "test_filename"})
    public void PrintNoteFile(String ptr_name) throws InterruptedException, IOException
    {
    	// Go to file Menu
    	NotepadSession.findElementByName("File").click();
    	log.info("Clicked on File Menu Successfully in Notepad");
    	 
    	// Save the file
    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Save\")]").click();
        log.info("Pressed Save button to Save the File");
    	
         
        // Go to file Menu
	    NotepadSession.findElementByName("File").click();
	    log.info("Clicked on File Menu Successfully in Notepad");
	    	
	    	
    	// Tap on Print
    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Print\")]").click();
    	log.info("Clicked on File -> Print option Successfully");
    	
    	//Select WiFi Printer
    	NotepadSession.findElementByName(ptr_name).click();
    	log.info("Selected Printer Successfully");
    	
    	//Tap on print icon (Give Print)
    	NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Print\")]").click();
    	log.info("Pressed Print Button Successfully");
	
    	
    	log.debug("Handles from Notepad Session");
    	//currentWindowHandle = NotepadSession.getWindowHandle();
    	allWindowHandles = NotepadSession.getWindowHandles();
    	for (String handle :allWindowHandles) {
    	    log.debug("Notepad:" + handle);
    	    log.debug("\n");
    	}
	}
	
	

	
	@Test
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
		
		    	  
	    // Create Desktop session 
	    capabilities = new DesiredCapabilities();
	    capabilities.setCapability("app","Root");
	    capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", "mannala7");
	    DesktopSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);

	    
	    //Get handle to PrinterQueue window
	    WebElement printerQueueWindow = DesktopSession.findElementByName(ptr_name);
    	String nativeWindowHandle = printerQueueWindow.getAttribute("NativeWindowHandle");
    	int printerQueueWindowHandle = Integer.parseInt(nativeWindowHandle);
    	log.debug("int value:" + nativeWindowHandle);
    	String printerQueueTopWindowHandle  = ("0x" + Integer.toHexString(printerQueueWindowHandle));
    	log.debug("Hex Value:" + printerQueueTopWindowHandle);

    	// Create a PrintQueueSession by attaching to an existing application top level window handle
    	DesiredCapabilities capabilities = new DesiredCapabilities();
    	capabilities.setCapability("appTopLevelWindow", printerQueueTopWindowHandle);
    	capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", device_name);
        PrintQueueSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
    	log.info("PrintQueue session created successfully " + PrintQueueSession );
    	
    	// Ensure correct PrintQueue is opened    	
    	Assert.assertTrue(PrintQueueSession.findElementByClassName("PrintUI_PrinterQueue").getAttribute("Name").contains(ptr_name));
	    log.info("Open printer queue is correct for the printer => "+ptr_name);
	    		    
	    //Validate Print Job Queued up
	    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));	
	    
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
        	        
    }
    
    
  
}

