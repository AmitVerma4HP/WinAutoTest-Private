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


package com.hp.win.core;


import java.net.MalformedURLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;


public class UwpAppBase extends Base {
	private static final Logger log = LogManager.getLogger(UwpAppBase.class);
	public static RemoteWebDriver Session = null;
		

	// Method to select desired printer from printers list combo box
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectDesiredPrinter(RemoteWebDriver Session, String ptr_name) throws MalformedURLException, InterruptedException {
		
		WebElement PrinterListComboBox = Session.findElementByClassName("ComboBox");
		Assert.assertNotNull(PrinterListComboBox);
		if (!PrinterListComboBox.getText().toString().contentEquals(ptr_name)) 
		{
			log.info("Desired printer => "+ptr_name+" is not selected so selecting it from drop down");
			PrinterListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElement(By.name(ptr_name)).click();
				log.info("Selected desired printer *****" +PrinterListComboBox.getText().toString()+"*****");
				Thread.sleep(2000);
			} catch (Exception e) {
				log.info("Printer under test "+ptr_name+ " is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name incorrectly in testsuite xml");
				e.printStackTrace();
				log.info("Error selecting printer under test");
				throw new RuntimeException(e);
			}			
		} else {
			log.info("Desired printer => " + PrinterListComboBox.getText().toString() + " <= is already selected so proceeding");
		}

	}

	
	// Method to select desired Copies option
	public static void SelectCopies_Uwp(RemoteWebDriver Session, String copies) throws MalformedURLException, InterruptedException {

		// Clicking on Copies Edit box.
		Session.findElementByXPath("//*[contains(@AutomationId,'JobCopiesAllDocuments_NumberText')]").click();
		Thread.sleep(2000);

		Session.getKeyboard().pressKey(Keys.CLEAR);
		Session.getKeyboard().pressKey(Keys.BACK_SPACE);
		Thread.sleep(1000);

		Session.getKeyboard().pressKey(copies);
		Thread.sleep(1000);

		log.info("Entered copies value ***** " + copies + " *****");
		Thread.sleep(2000);
	}

	
	// Method to select desired orientation
	public static void SelectOrientation_Uwp(RemoteWebDriver Session, String orientation) throws MalformedURLException, InterruptedException {

		WebElement OrientationListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Orientation']");
		Assert.assertNotNull(OrientationListComboBox);
		if (!OrientationListComboBox.getText().toString().contentEquals(orientation)) 
		{
			log.info("Desired Orientation option => " + orientation + " <= is not selected so selecting it from drop down");
			OrientationListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(orientation).click();
				Thread.sleep(2000);
				log.info("Selected desired orientation option *****" + OrientationListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+orientation+"\" Orientation is NOT FOUND so either 1) your Printer does not support \""+orientation+"\" Orientation OR 2) you have typed the Orientation value incorrectly in testsuite xml.");
            	log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+orientation+"\" Orientation is NOT FOUND so either 1) your Printer does not support \""+orientation+"\" Orientation OR 2) you have typed the Orientation value incorrectly in testsuite xml.");
				Reporter.log("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				//e.printStackTrace();
				log.info("Error selecting orientation option but continuing test with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails. 
				OrientationListComboBox.click();
			}			
		} else {
			log.info("Desired orientation option => " + OrientationListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired paper size
	// Possible candidate for re-factoring when there are multiple application in automation
	public static void SelectPaperSize_Uwp(RemoteWebDriver Session, String paper_size) throws MalformedURLException, InterruptedException {
		
		WebElement PaperSizeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Paper size']");
		Assert.assertNotNull(PaperSizeListComboBox);
		if (!PaperSizeListComboBox.getText().toString().contentEquals(paper_size)) 
		{
			log.info("Desired paper size => " + paper_size + " <= is not selected so selecting it from drop down");
			PaperSizeListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(paper_size).click();
				Thread.sleep(2000);
				log.info("Selected desired paper size *****" + PaperSizeListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+paper_size+"\" Paper Size is NOT FOUND so either 1) your Printer does not support \""+paper_size+"\" Paper Size OR 2) you have typed the Paper Size value incorrectly in testsuite xml.");
            	log.info("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+paper_size+"\" Paper Size is NOT FOUND so either 1) your Printer does not support \""+paper_size+"\" Paper Size OR 2) you have typed the Paper Size value incorrectly in testsuite xml.");
				Reporter.log("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				//e.printStackTrace();
				log.info("Error selecting paper size option but continuing test with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				PaperSizeListComboBox.click();
			}				
		} else {
			log.info("Desired paper size => " + PaperSizeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Margins
	public static void SelectPageMargins_Uwp(RemoteWebDriver Session, String page_margins)throws MalformedURLException, InterruptedException {

		WebElement PageMarginListComboBox = Session.findElementByXPath("//ComboBox[contains(@Name, 'Margins')]");
		Assert.assertNotNull(PageMarginListComboBox);
		if (!PageMarginListComboBox.getText().toString().contentEquals(page_margins))
		{
			log.info("Desired Page Margins => " + page_margins + " <= is not selected so selecting it from drop down");
			PageMarginListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(page_margins).click();
				Thread.sleep(2000);
				log.info("Selected desired margins *****" + PageMarginListComboBox.getText().toString() + "*****");		
			} catch (Exception e) {
				log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+page_margins+"\" Page Margin is NOT FOUND so either 1) your Printer does not support \""+page_margins+"\" Page Margin OR 2) you have typed the Page Margin value incorrectly in testsuite xml.");
            	log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+page_margins+"\" Page Margin is NOT FOUND so either 1) your Printer does not support \""+page_margins+"\" Page Margin OR 2) you have typed the Page Margin value incorrectly in testsuite xml.");
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				//e.printStackTrace();
				log.info("Error selecting desired Page Margin but continuing with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				PageMarginListComboBox.click();
			}			
		} else {
			log.info("Desired page margin => " + PageMarginListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select More Options link to access more print options.
	public static int OpenMoreSettings(RemoteWebDriver Session) throws InterruptedException {
		try {
			Session.findElementByName("More settings").click();
			log.info("Clicked 'More Settings' link successfully");
			Thread.sleep(2000);
			return 1;
		} catch (Exception e){
			log.info("More settings Option Not Found");
			return 0;
		}
		
	}

	
	// Method to return from More Options screen to access print button.
	public static void CloseMoreSettings(RemoteWebDriver Session) throws InterruptedException {
		
		Session.findElementByXPath("//Button[@Name = 'Ok']").click();
		Thread.sleep(1000);
		try{
			if (Session.findElementByXPath("//Button[@Name = 'Ok']").isDisplayed()){
				Session.findElementByXPath("//Button[@Name = 'Ok']").click();
				log.info("Clicked 'OK' button twice because of conflicting settings.");
			}else{
				log.info("There were no conflicting settings");
			}
		}catch(NoSuchElementException e){
				log.info("OK button not found again, already clicked");
			}
		Thread.sleep(2000);
		log.info("Clicked 'OK' button successfully.");
	}

	
	// Method to select Color options.
	public static void SelectColorOrMono_Uwp(RemoteWebDriver Session, String color_optn) throws InterruptedException {
					   
		WebElement ColorModeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Color mode']");
		Assert.assertNotNull(ColorModeListComboBox);           
        if(!ColorModeListComboBox.getText().toString().contentEquals(color_optn)) 
        {
	        log.info("Desired photo color mode => "+color_optn+" <= is not selected so selecting it from drop down");
	        ColorModeListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(color_optn).click();
				Thread.sleep(2000);
				log.info("Selected desired Color Option *****" + ColorModeListComboBox.getText().toString() + "*****");
            } catch(Exception e){
				log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+color_optn+"\" Color Options is NOT FOUND so either 1) your Printer does not support \""+color_optn+"\" Color Options OR 2) you have typed the Color Options value incorrectly in testsuite xml.");
            	log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+color_optn+"\" Color Options is NOT FOUND so either 1) your Printer does not support \""+color_optn+"\" Color Options OR 2) you have typed the Color Options value incorrectly in testsuite xml.");
				Reporter.log("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	        	//e.printStackTrace();
	            log.info("Error selecting desired color mode but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            ColorModeListComboBox.click();
            }         
        }else {
			log.info("Desired photo color mode  => " + ColorModeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired duplex option
	public static void SelectDuplexOption_Uwp(RemoteWebDriver Session, String duplex_optn)throws MalformedURLException, InterruptedException {
		
		String duplex_sel = duplex_optn.toLowerCase();
		if (duplex_sel.equals("none")) {
			duplex_sel = "one side";
		} else if(duplex_sel.equals("shortedge")) {
			duplex_sel = "short edge";
		} else {
			duplex_sel = "long edge";
		}

		WebElement DuplexListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Duplex printing']");
		Assert.assertNotNull(DuplexListComboBox);
		if (!DuplexListComboBox.getText().toString().contentEquals(duplex_sel)) 
		{
			log.info("Desired duplex option => " + duplex_sel + " <= is not selected so selecting it from drop down");
			DuplexListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByXPath("//*[contains(@Name,'"+duplex_sel+"')]").click();
				Thread.sleep(1000);
				log.info("Selected desired duplex option *****" + DuplexListComboBox.getText().toString() + " - " +duplex_optn +"*****");
			} catch (Exception e) {
				log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+duplex_optn+"\" Duplex Option is NOT FOUND so either 1) your Printer does not support \""+duplex_optn+"\" Duplex Option OR 2) you have typed the Duplex Option value incorrectly in testsuite xml.");
            	log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+duplex_optn+"\" Duplex Option is NOT FOUND so either 1) your Printer does not support \""+duplex_optn+"\" Duplex Option OR 2) you have typed the Duplex Options value incorrectly in testsuite xml.");
				Reporter.log("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				//e.printStackTrace();
				log.info("Error selecting duplex option but continuing with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				DuplexListComboBox.click();
			}			
		} else {
			log.info("Desired duplex option => " + DuplexListComboBox.getText().toString()	+ " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Borderless Printing Option
	public static void SelectBorderless_Uwp(RemoteWebDriver Session, String borderless) throws MalformedURLException, InterruptedException {

		WebElement BorderlessListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Borderless printing']");
		Assert.assertNotNull(BorderlessListComboBox);
		if (!BorderlessListComboBox.getText().toString().contentEquals(borderless)) 
		{
			log.info("Desired Borderless option => " + borderless + " <= is not selected so selecting it from drop down");
			BorderlessListComboBox.click();
			Thread.sleep(1000);
			try {
				Session.findElementByName(borderless).click();
				Thread.sleep(2000);
				log.info("Selected desired borderless option *****" + BorderlessListComboBox.getText().toString() + "*****");
			} catch (Exception e) {
				log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+borderless+"\" Borderless Option is NOT FOUND so either 1) your Printer does not support \""+borderless+"\" Borderless Option OR 2) you have typed the Borderless Option value incorrectly in testsuite xml.");
            	log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+borderless+"\" Borderless Option is NOT FOUND so either 1) your Printer does not support \""+borderless+"\" Borderless Option OR 2) you have typed the Borderless Options value incorrectly in testsuite xml.");
				Reporter.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				//e.printStackTrace();
				log.info("Error selecting desired Borderless option but continuing with rest of the print options");
				//throw new RuntimeException(e);
				//Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
				BorderlessListComboBox.click();
				
			}		
		} else {
			log.info("Desired Borderless option => " + BorderlessListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}

	
	// Method to select desired Paper Tray Option
	public static void SelectPaperTray_Uwp(RemoteWebDriver Session, String paper_tray) throws InterruptedException {
		   
		WebElement PaperTrayListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Paper tray']");
		Assert.assertNotNull(PaperTrayListComboBox);           
        if(!PaperTrayListComboBox.getText().toString().contentEquals(paper_tray)) 
		{
	        log.info("Desired Paper tray => "+paper_tray+" <= is not selected so selecting it from drop down");
	        PaperTrayListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(paper_tray).click();
				Thread.sleep(2000);
				log.info("Selected desired paper tray option *****" + PaperTrayListComboBox.getText().toString() + "*****");
            } catch(Exception e){
				log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+paper_tray+"\" PaperTray Option is NOT FOUND so either 1) your Printer does not support \""+paper_tray+"\" PaperTray Option OR 2) you have typed the PaperTray Option value incorrectly in testsuite xml.");
            	log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+paper_tray+"\" PaperTray Option is NOT FOUND so either 1) your Printer does not support \""+paper_tray+"\" PaperTray Option OR 2) you have typed the PaperTray Options value incorrectly in testsuite xml.");
				Reporter.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	        	//e.printStackTrace();
	            log.info("Error selecting desired Paper Tray but continuing with rest of the print options");     
				//throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            PaperTrayListComboBox.click();
            }   			
        }else {
			log.info("Desired Paper Tray option => " + PaperTrayListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Paper Type Option
	public static void SelectPaperType_Uwp(RemoteWebDriver Session, String paper_type) throws InterruptedException {
		   
		WebElement PaperTypeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Paper type']");
		Assert.assertNotNull(PaperTypeListComboBox);           
        if(!PaperTypeListComboBox.getText().toString().contentEquals(paper_type)) 
		{
	        log.info("Desired photo paper type => "+paper_type+" <= is not selected so selecting it from drop down");
	        PaperTypeListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(paper_type).click();
				Thread.sleep(2000);
				log.info("Selected desired paper type option *****" + PaperTypeListComboBox.getText().toString() + "*****");
            } catch(Exception e){
				log.info("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+paper_type+"\" PaperType Option is NOT FOUND so either 1) your Printer does not support \""+paper_type+"\" PaperType Option OR 2) you have typed the PaperType Option value incorrectly in testsuite xml.");
            	log.info("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+paper_type+"\" PaperType Option is NOT FOUND so either 1) your Printer does not support \""+paper_type+"\" PaperType Option OR 2) you have typed the PaperType Options value incorrectly in testsuite xml.");
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	        	//e.printStackTrace();
	            log.info("Error selecting desired Paper Type but continuing with rest of the print options");     
				//throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            PaperTypeListComboBox.click();
            }   			
        }else {
			log.info("Desired Paper Type option => " + PaperTypeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Output Quality Option
	public static void SelectOutputQuality_Uwp(RemoteWebDriver Session, String output_qlty) throws InterruptedException {
		   
		WebElement OutputQualityListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Output quality']");
		Assert.assertNotNull(OutputQualityListComboBox);           
        if(!OutputQualityListComboBox.getText().toString().contentEquals(output_qlty)) 
		{
	        log.info("Desired photo output quality => "+output_qlty+" <= is not selected so selecting it from drop down");
	        OutputQualityListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(output_qlty).click();
				Thread.sleep(2000);
				log.info("Selected desired Photo Output Quality option *****" + OutputQualityListComboBox.getText().toString() + "*****");
			} catch(Exception e){
				log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+output_qlty+"\" OutputQuality Option is NOT FOUND so either 1) your Printer does not support \""+output_qlty+"\" OutputQuality Option OR 2) you have typed the OutputQuality Option value incorrectly in testsuite xml.");
            	log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+output_qlty+"\" OutputQuality Option is NOT FOUND so either 1) your Printer does not support \""+output_qlty+"\" OutputQuality Option OR 2) you have typed the OutputQuality Options value incorrectly in testsuite xml.");
				Reporter.log("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	        	//e.printStackTrace();
	            log.info("Error selecting desired Photo Output Quality but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            OutputQualityListComboBox.click();
            }   			
        }else {
			log.info("Desired Photo Output Quality option => " + OutputQualityListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Stapling Option
	public static void SelectStaplingOption_Uwp(RemoteWebDriver Session, String stapling_optn) throws InterruptedException {
		   
		WebElement SelectStaplingOption = Session.findElementByXPath("//ComboBox[@Name = 'Stapling']");
		Assert.assertNotNull(SelectStaplingOption);           
        if(!SelectStaplingOption.getText().toString().contentEquals(stapling_optn)) 
		{
	        log.info("Desired Stapling Option => "+stapling_optn+" <= is not selected so selecting it from drop down");
	        SelectStaplingOption.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(stapling_optn).click();
				Thread.sleep(2000);
				log.info("Selected desired Stapling Option *****" + SelectStaplingOption.getText().toString() + "*****");
            } catch(Exception e){
				log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+stapling_optn+"\" Stapling Option is NOT FOUND so either 1) your Printer does not support \""+stapling_optn+"\" Stapling Option OR 2) you have typed the Stapling Option value incorrectly in testsuite xml.");
            	log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+stapling_optn+"\" Stapling Option is NOT FOUND so either 1) your Printer does not support \""+stapling_optn+"\" Stapling Option OR 2) you have typed the Stapling Options value incorrectly in testsuite xml.");
				Reporter.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	        	//e.printStackTrace();
	            log.info("Error selecting desired Stapling Option but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            SelectStaplingOption.click();
            } 	
        }else {
			log.info("Desired Stapling Option => " + SelectStaplingOption.getText().toString() + " <= is already selected so proceeding");
		}
	}
	
	
	// Method to select desired Header and Footer Options
		public static void SelectHeadersAndFooters_Uwp(RemoteWebDriver Session, String headerandfooter_optn) throws InterruptedException {
			 
			WebElement HeadersAndFooters = Session.findElementByXPath("//ComboBox[@Name = 'Headers and footers']");
			Assert.assertNotNull(HeadersAndFooters);           
	        if(!HeadersAndFooters.getText().toString().contentEquals(headerandfooter_optn)) 
			{
		        log.info("Desired Header and Footer Option => "+headerandfooter_optn+" <= is not selected so selecting it from drop down");
		        HeadersAndFooters.click();
		        Thread.sleep(1000);
		        try {
					//Session.findElementByXPath("//ComboBoxItem[@Name = '"+headerandfooter_optn+"']").click();
		        	Session.findElementByName(headerandfooter_optn).click();
		        	Thread.sleep(2000);
					log.info("Selected desired Header and Footer Option *****" + HeadersAndFooters.getText().toString() + "*****");
	            } catch(Exception e){
					log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	            	log.info("\""+headerandfooter_optn+"\" HeaderFooter Option is NOT FOUND so either 1) your Printer does not support \""+headerandfooter_optn+"\" HeaderFooter Option OR 2) you have typed the HeaderFooter Option value incorrectly in testsuite xml.");
	            	log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	            	
					//e.printStackTrace();
	            	//This is to insert msg to TestNG emailable-report.html
					Reporter.log("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					Reporter.log("\""+headerandfooter_optn+"\" HeaderFooter Option is NOT FOUND so either 1) your Printer does not support \""+headerandfooter_optn+"\" HeaderFooter Option OR 2) you have typed the HeaderFooter Options value incorrectly in testsuite xml.");
					Reporter.log("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		        	//e.printStackTrace();
		            log.info("Error selecting desired Header and Footer Option but continuing with rest of the print options");     
		            //throw new RuntimeException(e);
		            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
		            HeadersAndFooters.click();
	            } 	
	        }else {
				log.info("Desired Header and Footer Option => " + HeadersAndFooters.getText().toString() + " <= is already selected so proceeding");
			}
		} 

		
	// Method to select desired Scaling Option
	public static void SelectScale_Uwp(RemoteWebDriver Session, String scale_optn) throws InterruptedException {
		   
		WebElement ScaleListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Scale']");
		Assert.assertNotNull(ScaleListComboBox);           
        if(!ScaleListComboBox.getText().toString().contentEquals(scale_optn)) 
		{
	        log.info("Desired Scaling Option => "+scale_optn+" <= is not selected so selecting it from drop down");
	        ScaleListComboBox.click();
	        Thread.sleep(1000);
	        try {
				Session.findElementByName(scale_optn).click();
				Thread.sleep(2000);
				log.info("Selected desired Scaling and Footer Option *****" + ScaleListComboBox.getText().toString() + "*****");
            } catch(Exception e){
				log.info("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+scale_optn+"\" Scaling Option is NOT FOUND so either 1) your Printer does not support \""+scale_optn+"\" Scaling Option OR 2) you have typed the Scaling Option value incorrectly in testsuite xml.");
            	log.info("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+scale_optn+"\" Scaling Option is NOT FOUND so either 1) your Printer does not support \""+scale_optn+"\" Scaling Option OR 2) you have typed the Scaling Options value incorrectly in testsuite xml.");
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	        	//e.printStackTrace();
	            log.info("Error selecting desired scaling Option but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            ScaleListComboBox.click();
            } 	
        }else {
			log.info("Desired Scaling Option => " + ScaleListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	} 
	
	
	// Method to select desired Page Range Option
	public static void SelectPageRange_Uwp(RemoteWebDriver Session, String page_range) throws InterruptedException {
		   
		WebElement PageRangeListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Pages']");
		Assert.assertNotNull(PageRangeListComboBox);           
        if(!PageRangeListComboBox.getText().toString().contentEquals(page_range)) 
		{
	        log.info("Desired Page Range => "+page_range+" <= is not selected so selecting it from drop down");
	        PageRangeListComboBox.click();
	        Thread.sleep(1000);
	        try {
				if((!(page_range.equalsIgnoreCase("All pages"))) &&  (!(page_range.equalsIgnoreCase("Current page"))) && (!(page_range.equalsIgnoreCase("All")))){
					
					// Selecting the Custom option for Pages to enter desired page range to print.
					// Pdf has this option as "Page range" and webpage has this option as "Custom range"
					Session.findElementByXPath("//*[contains(@Name,'range')]").click();
					Thread.sleep(2000);
					
					// Clicking on Page Range Edit box.
					// PDF and Webpage have different ID for PageRange Edit Box
					Session.findElementByXPath("//*[@AutomationId = 'com.microsoft.JobCustomPageRange_ValueText' or @AutomationId = 'CustomRangeEdit_ValueText']").click();
					Thread.sleep(2000);

					Session.getKeyboard().pressKey(page_range);
					Session.findElementByName("PrintDialog").click();
					
					Thread.sleep(1000);

					log.info("Entered page range value ***** " + page_range + " *****");
				}else{
					Session.findElementByXPath("//*[contains(@Name,'"+page_range+"')]").click();
					Thread.sleep(2000);
				}
				log.info("Selected desired Page Range *****" +page_range+ "*****");
            } catch(Exception e){
				log.info("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	log.info("\""+page_range+"\" PageRange Option is NOT FOUND so either 1) your Printer does not support \""+page_range+"\" PageRange Option OR 2) you have typed the PageRange Option value incorrectly in testsuite xml.");
            	log.info("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            	
				//e.printStackTrace();
            	//This is to insert msg to TestNG emailable-report.html
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				Reporter.log("\""+page_range+"\" PageRange Option is NOT FOUND so either 1) your Printer does not support \""+page_range+"\" PageRange Option OR 2) you have typed the PageRange Options value incorrectly in testsuite xml.");
				Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	        	//e.printStackTrace();
	            log.info("Error selecting desired Page Range but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            PageRangeListComboBox.click();
            } 	
        }else {
			log.info("Desired Page Range => " + PageRangeListComboBox.getText().toString() + " <= is already selected so proceeding");
		}
	} 
	
	
	 // Method to select desired Collation option  
	 public static void SelectCollation_Uwp(RemoteWebDriver Session, String collation) throws MalformedURLException, InterruptedException {
	 		 
	 	WebElement CollationListComboBox = Session.findElementByXPath("//ComboBox[@Name = 'Collation']"); 		
        Assert.assertNotNull(CollationListComboBox);           
        if(!CollationListComboBox.getText().toString().contentEquals(collation)) 
        {
	        log.info("Desired collation option => "+collation+" <= is not selected so selecting it from drop down");
	        CollationListComboBox.click();
	        Thread.sleep(1000);
	        try {
	        	CollationListComboBox.findElement(By.name(collation)).click();		
	        	Thread.sleep(2000);
		        log.info("Selected collation option *****" +CollationListComboBox.getText().toString()+"*****");
	        	}catch(Exception e){
					log.info("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	            	log.info("\""+collation+"\" Collation Option is NOT FOUND so either 1) your Printer does not support \""+collation+"\" Collation Option OR 2) you have typed the Collation Option value incorrectly in testsuite xml.");
	            	log.info("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	            	
					//e.printStackTrace();
	            	//This is to insert msg to TestNG emailable-report.html
					Reporter.log("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					Reporter.log("\""+collation+"\" Collation Option is NOT FOUND so either 1) your Printer does not support \""+collation+"\" Collation Option OR 2) you have typed the Collation Option value incorrectly in testsuite xml.");
					Reporter.log("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	        	//e.printStackTrace();
	            log.info("Error selecting collation option but continuing with rest of the print options");     
	            //throw new RuntimeException(e);
	            //Clicking again on the ComboBox to close the expanded dropdown in order to access the next option which otherwise is not visible and hence test fails.
	            CollationListComboBox.click();
	        	}		        
	     } else {
	    	log.info("Desired collation option => " +CollationListComboBox.getText().toString()+" <= is already selected so proceeding");
        }
	 }		 		 
		 
}
