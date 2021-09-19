package bookstoread;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test names should tell what behavior they are testing rather than names of the methods they are testing
 * <p>
 * What makes a clean test? Three things.
 * Readability, readability, and readability.
 * Readability is perhaps even more important in unit tests than it is in production code.
 */
@DisplayName("A bookshelf")
@ExtendWith(BooksParameterResolver.class)
public class BookShelfSpec {
    private BookShelf bookShelfUnderTest;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;

    @BeforeEach
    void init(Map<String, Book> books) {
        bookShelfUnderTest = new BookShelf();
        this.effectiveJava = books.get("Effective Java");
        this.codeComplete = books.get("Code Complete");
        this.mythicalManMonth = books.get("The Mythical Man-Month");
        this.cleanCode = books.get("Clean Code");
    }

    @Nested
    @DisplayName("is empty")
    class IsEmpty {
        @Test
        @DisplayName("BookShelf is empty when no book is added to it")
        void shelfEmptyWhenNoBookAdded(TestInfo testInfo) {
            System.out.println(testInfo);
            List<Book> books = bookShelfUnderTest.books();
            assertTrue(books.isEmpty(), "BookShelf should be empty.");
        }

        @Test
        @DisplayName("BookShelf remains empty when the add is invoked without book")
        void emptyBookShelfWhenAddIsCalledWithoutBooks() throws BookShelfCapacityReached {
            bookShelfUnderTest.add();
            List<Book> books = bookShelfUnderTest.books();
            Assertions.assertEquals(0, books.size(), "BookShelf should by empty!");
        }
    }

    @Nested
    @DisplayName("after adding books")
    class BooksAreAdded {
        @Test
        @DisplayName("BookShelf contains two books when two books have been added")
        void bookshelfContainsTwoBooksWhenTwoBooksAdded(TestInfo testInfo) throws BookShelfCapacityReached {
            System.out.println(testInfo);
            bookShelfUnderTest.add(effectiveJava);
            bookShelfUnderTest.add(codeComplete);
            List<Book> books = bookShelfUnderTest.books();

            Assertions.assertEquals(2, books.size(), "BookShelf should have two books!");

        }

        @Test
        @DisplayName("BookShelf returns an immutable books collection to client")
        void booksReturnedFromBookshelfIsImmutableForClient() throws BookShelfCapacityReached {
            bookShelfUnderTest.add(effectiveJava, codeComplete);
            List<Book> books = bookShelfUnderTest.books();
            try {
                books.add(mythicalManMonth);
                fail("Should not be able to add book to books");
            } catch (Exception e) {
                assertTrue(e instanceof UnsupportedOperationException, "Should throw UnsupportedOperationException.");
            }
        }
    }

    @Nested
    @DisplayName("is arranged")
    class IsArranged {
        @Test
        @DisplayName("Bookshelf is arranged lexicographically by book title")
        void bookshelfArrangedByBookTitle() throws BookShelfCapacityReached {
            //Given a bookshelf with some books added
            bookShelfUnderTest.add(effectiveJava, codeComplete, mythicalManMonth);
            //When
            List<Book> booksArrangedByTitle = bookShelfUnderTest.arrange();
            //Then
            assertEquals(Arrays.asList(codeComplete, effectiveJava, mythicalManMonth), booksArrangedByTitle,
                    "Books in a bookshelf should be arranged lexicographically by book title");
        }

        @Test
        @DisplayName("BookShelf remains in the original order after the client invoke an arrange method")
        void booksInBookShelfAreInInsertionOrderAfterCallingArrange() throws BookShelfCapacityReached {
            //Given a bookshelf with some books added in certain order
            bookShelfUnderTest.add(effectiveJava, codeComplete, mythicalManMonth);
            //When we arrange the books
            bookShelfUnderTest.arrange();
            //Then we expect that the original list of books is still in the original order
            assertEquals(Arrays.asList(effectiveJava, codeComplete, mythicalManMonth), bookShelfUnderTest.books(),
                    "Books in bookshelf are in insertion order");
        }

        @Test
        @DisplayName("BookShelf is arranged by user provided criteria")
        void bookshelfArrangedByUserProvidedCriteria() throws BookShelfCapacityReached {
            //Given a bookshelf with some books added in certain order
            bookShelfUnderTest.add(effectiveJava, codeComplete, mythicalManMonth);
            //And a Comparator to compare in reversed order
            Comparator<Book> reversed = Comparator.<Book>naturalOrder().reversed();
            //When we arrange the books reversed
            List<Book> booksArrangedByTitleReversed = bookShelfUnderTest.arrange(reversed);
            //Then we expect that the original list of books is still in the original order
            assertThat(booksArrangedByTitleReversed).isSortedAccordingTo(reversed);
        }

        @Test
        @DisplayName("BookShelf is grouped by publication year")
        void groupBooksInsideNookShelfByPublicationYear() throws BookShelfCapacityReached {
            //Given a bookShelf with some books
            bookShelfUnderTest.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
            //When get the books grouped by publication year
            Map<Year, List<Book>> booksGroupedByPublicationYear =
                    bookShelfUnderTest.groupBy(b -> Year.of(b.getPublishedOn().getYear()));
            //Then we expect that booksGroupedByPublicationYear in the year 2008 contains the books for that year
            assertThat(booksGroupedByPublicationYear)
                    .containsKey(Year.of(2008))
                    .containsValues(Arrays.asList(effectiveJava, cleanCode));
        }

        @Test
        @DisplayName("BookShelf is grouped by author")
        void groupBooksInsideNookShelfByAuthor() throws BookShelfCapacityReached {
            //Given a bookShelf with some books
            bookShelfUnderTest.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
            //When get the books grouped by publication author
            Map<String, List<Book>> booksGroupedByAuthor =
                    bookShelfUnderTest.groupBy(Book::getAuthor);
            //Then we expect that booksGroupedByAuthor contains the books for the author
            assertThat(booksGroupedByAuthor)
                    .containsKey("Joshua Bloch")
                    .containsValues(singletonList(effectiveJava));
        }
    }

    @Test
    @DisplayName("Books are printed with all their fields")
    void everyBookIsBeingPrinted() {
        System.out.println(effectiveJava.toString());
        assertEquals("Book{title='Effective Java', author='Joshua Bloch', publishedOn=2008-05-08}",
                effectiveJava.toString(), "The expected result for ToString contains all the fields");
    }

    @Nested
    @DisplayName("search")
    class BookShelfSearchSpec {
        @BeforeEach
        void setup() throws BookShelfCapacityReached {
            bookShelfUnderTest.add(codeComplete, effectiveJava, mythicalManMonth, cleanCode);
        }

        @Test
        @DisplayName(" should find books with title containing text")
        void shouldFindBooksWithTitleContainingText() {
            List<Book> books = bookShelfUnderTest.findBooksByTitle("code");
            assertThat(books.size()).isEqualTo(2);
        }

        @Test
        @DisplayName(" should find books with title containing text and published after specified date.")
        void shouldFilterSearchedBooksBasedOnPublishedDate() {
            List<Book> books = bookShelfUnderTest.findBooksByTitle("code",
                    b -> b.getPublishedOn()
                            .isBefore(LocalDate.of(2005, 12, 31)));
            assertThat(books.size()).isEqualTo(1);
        }

        @Test
        @DisplayName(" should find books by author.")
        void shouldFilterSearchedBooksBasedOnAuthor() {
            List<Book> books = bookShelfUnderTest.findBooksByTitle("code",
                    b -> b.getPublishedOn()
                            .isBefore(LocalDate.of(2005, 12, 31)));
            assertThat(books.size()).isEqualTo(1);
        }

        //TODO: Implementar la interfaz de los filtros para que quede mejor Feature:
        // Search BookShelf https://apex.percipio.com/books/5a2c646a-a878-4e67-8177-bb69296be921
    }
}