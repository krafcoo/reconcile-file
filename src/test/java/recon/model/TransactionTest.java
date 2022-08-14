package recon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    @Test
    void shouldParseCorrectLine() {
        final Transaction t = Transaction.fromLine("Card Campaign,2014-01-12 03:23:42,-25000,CAPITAL BANK              MOGODITSHANE  BW,DEDUCT,0384012122267350,1,P_NzIyNTY4NzNfMTM4NjY3ODk2MC4wNzcx");
        assertEquals("Card Campaign", t.getProfileName());
        assertEquals("2014-01-12 03:23:42", t.getDate());
        assertEquals("-25000", t.getAmount());
        assertEquals("CAPITAL BANK              MOGODITSHANE  BW", t.getNarrative());
        assertEquals("DEDUCT", t.getDescription());
        assertEquals("0384012122267350", t.getId());
        assertEquals("1", t.getType());
        assertEquals("P_NzIyNTY4NzNfMTM4NjY3ODk2MC4wNzcx", t.getWalletReference());
    }

    @Test
    void shouldThrowExceptionOnIncorrectLine() {
        assertThrows(UnparsableTransactionLineException.class, () ->
                Transaction.fromLine("Card Campaign,-25000,CAPITAL BANK              MOGODITSHANE  BW,DEDUCT,0384012122267350,1,P_NzIyNTY4NzNfMTM4NjY3ODk2MC4wNzcx")
        );
    }
}