package com.example.carnation.domain.product.controller;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.product.dto.ProductResponseDto;
import com.example.carnation.domain.product.service.ProductService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 생성", description = "새로운 헬스케어 상품을 등록합니다.")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ApiResponse<ProductResponseDto> generateProduct(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody ProductResponseDto dto
    ) {
        ProductResponseDto response = productService.generate(authUser,dto);
        return ApiResponse.of(SUCCESS,response);
    }

    @GetMapping
    @Operation(summary = "상품 페이지네이션 조회", description = "등록되있는 헬스케어 상품을 반환합니다")
    public ApiResponse<Page<ProductResponseDto>> findPage(
            @ModelAttribute @Valid PageSearchDto pageSearchDto
    ){
        Pageable pageable = PageSearchDto.of(pageSearchDto);
        Page<ProductResponseDto> response = productService.findPage(pageable);
        return ApiResponse.of(SUCCESS, response);
    }
}
