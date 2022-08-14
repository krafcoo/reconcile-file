package recon.matching;

import org.junit.jupiter.api.Test;
import recon.model.Transaction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimilarityMatchingStrategyTest {

    SimilarityMatchingStrategy similarityMatchingStrategy = new SimilarityMatchingStrategy(Arrays.asList(new NarrativeMatcher(), new AmountMatcher()));

    @Test
    void compare_similar_amount_narative() {
        final MatchingResult matchingResult = similarityMatchingStrategy.compare(
                Arrays.asList(Transaction.builder().narrative("this is simple test").amount("1000").build()),
                Arrays.asList(Transaction.builder().narrative("this is simple text").amount("1000").build()),
                Arrays.asList("narrative", "amount"));
        assertNotNull(matchingResult);
        assertEquals(1, matchingResult.getMatched1().size());
        assertEquals(1, matchingResult.getMatched2().size());
    }

    @Test
    void compare_similar_narrative() {
        final MatchingResult matchingResult = similarityMatchingStrategy.compare(
                Arrays.asList(Transaction.builder().narrative("this is simple test").amount("1000").build()),
                Arrays.asList(Transaction.builder().narrative("this is simple text").amount("1000").build()),
                Arrays.asList("narrative"));
        assertNotNull(matchingResult);
        assertEquals(1, matchingResult.getMatched1().size());
        assertEquals(1, matchingResult.getMatched2().size());
    }

    @Test
    void compare_not_similar_narrative() {
        final MatchingResult matchingResult = similarityMatchingStrategy.compare(
                Arrays.asList(Transaction.builder().narrative("other").amount("1000").build()),
                Arrays.asList(Transaction.builder().narrative("this is simple text").amount("1000").build()),
                Arrays.asList("narrative"));
        assertNotNull(matchingResult);
        assertEquals(0, matchingResult.getMatched1().size());
        assertEquals(0, matchingResult.getMatched2().size());
    }

    @Test
    void compare_not_similar_narrativeNo_AmountYes() {
        final MatchingResult matchingResult = similarityMatchingStrategy.compare(
                Arrays.asList(Transaction.builder().narrative("other").amount("1000").build()),
                Arrays.asList(Transaction.builder().narrative("this is simple text").amount("1000").build()),
                Arrays.asList("narrative", "amount"));
        assertNotNull(matchingResult);
        assertEquals(0, matchingResult.getMatched1().size());
        assertEquals(0, matchingResult.getMatched2().size());
    }

    @Test
    void compare_similar() {
        List<Transaction> left = new LinkedList<>();
        left.add(Transaction.fromLine("Card Campaign,2014-01-12 03:23:42,-25000,CAPITAL BANK              MOGODITSHANE  BW,DEDUCT,0384012122267350,1,P_NzIyNTY4NzNfMTM4NjY3ODk2MC4wNzcx"));
        left.add(Transaction.fromLine("Card Campaign,2014-01-12 04:43:34,-15000,*DARY SOUTHRING           GABORONE      BW,DEDUCT,0384012170157788,1,P_NzIzNDk2ODZfMTM4NTY1OTU3My4yMDQ1"));
        left.add(Transaction.fromLine("Card Campaign,2014-01-12 04:51:23,-28000,*MOGODIT ENGEN            GABORONE      BW,DEDUCT,0304012174832907,1,P_NzQzMjY5OTBfMTM4MDYxMjkwNS40MTM="));
        left.add(Transaction.fromLine("Card Campaign,2014-01-12 04:51:23,-20000,*MOGODIT ENGEN            GABORONE      BW,DEDUCT,0222222222222222,1,P_NzQzMjY5OTBfMTM4MDYxMjkwNS40MTM="));
        List<Transaction> right = new LinkedList<>();
        right.add(Transaction.fromLine("Card Campaign,2014-01-12 03:23:42,-25000,CAPITAL BANK              MOGODITSHANE  BW,DEDUCT,0384012122267350,1,P_NzIyNTY4NzNfMTM4NjY3ODk2MC4wNzcx"));
        right.add(Transaction.fromLine("Card Campaign,2014-01-12 04:43:34,-15000,*DARY SOUTHRING           GABORONE      BW,DEDUCT,0384012170157788,1,P_NzIzNDk2ODZfMTM4NTY1OTU3My4yMDQ1"));
        right.add(Transaction.fromLine("Card Campaign,2014-01-12 07:50:22,-20000,*BDF THEBEPHATSWA         MOLEPOLOLE    BW,DEDUCT,0304012282224503,1,P_NzUyODU4ODJfMTM4NjMyODY5Ni4wNzky"));
        right.add(Transaction.fromLine("Card Campaign,2014-01-12 07:50:22,-20001,*MOGODI ENGEN            GABORONE       BW,DEDUCT,0111111111111111,1,P_NzUyODU4ODJfMTM4NjMyODY5Ni4wNzky"));

        final MatchingResult matchingResult = similarityMatchingStrategy.compare(left, right, Arrays.asList("narrative", "amount"));
        assertNotNull(matchingResult);
        assertEquals(3, matchingResult.getMatched1().size());
        assertEquals(3, matchingResult.getMatched2().size());
        assertEquals(1, matchingResult.getNotMatched1().size());
        assertEquals(1, matchingResult.getNotMatched2().size());

    }
}