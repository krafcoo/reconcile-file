package recon.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Unable to compare the input")  // 422
public class ComparisonException extends Throwable {
    public ComparisonException(Exception e) {

    }
}
