package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.exception.InvalidProductNameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProductNameTest {

    @Test
    void constructor_whenNameIsValid_shouldCreateInstance() {
        String validName = "X-Burger";

        ProductName productName = assertDoesNotThrow(() -> new ProductName(validName));

        assertNotNull(productName);
        assertEquals(validName, productName.value());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   "})
    void constructor_whenNameIsInvalid_shouldThrowInvalidProductNameException(String invalidName) {
        Exception exception = assertThrows(InvalidProductNameException.class, () -> {
            new ProductName(invalidName);
        });

        assertEquals("O nome do produto não pode ser nulo", exception.getMessage());
    }
}