package br.com.postech.soat.application.repositories;

import br.com.postech.soat.application.dto.FindProductQuery;
import br.com.postech.soat.domain.entity.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll(FindProductQuery request);

    Product save(Product product);

    Optional<Product> findById(UUID id);

    boolean existsBySku(String sku);
}
