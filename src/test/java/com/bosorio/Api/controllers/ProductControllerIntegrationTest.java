package com.bosorio.Api.controllers;

import com.bosorio.Api.dto.ProductDto;
import com.bosorio.Api.repositories.ProductRepository;
import com.bosorio.Api.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/products";

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        ProductDto productDto = ProductDto.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(1500))
                .build();

        productService.create(productDto);
    }

    @Test
    @Order(1)
    @DisplayName("Test creating a new product")
    void testCreateProduct() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(1500))
                .build();

        String jsonProductDTO = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProductDTO))
                        .andExpect(status().isCreated())
                        .andExpect(content().string("Product created successfully"));
    }

    @Test
    @Order(2)
    @DisplayName("Test get all products")
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Test get product by Id")
    @Order(3)
    void testGetProductById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(1500));
    }

    @Test
    @Order(4)
    @DisplayName("Test update product")
    void testUpdateProduct() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name("Test Product updated")
                .price(BigDecimal.valueOf(1500))
                .build();

        String jsonProductDTO = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(put(BASE_URL + "/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProductDTO))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully"));

    }

    @Test
    @Order(5)
    @DisplayName("Test update product availability")
    void testUpdateProductAvailability() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/6"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product availability updated successfully"));
    }

    @Test
    @Order(6)
    @DisplayName("Test delete product")
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/7"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

}
