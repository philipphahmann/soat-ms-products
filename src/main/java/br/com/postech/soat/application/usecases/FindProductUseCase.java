package br.com.postech.soat.application.usecases;

import br.com.postech.soat.commons.infrastructure.aop.monitorable.Monitorable;
import br.com.postech.soat.application.adapters.LoggerPort;
import br.com.postech.soat.application.dto.FindProductQuery;
import br.com.postech.soat.application.repositories.ProductRepository;
import br.com.postech.soat.domain.entity.Product;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
@Monitorable
public class FindProductUseCase{
    private final ProductRepository productRepository;
    private final LoggerPort logger;

    public FindProductUseCase(ProductRepository productRepository, LoggerPort logger) {
        this.productRepository = productRepository;
        this.logger = logger;
    }

    public List<Product> execute(FindProductQuery request) {
        logger.info("Starting to find products");
        return productRepository.findAll(request);
    }
}
