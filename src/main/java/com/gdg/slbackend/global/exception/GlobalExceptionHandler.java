package com.gdg.slbackend.global.exception;

import com.gdg.slbackend.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(GlobalException e) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = resolveHttpStatus(errorCode);

        // 비즈니스 예외는 warn 정도로 충분 (스택 필요하면 debug/trace로 변경)
        log.warn("GlobalException occurred. errorCode={}, message={}", errorCode, e.getMessage());

        return ResponseEntity.status(status)
                .body(ApiResponse.error(errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = "잘못된 요청입니다.";
        if (e.getBindingResult() != null && e.getBindingResult().getFieldError() != null) {
            message = e.getBindingResult().getFieldError().getDefaultMessage();
        }

        log.warn("Validation failed. message={}", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        // 여기서 반드시 스택 찍어야 진짜 원인 보임
        log.error("Unhandled exception occurred.", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }

    private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
        if (errorCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return switch (errorCode) {
            case INVALID_REQUEST -> HttpStatus.BAD_REQUEST;

            case UNAUTHORIZED, USER_BANNED -> HttpStatus.UNAUTHORIZED;

            case FORBIDDEN,
                 USER_NOT_SYSTEM_ADMIN,
                 COMMUNITY_NOT_ADMIN,
                 POST_MODIFY_FORBIDDEN,
                 COMMENT_MODIFY_FORBIDDEN,
                 RESOURCE_MODIFY_FORBIDDEN,
                 COMMUNITY_DELETE_FORBIDDEN -> HttpStatus.FORBIDDEN;

            case USER_NOT_FOUND,
                 COMMUNITY_NOT_FOUND,
                 POST_NOT_FOUND,
                 COMMENT_NOT_FOUND,
                 RESOURCE_NOT_FOUND,
                 RESOURCE_UPLOADER_NOT_FOUND,
                 RESOURCE_IMAGE_URL_NOT_FOUND -> HttpStatus.NOT_FOUND;

            case INSUFFICIENT_MILEAGE,
                 INVALID_FILE_URL,
                 INVALID_RESOURCE_IMAGE_URL -> HttpStatus.BAD_REQUEST;

            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
