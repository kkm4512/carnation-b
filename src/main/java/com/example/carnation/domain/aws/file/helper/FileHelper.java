package com.example.carnation.domain.aws.file.helper;

import com.example.carnation.common.exception.FileException;
import com.example.carnation.common.response.enums.FileApiResponseEnum;
import com.example.carnation.domain.aws.helper.S3Helper;
import com.example.carnation.domain.care.entity.CareMedia;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j(topic = "FileHelper")
@RequiredArgsConstructor
public class FileHelper {
    private final S3Helper s3Helper;
    /**
     * 파일을 저장 메서드
     */
    public void uploads(final List<MultipartFile> multipartFiles, final List<CareMedia> entities) {
        try {
            for (int i = 0; i < multipartFiles.size(); i++) {
                MultipartFile multipartFile = multipartFiles.get(i);
                CareMedia media = entities.get(i);
                String filePath = media.getFileRelativePath();
                s3Helper.upload(Path.of(filePath),multipartFile);
            }
        } catch (Exception e) {
            throw new FileException(FileApiResponseEnum.FILE_UPLOAD_FAILED,e);
        }
    }
}
