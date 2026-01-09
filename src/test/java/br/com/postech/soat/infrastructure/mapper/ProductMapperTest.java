package br.com.postech.soat.infrastructure.mapper;

import br.com.postech.soat.domain.entity.Product;
import br.com.postech.soat.domain.enumtypes.Category;
import br.com.postech.soat.infrastructure.persistence.entities.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void toDomain_whenEntityIsValid_shouldMapToDomain() {
        UUID id = UUID.randomUUID();
        ProductEntity entity = new ProductEntity();
        entity.setId(id);
        entity.setSku("SKU123");
        entity.setName("Test Name");
        entity.setPrice(new BigDecimal("10.50"));
        entity.setDescription("Test Desc");
        entity.setImage("http://test.com/img.png");
        entity.setCategory(Category.SNACK);
        entity.setActive(true);

        Product domain = ProductMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId().getValue());
        assertEquals("SKU123", domain.getSku().value());
        assertEquals("Test Name", domain.getName().value());
        assertEquals(new BigDecimal("10.50"), domain.getPrice().value());
        assertEquals("Test Desc", domain.getDescription().value());
        assertEquals("http://test.com/img.png", domain.getImage().value());
        assertEquals(Category.SNACK, domain.getCategory().category());
        assertTrue(domain.getActive());
    }

    @Test
    void toDomain_whenEntityIsNull_shouldReturnNull() {
        assertNull(ProductMapper.toDomain(null));
    }

    @Test
    void toEntity_whenDomainIsValid_shouldMapToEntity() {
        Product domain = Product.builder()
            .productId(UUID.randomUUID())
            .sku("SKU123")
            .name("Test Name")
            .price(new BigDecimal("10.50"))
            .description("Test Desc")
            .image("http://test.com/img.png")
            .category(Category.SNACK)
            .active(true)
            .build();

        ProductEntity entity = ProductMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId().getValue(), entity.getId());
        assertEquals(domain.getSku().value(), entity.getSku());
        assertEquals(domain.getName().value(), entity.getName());
        assertEquals(domain.getPrice().value(), entity.getPrice());
        assertEquals(domain.getDescription().value(), entity.getDescription());
        assertEquals(domain.getImage().value(), entity.getImage());
        assertEquals(domain.getCategory().category(), entity.getCategory());
        assertEquals(domain.getActive(), entity.getActive());
    }

    @Test
    void toEntity_whenDomainIsNull_shouldReturnNull() {
        assertNull(ProductMapper.toEntity(null));
    }
}