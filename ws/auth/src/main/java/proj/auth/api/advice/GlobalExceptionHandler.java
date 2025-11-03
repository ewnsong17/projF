// advice/GlobalExceptionHandler.java
package proj.auth.api.advice;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import proj.shared.model.common.ErrorRes;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRes> badReq(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorRes.builder().code("AUTH_ERROR").message(e.getMessage()).build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes> invalid(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(ErrorRes.builder().code("INVALID").message(e.getBindingResult().toString()).build());
    }
}
