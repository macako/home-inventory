package dev.macako.homeinventory.userservice.infrastructure.controller;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import dev.macako.homeinventory.userservice.domain.model.UserNotFoundException;
import dev.macako.homeinventory.userservice.infrastructure.model.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request)
      throws Exception {
    ErrorDetails errorDetails = createErrorDetails(ex.getMessage(), request);

    return new ResponseEntity<>(errorDetails, NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request)
      throws Exception {
    ErrorDetails errorDetails = createErrorDetails(ex.getMessage(), request);

    return new ResponseEntity<>(errorDetails, INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ErrorDetails errorDetails =
        createErrorDetails(
            format(
                "Total Errors: %s First Error: %s",
                ex.getErrorCount(), ex.getFieldError().getDefaultMessage()),
            request);

    return new ResponseEntity<>(errorDetails, BAD_REQUEST);
  }

  private ErrorDetails createErrorDetails(String ex, WebRequest request) {
    return ErrorDetails.builder()
        .timestamp(now())
        .message(ex)
        .details(request.getDescription(false))
        .build();
  }
}
