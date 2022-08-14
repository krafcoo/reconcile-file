package recon.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@Slf4j
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ComparisonError> handleException(Exception ex) {
        String error = ex.getClass().getSimpleName();
        ComparisonError apiError =
                new ComparisonError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(), error);
        return new ResponseEntity<ComparisonError>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ IncorrectInputFilesException.class })
    public ResponseEntity<ComparisonError> handleException(IncorrectInputFilesException ex) {
        String error = ex.getMessage();
        ComparisonError apiError =
                new ComparisonError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<ComparisonError>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}