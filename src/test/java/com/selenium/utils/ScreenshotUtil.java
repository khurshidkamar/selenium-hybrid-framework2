package com.selenium.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String takeScreenshot(WebDriver driver, String testName) {
        // Generate timestamp for unique filenames
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Create screenshot file
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Save inside /screenshots folder
        String screenshotPath = "screenshots/" + testName + "_" + timestamp + ".png";

        File destination = new File(screenshotPath);
        // Create the folder if missing, and check the result
        File parentDir = destination.getParentFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.out.println("⚠ Failed to create screenshot directory: " + parentDir.getAbsolutePath());
            }
        }

        try {
            Files.copy(src.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }

        return destination.getAbsolutePath();
    }
}