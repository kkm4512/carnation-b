package com.example.carnation.domain.care.controller;

import com.example.carnation.common.response.FileResponse;
import com.example.carnation.domain.care.service.CareRecordFileService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-records-files")
@Tag(name = "CareRecordFiles API", description = "간병 기록 파일 관리 API")
public class CareRecordFileController {
    
    private final CareRecordFileService careRecordFileService;

    @Operation(
            summary = "간병 매칭 PDF 다운로드",
            description = "특정 간병 매칭 기록을 PDF 파일로 변환하여 다운로드합니다."
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{careMatchingId}/pdf")
    public ResponseEntity<byte[]> createPdf(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long careMatchingId
    ) {
        byte[] pdfBytes = careRecordFileService.createPdf(authUser, careMatchingId);
        return FileResponse.of("카네이션 간병 서비스 이용내역 확인서" + ".pdf", pdfBytes);
    }
}
