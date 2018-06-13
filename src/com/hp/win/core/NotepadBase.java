package com.hp.win.core;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hp.win.core.ClassicBase;
import com.hp.win.utility.ScreenshotUtility;

import io.appium.java_client.windows.WindowsDriver;


@Listeners({ScreenshotUtility.class})
public class NotepadBase extends ClassicBase {

    private static final Logger log = LogManager.getLogger(ClassicBase.class);
    @SuppressWarnings("rawtypes")
	public static WindowsDriver   NotepadSession     = null;
    public static RemoteWebDriver PrintDialogSession = null;
    public static RemoteWebDriver PreferencesSession = null;
    public static RemoteWebDriver AdvancedSession    = null;
    public static RemoteWebDriver ConflictSession = null;


    // Method to open Notepad test file
    @SuppressWarnings("rawtypes")
	public static WindowsDriver OpenNoteFile(String device_name, String test_filename) throws MalformedURLException {

        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
            capabilities.setCapability("appArguments",test_filename );
            capabilities.setCapability("appWorkingDir", testfiles_loc);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName",device_name);
            NotepadSession = new WindowsDriver(new URL(WindowsApplicationDriverUrl), capabilities);   
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
    
    
    // Method to print from Notepad
    public static void PrintNotePadFile(String ptr_name, String orientation, String duplex_optn, String color_optn, String prnt_quality, String paper_size, String borderless, String paper_type, String paper_tray, String copies, String collation, String page_range, String pages_per_sheet, String device_name) throws InterruptedException, MalformedURLException  {

        // Store all parameters in a Hash Map to make them easier to find
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("orientation", orientation);
        parameters.put("duplex", duplex_optn);
        parameters.put("color", color_optn);
        parameters.put("printQuality", prnt_quality);
        parameters.put("paperSize", paper_size);
        parameters.put("borderless", borderless);
        parameters.put("paperType", paper_type);
        parameters.put("paperTray", paper_tray);
        parameters.put("copies", copies);
        parameters.put("collation", collation);
        parameters.put("pageRange", page_range);
        parameters.put("pagesPerSheet", pages_per_sheet);    	
    	
        // Maximize the Notepad window to prevent false errors if Notepad is partially off-screen
        NotepadSession.getKeyboard().sendKeys(Keys.COMMAND, Keys.ARROW_UP);
        NotepadSession.getKeyboard().releaseKey(Keys.ARROW_UP);
        NotepadSession.getKeyboard().releaseKey(Keys.COMMAND);
        
        // Go to file Menu
        NotepadSession.findElementByName("File").click();
        log.info("Clicked on File Menu in Notepad");
        Thread.sleep(1000);

        // Tap on Print
        NotepadSession.findElementByXPath("//MenuItem[starts-with(@Name, \"Print\")]").click();
        log.info("Clicked on File -> Print option Successfully");
        Thread.sleep(1000);

        log.info("Opening PrintDialogSession...");
        PrintDialogSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PrintDialogSession);
        
        //Select WiFi Printer
        log.info("Looking for '" + ptr_name + "'...");
        if(PrintDialogSession.findElementByName(ptr_name).isSelected()) {
            log.info("'" + ptr_name + "' is already selected.");
        }
        else {
            try {
                PrintDialogSession.findElementByName(ptr_name).click();
                log.info("Clicked on '" + ptr_name + "' successfully.");
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info("Printer under test is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name incorrectly in testsuite xml");
                throw new RuntimeException(e);
            }
        }
        
        // Get printer status and change settings on initial Print dialog box
        List<WebElement> editBoxes = PrintDialogSession.findElementsByClassName("Edit");
        for(WebElement box : editBoxes) {
        	
        	if((box.getAttribute("Name").equals("Status:")) && (!box.getText().toString().equals("Ready"))){
        		log.info("Printer is not in a 'Ready' state!");
        		log.info("Printer status is '" + box.getText().toString() + "'.");
        	}
        	
        	// Set the number of copies to print
        	// The number of copies always resets to 1 after Notepad is closed and reopened, so the element can be found by this number
        	if(box.getText().toString().equals("1")) {
        		try {
        			box.click();
        			box.sendKeys(Keys.BACK_SPACE);
        			box.sendKeys(parameters.get("copies"));
        			log.info("Successfully set number of copies to " + parameters.get("copies"));
        		} catch (Exception e) {
        			log.info("Couldn't enter number of copies. Using default amount of " + box.getText().toString() + "'.");
        		}
        		Thread.sleep(1000);
        		// If the number of desired copies is more than one and collation is requested, check the collated box
        		if((!parameters.get("copies").equals("1")) && (parameters.get("collation").equals("Collated"))) {
        			ClickButton(PrintDialogSession, "Collate");
        		}
        		// TODO: Add PageRange selection here??
        		break;
        	}
        }


        // Open Preferences window
        ClickButton(PrintDialogSession, "Preferences");

        // A new desktop session must be created every time a dialog box is created or destroyed
        // The previous desktop session should be quit first
        try {
            PrintDialogSession.quit();
            log.info("Closed PrintDialogSession...");
        } catch (Exception e) {
            log.info("PrintDialogSession already terminated.");
        }

        // After the previous desktop session was quit, a new desktop session can be created
        log.info("Opening PreferencesSession...");
        PreferencesSession = GetDesktopSession(device_name);
        Assert.assertNotNull(PreferencesSession);       
               
        // Create a hashmap of all tabs to check which ones are available
    	HashMap<String, String> tabs = new HashMap<String, String>();

    	List<WebElement> tabsList = PreferencesSession.findElementsByTagName("TabItem");
    	for(WebElement tab : tabsList) {
    		String value = tab.getText().toString();
    		String key = value;
    		tabs.put(key, value);
    	}
        
        
    	// WPF print settings windows have a 'Printing Shortcuts' tab, but Win32 print settings dialog boxes do not
    	if(tabs.get("Printing Shortcuts") != null) {
    		
        	log.info("WPF UI detected.");
        	
            RunWPFTest(PreferencesSession, tabs, parameters, device_name, ptr_name); 
            
            try {
/*                PreferencesSession.findElementByXPath("//Button[starts-with(@AutomationId, 'OkButton')]").click();
                log.info("Successfully clicked on OK button.");*/
            	PreferencesSession.findElementByXPath("//Button[starts-with(@AutomationId, 'CancelButton')]").click();
            	log.info("Successfully clicked on Cancel button.");
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info("Could not click on OK button.");
                throw new RuntimeException(e);
            }
            
        	ConflictSession = Base.GetDesktopSession(device_name);
        	Assert.assertNotNull(ConflictSession);
            
        	log.info("Checking for print settings conflicts...");
        	try {
        		if(ConflictSession.findElementByXPath("//Text[contains(@Name, 'Selection conflicts were encountered. Would you like them auto-resolved?')]").isDisplayed()) {
        		boolean conflict = ConflictSession.findElementByXPath("//Text[contains(@Name, 'Selection conflicts were encountered. Would you like them auto-resolved?')]").isDisplayed();	
        		log.info("Conflict is '" + conflict + "'. One or more conflicts was found. Resolving all conflicts automatically. Compare printer output to desired settings.");
        			ClickButton(ConflictSession, "Yes");
        		}
        		else {
        			log.info("No print settings conflicts found.");
        		}
        	} catch (Exception e) {
        		log.info("There was a problem resolving the print settings conflict.");
        	}

    	}
    	else {
    		
        	log.info("Win32 UI detected.");
        	
            RunWin32Test(PreferencesSession, tabs, parameters, device_name);
    	}
    	
    	// Quit the session used to detect print settings conflicts
    	if(ConflictSession != null) {
    		try {
    			ConflictSession.quit();
    			log.info("Successfully quit ConflictSession.");
    		} catch (Exception e) {
    			log.info("ConflictSession already quit.");
    		}
    	}
    	
    	// Reopening PreferencesSession to complete the test
    	PreferencesSession = ClassicBase.GetDesktopSession(device_name);
    	Assert.assertNotNull(PreferencesSession);
    	
        ClickButton(PreferencesSession, "Print");
        
    }
   
    
    public static void RunWin32Test(RemoteWebDriver session, HashMap<String, String> tabs, HashMap<String, String> parameters, String device_name) throws InterruptedException, MalformedURLException {
        
/*        // Change the settings on the Layout tab
    	ClassicBase.SelectPreferencesTab_Classic(session, tabs.get("Layout").toString());
        
        // Select duplex option
        ChooseDuplexOrSimplex_Win32(session, parameters.get("duplex").toString(), device_name);
        
        // Select orientation
        ChooseOrientation_Win32(session, parameters.get("orientation").toString(), device_name);


        // Change the settings on the Paper/Quality tab
        ClassicBase.SelectPreferencesTab_Classic(session, tabs.get("Paper/Quality").toString());
        
        // Select color option
        log.info("Selecting color choice.");
        log.info("Using '" + parameters.get("color"));
        ChooseColorOrMono_Win32(session, parameters.get("color").toString());
        
        // Select print quality
        log.info("Selecting print quality.");
        log.info("Using " + parameters.get("printQuality"));
        ChoosePrintQuality_Win32(session, parameters.get("printQuality").toString());*/
        
        // Now open the Advanced settings
        ClickButton(session, "Advanced...");
        
        // Close the session for the Preferences dialog box
        try {
            session.quit();
            log.info("Closed " + session + "...");
        } catch (Exception e) {
            log.info("'" + session + "' already terminated.");
        }
        
        
        // Open a session for the Advanced dialog box
        log.info("Opening AdvancedSession...");
        AdvancedSession = GetDesktopSession(device_name);
        Assert.assertNotNull(AdvancedSession);
        
        //ChoosePaperSize_Win32(AdvancedSession, parameters.get("paperSize"), device_name);
        
        ChooseBorderlessOption_Win32(AdvancedSession, parameters.get("borderless"), device_name);
        
        //Quitting Advanced dialog
        ClickButton(AdvancedSession, "OK");
        
        //Quitting Preferences dialog
        ClickButton(AdvancedSession, "OK");
       
        //Quitting Advanced session in order to open new desktop session to check for printer conflict popup dialog
    	try {
    		AdvancedSession.quit();
    	} catch (Exception e) {
    		log.info("AdvancedSession already closed.");
    	}
    	
    	// TODO: SELECT BORDERLESS OPTION
        
    	ConflictSession = Base.GetDesktopSession(device_name);
    	Assert.assertNotNull(ConflictSession);
    	
    	log.info("Checking for print settings conflicts...");
        	try {
        		        		
        		//if(ConflictSession.findElementByName("Resolve all conflicts for me automatically.").isSelected()) {
        		if(ConflictSession.findElementByName("OK").isDisplayed()) {
        			/*        			log.info("One or more conflicts was found. Resolving all conflicts automatically. Compare printer output to desired settings.");
        			ClickButton(ConflictSession, "OK");
        		}
        		else {*/
        			ConflictSession.findElementByName("Resolve all conflicts for me automatically.").click();
        			log.info("One or more conflicts was found. Resolving all conflicts automatically. Compare printer output to desired settings.");
        			ClickButton(ConflictSession, "OK");
        		}
        		else {
        			log.info("No print settings conflicts found.");
        		}
        	} catch (Exception e) {
        		log.info("There was a problem resolving the print settings conflicts.");
        	}

        	
    }

    
    public static void RunWPFTest(RemoteWebDriver session, HashMap<String, String> tabs, HashMap<String, String> parameters, String device_name, String ptr_name) throws InterruptedException, MalformedURLException {

        // Ensure correct printer selected
    	// TODO: The "contains" method returns a false positive - needs fixing
        Assert.assertTrue(session.findElementByClassName("Window").getAttribute("Name").equals(ptr_name));
        log.info("Opened printer settings is correct for the printer => " + ptr_name);

        /*  
         *  The "Printing Shortcuts" tab will not be used to test printer settings
         *  so that there can be greater accuracy with the settings
         */

        if(tabs.get("Paper/Quality") != null) {
        	SelectPreferencesTab_Classic(session, tabs.get("Paper/Quality"));

        	// Get a hash map of all text elements on the tab in order to confirm which settings are on the tab
        	HashMap<String, String> settings = GetHashMap(session, "TextBlock");
        	SetPaperQualityTabOptions(session, parameters, settings, tabs, device_name);
        }
        
        // Sometimes duplex settings are on a "Layout" tab
        if((tabs.get("Layout") != null)) {
        	SelectPreferencesTab_Classic(session, tabs.get("Layout"));
        	HashMap<String, String> settings = GetHashMap(session, "TextBlock");
        	SetLayoutFinishingTabOptions(session, parameters, settings, tabs, device_name);
        }
        
        // Sometimes duplex settings are on a "Finishing" tab
        if((tabs.get("Finishing") != null)) {
        	SelectPreferencesTab_Classic(session, tabs.get("Finishing"));

        	HashMap<String, String> settings = GetHashMap(session, "TextBlock");

        	SetLayoutFinishingTabOptions(session, parameters, settings, tabs, device_name);
        }
        
        
        // Sometimes color settings are on a separate "Color" tab
        if((tabs.get("Color") != null)) {
        	SelectPreferencesTab_Classic(session, tabs.get("Color"));
        	HashMap<String, String> settings = GetHashMap(session, "TextBlock)");
        	SetColorTabOptions(session, parameters, settings, tabs, device_name);
        }


    }
    
    
    public static void SetPaperQualityTabOptions(RemoteWebDriver session, HashMap<String, String> parameters, HashMap<String, String> settings, HashMap<String, String> tabs, String device_name) throws InterruptedException {

    	// Paper Size:
    	if((settings.get("paper size") != null) || (settings.get("paper sizes") != null)) {
    		log.info("Selecting paper size...");
    		SelectListItem_WPF(session, "cboPageMediaSize", parameters.get("paperSize"), device_name);
    	}

    	//TODO: (Custom button?)

    	// Paper tray selection
    	if((settings.get("paper tray") != null) || (settings.get("paper source") != null)) {
    		log.info("Selecting input tray...");
    		SelectListItem_WPF(session, "cboDocumentInputBin", parameters.get("paperTray"), device_name);
    	}

    	// Paper type
    	if(settings.get("paper type") != null) {
    		log.info("Selecting paper type...");
    		SelectListItem_WPF(session, "cboPageMediaType", parameters.get("paperType"), device_name);
    	}

    	// Borderless option
    	if(settings.get("borderless printing") != null) {
    		log.info("Selecting borderless option...");
    		SelectListItem_WPF(session, "cboBorderless", parameters.get("borderless"), device_name);
    	}
    	
    	// Print quality selection
    	if(settings.get("print quality") != null) {
    		log.info("Selecting print quality...");
    		SelectListItem_WPF(session, "cboResolution", parameters.get("printQuality"), device_name);
    	}

    	// Some printer settings windows have a separate 'Color' tab, others have the color setting on the Paper/Quality tab
    	if(tabs.get("Color") == null) {
    		// Color selection - colorSel variable must be set for actual values in UI based on input from test parameters
    		String colorSel = null;
    		if(settings.get("print in grayscale") != null) {
    			log.info("Selecting color option...");
    			if((parameters.get("color").toLowerCase().equals("color")) || (parameters.get("color").toLowerCase().equals("off"))) {
    				colorSel = "Off";
    			}
    			else if((parameters.get("color").toLowerCase().equals("blackandwhite")) || (parameters.get("color").toLowerCase().equals("black ink only"))) {
    				colorSel = "Black Ink Only";
    			}
    			else if(parameters.get("color").toLowerCase().equals("grayscale")) {
    				colorSel = "High Quality Grayscale";
    			}
    			else {
    				log.info("Color option '" + parameters.get("color") + "' is not recognized. Please use 'Off', 'Black Ink Only', or 'Grayscale'.");
    			}
    		}
    		SelectListItem_WPF(session, "cboPrintInGrayscale", colorSel, device_name);
    	}

    }
    
    
    public static void SetLayoutFinishingTabOptions(RemoteWebDriver session, HashMap<String, String> parameters, HashMap<String, String> settings, HashMap<String, String> tabs, String device_name) throws InterruptedException {

    	// If there is a duplex option, make a selection
    	if(settings.get("print on both sides") != null) {
    		SelectDuplexOption(session, parameters,settings, tabs);
    	}
    	
    	// If there is an orientation option, make a selection
    	if(settings.get("orientation") != null) {
    		SelectOrientation(session, parameters, settings, tabs);
    	}
    }
    

    public static void SetColorTabOptions(RemoteWebDriver session, HashMap<String, String> parameters, HashMap<String, String> settings, HashMap<String, String> tabs, String device_name) throws InterruptedException {
    	// Color selection - colorSel variable must be set for actual values in UI based on input from test parameters
    	String colorSel = null;
    	log.info("Selecting color option...");
    	if((parameters.get("color").toLowerCase().equals("color")) || (parameters.get("color").toLowerCase().equals("off"))) {
    		colorSel = "Off";
    	}
    	else if((parameters.get("color").toLowerCase().equals("blackandwhite")) || (parameters.get("color").toLowerCase().equals("black ink only"))) {
    		colorSel = "Black Ink Only";
    	}
    	else if(parameters.get("color").toLowerCase().equals("grayscale")) {
    		colorSel = "High Quality Grayscale";
    	}
    	else {
    		log.info("Color option '" + parameters.get("color") + "' is not recognized. Please use 'Off', 'Black Ink Only', or 'Grayscale'.");
    	}
    	SelectListItem_WPF(session, "cboPrintInGrayscale", colorSel, device_name);
    }
    
    
    public static void SelectOrientation(RemoteWebDriver session, HashMap<String, String> parameters, HashMap<String, String> settings, HashMap<String, String> tabs) throws InterruptedException {
		if(parameters.get("orientation").equals("Portrait")) {
			try {
				session.findElementByXPath("//RadioButton[@AutomationId='rdoPortrait']").click();
				log.info("Successfully selected 'Portrait'.");
			} catch (Exception e) {
				log.info("There was a problem selecting 'Portrait'.");
			}
			Thread.sleep(1000);
		}
		else if(parameters.get("orientation").equals("Landscape")) {
			try {
				session.findElementByXPath("//RadioButton[@AutomationId='rdoLandscape']").click();
				log.info("Successfully selected 'Landscape'.");
			} catch (Exception e) {
				log.info("There was a problem selecting 'Landscape'.");
			}
		}
		else {
			log.info("Orientation selection not recognized. Select 'Portrait' or 'Landscape.'");
		}
    }
    
    
    public static void SelectDuplexOption(RemoteWebDriver session, HashMap<String, String> parameters, HashMap<String, String> settings, HashMap<String, String> tabs) throws InterruptedException {
		log.info("Selecting duplex option...");
		
		if(parameters.get("duplex").equals("None")) {
			if(session.findElementByXPath("//CheckBox[@AutomationId='chkPrintOnBothSides']").isSelected()) {
				ClassicBase.ClickCheckbox_WPF(session, "Print on Both Sides:");
			}
			log.info("Document will print on one side of paper.");
		}
		
		else if(parameters.get("duplex").equals("Flip on Long Edge")) {   			
			// If the Print on Both Sides checkbox is not checked, check it
			if(!session.findElementByXPath("//CheckBox[@AutomationId='chkPrintOnBothSides']").isSelected()) {
				ClassicBase.ClickCheckbox_WPF(session, "chkPrintOnBothSides");
			}
			
			// If the Flip Pages Up checkbox is checked, then uncheck it
			if(session.findElementByXPath("//CheckBox[@AutomationId='chkFlipPagesUp']").isSelected()){
					ClassicBase.ClickCheckbox_WPF(session, "chkFlipPagesUp");
			}
			
			log.info("'Print on Both Sides' is selected. Document will flip on long edge.");
		}
		
		else if(parameters.get("duplex").equals("Flip on Short Edge")) {
			// If the Print on Both Sides checkbox is not checked, check it
			if(!session.findElementByXPath("//CheckBox[@AutomationId='chkPrintOnBothSides']").isSelected()) {
				ClassicBase.ClickCheckbox_WPF(session, "chkPrintOnBothSides");
			}
			
			// If the Flip Pages Up checkbox is not checked, check it
			if(!session.findElementByXPath("//CheckBox[@AutomationId='chkFlipPagesUp']").isSelected()) {
				ClassicBase.ClickCheckbox_WPF(session, "chkFlipPagesUp");
			}
			
			log.info("'Flip Pages Up' is selected. Document will flip on short edge.");
		}
		
		else {
			log.info("Duplex selection not recognized. Select 'None,' 'Flip on Long Edge,' or 'Flip on Short Edge.'");
		}
    }
    
    
    public static HashMap<String, String> GetHashMap(RemoteWebDriver session, String className){
    	// Create a hashmap of all desired elements
    	HashMap<String, String> elPairs = new HashMap<String, String>();
    	List<WebElement> elList = session.findElementsByClassName(className);
    	for(WebElement el : elList) {
    		// Remove special characters like '_' and ':' to simplify the keys in the pair
    		String value = el.getText().toString();
    		String key = null;
    		if(value.contains(":")) {
    			String temp1 = value.substring(0, value.lastIndexOf(':'));
    			String temp2 = temp1.toLowerCase();
    			key = temp2.replaceAll("_", "");
    		}
    		else {
    			String temp = value.toLowerCase();
    			key = temp.replaceAll("_", "");
    		}
    		elPairs.put(key, value);
    		//log.info("Added '" + elPairs.get(key) + "' to hashmap.");
    	}
    	return elPairs;
    }
}

