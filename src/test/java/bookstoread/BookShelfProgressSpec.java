package bookstoread;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("A bookshelf progress")
@ExtendWith(BooksParameterResolver.class)
public class BookShelfProgressSpec {
    private BookShelf bookShelfUnderTest;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;
    private Book refactoring;

    @BeforeEach
    void init(Map<String, Book> books) {
        bookShelfUnderTest = new BookShelf();
        this.effectiveJava = books.get("Effective Java");
        this.codeComplete = books.get("Code Complete");
        this.mythicalManMonth = books.get("The Mythical Man-Month");
        this.cleanCode = books.get("Clean Code");
    }

    @Test
    @DisplayName("is 0% completed and 100% to-read when no book is read yet")
    @Disabled
    void progress100PercentUnread() throws BookShelfCapacityReached {
        bookShelfUnderTest.add(effectiveJava, codeComplete, mythicalManMonth, refactoring, cleanCode);

        Progress progress = bookShelfUnderTest.progress();
        assertThat(progress.completed()).isEqualTo(0);
        assertThat(progress.toRead()).isEqualTo(100);
    }

    @Test
    @DisplayName("is 40% completed and 60% to-read when 2 books are finished and 3 books not read yet")
    @Disabled
    void progressWithCompletedAndToReadPercentages() throws BookShelfCapacityReached {
        //Given a bookshelf with 5 books added and two have already read
        effectiveJava.startedReadingOn(LocalDate.of(2016, Month.JULY, 1));
        effectiveJava.finishedReadingOn(LocalDate.of(2016, Month.JULY, 31));
        cleanCode.startedReadingOn(LocalDate.of(2016, Month.AUGUST, 1));
        cleanCode.finishedReadingOn(LocalDate.of(2016, Month.AUGUST, 31));
        bookShelfUnderTest.add(effectiveJava, codeComplete, mythicalManMonth, refactoring, cleanCode);

        //Then the progress should be 40% read 60% to read
        Progress progress = bookShelfUnderTest.progress();

        assertThat(progress.completed()).isEqualTo(100);
        assertThat(progress.toRead()).isEqualTo(0);
    }

}