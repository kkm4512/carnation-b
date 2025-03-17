package com.example.carnation.domain.aws.file.util;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUtil {

    public static String getFileExtension(MultipartFile file) {
        if (file.getOriginalFilename() == null || !file.getOriginalFilename().contains(".")) {
            return "";
        }
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
    }

    public static String getUniqueFilePath(MultipartFile file, Long id, String path) {
        String fileExtension = FileUtil.getFileExtension(file);
        String uniqueFileName = UUID.randomUUID() + fileExtension;
        return Paths.get("careAssignment", path, String.valueOf(id), uniqueFileName).toString();
    }

    public static <T> List<T> toList(List<T> list1, List<T> list2) {
        if (list1 == null && list2 == null) {
            return List.of(); // 두 리스트 모두 null이면 빈 리스트 반환
        }
        if (list1 == null) {
            return List.copyOf(list2); // list1이 null이면 list2 반환 (불변 리스트로 복사)
        }
        if (list2 == null) {
            return List.copyOf(list1); // list2가 null이면 list1 반환 (불변 리스트로 복사)
        }
        return Stream.concat(list1.stream(), list2.stream()).toList();
    }


}
