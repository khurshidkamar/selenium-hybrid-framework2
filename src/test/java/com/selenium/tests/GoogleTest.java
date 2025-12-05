package com.selenium.tests;

import com.selenium.base.BaseTest;
import com.selenium.pages.GoogleHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleTest extends BaseTest {

    @Test
    public void searchInGoogle() throws InterruptedException {
        GoogleHomePage googlePage = new GoogleHomePage(driver);

        // Open Google
        googlePage.open();

        // Search for something
        googlePage.search("Selenium WebDriver");

        // Tiny wait just to be safe (later we'll use proper waits)
        Thread.sleep(30000);

        String title = googlePage.getTitle();

        Assert.assertNotNull(title, "Page title is null");
        Assert.assertTrue(title.toLowerCase().contains("selenium webdriver"),
                "Title does not contain search term");
    }
}