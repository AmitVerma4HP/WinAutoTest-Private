package com.hp.win.utility;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.hp.win.core.Base;

public class ScreenshotUtility extends Base implements ITestListener {

	public static String sTestName;
	private static final Logger log = LogManager.getLogger(ScreenshotUtility.class);
	protected static RemoteWebDriver curSession = null;

	public ScreenshotUtility() throws IOException {
		super();
	}

	public void onStart(ITestContext tr) {
		// TODO Auto-generated method stub
		sTestName = tr.getName();
	}

	public void onFinish(ITestContext tr) {
		// TODO Auto-generated method stub

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult tr) {
		// TODO Auto-generated method stub

	}

	public void onTestFailure(ITestResult tr) {
		// TODO Auto-generated method stub
		captureScreenShot(tr, "fail");
	}

	public void onTestSkipped(ITestResult tr) {
		// TODO Auto-generated method stub

	}

	public void onTestStart(ITestResult tr) {
		// TODO Auto-generated method stub

	}

	public void onTestSuccess(ITestResult tr) {
		// TODO Auto-generated method stub
	}

	
	// Method for capture screenshot
	// Session information for capturing a Screenshot is determined based on the class and method names
	// that is being executed.
	public void captureScreenShot(ITestResult result, String status) {
		String curdevice = "";
		log.info("Capturing the screenshot now");

		String destDir = System.getProperty("user.dir");
		String passfailMethod = result.getMethod().getRealClass().getSimpleName() + "."
				+ result.getMethod().getMethodName();

		// Conditions to capture the appropriate Session to use in capturing the screenshot.
		if (passfailMethod.contains("Photos")) {
			curSession = Base.PhotosSession;
		} else if (passfailMethod.contains("MsWord")) {
			curSession = Base.MsWordSession;
		} else if (passfailMethod.contains("Note")) {
			curSession = Base.NotepadSession;
		} else if (passfailMethod.contains("Discover")) {
			curSession = Base.DesktopSession;
		} else if (passfailMethod.contains("Queue")) {
			curSession = Base.PrintQueueSession;
		}

		// To capture screenshot.
		File scrFile = ((TakesScreenshot) curSession).getScreenshotAs(OutputType.FILE);
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");

		// If status = fail then set folder name "screenshots/Failures"
		if (status.equalsIgnoreCase("fail")) {
			destDir = destDir + "/screenshots/Failures/" + (dateFormat.format(new Date()).substring(0, 11));
		}

		// If status = pass then set folder name "screenshots/Success" Below 2
		// lines can be comment if we dont want to capture screenshot upon success
		/* else if (status.equalsIgnoreCase("pass")) { destDir =
		 * "screenshots/Success"; }
		 */

		// To create folder to store screenshots
		new File(destDir).mkdirs();

		// Set file name with combination of test class name + date time.
		String destFile = passfailMethod + " - " + curdevice + "-" + dateFormat.format(new Date()) + ".png";

		try {
			// Store file at destination folder location
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method returns the testname of each test used in naming the log
	// files, screenshots and videos captured.
	public static String getTestName() {
		log.info("Getting test name:" + sTestName);
		return sTestName;
	}
}