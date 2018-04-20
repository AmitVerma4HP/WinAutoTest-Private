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
    public static void PrintNotePadFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String paper_size, String device_name) throws InterruptedException, MalformedURLException  {
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

        RemoteWebDriver PreferencesSession = GetDesktopSession(device_name);
        // Select Preferences on the Layout tab first
        ChooseDuplexOrSimplex_Notepad(PreferencesSession, duplex_optn, device_name);
        ChooseOrientation_Notepad(PreferencesSession, orientation, device_name);

        // Select settings on Paper/Quality tab after the Layout tab
        ChooseColorOrMono_Notepad(PreferencesSession, color_optn);

        RemoteWebDriver AdvancedSession = GetDesktopSession(device_name);
        // Now open the Advanced settings
        ClickButton(PreferencesSession, "Advanced");
        ChoosePaperSize_Notepad(AdvancedSession, paper_size, device_name);

        // Close print option windows
        ClickButton(AdvancedSession, "OK");
        ClickButton(PreferencesSession, "OK");


        //Tap on print icon (Give Print)    	
        ClickButton(PreferencesSession, "Print");
    }


    // Method to select the necessary tab to change the print preference options
    public static void SelectPreferencesTab_Notepad(RemoteWebDriver session, String desiredTab) throws InterruptedException {

        try {
            if(session.findElementByName(desiredTab).isSelected()) {
                log.info("'" + desiredTab + "' tab is already selected.");
            }
            else {
                session.findElementByName(desiredTab).click();
                log.info("Successfully clicked on '" + desiredTab + ".'");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.info("Error: Tab '" + desiredTab + "' doesn't exist. Use 'Layout' or 'Paper/Quality.'");
            throw new RuntimeException(e);
        }

    }


    // Method to select a list item from a combo box drop down menu
    public static void SelectListItem_Notepad(RemoteWebDriver DialogSession, String boxName, String listSel, String device_name) throws InterruptedException, MalformedURLException {

        //RemoteWebDriver DialogSession = GetDesktopSession(device_name);

        // Several elements have the same name, so this list will loop through them and find the correct one (if they exist)
        List<WebElement> nameList = DialogSession.findElementsByName(boxName);

        if(nameList.size() != 0) {
            for (WebElement el : nameList) {
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
                    log.info("Printer does not support '" + boxName + "'.");
                    return;
                }
            }

            WebElement listItem = DialogSession.findElementByName(listSel);

            try {
                log.info("Found option " + listItem.getText().toString() + "' with text '" + listItem.getText().toString() + "'");
                listItem.click();
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info("Unable to click on list item.");
                throw new RuntimeException(e);
            }
        }
        else {
            log.info("Print setting " + listSel + " is not available for this printer.");
        }
    }


    public static String GetDefaultPaperSize(List<WebElement> boxList) {
        String defaultPaperSize = "Letter";
        for(WebElement el : boxList) {
            switch (el.getText().toString()){
            case "A3":
                defaultPaperSize = "A3";
                break;
            case "A4":
                defaultPaperSize = "A4";
                break;
            case "A5":
                defaultPaperSize = "A5";
                break;
            case "A6":
                defaultPaperSize = "A6";
                break;
            case "B4 (JIS)":
                defaultPaperSize = "B4 (JIS)";
                break;
            case "B5 (JIS)":
                defaultPaperSize = "B5 (JIS)";
                break;
            case "Executive":
                defaultPaperSize = "Executive";
                break;
            case "Japanese Postcard":
                defaultPaperSize = "Japanese Postcard";
                break;
            case "Legal":
                defaultPaperSize = "Legal";
                break;
            case "Letter":
                //defaultPaperSize = "Letter";
                break;
            case "North America 4x 6":
                defaultPaperSize = "North America 4x 6";
                break;
            case "North America 5x 7":
                defaultPaperSize = "North America 5x 7";
                break;
            case "North America 5x 8":
                defaultPaperSize = "North America 5x 8";
                break;
            case "Statement":
                defaultPaperSize = "Statement";
                break;
            case "Tabloid":
                defaultPaperSize = "Tabloid";
                break;
            default:
                log.info("You are looking for a paper size that doesn't exist...");

            }
        }
        return defaultPaperSize;
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


    public static void ChooseOrientation_Notepad(RemoteWebDriver session, String option, String device_name) throws InterruptedException, MalformedURLException {

        SelectListItem_Notepad(session, "Orientation: ", option, device_name);

    }


    // Method to select duplex option


    public static void ChooseDuplexOrSimplex_Notepad(RemoteWebDriver session, String duplexSel, String device_name) throws InterruptedException, MalformedURLException {

        SelectListItem_Notepad(session, "Print on Both Sides: ", duplexSel, device_name);

    }



    public static void ChooseColorOrMono_Notepad(RemoteWebDriver session, String color_optn) throws InterruptedException {
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

        SelectPreferencesTab_Notepad(session, "Paper/Quality");

        try {
            if(session.findElementByName(color_choice).isSelected()) {
                log.info("'" + color_optn + "' is already selected.");
            }
            else {
                session.findElementByName(color_choice).click();
                log.info("Successfully clicked on '" + color_choice + ".'");
            }
        } catch (Exception e) {
            log.info("Error: Tab '" + color_choice + "' doesn't exist. Use 'color' or 'mono.'");
            throw new RuntimeException(e);
        }

    }


    // Method to select the paper size
    public static void ChoosePaperSize_Notepad(RemoteWebDriver session, String size, String device_name) throws InterruptedException, MalformedURLException {   

        SelectListItem_Notepad(session, "Paper Size: ", size, device_name);

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
