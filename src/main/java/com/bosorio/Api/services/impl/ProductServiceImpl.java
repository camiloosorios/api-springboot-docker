package com.bosorio.Api.services.impl;

import com.bosorio.Api.Exceptions.BadRequestException;
import com.bosorio.Api.Exceptions.InternalServerErrorException;
import com.bosorio.Api.Exceptions.NotFoundException;
import com.bosorio.Api.dto.ProductDto;
import com.bosorio.Api.entities.Product;
import com.bosorio.Api.repositories.ProductRepository;
import com.bosorio.Api.services.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void create(ProductDto productDto) {
        if (productDto.getName() == null || productDto.getName().isBlank()) {
            throw new BadRequestException("Name cannot be blank");
        }
        if (productDto.getPrice() == null || productDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Price must be greater than zero");
        }
        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .build();

        try {
            productRepository.save(product);

        } catch (RuntimeException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public List<ProductDto> getAll() {
        List<Product> products = (List<Product>) productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        products.forEach(product -> {
            productDtos.add(ProductDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .availability(product.getAvailability())
                    .createdAt(product.getCreatedAt())
                    .updatedAt(product.getUpdatedAt())
                    .build());
        });

        return productDtos;
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .availability(product.getAvailability())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public void update(ProductDto productDto, Long id) {
        ProductDto productToUpdate = getById(id);
        if (productDto.getName() == null || productDto.getName().isBlank()) {
            throw new BadRequestException("Name cannot be blank");
        }
        if (productDto.getPrice() == null || productDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Price must be greater than zero");
        }
        Product product = Product.builder()
                .id(id)
                .name(productDto.getName())
                .price(productDto.getPrice())
                .availability(productToUpdate.getAvailability())
                .createdAt(productToUpdate.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        try {
            productRepository.save(product);
        } catch (RuntimeException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public void updateAvailability(Long id) {
        ProductDto productToUpdate = getById(id);
        Product product = Product.builder()
                .id(id)
                .name(productToUpdate.getName())
                .price(productToUpdate.getPrice())
                .availability(!productToUpdate.getAvailability())
                .createdAt(productToUpdate.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        try {
            productRepository.save(product);
        } catch (RuntimeException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        try {
            productRepository.delete(product);
        } catch (RuntimeException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
