package com.microservices.productservice.service;

import com.microservices.productservice.model.Product;
import com.microservices.productservice.model.dto.ProductRequest;
import com.microservices.productservice.model.dto.ProductResponse;
import com.microservices.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {}: {} saved", product.getId(), product.getName());
    }

    public List<ProductResponse> getAllProducts(){
        log.info("All products are requested");
        return productRepository.findAll().stream().map( product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build()
        ).toList();
    }
}
