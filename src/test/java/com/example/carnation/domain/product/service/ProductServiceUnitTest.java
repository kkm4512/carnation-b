package com.example.carnation.domain.product.service;

import com.example.carnation.domain.product.cars.ProductCommand;
import com.example.carnation.domain.product.cars.ProductQuery;
import com.example.carnation.domain.product.dto.ProductRequestDto;
import com.example.carnation.domain.product.dto.ProductResponseDto;
import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.user.MockUserInfo;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import com.example.carnation.domain.product.MockProductInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductCommand productCommand;

    @Mock
    private ProductQuery productQuery;

    @Mock
    private UserQuery userQuery;

    private AuthUser mockAuthUser;
    private User mockUser;
    private Product mockProduct;
    private ProductRequestDto mockProductRequestDto1;

    @BeforeEach
    void setUp() {
        mockAuthUser = MockUserInfo.getAuthUser1();
        mockUser = MockUserInfo.getUserNoId1();
        mockProductRequestDto1 = MockProductInfo.getProductRequestDto1();
        mockProduct = MockProductInfo.getProduct1();
    }

    @Test
    @DisplayName("상품 생성 성공 테스트")
    void test1() {
        // Given
        given(userQuery.readById(mockAuthUser.getUserId())).willReturn(mockUser);
        given(productCommand.create(any(Product.class))).willReturn(mockProduct);

        // When
        ProductResponseDto result = productService.generate(mockAuthUser, mockProductRequestDto1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(mockProduct.getId());
        verify(userQuery).readById(mockAuthUser.getUserId());
        verify(productCommand).create(any(Product.class));
    }

    @Test
    @DisplayName("상품 조회 성공 테스트")
    void test2() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(mockProduct), pageable, 1);
        given(productQuery.readPage(pageable)).willReturn(productPage);

        // When
        Page<ProductResponseDto> result = productService.findPage(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(productQuery).readPage(pageable);
    }
}
