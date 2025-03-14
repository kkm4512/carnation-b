package com.example.carnation.common.dto;

import com.example.carnation.common.dto.constans.SortByEnum;
import com.example.carnation.common.dto.constans.SortEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "페이징 및 정렬 요청 DTO")
public class PageSearchDto {

    @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
    @Schema(description = "조회할 페이지 번호 (0부터 시작)", example = "0")
    private Integer page = 0;

    @PositiveOrZero(message = "사이즈는 0 이상이어야 합니다.")
    @Schema(description = "한 페이지에 포함될 데이터 개수", example = "10")
    private Integer size = 10;

    @Schema(description = "정렬 방식 (오름차순: ASC, 내림차순: DESC)", example = "DESC")
    private SortEnum sort = SortEnum.DESC;

    @Schema(description = "정렬 기준 필드", example = "CREATED_AT")
    private SortByEnum sortBy = SortByEnum.CREATED_AT; // 기본 정렬 필드

    /**
     * SearchDto를 PageRequest로 변환
     * @param pageSearchDto 페이징 및 정렬 요청 DTO
     * @return PageRequest 객체
     */
    @Schema(description = "페이징 요청 객체 생성 메서드")
    public static PageRequest of(PageSearchDto pageSearchDto) {
        return PageRequest.of(
                pageSearchDto.getPage(),
                pageSearchDto.getSize(),
                pageSearchDto.getSort().getDirection(),
                pageSearchDto.getSortBy().getDescription()
        );
    }
}
