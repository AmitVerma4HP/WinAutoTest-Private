package com.hp.win.core;

import java.net.MalformedURLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;



public class Win32Base extends Base {

    private static final Logger log = LogManager.getLogger(Win32Base.class);

    // Method to select the necessary tab to change the print preference options
    public static void SelectPreferencesTab_Win32(RemoteWebDriver session, String desiredTab) throws InterruptedException {

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
            log.info("Error: Tab '" + desiredTab + "' doesn't exist. Use 'Layout' or 'Paper/Quality.'");
            throw new RuntimeException(e);
        }

    }
   
    public static boolean ConfirmGroupExists_Win32(RemoteWebDriver session, String groupName){
        
        boolean exists = false;
        
        try {
            
            WebElement group = session.findElementByName(groupName);
            exists = true;
            
        } catch (Exception e) {
            
            log.info("Printer does not support '" + groupName + "'. Confirm printer's available settings.");
            return exists;
        
        }
        
        return exists;
    }
    
    
    // Method to select a list item from a combo box drop down menu
    // -- also confirms if the setting is available
    public static void SelectListItem_Win32(RemoteWebDriver dialogSession, String boxName, String listSel, String device_name) throws InterruptedException, MalformedURLException {

        // Several elements have the same name, so this list will loop through them and find the correct one (if it exists)
        List<WebElement> nameList = dialogSession.findElementsByName(boxName);

        if(nameList.size() != 0) {
            for (WebElement el : nameList) {
                //log.info("Looking for " + el.getText().toString()); - Use as trace in log to confirm elements in list - EMC
                if(el.getAttribute("Name").toString().equals(boxName)) {
                    if(el.getAttribute("LocalizedControlType").equals("combo box")) {
                        try {
                            log.info("Going to click on '" + el.getAttribute("Name").toString() + "' combo box...");
                            el.click();
                            Thread.sleep(1000);
                            break;
                        } catch (Exception e) {
                            log.info("Unable to click on the combo box.");
                            throw new RuntimeException(e);
                        }
                    }
                } else
                {
                    log.info("Printer does not support '" + boxName + ".'. Confirm printer's available settings.");
                    return;
                }
            }
        
            WebElement listItem = dialogSession.findElementByName(listSel);

            try {
                log.info("Going to click on '" + listItem.getText().toString() + "'");
                listItem.click();
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info("Unable to click on list item.");
                throw new RuntimeException(e);
            }
        }
        else {
            log.info("Printer does not support '" + boxName + ".'. Confirm printer's available settings.");
        }
    }

    
    // Method to click a radio button
    public static void SelectRadioButton_Win32(RemoteWebDriver session, String settingsSel, String radioGroup) {
        
        if (ConfirmGroupExists_Win32(session, radioGroup)){
            try {
                session.findElementByName(settingsSel).click();
                log.info("Successfully clicked on '" + settingsSel + ".'");
            } catch (Exception e) {
                log.info("Couldn't click on '" + settingsSel + "' button.");
                throw new RuntimeException(e);
            }
        }
        else {
            return;
        }
    }

    
    // Method to select print orientation
    public static void ChooseOrientation_Win32(RemoteWebDriver session, String option, String device_name) throws InterruptedException, MalformedURLException {

        SelectListItem_Win32(session, "Orientation: ", option, device_name);

    }


    // Method to select duplex option
    public static void ChooseDuplexOrSimplex_Win32(RemoteWebDriver session, String duplexSel, String device_name) throws InterruptedException, MalformedURLException {

        SelectListItem_Win32(session, "Print on Both Sides: ", duplexSel, device_name);

    }



    // Method to select color option
    public static void ChooseColorOrMono_Win32(RemoteWebDriver session, String color_optn) throws InterruptedException {
        
        // Test parameters have been paraphrased to prevent confusion around the two '&' in 'Black && White'
        // They are changed to their actual element names here
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

        SelectRadioButton_Win32(session, color_choice, "Color");

    }

    
    // Method to select print quality
    public static void ChoosePrintQuality_Win32(RemoteWebDriver session, String qualitySel) {
        SelectRadioButton_Win32(session, qualitySel, "Quality Settings");
    }
    

    // Method to select the paper size
    public static void ChoosePaperSize_Win32(RemoteWebDriver session, String size, String device_name) throws InterruptedException, MalformedURLException {   
        
        SelectListItem_Win32(session, "Paper Size: ", size, device_name);

    }

}
