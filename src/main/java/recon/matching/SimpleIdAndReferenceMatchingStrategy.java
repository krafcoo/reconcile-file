package recon.matching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recon.model.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SimpleIdAndReferenceMatchingStrategy implements MatchingStrategy {

    public MatchingResult compare(List<Transaction> transactions1, List<Transaction> transactions2) {
        final Map<String,Transaction> file1 = new HashMap<>();
        final Map<String, Transaction> file2 = new HashMap<>();

        for (Transaction transaction : transactions1) {
            if (file1.containsKey(transaction.getId())) {
                System.out.println();
            }
            file1.put(transaction.getId(), transaction);
        }
        for (Transaction transaction : transactions2) {
            if (file2.containsKey(transaction.getId())) {
                System.out.println();
            }
            file2.put(transaction.getId(), transaction);
        }

        List<Transaction> notMatched1 = new LinkedList<>();
        List<Transaction> notMatched2 = new LinkedList<>();
        Set<TxnPair> txnPair1 = new HashSet<>();
        Set<TxnPair> txnPair2 = new HashSet<>();
        compare(transactions1, file2, txnPair1, notMatched1);
        compare(transactions2, file1, txnPair2, notMatched2);

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
