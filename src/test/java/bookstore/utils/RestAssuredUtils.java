package bookstore.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RestAssuredUtils {
    public static Response get(String url) {
        return RestAssured.given().get(url);
    }

    public static Response post(String url, Object body) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(url);
    }

    public static Response delete(String url) {
        return RestAssured.given().delete(url);
    }

    public static Response put(String url, Object body) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .put(url);
    }
}