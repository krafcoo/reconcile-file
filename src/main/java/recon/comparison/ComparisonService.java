package recon.comparison;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recon.comparison.ComparisonResult;
import recon.comparison.FileComparisonSummary;
import recon.matching.MatchingResult;
import recon.comparison.SimilarityComparator;
import recon.comparison.EqualityComparator;
import recon.model.Transaction;

import java.util.List;

@Service
public class ComparisonService {

    private final EqualityComparator identicalMatching;
    private final SimilarityComparator similarityComparator;

    @Autowired
    public ComparisonService(EqualityComparator identicalMatching, SimilarityComparator similarityComparator) {
        this.identicalMatching = identicalMatching;
        this.similarityComparator = similarityComparator;
    }

    public ComparisonResult compare(String firstFileName,
                                    List<Transaction> firstTxns,
                                    String secondFileName,
                                    List<Transaction> secondTxns,
                                    List<String> similarityFields) {
        final MatchingResult equalityResult = identicalMatching.compare(firstTxns, secondTxns);
        final MatchingResult similarityResult = similarityComparator.compare(equalityResult.getNotMatched1(), equalityResult.getNotMatched2(), similarityFields);

        final FileComparisonSummary fileComparisonSummary1 = FileComparisonSummary.builder()
                .fileName(firstFileName)
                .total(firstTxns.size())
                .matched(equalityResult.getMatched1().size())
                .similar(similarityResult.getMatched1().size())
                .unmatched(similarityResult.getNotMatched1().size())
                .unmatchedRecords(similarityResult.getNotMatched1()).build();
        final FileComparisonSummary fileComparisonSummary2 = FileComparisonSummary.builder()
                .fileName(secondFileName)
                .total(secondTxns.size())
                .matched(equalityResult.getMatched2().size())
                .similar(similarityResult.getMatched2().size())
                .unmatched(similarityResult.getNotMatched2().size())
                .unmatchedRecords(similarityResult.getNotMatched2()).build();

        return new ComparisonResult(fileComparisonSummary1,fileComparisonSummary2, similarityResult.getMatched1());
    }
}
