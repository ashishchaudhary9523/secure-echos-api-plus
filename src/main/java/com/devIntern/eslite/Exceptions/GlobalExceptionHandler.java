package com.devIntern.eslite.Exceptions;

import com.devIntern.eslite.payload.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFound(ResourceNotFoundException resourceNotFoundException ,
                                                         WebRequest webRequest){
      ErrorDetails details = new ErrorDetails(new Date() ,
              resourceNotFoundException.getMessage() ,
              webRequest.getDescription(false)
              );

      return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(SecureEchoAPIException.class)
  public ResponseEntity<ErrorDetails> blogAPIException(SecureEchoAPIException secureEchoAPIException,
                                                       WebRequest webRequest) {

    ErrorDetails details = new ErrorDetails(new Date() ,
            secureEchoAPIException.getMessage() ,
            webRequest.getDescription(false)
            );

    return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> globalException(Exception exception ,
                                                      WebRequest webRequest){

      ErrorDetails details = new ErrorDetails(new Date() ,
              exception.getMessage() ,
              webRequest.getDescription(false)
      );

      return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatusCode status,
                                                                WebRequest request) {

    Map<String , String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError)error).getField();
      String message = error.getDefaultMessage();
      errors.put(fieldName, message);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
