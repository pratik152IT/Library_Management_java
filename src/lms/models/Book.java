package lms.models;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private boolean available;

    public Book(int bookId, String title, String author, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }

    @Override
    public String toString() {
        return String.format("%d: \"%s\" by %s  [available=%s]", bookId, title, author, available ? "yes" : "no");
    }
}
