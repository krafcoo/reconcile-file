package recon.comparison;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recon.matching.MatchingResult;
import recon.matching.TransactionFieldMatcher;
import recon.model.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SimilarityComparator {

    private final StringSimilarityServiceImpl service;

    private final Map<String, TransactionFieldMatcher> matchers = new HashMap<>();

    @Autowired
    public SimilarityComparator(List<TransactionFieldMatcher> matchers) {
        matchers.stream().forEach(m -> this.matchers.put(m.getId(), m));
        SimilarityStrategy strategy = new JaroWinklerStrategy();
        service = new StringSimilarityServiceImpl(strategy);
    }


    public MatchingResult compare(List<Transaction> txnsFirst, List<Transaction> txnsSecond, List<String> similarityFields) {
        final Set<TxnPair> similar1 = new HashSet<>();
        final Set<TxnPair> similar2 = new HashSet<>();
        final LinkedList<Transaction> notMatched1 = new LinkedList<>();
        final LinkedList<Transaction> notMatched2 = new LinkedList<>();
        notMatched1.addAll(txnsFirst);
        notMatched2.addAll(txnsSecond);
        for (Transaction transactionFirst : txnsFirst) {
            for (Transaction transactionSecond : txnsSecond) {
                boolean matched = true;
                for (String key : similarityFields) {
                    final TransactionFieldMatcher transactionFieldMatcher = matchers.get(key);
                    if (!transactionFieldMatcher.match(transactionFirst, transactionSecond)) {
                        matched= false;
                        break;
                    }
                }
                if (matched) {
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