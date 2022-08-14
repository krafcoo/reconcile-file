package recon.comparison;

import org.springframework.stereotype.Component;
import recon.matching.MatchingResult;
import recon.model.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class EqualityComparator {

    public MatchingResult compare(List<Transaction> transactionsFirst, List<Transaction> transactionsSecond) {
        final Map<String,Transaction> fileFirst = new HashMap<>();
        final Map<String, Transaction> fileSecond = new HashMap<>();

        for (Transaction transaction : transactionsFirst) {
            if (fileFirst.containsKey(transaction.getId())) {
                System.out.println();
            }
            fileFirst.put(transaction.getId(), transaction);
        }
        for (Transaction transaction : transactionsSecond) {
            if (fileSecond.containsKey(transaction.getId())) {
                System.out.println();
            }
            fileSecond.put(transaction.getId(), transaction);
        }

        List<Transaction> notMatched1 = new LinkedList<>();
        List<Transaction> notMatched2 = new LinkedList<>();
        Set<TxnPair> txnPair1 = new HashSet<>();
        Set<TxnPair> txnPair2 = new HashSet<>();
        compare(transactionsFirst, fileSecond, txnPair1, notMatched1);
        compare(transactionsSecond, fileFirst, txnPair2, notMatched2);

        return MatchingResult.builder().matched1(txnPair1).matched2(txnPair2).notMatched1(notMatched1).notMatched2(notMatched2).build();
    }

    private void compare(List<Transaction> txns, Map<String, Transaction> reference, Set<TxnPair> txnPair, List<Transaction> notMatched) {
        for (Transaction txn : txns) {
            final Transaction t2ById = reference.get(txn.getId());
            if (t2ById == null) {
                notMatched.add(txn);
            } else {
                txnPair.add(new TxnPair(txn, t2ById));
            }
        }
    }

}
