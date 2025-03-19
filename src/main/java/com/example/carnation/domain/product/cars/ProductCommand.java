package com.example.carnation.domain.product.cars;

import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCommand {
    private final ProductRepository repository;

    public Product create(Product entity) {
        return repository.save(entity);
    }
}
