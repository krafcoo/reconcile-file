package recon.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import recon.matching.ComparisonResult;
import recon.model.Transaction;
import recon.model.UnparsableTransactionLineException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
public class FileUploadController {

    @Autowired
    private ComparisonService comparisonService;

    @PostMapping("/compare")
    public ComparisonResult handleFileUpload(@RequestParam("firstFile") MultipartFile firstFile, @RequestParam("secondFile") MultipartFile secondFile) throws IOException {

        final List<Transaction> transactions1 = parse(firstFile);
        final List<Transaction> transactions2 = parse(secondFile);

        return comparisonService.compare(firstFile.getOriginalFilename(), transactions1, secondFile.getOriginalFilename(), transactions2);
    }

    public List<Transaction> parse(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to process empty file.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        List<Transaction> txns = new LinkedList<Transaction>();
        if (reader.ready()) {
            reader.readLine();// skip first line
        }
        while (reader.ready()) {
            String line = reader.readLine();
            try {
                txns.add(Transaction.fromLine(line));
            } catch (UnparsableTransactionLineException e) {
                log.error("Error while parsing lines for file: " + file.getName(), e);
            }
        }
        return txns;

    }

}
