package recon.matching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import recon.model.Transaction;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class NarrativeMatcherTest {

    private NarrativeMatcher narrativeMatcher;

    @BeforeEach
    void setUp() {
        narrativeMatcher = new NarrativeMatcher();
    }

    @Test
    public void shouldMatch_similarText() {
        final Transaction first = Transaction.builder().narrative("This is simple text").build();
        final Transaction second = Transaction.builder().narrative("This is simple test").build();
        assertTrue(narrativeMatcher.match(first, second));
    }

    @Test
    public void shouldMatch_empty() {
        final Transaction first = Transaction.builder().narrative("").build();
        final Transaction second = Transaction.builder().narrative("").build();
        assertFalse(narrativeMatcher.match(first, second));
    }

    @Test
    public void shouldMatch_null() {
        final Transaction first = Transaction.builder().build();
        final Transaction second = Transaction.builder().build();

        assertThrows(NullPointerException.class, () -> narrativeMatcher.match(first, second));
    }

    @Test
    public void shouldNotMatch_differsTooMuch() {
        final Transaction first = Transaction.builder().narrative("This is simple text").build();
        final Transaction second = Transaction.builder().narrative("Could this be simple test").build();
        assertFalse(narrativeMatcher.match(first, second));
    }
}