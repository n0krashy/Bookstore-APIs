package bookstore.model;

import lombok.Data;

@Data
public class Author {
    private Integer id;
    private Integer idBook;
    private String firstName;
    private String lastName;

    public Author() {
    }

    public Author(Integer id, Integer idBook) {
        this.id = id;
        this.idBook = idBook;
    }

    public Author(Integer id, Integer idBook, String firstName, String lastName) {
        this.id = id;
        this.idBook = idBook;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}