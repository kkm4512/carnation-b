package com.example.carnation.common.response;

import com.example.carnation.common.response.enums.ApiResponseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Schema(description = "공통 응답 모델")
public class ApiResponse<T> {
    @Schema(description = "HTTP 상태 코드", example = "200")
    private final int code;
    @Schema(description = "HTTP 상태 코드에 따른 커스텀 응답 메세지", example = "요청하신 작업에 성공 하였습니다")
    private final String message;
    @Schema(description = "반환할 데이터", example = "null | String | Object | []")
    private T data;

    // 데이터, 상태코드, 메시지 반환 생성자 (ApiResponseEnumImpl)
    public ApiResponse(ApiResponseEnum apiResponseEnum) {
        this.code = apiResponseEnum.getHttpStatus().value();
        this.message = apiResponseEnum.getMessage();
    }

    // 데이터, 상태코드, 메시지 반환 생성자 (ApiResponseEnumImpl)
    public ApiResponse(ApiResponseEnum apiResponseEnum, T data) {
        this.code = apiResponseEnum.getHttpStatus().value();
        this.message = apiResponseEnum.getMessage();
        this.data = data;
    }

    // 데이터, 상태코드, 메시지 반환 생성자 (data,httpStatus,message)
    public ApiResponse(HttpStatus httpStatus, String message, T data) {
        this.code = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    // 상태코드, 메세지, 파일 반환시 사용 (서비스 -> 컨트롤러)
    public static ApiResponse<FileResponse> of(ApiResponseEnum apiResponseEnum, FileResponse fileResponse) {
        return new ApiResponse<>(apiResponseEnum, fileResponse);
    }


    // 상태코드, 메세지만 반환시 사용 (서비스 -> 컨트롤러)
    @Schema(description = "데이터를 반환하지않는 공통 응답", example = "Return Void") // DTO 설명 추가
    public static <T> ApiResponse<T> of(ApiResponseEnum apiResponseEnum) {
        return new ApiResponse<>(apiResponseEnum);
    }

    // 데이터, 상태코드, 메세지만 반환시 사용 (서비스 -> 컨트롤러)
    @Schema(description = "데이터를 반환하는 공통 응답", example = "Return data<T>") // DTO 설명 추가
    public static <T> ApiResponse<T> of(ApiResponseEnum apiResponseEnum, T data) {
        return new ApiResponse<>(apiResponseEnum,data);
    }
}
