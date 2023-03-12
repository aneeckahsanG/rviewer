package com.rviewer.skeletons.utils.advice;

import com.rviewer.skeletons.utils.exception.BadRequestException;
import com.rviewer.skeletons.utils.exception.DispenserDoesNotExistException;
import com.rviewer.skeletons.utils.exception.DispenserSameStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionAdvise extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DispenserSameStatusException.class)
    public ResponseEntity handleDispenserSameStatusException(
            DispenserSameStatusException ex, WebRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(DispenserDoesNotExistException.class)
    public ResponseEntity handleDispenserDoesNotExistException(
            DispenserDoesNotExistException ex, WebRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleDispenserDoesNotExistException(
            BadRequestException ex, WebRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @Override
    protected ResponseEntity handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().body("Bad Request");
    }
    @Override
    protected ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        return ResponseEntity.internalServerError().body("Unexpected API error");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity processAllError(Exception ex) {
        return ResponseEntity.internalServerError().body(ex);

    }
}
