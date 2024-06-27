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
                .name("Test Product")
                .price(BigDecimal.valueOf(1500))
                .build();

        assertDoesNotThrow(() -> productService.create(productDto));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test create product without name throw bad request exception")
    void invalidProductNameThrowsBadRequestException() {
        ProductDto productDto = ProductDto.builder()
                .name("")
                .price(BigDecimal.valueOf(100))
                .build();

        assertThrows(BadRequestException.class, () -> productService.create(productDto));
    }

    @Test
    @DisplayName("Test create product without name throw bad request exception")
    void invalidProductPriceThrowsBadRequestException() {
        ProductDto productDto = ProductDto.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(0))
                .build();

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
    void getProductByIdReturnsProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = productService.getById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    @DisplayName("Test get product does not exist throws not found exception")
    void getProductByIdDoesNotExistThrowsNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getById(1L));
    }

    @Test
    @DisplayName("Test update product successfully")
    void testUpdateProductSuccessfully() {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(1500))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductDto updatedProduct = ProductDto.builder()
                .name("Test Product Updated")
                .price(BigDecimal.valueOf(1500))
                .build();

        assertDoesNotThrow(() -> productService.update(updatedProduct, 1L));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test update product with invalid name throws bad request exception")
    void UpdateWithInvalidNameThrowsBadRequestException() {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(1500))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductDto updatedProduct = ProductDto.builder()
                .name("")
                .price(BigDecimal.valueOf(1500))
                .build();

        verify(productRepository, times(0)).save(any(Product.class));
        assertThrows(BadRequestException.class, () -> productService.update(updatedProduct, 1L));
    }

    @Test
    @DisplayName("Test update product with invalid price throws bad request exception")
    void UpdateWithInvalidPriceThrowsBadRequestException() {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(0))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductDto updatedProduct = ProductDto.builder()
                .name("Test Product Updated")
                .price(BigDecimal.valueOf(1500))
                .build();

        verify(productRepository, times(0)).save(any(Product.class));
        assertDoesNotThrow(() -> productService.update(updatedProduct, 1L));
    }

    @Test
    @DisplayName("Test update product availability successfully")
    void testUpdateProductAvailability() {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(1500))
                .availability(true)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> productService.updateAvailability(1L));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test delete project successfully")
    void deleteProductSuccessfully() {
        Product product = Product.builder().id(1L).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    @DisplayName("Test delete project does not exist throws not found exception")
    void delete_ProductDoesNotExist_ThrowsNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        verify(productRepository, times(0)).delete(any(Product.class));
        assertThrows(NotFoundException.class, () -> productService.delete(1L));
    }
}
