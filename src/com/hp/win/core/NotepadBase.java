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
		public static void PrintNotePadFile(String ptr_name, String duplex_optn, String color_optn) throws InterruptedException {
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
	    	
	    	// Open Preferences window
	    	ClickButton(NotepadSession, "Preferences");
	    	
	    	// Select Preferences
	        ChooseDuplexOrSimplex_Notepad(duplex_optn);
	        ChooseColorOrMono_Notepad(color_optn);
	        
	        // Close Preferences window
	        ClickButton(NotepadSession, "OK");
	    	
	    	//Select WiFi Printer
	        log.info("Looking for " + ptr_name + "...");
	    	NotepadSession.findElementByName(ptr_name).click();
	    	log.info("Selected Printer Successfully");
	    	Thread.sleep(1000); 
	    	
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
		
		
		// Method to select duplex option
		public static void ChooseDuplexOrSimplex_Notepad(String option) throws InterruptedException {
            SelectPreferencesTab_Notepad("Layout");
            
            // get a list of all combo boxes available
            List<WebElement> DuplexComboBoxList = NotepadSession.findElementsByTagName("ComboBox");
            Assert.assertNotNull(DuplexComboBoxList);
            
            // iterate through the combo box list and select the correct combo box (the duplex one in this case)
            for(WebElement el : DuplexComboBoxList) {
                if(!el.getText().equals("None")) {
                    log.info("Trying to click on " + DuplexComboBoxList.get(1).getText().toString());
                    try {
                        WebElement duplex = DuplexComboBoxList.get(1); // 1 is the current index for the duplex combo box
                        duplex.click();
                        Thread.sleep(1000);
                        ChooseDuplexOrSimplex_Old(option, duplex);
                    } catch(Exception e)
                    {
                        log.info("Can't click on duplex combo box.");    
                        throw new RuntimeException(e);
                    }
                    break;
                }
                else {
                    ChooseDuplexOrSimplex_Old(option, el);
                    break;
                }
                }
            }
        
		// This is the original method to select a duplex option - it has become a helper function for now
	    // NOTE: Cannot currently select list items from the combo box's drop down menu
        // A workaround is to use the arrow keys to navigate to the selection
        // This issue has been already reported here: https://github.com/Microsoft/WinAppDriver/issues/389
        public static void ChooseDuplexOrSimplex_Old(String option, WebElement duplex) throws InterruptedException{
            
            String optn = option.toLowerCase();
            String simplex = "None";
            String shortEdge = "Flip on Short Edge";
            String longEdge = "Flip on Long Edge";
            
            switch(optn) {
            case "simplex":
                log.info("Selected '" + simplex + "'...");
                duplex.click();
                String comboBoxText = duplex.getText();
                Assert.assertEquals(comboBoxText, simplex);
                Thread.sleep(1000);
                break;
                
            case "long edge":
                log.info("Navigating to '" + longEdge + "'...");
                try {
                    NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
                    duplex.click();
                    Thread.sleep(1000);
                    
                    // Make sure the option we want is selected
                    comboBoxText = duplex.getText();
                    Assert.assertEquals(comboBoxText, longEdge);
                    
                    log.info("Selected '" + comboBoxText + ".'");
                    Thread.sleep(1000);
                } catch(Exception e)
                {
                    log.info("Can't find '" + longEdge + ".'");     
                    throw new RuntimeException(e);
                }
                break;
                
            case "short edge":
                log.info("Navigating to " + shortEdge + "...");
                try {
                    NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
                    NotepadSession.getKeyboard().sendKeys(Keys.ARROW_DOWN);
                    duplex.click();
                    Thread.sleep(1000);

                    comboBoxText = duplex.getText();
                    Assert.assertEquals(comboBoxText, shortEdge);
                    log.info("Selected '" + comboBoxText + ".'");
                    Thread.sleep(1000);
                    
                } catch(Exception e)
                {
                    log.info("Can't find '" + shortEdge + ".'");    
                    throw new RuntimeException(e);
                }
                Thread.sleep(1000);
                break;
                
            default:
                log.info("Invalid duplex selection. Please use 'simplex,' 'long edge,' or 'short edge.'");
            
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
