package bookstoread;


import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class BooksParameterResolver implements ParameterResolver {
    private final Map<String, Book> books;

    public BooksParameterResolver() {
        Map<String, Book> books = new HashMap<>();
        for (Book b : createBooks()) {
            books.put(b.getTitle(), b);
        }
        this.books = books;
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext)
            throws ParameterResolutionException {

        Parameter parameter = parameterContext.getParameter();
        return Objects
                .equals(
                        parameter.getParameterizedType().getTypeName(),
                        "java.util.Map<java.lang.String, bookstoread.Book>");
    }

    @Override
    public Map<String, Book> resolveParameter(
            final ParameterContext parameterContext,
            final ExtensionContext extensionContext) throws ParameterResolutionException {
        ExtensionContext.Store store = extensionContext.getStore(ExtensionContext.Namespace.create(Book.class));
        return (Map<String, Book>) store.getOrComputeIfAbsent("books", k -> getBooks());
    }

    private Map<String, Book>  getBooks() {
        return books;
    }

    public List<Book> createBooks() {
        return Arrays.asList(
                new Book("Effective Java", "Joshua Bloch", LocalDate.of(2008, Month.MAY, 8)),
                new Book("Code Complete", "Steve McConnel", LocalDate.of(2004, Month.JUNE, 9)),
                new Book("The Mythical Man-Month", "Frederick Phillips Brooks", LocalDate.of(1975, Month.JANUARY, 1)),
                new Book("Clean Code", "Robert C. Martin", LocalDate.of(2008, Month.AUGUST, 1)),
                new Book("Refactoring: Improving the Design of Existing Code", "Martin Fowler", LocalDate.of(2002, Month.MARCH, 9))
        );
    }
}
