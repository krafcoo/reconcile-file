package recon.comparison;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import recon.model.Transaction;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileComparisonSummary {
    private String fileName;
    private int total;
    private int matched;
    private int similar;
    private int unmatched;
    private List<Transaction> unmatchedRecords;

}
