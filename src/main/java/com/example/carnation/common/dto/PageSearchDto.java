package com.example.carnation.common.dto;

import com.example.carnation.common.dto.constans.SortByEnum;
import com.example.carnation.common.dto.constans.SortEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "페이징 및 정렬 요청 DTO")
public class PageSearchDto {

    @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
    @Schema(description = "조회할 페이지 번호 (0부터 시작)", example = "0")
    private Integer page;

    @PositiveOrZero(message = "사이즈는 0 이상이어야 합니다.")
    @Schema(description = "한 페이지에 포함될 데이터 개수", example = "10")
    private Integer size;

    @Schema(description = "정렬 방식 (오름차순: ASC, 내림차순: DESC)", example = "DESC")
    private SortEnum sort;

    @Schema(description = "정렬 기준 필드", example = "CREATED_AT")
    private SortByEnum sortBy;

    @Schema(description = "페이징 요청 객체 생성 메서드")
    public static PageRequest of(PageSearchDto pageSearchDto) {
        return PageRequest.of(
                pageSearchDto.getPage() == null ? 0 : pageSearchDto.getPage(),
                pageSearchDto.getSize() == null ? 10 : pageSearchDto.getSize(),
                Sort.by(
                        (pageSearchDto.getSort() != null ? pageSearchDto.getSort().getDirection() : Sort.Direction.DESC),
                        (pageSearchDto.getSortBy() != null ? pageSearchDto.getSortBy().getDescription() : "createdAt")
                )
        );
    }


}
