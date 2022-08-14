package recon.matching;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import recon.model.Transaction;

import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
public class TxnPair {

    private Transaction first;
    private Transaction second;

    public TxnPair(Transaction first, Transaction second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TxnPair txnPair = (TxnPair) o;
        return first.equals(txnPair.first) && second.equals(txnPair.second) ||
                first.equals(txnPair.second) && second.equals(txnPair.first);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first) + Objects.hash(second);
    }
}
