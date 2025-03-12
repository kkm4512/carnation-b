package com.example.carnation.domain.file.validation;

import com.example.carnation.common.exception.FileException;
import com.example.carnation.common.response.enums.FileApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j(topic = "FileValidation")
@RequiredArgsConstructor
public class FileValidation {
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB (이미지)
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB (동영상)

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp");
    private static final List<String> ALLOWED_VIDEO_EXTENSIONS = Arrays.asList("mp4", "avi", "mov", "wmv", "flv", "mkv");

    private static final int MAX_IMAGE_COUNT = 5; // 이미지 최대 개수
    private static final int MAX_VIDEO_COUNT = 1; // 동영상 최대 개수

    /**
     * 이미지 파일 개수 검증
     * @param files 업로드된 이미지 파일 목록
     */
    public static void countImagesFiles(List<MultipartFile> files) {
        if (files != null) {
            if (files.size() > MAX_IMAGE_COUNT) {
                log.error("[파일 개수 초과] 허용된 이미지 파일 개수({}) 초과: {}개", MAX_IMAGE_COUNT, files.size());
                throw new FileException(FileApiResponse.FILE_COUNT_IMAGE);
            }
        }
    }

    /**
     * 동영상 파일 개수 검증
     * @param files 업로드된 동영상 파일 목록
     */
    public static void countVideoFiles(List<MultipartFile> files) {
        if (files != null) {
            if (files.size() > MAX_VIDEO_COUNT) {
                log.error("[파일 개수 초과] 허용된 동영상 파일 개수({}) 초과: {}개", MAX_VIDEO_COUNT, files.size());
                throw new FileException(FileApiResponse.FILE_COUNT_VIDEO);
            }
        }
    }

    /**
     * 이미지 파일 검증 (크기 및 확장자)
     * @param file 업로드된 이미지 파일
     */
    public static void validateImageFile(MultipartFile file) {
        if (file != null) {
            validateFile(file, MAX_IMAGE_SIZE, ALLOWED_IMAGE_EXTENSIONS, FileApiResponse.INVALID_IMAGE_FILE);
        }

    }

    /**
     * 동영상 파일 검증 (크기 및 확장자)
     * @param file 업로드된 동영상 파일
     */
    public static void validateVideoFile(MultipartFile file) {
        if (file != null) {
            validateFile(file, MAX_VIDEO_SIZE, ALLOWED_VIDEO_EXTENSIONS, FileApiResponse.INVALID_VIDEO_FILE);
        }
    }

    /**
     * 파일 크기 및 확장자 검증 (공통 메서드)
     * @param file 업로드된 파일
     * @param maxSize 허용된 최대 크기
     * @param allowedExtensions 허용된 확장자 리스트
     * @param errorResponse 확장자 오류 시 반환할 예외 응답 Enum
     */
    private static void validateFile(MultipartFile file, long maxSize, List<String> allowedExtensions, FileApiResponse errorResponse) {
        if (file.getSize() > maxSize) {
            log.error("[파일 검증 실패] 파일 크기 초과: {} bytes (허용 최대: {} bytes) - {}", file.getSize(), maxSize, file.getOriginalFilename());
            throw new FileException(FileApiResponse.FILE_SIZE_EXCEEDED);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isAllowedExtension(originalFilename, allowedExtensions)) {
            log.error("[파일 검증 실패] 허용되지 않은 확장자: {}", originalFilename);
            throw new FileException(errorResponse);
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
