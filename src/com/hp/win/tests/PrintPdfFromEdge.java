package com.hp.win.tests;


import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.EdgeAppBase;
import com.hp.win.core.UwpAppBase;
import com.hp.win.core.UwpAppBase;
import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hp.win.utility.*;							

import java.io.IOException;

	@Listeners({ScreenshotUtility.class})
	public class PrintPdfFromEdge extends UwpAppBase {
		private static final Logger log = LogManager.getLogger(PrintPdfFromEdge.class);
		private static String currentClass;	
		public static RemoteWebDriver MsEdgeSession = null;
		public static String expectedPrintjob;
		
		@BeforeClass
		@Parameters({ "device_name", "ptr_name", "test_filename"})
	    public static void setup(String device_name, String ptr_name, String test_filename) throws InterruptedException, IOException { 
	       
		currentClass = PrintPdfFromEdge.class.getSimpleName();
				
		//Start PrintTrace log capturing 
		PrintTraceCapture.StartLogCollection(currentClass);											   
	    MsEdgeSession = EdgeAppBase.OpenPdfEdgeApp(device_name, test_filename);
	    Thread.sleep(2000);
	  
	    }
	
		
	@Test
	@Parameters({ "ptr_name", "test_filename","copies","page_range","orientation","paper_size","page_margins","color_optn","duplex_optn","borderless","paper_tray","paper_type","output_qlty","stapling_optn","headerandfooter_optn","scale_optn","collation_optn"})
    public void PrintEdge(String ptr_name, String test_filename, @Optional("1")String copies,@Optional("All pages")String page_range, @Optional("Portrait")String orientation, @Optional("Letter")String paper_size, @Optional("Normal")String page_margins,  @Optional("Color")String color_optn,  @Optional("None")String duplex_optn,  @Optional("On")String borderless,  @Optional("Auto select")String paper_tray, @Optional("Plain Paper")String paper_type, @Optional("Normal")String output_qlty, @Optional("Staple")String stapling_optn, @Optional("On")String headerandfooter_optn, @Optional("Shrink to fit")String scale_optn, @Optional("Uncollated")String collation_optn) throws InterruptedException, IOException
    {
		
		// Method to Print Web page to Printer Under Test
		EdgeAppBase.PrintEdge(ptr_name, test_filename);
		
		// Method to select the desired printer.
		UwpAppBase.SelectDesiredPrinter(MsEdgeSession, ptr_name);
		
		//Select Desired Orientation Option.
		if (MsEdgeSession.findElementsByXPath("//ComboBox[@Name = 'Orientation']").size()!=0){
			UwpAppBase.SelectOrientation_Uwp(MsEdgeSession,orientation);
		}else{
			log.info("The desired Orientation feature is not supported by the printer ");
		}
		
		//Enter desired Copies value.
		if (MsEdgeSession.findElementsByName("Copies").size()!=0){
			UwpAppBase.SelectCopies_Uwp(MsEdgeSession,copies);
		}else{
			log.info("The desired Copies selection feature is not supported by the printer ");
		}
		
					
		//Select Desired Pages(page range) Option.
		if (MsEdgeSession.findElementsByName("Pages").size()!=0){
			UwpAppBase.SelectPageRange_Uwp(MsEdgeSession,page_range);
		}else{
			log.info("The desired Pages(Page Range) feature is not supported by the printer ");
		}
		
		//Select Desired Scaling Option.
		if (MsEdgeSession.findElementsByName("Scale").size()!=0){
			UwpAppBase.SelectScale_Uwp(MsEdgeSession,scale_optn);
		}else{
			log.info("The desired Scaling feature is not supported by the printer ");
		}
		
		//Select Desired Page Margins Option.
		if (MsEdgeSession.findElementsByName("Margins").size()!=0){
			UwpAppBase.SelectPageMargins_Uwp(MsEdgeSession,page_margins);
		}else{
			log.info("The desired Page Margin feature is not supported by the printer ");
		}
		
		//Select Desired Borderless Printing Option.
		if (MsEdgeSession.findElementsByName("Borderless printing").size()!=0){
			UwpAppBase.SelectBorderless_Uwp(MsEdgeSession,borderless);
		}else{
			log.info("The desired Borderless feature is not supported by the printer ");
		}
		
		//Select Desired Header and Footer Option.
		if (MsEdgeSession.findElementsByName("Headers and footers").size()!=0){
			UwpAppBase.SelectHeadersAndFooters_Uwp(MsEdgeSession,headerandfooter_optn);
		}else{
			log.info("The desired Header and Footer feature is not supported by the printer ");
		}
		
		//Opening more settings to access more printing options.
		int MoreSettings = UwpAppBase.OpenMoreSettings(MsEdgeSession);
		if	(MoreSettings == 1){	
			
			//Select Desired Duplex Option.			
			if (MsEdgeSession.findElementsByName("Duplex printing").size()!=0){
				UwpAppBase.SelectDuplexOption_Uwp(MsEdgeSession,duplex_optn);
			}else{
				log.info("The desired Duplexing feature is not supported by the printer ");
			}
			
			//Select Desired Collation Option.			
			if (MsEdgeSession.findElementsByName("Collation").size()!=0){
				UwpAppBase.SelectCollation_Uwp(MsEdgeSession,collation_optn);
			}else{
				log.info("The desired Collation feature is not supported by the printer ");
			}
			
			//Select Desired Paper Size Option
			if (MsEdgeSession.findElementsByName("Paper size").size()!=0){
				UwpAppBase.SelectPaperSize_Uwp(MsEdgeSession,paper_size);
			}else{
				log.info("The desired Paper Size feature is not supported by the printer ");
			}
						
			//Select Desired Paper Type Option.			
			if (MsEdgeSession.findElementsByName("Paper type").size()!=0){
				UwpAppBase.SelectPaperType_Uwp(MsEdgeSession,paper_type);
			}else{
				log.info("The desired Paper Type feature is not supported by the printer ");
			}
			
			//Select Desired Paper Tray Option.			
			if (MsEdgeSession.findElementsByName("Paper tray").size()!=0){
				UwpAppBase.SelectPaperTray_Uwp(MsEdgeSession,paper_tray);
			}else{
				log.info("The desired Paper Tray feature is not supported by the printer ");
			}
							
			//Select Desired Output Quality Option.			
			if (MsEdgeSession.findElementsByName("Output quality").size()!=0){
				UwpAppBase.SelectOutputQuality_Uwp(MsEdgeSession,output_qlty);
			}else{
				log.info("The desired Output Quality feature is not supported by the printer ");
			}
			
			//Select Desired Color Mode Option.			
			if (MsEdgeSession.findElementsByName("Color mode").size()!=0){
				UwpAppBase.SelectColorOrMono_Uwp(MsEdgeSession,color_optn);
			}else{
				log.info("The desired Color Mode feature is not supported by the printer ");
			}
			
			//Select Desired Stapling Option.			
			if (MsEdgeSession.findElementsByName("Stapling").size()!=0){
				UwpAppBase.SelectStaplingOption_Uwp(MsEdgeSession,stapling_optn);
			}else{
				log.info("The desired Stapling feature is not supported by the printer ");
			}
						
			//Closing more settings after accessing more printing options.
			UwpAppBase.CloseMoreSettings(MsEdgeSession);
			
		}else{
			log.info("More Settings page could not be opened");
			MsEdgeSession.findElementByXPath("//Button[@AutomationId = 'CloseButton']").click();
			Assert.fail();
		}
			
			// Tap on Print button(Give Print)
	    	MsEdgeSession.findElementByXPath("//Button[@AutomationId = 'PrintButton']").click();
	    	log.info("Clicked on final Print button -> Print option Successfully");
		    
	    }
 
		
	@Test(dependsOnMethods = { "PrintEdge" })
	@Parameters({ "device_name", "ptr_name", "test_filename"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String test_filename) throws IOException, InterruptedException 
	{
		// Open Print Queue
		Base.OpenPrintQueue(ptr_name);
				
		// Method to attach session to Printer Queue Window
		Base.SwitchToPrinterQueue(device_name,ptr_name);
		
		log.info("Expected queued job should be => "+test_filename);
	    
		 //Validate Print Job Queued up
	    try {
	    	//Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(test_filename));
	    	Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains("PDF"));
	    }catch(NoSuchElementException e) {
	    	log.info("Expected Print job is not found in print queue");
	     	throw new RuntimeException(e);
	    }catch(Exception e) {
	    	log.info("Error validating print job in print queue");
	    	throw new RuntimeException(e);
	    }
	    
	    //log.info("Found correct job in print queue => "+test_filename);
	    log.info("Found PDF job in print queue");
	    PrintQueueSession.close();
	    log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");	    
	    
	}
    
    @AfterClass(alwaysRun=true)
    public static void TearDown() throws IOException, InterruptedException 
    {	        
    
		if (MsEdgeSession!= null)
		{
			MsEdgeSession.quit();
		}
		
		if (DesktopSession!=null)
		{
			DesktopSession.quit();
		}
		
		if (PrintQueueSession!=null)
		{
			PrintQueueSession.quit();
		}
				
		//Stop PrintTrace log capturing.
		PrintTraceCapture.StopLogCollection(currentClass);					   
    }
}
