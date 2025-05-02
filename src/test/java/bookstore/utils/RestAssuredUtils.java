package bookstore.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RestAssuredUtils {
    public static Response get(String url) {
        return given().get(url);
    }

    public static Response post(String url, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(url);
    }

    public static Response delete(String url) {
        return given().delete(url);
    }

    public static Response put(String url, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .put(url);
    }

    public static Response patch(String url, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch(url)
                .then()
                .extract()
                .response();
    }
}