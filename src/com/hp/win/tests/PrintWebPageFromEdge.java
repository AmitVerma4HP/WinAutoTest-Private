package com.hp.win.tests;


import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.EdgeAppBase;
import com.hp.win.utility.ScreenshotUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hp.win.utility.*;							

import java.io.IOException;

	@Listeners({ScreenshotUtility.class})
	public class PrintWebPageFromEdge extends EdgeAppBase {
		private static final Logger log = LogManager.getLogger(PrintWebPageFromEdge.class);
		private static String currentClass;	
		public static RemoteWebDriver MsEdgeSession = null;
		public static String expectedPrintjob;
		static WebDriverWait wait;
		
		@BeforeClass
		@Parameters({ "device_name", "ptr_name", "web_url"})
	    public static void setup(String device_name, String ptr_name, String web_url) throws InterruptedException, IOException { 
	       
		currentClass = PrintWebPageFromEdge.class.getSimpleName();
				
		//Start PrintTrace log capturing 
		PrintTraceCapture.StartLogCollection(currentClass);											   
	    MsEdgeSession = EdgeAppBase.OpenEdgeApp(device_name, web_url);
	    Thread.sleep(2000);
	    
	    String expectedPrintjob = MsEdgeSession.getTitle().toString();	
	    log.info("Expected Print Job: " + expectedPrintjob);
	    }
	
		
	@Test
	@Parameters({ "ptr_name", "web_url","copies","page_range","orientation","paper_size","page_margins","color_optn","duplex_optn","borderless","paper_tray","paper_type","output_qlty","stapling_optn","headerandfooter_optn","scale_optn","collation_optn"})
    public void PrintEdge(String ptr_name, String web_url, @Optional("1")String copies,@Optional("All")String page_range, @Optional("Portrait")String orientation, @Optional("Letter")String paper_size, @Optional("Normal")String page_margins,  @Optional("Color")String color_optn,  @Optional("None")String duplex_optn,  @Optional("On")String borderless,  @Optional("Auto select")String paper_tray, @Optional("Plain Paper")String paper_type, @Optional("Normal")String output_qlty, @Optional("Staple")String stapling_optn, @Optional("On")String headerandfooter_optn, @Optional("Shrink to fit")String scale_optn, @Optional("Uncollated")String collation_optn) throws InterruptedException, IOException
    {
		
		// Method to Print Web page to Printer Under Test
		EdgeAppBase.PrintEdge(ptr_name,web_url);
		
		// Method to select the desired printer.
		EdgeAppBase.SelectDesiredPrinter(MsEdgeSession, ptr_name);
		
		// Uncheck option "“Let the app change my printing preferences” if it is checked in App Print UI
		Base.UncheckAppChangePreference(MsEdgeSession);
		
		//Select Desired Orientation Option.
		if (MsEdgeSession.findElementsByXPath("//ComboBox[@Name = 'Orientation']").size()!=0){
			EdgeAppBase.SelectOrientation_Uwp(MsEdgeSession,orientation);
		}else{
			log.info("........................................................");
			log.info("The desired Orientation selection didnt appear in App UI");
			log.info("........................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log("........................................................");
			Reporter.log("The desired Orientation selection didnt appear in App UI");
			Reporter.log("........................................................");
		}
		
		//Enter desired Copies value.
		if (MsEdgeSession.findElementsByName("Copies").size()!=0){
			EdgeAppBase.SelectCopies_Uwp(MsEdgeSession,copies);
		}else{
			log.info("...................................................");
			log.info("The desired Copies selection didnt appear in App UI");
			log.info("...................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log("...................................................");
			Reporter.log("The desired Copies selection didnt appear in App UI");
			Reporter.log("...................................................");
		}
		
					
		//Select Desired Pages(page range) Option.
		if (MsEdgeSession.findElementsByName("Pages").size()!=0){
			EdgeAppBase.SelectPageRange_Uwp(MsEdgeSession,page_range);
		}else{
			log.info(".............................................................");
			log.info("The desired Pages(PageRange) selection didnt appear in App UI");
			log.info(".............................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log(".............................................................");
			Reporter.log("The desired Pages(PageRange) selection didnt appear in App UI");
			Reporter.log(".............................................................");
		}
		
		//Select Desired Scaling Option.
		if (MsEdgeSession.findElementsByName("Scale").size()!=0){
			EdgeAppBase.SelectScale_Uwp(MsEdgeSession,scale_optn);
		}else{
			log.info("..................................................");
			log.info("The desired Scale selection didnt appear in App UI");
			log.info("..................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log("..................................................");
			Reporter.log("The desired Scale selection didnt appear in App UI");
			Reporter.log("..................................................");
		}
		
		//Select Desired Page Margins Option.
		if (MsEdgeSession.findElementsByName("Margins").size()!=0){
			EdgeAppBase.SelectPageMargins_Uwp(MsEdgeSession,page_margins);
		}else{
			log.info("........................................................");
			log.info("The desired Page Margin selection didnt appear in App UI");
			log.info("........................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log("........................................................");
			Reporter.log("The desired Page Margin selection didnt appear in App UI");
			Reporter.log("........................................................");
		}
		
		//Select Desired Borderless Printing Option.
		if (MsEdgeSession.findElementsByName("Borderless printing").size()!=0){
			EdgeAppBase.SelectBorderless_Uwp(MsEdgeSession,borderless);
		}else{
			log.info(".......................................................");
			log.info("The desired Borderless selection didnt appear in App UI");
			log.info(".......................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log(".......................................................");
			Reporter.log("The desired Borderless selection didnt appear in App UI");
			Reporter.log(".......................................................");
		}
		
		//Select Desired Header and Footer Option.
		if (MsEdgeSession.findElementsByName("Headers and footers").size()!=0){
			EdgeAppBase.SelectHeadersAndFooters_Uwp(MsEdgeSession,headerandfooter_optn);
		}else{
			log.info("................................................................");
			log.info("The desired Headers and Footers selection didnt appear in App UI");
			log.info("................................................................");
			
			//This is to insert msg to TestNG emailable-report.html
			Reporter.log("................................................................");
			Reporter.log("The desired Headers and Footers selection didnt appear in App UI");
			Reporter.log("................................................................");
		}
		
		//Opening more settings to access more printing options.
		int MoreSettings = EdgeAppBase.OpenMoreSettings(MsEdgeSession);
		if	(MoreSettings == 1){	
			
			//Select Desired Duplex Option.			
			if (MsEdgeSession.findElementsByName("Duplex printing").size()!=0){
				EdgeAppBase.SelectDuplexOption_Uwp(MsEdgeSession,duplex_optn);
			}else{
				log.info("...................................................");
				log.info("The desired Duplex selection didnt appear in App UI");
				log.info("...................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("...................................................");
				Reporter.log("The desired Duplex selection didnt appear in App UI");
				Reporter.log("...................................................");
			}
			
			//Select Desired Collation Option.			
			if (MsEdgeSession.findElementsByName("Collation").size()!=0){
				EdgeAppBase.SelectCollation_Uwp(MsEdgeSession,collation_optn);
			}else{
				log.info("......................................................");
				log.info("The desired Collation selection didnt appear in App UI");
				log.info("......................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("......................................................");
				Reporter.log("The desired Collation selection didnt appear in App UI");
				Reporter.log("......................................................");
			}
			
			//Select Desired Paper Size Option
			if (MsEdgeSession.findElementsByName("Paper size").size()!=0){
				EdgeAppBase.SelectPaperSize_Uwp(MsEdgeSession,paper_size);
			}else{
				log.info("......................................................");
				log.info("The desired PaperSize selection didnt appear in App UI");
				log.info("......................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("......................................................");
				Reporter.log("The desired PaperSize selection didnt appear in App UI");
				Reporter.log("......................................................");
			}
						
			//Select Desired Paper Type Option.			
			if (MsEdgeSession.findElementsByName("Paper type").size()!=0){
				EdgeAppBase.SelectPaperType_Uwp(MsEdgeSession,paper_type);
			}else{
				log.info("......................................................");
				log.info("The desired PaperType selection didnt appear in App UI");
				log.info("......................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("......................................................");
				Reporter.log("The desired PaperType selection didnt appear in App UI");
				Reporter.log("......................................................");
			}
			
			//Select Desired Paper Tray Option.			
			if (MsEdgeSession.findElementsByName("Paper tray").size()!=0){
				EdgeAppBase.SelectPaperTray_Uwp(MsEdgeSession,paper_tray);
			}else{
				log.info("......................................................");
				log.info("The desired PaperTray selection didnt appear in App UI");
				log.info("......................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("......................................................");
				Reporter.log("The desired PaperTray selection didnt appear in App UI");
				Reporter.log("......................................................");
			}
							
			//Select Desired Output Quality Option.			
			if (MsEdgeSession.findElementsByName("Output quality").size()!=0){
				EdgeAppBase.SelectOutputQuality_Uwp(MsEdgeSession,output_qlty);
			}else{
				log.info("...........................................................");
				log.info("The desired Output Quality selection didnt appear in App UI");
				log.info("...........................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log("...........................................................");
				Reporter.log("The desired Output Quality selection didnt appear in App UI");
				Reporter.log("...........................................................");
			}
			
			//Select Desired Color Mode Option.			
			if (MsEdgeSession.findElementsByName("Color mode").size()!=0){
				EdgeAppBase.SelectColorOrMono_Uwp(MsEdgeSession,color_optn);
			}else{
				log.info(".......................................................");
				log.info("The desired Color Mode selection didnt appear in App UI");
				log.info(".......................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log(".......................................................");
				Reporter.log("The desired Color Mode selection didnt appear in App UI");
				Reporter.log(".......................................................");
			}
			
			//Select Desired Stapling Option.			
			if (MsEdgeSession.findElementsByName("Stapling").size()!=0){
				EdgeAppBase.SelectStaplingOption_Uwp(MsEdgeSession,stapling_optn);
			}else{
				log.info(".....................................................");
				log.info("The desired Stapling selection didnt appear in App UI");
				log.info(".....................................................");
				
				//This is to insert msg to TestNG emailable-report.html
				Reporter.log(".....................................................");
				Reporter.log("The desired Stapling selection didnt appear in App UI");
				Reporter.log(".....................................................");
			}
						
			//Closing more settings after accessing more printing options.
			EdgeAppBase.CloseMoreSettings(MsEdgeSession);
			
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
	@Parameters({ "device_name", "ptr_name", "web_url"})
	public void ValidatePrintQueue(String device_name, String ptr_name, String web_url) throws IOException, InterruptedException 
	{
		// Open Print Queue
		Base.OpenPrintQueue(ptr_name);
				
		// Method to attach session to Printer Queue Window
		Base.SwitchToPrinterQueue(device_name,ptr_name);
		
	    log.info("Expected queued job should be => "+web_url);
	    //Validate Print Job Queued up
	    try {
	    	String[] printJob = PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").toLowerCase().toString().split(" ");
	    	log.info(printJob[0]);
	    	Assert.assertTrue(web_url.contains(printJob[0]));
	    	//Assert.assertTrue(PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").contains(expectedPrintjob));
	    }catch(NoSuchElementException e) {
	    	log.info("Expected Print job is not found in print queue");
	     	throw new RuntimeException(e);
	    }catch(Exception e) {
	    	log.info("Error validating print job in print queue");
	    	throw new RuntimeException(e);
	    }
	    
	    log.info("Found correct job in print queue => "+web_url);
	    
	    //Waiting for the job to get spooled completely before closing the print queue window.
	    wait = new WebDriverWait(PrintQueueSession,60);
	    wait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("//ListItem[@AutomationId='ListViewItem-1']"),"Spooling"));
	    log.info("Waiting until the job spooling is completed");
	    
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
