package co.com.bancolombia.apirest;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@Order(-1)
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobal(Exception ex) {
        return ResponseEntity.unprocessableEntity()
                .body(new ErrorMessage(ex.getMessage(), Instant.now().toString()));
    }

    public record ErrorMessage(String message, String eventDate) {
    }
}
