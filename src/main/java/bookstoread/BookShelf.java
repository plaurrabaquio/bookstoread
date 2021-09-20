package bookstoread;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookShelf {

    private final List<Book> books = new ArrayList<>();
    int capacity = Integer.MAX_VALUE;

    public BookShelf(int capacity) {
        this.capacity = capacity;
    }

    public BookShelf() {
    }

    public List<Book> books() {
        return Collections.unmodifiableList(books);
    }

    public void add(Book... booksToAdd) throws BookShelfCapacityReached{

        for (Book book : booksToAdd) {
            if (books.size() == capacity) {
                throw new BookShelfCapacityReached(String.format("BookShelf capacity of %d is reached. You can't add more books.", capacity));
            }
            books.add(book);
        }

    }

    public List<Book> arrange() {
        return arrange(Comparator.<Book>naturalOrder());
    }

    public List<Book> arrange(Comparator<Book> criteria) {
        return books.stream().sorted(criteria).collect(Collectors.toList());
    }

    public <K> Map<K, List<Book>> groupBy(Function<Book, K> fx) {
        return books
                .stream()
                .collect(Collectors.groupingBy(fx));
    }

    public Progress progress() {
        int booksRead = Long.valueOf(books.stream().filter(Book::isRead).count()).intValue();

        int booksToRead = books.size() - booksRead;
        int percentageCompleted = booksRead * 100 / books.size();
        int percentageToRead = booksToRead * 100 / books.size();
        int booksInProgress = Long.valueOf(books.stream().filter(Book::isProgress).count()).intValue();

        return new Progress(percentageCompleted, percentageToRead, booksInProgress);
    }

    public List<Book> findBooksByTitle(String title) {
        return findBooksByTitle(title, b -> true);
    }

    public List<Book> findBooksByTitle(String title, BookFilter  filter) {
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(b -> filter.apply(b))
                .collect(Collectors.toList());
    }
}
