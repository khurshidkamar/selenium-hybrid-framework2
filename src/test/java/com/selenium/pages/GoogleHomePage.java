package com.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class GoogleHomePage extends BasePage {

    private final By searchBox = By.name("q");

    public GoogleHomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://www.google.com");
        waitForVisibility(searchBox);
    }

    public void search(String query) {
        type(searchBox, query);

        // Small human-like pause helps avoid CAPTCHA
        try { Thread.sleep(300); } catch (Exception ignored) {}

        driver.findElement(searchBox).sendKeys(Keys.ENTER);
    }
}