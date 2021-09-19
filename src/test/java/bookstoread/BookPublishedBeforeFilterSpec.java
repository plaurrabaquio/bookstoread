package bookstoread;

import org.junit.jupiter.api.BeforeEach;

public class BookPublishedBeforeFilterSpec implements FilterBoundaryTests {
    BookFilter filter;

    @BeforeEach
    void init() {
        filter = BookPublishedYearFilter.Before(2007);
    }

    @Override
    public BookFilter get() {
        return filter;
    }
}
