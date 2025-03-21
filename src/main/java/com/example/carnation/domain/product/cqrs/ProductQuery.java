package com.example.carnation.domain.product.cqrs;

import com.example.carnation.common.exception.ProductException;
import com.example.carnation.common.response.enums.ProductApiResponseEnum;
import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductQuery {
    private final ProductRepository repository;

    public Page<Product> readPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Product readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductException(ProductApiResponseEnum.PRODUCT_NOT_FOUND));
    }
}
