package bookstore.model;

public class Author {
    public int id;
    public int idBook;
    public String firstName;
    public String lastName;

    public Author() {
    }

    public Author(int id, int idBook, String firstName, String lastName) {
        this.id = id;
        this.idBook = idBook;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}