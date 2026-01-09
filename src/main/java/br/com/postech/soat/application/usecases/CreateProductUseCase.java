package br.com.postech.soat.application.usecases;

import br.com.postech.soat.commons.infrastructure.aop.monitorable.Monitorable;
import br.com.postech.soat.commons.infrastructure.exception.ResourceConflictException;
import br.com.postech.soat.application.adapters.LoggerPort;
import br.com.postech.soat.application.dto.CreateProductDto;
import br.com.postech.soat.application.repositories.ProductRepository;
import br.com.postech.soat.domain.entity.Product;
import br.com.postech.soat.domain.valueobject.ProductCategory;
import br.com.postech.soat.domain.valueobject.ProductDescription;
import br.com.postech.soat.domain.valueobject.ProductImage;
import br.com.postech.soat.domain.valueobject.ProductName;
import br.com.postech.soat.domain.valueobject.ProductPrice;
import br.com.postech.soat.domain.valueobject.ProductSKU;
import org.springframework.stereotype.Service;

@Service
@Monitorable
public class CreateProductUseCase {
    private final ProductRepository productRepository;
    private final LoggerPort logger;

    public CreateProductUseCase(ProductRepository productRepository, LoggerPort logger) {
        this.productRepository = productRepository;
        this.logger = logger;
    }

    public Product execute(CreateProductDto request){
        ProductSKU sku = new ProductSKU(request.sku());
        ProductName name = new ProductName(request.name());
        ProductPrice price = new ProductPrice(request.price());
        ProductDescription description = new ProductDescription(request.description());
        ProductImage image = new ProductImage(request.image());
        ProductCategory category = new ProductCategory(request.category());

        validateSkuDoesNotExist(sku, productRepository);

        final Product product = Product.create(sku, name, price,
            description, image, category);

        logger.info("Domain product created: " + product.getSku());

        return productRepository.save(product);
    }

    private void validateSkuDoesNotExist(ProductSKU sku, ProductRepository productRepository){
        boolean exists = productRepository.existsBySku(sku.value());
        if (exists) {
            throw new ResourceConflictException(
                "SKU: " + sku.value() + " já existe");
            }
    }
}
