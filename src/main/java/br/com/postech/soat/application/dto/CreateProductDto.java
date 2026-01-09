package br.com.postech.soat.application.dto;

import java.math.BigDecimal;

public record CreateProductDto(
    String sku,
    String name,
    String description,
    BigDecimal price,
    String image,
    String category
    ){
}