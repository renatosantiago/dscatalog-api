package com.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.dscatalog.services.exceptions.DataBaseException;
import com.dscatalog.services.exceptions.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {
  
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
    StandardError erro = new StandardError();
    erro.setTimeStamp(Instant.now());
    erro.setStatus(HttpStatus.NOT_FOUND.value());
    erro.setError("Resource not found");
    erro.setMessage(e.getMessage());
    erro.setPath(request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
  }

  @ExceptionHandler(DataBaseException.class)
  public ResponseEntity<StandardError> resourceNotFound(DataBaseException e, HttpServletRequest request) {
    StandardError erro = new StandardError();
    erro.setTimeStamp(Instant.now());
    erro.setStatus(HttpStatus.BAD_REQUEST.value());
    erro.setError("Database Exception");
    erro.setMessage(e.getMessage());
    erro.setPath(request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
    ValidationError erro = new ValidationError();
    erro.setTimeStamp(Instant.now());
    erro.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    erro.setError("Validation Exception");
    erro.setMessage(e.getMessage());
    erro.setPath(request.getRequestURI());

    for (FieldError f : e.getBindingResult().getFieldErrors()) {
      erro.addErrors(f.getField(), f.getDefaultMessage());
    }

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
  }

  @ExceptionHandler(AmazonServiceException.class)
  public ResponseEntity<StandardError> amazonExceptionHandler(AmazonServiceException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    StandardError erro = new StandardError();
    erro.setTimeStamp(Instant.now());
    erro.setStatus(status.value());
    erro.setError("AWS Exception");
    erro.setMessage(e.getMessage());
    erro.setPath(request.getRequestURI());
    return ResponseEntity.status(status).body(erro);
  
  }

  @ExceptionHandler(AmazonClientException.class)
  public ResponseEntity<StandardError> amazonExceptionHandler(AmazonClientException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    StandardError erro = new StandardError();
    erro.setTimeStamp(Instant.now());
    erro.setStatus(status.value());
    erro.setError("AWS Exception");
    erro.setMessage(e.getMessage());
    erro.setPath(request.getRequestURI());
    return ResponseEntity.status(status).body(erro);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<StandardError> ilegalArgument(IllegalArgumentException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    StandardError erro = new StandardError();
    erro.setTimeStamp(Instant.now());
    erro.setStatus(status.value());
    erro.setError("Bad request");
    erro.setMessage(e.getMessage());
    erro.setPath(request.getRequestURI());
    return ResponseEntity.status(status).body(erro);
  }
}
