package br.com.postech.soat.infrastructure.persistence;

import br.com.postech.soat.commons.infrastructure.aop.monitorable.Monitorable;
import br.com.postech.soat.application.dto.FindProductQuery;
import br.com.postech.soat.application.repositories.ProductRepository;
import br.com.postech.soat.domain.entity.Product;
import br.com.postech.soat.infrastructure.mapper.ProductMapper;
import br.com.postech.soat.infrastructure.persistence.entities.ProductEntity;
import br.com.postech.soat.infrastructure.persistence.entities.repositories.ProductJpaRepository;
import br.com.postech.soat.infrastructure.persistence.specification.ProductSpecification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Monitorable
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;

    @Override
    public List<Product> findAll(FindProductQuery request) {

        Specification<ProductEntity> spec = ProductSpecification.fromDomain(request);
        List<ProductEntity> result = jpaRepository.findAll(spec);

        return result.stream().map(ProductMapper::toDomain).toList();
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = ProductMapper.toEntity(product);
        final ProductEntity saved = jpaRepository.save(entity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(ProductMapper::toDomain);
    }


    @Override
    public boolean existsBySku(String sku) {
        return jpaRepository.existsBySku(sku);
    }
}
