package bookstore.tests;

import bookstore.config.ConfigReader;
import bookstore.data.JsonDataReader;
import bookstore.model.Book;
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

    @Test
    @Description("Test get a book by invalid ID and verifies that response status code is 404")
    public void getBookByInvalidId() {
        int invalidBookId = 9999;
        Allure.step("Getting book with invalid ID " + invalidBookId);
        Response response = RestAssuredUtils.get(baseUrl + invalidBookId);
        Allure.step("Verifying response status code is 404");
        assertEquals(response.statusCode(), 404);
    }

    @Test
    @Description("Test adds a book with invalid empty data and verifies that response status code is 400")
    public void addBookWithInvalidData() {
        Book invalidBook = new Book();  // no fields set
        Allure.step("Sending POST request to add an invalid book");
        Response response = RestAssuredUtils.post(baseUrl, invalidBook);
        Allure.step("Verifying response status code is 400");
        assertEquals(response.statusCode(), 400);
    }

    @Test(dataProvider = "bookData")
    @Description("Test adds a book and verifies that response status code is 200")
    public void addBook(Book book) {
        Allure.step("Sending POST request to add book");
        Response response = RestAssuredUtils.post(baseUrl, book);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying book title in response");
        assertEquals(response.jsonPath().getString("title"), book.getTitle());
    }

    @Test(dataProvider = "bookData")
    @Description("Test creates then updates a book and verifies that response status code is 200")
    public void updateBook(Book book) {
        Allure.step("Creating a new book");
        RestAssuredUtils.post(baseUrl, book);

        book.setTitle(book.getTitle() + " Updated");

        Allure.step("Updating book with ID " + book.getId());
        Response response = RestAssuredUtils.put(baseUrl + book.getId(), book);
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
        Allure.step("Verifying updated title in response");
        assertEquals(response.jsonPath().getString("title"), book.getTitle());
    }

    @Test
    @Description("Test update a book with an invalid ID and verifies that response status code is 404")
    @Issue("Should return 404 instead of 400 for updating a book with invalid ID as a best practice")
    public void updateBookWithInvalidId() {
        Book book = new Book();
        book.setId(9999);
        book.setTitle("Invalid Update");

        Allure.step("Updating book with invalid ID " + book.getId());
        Response response = RestAssuredUtils.put(baseUrl + book.getId(), book);
        Allure.step("Verifying response status code is 404");
        assertEquals(response.statusCode(), 404);
    }

    @Test(dataProvider = "bookData")
    @Description("Test deletes a book and verifies that response status code is 200")
    public void deleteBook(Book book) {
        Allure.step("Creating a new book");
        RestAssuredUtils.post(baseUrl, book);

        Allure.step("Deleting book with ID " + book.getId());
        Response response = RestAssuredUtils.delete(baseUrl + book.getId());
        Allure.step("Verifying response status code is 200");
        assertEquals(response.statusCode(), 200);
    }

    @Test
    @Description("Test deletes a book with an invalid ID and verifies that response status code is 404")
    @Issue("Returns 200 instead of 404 when deleting a book with invalid ID")
    public void deleteBookWithInvalidId() {
        int invalidBookId = 9999;
        Allure.step("Deleting book with invalid ID " + invalidBookId);
        Response response = RestAssuredUtils.delete(baseUrl + invalidBookId);
        Allure.step("Verifying response status code is 404");
        assertEquals(response.statusCode(), 404);
    }

    @Test
    @Description("Test get authors of a book using invalid ID returns 404")
    public void getAuthorsOfBookWithInvalidId() {
        int invalidBookId = 9999;
        String url = baseUrl + "authors/books/" + invalidBookId;
        Allure.step("Getting authors of invalid book ID " + invalidBookId);
        Response response = RestAssuredUtils.get(url);
        Allure.step("Verifying response status code is 404");
        assertEquals(response.statusCode(), 404);
    }

    @Test
    @Description("Test patching a book, expecting 405 status code")
    public void patchBook() {
        int id = 1;
        Map<String, Object> patchData = new HashMap<>();
        patchData.put("title", "GhostBook");

        Allure.step("Sending PATCH request to book ID " + id);
        Response response = RestAssuredUtils.patch(baseUrl + id, patchData);

        Allure.step("Verifying response status code is 405");
        assertEquals(response.statusCode(), 405);
    }

    @Test(dataProvider = "invalidBookJson")
    @Description("Test creating a book with invalid data type")
    public void createBookWithInvalidDataType(Map<String, Object> invalidBook) {
        Allure.step("Sending POST request with invalid book JSON");
        Response response = RestAssuredUtils.post(baseUrl, invalidBook);
        Allure.step("Verifying response status code is 400");
        assertEquals(response.statusCode(), 400);
    }

    @Test(dataProvider = "invalidBookJson")
    @Description("Test updating a book with invalid data type")
    public void updateBookWithInvalidDataType(Map<String, Object> invalidBook) {
        int validBookId = 1; // Change as needed
        Allure.step("Sending PUT request with invalid book JSON to update ID " + validBookId);
        Response response = RestAssuredUtils.put(baseUrl + validBookId, invalidBook);
        Allure.step("Verifying response status code is 400");
        assertEquals(response.statusCode(), 400);
    }

    @DataProvider(name = "invalidBookJson")
    public Object[][] invalidBookJson() {
        List<Map<String, Object>> list = JsonDataReader.readInvalidData("data/booksInvalidData.json");
        return list.stream().map(item -> new Object[]{item}).toArray(Object[][]::new);
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
}
