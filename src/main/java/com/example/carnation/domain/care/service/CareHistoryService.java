package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.constans.MediaType;
import com.example.carnation.domain.care.cqrs.CareAssignmentQuery;
import com.example.carnation.domain.care.cqrs.CareHistoryCommand;
import com.example.carnation.domain.care.cqrs.CareHistoryQuery;
import com.example.carnation.domain.care.dto.CareHistoryFilesRequestDto;
import com.example.carnation.domain.care.dto.CareHistoryRequestDto;
import com.example.carnation.domain.care.dto.CareHistoryResponseDto;
import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.entity.CareMedia;
import com.example.carnation.domain.file.helper.FileHelper;
import com.example.carnation.domain.file.util.FileUtil;
import com.example.carnation.domain.file.validation.FileValidation;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "CareHistoryService")
@RequiredArgsConstructor
public class CareHistoryService {
    private final CareAssignmentQuery careAssignmentQuery;
    private final CareHistoryCommand careHistoryCommand;
    private final CareHistoryQuery careHistoryQuery;
    private final FileHelper fileHelper;

    private static final String AWS_BASE_URL =  "https://carnation-b-bucket.s3.ap-northeast-2.amazonaws.com";


    public CareHistory create(AuthUser authUser, Long careAssignmentId, CareHistoryRequestDto careHistoryRequestDto, CareHistoryFilesRequestDto careHistoryFilesRequestDto) {
        User user = User.of(authUser);
        FileValidation.countImagesFiles(careHistoryFilesRequestDto.getImageFiles());
        FileValidation.countVideoFiles(careHistoryFilesRequestDto.getVideoFiles());
        List<MultipartFile> multipartFiles = FileUtil.toList(careHistoryFilesRequestDto.getImageFiles(), careHistoryFilesRequestDto.getVideoFiles());
        CareAssignment careAssignment = careAssignmentQuery.findOne(careAssignmentId);
        user.isMe(careAssignment.getUser().getId());
        CareHistory careHistory = CareHistory.of(careAssignment, careHistoryRequestDto);
        List<CareMedia> medias = new ArrayList<>();
        for (MultipartFile file : careHistoryFilesRequestDto.getImageFiles()) {
            FileValidation.validateImageFile(file);
            String relativePath = FileUtil.getUniqueFilePath(file, careAssignment.getId(), "/images");
            String absolutePath = Paths.get(AWS_BASE_URL, relativePath).toString();
            CareMedia careMedia = CareMedia.builder()
                    .fileRelativePath(relativePath)
                    .fileAbsolutePath(absolutePath)
                    .mediaType(MediaType.IMAGE)
                    .careHistory(careHistory)
                    .fileName(file.getName())
                    .fileOriginName(file.getOriginalFilename())
                    .fileSize(String.valueOf(file.getSize()))
                    .build();
            medias.add(careMedia);
        }

        for (MultipartFile file : careHistoryFilesRequestDto.getVideoFiles()) {
            FileValidation.validateVideoFile(file);
            String relativePath = FileUtil.getUniqueFilePath(file, careAssignment.getId(), "/videos");
            String absolutePath = Paths.get(AWS_BASE_URL, relativePath).toString();

            CareMedia careMedia = CareMedia.builder()
                    .fileRelativePath(relativePath)
                    .fileAbsolutePath(absolutePath)
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
        fileHelper.uploads(multipartFiles, medias);
        return savedCareHistory;
    }



    /**
     * 현재 로그인한 사용자와, 요청한 간병 배정 데이터의 사용자가 같은지 확인
     * 같다면, 해당 간병 배정속, 간병인이 작성한 모든 데이터 반환
     */
    public Page<CareHistoryResponseDto> readAllMePage(AuthUser authUser, Long careAssignmentId, Pageable pageable) {
        User user = User.of(authUser);
        CareAssignment careAssignment = careAssignmentQuery.findOne(careAssignmentId);
        user.isMe(careAssignment.getUser().getId());
        Page<CareHistory> responses = careHistoryQuery.readAllMePage(careAssignment.getCaregiver(), pageable);
        return responses.map(CareHistoryResponseDto::of);
    }


}
