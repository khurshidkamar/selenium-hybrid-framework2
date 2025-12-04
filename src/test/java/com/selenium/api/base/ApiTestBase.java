package com.selenium.api.base;

import com.selenium.config.ConfigReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class ApiTestBase {

    @BeforeClass
    public void setUpApi() {
        // Set the base URI for all RestAssured requests in this test class
        RestAssured.baseURI = ConfigReader.getApiBaseUrl();
    }
}