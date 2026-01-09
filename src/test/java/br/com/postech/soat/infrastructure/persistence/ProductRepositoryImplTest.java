package br.com.postech.soat.infrastructure.persistence;

import br.com.postech.soat.application.dto.FindProductQuery;
import br.com.postech.soat.application.repositories.ProductRepository;
import br.com.postech.soat.domain.entity.Product;
import br.com.postech.soat.domain.enumtypes.Category;
import br.com.postech.soat.infrastructure.persistence.entities.ProductEntity;
import br.com.postech.soat.infrastructure.persistence.entities.repositories.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class ProductRepositoryImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.flyway.url", postgresContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgresContainer::getUsername);
        registry.add("spring.flyway.password", postgresContainer::getPassword);
    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductJpaRepository jpaRepository;

    @BeforeEach
    void setUp() {
        jpaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar um Product (Domínio) e buscá-lo por ID")
    void testSaveAndFindById() {
        Product domain = Product.builder()
            .productId(UUID.randomUUID())
            .sku("SKU123")
            .name("Test Name")
            .price(new BigDecimal("10.99"))
            .description("Test Desc")
            .image("http://test.com/img.png")
            .category(Category.SNACK)
            .active(true)
            .build();

        productRepository.save(domain);

        Optional<Product> found = productRepository.findById(domain.getId().getValue());

        assertTrue(found.isPresent());
        assertEquals("SKU123", found.get().getSku().value());
        assertEquals("Test Name", found.get().getName().value());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para ID não existente")
    void testFindById_NotFound() {
        Optional<Product> found = productRepository.findById(UUID.randomUUID());

        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve checar corretamente se um SKU existe")
    void testExistsBySku() {
        ProductEntity entity = new ProductEntity(
            UUID.randomUUID(),
            "SKU-EXISTS",
            true, "Test",
            BigDecimal.TEN,
            "Desc",
            "http://example.com/image.png",
            Category.DRINK
        );
        jpaRepository.save(entity);

        assertTrue(productRepository.existsBySku("SKU-EXISTS"));
        assertFalse(productRepository.existsBySku("SKU-NOT-EXISTS"));
    }

    @Test
    @DisplayName("Deve filtrar produtos por SKU e Categoria (testando 'fromDomain' e 'hasSku', 'hasCategory')")
    void testFindAll_WithSkuAndCategoryFilters() {
        ProductEntity snack = new ProductEntity(
                UUID.randomUUID(),
                "SKU-SNACK",
                true,
                "Snack",
                BigDecimal.TEN,
                "Desc",
                "http://example.com/image.png",
                Category.SNACK
        );

        ProductEntity drink = new ProductEntity(
                UUID.randomUUID(),
                "SKU-DRINK",
                true,
                "Drink",
                BigDecimal.TEN,
                "Desc",
                "http://example.com/image.png",
                Category.DRINK
        );

        jpaRepository.saveAll(List.of(snack, drink));

        FindProductQuery query = FindProductQuery.from("SKU-SNACK", "SNACK");
        List<Product> result = productRepository.findAll(query);

        // Assert (Testa o 'if' do 'hasSku' e 'hasCategory')
        assertEquals(1, result.size());
        assertEquals("SKU-SNACK", result.get(0).getSku().value());
    }

    @Test
    @DisplayName("Deve retornar apenas produtos ativos (testando 'fromDomain', 'isActive' e 'else' dos filtros)")
    void testFindAll_NoFilters_ShouldReturnOnlyActive() {
        ProductEntity snack = new ProductEntity(
                UUID.randomUUID(), "SKU-SNACK",
                true,
                "Snack",
                BigDecimal.TEN,
                "Desc",
                "http://example.com/image.png",
                Category.SNACK
        );

        ProductEntity drink = new ProductEntity(
                UUID.randomUUID(),
                "SKU-DRINK",
                true,
                "Drink",
                BigDecimal.TEN,
                "Desc",
                "http://example.com/image.png",
                Category.DRINK
        );

        ProductEntity inactive = new ProductEntity(
                UUID.randomUUID(),
                "SKU-INACTIVE",
                false,
                "Old Drink",
                BigDecimal.TEN,
                "Desc",
                "http://example.com/image.png",
                Category.DRINK
        );

        jpaRepository.saveAll(List.of(snack, drink, inactive));

        FindProductQuery query = FindProductQuery.from(null, null);
        List<Product> result = productRepository.findAll(query);

        // Assert (Testa o 'else' do 'hasSku' e 'hasCategory' e o 'isActive')
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getActive()));
    }
}