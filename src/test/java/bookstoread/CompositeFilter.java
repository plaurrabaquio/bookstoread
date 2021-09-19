package bookstoread;

import java.util.ArrayList;
import java.util.List;

public class CompositeFilter implements BookFilter {
    private List<BookFilter> filters;
    CompositeFilter(){
        filters = new ArrayList<>();
    }

    @Override
    public boolean apply(final Book b) {
        return filters.stream()
                .map(bookFilter -> bookFilter.apply(b))
                .reduce(true, (b1, b2) -> b1 && b2);
    }

    public void addFilter(final BookFilter bookFilter) {
        filters.add(bookFilter);
    }
}
