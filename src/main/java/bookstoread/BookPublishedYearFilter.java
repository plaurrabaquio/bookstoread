package bookstoread;

import java.time.LocalDate;

class BookPublishedYearFilter implements BookFilter {
    private LocalDate afterDate;
    private LocalDate beforeDate;

    static BookPublishedYearFilter After(int year){
        BookPublishedYearFilter filter = new BookPublishedYearFilter();
        filter.afterDate = LocalDate.of(year, 12,31);
        return filter;
    }

    static BookPublishedYearFilter Before(int year){
        BookPublishedYearFilter filter = new BookPublishedYearFilter();
        filter.beforeDate = LocalDate.of(year, 12,31);
        return filter;
    }


    @Override
    public boolean apply(Book b) {
        if(afterDate != null){
            return b.getPublishedOn().isAfter(afterDate);
        }else if(beforeDate != null){
            return b.getPublishedOn().isBefore(beforeDate);
        }else{
            return false;
        }
    }
}
