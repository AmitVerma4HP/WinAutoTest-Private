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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;


public class AcrobatReaderBase extends Base {

    private static final Logger log = LogManager.getLogger(AcrobatReaderBase.class);
	public static RemoteWebDriver acrobatSession = null;
	public static RemoteWebDriver acrobatAppSession = null;
    public static WebDriverWait wait;
    
    // Method to open testfile with Acrobat Reader App 
    public static RemoteWebDriver OpenAcrobatReader(String device_name, String test_filename, String acrobat_exe_loc) throws InterruptedException, MalformedURLException {
    	
    	 log.info("Acrobat Reader exe location: " +acrobat_exe_loc);

		   try {
		    	capabilities = new DesiredCapabilities();
		        capabilities.setCapability("app", acrobat_exe_loc);
		        capabilities.setCapability("appArguments",test_filename );
		        capabilities.setCapability("appWorkingDir", testfiles_loc);
		        capabilities.setCapability("platformName", "Windows");
		        capabilities.setCapability("deviceName",device_name);
		        acrobatSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);	
		        Assert.assertNotNull(acrobatSession);
		        acrobatSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);      			        
		   		}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error opening PDF file from Acrobat");
	            throw new RuntimeException(e);
	        	}
		    log.info("Opened"+test_filename+"file from "+testfiles_loc);		    
		    Thread.sleep(3000); 
		    
		    SwitchToAcrobatApp(device_name);
		    		    
			return acrobatAppSession; // Return Acrobat App Session which has actual control of the App
    }
   

     // Method to select desired printer from printers list combo box
	 public static void SelectDesiredPrinter_AcrobatPdf(String ptr_name, RemoteWebDriver session, String device) throws MalformedURLException, InterruptedException {
		 
		    WebElement PrinterListComboBox = session.findElementByClassName("ComboBox");		 			 	 
	        Assert.assertNotNull(PrinterListComboBox);           
	        if(!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) 
	        {
		        log.info("Desired printer  => "+ptr_name+" <=  is not selected so selecting it from drop down");
		        PrinterListComboBox.click();
		        Thread.sleep(2000);
		        try {		   
		        	RemoteWebDriver TmpSession = GetDesktopSession(device); //Printer selection comes in Desktop Session so had to use it to select desired printer
		        	TmpSession.findElement(By.name(ptr_name)).click();		        	
		        	Thread.sleep(1000);
		        	log.info("Selected desired printer *****" +PrinterListComboBox.getText().toString()+"*****");
		        	TmpSession.quit();
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

	 
	 	// Method to select desired Copies option between 1-999
		public static void SelectCopies_Acrobat(RemoteWebDriver session, String copies) throws MalformedURLException, InterruptedException {
			
			// Ensure supported copies value entered
			if(Integer.parseInt(copies) >0 && Integer.parseInt(copies) <1000) {			
				// Clicking on Copies Edit box.
				session.findElementByXPath("//Edit[@Name = 'RichEdit Control']").click();
				Thread.sleep(1000);
				session.findElementByXPath("//Edit[@Name = 'RichEdit Control']").clear();			
				Thread.sleep(1000);
				session.findElementByXPath("//Edit[@Name = 'RichEdit Control']").sendKeys(copies);
				log.info("Entered copies value ***** " + copies + " *****");
				Thread.sleep(1000);
			} else {
				log.info("-----------------------------------------------------------------------------------------------------------------------------");
				log.info("Copies value "+copies+" is not between 1-999 so SKIPPING setting copies. Check the copies paramter value in XML");
				log.info("-----------------------------------------------------------------------------------------------------------------------------");
				
				//This is to insert msg to TestNG emailable-report.html 
        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------");
        		Reporter.log("Copies value "+copies+" is not between 1-999 so SKIPPING setting copies. Check the copies paramter value in XML");
        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------");			
				}
		}
		
		
	 	// Method to Enter desired Page Count value
		public static void SelectPageCount_Acrobat(RemoteWebDriver session, String page_count) throws MalformedURLException, InterruptedException {
			
			if(page_count.toLowerCase().contains("all")) {
				if(!session.findElementByXPath("//RadioButton[@Name = 'All']").isSelected()) {
					log.info("Desired Page Count " +page_count+" NOT selected so selecting it");
					Thread.sleep(1000);
					session.findElementByXPath("//RadioButton[@Name = 'All']").click();
					Thread.sleep(1000);
					log.info("Selected desired page count *****" +page_count+"*****");
				}else {
					log.info("Desired Page Count " +page_count+" is already SELECTED so proceeding further");
				}
			}else if(page_count.toLowerCase().contains("current")) {
					if(!session.findElementByXPath("//RadioButton[@Name = 'Current page']").isSelected()) {
						log.info("Desired Page Count " +page_count+" NOT selected so selecting it");
						Thread.sleep(1000);
						session.findElementByXPath("//RadioButton[@Name = 'Current page']").click();
						Thread.sleep(1000);
						log.info("Selected desired page count *****" +page_count+"*****");
					}else {
						log.info("Desired Page Count " +page_count+" is already SELECTED so proceeding further");
					}			
			} else {			
					session.findElementByXPath("//RadioButton[@Name = 'Pages']").click();
					Thread.sleep(1000);
					session.getKeyboard().pressKey(Keys.TAB);
					Thread.sleep(1000);
					session.getKeyboard().pressKey(page_count);
					Thread.sleep(1000);
					session.findElementByXPath("//RadioButton[@Name = 'Pages']").click();
					log.info("Entered desired page count value ***** " + page_count + " *****");
					Thread.sleep(1000);	
			}	 
		}
		
		
		// Method to Select Desired Duplex Option
		public static void SelectDuplex_Acrobat(RemoteWebDriver session, String duplex) throws MalformedURLException, InterruptedException {
			
			if(duplex.toLowerCase().contains("none")) {
				if(session.findElementByXPath("//CheckBox[@Name = 'Print on both sides of paper']").isSelected()) {
					log.info("Desired Duplex Option " +duplex+" NOT selected so selecting it");
					Thread.sleep(1000);
					session.findElementByXPath("//CheckBox[@Name = 'Print on both sides of paper']").click();
					Thread.sleep(1000);
					log.info("Selected desired Duplex Option *****" +duplex+"*****");
				}else {
					log.info("Desired Duplex Option " +duplex+" is already SELECTED so proceeding further");
				}
			}else if(duplex.toLowerCase().contains("shortedge")) {
					if(!session.findElementByXPath("//CheckBox[@Name = 'Print on both sides of paper']").isSelected()) {						
						session.findElementByXPath("//CheckBox[@Name = 'Print on both sides of paper']").click();		
						Thread.sleep(1000);
						if(!session.findElementByXPath("//RadioButton[@Name = 'Flip on short edge']").isSelected()) {
							log.info("Desired Duplex Option " +duplex+" NOT selected so selecting it");
							Thread.sleep(1000);
							session.findElementByXPath("//RadioButton[@Name = 'Flip on short edge']").click();
							Thread.sleep(1000);
							log.info("Selected desired Duplex Option *****" +duplex+"*****");
						}else {
							log.info("Desired Duplex Options " +duplex+" is already SELECTED so proceeding further");
							  }
						}
			} else if(duplex.toLowerCase().contains("longedge")) {
				if(!session.findElementByXPath("//CheckBox[@Name = 'Print on both sides of paper']").isSelected()) {						
					session.findElementByXPath("//CheckBox[@Name = 'Print on both sides of paper']").click();		
					Thread.sleep(1000);
					if(!session.findElementByXPath("//RadioButton[@Name = 'Flip on long edge']").isSelected()) {
						log.info("Desired Duplex Option " +duplex+" NOT selected so selecting it");
						Thread.sleep(1000);
						session.findElementByXPath("//RadioButton[@Name = 'Flip on long edge']").click();
						Thread.sleep(1000);
						log.info("Selected desired Duplex Option *****" +duplex+"*****");
					}else {
						log.info("Desired Duplex Option " +duplex+" is already SELECTED so proceeding further");
						  }
					}
			} else {				
				log.info("-----------------------------------------------------------------------------------------------------------------------------");
				log.info("Duplex Option "+duplex+" is NOT FOUND so SKIPPING setting duplex option. Check the duplex option parameter value in XML");
				log.info("-----------------------------------------------------------------------------------------------------------------------------");
				
				//This is to insert msg to TestNG emailable-report.html 
        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------");
        		Reporter.log("Duplex Option "+duplex+" is NOT FOUND so SKIPPING setting duplex option. Check the duplex option parameter value in XML");
        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------");			
				}				
		}
		
		
		// Method to Select desired Orientation value
		public static void SelectOrientation_Acrobat(RemoteWebDriver session, String orientation) throws MalformedURLException, InterruptedException {
					
			if(orientation.toLowerCase().contains("auto")) {
				if(!session.findElementByXPath("//RadioButton[@Name = 'Auto portrait/landscape']").isSelected()) {
					log.info("Desired Orientation " +orientation+" NOT selected so selecting it");
					Thread.sleep(1000);
					session.findElementByXPath("//RadioButton[@Name = 'Auto portrait/landscape']").click();
					Thread.sleep(1000);
					log.info("Selected desired Orientation *****" +orientation+"*****");
					}else {
					log.info("Desired Orientation " +orientation+" is already SELECTED so proceeding further");
					}
				}else if(orientation.toLowerCase().contains("portrait")) {
					if(!session.findElementByXPath("//RadioButton[@Name = 'Portrait']").isSelected()) {
					log.info("Desired Orientation " +orientation+" NOT selected so selecting it");
					Thread.sleep(1000);
					session.findElementByXPath("//RadioButton[@Name = 'Portrait']").click();
					Thread.sleep(1000);
					log.info("Selected desired Orientation *****" +orientation+"*****");
					}else {
						log.info("Desired Orientation " +orientation+" is already SELECTED so proceeding further");
					}
				} else if(orientation.toLowerCase().contains("landscape")) {
					if(!session.findElementByXPath("//RadioButton[@Name = 'Landscape']").isSelected()) {
					log.info("Desired Orientation " +orientation+" NOT selected so selecting it");
					Thread.sleep(1000);
					session.findElementByXPath("//RadioButton[@Name = 'Landscape']").click();
					Thread.sleep(1000);
					log.info("Selected desired Orientation *****" +orientation+"*****");
					} else {
					log.info("Desired Orientation " +orientation+" is already SELECTED so proceeding further");
					}
				} else {					
					log.info("-----------------------------------------------------------------------------------------------------------------------------------");
					log.info("Orientation "+orientation+" is NOT FOUND so SKIPPING setting Orientation. Check the orientation option parameter value in XML");
					log.info("-----------------------------------------------------------------------------------------------------------------------------------");
					
					//This is to insert msg to TestNG emailable-report.html 
	        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------------");
	        		Reporter.log("Orientation "+orientation+" is NOT FOUND so SKIPPING setting Orientation. Check the orientation option parameter value in XML");
	        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------------");		
					}
			}
		
		
		
		// Method to Select desired Color Option value
		public static void SelectColor_Acrobat(RemoteWebDriver session, String color) throws MalformedURLException, InterruptedException {
						
				if(color.toLowerCase().contains("color")) {
					if(session.findElementByXPath("//CheckBox[@Name = 'Print in grayscale (black and white)']").isSelected()) {
						log.info("Desired Color Option " +color+" NOT selected so selecting it");
						Thread.sleep(1000);
						session.findElementByXPath("//CheckBox[@Name = 'Print in grayscale (black and white)']").click();
						Thread.sleep(1000);
						log.info("Selected desired Color Option *****" +color+"*****");
						}else {
							log.info("Desired Color Option " +color+" is already SELECTED so proceeding further");
							}
				}else if(color.toLowerCase().contains("mono")) {
					if(!session.findElementByXPath("//CheckBox[@Name = 'Print in grayscale (black and white)']").isSelected()) {
						log.info("Desired Color Option " +color+" NOT selected so selecting it");
						Thread.sleep(1000);
						session.findElementByXPath("//CheckBox[@Name = 'Print in grayscale (black and white)']").click();
						Thread.sleep(1000);
						log.info("Selected desired Color Option *****" +color+"*****");
						}else {
							log.info("Desired Color Option " +color+" is already SELECTED so proceeding further");
							}				
				}else{					
					log.info("-----------------------------------------------------------------------------------------------------------------------------------");
					log.info("Color Option "+color+" is NOT FOUND so SKIPPING setting Color Option. Check the color option parameter value in XML");
					log.info("-----------------------------------------------------------------------------------------------------------------------------------");
					
					//This is to insert msg to TestNG emailable-report.html 
	        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------------");
	        		Reporter.log("Color Option "+color+" is NOT FOUND so SKIPPING setting Color Option. Check the color option parameter value in XML");
	        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------------");		
				}
		}
		
		
		// Method to Select desired Scaling value
		public static void SelectScale_Acrobat(RemoteWebDriver session, String scale) throws MalformedURLException, InterruptedException {
						
				if(scale.toLowerCase().contains("fit")) {
					if(!session.findElementByXPath("//RadioButton[@Name = 'Fit']").isSelected()) {
						log.info("Desired Scale Option " +scale+" NOT selected so selecting it");
						Thread.sleep(1000);
						session.findElementByXPath("//RadioButton[@Name = 'Fit']").click();
						Thread.sleep(1000);
						log.info("Selected desired Scale Option *****" +scale+"*****");
						}else {
							log.info("Desired Scale Option " +scale+" is already SELECTED so proceeding further");
							}
				}else if(scale.toLowerCase().contains("actual")) {
					if(!session.findElementByXPath("//RadioButton[@Name = 'Actual size']").isSelected()) {
						log.info("Desired Scale Option " +scale+" NOT selected so selecting it");
						Thread.sleep(1000);
						session.findElementByXPath("//RadioButton[@Name = 'Actual size']").click();
						Thread.sleep(1000);
						log.info("Selected desired Scale Option *****" +scale+"*****");
						}else {
							log.info("Desired Scale Option " +scale+" is already SELECTED so proceeding further");
							}			
				}else if(scale.toLowerCase().contains("shrink")) {
					if(!session.findElementByXPath("//RadioButton[@Name = 'Shrink oversized pages']").isSelected()) {
						log.info("Desired Scale Option " +scale+" NOT selected so selecting it");
						Thread.sleep(1000);
						session.findElementByXPath("//RadioButton[@Name = 'Shrink oversized pages']").click();
						Thread.sleep(1000);
						log.info("Selected desired Scale Option *****" +scale+"*****");
						}else {
							log.info("Desired Scale Option " +scale+" is already SELECTED so proceeding further");
							}	
				}else if(scale.matches("[0-9]+")) {
						log.info("Desired Scale Option " +scale+" NOT selected so selecting it");
						Thread.sleep(1000);
						session.findElementByXPath("//RadioButton[@Name = 'Custom Scale:']").click();
						Thread.sleep(1000);
						session.getKeyboard().pressKey(Keys.TAB);
						Thread.sleep(1000);
						session.getKeyboard().pressKey(scale);
						Thread.sleep(1000);
						session.findElementByXPath("//RadioButton[@Name = 'Custom Scale:']").click();
						log.info("Selected desired Custom Scale Option *****" +scale+"%*****");
				}else{					
					log.info("-----------------------------------------------------------------------------------------------------------------------------------");
					log.info("Scale Option "+scale+" is NOT FOUND so SKIPPING setting Scale Option. Check the scale option parameter value in XML");
					log.info("-----------------------------------------------------------------------------------------------------------------------------------");
					
					//This is to insert msg to TestNG emailable-report.html 
	        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------------");
	        		Reporter.log("Scale Option "+scale+" is NOT FOUND so SKIPPING setting Scale Option. Check the scale option parameter value in XML");
	        		Reporter.log("-------------------------------------------------------------------------------------------------------------------------------");	
				}
		}
		
		
	 	// Method to Select desired Paper Size
		public static void SelectPaperSize_Acrobat(RemoteWebDriver session, String paper_size,String device) throws MalformedURLException, InterruptedException {
			
			session.findElementByXPath("//Button[@Name = 'Page Setup...']").click();
			Thread.sleep(1000);
			RemoteWebDriver TmpSession = GetDesktopSession(device); 
			WebElement PaperSizeListComboBox = session.findElementByXPath("//ComboBox[@Name = 'Size:']");	 		
	        Assert.assertNotNull(PaperSizeListComboBox);           
	        if(!PaperSizeListComboBox.getText().toString().contentEquals(paper_size))   {
		        log.info("Desired paper size => "+paper_size+" <= is not selected so selecting it from drop down");	  
		        PaperSizeListComboBox.click();
		        Thread.sleep(1000);		        
		        try {
		        	TmpSession.findElement(By.name(paper_size)).click(); // Listed paper size comes in Desktop Session so had to use it to select desired paper size
		        	Thread.sleep(1000);
		        	log.info("Selected desired paper size *****" +PaperSizeListComboBox.getText().toString()+"*****");
		        	TmpSession.quit();
		        	}catch(Exception e){
		        		log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		        		log.info("\""+paper_size+"\" Paper Size is NOT FOUND so either 1) Your Printer does not support \""+paper_size+"\" Paper Size OR 2) You have typed the paper size value incorrectly in testsuite xml");
		        		log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		        		
		        		//This is to insert msg to TestNG emailable-report.html 
		        		Reporter.log("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		        		Reporter.log("\""+paper_size+"\" Paper Size is NOT FOUND so either 1) Your Printer does not support \""+paper_size+"\" Paper Size OR 2) You have typed the paper size value incorrectly in testsuite xml");
		        		Reporter.log("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			        	//e.printStackTrace();
			            log.info("Error selecting paper size option but continuing test with rest of the print options");  
		        	}		     		        
		     } else {
		    	log.info("Desired paper size => " +PaperSizeListComboBox.getText().toString()+" <= is already selected so proceeding");
	        }
	        Thread.sleep(1000);
	        session.findElementByXPath("//Button[@Name = 'OK']").click();
	        Thread.sleep(1000);	        
	 }
		
		
		
		// Method to switch from to Acrobat session
		public static void SwitchToAcrobatApp(String device_name) throws MalformedURLException {
			try {
			
				DesktopSession = Base.GetDesktopSession(device_name);
			    
			    //Get handle to PrinterQueue window
			    WebElement AcrobatAppWindow = DesktopSession.findElementByClassName("AcrobatSDIWindow");
		    	String nativeWindowHandle = AcrobatAppWindow.getAttribute("NativeWindowHandle");
		    	int acrobatAppWindowHandle = Integer.parseInt(nativeWindowHandle);
		    	log.debug("int value:" + nativeWindowHandle);
		    	String acrobatAppTopWindowHandle  = hex.concat(Integer.toHexString(acrobatAppWindowHandle));
		    	log.debug("Hex Value:" + acrobatAppTopWindowHandle);
	
		    	log.info("Successfully got Acrobat App handle.");
		    	// Create a PrintQueueSession by attaching to an existing application top level window handle
		    	DesiredCapabilities capabilities = new DesiredCapabilities();
		    	capabilities.setCapability("appTopLevelWindow", acrobatAppTopWindowHandle);
		    	capabilities.setCapability("platformName", "Windows");
		        capabilities.setCapability("deviceName", device_name);
		        acrobatAppSession = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
				}catch(Exception e){
					e.printStackTrace();
					log.info("Error getting Acrobat App session");
					//throw new RuntimeException(e);
		        	}
			log.info("Acrobat App session created successfully");    	
	    	
		}
	
		// Method to find if App is closed or not , if not then close the Acrobat App
		public static void CloseAcrobat(String device_name) throws MalformedURLException {
			
			try {
				SwitchToAcrobatApp(device_name);
			}catch(Exception e) {
				log.info("Cant find Arobat App Session so Looks like App is already closed");
			}		
			
			//Click on Close button if App is still open
			try {
				acrobatAppSession.findElementByXPath("//Button[@Name = 'Close']").click();
				Thread.sleep(2000);
				log.info("Closed Acrobat App Successfully");
			}catch(Exception e) {
				log.info("Error closing Acrobat App");
			}
			
		}
	 
		
}
