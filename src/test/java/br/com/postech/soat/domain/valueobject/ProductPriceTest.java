package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.exception.InvalidProductPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductPriceTest {

    @Test
    void constructor_whenPriceIsValid_shouldCreateInstance() {
        BigDecimal validPrice = new BigDecimal("25.99");

        ProductPrice productPrice = assertDoesNotThrow(() -> new ProductPrice(validPrice));

        assertNotNull(productPrice);
        assertEquals(validPrice, productPrice.value());
    }

    @Test
    void constructor_whenPriceIsNull_shouldThrowInvalidProductPriceException() {
        Exception exception = assertThrows(InvalidProductPriceException.class, () -> {
            new ProductPrice(null);
        });

        assertEquals("O valor do produto não pode ser nulo", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.0", "0", "-0.01", "-10.50"})
    void constructor_whenPriceIsInvalid_shouldThrowInvalidProductPriceException(String invalidPriceString) {
        BigDecimal invalidPrice = new BigDecimal(invalidPriceString);

        Exception exception = assertThrows(InvalidProductPriceException.class, () -> {
            new ProductPrice(invalidPrice);
        });

        assertEquals("O valor do produto não pode menor ou igual a que zero", exception.getMessage());
    }
}