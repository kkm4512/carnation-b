package com.example.carnation.domain.file.util;

import com.example.carnation.common.exception.FileException;
import com.example.carnation.common.response.enums.FileApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtil {
    public static String getFileExtension(MultipartFile file) {
        if (file.getOriginalFilename() == null || !file.getOriginalFilename().contains(".")) {
            return "";
        }
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
    }

    public static File mkdir(String path) {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            boolean flag = parentDir.mkdirs();
            if (!flag) {
                throw new FileException(FileApiResponse.DIRECTORY_MAKE_FAIL);
            }
        }
        return file;
    }

    public static String getUniqueFilePath(MultipartFile file, Long id, String path) {
        String fileExtension = FileUtil.getFileExtension(file);
        String uniqueFileName = UUID.randomUUID() + fileExtension;
        return Paths.get("careAssignment" + path, String.valueOf(id), uniqueFileName).toString();
    }

    /**
     * InputStream을 받아 파일로 저장하는 메서드
     */
    public static void copyFile(MultipartFile multipartFile, File file) {
        if (multipartFile == null || file == null) {
            throw new FileException(FileApiResponse.EMPTY_FILE);
        }
        try {
            try (InputStream inputStream = multipartFile.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(file);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {

                byte[] buffer = new byte[8192]; // 8KB 버퍼
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                }
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            throw new FileException(FileApiResponse.FILE_UPLOAD_FAILED, e);
        } catch (SecurityException e) {
            throw new FileException(FileApiResponse.FILE_ACCESS_DENIED, e);
        }
    }

}
