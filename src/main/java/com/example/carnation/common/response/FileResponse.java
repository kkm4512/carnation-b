package com.example.carnation.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "파일 응답 모델")
public class FileResponse {
    @Schema(description = "파일명", example = "report.pdf")
    private String filename;

    @Schema(description = "파일 데이터 (바이트 배열)")
    private byte[] fileData;

    @Schema(description = "파일 크기", example = "102400")
    private long fileSize;

    /**
     * 파일 다운로드 응답을 생성하는 정적 메서드
     *
     * @param filename 다운로드할 파일명
     * @param fileData 파일 데이터 (byte[])
     * @return ResponseEntity<byte[]> 형태의 파일 응답
     */
    public static ResponseEntity<byte[]> of(String filename, byte[] fileData) {
        try {
            // 한글 파일명 인코딩 (브라우저 호환성을 위해)
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20"); // 공백을 %20으로 변환

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(fileData.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);
        } catch (Exception e) {
            throw new RuntimeException("파일명 인코딩 중 오류 발생", e);
        }
    }
}
