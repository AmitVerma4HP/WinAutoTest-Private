package com.hp.win.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;



public class MsWordAppBase extends Base {
		private static final Logger log = LogManager.getLogger(MsWordAppBase.class);
		protected static RemoteWebDriver MsWordSession = null;
		protected static RemoteWebDriver PrintSettingConflictSession = null;		
		protected static RemoteWebDriver MsWordFirstSession = null;
		
		
	
	  
	 // Method to open MS Word test file
	 public static RemoteWebDriver OpenMsWordFile(String device_name, String test_filename) throws MalformedURLException, InterruptedException {
			
			   try {
			    	capabilities = new DesiredCapabilities();
			        capabilities.setCapability("app", "C:\\Program Files\\Microsoft Office\\Office16\\WINWORD.EXE");
			        capabilities.setCapability("appArguments",test_filename );
			        capabilities.setCapability("appWorkingDir", testfiles_loc);
			        capabilities.setCapability("platformName", "Windows");
			        capabilities.setCapability("deviceName",device_name);
			        MsWordFirstSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
			        Assert.assertNotNull(MsWordFirstSession);
			        MsWordFirstSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);      			        
			   		}catch(Exception e){
		            e.printStackTrace();
		            log.info("Error opening MS Word file");
		            throw new RuntimeException(e);
		        	}
			    log.info("Opened"+test_filename+"file from "+testfiles_loc);
			    
			    Thread.sleep(3000); 
			    
			    log.info("Relauching MSWord App to Get Main Window Session");
			    
			    try {
			    capabilities = new DesiredCapabilities();
		        capabilities.setCapability("app", "C:\\Program Files\\Microsoft Office\\Office16\\WINWORD.EXE");
		        capabilities.setCapability("appArguments",test_filename );
		        capabilities.setCapability("appWorkingDir", testfiles_loc);
		        capabilities.setCapability("platformName", "Windows");
		        capabilities.setCapability("deviceName",device_name);
		        MsWordSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
		        Assert.assertNotNull(MsWordSession);
		        MsWordSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);      			        
		   		}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error opening MS Word file");
	            throw new RuntimeException(e);
	        	}
			    
			    log.info("Relauching MSWord App Successful and Got Main Window Session");
			   
				return MsWordSession;
			
		}
	 
	 
	 
	 // Method to select desired printer from printers list combo box
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectDesiredPrinter_Msword(String ptr_name) throws MalformedURLException, InterruptedException {
		 
		 	WebElement PrinterListComboBox = MsWordSession.findElementByClassName("NetUIDropdownAnchor");		
	        Assert.assertNotNull(PrinterListComboBox);           
	        if(!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) 
	        {
		        log.info("Desired printer  => "+ptr_name+" <=  is not selected so selecting it from drop down");
		        PrinterListComboBox.click();
		        Thread.sleep(1000);
		        try {
		        	PrinterListComboBox.findElement(By.name(ptr_name)).click();
		        	}catch(Exception e){
		        	log.info("Printer under test is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting printer under test");     
		            throw new RuntimeException(e);
		        	}
		        Thread.sleep(1000);
		        log.info("Selected desired printer *****" +PrinterListComboBox.getText().toString()+"*****");
		    } else {
		    	log.info("Desired printer => " +PrinterListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
		 
	 }
	 
	 // Method to select desired paper size  
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectPaperSize_Msword(String paper_size) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement PaperSizeListComboBox = MsWordSession.findElementByName("Page Size");		 		
	        Assert.assertNotNull(PaperSizeListComboBox);           
	        if(!PaperSizeListComboBox.getText().toString().contentEquals(paper_size)) 
	        {
		        log.info("Desired paper size => "+paper_size+" <= is not selected so selecting it from drop down");
		        
		        // if paper size is not visible then WinAppDriver auto scroll to make list item visible and then selects it.
		        PaperSizeListComboBox.click();
		        Thread.sleep(1000);
		        try {
		        	PaperSizeListComboBox.findElement(By.name(paper_size)).click();		        	
		        	}catch(Exception e){
		        	log.info("Desired Paper Size is not found so make sure Printer Support this paper size OR have typed the paper size name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting desired paper size");     
		            throw new RuntimeException(e);
		        	}
		        Thread.sleep(1000);
		        log.info("Selected desired paper size *****" +PaperSizeListComboBox.getText().toString()+"*****");
		     } else {
		    	log.info("Desired paper size => " +PaperSizeListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
	 }
	 
	 
	 // Method to select desired duplex option
	 public static void SelectDuplexOption_Msword(String duplex_option) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement DuplexListComboBox = MsWordSession.findElementByName("Two-Sided Printing");		 		
	        Assert.assertNotNull(DuplexListComboBox);  
	        DuplexListComboBox.click();
	        Thread.sleep(1000);
	        if(duplex_option.toLowerCase().contains("long"))
	        	{
	        	try {	        		
	        		DuplexListComboBox.findElement(By.name("Print One Sided")).click();
	        		Thread.sleep(1000);
	        		DuplexListComboBox.click();
	        		Thread.sleep(1000);
	        		// Using keys as xpath does not work for longedge as it has only help text as unique property
	        		MsWordSession.getKeyboard().pressKey(Keys.ARROW_DOWN); 
	        		Thread.sleep(1000);
	        		MsWordSession.getKeyboard().pressKey(Keys.ENTER);
	        		
	        		}catch(Exception e){
		            log.info("Error selecting duplex long edge option");
		           	e.printStackTrace();
		            throw new RuntimeException(e);
	        		}
	        	Thread.sleep(1000);
	        	log.info("Selected => ***** Duplex LongEdge ***** Option");
	        	} 
	        else if(duplex_option.toLowerCase().contains("short"))
	        	{
	        	try {
	        		DuplexListComboBox.findElement(By.name("Print One Sided")).click();
	        		Thread.sleep(1000);
	        		DuplexListComboBox.click();
	        		Thread.sleep(1000);
	        		
	        		// Using keys as xpath does not work for shortedge as it has only help text as unique property
	        		MsWordSession.getKeyboard().pressKey(Keys.ARROW_DOWN); 
	        		Thread.sleep(1000);
	        		MsWordSession.getKeyboard().pressKey(Keys.ARROW_DOWN);
	        		Thread.sleep(1000);
	        		MsWordSession.getKeyboard().pressKey(Keys.ENTER);
	        		
	        		}catch(Exception e){
		            log.info("Error selecting duplex short option");
		           	e.printStackTrace();
		            throw new RuntimeException(e);
	        	}
	        	Thread.sleep(1000);
	        	log.info("Selected => ***** Duplex ShortEdge ***** Option");
	        	}
	        else {
	        	try {
	        	DuplexListComboBox.findElement(By.name(duplex_option)).click();
	        	Thread.sleep(1000);
	        	log.info("Selected desired duplex option *****" +DuplexListComboBox.getText().toString()+"*****");
	        	}catch(Exception e){
	        	log.info("Desired duplex option is not found so make sure Printer Support this duplex option OR have typed the duplex option name incorrectly in testsuite xml");
	        	e.printStackTrace();
	            log.info("Error selecting duplex option");     
	            throw new RuntimeException(e);
	        		}
	        	}   
	 }
	 
	 
	 // Method to select desired orientation  
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectOrientation_Msword(String orientation) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement OrientationListComboBox = MsWordSession.findElementByName("Orientation");		 		
	        Assert.assertNotNull(OrientationListComboBox);           
	        if(!OrientationListComboBox.getText().toString().contentEquals(orientation)) 
	        {
		        log.info("Desired duplex option => "+orientation+" <= is not selected so selecting it from drop down");
		        OrientationListComboBox.click();
		        Thread.sleep(1000);
		        try {		        	
		        	OrientationListComboBox.findElement(By.name(orientation)).click();		        	
		        	}catch(Exception e){
		        	log.info("Desired duplex option is not found so make sure Printer Support this duplex option OR have typed the duplex option name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting duplex option");     
		            throw new RuntimeException(e);
		        	}
		        Thread.sleep(1000);
		        log.info("Selected desired duplex option *****" +OrientationListComboBox.getText().toString()+"*****");
		     } else {
		    	log.info("Desired duplex option => " +OrientationListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
	 }
	 
	 
	 
	 // Method to select desired Collation option  
	 // Possible candidate for re-factoring when there are multiple application in automation
	 public static void SelectCollation_Msword(String collation) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement CollationListComboBox = MsWordSession.findElementByName("Collation");		 		
	        Assert.assertNotNull(CollationListComboBox);           
	        if(!CollationListComboBox.getText().toString().contentEquals(collation)) 
	        {
		        log.info("Desired duplex option => "+collation+" <= is not selected so selecting it from drop down");
		        CollationListComboBox.click();
		        Thread.sleep(1000);
		        try {
		        	CollationListComboBox.findElement(By.name(collation)).click();		        	
		        	}catch(Exception e){
		        	log.info("Desired duplex option is not found so make sure Printer Support this duplex option OR have typed the duplex option name incorrectly in testsuite xml");
		        	e.printStackTrace();
		            log.info("Error selecting duplex option");     
		            throw new RuntimeException(e);
		        	}
		        Thread.sleep(1000);
		        log.info("Selected desired duplex option *****" +CollationListComboBox.getText().toString()+"*****");
		     } else {
		    	log.info("Desired duplex option => " +CollationListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
	 }
	 
	 
	 
	 // Method to select desired Copies option  
	 // Possible candidate for changing approach  
	 public static void SelectCopies_Msword(String copies) throws MalformedURLException, InterruptedException {
		 	
		 	
		 	//Directly working with EditBox "Copies" is erroring out so trying to select working with copies using keys "tab" 
		 	
		 	//Click on Print in the Menu Item and then press 2 Tabs
	        MsWordSession.findElementByXPath("//TabItem[@Name='Print']").click();
	        Thread.sleep(1000);
	        
	        // Press Tab - Once
	        MsWordSession.getKeyboard().pressKey(Keys.TAB);
	        //MsWordSession.getKeyboard().releaseKey(Keys.TAB);	        
	        Thread.sleep(1000);
	        
	        // Press Tab - Twice and you should be on Copies EditBox now
	        MsWordSession.getKeyboard().pressKey(Keys.TAB);
	        //MsWordSession.getKeyboard().releaseKey(Keys.TAB);
	        Thread.sleep(1000);
	        
	        MsWordSession.getKeyboard().pressKey(copies);
	        Thread.sleep(1000);

		 	//Click on Print in the Menu Item (outside Print group screen to save entered copies value)
	        MsWordSession.findElementByXPath("//TabItem[@Name='Print']").click();
	        Thread.sleep(1000);
	        log.info("Entered copies value ***** "+copies+" *****");        	        

	 }

	 

	 // Method to select desired PagesToPrint option  
	 public static void SelectPagesToPrint_Msword(String pages_to_print, String page_count) throws MalformedURLException, InterruptedException {
		 		 
		 	WebElement PrintPagesComboBox = MsWordSession.findElementByName("Print What");		 		
	        Assert.assertNotNull(PrintPagesComboBox);
	        
	        if(!PrintPagesComboBox.getText().toString().contentEquals(pages_to_print)) 
	        {
	        	log.info("Desired pages to print option => "+pages_to_print+" <= is not selected so selecting it from drop down");
		        try 
		        {		        		        	
			        if(!page_count.contentEquals("NA"))
			        {
			        	// Go for selecting "Custom Print" and then enter page number
			        	PrintPagesComboBox.click();
			        	Thread.sleep(1000);
			        	PrintPagesComboBox.findElement(By.name(pages_to_print)).click();
			        	Thread.sleep(1000);
			        	log.info("Current selection indicates Custom Page Print so going with this option");
			        	
			        	// Pages combo is already selected with above steps to go ahead and enter page number to print		            
				        MsWordSession.getKeyboard().pressKey(page_count);
				        Thread.sleep(1000);
				        log.info("Entered desired page count value ***** "+page_count+" *****"); 			        	
			        }		
			        
			        else 
			        {	
			        	// Go for selecting non custom Print option from combo box
			        	PrintPagesComboBox.click();
			        	Thread.sleep(1000);
			        	PrintPagesComboBox.findElement(By.name(pages_to_print)).click();
			        	//log.info("Selected desired pages to print option ***** "+pages_to_print+" *****");     	
			        }
		        
		          }catch(Exception e){
			        	log.info("Desired pages to print option is not found so check you have selected relevant test file OR have typed the pages to print option name incorrectly in testsuite xml");
			        	e.printStackTrace();
			            log.info("Error selecting pages to print option");     
			            throw new RuntimeException(e);
			        }
		        Thread.sleep(1000);
		        log.info("Selected desired pages to print option *****" +PrintPagesComboBox.getText().toString()+"*****");
		  } 
	      else 
		  {
		    	log.info("Desired pages to print option => " +PrintPagesComboBox.getText().toString()+" <= is already selected so proceeding");
		  }
	 }
	 

}
