package br.com.postech.soat.infrastructure.http;

import br.com.postech.soat.openapi.api.ProductApi;
import br.com.postech.soat.openapi.model.ProductCategoryDto;
import br.com.postech.soat.openapi.model.PostProducts201ResponseDto;
import br.com.postech.soat.openapi.model.PostProductsRequestDto;
import br.com.postech.soat.openapi.model.ProductDto;
import br.com.postech.soat.openapi.model.PutProductsRequestDto;
import br.com.postech.soat.application.adapters.LoggerPort;
import br.com.postech.soat.application.dto.CreateProductDto;
import br.com.postech.soat.application.dto.FindProductQuery;
import br.com.postech.soat.application.dto.UpdateProductDto;
import br.com.postech.soat.application.repositories.ProductRepository;
import br.com.postech.soat.application.usecases.CreateProductUseCase;
import br.com.postech.soat.application.usecases.DeleteProductUseCase;
import br.com.postech.soat.application.usecases.FindProductUseCase;
import br.com.postech.soat.application.usecases.UpdateProductUseCase;
import br.com.postech.soat.application.usecases.FindProductByIdUseCase;
import br.com.postech.soat.domain.entity.Product;
import br.com.postech.soat.infrastructure.LoggerAdapter;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductApi {
    private final CreateProductUseCase createProductUseCase;
    private final FindProductUseCase findProductUseCase;
    private final FindProductByIdUseCase findProductByIdUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final ProductWebMapper productWebMapper;

    public ProductController(ProductRepository productRepository, ProductWebMapper productWebMapper) {
        LoggerPort logger = new LoggerAdapter();

        this.createProductUseCase = new CreateProductUseCase(productRepository, logger);
        this.findProductUseCase = new FindProductUseCase(productRepository, logger);
        this.findProductByIdUseCase = new FindProductByIdUseCase(productRepository, logger);
        this.updateProductUseCase = new UpdateProductUseCase(productRepository, logger);
        this.deleteProductUseCase = new DeleteProductUseCase(productRepository, logger);

        this.productWebMapper = productWebMapper;
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProduct(String sku, ProductCategoryDto categoryDto) {

        String categoryString = null;
        if (categoryDto != null) { categoryString = categoryDto.getValue(); }

        FindProductQuery request = FindProductQuery.from(sku, categoryString);
        final List<Product> result = findProductUseCase.execute(request);
        return ResponseEntity.ok(productWebMapper.toListResponse(result));
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") UUID productId) {
        Product product = findProductByIdUseCase.execute(productId);
        return ResponseEntity.ok(productWebMapper.toResponse(product));
    }

    @Override
    public ResponseEntity<PostProducts201ResponseDto> postProducts(PostProductsRequestDto productDto){
        CreateProductDto productRequest = productWebMapper.toCreateRequest(productDto);
        final Product product = createProductUseCase.execute(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productWebMapper.toCreateResponse(product));
    }

    @Override
    public ResponseEntity<ProductDto> putProducts(UUID uuid, PutProductsRequestDto putProductRequest) {
        UpdateProductDto productRequest = productWebMapper.toUpdateRequest(putProductRequest);
        final Product product = updateProductUseCase.execute(uuid, productRequest);
        return ResponseEntity.status(HttpStatus.OK)
            .body(productWebMapper.toResponse(product));
    }

    @Override
    public ResponseEntity<Void> deleteProducts(UUID productId) {
        deleteProductUseCase.execute(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
