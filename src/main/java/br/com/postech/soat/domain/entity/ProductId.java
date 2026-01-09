package br.com.postech.soat.domain.entity;

import java.util.Objects;
import java.util.UUID;

public class ProductId {
    private final UUID value;

    public ProductId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductId não pode ser nulo");
        }
        this.value = value;
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
    
    public static ProductId of(UUID value) {
        return new ProductId(value);
    }
    
    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(value, productId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}