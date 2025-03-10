package com.example.carnation.domain.file.helper;

import com.example.carnation.common.exception.FileException;
import com.example.carnation.common.response.enums.FileApiResponse;
import com.example.carnation.domain.care.entity.CareMedia;
import com.example.carnation.domain.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Component
@Slf4j(topic = "FileHelper")
@RequiredArgsConstructor
public class FileHelper {

    /**
     * 파일을 저장 메서드
     */
    public void uploads(List<MultipartFile> multipartFiles, List<CareMedia> entities) {
        try {
            for (int i = 0; i < multipartFiles.size(); i++) {
                MultipartFile multipartFile = multipartFiles.get(i);
                CareMedia media = entities.get(i);
                String filePath = media.getFilePath(); // 파일 저장 경로
                File file = FileUtil.mkdir(filePath); // 디렉토리 없으면 생성
                FileUtil.copyFile(multipartFile, file); // 해당 디렉토리 위치에 파일 생성
                log.info("파일 저장 완료: {}", file.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new FileException(FileApiResponse.FILE_UPLOAD_FAILED,e);
        }
    }
}
