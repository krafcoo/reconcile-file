package recon.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class Transaction {
    private String profileName;
    private String date;
    private String amount;
    private String narrative;
    private String description;
    private String id;
    private String type;
    private String walletReference;

    @JsonIgnore
    public int getTransactionAmountInt() {
        return Integer.parseInt(amount);
    }

    public static Transaction fromLine(String line) throws UnparsableTransactionLineException {
        final String[] csvLine = line.split(",");
        if (csvLine.length == 8) {
            final Transaction transaction = new Transaction(csvLine[0], csvLine[1], csvLine[2], csvLine[3], csvLine[4], csvLine[5], csvLine[6], csvLine[7]);
            try {
                transaction.getTransactionAmountInt();
            } catch (Exception e) {
                throw new UnparsableTransactionLineException(line, e);
            }
            return transaction;
        }
        throw new UnparsableTransactionLineException(line);
    }


}
