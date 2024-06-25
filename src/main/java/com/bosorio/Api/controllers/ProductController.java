package com.bosorio.Api.controllers;

import com.bosorio.Api.Exceptions.BadRequestException;
import com.bosorio.Api.Exceptions.NotFoundException;
import com.bosorio.Api.dto.ProductDto;
import com.bosorio.Api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDto productDto) {
        try {
            productService.create(productDto);

            return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
        } catch (RuntimeException e) {
            return handleException(e);
        }
    }

    @GetMapping
    public List<ProductDto> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        try {
            ProductDto productDto = productService.getById(id);

            return ResponseEntity.status(HttpStatus.OK).body(productDto);
        } catch (RuntimeException e) {
            return handleException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody ProductDto productDto) {
        try {
            productService.update(productDto, id);

            return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
        } catch (RuntimeException e) {
            return handleException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAvailability(@PathVariable Long id) {
        try {
            productService.updateAvailability(id);

            return ResponseEntity.status(HttpStatus.OK).body("Product availability updated successfully");
        } catch (RuntimeException e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            productService.delete(id);

            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } catch (RuntimeException e) {
            return handleException(e);
        }
    }

    private ResponseEntity<?> handleException(RuntimeException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());

        if (e instanceof BadRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } else if (e instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
