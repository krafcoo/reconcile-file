package recon.matching;

import org.springframework.stereotype.Component;
import recon.model.Transaction;

import java.util.Objects;
@Component
public class WalletReferenceMatcher implements TransactionFieldMatcher {
    @Override
    public String getId() {
        return "wallet";
    }

    @Override
    public String getDescription() {
        return "Wallet reference";
    }

    @Override
    public boolean match(Transaction that, Transaction other) {
        return Objects.equals(that.getWalletReference(), other.getWalletReference());
    }
}
