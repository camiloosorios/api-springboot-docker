package com.bosorio.Api.services;

import com.bosorio.Api.Exceptions.BadRequestException;
import com.bosorio.Api.Exceptions.InternalServerErrorException;
import com.bosorio.Api.Exceptions.NotFoundException;
import com.bosorio.Api.dto.ProductDto;
import com.bosorio.Api.entities.Product;
import com.bosorio.Api.repositories.ProductRepository;
import com.bosorio.Api.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test create product successfully")
    void testCreateProductSuccessfully() {
        ProductDto productDto = ProductDto.builder()
                .name("Product 1")
                .price(BigDecimal.valueOf(1500))
                .build();

        assertDoesNotThrow(() -> productService.create(productDto));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test create invalid product throw bad request exception")
    void createInvalidProductThrowsBadRequestException() {
        ProductDto productDto = new ProductDto();
        productDto.setName("");
        productDto.setPrice(BigDecimal.valueOf(100));

        assertThrows(BadRequestException.class, () -> productService.create(productDto));
    }

    @Test
    @DisplayName("Test get all products")
    void getAllReturnsListOfProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        assertEquals(2, productService.getAll().size());
    }

    @Test
    @DisplayName("Test get product by id")
    void getByIdProductExistsReturnsProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = productService.getById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void getById_ProductDoesNotExist_ThrowsNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getById(1L));
    }

    @Test
    void delete_ProductExists_Success() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void delete_ProductDoesNotExist_ThrowsNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.delete(1L));
    }
}
