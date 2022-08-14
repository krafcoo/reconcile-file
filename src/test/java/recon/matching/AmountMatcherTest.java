package recon.matching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recon.model.Transaction;

import static org.junit.jupiter.api.Assertions.*;

class AmountMatcherTest {

    private AmountMatcher amountMatcher;

    @BeforeEach
    void setUp() {
        amountMatcher = new AmountMatcher();
    }

    @Test
    void match_similar() {
        final Transaction first = Transaction.builder().amount("5000").build();
        final Transaction second = Transaction.builder().amount("5005").build();
        assertTrue(amountMatcher.match(first, second));
    }

    @Test
    void match_notsimilar() {
        final Transaction first = Transaction.builder().amount("5000").build();
        final Transaction second = Transaction.builder().amount("1005").build();
        assertFalse(amountMatcher.match(first, second));
    }
}