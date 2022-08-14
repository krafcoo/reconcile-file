package recon.matching;

import recon.model.Transaction;

public interface TransactionFieldMatcher {
    String getId();

    String getDescription();

    boolean match(Transaction that, Transaction other);
}
