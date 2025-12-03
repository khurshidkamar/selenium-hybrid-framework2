package com.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BasePage {

    protected WebDriver driver;

    // Constructor: every page will pass its WebDriver here
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    // Find element
    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    // Click element
    protected void click(By locator) {
        find(locator).click();
    }

    // Type into element
    protected void type(By locator, String text) {
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(text);
    }

    // Get page title
    public String getTitle() {
        return driver.getTitle();
    }
}