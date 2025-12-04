package com.selenium.base;

import com.selenium.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseTest {

    protected WebDriver driver;

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeMethod
    public void setUp() {
        log.info("Initializing browser...");
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
        log.info("Browser initialized.");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // Take screenshot if test FAILED
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test FAILED: {}", result.getName());
            String testName = result.getName();
            String screenshotPath = ScreenshotUtil.takeScreenshot(driver, testName);

            System.out.println("❌ Test failed: " + testName);
            System.out.println("📸 Screenshot saved at: " + screenshotPath);
            log.error("Screenshot saved at: {}", screenshotPath);
        }
        log.info("Closing browser...");
        // Quit driver
        DriverFactory.quitDriver();
    }
}