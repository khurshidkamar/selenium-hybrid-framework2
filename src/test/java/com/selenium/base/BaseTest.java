package com.selenium.base;

import com.selenium.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // Take screenshot if test FAILED
        if (result.getStatus() == ITestResult.FAILURE) {
            String testName = result.getName();
            String screenshotPath = ScreenshotUtil.takeScreenshot(driver, testName);

            System.out.println("❌ Test failed: " + testName);
            System.out.println("📸 Screenshot saved at: " + screenshotPath);
        }

        // Quit driver
        DriverFactory.quitDriver();
    }
}