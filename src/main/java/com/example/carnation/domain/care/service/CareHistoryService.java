package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.constans.MediaType;
import com.example.carnation.domain.care.cqrs.CareAssignmentQuery;
import com.example.carnation.domain.care.cqrs.CareHistoryCommand;
import com.example.carnation.domain.care.dto.CareHistoryRequestDto;
import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.entity.CareMedia;
import com.example.carnation.domain.file.helper.FileHelper;
import com.example.carnation.domain.file.util.FileUtil;
import com.example.carnation.domain.file.validation.FileValidation;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CareHistoryService {
    private final CareAssignmentQuery careAssignmentQuery;
    private final CareHistoryCommand careHistoryCommand;
    private final FileHelper fileHelper;

    public CareHistory create(AuthUser authUser, Long careAssignmentId, CareHistoryRequestDto dto) {
        User user = User.of(authUser);
        List<MultipartFile> multipartFiles = Stream.concat(
                dto.getImageFiles().stream(),
                dto.getVideoFiles().stream()
        ).toList();
        CareAssignment careAssignment = careAssignmentQuery.findOne(careAssignmentId);
        user.isMe(careAssignment.getUser().getId());
        CareHistory careHistory = CareHistory.of(careAssignment, dto);
        List<CareMedia> medias = new ArrayList<>();
        for (MultipartFile file : dto.getImageFiles()) {
            FileValidation.validateImageFile(file);
            String filePath = FileUtil.getUniqueFilePath(file,careAssignment.getId(),"/images");
            CareMedia careMedia = CareMedia.builder()
                    .filePath(filePath)
                    .mediaType(MediaType.IMAGE)
                    .careHistory(careHistory)
                    .fileName(file.getName())
                    .fileOriginName(file.getOriginalFilename())
                    .fileSize(String.valueOf(file.getSize()))
                    .build();
            medias.add(careMedia);
        }
        for (MultipartFile file : dto.getVideoFiles()) {
            FileValidation.validateVideoFile(file);
            String filePath = FileUtil.getUniqueFilePath(file,careAssignment.getId(),"/videos");
            CareMedia careMedia = CareMedia.builder()
                    .filePath(filePath)
                    .mediaType(MediaType.VIDEO)
                    .careHistory(careHistory)
                    .fileName(file.getName())
                    .fileOriginName(file.getOriginalFilename())
                    .fileSize(String.valueOf(file.getSize()))
                    .build();
            medias.add(careMedia);
        }
        careHistory.getMedias().addAll(medias);
        CareHistory savedCareHistory = careHistoryCommand.save(careHistory);
        fileHelper.uploads(multipartFiles,medias);
        return savedCareHistory;
    }


}
