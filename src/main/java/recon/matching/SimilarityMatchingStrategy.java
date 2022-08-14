package recon.matching;

import org.springframework.stereotype.Component;
import recon.model.Transaction;
import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityServiceImpl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class SimilarityMatchingStrategy implements MatchingStrategy {

    private final StringSimilarityServiceImpl service;

    public SimilarityMatchingStrategy() {
        SimilarityStrategy strategy = new JaroWinklerStrategy();
        service = new StringSimilarityServiceImpl(strategy);
    }

    public MatchingResult compare(List<Transaction> txnsFirst, List<Transaction> txnsSecond) {
        final Set<TxnPair> similar1 = new HashSet<>();
        final Set<TxnPair> similar2 = new HashSet<>();
        final LinkedList<Transaction> notMatched1 = new LinkedList<>();
        final LinkedList<Transaction> notMatched2 = new LinkedList<>();
        notMatched1.addAll(txnsFirst);
        notMatched2.addAll(txnsSecond);
        for (Transaction transactionFirst : txnsFirst) {
            for (Transaction transactionSecond : txnsSecond) {
                final boolean narrativeSimilarity = service.score(transactionFirst.getNarrative(), transactionSecond.getNarrative()) > 0.9;
                final int amount1 = transactionFirst.getTransactionAmountInt();
                final int amount2 = transactionSecond.getTransactionAmountInt();
                final int difference = amount1 - amount2;
                final boolean amountSimilarity = Math.abs(difference) < 10;
                if (narrativeSimilarity && amountSimilarity) {
                    similar1.add(new TxnPair(transactionFirst, transactionSecond));
                    similar2.add(new TxnPair(transactionSecond, transactionFirst));
                    notMatched1.remove(transactionFirst);
                    notMatched2.remove(transactionSecond);
                }
            }
        }
        return MatchingResult.builder()
                .matched1(similar1)
                .matched2(similar2)
                .notMatched1(notMatched1)
                .notMatched2(notMatched2).build();
    }
}
