package recon.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Incorrect input")  // 400
public class IncorrectInputFilesException extends Exception {
    public IncorrectInputFilesException(Exception e) {
        super(e);
    }

    public IncorrectInputFilesException(String msg) {
        super(msg);
    }
}
