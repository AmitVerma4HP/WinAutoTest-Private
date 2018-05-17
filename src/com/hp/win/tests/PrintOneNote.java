package com.hp.win.tests;


import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.Base;
import com.hp.win.core.OneNoteAppBase;
import com.hp.win.utility.ScreenshotUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import com.hp.win.utility.*;							

import java.io.IOException;

	@Listeners({ScreenshotUtility.class})
	public class PrintOneNote extends OneNoteAppBase {
		private static final Logger log = LogManager.getLogger(PrintOneNote.class);
		private static String currentClass;	
		//public static RemoteWebDriver OneNoteSession = null;
		public static String expectedPrintjob;
		
		@BeforeClass
		@Parameters({ "device_name", "ptr_name"})
	    public static void setup(String device_name, String ptr_name) throws InterruptedException, IOException { 
	       
		currentClass = PrintOneNote.class.getSimpleName();
				
		//Start PrintTrace log capturing 
		PrintTraceCapture.StartLogCollection(currentClass);											   
	    OneNoteSession = OneNoteAppBase.OpenOneNoteApp(device_name);
	    Thread.sleep(2000);
	  
	    }
	
		
	@Test
	@Parameters({ "ptr_name", "copies","page_range","orientation","paper_size","page_margins","color_optn","duplex_optn","borderless","paper_tray","paper_type","output_qlty","stapling_optn","headerandfooter_optn","scale_optn","collation_optn"})
    public void OneNotePrint(String ptr_name,@Optional("1")String copies,@Optional("All pages")String page_range, @Optional("Portrait")String orientation, @Optional("Letter")String paper_size, @Optional("Normal")String page_margins,  @Optional("Color")String color_optn,  @Optional("None")String duplex_optn,  @Optional("On")String borderless,  @Optional("Auto select")String paper_tray, @Optional("Plain Paper")String paper_type, @Optional("Normal")String output_qlty, @Optional("Staple")String stapling_optn, @Optional("On")String headerandfooter_optn, @Optional("Shrink to fit")String scale_optn, @Optional("Uncollated")String collation_optn) throws InterruptedException, IOException
    {
		
		// Method to Print Web page to Printer Under Test
		OneNoteAppBase.PrintOneNote(ptr_name);
		
		// Method to select the desired printer.
		OneNoteAppBase.SelectDesiredPrinter(OneNoteSession, ptr_name);
		
		//Select Desired Orientation Option.
		if (OneNoteSession.findElementsByXPath("//ComboBox[@Name = 'Orientation']").size()!=0){
			OneNoteAppBase.SelectOrientation_Uwp(OneNoteSession,orientation);
		}else{
			log.info("The desired Orientation feature is not supported by the printer ");
		}
		
		//Enter desired Copies value.
		if (OneNoteSession.findElementsByName("Copies").size()!=0){
			OneNoteAppBase.SelectCopies_Uwp(OneNoteSession,copies);
		}else{
			log.info("The desired Copies selection feature is not supported by the printer ");
		}
		
					
		//Select Desired Pages(page range) Option.
		if (OneNoteSession.findElementsByName("Pages").size()!=0){
			OneNoteAppBase.SelectPageRange_Uwp(OneNoteSession,page_range);
		}else{
			log.info("The desired Pages(Page Range) feature is not supported by the printer ");
		}
		
		//Select Desired Scaling Option.
		if (OneNoteSession.findElementsByName("Scale").size()!=0){
			OneNoteAppBase.SelectScale_Uwp(OneNoteSession,scale_optn);
		}else{
			log.info("The desired Scaling feature is not supported by the printer ");
		}
		
		//Select Desired Page Margins Option.
		if (OneNoteSession.findElementsByName("Margins").size()!=0){
			OneNoteAppBase.SelectPageMargins_Uwp(OneNoteSession,page_margins);
		}else{
			log.info("The desired Page Margin feature is not supported by the printer ");
		}
		
		//Select Desired Borderless Printing Option.
		if (OneNoteSession.findElementsByName("Borderless printing").size()!=0){
			OneNoteAppBase.SelectBorderless_Uwp(OneNoteSession,borderless);
		}else{
			log.info("The desired Borderless feature is not supported by the printer ");
		}
		
		//Select Desired Header and Footer Option.
		if (OneNoteSession.findElementsByName("Headers and footers").size()!=0){
			OneNoteAppBase.SelectHeadersAndFooters_Uwp(OneNoteSession,headerandfooter_optn);
		}else{
			log.info("The desired Header and Footer feature is not supported by the printer ");
		}
		
		//Opening more settings to access more printing options.
		int MoreSettings = OneNoteAppBase.OpenMoreSettings(OneNoteSession);
		if	(MoreSettings == 1){	
			
			//Select Desired Duplex Option.			
			if (OneNoteSession.findElementsByName("Duplex printing").size()!=0){
				OneNoteAppBase.SelectDuplexOption_Uwp(OneNoteSession,duplex_optn);
			}else{
				log.info("The desired Duplexing feature is not supported by the printer ");
			}
			
			//Select Desired Collation Option.			
			if (OneNoteSession.findElementsByName("Collation").size()!=0){
				OneNoteAppBase.SelectCollation_Uwp(OneNoteSession,collation_optn);
			}else{
				log.info("The desired Collation feature is not supported by the printer ");
			}
			
			//Select Desired Paper Size Option
			if (OneNoteSession.findElementsByName("Paper size").size()!=0){
				OneNoteAppBase.SelectPaperSize_Uwp(OneNoteSession,paper_size);
			}else{
				log.info("The desired Paper Size feature is not supported by the printer ");
			}
						
			//Select Desired Paper Type Option.			
			if (OneNoteSession.findElementsByName("Paper type").size()!=0){
				OneNoteAppBase.SelectPaperType_Uwp(OneNoteSession,paper_type);
			}else{
				log.info("The desired Paper Type feature is not supported by the printer ");
			}
			
			//Select Desired Paper Tray Option.			
			if (OneNoteSession.findElementsByName("Paper tray").size()!=0){
				OneNoteAppBase.SelectPaperTray_Uwp(OneNoteSession,paper_tray);
			}else{
				log.info("The desired Paper Tray feature is not supported by the printer ");
			}
							
			//Select Desired Output Quality Option.			
			if (OneNoteSession.findElementsByName("Output quality").size()!=0){
				OneNoteAppBase.SelectOutputQuality_Uwp(OneNoteSession,output_qlty);
			}else{
				log.info("The desired Output Quality feature is not supported by the printer ");
			}
			
			//Select Desired Color Mode Option.			
			if (OneNoteSession.findElementsByName("Color mode").size()!=0){
				OneNoteAppBase.SelectColorOrMono_Uwp(OneNoteSession,color_optn);
			}else{
				log.info("The desired Color Mode feature is not supported by the printer ");
			}
			
			//Select Desired Stapling Option.			
			if (OneNoteSession.findElementsByName("Stapling").size()!=0){
				OneNoteAppBase.SelectStaplingOption_Uwp(OneNoteSession,stapling_optn);
			}else{
				log.info("The desired Stapling feature is not supported by the printer ");
			}
						
			//Closing more settings after accessing more printing options.
			OneNoteAppBase.CloseMoreSettings(OneNoteSession);
			
		}else{
			log.info("More Settings page could not be opened");
			OneNoteSession.findElementByXPath("//Button[@AutomationId = 'CloseButton']").click();
			Assert.fail();
		}
			
			// Tap on Print button(Give Print)
	    	OneNoteSession.findElementByXPath("//Button[@AutomationId = 'PrintButton']").click();
	    	log.info("Clicked on final Print button -> Print option Successfully");
		    
	    }
 
		
	@Test(dependsOnMethods = { "OneNotePrint" })
	@Parameters({ "device_name", "ptr_name"})
	public void ValidatePrintQueue(String device_name, String ptr_name) throws IOException, InterruptedException 
	{
		String expectedJob = OneNoteSession.getTitle().toString().substring(0,25).trim();
		log.info("Expected queued job should be => "+expectedJob);
		
		// Open Print Queue
		Base.OpenPrintQueue(ptr_name);
				
		// Method to attach session to Printer Queue Window
		Base.SwitchToPrinterQueue(device_name,ptr_name);
			    
		 //Validate Print Job Queued up
	    try {
	    	String printJob = PrintQueueSession.findElementByXPath("//ListItem[@AutomationId='ListViewItem-0']").getAttribute("Name").toString();
	    	Assert.assertTrue((printJob.contains(expectedJob)) || (printJob.contains("PDF")));
	    	log.info("Found expected job in print queue => "+printJob);
	    }catch(NoSuchElementException e) {
	    	log.info("Expected Print job is not found in print queue");
	     	throw new RuntimeException(e);
	    }catch(Exception e) {
	    	log.info("Error validating print job in print queue");
	    	throw new RuntimeException(e);
	    }
	    
	    PrintQueueSession.close();
	    log.info("Tester MUST validate printed output physical copy to ensure job is printed with correct Print Options");	    
	    
	}
    
    @AfterClass(alwaysRun=true)
    public static void TearDown() throws IOException, InterruptedException 
    {	        
    
		if (OneNoteSession!= null)
		{
			OneNoteSession.quit();
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
