package com.example.carnation.domain.aws.helper;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.carnation.common.exception.AwsException;
import com.example.carnation.common.exception.FileException;
import com.example.carnation.common.response.enums.AwsApiResponse;
import com.example.carnation.common.response.enums.FileApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Component
@Slf4j(topic = "S3Helper")
@RequiredArgsConstructor
public class S3Helper {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public void upload(Path path, MultipartFile file) {
        log.info("[파일 업로드 시작] 파일 경로: {}, 파일 크기: {} bytes, 파일 타입: {}",
                path.toString(), file.getSize(), file.getContentType());

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, path.toString(), file.getInputStream(), metadata);

            log.info("[파일 업로드 완료] 파일 경로: {}, 파일 크기: {} bytes", path, file.getSize());
        } catch (IOException e) {
            log.error("[파일 업로드 실패] 파일 읽기 오류 - 경로: {}, 오류 메시지: {}", path, e.getMessage(), e);
            throw new FileException(FileApiResponse.FILE_READ_ERROR, e);
        } catch (AmazonServiceException e) {
            log.error("[파일 업로드 실패] AWS S3 서비스 오류 - 경로: {}, 오류 메시지: {}", path, e.getMessage(), e);
            throw new AwsException(AwsApiResponse.AWS_STORAGE_ERROR, e);
        } catch (SdkClientException e) {
            log.error("[파일 업로드 실패] AWS 네트워크 오류 - 경로: {}, 오류 메시지: {}", path, e.getMessage(), e);
            throw new AwsException(AwsApiResponse.AWS_CONNECTION_ERROR, e);
        } catch (Exception e) {
            log.error("[파일 업로드 실패] 기타 예외 - 경로: {}, 오류 메시지: {}", path, e.getMessage(), e);
            throw new FileException(FileApiResponse.FILE_STORAGE_ERROR, e);
        }
    }

}
