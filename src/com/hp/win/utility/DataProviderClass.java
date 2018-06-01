package com.hp.win.utility;

import org.testng.annotations.DataProvider;

public class DataProviderClass {
	
	@DataProvider(name="PhotoPrint")
	public static Object [][] dataProviderMethod()
	{
		Object [][] printerList = new Object[3][1];
		
		{
			printerList[0][0] = "First Printer";
			printerList[1][0] = "Second Printer";
			printerList[2][0] = "Third Printer";
			
		}
		return printerList;
	}
}