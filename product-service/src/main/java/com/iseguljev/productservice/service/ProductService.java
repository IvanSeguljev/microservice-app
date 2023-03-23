package com.iseguljev.productservice.service;

import com.iseguljev.productservice.dto.ProductRequest;
import com.iseguljev.productservice.dto.ProductResponse;
import com.iseguljev.productservice.model.Product;
import com.iseguljev.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

        return products.stream().map(x->ProductResponse.builder()
                        .id(x.getId())
                        .name(x.getName())
                        .description(x.getDescription())
                        .price(x.getPrice())
                        .build())
                .toList();
    }
}
