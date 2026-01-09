package br.com.postech.soat.infrastructure.http;

import br.com.postech.soat.openapi.model.PostProducts201ResponseDto;
import br.com.postech.soat.openapi.model.PostProductsRequestDto;
import br.com.postech.soat.openapi.model.ProductDto;
import br.com.postech.soat.openapi.model.ProductCategoryDto;
import br.com.postech.soat.openapi.model.PutProductsRequestDto;
import br.com.postech.soat.infrastructure.LoggerAdapter;
import br.com.postech.soat.application.repositories.ProductRepository;
import br.com.postech.soat.application.dto.CreateProductDto;
import br.com.postech.soat.application.dto.FindProductQuery;
import br.com.postech.soat.application.dto.UpdateProductDto;
import br.com.postech.soat.domain.enumtypes.Category;
import br.com.postech.soat.domain.entity.Product;
import br.com.postech.soat.commons.infrastructure.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductWebMapper productWebMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private LoggerAdapter loggerAdapter;

    private ProductController productController;

    private Product product;
    private ProductDto responseDto;
    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // O Controller é instanciado com os mocks
        productController = new ProductController(productRepository, productWebMapper);

        productId = UUID.randomUUID();
        product = Product.builder()
                .productId(productId)
                .sku("Test SKU")
                .name("Test Product")
                .description("This is a test product.")
                .image("http://example.com/image.jpg")
                .price(BigDecimal.valueOf(19.99))
                .category(Category.DRINK)
                .active(true)
                .build();

        responseDto = new ProductDto()
                .id(product.getId().getValue())
                .name(product.getName().value())
                .sku(product.getSku().value())
                .description(product.getDescription().value())
                .price(product.getPrice().value().doubleValue())
                .active(product.getActive())
                .image(product.getImage().value())
                .category(ProductCategoryDto.fromValue(product.getCategory().value()));

        // --- Mocks Lenientes (para todos os testes) ---

        // Mocks para GET /products (lista)
        lenient().when(productRepository.findAll(any(FindProductQuery.class))).thenReturn(List.of(product));
        lenient().when(productWebMapper.toListResponse(List.of(product))).thenReturn(List.of(responseDto));

        // Mocks para GET /products/{id}, PUT, e DELETE
        lenient().when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        lenient().when(productRepository.findById(not(eq(productId)))).thenReturn(Optional.empty());

        // Mock para 'save' (usado por POST, PUT, DELETE)
        lenient().when(productRepository.save(any(Product.class))).thenReturn(product);

        // Mocks para mappers (POST, PUT, GET /id)
        lenient().when(productWebMapper.toResponse(any(Product.class))).thenReturn(responseDto);
        lenient().when(productWebMapper.toCreateRequest(any(PostProductsRequestDto.class))).thenReturn(mock(CreateProductDto.class));
        lenient().when(productWebMapper.toUpdateRequest(any(PutProductsRequestDto.class))).thenReturn(mock(UpdateProductDto.class));
        lenient().when(productWebMapper.toCreateResponse(any(Product.class))).thenReturn(new PostProducts201ResponseDto().id(productId));
    }

    /**
        Testes GET /products (Listar/Filtrar)
    **/
    @Test
    void getProduct_whenCategoryAndSkuProvided_shouldReturnOk() {
        String sku = "Test SKU";
        ProductCategoryDto categoryDto = ProductCategoryDto.fromValue("DRINK");
        ResponseEntity<List<ProductDto>> response = productController.getProduct(sku, categoryDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getProduct_whenOnlyCategoryProvided_shouldReturnOk() {
        ProductCategoryDto categoryDto = ProductCategoryDto.fromValue("DRINK");
        ResponseEntity<List<ProductDto>> response = productController.getProduct(null, categoryDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getProduct_whenOnlySkuProvided_shouldReturnOk() {
        String sku = "Test SKU";
        ResponseEntity<List<ProductDto>> response = productController.getProduct(sku, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getProduct_whenNoFilterProvided_shouldReturnOk() {
        ResponseEntity<List<ProductDto>> response = productController.getProduct(null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getProduct_whenNoProductsFound_shouldReturnOkWithEmptyList() {
        when(productRepository.findAll(any(FindProductQuery.class))).thenReturn(Collections.emptyList());
        when(productWebMapper.toListResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProductDto>> response = productController.getProduct("SKU", ProductCategoryDto.fromValue("DRINK"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    /**
        Testes o GET /products/{id}
    **/
    @Test
    void getProductById_whenProductExists_shouldReturnOk() {
        ResponseEntity<ProductDto> response = productController.getProductById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        verify(productRepository, times(1)).findById(productId);
        verify(productWebMapper, times(1)).toResponse(product);
    }

    @Test
    void getProductById_whenProductNotFound_shouldThrowNotFoundException() {
        UUID notFoundId = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> {
            productController.getProductById(notFoundId);
        });

        verify(productRepository, times(1)).findById(notFoundId);
        verify(productWebMapper, never()).toResponse(any());
    }

    /**
        Testes POST /products
    **/
    @Test
    void postProducts_whenValidRequest_shouldReturnCreated() {
        PostProductsRequestDto requestDto = new PostProductsRequestDto()
            .sku("NEW-SKU")
            .name("NEW-PROD")
            .price(10.0)
            .category(ProductCategoryDto.SNACK)
            .description("Desc")
            .image("http://example.com/img.jpg");

        CreateProductDto createDto = new CreateProductDto(
            "NEW-SKU",
            "NEW-PROD",
            "Desc",
            BigDecimal.valueOf(10.0),
            "http://example.com/img.jpg",
            "SNACK"
        );

        when(productWebMapper.toCreateRequest(requestDto)).thenReturn(createDto);
        when(productRepository.existsBySku("NEW-SKU")).thenReturn(false);

        ResponseEntity<PostProducts201ResponseDto> response = productController.postProducts(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());

        verify(productWebMapper, times(1)).toCreateRequest(requestDto);
        verify(productRepository, times(1)).existsBySku("NEW-SKU");
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productWebMapper, times(1)).toCreateResponse(product);
    }

    @Test
    void postProducts_whenSkuAlreadyExists_shouldThrowResourceConflictException() {
        String existingSku = "SKU-EXISTENTE";

        PostProductsRequestDto requestDto = new PostProductsRequestDto()
            .sku(existingSku)
            .name("Produto Duplicado")
            .price(10.0)
            .category(ProductCategoryDto.SNACK)
            .description("Descrição")
            .image("http://example.com/imagem.jpg");

        CreateProductDto createDto = new CreateProductDto(
            existingSku,
            "Produto Duplicado",
            "Descrição",
            BigDecimal.valueOf(10.0),
            "http://example.com/imagem.jpg",
            "SNACK"
        );

        when(productWebMapper.toCreateRequest(requestDto)).thenReturn(createDto);
        when(productRepository.existsBySku(existingSku)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> {
            productController.postProducts(requestDto);
        });

        verify(productRepository, times(1)).existsBySku(existingSku);
        verify(productRepository, never()).save(any(Product.class));
    }

    /**
        Testes PUT /products/{id}
    **/
    @Test
    void putProducts_whenProductExists_shouldReturnOk() {
        PutProductsRequestDto requestDto = new PutProductsRequestDto()
            .name("Produto Atualizado")
            .price(10.0)
            .description("Descrição")
            .image("http://example.com/imagem.jpg")
            .category(ProductCategoryDto.SNACK);

        UpdateProductDto updateDto = new UpdateProductDto(
            "Produto Atualizado",
            BigDecimal.valueOf(10.0),
            "Descrição",
            "http://example.com/imagem.jpg",
            "SNACK"
        );

        when(productWebMapper.toUpdateRequest(requestDto)).thenReturn(updateDto);

        ResponseEntity<ProductDto> response = productController.putProducts(productId, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());

        verify(productRepository, times(1)).findById(productId);
        verify(productWebMapper, times(1)).toUpdateRequest(requestDto);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productWebMapper, times(1)).toResponse(product);
    }

    @Test
    void putProducts_whenProductNotFound_shouldThrowNotFoundException() {
        UUID notFoundId = UUID.randomUUID();
        PutProductsRequestDto requestDto = new PutProductsRequestDto();

        assertThrows(NotFoundException.class, () -> {
            productController.putProducts(notFoundId, requestDto);
        });

        verify(productRepository, times(1)).findById(notFoundId);
        verify(productRepository, never()).save(any());
        verify(productWebMapper, never()).toResponse(any());
    }

    /**
        Testes DELETE /products/{id}
    **/
    @Test
    void deleteProducts_whenProductExists_shouldReturnNoContent() {
        ResponseEntity<Void> response = productController.deleteProducts(productId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProducts_whenProductNotFound_shouldThrowNotFoundException() {
        UUID notFoundId = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> {
            productController.deleteProducts(notFoundId);
        });

        verify(productRepository, times(1)).findById(notFoundId);
        verify(productRepository, never()).save(any());
    }
}