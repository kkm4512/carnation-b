package com.example.carnation.common.exception;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.common.response.enums.ApiResponseEnum;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.*;

@RestControllerAdvice
@Slf4j(topic = "GlobalExceptionHandler")
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> baseException(BaseException e) {
        ApiResponseEnum apiResponseEnum = e.getApiResponseEnum();
        ApiResponse<Void> apiResponse = new ApiResponse<>(apiResponseEnum);
        return ResponseEntity.status(apiResponse.getCode()).body(apiResponse);
    }

    // @Valid 유효성 검사 실패 시 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(FAIL.getMessage());
        ApiResponse<Void> apiResponse = new ApiResponse<>(FAIL.getHttpStatus(), errorMessage, null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Validated 유효성 예외처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse(FAIL.getMessage());
        ApiResponse<Void> apiResponse = new ApiResponse<>(FAIL.getHttpStatus(), errorMessage, null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // SQL 관련 예외 처리 (DataIntegrityViolationException 포함)
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<String>> handleSQLException(Exception ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>(
                INTERNAL_SERVER_ERROR.getHttpStatus(),
                "데이터베이스 처리 중 오류가 발생했습니다.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    // 엔티티가 존재하지 않을 때 예외 처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>(NOT_FOUND.getHttpStatus(), NOT_FOUND.getMessage(), ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 지원되지 않는 HTTP 메서드 요청 예외 처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>(METHOD_NOT_ALLOWED.getHttpStatus(), METHOD_NOT_ALLOWED.getMessage(), ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 필수 요청 파라미터 누락 예외 처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>(FAIL.getHttpStatus(), FAIL.getMessage(), ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
