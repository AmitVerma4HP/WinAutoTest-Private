package com.hp.win.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

public class PrintFromNotepad {

    private static RemoteWebDriver NotepadSession = null;
    private static RemoteWebDriver DesktopSession = null;
    private static RemoteWebDriver PrintQueueSession = null;
    private static DesiredCapabilities capabilities;
    private static String WindowsApplicationDriverUrl = "http://127.0.0.1:4723/wd/hub";
    
    public static Process process;
	public static InputStream is;
	public static InputStreamReader isr;
	public static BufferedReader br;
	public static String line;
	public static String printerName = null;
	public static String currentWindowHandle = null;
	private Set<String> allWindowHandles =  new HashSet<String>();
	private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);

    @BeforeClass
    public static void setup() {
        try {
    	    capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
            capabilities.setCapability("appArguments", "test1.txt");
            capabilities.setCapability("appWorkingDir", "C:\\SourceFile\\");
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "VERAMIT6");
            NotepadSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
            Assert.assertNotNull(NotepadSession);
            NotepadSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            
            System.out.println("Open test1.txt file");
            
           openPrintQueue("HP ENVY 5530 Series Class Driver");   
         
                
        }catch(Exception e){
            e.printStackTrace();
        } finally {
   }
}

	
	@Test
    public void PrintNoteFile() throws InterruptedException, IOException
    {
    	// Go to file Menu
    	NotepadSession.findElementByName("File").click();
    	System.out.println("Clicked on File Menu Successfully in Notepad");
    	 
    	// Save the file
    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Save\")]").click();
        System.out.println("Pressed Save button to Save the File");
    	
         
        // Go to file Menu
	    NotepadSession.findElementByName("File").click();
	    System.out.println("Clicked on File Menu Successfully in Notepad");
	    	
	    	
    	// Tap on Print
    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Print\")]").click();
    	System.out.println("Clicked on File -> Print option Successfully");
    	
    	//Select WiFi Printer
    	NotepadSession.findElementByName("HP ENVY 5530 Series Class Driver").click();
    	System.out.println("Selected Printer Successfully");
    	
    	//Tap on print icon (Give Print)
    	NotepadSession.findElementByXPath("//Button[starts-with(@Name, \"Print\")]").click();
    	System.out.println("Pressed Print Button Successfully");
	
    	
    	System.out.println("Handles from Notepad Session");
    	//currentWindowHandle = NotepadSession.getWindowHandle();
    	allWindowHandles = NotepadSession.getWindowHandles();
    	for (String handle :allWindowHandles) {
    	    System.out.println("Notepad:" + handle);
    	    System.out.println("\n");
    	}
	}

	
	@Test
	public void ValidatePrintQueue() throws IOException, InterruptedException 
	{
		
		//Validate print icon appears in task bar or check print job in queue
    	// Open notification then print queue then check job name and status
	    
	   // Create session by attaching to PrintQueue top level window
	    capabilities = new DesiredCapabilities();
	    capabilities.setCapability("app","Root");
	    //capabilities.setCapability("appTopLevelWindow",0x3098C);
	    capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", "mannala7");
	    DesktopSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);

	    
	    
	    //WebElement printerQueueWindow = DesktopSession.findElementByClassName("PrintUI_PrinterQueue");
	    WebElement printerQueueWindow = DesktopSession.findElementByName("HP ENVY 5530 Series Class Driver - Offline");
    	String nativeWindowHandle = printerQueueWindow.getAttribute("NativeWindowHandle");
    	int printerQueueWindowHandle = Integer.parseInt(nativeWindowHandle);
    	System.out.println("int value:" + nativeWindowHandle);
    	String printerQueueTopWindowHandle  = ("0x" + Integer.toHexString(printerQueueWindowHandle));
    	System.out.println("Hex Value:" + printerQueueTopWindowHandle);

    	// Create a new session by attaching to an existing application top level window
    	DesiredCapabilities capabilities = new DesiredCapabilities();
    	//capabilities.setCapability("app", "C:\\Windows\\System32\\printui.dll");
    	capabilities.setCapability("appTopLevelWindow", printerQueueTopWindowHandle);
    	capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", "mannala7");
        PrintQueueSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
    	System.out.println("Queue session created: " + PrintQueueSession );
    	
    	// Ensure you have Correct Print Queue opened
    	//Assert.assertNotNull(PrintQueueSession.findElementByXPath("//TitleBar//[)
    	
    	Assert.assertTrue(PrintQueueSession.findElementByClassName("PrintUI_PrinterQueue").getAttribute("Name").contains("HP ENVY 5530 Series Class Driver - Offline"));
	    System.out.println("Double checked that we opened correct printer queue => ");
	    		    
	    //Validate Job Name
	   // Assert.assertTrue(DesktopSession.findElementByXPath("SysListView32//[starts-with(@Name, \"test1.txt - Notepad\")]").getText().contains("test1 - Notepad"),"Expected Print Job NOT FOUND in Print Queue");
	    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains("test1.txt - Notepad"));	
	    
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
    
    
    public static void openPrintQueue(String printerName) throws IOException {
		
		try {		
			process = new ProcessBuilder("rundll32.exe","printui.dll","PrintUIEntry","/o","/n",printerName).start();
			PrintFromNotepad.startBuilder(process);
			System.out.println("Opened printer queue => " +printerName);
			
		} catch (Exception e) {
			System.out.println("Error Occured while getting device property");
			e.printStackTrace();

		}

	}
    
	// Method to start the process builder
	public static BufferedReader startBuilder(Object process) throws IOException {
		is = ((Process) process).getInputStream();
		isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
		return br;

	}
}

