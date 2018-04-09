package com.hp.win.utility;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
		Base.captureScreenShot(tr, "fail");
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

	
	// This method returns the testname of each test used in naming the log
	// files, screenshots and videos captured.
	public static String getTestName() {
		log.info("Getting test name:" + sTestName);
		return sTestName;
	}
}