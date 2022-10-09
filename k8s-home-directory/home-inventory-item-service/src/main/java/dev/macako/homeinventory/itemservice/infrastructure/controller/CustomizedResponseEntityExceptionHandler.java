package dev.macako.homeinventory.itemservice.infrastructure.controller;

import dev.macako.homeinventory.itemservice.domain.model.UserNotFoundException;
import dev.macako.homeinventory.itemservice.infrastructure.model.ErrorDetails;
import feign.FeignException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({UserNotFoundException.class, FeignException.NotFound.class})
  public ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request)
      throws Exception {
    ErrorDetails errorDetails =
        new ErrorDetails(now(), ex.getMessage(), request.getDescription(false));

    return new ResponseEntity<>(errorDetails, NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request)
      throws Exception {
    ErrorDetails errorDetails =
        new ErrorDetails(now(), ex.getMessage(), request.getDescription(false));

    return new ResponseEntity<>(errorDetails, INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ErrorDetails errorDetails =
        new ErrorDetails(
            now(),
            "Total Errors:"
                + ex.getErrorCount()
                + " First Error:"
                + ex.getFieldError().getDefaultMessage(),
            request.getDescription(false));

    return new ResponseEntity<>(errorDetails, BAD_REQUEST);
  }
}
