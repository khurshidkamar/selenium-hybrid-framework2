package com.selenium.tests;

import com.selenium.base.BaseTest;
import com.selenium.pages.LoginPage;
import com.selenium.pages.SecureAreaPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void validLogin_shouldShowSuccessMessage() {
        LoginPage loginPage = new LoginPage(driver);
        SecureAreaPage secureAreaPage = new SecureAreaPage(driver);

        loginPage.open();
        loginPage.enterUsername("tomsmith");
        loginPage.enterPassword("SuperSecretPassword!");
        loginPage.clickLogin();

        String message = secureAreaPage.getSuccessMessage();

        Assert.assertTrue(
                message.contains("You logged into a secure area!"),
                "Success message did not match. Actual: " + message
        );
//        Assert.assertTrue(message.contains("WRONG TEXT"));
    }

    @Test
    public void invalidLogin_shouldShowErrorMessage() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.enterUsername("wrongUser");
        loginPage.enterPassword("wrongPass");
        loginPage.clickLogin();

        String error = loginPage.getErrorMessage();

        Assert.assertTrue(
                error.contains("Your username is invalid!"),
                "Error message did not match. Actual: " + error
                );

//        Assert.assertTrue(message.contains("WRONG TEXT"));

    }
}