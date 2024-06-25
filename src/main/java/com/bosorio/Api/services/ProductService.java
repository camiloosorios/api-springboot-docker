package com.bosorio.Api.services;


import com.bosorio.Api.dto.ProductDto;

import java.util.List;

public interface ProductService {

    void create(ProductDto productDto);

    List<ProductDto> getAll();

    ProductDto getById(Long id);

    void update(ProductDto productDto, Long id);

    void updateAvailability(Long id);

    void delete(Long id);

}
