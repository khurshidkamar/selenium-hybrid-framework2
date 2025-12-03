package com.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class GoogleHomePage extends BasePage {

    // Locators
    private final By searchBox = By.name("q");

    // Constructor: passes driver to BasePage
    public GoogleHomePage(WebDriver driver) {
        super(driver);
    }

    // Open Google home page
    public void open() {
        driver.get("https://www.google.com");
    }

    // Perform a search
    public void search(String query) {
        type(searchBox, query);
        // Press ENTER to submit search
        driver.findElement(searchBox).sendKeys(Keys.ENTER);
    }
}