package bookstoread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public interface FilterBoundaryTests {
    BookFilter get();

    @Test
    @DisplayName("should not fail for null book.")
    default void nullTest() {
        assertThat(get().apply(null)).isFalse();
    }
}
