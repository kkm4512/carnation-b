package com.example.carnation.domain.product.service;

import com.example.carnation.domain.product.cars.ProductCommand;
import com.example.carnation.domain.product.cars.ProductQuery;
import com.example.carnation.domain.product.dto.ProductRequestDto;
import com.example.carnation.domain.product.dto.ProductResponseDto;
import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductCommand productCommand;
    private final ProductQuery productQuery;
    private final UserQuery userQuery;

    public ProductResponseDto generate(final AuthUser authUser, final ProductRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Product product = Product.of(user,dto);
        Product savedProduct = productCommand.create(product);
        return ProductResponseDto.of(savedProduct);
    }

    public Page<ProductResponseDto> findPage(Pageable pageable) {
        Page<Product> products = productQuery.readPage(pageable);
        return products.map(ProductResponseDto::of);
    }
}
