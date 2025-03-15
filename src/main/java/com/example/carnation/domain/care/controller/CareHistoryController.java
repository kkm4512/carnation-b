package com.example.carnation.domain.care.controller;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.care.dto.CareHistoryFilesRequestDto;
import com.example.carnation.domain.care.dto.CareHistoryRequestDto;
import com.example.carnation.domain.care.dto.CareHistoryResponseDto;
import com.example.carnation.domain.care.service.CareHistoryService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-history")
@Tag(name = "CareHistory API", description = "간병 기록 관리 API")
public class CareHistoryController {
    private final CareHistoryService careHistoryService;

    @PostMapping(value = "/{careMatchingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "간병 기록 등록",
            description = "간병기록을 등록합니다."
    )
    public ApiResponse<Void> generate(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long careMatchingId,
            @RequestPart(value = "careHistoryRequestDto") @Valid CareHistoryRequestDto careHistoryRequestDto,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "videoFiles", required = false) List<MultipartFile> videoFiles
    ) {
        CareHistoryFilesRequestDto careHistoryFilesRequestDto = CareHistoryFilesRequestDto.of(imageFiles, videoFiles);
        careHistoryService.generate(authUser, careMatchingId, careHistoryRequestDto, careHistoryFilesRequestDto);
        return ApiResponse.of(SUCCESS);
    }




    @GetMapping(value = "/{careMatchingId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "특정 간병 매칭에 대한, 자신의 모든 간병인 히스토리를 조회합니다."
    )
    public ApiResponse<Page<CareHistoryResponseDto>> findPageMe(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long careMatchingId,
            @Parameter(description = "페이징 처리에 필요한 page(페이지 번호), size(페이지 크기), sort(정렬 방식), sortBy(정렬 기준 필드)")
            @ModelAttribute @Valid PageSearchDto pageSearchDto
    ) {
        Pageable pageable = PageSearchDto.of(pageSearchDto);
        Page<CareHistoryResponseDto> response = careHistoryService.findPageMe(authUser, careMatchingId, pageable);
        return ApiResponse.of(SUCCESS,response);
    }
}

