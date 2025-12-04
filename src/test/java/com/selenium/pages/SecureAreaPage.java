package com.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SecureAreaPage extends BasePage {

    private final By successMessage = By.id("flash");
    private final By logoutButton   = By.cssSelector("a.button.secondary.radius");

    public SecureAreaPage(WebDriver driver) {
        super(driver);
    }

    public String getSuccessMessage() {
        return waitForVisibility(successMessage).getText();
    }

    public void clickLogout() {
        click(logoutButton);
    }
}