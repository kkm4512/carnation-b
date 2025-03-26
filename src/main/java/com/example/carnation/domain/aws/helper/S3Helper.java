package com.example.carnation.domain.aws.helper;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.carnation.common.exception.AwsException;
import com.example.carnation.common.exception.FileException;
import com.example.carnation.common.response.enums.AwsApiResponseEnum;
import com.example.carnation.common.response.enums.FileApiResponseEnum;
import com.example.carnation.init.PropertyInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Component
@Slf4j(topic = "S3Helper")
@RequiredArgsConstructor
public class S3Helper {
    private String bucket = PropertyInfo.AWS_S3_BUCKET;

    private final AmazonS3Client amazonS3Client;

    public void upload(Path path, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, path.toString(), file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new FileException(FileApiResponseEnum.FILE_READ_ERROR, e);
        } catch (AmazonServiceException e) {
            throw new AwsException(AwsApiResponseEnum.AWS_STORAGE_ERROR, e);
        } catch (SdkClientException e) {
            throw new AwsException(AwsApiResponseEnum.AWS_CONNECTION_ERROR, e);
        } catch (Exception e) {
            throw new FileException(FileApiResponseEnum.FILE_STORAGE_ERROR, e);
        }
    }

}
