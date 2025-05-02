package bookstore.tests;

import bookstore.config.ConfigReader;
import bookstore.data.JsonDataReader;
import bookstore.model.Author;
import bookstore.utils.RestAssuredUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class AuthorTests {

    private final String baseUrl = ConfigReader.get("base.url") + "api/v1/Authors/";

    @Test
    @Description("Test get all authors and verifies that response status code is 200")
    public void getAllAuthors() {
        Allure.step("Getting all authors");
        Response response = RestAssuredUtils.get(baseUrl);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
    }

    @Test
    @Description("Test get an author by ID and verifies that response status code is 200")
    public void getAuthorById() {
        int authorId = 1;
        Allure.step("Getting author with ID " + authorId);
        Response response = RestAssuredUtils.get(baseUrl + authorId);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying author ID in response");
        assertEquals(response.jsonPath().getInt("id"), authorId);
    }

    @Test
    @Description("Test get an author by invalid ID and verifies that response status code is 404")
    public void getAuthorByInvalidId() {
        int invalidAuthorId = 9999;  // assuming this author ID doesn't exist
        Allure.step("Getting author with invalid ID " + invalidAuthorId);
        Response response = RestAssuredUtils.get(baseUrl + invalidAuthorId);
        Allure.step("Verifying response status code is 404");
        assertEquals(response.statusCode(), 404);
    }

    @Test
    @Description("Test adds an author with null required data (id) and verifies that response status code is 400")
    public void addAuthorWithNoId() {
        Author invalidAuthor = new Author(null, 12); // missing required field
        Allure.step("Sending POST request to add an invalid author");
        Response response = RestAssuredUtils.post(baseUrl, invalidAuthor);
        Allure.step("Verifying response status code is 400");
        assertEquals(response.statusCode(), 400);
    }

    @Test
    @Description("Test adds an author with null required data (idBook) and verifies that response status code is 400")
    public void addAuthorWithNoBookId() {
        Author invalidAuthor = new Author(12, null); // missing required field
        Allure.step("Sending POST request to add an invalid author");
        Response response = RestAssuredUtils.post(baseUrl, invalidAuthor);
        Allure.step("Verifying response status code is 400");
        assertEquals(response.statusCode(), 400);
    }

    @Test(dataProvider = "authorData")
    @Description("Test adds an author and verifies that response status code is 200")
    public void addAuthor(Author author) {
        Allure.step("Sending POST request to add author");
        Response response = RestAssuredUtils.post(baseUrl, author);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying author first name in response");
        assertEquals(response.jsonPath().getString("firstName"), author.getFirstName());
    }

    @Test(dataProvider = "authorData")
    @Description("Test creates then updates an author and verifies that response status code is 200")
    public void updateAuthor(Author author) {
        Allure.step("Creating a new author");
        RestAssuredUtils.post(baseUrl, author);

        author.setFirstName(author.getFirstName() + " Updated");

        Allure.step("Updating author with ID " + author.getId());
        Response response = RestAssuredUtils.put(baseUrl + author.getId(), author);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying author first name in response");
        assertEquals(response.jsonPath().getString("firstName"), author.getFirstName());
    }

    @Test
    @Description("Test update an author with an invalid ID and verifies that response status code is 404")
    @Issue("Should return 404 instead of 400 for updating an author with invalid ID as a best practice")
    public void updateAuthorWithInvalidId() {
        Author author = new Author();
        author.setId(9999);  // assuming this author ID doesn't exist
        author.setFirstName("Updated Author");

        Allure.step("Updating author with invalid ID " + author.getId());
        Response response = RestAssuredUtils.put(baseUrl + author.getId(), author);
        Allure.step("Verifying response status code is 404");
        assertEquals(response.statusCode(), 404);
    }


    @Test(dataProvider = "authorData")
    @Description("Test deletes an author and verifies that response status code is 200")
    public void deleteAuthor(Author author) {
        Allure.step("Creating a new author");
        RestAssuredUtils.post(baseUrl, author);

        Allure.step("Deleting author with ID " + author.getId());
        Response response = RestAssuredUtils.delete(baseUrl + author.getId());
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
    }

    @Test
    @Description("Test deletes an author with an invalid ID and verifies that response status code is 404")
    @Issue("Returning 200 instead of 404 for deleting an author with invalid ID")
    public void deleteAuthorWithInvalidId() {
        int invalidAuthorId = 9999;  // assuming this author ID doesn't exist
        Allure.step("Deleting author with invalid ID " + invalidAuthorId);
        Response response = RestAssuredUtils.delete(baseUrl + invalidAuthorId);
        Allure.step("Verifying response status code is 404");
        assertEquals(response.statusCode(), 404);
    }

    @Test
    @Description("Test patching an author, expecting 405 status code")
    public void patchAuthor() {
        int id = 1;
        Map<String, Object> patchData = new HashMap<>();
        patchData.put("firstName", "GhostAuthor");

        Allure.step("Sending PATCH request to author ID " + id);
        Response response = RestAssuredUtils.patch(baseUrl + id, patchData);

        Allure.step("Verifying response status code is 405");
        assertEquals(response.statusCode(), 405);
    }

    @DataProvider(name = "authorData")
    public Object[][] authorData() {
        Author[] authors = JsonDataReader.readData("data/authorsData.json", Author[].class);
        Object[][] data = new Object[authors.length][1];
        for (int i = 0; i < authors.length; i++) {
            data[i][0] = authors[i];
        }
        return data;
    }

    @Test(dataProvider = "invalidAuthorJson")
    @Description("Test author creation with invalid data")
    public void addAuthorWithInvalidJson(Map<String, Object> invalidAuthor) {
        Allure.step("Sending POST request with invalid author JSON: " + invalidAuthor);
        Response response = RestAssuredUtils.post(baseUrl, invalidAuthor);
        Allure.step("Verifying response status code is 400");
        assertEquals(response.statusCode(), 400);
    }

    @Test(dataProvider = "invalidAuthorJson")
    @Description("Test updating an author with invalid data")
    public void updateAuthorWithInvalidJson(Map<String, Object> invalidAuthor) {
        int validAuthorId = 1; // Use a valid ID to reach the endpoint
        Allure.step("Sending PUT request with invalid author JSON to update ID " + validAuthorId);
        Response response = RestAssuredUtils.put(baseUrl + validAuthorId, invalidAuthor);
        Allure.step("Verifying response status code is 400");
        assertEquals(response.statusCode(), 400);
    }

    @DataProvider(name = "invalidAuthorJson")
    public Object[][] invalidAuthorJson() {
        List<Map<String, Object>> data = JsonDataReader.readInvalidData("data/authorsInvalidData.json");
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }
}