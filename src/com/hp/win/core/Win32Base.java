package com.hp.win.core;

import java.net.MalformedURLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;



public class Win32Base extends Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);

    public static WebDriverWait wait;
    
    // Method to select the necessary tab to change the print preference options
    public static void SelectPreferencesTab_Win32(RemoteWebDriver session, String desiredTab) throws InterruptedException {

    	// If the 'OK' button is clickable then other elements in the dialog box are also clickable
        wait = new WebDriverWait(session, 30);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("OK")));
        log.info("Waited for combo box to become clickable.");
        
        try {
            if(session.findElementByName(desiredTab).isSelected()) {
                log.info("'" + desiredTab + "' tab is already selected.");
            }
            else {
                session.findElementByName(desiredTab).click();
                log.info("Successfully clicked on '" + desiredTab + "'.");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
        	log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            log.info("There was a problem clicking on the '" + desiredTab + "' tab.' Please confirm that the tab exists and is clickable when this printer is selected.");
            log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            //throw new RuntimeException(e);
            
          //This is to insert msg to TestNG emailable-report.html 
        	Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    		Reporter.log("There was a problem clicking on '" + desiredTab + "Please confirm that the tab exists" );
    		Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            //throw new RuntimeException(e);
            
            
        }

    }
   
    @SuppressWarnings("unused")
	public static boolean ConfirmGroupExists_Win32(RemoteWebDriver session, String groupName){
        
        boolean exists = false;
        
        try {
            
            WebElement group = session.findElementByName(groupName);
            exists = true;
            
        } catch (Exception e) {
            
            log.info("'" + groupName + "' was not found. Please confirm printer's available settings. If it is a valid setting, please confirm that the setting is seen in the print settings dialog box.");
            return exists;
        
        }
        
        return exists;
    }
    
    public static void ConfirmDialogBox(String device_name, RemoteWebDriver session, String dialogTitle) throws InterruptedException {
        
        // Several elements have the same name, so this list will loop through them and find the correct one (if it exists)
        List<WebElement> titles = session.findElementsByName(dialogTitle);
        
        // If the list is empty, the dialog box is closed
        Assert.assertNotEquals(titles.size(), 0);
        
        for(WebElement title : titles) {
            String type = title.getAttribute("LocalizedControlType").toString();
            if(type.equals("dialog")) {
                Assert.assertNotNull(session.findElementByName(dialogTitle));
                log.info("Confirmed dialog box '" + title.getAttribute("Name").toString() + "' is on top.");
/*                session.getKeyboard().pressKey(Keys.TAB);
                log.info("Successfully used 'Tab' to ensure dialog '" + title.getAttribute("Name").toString() + "' is in focus.");*/
            }
        }
            
            

    }
    
    // Method to select a list item from a combo box drop down menu
    // -- also confirms if the setting is available
    public static void SelectListItem_Win32(RemoteWebDriver dialogSession, String boxName, String listSel, String device_name) throws InterruptedException, MalformedURLException {
       
        // Several elements have the same name, so this list will loop through them and find the correct one (if it exists)
        List<WebElement> nameList = dialogSession.findElementsByName(boxName);      
        
        // If there are elements that have the name we are looking for...
        if(nameList.size() != 0) {

            // Look for those elements in the list
            for (WebElement li : nameList) {
                
                // If we find an element in the list that has the name we are looking for...
                if(li.getAttribute("Name").toString().equals(boxName)) {
                    
                    // If that element is a combo box...
                    if(li.getAttribute("LocalizedControlType").equals("combo box")) {
                        
                        // Click on the combo box
                        try {
                            li.click();
                            log.info("Clicked on '" + li.getAttribute("Name").toString() + "' combo box...");
                            Thread.sleep(1000);
                            
                            // Find the desired list item and click on it
                            //WebElement listItem = dialogSession.findElementByName(listSel);
                            try {
                            	WebElement listItem = dialogSession.findElementByName(listSel);
                            	listItem.click();
                            	log.info("Clicked on '" + listItem.getText().toString() + "'");
                            	Thread.sleep(1000);
                            } catch (Exception e) {
                            	dialogSession.findElementByClassName("SysTreeView32").click();
                            	log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                            	log.info("There was a problem clicking on '" + listSel + "'. Either 1) your Printer does not support '" + listSel + "' OR 2) you have typed the option value incorrectly in testsuite xml.");
                            	log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                
                                //This is to insert msg to TestNG emailable-report.html 
                            	Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        		Reporter.log("There was a problem clicking on '" + listSel + "'. Either 1) your Printer does not support '" + listSel + "' OR 2) you have typed the option value incorrectly in testsuite xml.");
                        		Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                Thread.sleep(1000);
                        		//throw new RuntimeException(e);
                            }
                            break;
                        } catch (Exception e) {
                            log.info("There was a problem clicking on '" + boxName + "'. Continuing with the default selection.");
                            //throw new RuntimeException(e);
                        }
                        
                    }
                
                // If there is an element with the name we're looking for, but there is no combo box...
                } else
                {
                    log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	        		log.info("Cannot find a combo box with the name '" + boxName + "'. Please confirm the printer's settings. If it is a valid setting, please confirm that it is seen in the print settings dialog box.");
	        		log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	        		
	        		//This is to insert msg to TestNG emailable-report.html 
	        		Reporter.log("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	        		Reporter.log("\""+boxName+"\" combo box NOT FOUND so either 1) Your Printer does not support \""+boxName+"\" Option OR 2) You have typed the value incorrectly in testsuite xml");
	        		Reporter.log("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");                    
                }
            }
        
       }        
        // If there are no elements that have the name we're looking for...
        else {
            log.info("'" + boxName + "' is not found in the print settings. Please confirm the printer's settings. If it is a valid setting, please confirm that it is seen in the print settings dialog box.");
            return;
        }
    }

    
    // Method to click a radio button
    public static void SelectRadioButton_Win32(RemoteWebDriver session, String settingsSel, String radioGroup) {
        
        if (ConfirmGroupExists_Win32(session, radioGroup)){
            
        	// Using the 'OK' button's status -- if the 'OK' button is clickable then other elements in the dialog box are clickable
            wait = new WebDriverWait(session, 30);
            wait.until(ExpectedConditions.elementToBeClickable(By.name("OK")));
            log.info("Waited for combo box to become clickable.");
            
            try {
            	session.findElementByXPath("//RadioButton[@Name = '" +settingsSel+ "']").click();
                log.info("Successfully clicked on '" + settingsSel + ".'");
            } catch (Exception e) {
                //e.printStackTrace();
            	log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                log.info("There was a problem clicking on '" + settingsSel + "'. Either 1) your Printer does not support " + settingsSel + " OR 2) you have typed the option value incorrectly in testsuite xml.");
                log.info("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                
                //This is to insert msg to TestNG emailable-report.html 
            	Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        		Reporter.log("There was a problem clicking on '" + settingsSel + "'. Either 1) your Printer does not support " + settingsSel + " OR 2) you have typed the option value incorrectly in testsuite xml.");
        		Reporter.log("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        		//throw new RuntimeException(e);
        	}
        }
        else {
            return;
        }
    }
    
    
    public static void ComboBoxHotkeySelect(RemoteWebDriver session, String boxName, String key, String option, String device_name) throws InterruptedException {

        try {
            List<WebElement> list = session.findElementsByName(boxName);

            if(list.size() > 0) {
                session.getKeyboard().pressKey(Keys.ALT + key);
                session.getKeyboard().releaseKey(key);
                session.getKeyboard().releaseKey(Keys.ALT);
                log.info("Successfully focused on '" + boxName + "' combo box.");

                session.getKeyboard().pressKey(Keys.ARROW_DOWN);
                log.info("Successfully opened '" + boxName + "' list.");


                log.info("Going to try to click on '" + option + "'...");
                try {
                    List<WebElement> listItems = session.findElementsByName(option);
                    if(listItems.size() > 0) {
                    session.findElementByName(option).click();
                    Thread.sleep(1000);
                    log.info("Successfully clicked on '" + option + "'...");
                    }
                    else {
                        log.info("List item size is " + listItems.size() + ".");
                    }

                } catch (Exception e1) {
                    log.info("There was a problem clicking on '\" + option + \"'. Continuing with the default selection.");
                    //throw new RuntimeException(e1);
                }
            }
            else {
                log.info("'" + boxName + "' setting is not found. Please confirm the printer's settings. If it is a valid setting, please confirm that it is seen in the print settings dialog box.");
                return;
            }

        } catch (Exception e) {
            log.info("Could not find '" + boxName + "'. Please confirm the printer's settings. If it is a valid setting, please confirm that it is seen in the print settings dialog box.");
            return;
        }
        
    }
    
    
    // Method to select print option
    public static void ChooseOrientation_Win32(RemoteWebDriver session, String option, String device_name) throws InterruptedException, MalformedURLException {

        SelectListItem_Win32(session, "Orientation: ", option, device_name);

    }


    // Method to select duplex option
    public static void ChooseDuplexOrSimplex_Win32(RemoteWebDriver session, String duplexSel, String device_name) throws InterruptedException, MalformedURLException {

        SelectListItem_Win32(session, "Print on Both Sides: ", duplexSel, device_name);

    }



    // Method to select color option
    public static void ChooseColorOrMono_Win32(RemoteWebDriver session, String color_optn) {
        
        // Test parameters have been paraphrased to prevent confusion around the two '&' in 'Black && White'
        // They are changed to their actual element names here
        String color = "Color";
        String mono = "Black && White";
        String color_choice = null;
        String color_sel = color_optn.toLowerCase();

        if(color_sel.equals("color")){
            color_choice = color;
        }
        else if(color_sel.equals("mono")||color_sel.equals("Black && White")||color_sel.equals("monochrome")) {
            color_choice = mono;
        }else{
        	color_choice = color_sel;
        }
        
        SelectRadioButton_Win32(session, color_choice, "Color");

    }

    
    // Method to select print quality
    public static void ChoosePrintQuality_Win32(RemoteWebDriver session, String qualitySel){
        SelectRadioButton_Win32(session, qualitySel, "Quality Settings");
    }
    

    // Method to select the paper size
    public static void ChoosePaperSize_Win32(RemoteWebDriver session, String size, String device_name) throws InterruptedException, MalformedURLException {   
        
        SelectListItem_Win32(session, "Paper Size: ", size, device_name);

    }
    // Method to select the paper source
    public static void ChoosePaperSource_Win32(RemoteWebDriver session, String source, String device_name) throws InterruptedException, MalformedURLException {   
        
        SelectListItem_Win32(session, "Paper Source: ", source, device_name);

    }
    
    // Method to select the paper type(Media)
    public static void ChoosePaperType_Win32(RemoteWebDriver session, String media, String device_name) throws InterruptedException, MalformedURLException {   
        
        SelectListItem_Win32(session, "Media: ", media, device_name);

    }
}
