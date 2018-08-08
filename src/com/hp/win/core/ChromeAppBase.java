package com.hp.win.core;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

public class ChromeAppBase extends Win32Base {
	private static final Logger log = LogManager.getLogger(ChromeAppBase.class);
	public static RemoteWebDriver ChromeSession = null;
	public static RemoteWebDriver ChromeSession1 = null;
	public static RemoteWebDriver PrintDialogSession = null;
	public static RemoteWebDriver PreferencesSession = null;
	public static RemoteWebDriver AdvancedSession = null;

	// Method to open Chrome browser with desired URL.
	public static RemoteWebDriver OpenChromeApp(String device_name, String web_url, String chrome_exe_loc)
			throws MalformedURLException {

		try {
			capabilities = new DesiredCapabilities();
			capabilities.setCapability("app", chrome_exe_loc);
			capabilities.setCapability("appArguments", testfiles_loc + web_url);
			capabilities.setCapability("appWorkingDir", testfiles_loc);
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", device_name);
			ChromeSession = new RemoteWebDriver(new URL(WindowsApplicationDriverUrl), capabilities);
			Assert.assertNotNull(ChromeSession);
			Thread.sleep(1000);
		} catch (Exception e) {
			log.info("Error opening Chrome app");
		}

		log.info("Opened ChromeSession successfully");
		return ChromeSession;

	}

	// Method to print web page from Chrome Browser
	public void PrintChromeFile(String ptr_name, String orientation, String duplex_optn, String color_optn,
			String prnt_quality, String paper_size, String device_name)
			throws InterruptedException, MalformedURLException {

		// Launching the Classic print window as the UWP print window is not recognized by WinAppDriver.
		Thread.sleep(1000);
		ChromeSession.getKeyboard().sendKeys(Keys.CONTROL, Keys.SHIFT);
		ChromeSession.getKeyboard().pressKey("p");
		log.info("Pressed CTRL+SHIFT+P to get to Print Option");
		Thread.sleep(1000);
		ChromeSession.getKeyboard().releaseKey(Keys.SHIFT);
		ChromeSession.getKeyboard().releaseKey(Keys.CONTROL);
		Thread.sleep(3000);

		// launching Print properties screen for printing.
		PrintDialogSession = GetDesktopSession(device_name);
		Assert.assertNotNull(PrintDialogSession);
		log.info("Successfully launched PrintDialogSession.");

		// Select WiFi Printer
		log.info("Looking for '" + ptr_name + "'...");
		if (PrintDialogSession.findElementByName(ptr_name).isSelected()) {
			log.info("'" + ptr_name + "' is already selected.");
		} else {
			try {
				PrintDialogSession.findElementByName(ptr_name).click();
				log.info("Clicked on '" + ptr_name + "' successfully.");
				Thread.sleep(1000);
			} catch (Exception e) {
				log.info("Printer under test " + ptr_name
						+ " is not found so make sure you have \"discovered and added printer\" before running this test OR have typed the printer name incorrectly in testsuite xml");
				throw new RuntimeException(e);
			}
		}

		// Open Preferences window
		ClickButton(PrintDialogSession, "Preferences");

		// A new desktop session must be created every time a dialog box is created or destroyed
		try {
			PrintDialogSession.quit();
			log.info("Closed PrintDialogSession...");
		} catch (Exception e) {
			log.info("PrintDialogSession already terminated.");
		}

		// In order to access the Preferences dialog, we need to start a new desktop session
		PreferencesSession = GetDesktopSession(device_name);
		Assert.assertNotNull(PreferencesSession);
		log.info("Successfully launched PreferencesSession.");

		
		// In Layout tab on the Preferences.
		// Select Duplex settings on Layout tab in Preferences window.
		ChooseDuplexOrSimplex_Win32(PreferencesSession, duplex_optn, device_name);

		// Select Orientation settings on Layout tab in Preferences window.
		ChooseOrientation_Win32(PreferencesSession, orientation, device_name);

		// Select Paper/Quality tab after the Layout tab in Preferences window.
		SelectPreferencesTab_Win32(PreferencesSession, "Paper/Quality");

		// Select Color settings on Paper/Quality tab in Preferences window.
		ChooseColorOrMono_Win32(PreferencesSession, color_optn);

		// Select Print Quality settings on Paper/Quality tab in Preferences window.
		ChoosePrintQuality_Win32(PreferencesSession, prnt_quality);

		// Now open the Advanced settings
		ClickButton(PreferencesSession, "Advanced...");

		// Close the session for the Preferences dialog box
		try {
			PreferencesSession.quit();
			log.info("Closed PreferencesSession...");
		} catch (Exception e) {
			log.info("PreferencesSession already terminated.");
		}

		// Open a session for the Advanced dialog box
		AdvancedSession = GetDesktopSession(device_name);
		Assert.assertNotNull(AdvancedSession);
		log.info("Successfully launched AdvancedSession.");

		// Select Paper size on Advanced window.
		ChoosePaperSize_Win32(AdvancedSession, paper_size, device_name);

		ClickButton(AdvancedSession, "OK");
		ClickButton(AdvancedSession, "OK");
		ClickButton(AdvancedSession, "Print");

		// A new desktop session must be created every time a dialog box is created or destroyed
		try {
			AdvancedSession.quit();
			log.info("Closed AdvancedSession...");
		} catch (Exception e) {
			log.info("AdvancedSession already terminated.");
		}
		Thread.sleep(1000);
	}

	// Method to switch from NotePad session to PrintQueue Window
	public static void SwitchToChromeSession(String device_name) throws MalformedURLException {
		try {

			DesktopSession = Base.GetDesktopSession(device_name);

			// Get handle to Chrome browser window
			WebElement googleChromeWindow = DesktopSession.findElementByClassName("Chrome_WidgetWin_1");
			String nativeWindowHandle = googleChromeWindow.getAttribute("NativeWindowHandle");
			int googleChromeWindowHandle = Integer.parseInt(nativeWindowHandle);
			log.debug("int value:" + nativeWindowHandle);
			String googleChromeTopWindowHandle = hex.concat(Integer.toHexString(googleChromeWindowHandle));
			log.debug("Hex Value:" + googleChromeTopWindowHandle);

			log.info("Successfully got Chrome session handle.");

			// Create a ChromeSession by attaching to an existing application top level window handle
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("appTopLevelWindow", googleChromeTopWindowHandle);
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", device_name);
			ChromeSession1 = new WindowsDriver<WindowsElement>(new URL(WindowsApplicationDriverUrl), capabilities);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error getting Chrome session");
			// throw new RuntimeException(e);
		}
		log.info("Switched back to chrome session successfully");

	}

}
