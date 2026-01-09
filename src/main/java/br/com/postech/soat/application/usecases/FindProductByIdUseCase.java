package br.com.postech.soat.application.usecases;

import br.com.postech.soat.application.adapters.LoggerPort;
import br.com.postech.soat.application.repositories.ProductRepository;
import br.com.postech.soat.commons.infrastructure.exception.NotFoundException;
import br.com.postech.soat.domain.entity.Product;
import java.util.UUID;

public class FindProductByIdUseCase {
    private final ProductRepository productRepository;
    private final LoggerPort logger;

    public FindProductByIdUseCase(ProductRepository productRepository, LoggerPort logger) {
        this.productRepository = productRepository;
        this.logger = logger;
    }

    public Product execute(UUID uuid) {
        logger.info("Finding product with ID: " + uuid);

        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + uuid));

        logger.info("Product found with ID: " + uuid);
        return product;
    }
}