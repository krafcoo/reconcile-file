package recon.matching;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.springframework.stereotype.Component;
import recon.model.Transaction;

@Component
public class NarrativeMatcher implements TransactionFieldMatcher {

    private final StringSimilarityServiceImpl service;

    public NarrativeMatcher() {
        SimilarityStrategy strategy = new JaroWinklerStrategy();
        service = new StringSimilarityServiceImpl(strategy);
    }

    @Override
    public String getId() {
        return "narrative";
    }

    @Override
    public String getDescription() {
        return "Narrative";
    }

    @Override
    public boolean match(Transaction that, Transaction other) {
        return service.score(that.getNarrative(), other.getNarrative()) > 0.9;
    }

}
