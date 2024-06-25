package com.bosorio.Api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    private String name;

    private BigDecimal price;

    private Boolean availability;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
