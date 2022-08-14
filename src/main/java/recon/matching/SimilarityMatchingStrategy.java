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

    public MatchingResult compare(List<Transaction> transactions1, List<Transaction> transactions2) {
        final Set<TxnPair> similar1 = new HashSet<>();
        final Set<TxnPair> similar2 = new HashSet<>();
        final LinkedList<Transaction> notMatched1 = new LinkedList<>();
        final LinkedList<Transaction> notMatched2 = new LinkedList<>();
        notMatched1.addAll(transactions1);
        notMatched2.addAll(transactions2);
        for (Transaction transaction1 : transactions1) {
            for (Transaction transaction2 : transactions2) {
                final boolean narrativeSimilarity = service.score(transaction1.getNarrative(), transaction2.getNarrative()) > 0.9;
                final int amount1 = transaction1.getTransactionAmountInt();
                final int amount2 = transaction2.getTransactionAmountInt();
                final int difference = amount1 - amount2;
                final boolean amountSimilarity = Math.abs(difference) < 10;
                if (narrativeSimilarity && amountSimilarity) {
                    similar1.add(new TxnPair(transaction1, transaction2));
                    similar2.add(new TxnPair(transaction2, transaction1));
                    notMatched1.remove(transaction1);
                    notMatched2.remove(transaction2);
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
