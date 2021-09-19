package bookstoread;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BookFilterSpec {
    private Book cleanCode;
    private Book codeComplete;
    private Book mythicalManMonth;

    @BeforeEach
    void init() {
        cleanCode = new Book("Clean Code", "Robert C. Martin", LocalDate.of(2008, Month.AUGUST, 1));
        codeComplete = new Book("Code Complete", "Steve McConell", LocalDate.of(2004, Month.JUNE, 9));
        mythicalManMonth = new Book("The Mythical Man-Month", "Frederick Phillips Brooks", LocalDate.of(1975, Month.JANUARY, 1));
    }

    @Nested
    @DisplayName("book published date")
    class BookPublishedFilterSpec {
        @Test
        @DisplayName("is after specified year")
        void validateBookPublishedDatePostAskedYear() {
            BookFilter filter = BookPublishedYearFilter.After(2007);
            assertTrue(filter.apply(cleanCode));
            assertFalse(filter.apply(codeComplete));
        }

        @Test
        @DisplayName("is before specified year")
        void validateBookPublishedDateBeforeAskedYear() {
            BookFilter filter = BookPublishedYearFilter.Before(2007);
            assertFalse(filter.apply(cleanCode));
            assertTrue(filter.apply(codeComplete));
        }

        @Test
        @DisplayName("Composite criteria is based on multiple filters")
        void shouldFilterOnMultipleCriteria(){
            CompositeFilter compositeFilter = new CompositeFilter();
            compositeFilter.addFilter(b-> false);
            assertFalse(compositeFilter.apply(cleanCode));
        }

        @Test
        @DisplayName("Composite criteria does not invoke after first failure")
        void shouldNotInvokeAfterFirstFailure(){
            CompositeFilter compositeFilter = new CompositeFilter();

            BookFilter invokedMockedFilter = Mockito.mock(BookFilter.class);
            Mockito.when(invokedMockedFilter.apply(cleanCode)).thenReturn(false);
            compositeFilter.addFilter(invokedMockedFilter);

            BookFilter nonInvokedMockedFilter = Mockito.mock(BookFilter.class);
            Mockito.when(nonInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(nonInvokedMockedFilter);

            assertFalse(compositeFilter.apply(cleanCode));
            Mockito.verify(invokedMockedFilter).apply(cleanCode);
            Mockito.verifyZeroInteractions(nonInvokedMockedFilter);
        }

        @Test
        @DisplayName("Composite criteria invokes all filters")
        void shouldInvokeAllFilters(){
            CompositeFilter compositeFilter = new CompositeFilter();
            BookFilter firstInvokedMockedFilter = Mockito.mock(BookFilter.class);
            Mockito.when(firstInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(firstInvokedMockedFilter);

            BookFilter secondInvokedMockedFilter = Mockito.mock(BookFilter.class);
            Mockito.when(secondInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(secondInvokedMockedFilter);
            assertTrue(compositeFilter.apply(cleanCode));
            Mockito.verify(firstInvokedMockedFilter).apply(cleanCode);
            Mockito.verify(secondInvokedMockedFilter).apply(cleanCode);
        }

        @Test
        @DisplayName("Composite criteria invokes multiple filters")
        void shouldFilterOnMultiplesCriteria(){
            CompositeFilter compositeFilter = new CompositeFilter();
            final Map<Integer, Boolean> invocationMap = new HashMap<>();
            compositeFilter.addFilter( b -> {
                invocationMap.put(1,true);
                return true;
            });
            compositeFilter.apply(cleanCode);
        }

        @Test
        void throwsExceptionWhenBooksAreAddedAfterCapacityIsReached() throws BookShelfCapacityReached {
            BookShelf bookShelf = new BookShelf(2);
            bookShelf.add(cleanCode, codeComplete);
            BookShelfCapacityReached throwException = assertThrows(BookShelfCapacityReached.class,
                    () -> bookShelf.add(mythicalManMonth));
            assertEquals("BookShelf capacity of 2 is reached. You can't add more books.", throwException.getMessage());
        }

    }

}
