package com.expensetracker.expensetracker.exception;

import com.expensetracker.expensetracker.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation error on {}: {}", request.getRequestURI(), ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", request, errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Conflict: duplicate entry or constraint violation", request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        log.warn("User lookup failed on {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleExpenseNotFoundException(ExpenseNotFoundException ex, HttpServletRequest request) {
        log.warn("Expense lookup failed on {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingRequestHeaderException(MissingRequestHeaderException ex, HttpServletRequest request) {
        log.warn("Missing required header on {}: {}", request.getRequestURI(), ex.getHeaderName());
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing required header: " + ex.getHeaderName(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.warn("Invalid request parameter or header value on {} for {}: {}", request.getRequestURI(), ex.getName(), ex.getValue());
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid value for " + ex.getName(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("No resource found on {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Resource not found", request);
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponseDTO> handleErrorResponseException(ErrorResponseException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        log.warn("Handled framework exception on {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(status, ex.getBody().getDetail(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error on {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        return buildResponse(status, message, request, null);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message, HttpServletRequest request, Map<String, String> validationErrors) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
