package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.exception.InvalidProductCategoryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductCategoryTest {

    @Test
    void constructor_whenSkuIsInvalid_shouldThrowInvalidProductCategoryException() {
        assertThrows(InvalidProductCategoryException.class, () -> {
            new ProductCategory("WRONG_CATEGORY");
        });
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   "})
    void constructor_whenSkuIsNull_shouldThrowInvalidProductCategoryException(String invalidCategory) {
        assertThrows(InvalidProductCategoryException.class, () -> {
            new ProductCategory(invalidCategory);
        });
    }
}
