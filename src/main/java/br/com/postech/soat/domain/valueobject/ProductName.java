package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.exception.InvalidProductNameException;

public record ProductName (String value) {
    public ProductName {
        validate(value);
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidProductNameException("O nome do produto não pode ser nulo");
        }
    }
}
