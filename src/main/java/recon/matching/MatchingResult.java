package recon.matching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import recon.comparison.TxnPair;
import recon.model.Transaction;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@Builder
public class MatchingResult {

    private List<Transaction> notMatched1;
    private List<Transaction> notMatched2;
    private Set<TxnPair> matched1;
    private Set<TxnPair> matched2;

}
