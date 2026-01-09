package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.exception.InvalidProductSKUException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProductSKUTest {

    @Test
    void constructor_whenSkuIsValid_shouldCreateInstance() {
        String validSku = "SKU-VALIDO-123";

        ProductSKU productSKU = assertDoesNotThrow(() -> new ProductSKU(validSku));

        assertNotNull(productSKU);
        assertEquals(validSku, productSKU.value());
    }

    @Test
    void constructor_whenSkuWith16Chars_shouldCreateInstance() {
        String validSku = "1234567890ABCDEF";

        ProductSKU productSKU = assertDoesNotThrow(() -> new ProductSKU(validSku));

        assertNotNull(productSKU);
        assertEquals(validSku, productSKU.value());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   "})
    void constructor_whenSkuNullOrIsBlank_shouldThrowInvalidProductSKUException(String invalidSku) {
        Exception exception = assertThrows(InvalidProductSKUException.class, () -> {
            new ProductSKU(invalidSku);
        });

        assertEquals("SKU não pode ser nulo", exception.getMessage());
    }

    @Test
    void constructor_whenSkuExceedCharLimit_shouldThrowInvalidProductSKUException() {
        String longSku = "1234567890ABCDEFG";

        Exception exception = assertThrows(InvalidProductSKUException.class, () -> {
            new ProductSKU(longSku);
        });

        assertEquals("SKU não pode conter mais de 16 caracteres", exception.getMessage());
    }
}