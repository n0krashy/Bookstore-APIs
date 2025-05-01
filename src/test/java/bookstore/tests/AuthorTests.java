package bookstore.tests;

import bookstore.config.ConfigReader;
import bookstore.data.JsonDataReader;
import bookstore.model.Author;
import bookstore.utils.RestAssuredUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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

    @DataProvider(name = "authorData")
    public Object[][] authorData() {
        Author[] authors = JsonDataReader.readData("data/authorsData.json", Author[].class);
        Object[][] data = new Object[authors.length][1];
        for (int i = 0; i < authors.length; i++) {
            data[i][0] = authors[i];
        }
        return data;
    }

    @Test(dataProvider = "authorData")
    @Description("Test adds an author and verifies that response status code is 200")
    public void addAuthor(Author author) {
        Allure.step("Sending POST request to add author");
        Response response = RestAssuredUtils.post(baseUrl, author);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying author first name in response");
        assertEquals(response.jsonPath().getString("firstName"), author.firstName);
    }

    @Test(dataProvider = "authorData")
    @Description("Test creates then updates an author and verifies that response status code is 200")
    public void updateAuthor(Author author) {
        Allure.step("Creating a new author");
        RestAssuredUtils.post(baseUrl, author);

        author.firstName = author.firstName + " Updated";

        Allure.step("Updating author with ID " + author.id);
        Response response = RestAssuredUtils.put(baseUrl + author.id, author);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying author first name in response");
        assertEquals(response.jsonPath().getString("firstName"), author.firstName);
    }

    @Test(dataProvider = "authorData")
    @Description("Test deletes an author and verifies that response status code is 200")
    public void deleteAuthor(Author author) {
        Allure.step("Creating a new author");
        RestAssuredUtils.post(baseUrl, author);

        Allure.step("Deleting author with ID " + author.id);
        Response response = RestAssuredUtils.delete(baseUrl + author.id);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
    }
}
