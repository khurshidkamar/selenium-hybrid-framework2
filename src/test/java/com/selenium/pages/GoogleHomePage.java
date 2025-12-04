package com.selenium.pages;

import com.selenium.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class GoogleHomePage extends BasePage {

    private final By searchBox = By.name("q");

    public GoogleHomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(ConfigReader.getBaseUrl());
        waitForVisibility(searchBox);
    }

    public void search(String query) {
        type(searchBox, query);

        // Small human-like pause helps avoid CAPTCHA
        try { Thread.sleep(3000); } catch (Exception ignored) {}

        driver.findElement(searchBox).sendKeys(Keys.ENTER);
    }
}