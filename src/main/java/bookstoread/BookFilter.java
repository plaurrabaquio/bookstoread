package bookstoread;

@FunctionalInterface
public interface BookFilter {
    boolean apply(Book b);
}
