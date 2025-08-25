package blog_backend.apiError;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import blog_backend.apiError.CustomException.EmailAlreadyUsed;
import blog_backend.apiError.CustomException.UserNotFoundException;

@ControllerAdvice
public class HandlerExceptionPersonnaliser {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handler(UserNotFoundException e){
        ApiError error = new ApiError();
        error.setMessage(e.getMessage());
        error.setCode(HttpStatus.NOT_FOUND.value());
        error.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyUsed.class)
    public ResponseEntity<ApiError> handler(EmailAlreadyUsed e){
        ApiError error = new ApiError();
        error.setMessage(e.getMessage());
        error.setCode(HttpStatus.BAD_REQUEST.value());
        error.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
