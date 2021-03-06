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


package com.hp.win.tests;


import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.NotepadBase;
import com.hp.win.utility.GetWindowsBuild;
import com.hp.win.utility.PrintTraceCapture;
import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;


@Listeners({ScreenshotUtility.class})

public class PrintFromNotepad extends NotepadBase{
	private static final Logger log = LogManager.getLogger(PrintFromNotepad.class);
	private static String currentClass;	


    
    @BeforeClass
	@Parameters({ "device_name", "ptr_name", "test_filename" })
    public static void setup(String device_name, String ptr_name, @Optional("NotepadTestFile1.txt")String test_filename) throws InterruptedException, IOException {

		currentClass = PrintFromNotepad.class.getSimpleName();
	
		//Start PrintTrace log capturing 
		PrintTraceCapture.StartLogCollection(currentClass);	
		
		//Get windows build info
    	GetWindowsBuild.GetWindowsBuildInfo();
    	GetWindowsBuild.PrintWindowsBuildInfo();
    	
		NotepadSession = NotepadBase.OpenNoteFile(device_name, test_filename);
       
    	Thread.sleep(1000);                         
                   	
    }

	
	@Test
	@Parameters({ "ptr_name", "orientation", "duplex_optn", "color_optn", "prnt_quality", "paper_size", "device_name" })
    public void PrintNoteFile(String ptr_name, @Optional("Portrait")String orientation, @Optional("None")String duplex_optn, @Optional("Color")String color_optn, @Optional("Draft")String prnt_quality, @Optional("Letter")String paper_size, String device_name) throws InterruptedException, IOException
    {   
		// Method to Print Notepad File to Printer Under Test
		PrintNotePadFile(ptr_name, orientation, duplex_optn, color_optn, prnt_quality, paper_size, device_name);
	}
	
	
	@Test(dependsOnMethods = { "PrintNoteFile" })
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
	    // Method to open the print queue (Moved from setup() method)
	    Base.OpenPrintQueue(ptr_name);
	    
		// Method to attach session to Printer Queue Window
		Base.SwitchToPrinterQueue(device_name,ptr_name);
		
	    //Take out .txt from file name for validation in Assert.
	    test_filename = test_filename.substring(0, test_filename.lastIndexOf('.'));
	    
	    log.info("Expected queued job should be => "+test_filename);

	    //Validate Print Job Queued up
	    Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
	    log.info("Found correct job in print queue => "+test_filename);
	    
	    
	    //Waiting for the job to get spooled completely before closing the print queue window.
	    wait = new WebDriverWait(PrintQueueSession,60);
	    wait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("//ListItem[@AutomationId='ListViewItem-1']"),"Spooling"));
	    log.info("Waiting until the job spooling is completed");
	    
	    PrintQueueSession.close();
	    log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");	    
	    
	    
	    // Get the Notepad handle so that we can switch back to the app and close it
        try {
            
            DesktopSession = Base.GetDesktopSession(device_name);
            
            //Get handle to Notepad window
            WebElement notepadWindow = DesktopSession.findElementByClassName("Notepad");
            String nativeWindowHandle = notepadWindow.getAttribute("NativeWindowHandle");
            int notepadWindowHandle = Integer.parseInt(nativeWindowHandle);
            log.debug("int value:" + nativeWindowHandle);
            String notepadTopWindowHandle  = hex.concat(Integer.toHexString(notepadWindowHandle));
            log.debug("Hex Value:" + notepadTopWindowHandle);

            // Create a NotepadSession by attaching to an existing application top level window handle
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("appTopLevelWindow", notepadTopWindowHandle);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", device_name);
            NotepadSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
            }catch(Exception e){
                e.printStackTrace();
                log.info("Error getting Notepad session to close notepage testfile");
                // Dont have to throw runtime exception just because we could not close notepad testfile because it will terminate the testcases
                //throw new RuntimeException(e);
                }
        log.info("Notepad session created successfully");
	    
	}

    

	@AfterClass(alwaysRun=true)
	public static void TearDown() throws NoSuchSessionException, IOException, InterruptedException
	{	        

	    try {
	        NotepadSession.close();
	        NotepadSession.quit();
	    } catch (Exception e)
	    {
	        log.info("NotepadSession has already been terminated.");
	    }

	    try {
	        DesktopSession.close();
	        DesktopSession.quit();
	    } catch (Exception e)
	    {
	        log.info("DesktopSession has already been terminated.");
	    }

	    try {
	        PrintQueueSession.close();
	        PrintQueueSession.quit();
	    } catch (Exception e)
	    {
	        log.info("PrintQueueSession has already been terminated.");
	    }

	    //Stop PrintTrace log capturing.
	    PrintTraceCapture.StopLogCollection(currentClass);	

	}

      
}

