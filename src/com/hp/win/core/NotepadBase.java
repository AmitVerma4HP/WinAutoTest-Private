package com.hp.win.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;



public class NotepadBase extends Base {
		
		private static final Logger log = LogManager.getLogger(NotepadBase.class);
		public static RemoteWebDriver NotepadSession = null;		
		
		
	  // Method to print from Notepad
		public static void PrintNotePadFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String paper_size) throws InterruptedException {
			// Go to file Menu
			NotepadSession.findElementByName("File").click();
	    	log.info("Clicked on File Menu in Notepad");
	    	Thread.sleep(1000);
	    	
	    	// Save the file
	    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Save\")]").click();
	        log.info("Pressed Save button to Save the File");
	        Thread.sleep(1000);
	         
	        // Go to file Menu
		    NotepadSession.findElementByName("File").click();
		    log.info("Clicked on File Menu in Notepad");
		    Thread.sleep(1000);
		    	
	    	// Tap on Print
	    	NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Print\")]").click();
	    	log.info("Clicked on File -> Print option Successfully");
	    	Thread.sleep(1000);
	    	
	          //Select WiFi Printer
            log.info("Looking for " + ptr_name + "...");
            NotepadSession.findElementByName(ptr_name).click();
            log.info("Selected Printer Successfully");
            Thread.sleep(1000); 
	    	
	    	// Open Preferences window
	    	ClickButton(NotepadSession, "Preferences");
	    	
	    	// Select Preferences
	        ChooseDuplexOrSimplex_Notepad(duplex_optn);
	        ChooseColorOrMono_Notepad(color_optn);
	    	ChooseOrientation_Notepad(orientation);
	    	
	    	ClickButton(NotepadSession, "Advanced");
	    	ChoosePaperSize_Notepad(paper_size);
	    	
	        // Close print option windows
	        ClickButton(NotepadSession, "OK");
	        ClickButton(NotepadSession, "OK");

	    	
	    	//Tap on print icon (Give Print)    	
	    	ClickButton(NotepadSession, "Print");
		}
		
		
		// Method to select the necessary tab to change the print preference options
		public static void SelectPreferencesTab_Notepad(String desiredTab) throws InterruptedException {
		    
		    try {
		        if(NotepadSession.findElementByName(desiredTab).isSelected()) {
		            log.info("'" + desiredTab + "' tab is already selected.");
		        }
		        else {
		            NotepadSession.findElementByName(desiredTab).click();
		            log.info("Successfully clicked on '" + desiredTab + ".'");
		            Thread.sleep(1000);
		        }
		    } catch (Exception e) {
		        log.info("Error: Tab '" + desiredTab + "' doesn't exist. Use 'Layout' or 'Paper/Quality.'");
		        throw new RuntimeException(e);
		    }

		}
		
		
		public static WebElement SelectComboBox_Notepad(String defaultSel) {
            
		    WebElement boxSel = null;
		    
            // get a list of all combo boxes available
            List<WebElement> AllComboBoxList = NotepadSession.findElementsByTagName("ComboBox");
            Assert.assertNotNull(AllComboBoxList);
            
            // Get the last combo box in the list
            int lastIndex = (AllComboBoxList.size() - 1);
            String lastValue = AllComboBoxList.get(lastIndex).getText().toString();
            
            // loop through the combo box list and select the correct combo box
            for(WebElement box : AllComboBoxList) {
                boxSel = box;
                if(box.getText().equals(defaultSel)) {
                    log.info("Going to click on combo box with default value '" + box.getText().toString() + "'...");
                    try {
                        box.click();
                        Thread.sleep(1000);
                        
                        return boxSel;
                        //break;
                        //ChooseDuplexOrSimplex_Old(defaultSel, box);
                    } catch (Exception e) {
                        log.info("Can't click on the combo box.");
                        throw new RuntimeException(e);
                    }
                }
                else if(box.getText().equals(lastValue)) {
                    log.info("Combo box with default value '" + defaultSel + "' is not available for this printer.");
                    break;
                }
                else{
                    log.info("On combo box '" + box.getText().toString() + "'. Going to keep looking.");
                }

            }
            return boxSel;
		}
		
		
		public static boolean Up_SearchListAndSelect_Notepad(String selection, WebElement listItem) throws InterruptedException {
		    
		    boolean found = false;
		    
		    // If the list item is already highlighted, we don't need to search the list for it
		    if(listItem.getText().equals(selection)) {
		        log.info("'" + selection + "' is already selected." );
		        try {
		            listItem.click();
		            Thread.sleep(1000);                     
		            log.info("Successfully clicked on '" + listItem.getText().toString() + ".'");
		            found = true;
		            return found;
                } catch(Exception e)
                {
                    log.info("Couldn't click on '" + selection + ".'");     
                    throw new RuntimeException(e);
                }
		    }
		    
		    log.info("Searching list for '" + selection + "...'");
		    
		    // Search up the list with the up arrow key
		    while(true) {
		        // Get the current list item for comparison to the next list item
		        String currText = listItem.getText();
		        
		        // Move up the list
		        NotepadSession.getKeyboard().sendKeys(Keys.ARROW_UP);
		        String newText = listItem.getText();
		        
		        // Check if we're at the top of the list
		        if(currText.equals(newText)) {
		            log.info("Top of the list has been reached. Going to change directions.");
		            break;
		        }
		        
		        // If we land on the correct list item, click on it to select it
                if(listItem.getText().equals(selection)) {
		            try {
		                listItem.click();
		                Thread.sleep(1000);	                    
		                log.info("Successfully clicked on '" + listItem.getText().toString() + ".'");
		                found = true;
		                break;
		            } catch(Exception e)
		            {
		                log.info("Can't find '" + selection + ".'");     
		                throw new RuntimeException(e);
		            }
		        }

		    }
		    return found;
		}
		
		public static boolean Down_SearchListAndSelect_Notepad(String selection, WebElement listItem) throws InterruptedException {
	           
		    boolean found = false;
	            
	        // If the list item is already highlighted, we don't need to search the list for it
	        if(listItem.getText().equals(selection)) {
	            log.info("'" + selection + "' is already selected." );
	            try {
	                listItem.click();
	                Thread.sleep(1000);                     
	                log.info("Successfully clicked on '" + listItem.getText().toString() + ".'");
	                found = true;
	                return found;
	            } catch(Exception e)
	            {
	                log.info("Couldn't click on '" + selection + ".'");     
	                throw new RuntimeException(e);
	            }
	        }
	            
	        log.info("Searching list for '" + selection + "...'");
	            
	        // Search up the list with the up arrow key
	        while(true) {
	            // Get the current list item for comparison to the next list item
                String currText = listItem.getText();
                
	               // If we land on the correct list item, click on it to select it
                if(currText.equals(selection)) {
                    try {
                        listItem.click();
                        Thread.sleep(1000);                     
                        log.info("Successfully clicked on '" + listItem.getText().toString() + ".'");
                        found = true;
                        break;
                    } catch(Exception e)
                    {
                        log.info("Can't find '" + selection + ".'");     
                        throw new RuntimeException(e);
                    }
                }
	                
	            // Move up the list
	            NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
	            String newText = listItem.getText();
	            // Check if we're at the top of the list
	            if(currText.equals(newText) && !currText.equals(selection)) {
	                log.info("End of the list has been reached. Going to change directions.");
	                break;
	            }
	        }
	        return found;
	    }
	      
	      
		public static void ChooseOrientation_Notepad(String option) throws InterruptedException {	    
		    String orientationDefault = "Portrait";
		    String landscape = "Landscape";
	        String portrait = "Portrait";
		    String optn = option.toLowerCase();
		    String selection = orientationDefault;
		    
		    switch(optn) {
		    case "landscape":
		        selection = landscape;
		        break;
		    case "portrait":
		        selection = portrait;
		        break;
		    default:
		        log.info("Orientation selection is incorrect. Please use 'landscape' or 'portrait.'");
		        return;
		            
		    }
	        
		    // Get the Orientation combo box element
            WebElement box = SelectComboBox_Notepad(orientationDefault);
            
            // If we don't find the desired orientation  option by searching down the list, we will search up the list
            if(box.getText().equals(orientationDefault)) {
                boolean found = Down_SearchListAndSelect_Notepad(selection, box);
                if(found == false) {
                    Up_SearchListAndSelect_Notepad(selection, box);
                }
            }
            else {
                log.info("Moving on to next option...");
            }
		}
		
		
		// Method to select duplex option
		public static void ChooseDuplexOrSimplex_Notepad(String option) throws InterruptedException {
            String duplexDefault = "None"; // A combo box's text value is the value shown in the box - "None" is the default value for duplex - EMC
            String simplex = "None";
            String shortEdge = "Flip on Short Edge";
            String longEdge = "Flip on Long Edge";
            String optn = option.toLowerCase();
            
            String selection = duplexDefault;
            switch(optn) {
            case "simplex":
                selection = simplex;
                break;
            case "long edge":
                selection = longEdge;
                break;
            case "short edge":
                selection = shortEdge;
                break;
            default:
                log.info("Duplex selection is incorrect. Please use 'simplex,' 'long edge,' or 'short edge.'");
                return;
            }

            
            // Make sure we're on the correct preferences tab
		    SelectPreferencesTab_Notepad("Layout");
		    
		    // Get the Duplex combo box element
            WebElement box = SelectComboBox_Notepad(duplexDefault);
            
            // If we don't find the desired duplex option by searching down the list, we will search up the list
            if(!box.equals(null)) {
                boolean found = Down_SearchListAndSelect_Notepad(selection, box);
                if(found == false) {
                    Up_SearchListAndSelect_Notepad(selection, box);
                }
            }
            }

		
		
		public static void ChooseColorOrMono_Notepad(String color_optn) throws InterruptedException {
		    String color = "Color";
		    String mono = "Black && White";
		    String color_choice;
		    
		    String color_sel = color_optn.toLowerCase();
		    
		    if(color_sel.equals("mono")) {
		        color_choice = mono;
		    }
		    else {
		        color_choice = color;
		    }
		    
		    SelectPreferencesTab_Notepad("Paper/Quality");
		    
	          try {
	                if(NotepadSession.findElementByName(color_choice).isSelected()) {
	                    log.info("'" + color_optn + "' is already selected.");
	                }
	                else {
	                    NotepadSession.findElementByName(color_choice).click();
	                    log.info("Successfully clicked on '" + color_choice + ".'");
	                }
	            } catch (Exception e) {
	                log.info("Error: Tab '" + color_choice + "' doesn't exist. Use 'color' or 'mono.'");
	                throw new RuntimeException(e);
	            }
		    
		}

		
		// Method to select the paper size
		public static void ChoosePaperSize_Notepad(String size) throws InterruptedException{   
            String paperSizeDefault = "Letter"; // A combo box's text value is the value shown in the box - "None" is the default value for duplex - EMC
            WebElement box = SelectComboBox_Notepad(paperSizeDefault);
            
            // If we don't find the desired paper size option by searching down the list, we will search up the list
            if(box.getText().equals(paperSizeDefault)) {
                boolean found = Down_SearchListAndSelect_Notepad(size, box);
                if(found == false) {
                    Up_SearchListAndSelect_Notepad(size, box);
                }
            }
            else {
                log.info("Moving on to next option...");
            }
		}

		
		// Method to open Notepad test file
		public static RemoteWebDriver OpenNoteFile(String device_name, String test_filename) throws MalformedURLException {
			
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
		   		}catch(Exception e){
	            e.printStackTrace();
	            log.info("Error opening notepad file");
	            throw new RuntimeException(e);
	        	}
		 log.info("Opened"+test_filename+"file from "+testfiles_loc);
		 return NotepadSession;
	  }

}
