package com.selenium.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);

    public static String takeScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        long threadId = Thread.currentThread().threadId();

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String screenshotPath = "screenshots/" + testName + "_T" + threadId + "_" + timestamp + ".png";
        File dest = new File(screenshotPath);

        // Ensure directory exists (and check result to avoid static-analysis warning)
        File parentDir = dest.getParentFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                log.warn("Could not create screenshot directory: {}", parentDir.getAbsolutePath());
            }
        }

        try {
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to save screenshot", e);
            return null;
        }

        log.info("Saved screenshot: {}", dest.getAbsolutePath());
        return dest.getAbsolutePath();
    }
}