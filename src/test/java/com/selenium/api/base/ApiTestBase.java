package com.selenium.api.base;

import com.selenium.config.ConfigReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import io.qameta.allure.restassured.AllureRestAssured;
public class ApiTestBase {

    @BeforeClass
    public void setUpApi() {
        RestAssured.baseURI = ConfigReader.getApiBaseUrl();

        // Add Allure filter so all API calls are logged in the report
        RestAssured.filters(new AllureRestAssured());
    }
}