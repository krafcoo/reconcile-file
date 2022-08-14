package recon.matching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recon.model.Transaction;

import static org.junit.jupiter.api.Assertions.*;

class WalletReferenceMatcherTest {

    private WalletReferenceMatcher walletReferenceMatcher;

    @BeforeEach
    void setUp() {
        walletReferenceMatcher = new WalletReferenceMatcher();
    }

    @Test
    public void shouldMatch_sameWallet() {
        final Transaction first = Transaction.builder().walletReference("QWERTYUIOP").build();
        final Transaction second = Transaction.builder().walletReference("QWERTYUIOP").build();
        assertTrue(walletReferenceMatcher.match(first, second));
    }

    @Test
    public void shouldNotMatch_differentWallet() {
        final Transaction first = Transaction.builder().walletReference("--QWERTYUIOP--").build();
        final Transaction second = Transaction.builder().walletReference("QWERTYUIOP").build();
        assertFalse(walletReferenceMatcher.match(first, second));
    }

}