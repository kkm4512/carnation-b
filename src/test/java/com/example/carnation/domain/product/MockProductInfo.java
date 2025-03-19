package com.example.carnation.domain.product;

import com.example.carnation.domain.product.dto.ProductRequestDto;
import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.user.MockUserInfo;

public class MockProductInfo {

    public static ProductRequestDto getProductRequestDto1() {
        return new ProductRequestDto (
                "Test Product",
                "Test",
                10000,
                1,
                "의류",
                "Test Brand"
        );
    }

    public static Product getProduct1() {
        return Product.of(
                MockUserInfo.getUser1(),
                getProductRequestDto1()
        );
    }
}
