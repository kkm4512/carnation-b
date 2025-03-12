package com.example.carnation.common.exception;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.common.response.enums.ApiResponseEnum;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.carnation.common.response.enums.BaseApiResponse.*;

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

    // 데이터 무결성 위반 예외 처리 (예: Unique Key 위반)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ApiResponse<Void> apiResponse = new ApiResponse<>(CONFLICT.getHttpStatus(), CONFLICT.getMessage() + ":" + ex.getMessage(), null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 엔티티가 존재하지 않을 때 예외 처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiResponse<Void> apiResponse = new ApiResponse<>(NOT_FOUND.getHttpStatus(), NOT_FOUND.getMessage() + ":" + ex.getMessage(), null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 지원되지 않는 HTTP 메서드 요청 예외 처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ApiResponse<Void> apiResponse = new ApiResponse<>(METHOD_NOT_ALLOWED.getHttpStatus(), METHOD_NOT_ALLOWED.getMessage() + ":" + ex.getMessage(), null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 필수 요청 파라미터 누락 예외 처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ApiResponse<Void> apiResponse = new ApiResponse<>(FAIL.getHttpStatus(), FAIL.getMessage() + ":" + ex.getMessage(), null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 기타 예외 처리 (포괄적인 예외 캐치)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        ApiResponse<String> apiResponse = ApiResponse.of(INTERNAL_SERVER_ERROR,e.getMessage());
        return ResponseEntity.status(apiResponse.getCode()).body(apiResponse);
    }
}
