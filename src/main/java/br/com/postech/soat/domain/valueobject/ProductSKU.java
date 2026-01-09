package br.com.postech.soat.domain.valueobject;


import br.com.postech.soat.domain.exception.InvalidProductSKUException;

public record ProductSKU(String value) {
    public ProductSKU {
        validate(value);
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidProductSKUException("SKU não pode ser nulo");
        }

        if (value.length() > 16) {
            throw new InvalidProductSKUException("SKU não pode conter mais de 16 caracteres");
        }
    }
}