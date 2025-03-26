package com.example.carnation.domain.order.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.order.dto.OrderRequestDto;
import com.example.carnation.domain.order.dto.OrderSearchDto;
import com.example.carnation.domain.order.dto.OrderSimpleResponseDto;
import com.example.carnation.domain.order.service.OrderService;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentSimpleResponseDto;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order API", description = "주문 생성 및 관리 API")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "주문 생성", description = "특정 상품(Product)에 대한 주문을 생성합니다.")
    public ApiResponse<KakaoPaymentSimpleResponseDto> generate(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid OrderRequestDto dto
    ) {

        KakaoPaymentSimpleResponseDto response = orderService.generate(authUser, dto);
        return ApiResponse.of(SUCCESS,response);
    }

    @Operation(
            summary = "내 주문 목록 조회",
            description = "로그인한 사용자의 주문 목록을 페이지 형태로 조회합니다. 결제 수단, 결제 여부로 필터링할 수 있습니다."
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/me")
    public ApiResponse<Page<OrderSimpleResponseDto>> findPageMe(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute @Valid OrderSearchDto dto
    ) {
        Page<OrderSimpleResponseDto> response = orderService.findPageMe(authUser, dto);
        return ApiResponse.of(SUCCESS, response);
    }
}
