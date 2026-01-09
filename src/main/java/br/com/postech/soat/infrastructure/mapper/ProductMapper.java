package br.com.postech.soat.infrastructure.mapper;

import br.com.postech.soat.domain.entity.Product;
import br.com.postech.soat.infrastructure.persistence.entities.ProductEntity;

public interface ProductMapper {

    static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return Product.builder()
            .productId(entity.getId())
            .sku(entity.getSku())
            .active(entity.getActive())
            .name(entity.getName())
            .price(entity.getPrice())
            .description(entity.getDescription())
            .image(entity.getImage())
            .category(entity.getCategory())
            .build();
    }

    static ProductEntity toEntity(Product domain) {
        if (domain == null) {
            return null;
        }
        ProductEntity entity = new ProductEntity();
        entity.setId(domain.getId().getValue());
        entity.setSku(domain.getSku().value());
        entity.setActive(domain.getActive());
        entity.setName(domain.getName().value());
        entity.setPrice(domain.getPrice().value());
        entity.setDescription(domain.getDescription().value());
        entity.setImage(domain.getImage().value());
        entity.setCategory(domain.getCategory().category());
        return entity;
    }
}
