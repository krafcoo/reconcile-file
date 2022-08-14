package matching;

import recon.matching.AmountMatcher;
import recon.matching.NarrativeMatcher;
import recon.comparison.SimilarityComparator;
import recon.comparison.EqualityComparator;
import recon.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recon.comparison.ComparisonResult;
import recon.comparison.ComparisonService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparisonServiceTest {


    private ComparisonService comparisonService;

    @BeforeEach
    void setUp() {
        comparisonService = new ComparisonService(new EqualityComparator(), new SimilarityComparator(Arrays.asList(new AmountMatcher(), new NarrativeMatcher())));
    }

    @Test
    void compare_only_identical() {
        List<Transaction> left = new LinkedList<>();
        left.add(Transaction.fromLine("Card Campaign,2014-01-12 03:23:42,-25000,CAPITAL BANK              MOGODITSHANE  BW,DEDUCT,0384012122267350,1,P_NzIyNTY4NzNfMTM4NjY3ODk2MC4wNzcx"));
        left.add(Transaction.fromLine("Card Campaign,2014-01-12 04:43:34,-15000,*DARY SOUTHRING           GABORONE      BW,DEDUCT,0384012170157788,1,P_NzIzNDk2ODZfMTM4NTY1OTU3My4yMDQ1"));
        left.add(Transaction.fromLine("Card Campaign,2014-01-12 04:51:23,-28000,*MOGODIT ENGEN            GABORONE      BW,DEDUCT,0304012174832907,1,P_NzQzMjY5OTBfMTM4MDYxMjkwNS40MTM="));
        List<Transaction> right = new LinkedList<>();
        right.add(Transaction.fromLine("Card Campaign,2014-01-12 03:23:42,-25000,CAPITAL BANK              MOGODITSHANE  BW,DEDUCT,0384012122267350,1,P_NzIyNTY4NzNfMTM4NjY3ODk2MC4wNzcx"));
        right.add(Transaction.fromLine("Card Campaign,2014-01-12 04:43:34,-15000,*DARY SOUTHRING           GABORONE      BW,DEDUCT,0384012170157788,1,P_NzIzNDk2ODZfMTM4NTY1OTU3My4yMDQ1"));
        right.add(Transaction.fromLine("Card Campaign,2014-01-12 07:50:22,-20000,*BDF THEBEPHATSWA         MOLEPOLOLE    BW,DEDUCT,0304012282224503,1,P_NzUyODU4ODJfMTM4NjMyODY5Ni4wNzky"));

        final ComparisonResult compare = comparisonService.compare("f1", left, "f2", right, Arrays.asList("narrative", "amount"));
        assertNotNull(compare);
        assertEquals(2, compare.getFirstSummary().getMatched());
        assertEquals(2, compare.getSecondSummary().getMatched());
        assertEquals(0, compare.getFirstSummary().getSimilar());
        assertEquals(0, compare.getSecondSummary().getSimilar());
        assertEquals(1, compare.getFirstSummary().getUnmatchedRecords().size());
        assertEquals(1, compare.getSecondSummary().getUnmatchedRecords().size());
        assertEquals(3, compare.getFirstSummary().getTotal());
        assertEquals(3, compare.getSecondSummary().getTotal());
    }

    @Test
    void compare_have_similar() {
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

        final ComparisonResult compare = comparisonService.compare("f1", left, "f2", right, Arrays.asList("narrative", "amount"));
        assertNotNull(compare);
        assertEquals(2, compare.getFirstSummary().getMatched());
        assertEquals(2, compare.getSecondSummary().getMatched());
        assertEquals(1, compare.getFirstSummary().getSimilar());
        assertEquals(1, compare.getSecondSummary().getSimilar());
        assertEquals(1, compare.getFirstSummary().getUnmatchedRecords().size());
        assertEquals(1, compare.getSecondSummary().getUnmatchedRecords().size());
        assertEquals(4, compare.getFirstSummary().getTotal());
        assertEquals(4, compare.getSecondSummary().getTotal());
    }
}