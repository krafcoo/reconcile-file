package recon.model;

public class UnparsableTransactionLineException extends RuntimeException{
    public UnparsableTransactionLineException(String line) {
        super("Unable to parse line: " + line);
    }

    public UnparsableTransactionLineException(String line, Exception e) {
        super("Unable to parse line: " + line, e);
    }
}
