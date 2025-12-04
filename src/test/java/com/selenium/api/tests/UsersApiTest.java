package com.selenium.api.tests;

import com.selenium.api.base.ApiTestBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UsersApiTest extends ApiTestBase {

    @Test(groups = "api")
    public void getSinglePost_shouldReturn200() {
        // This will call: https://jsonplaceholder.typicode.com/posts/1
        Response response = RestAssured
                .given()
                .when()
                .get("/posts/1")
                .then()
                .extract()
                .response();

//        System.out.println("Status code: " + response.getStatusCode());
//        System.out.println("Response body: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");

        String title = response.jsonPath().getString("title");
        Assert.assertNotNull(title, "Title is null");
    }
}