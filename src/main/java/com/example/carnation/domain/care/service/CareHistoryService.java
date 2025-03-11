package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.constans.MediaType;
import com.example.carnation.domain.care.cqrs.CareAssignmentQuery;
import com.example.carnation.domain.care.cqrs.CareHistoryCommand;
import com.example.carnation.domain.care.cqrs.CareHistoryQuery;
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
import java.util.stream.Stream;

@Service
@Slf4j(topic = "CareHistoryService")
@RequiredArgsConstructor
public class CareHistoryService {
    private final CareAssignmentQuery careAssignmentQuery;
    private final CareHistoryCommand careHistoryCommand;
    private final CareHistoryQuery careHistoryQuery;
    private final FileHelper fileHelper;

    private static final String AWS_BASE_URL =  "https://carnation-b-bucket.s3.ap-northeast-2.amazonaws.com";


    public CareHistory create(AuthUser authUser, Long careAssignmentId, CareHistoryRequestDto dto) {
        User user = User.of(authUser);
        log.info("[간병 기록 생성] 요청자: userId={}, 배정 ID: {}", user.getId(), careAssignmentId);
        FileValidation.countImagesFiles(dto.getImageFiles());
        FileValidation.countVideoFiles(dto.getVideoFiles());
        List<MultipartFile> multipartFiles = Stream.concat(dto.getImageFiles().stream(), dto.getVideoFiles().stream()).toList();
        log.info("[간병 기록 생성] 업로드할 파일 개수: {} (이미지: {}, 비디오: {})", multipartFiles.size(), dto.getImageFiles().size(), dto.getVideoFiles().size());
        CareAssignment careAssignment = careAssignmentQuery.findOne(careAssignmentId);
        user.isMe(careAssignment.getUser().getId());
        CareHistory careHistory = CareHistory.of(careAssignment, dto);
        log.info("[간병 기록 생성] 간병 기록 객체 생성 완료 (careAssignmentId={})", careAssignmentId);
        List<CareMedia> medias = new ArrayList<>();
        for (MultipartFile file : dto.getImageFiles()) {
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
        log.info("[간병 기록 생성] 이미지 파일 처리 완료: {}개", dto.getImageFiles().size());

        for (MultipartFile file : dto.getVideoFiles()) {
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
        log.info("[간병 기록 생성] 비디오 파일 처리 완료: {}개", dto.getVideoFiles().size());
        careHistory.getMedias().addAll(medias);
        log.info("[간병 기록 생성] CareHistory에 미디어 데이터 추가 완료 (총 {}개)", medias.size());
        CareHistory savedCareHistory = careHistoryCommand.save(careHistory);
        log.info("[간병 기록 생성] 간병 기록 저장 완료 (careHistoryId={})", savedCareHistory.getId());
        fileHelper.uploads(multipartFiles, medias);
        log.info("[간병 기록 생성] 파일 업로드 완료");
        return savedCareHistory;
    }



    /**
     * 현재 로그인한 사용자와, 요청한 간병 배정 데이터의 사용자가 같은지 확인
     * 같다면, 해당 간병 배정속, 간병인이 작성한 모든 데이터 반환
     */
    public Page<CareHistoryResponseDto> readAllMePage(AuthUser authUser, Long careAssignmentId, Pageable pageable) {
        User user = User.of(authUser);
        log.info("[간병 기록 조회] 요청자: userId={}, 배정 ID: {}", user.getId(), careAssignmentId);

        CareAssignment careAssignment = careAssignmentQuery.findOne(careAssignmentId);
        user.isMe(careAssignment.getUser().getId());

        Page<CareHistory> responses = careHistoryQuery.readAllMePage(careAssignment.getCaregiver(), pageable);
        log.info("[간병 기록 조회] 완료 (caregiverId={}, 기록 개수={})", careAssignment.getCaregiver().getId(), responses.getTotalElements());

        return responses.map(CareHistoryResponseDto::of);
    }


}
