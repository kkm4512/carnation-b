package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FileApiResponse implements ApiResponseEnum {

    // 400 Bad Request (잘못된 요청)
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "지원되지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 허용된 제한을 초과했습니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "업로드된 파일이 비어 있습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),
    INVALID_IMAGE_FILE(HttpStatus.BAD_REQUEST, "유효하지 않은 이미지 파일입니다."),
    INVALID_VIDEO_FILE(HttpStatus.BAD_REQUEST, "유효하지 않은 비디오 파일입니다."),
    DIRECTORY_MAKE_FAIL(HttpStatus.BAD_REQUEST, "디렉토리 생성에 실패 하였습니다"),
    FILE_READ_ERROR(HttpStatus.BAD_REQUEST, "파일을 읽는 중 오류가 발생했습니다."),
    FILE_COUNT_IMAGE(HttpStatus.BAD_REQUEST, "이미지 파일은 5개까지 첨부 가능합니다."),
    FILE_COUNT_VIDEO(HttpStatus.BAD_REQUEST, "동영상 파일은 1개까지 첨부 가능합니다."),


    // 403 Forbidden (권한 없음)
    FILE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 파일에 대한 접근 권한이 없습니다."),

    // 404 Not Found (파일 없음)
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 파일을 찾을 수 없습니다."),

    // 409 Conflict (파일 중복)
    FILE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 동일한 파일이 존재합니다."),

    // 500 Internal Server Error (서버 내부 오류)
    FILE_STORAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다."),
    FILE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 중 오류가 발생했습니다."),
    FILE_DELETION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 중 오류가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    FileApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
