package com.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final String URL = "https://the-internet.herokuapp.com/login";

    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton   = By.cssSelector("button.radius");
    private final By errorMessage  = By.id("flash");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(URL);
    }

    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public String getErrorMessage() {
        return waitForVisibility(errorMessage).getText();
    }
}