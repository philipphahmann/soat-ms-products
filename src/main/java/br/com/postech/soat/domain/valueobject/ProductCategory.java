package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.enumtypes.Category;
import br.com.postech.soat.domain.exception.InvalidProductCategoryException;
import org.apache.commons.lang3.StringUtils;

public record ProductCategory(String value) {
    public ProductCategory{
        validate(value);
    }

    private void validate(String value) {
        if (StringUtils.isBlank(value)) {
            throw new InvalidProductCategoryException("SKU não pode ser nulo ou vazio.");
        }

        try {
            Category.entryOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidProductCategoryException("Valor inválido para a categoria: " + value);
        }
    }

    public Category category() {
        return Category.entryOf(value);
    }
}
