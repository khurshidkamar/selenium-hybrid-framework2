package com.selenium.api.tests;

import com.selenium.api.base.ApiTestBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostsApiTest extends ApiTestBase {

    @Test
    public void getSinglePost_shouldReturn200() {
        Response response = RestAssured
                .given()
                .when()
                .get("/posts")
                .then()
                .extract()
                .response();

//        System.out.println("Status code: " + response.getStatusCode());
//        System.out.println("Body:'\n'" + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");

        String title = response.jsonPath().getString("title");
        Assert.assertNotNull(title, "Title is null");
    }

    @Test
    public void createPost_shouldReturn201() {
        String requestBody = """
            {
              "title": "My API Post",
              "body": "This is a test post from RestAssured",
              "userId": 1
            }
            """;

        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .extract()
                .response();

//        System.out.println("Status code: " + response.getStatusCode());
//        System.out.println("Body:'\n'" + response.getBody().asString());
//
        Assert.assertEquals(response.getStatusCode(), 201, "Status code is not 201");

        String title = response.jsonPath().getString("title");
        Assert.assertEquals(title, "My API Post", "Title does not match");
    }

    @Test
    public void updatePost_shouldReturn200() {
        String requestBody = """
            {
              "id": 1,
              "title": "Updated Title",
              "body": "Updated body via PUT",
              "userId": 1
            }
            """;

        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put("/posts/1")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");

        String title = response.jsonPath().getString("title");
        Assert.assertEquals(title, "Updated Title", "Title was not updated");
    }

    @Test
    public void deletePost_shouldReturn200Or204() {
        Response response = RestAssured
                .given()
                .when()
                .delete("/posts/1")
                .then()
                .extract()
                .response();

        int status = response.getStatusCode();
        Assert.assertTrue(status == 200 || status == 204,
                "Expected 200 or 204 but got " + status);
    }
}
