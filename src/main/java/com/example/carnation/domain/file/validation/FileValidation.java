package com.example.carnation.domain.file.validation;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.FileApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileValidation {
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB (이미지)
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB (동영상)

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp");
    private static final List<String> ALLOWED_VIDEO_EXTENSIONS = Arrays.asList("mp4", "avi", "mov", "wmv", "flv", "mkv");

    /**
     * 이미지 파일 검증 (크기 및 확장자)
     * @param file 업로드된 MultipartFile
     */
    public static void validateImageFile(MultipartFile file) {
        validateFile(file, MAX_IMAGE_SIZE, ALLOWED_IMAGE_EXTENSIONS, FileApiResponse.INVALID_IMAGE_FILE);
    }

    /**
     * 동영상 파일 검증 (크기 및 확장자)
     * @param file 업로드된 MultipartFile
     */
    public static void validateVideoFile(MultipartFile file) {
        validateFile(file, MAX_VIDEO_SIZE, ALLOWED_VIDEO_EXTENSIONS, FileApiResponse.INVALID_VIDEO_FILE);
    }

    /**
     * 파일 크기 및 확장자 검증 (공통 메서드)
     * @param file 업로드된 파일
     * @param maxSize 허용된 최대 크기
     * @param allowedExtensions 허용된 확장자 리스트
     * @param errorResponse 확장자 오류 시 반환할 예외 응답 Enum
     */
    private static void validateFile(MultipartFile file, long maxSize, List<String> allowedExtensions, FileApiResponse errorResponse) {
        if (file == null || file.isEmpty()) {
            throw new CareException(FileApiResponse.EMPTY_FILE);
        }

        if (file.getSize() > maxSize) {
            throw new CareException(FileApiResponse.FILE_SIZE_EXCEEDED);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isAllowedExtension(originalFilename, allowedExtensions)) {
            throw new CareException(errorResponse);
        }
    }

    /**
     * 확장자가 허용된 목록에 포함되는지 확인
     * @param filename 파일 이름
     * @param allowedExtensions 허용된 확장자 리스트
     * @return 허용된 경우 true, 아니면 false
     */
    private static boolean isAllowedExtension(String filename, List<String> allowedExtensions) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return allowedExtensions.contains(extension);
    }
}
