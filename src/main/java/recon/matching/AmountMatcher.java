package recon.matching;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.springframework.stereotype.Component;
import recon.model.Transaction;

@Component
public class AmountMatcher implements TransactionFieldMatcher {

    @Override
    public String getId() {
        return "amount";
    }

    @Override
    public String getDescription() {
        return "Amount";
    }

    @Override
    public boolean match(Transaction that, Transaction other) {
        final int amount1 = that.getTransactionAmountInt();
        final int amount2 = other.getTransactionAmountInt();
        final int difference = amount1 - amount2;
        return Math.abs(difference) < 10;
    }

}
