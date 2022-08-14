package recon.web;

import org.springframework.beans.factory.annotation.Autowired;
import recon.matching.ComparisonResult;
import recon.matching.FileComparisonSummary;
import recon.matching.MatchingResult;
import recon.matching.SimilarityMatchingStrategy;
import recon.matching.SimpleIdAndReferenceMatchingStrategy;
import recon.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComparisonService {

    private final SimpleIdAndReferenceMatchingStrategy identicalMatching;
    private final SimilarityMatchingStrategy similarityMatchingStrategy;

    @Autowired
    public ComparisonService(SimpleIdAndReferenceMatchingStrategy identicalMatching, SimilarityMatchingStrategy similarityMatchingStrategy) {
        this.identicalMatching = identicalMatching;
        this.similarityMatchingStrategy = similarityMatchingStrategy;
    }

    public ComparisonResult compare(String firstFileName, List<Transaction> firstTxns, String secondFileName, List<Transaction> secondTxns) {
        final MatchingResult equalityResult = identicalMatching.compare(firstTxns, secondTxns);
        final MatchingResult similarityResult = similarityMatchingStrategy.compare(equalityResult.getNotMatched1(), equalityResult.getNotMatched2());

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
