package bookstore.tests;

import bookstore.config.ConfigReader;
import bookstore.data.JsonDataReader;
import bookstore.model.Author;
import bookstore.utils.RestAssuredUtils;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AuthorTests {

    private final String baseUrl = ConfigReader.get("base.url") + "api/v1/Authors/";

    @Test
    public void getAllAuthors() {
        Response response = RestAssuredUtils.get(baseUrl);
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void getAuthorById() {
        int authorId = 1;
        Response response = RestAssuredUtils.get(baseUrl + authorId);
        assertEquals(response.statusCode(), 200);
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
    public void addAuthor(Author author) {
        Response response = RestAssuredUtils.post(baseUrl, author);
        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("firstName"), author.firstName);
    }

    @Test(dataProvider = "authorData")
    public void updateAuthor(Author author) {
        RestAssuredUtils.post(baseUrl, author);

        author.firstName = author.firstName + " Updated";

        Response response = RestAssuredUtils.put(baseUrl + author.id, author);
        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("firstName"), author.firstName);
    }

    @Test(dataProvider = "authorData")
    public void deleteAuthor(Author author) {
        RestAssuredUtils.post(baseUrl, author);

        Response response = RestAssuredUtils.delete(baseUrl + author.id);
        assertEquals(response.statusCode(), 200);
    }
}
