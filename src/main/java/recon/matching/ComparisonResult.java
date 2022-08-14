package recon.matching;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
public class ComparisonResult {
    private FileComparisonSummary firstSummary;
    private FileComparisonSummary secondSummary;

    private List<TxnPair> unmatchedRecords;

    public ComparisonResult(FileComparisonSummary firstSummary,
                            FileComparisonSummary secondSummary,
                            Set<TxnPair> similar) {
        final List<TxnPair> txnPairs = new ArrayList<>();
        this.firstSummary = firstSummary;
        this.secondSummary = secondSummary;
        txnPairs.addAll(similar);
        firstSummary.getUnmatchedRecords().stream().map(t -> new TxnPair(t, null)).forEach(txnPairs::add);
        secondSummary.getUnmatchedRecords().stream().map(t -> new TxnPair(null, t)).forEach(txnPairs::add);
        this.unmatchedRecords = txnPairs;
    }
}
