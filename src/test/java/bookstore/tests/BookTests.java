package bookstore.tests;

import bookstore.config.ConfigReader;
import bookstore.data.JsonDataReader;
import bookstore.model.Book;
import bookstore.utils.RestAssuredUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BookTests {

    private final String baseUrl = ConfigReader.get("base.url") + "api/v1/Books/";

    @Test
    @Description("Test get all books and verifies that response status code is 200")
    public void getAllBooks() {
        Allure.step("Getting all books");
        Response response = RestAssuredUtils.get(baseUrl);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
    }

    @Test
    @Description("Test get a book by ID and verifies that response status code is 200")
    public void getBookById() {
        int bookId = 1;
        Allure.step("Getting book with ID " + bookId);
        Response response = RestAssuredUtils.get(baseUrl + bookId);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying book ID in response");
        assertEquals(response.jsonPath().getInt("id"), bookId);
    }

    @DataProvider(name = "bookData")
    public Object[][] bookData() {
        Book[] books = JsonDataReader.readData("data/booksData.json", Book[].class);
        Object[][] data = new Object[books.length][1];
        for (int i = 0; i < books.length; i++) {
            data[i][0] = books[i];
        }
        return data;
    }

    @Test(dataProvider = "bookData")
    @Description("Test adds a book and verifies that response status code is 200")
    public void addBook(Book book) {
        Allure.step("Sending POST request to add book");
        Response response = RestAssuredUtils.post(baseUrl, book);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying book title in response");
        assertEquals(response.jsonPath().getString("title"), book.title);
    }

    @Test(dataProvider = "bookData")
    @Description("Test creates then updates a book and verifies that response status code is 200")
    public void updateBook(Book book) {
        Allure.step("Creating a new book");
        RestAssuredUtils.post(baseUrl, book);

        book.title = book.title + " Updated";

        Allure.step("Updating book with ID " + book.id);
        Response response = RestAssuredUtils.put(baseUrl + book.id, book);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying book title in response");
        assertEquals(response.jsonPath().getString("title"), book.title);
    }

    @Test(dataProvider = "bookData")
    @Description("Test deletes a book and verifies that response status code is 200")
    public void deleteBook(Book book) {
        Allure.step("Creating a new book");
        RestAssuredUtils.post(baseUrl, book);

        Allure.step("Deleting book with ID " + book.id);
        Response response = RestAssuredUtils.delete(baseUrl + book.id);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
    }

}